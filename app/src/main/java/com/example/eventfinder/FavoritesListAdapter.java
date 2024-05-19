package com.example.eventfinder;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.widget.Toast;

public class FavoritesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<EventListCard> mData;
    //    private String search_url;
    public FavoritesListAdapter(Context mContext, List<EventListCard> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    public static final String mypreference = "mypref";
    public SharedPreferences sharedpreferences;
    public JSONArray favoritesList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.event_list_card, parent, false);
        sharedpreferences = mContext.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains("favorites")) {
            Log.d("shared pref", (sharedpreferences.getString("favorites", "")));
            try {
                favoritesList = new JSONArray(sharedpreferences.getString("favorites", "[]"));
            }catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            for (int i = 0; i < favoritesList.length(); i++) {
                try{
                    Log.d("split", favoritesList.getJSONObject(i).toString());
                }catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        else{
            try {
                favoritesList = new JSONArray("[]");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("favorites", "[]");
            editor.apply();
        }
        return new EventHolder(itemView);
    }
    public void setResults(List<EventListCard> results ){ //, RecyclerViewClickListener listener) {
        //this.results.clear();
        this.mData = results;
        notifyDataSetChanged();

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EventHolder eventHolder = (EventHolder) holder;
        eventHolder.name.setText(mData.get(position).getName());
        eventHolder.name.setSelected(true);
        eventHolder.venue.setText(mData.get(position).getVenue());
        eventHolder.venue.setSelected(true);
        Glide.with(this.mContext).load(mData.get(position).getImageUrl()).placeholder(R.drawable.logo).into(eventHolder.event_image);
        eventHolder.date.setText(mData.get(position).getDate());
        eventHolder.time.setText(mData.get(position).getTime());
        eventHolder.genre.setText(mData.get(position).getGenre());
//        eventHolder.favorite.setText(mData.get(position).getFavoriteString());

        for (int i = 0; i < favoritesList.length(); ++i) {
            try {
                if (favoritesList.getJSONObject(i).getString("eventId").equals(mData.get(position).getEventId())) {
                    mData.get(position).setFavorite(true);
                    eventHolder.favorite_icon.setImageResource(R.drawable.ic_favorite_fill);
                }
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }

        eventHolder.favorite_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mData.get(position).getFavorite()){
                    eventHolder.favorite_icon.setImageResource(R.drawable.ic_favorite_fill);
                    mData.get(position).setFavorite(true);
                    JSONObject favEventData = new JSONObject();
                    try{
                        favEventData.put("eventId", mData.get(position).getEventId());
                        favEventData.put("name", mData.get(position).getName());
                        favEventData.put("venue", mData.get(position).getVenue());
                        favEventData.put("imageUrl", mData.get(position).getImageUrl());
                        favEventData.put("date", mData.get(position).getDate());
                        favEventData.put("time", mData.get(position).getTime());
                        favEventData.put("genre", mData.get(position).getGenre());
                        favEventData.put("isFavorite", true);

                        sharedpreferences = mContext.getSharedPreferences(mypreference,
                                Context.MODE_PRIVATE);
                        try {
                            favoritesList = new JSONArray(sharedpreferences.getString("favorites", "[]"));
                        }catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        favoritesList.put(favEventData);
                        Log.d("favoritesList", favoritesList.toString());
                        SharedPreferences.Editor editor = sharedpreferences.edit();
//                        Gson gson = new Gson();
//                        String StrJson=gson.toJson(favoritesList);
                        editor.putString("favorites", favoritesList.toString());
                        editor.apply();
                        notifyItemInserted(position);
                        notifyDataSetChanged();
                        Toast.makeText(mContext, mData.get(position).getName()+" added to favorites", Toast.LENGTH_SHORT).show();


                    }catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }

                }
                else{
                    eventHolder.favorite_icon.setImageResource(R.drawable.ic_favorite);
                    mData.get(position).setFavorite(false);

                    sharedpreferences = mContext.getSharedPreferences(mypreference,
                            Context.MODE_PRIVATE);
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
                        if(eventId.equals(mData.get(position).getEventId())){
                            favoritesList.remove(j);
                            Log.d("remove favorite", mData.get(position).getName());
                            Log.d("favoritesList", favoritesList.toString());
                            break;
                        }
                    }
                    // save
                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    Gson gson = new Gson();
//                    String StrJson=gson.toJson(favoritesList);
                    editor.putString("favorites", favoritesList.toString());
                    editor.apply();
                    Toast.makeText(mContext, mData.get(position).getName()+" removed from favorites", Toast.LENGTH_SHORT).show();
                    mData.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    System.out.println("Notify data set changed");
//                    if(mData.isEmpty()){
//                        System.out.println(v.p);
////                        v.findViewById(R.id.no_favorites).setVisibility(View.VISIBLE);
//                    }
                }

            }
        });

        eventHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("card view", "onClick: card view");
                String eventDetailsUrl= "https://event-app-8.wl.r.appspot.com/eventdetails?id="+mData.get(position).getEventId();
                Intent intent = new Intent(mContext, EventDetails.class);
                intent.putExtra("eventDetailsUrl",eventDetailsUrl);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView venue;
        public TextView date;
        public TextView time;
        public TextView genre;
        public ImageView favorite_icon;
        public ImageView event_image;
        public CardView cardView;

        public EventHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.event_card);
            name = itemView.findViewById(R.id.event_name);
            venue = itemView.findViewById(R.id.event_venue);
            date = itemView.findViewById(R.id.event_date);
            time = itemView.findViewById(R.id.event_time);
            event_image = itemView.findViewById(R.id.event_image);
            genre = itemView.findViewById(R.id.event_genre);
            favorite_icon = itemView.findViewById(R.id.favorite_icon);
            if (genre == null){
                Log.d("category", "null category");
            }
        }

    }

}

