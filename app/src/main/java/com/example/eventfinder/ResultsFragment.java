package com.example.eventfinder;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RequestQueue requestQueue;
    private List<EventListCard> eventList= new ArrayList<>();
    private String searchUrl,keyword,distance,location,lat,lng;
    private int category;
    private Boolean autoDetect;
    private Boolean noLocationResults;
    private Context context;
    private TextView noResults;
    private RecyclerView searchResults;
    private ImageView button_go_back_main;
    private EventListAdapter myAdapter;
    private ProgressBar progressLoader;

    public ResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultsFragment newInstance(String param1, String param2) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAdapter = new EventListAdapter(getContext(),new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_results, container, false);
        Bundle bundle=getArguments();
        searchUrl=bundle.getString("eventSearchUrl");
        noLocationResults=bundle.getBoolean("noLocResults");
        keyword=bundle.getString("keyword");
        distance=bundle.getString("distance");
        lat=bundle.getString("lat");
        lng=bundle.getString("lng");
        category=bundle.getInt("category");
        autoDetect=bundle.getBoolean("autoDetect");
        location=bundle.getString("location");
        getEventSearchResults(view);
        button_go_back_main = view.findViewById(R.id.back_result);
        progressLoader=view.findViewById(R.id.progress_loader);
        button_go_back_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("keyword",keyword);
                bundle.putString("distance",distance);
                bundle.putInt("category",category);
                bundle.putBoolean("autoDetect",autoDetect);
                bundle.putString("lat",lat);
                bundle.putString("lng",lng);
                bundle.putString("location",location);
                Navigation.findNavController(view).navigate(R.id.action_resultsFragment_to_searchFragment,bundle);
//                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_resultsFragment,bundle);
//                Fragment fragment = new BlankFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });
        return view;
    }

    private boolean getEventSearchResults(View view) {
        noResults=view.findViewById(R.id.no_results);
        searchResults=view.findViewById(R.id.search_results);
        Log.d("search_url", "url is "+ searchUrl);
        requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(searchUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(!response.has("_embedded") || noLocationResults){
                                noResults.setVisibility(View.VISIBLE);
                                searchResults.setVisibility(View.GONE);
                                progressLoader.setVisibility(View.INVISIBLE);
                            }
                            else{
                                noResults.setVisibility(View.GONE);
                                JSONObject embedded = response.getJSONObject("_embedded");
                                JSONArray events = embedded.getJSONArray("events");
                                JSONObject currentEvent = null;
                                eventList.clear();
                                for (int j = 0; j < events.length(); j++) {
                                    currentEvent = events.getJSONObject(j);
                                    String name = currentEvent.getString("name");
                                    String eventId = currentEvent.getString("id");
                                    String imageUrl = currentEvent.getJSONArray("images").getJSONObject(0).getString("url");
                                    String venue = currentEvent.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                                    DateFormat date1GivenFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    DateFormat date1Format = new SimpleDateFormat("MM/dd/yyyy");
                                    String date = date1Format.format(date1GivenFormat.parse(currentEvent.getJSONObject("dates").getJSONObject("start").getString("localDate")));
                                    DateFormat dateGivenFormat = new SimpleDateFormat("hh:mm:ss");
                                    DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                                    String time = dateFormat.format(dateGivenFormat.parse(currentEvent.getJSONObject("dates").getJSONObject("start").getString("localTime")));
                                    String genre = currentEvent.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");

                                    Log.d("name", name);
                                    Log.d("venue", venue);
                                    Log.d("date", date);
                                    Log.d("category", genre);
                                    Log.d("time", time);

                                    System.out.println("------eventId:"+eventId);
                                    EventListCard eventCard = new EventListCard(eventId,name, venue, date, genre,imageUrl, time, false);
                                    System.out.println("Size "+eventList.size());
                                    eventList.add(eventCard);

                                }
                                progressLoader.setVisibility(View.INVISIBLE);
                                searchResults.setVisibility(View.VISIBLE);
                                myAdapter.setResults(eventList);
                                searchResults.setLayoutManager(new GridLayoutManager(getContext(),1));
                                searchResults.setAdapter(myAdapter);
                            }

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        requestQueue.add(jsonObjectRequest);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        getEventSearchResults(getView());
    }
}