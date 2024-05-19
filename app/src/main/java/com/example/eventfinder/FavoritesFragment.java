package com.example.eventfinder;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {

    ArrayList<EventListCard> dataList = new ArrayList<>();

    private RequestQueue requestQueue;
    private List<EventListCard> eventList;
    public static final String mypreference = "mypref";
    public SharedPreferences sharedpreferences;
    public JSONArray favoritesList;

    private RecyclerView favResults;
    private ProgressBar progressLoader;

    private TextView noFavorites;
    private FavoritesListAdapter myAdapterFavorite;

    private Paint mClearPaint;
    private ColorDrawable mBackground;
    private int backgroundColor;
    private Drawable deleteDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;
    Context mContext;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAdapterFavorite = new FavoritesListAdapter(getContext(), eventList);
        mContext = getContext();
        mBackground = new ColorDrawable();
        backgroundColor = Color.parseColor("#FF0000");
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        deleteDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_delete);
        intrinsicWidth = deleteDrawable.getIntrinsicWidth();
        intrinsicHeight = deleteDrawable.getIntrinsicHeight();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_favorites, container, false);
        progressLoader=view.findViewById(R.id.progress_loader);
        sharedpreferences = mContext.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        System.out.println("on create view");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getFavoriteEvents();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFavoriteEvents();
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                System.out.println("In onSwiped------------------------------>");
                sharedpreferences = mContext.getSharedPreferences(mypreference,
                        Context.MODE_PRIVATE);
                myAdapterFavorite.notifyItemRemoved(position);
                try {
                    favoritesList = new JSONArray(sharedpreferences.getString("favorites", "[]"));
                }catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
                for(int j = 0; j < favoritesList.length(); ++j){
                    JSONObject event;
                    String eventId;
                    try{
                        event = favoritesList.getJSONObject(j);
                        eventId = event.getString("eventId");
                    }catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    if(eventId.equals(eventList.get(position).getEventId())){
                        favoritesList.remove(j);
                        Log.d("remove favorite", eventList.get(position).getName());
                        Log.d("favoritesList", favoritesList.toString());
                        break;
                    }
                }
                Toast.makeText(mContext, eventList.get(position).getName()+" removed from favorites", Toast.LENGTH_SHORT).show();
                eventList.remove(position);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("favorites", favoritesList.toString());
                editor.apply();

                if(eventList.isEmpty()){
                    favResults.setVisibility(View.GONE);
                    noFavorites.setVisibility(View.VISIBLE);
                }
                else{
                    favResults.setVisibility(View.VISIBLE);
                    noFavorites.setVisibility(View.GONE);
                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                int itemHeight = itemView.getHeight();

                boolean isCancelled = dX == 0 && !isCurrentlyActive;


                mBackground.setColor(backgroundColor);
                mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                mBackground.draw(c);

                int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
                int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
                int deleteIconRight = itemView.getRight() - deleteIconMargin;
                int deleteIconBottom = deleteIconTop + intrinsicHeight;


                deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                deleteDrawable.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(favResults);
    }
    public void getFavoriteEvents(){
        System.out.println("in get fav ");
        View view=getView();
        noFavorites=view.findViewById(R.id.no_favorites);
        favResults = view.findViewById(R.id.favorites_results);
        String temp = sharedpreferences.getString("favorites", null);
        try {
            JSONArray favoritesArray=new JSONArray(temp);
            System.out.println(favoritesArray.length());
            if(favoritesArray.length()==0) {
                favResults.setVisibility(View.GONE);
                progressLoader.setVisibility(View.INVISIBLE);
                noFavorites.setVisibility(View.VISIBLE);
            }else{
                noFavorites.setVisibility(View.GONE);
                progressLoader.setVisibility(View.INVISIBLE);
                favResults.setVisibility(View.VISIBLE);
                eventList = new ArrayList<>();
                for (int i = 0; i < favoritesArray.length(); i++) {
                    try{
                        JSONObject currentObj = favoritesArray.getJSONObject(i);
                        EventListCard eventCard = new EventListCard(currentObj.getString("eventId"),currentObj.getString("name"), currentObj.getString("venue"), currentObj.getString("date"), currentObj.getString("genre"),currentObj.getString("imageUrl"), currentObj.getString("time"), true);
                        eventList.add(eventCard);
                    }catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        myAdapterFavorite.setResults(eventList);
        favResults.setLayoutManager(new GridLayoutManager(getContext(),1));
        favResults.setAdapter(myAdapterFavorite);
    }
}
