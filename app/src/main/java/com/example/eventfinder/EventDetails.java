package com.example.eventfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventDetails extends AppCompatActivity {
    ViewPager2 viewPager2;

    TabLayout tabView;
    private Boolean isEventFavorite;
//    LinearLayout artistLabel,venueLabel,dateLabel,timeLabel,ticketLabel,priceLabel,buyTicketLabel,genreLabel;
//    TextView artistValue,venueValue,dateValue,timeValue,priceValue,buyTicketValue,ticketValue,genreValue;

    private RequestQueue requestQueue;

    private Event eventData;

    private Venue venueData;
    private List<Artist> artistData;
    String eventDetailsUrl,eventName;

    private ImageView fbIcon,twitterIcon,favIcon;
    public static final String mypreference = "mypref";

    String artistUrl="https://event-app-8.wl.r.appspot.com/artistdetails?artist=";
    String venueUrl="https://event-app-8.wl.r.appspot.com/venuedetails?name=";
    TextView title;

    private ProgressBar progressLoader;
    public SharedPreferences sharedpreferences;
    public JSONArray favoritesList;
    private ImageButton backBtn;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar=findViewById(R.id.toolbar_event);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        eventDetailsUrl=getIntent().getStringExtra("eventDetailsUrl");
        eventName=getIntent().getStringExtra("eventName");
        isEventFavorite=getIntent().getBooleanExtra("eventFavorite",false);
        title=findViewById(R.id.event_name_title);
        fbIcon=findViewById(R.id.fb_icon);
        favIcon=findViewById(R.id.fav_icon);
        twitterIcon=findViewById(R.id.twitter_icon);

        if(isEventFavorite){
            favIcon.setImageResource(R.drawable.ic_favorites_fill);
        }else{
            favIcon.setImageResource(R.drawable.ic_favorites);
        }

        progressLoader=findViewById(R.id.progress_loader);
        title.setText(eventName);
        title.setSelected(true);
        viewPager2 = findViewById(R.id.view_pager_event);
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tabView =  findViewById(R.id.tab_layout_event);
        fbIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fb = "https://www.facebook.com/sharer/sharer.php?u=" + eventData.getTicketUrl() + "&quote=Check%20" + eventName + "%20on%20Ticketmaster.%0A";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fb));
                startActivity(browserIntent);
            }
        });
        twitterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String twitter = "https://twitter.com/intent/tweet?text=Check%20"+eventName+"%20on%20Ticketmaster.%0A&url="+eventData.getTicketUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter));
                startActivity(browserIntent);
            }
        });
        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                if (eventData.getIsFavorite()) {
                    try {
                        favoritesList = new JSONArray(sharedpreferences.getString("favorites", "[]"));
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    for (int j = 0; j < favoritesList.length(); ++j) {
                        JSONObject event;
                        String eventId;
                        try {
                            event = favoritesList.getJSONObject(j);
                            eventId = event.getString("eventId");
                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (eventId.equals(eventData.getEventId())) {
                            Toast.makeText(getApplicationContext(), eventData.getName()+" removed from favorites", Toast.LENGTH_SHORT).show();
                            favoritesList.remove(j);
                            favIcon.setImageResource(R.drawable.ic_favorites);
                            eventData.setIsFavorite(false);
                            break;
                        }
                    }
                } else {
                    JSONObject favEventData = new JSONObject();
                    try {
                        favEventData.put("eventId", eventData.getEventId());
                        favEventData.put("name", eventData.getName());
                        favEventData.put("venue", eventData.getVenue());
                        favEventData.put("imageUrl", eventData.getEventImageUrl());
                        favEventData.put("date", eventData.getDate());
                        favEventData.put("time", eventData.getTime());
                        favEventData.put("genre", eventData.getGenre());
                        favEventData.put("isFavorite", true);
                        try {
                            favoritesList = new JSONArray(sharedpreferences.getString("favorites", "[]"));
                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        favoritesList.put(favEventData);
                        Log.d("favoritesList", favoritesList.toString());
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("favorites", favoritesList.toString());
                        eventData.setIsFavorite(true);
                        Toast.makeText(getApplicationContext(), eventData.getName()+" added to favorites", Toast.LENGTH_SHORT).show();
                        favIcon.setImageResource(R.drawable.ic_favorites_fill);
                        editor.apply();
                    } catch (JSONException ex) {

                    }


                }
            }
        });
        getEventDetails(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                DetailsFragment tab1 = new DetailsFragment(
                        eventData.getName(),
                        eventData.getEventId(),
                        eventData.getVenue(),
                        eventData.getDate(),
                        eventData.getGenre(),
                        eventData.getTime(),
                        eventData.getIsFavorite(),
                        eventData.getArtistsTeams(),
                        eventData.getPriceRange(),
                        eventData.getTicketStatus(),
                        eventData.getTicketUrl(),
                        eventData.getSeatmap()
                );
                getArtistDetails(new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Here success");
                        ArtistFragment tab2 = new ArtistFragment(artistData);
                        getVenueDetails(new VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                                VenueFragment tab3 = new VenueFragment(venueData);
                                progressLoader.setVisibility(View.GONE);

                                EventDetailsPagerAdapter adapter = new EventDetailsPagerAdapter(getSupportFragmentManager(), getLifecycle());
                                adapter.addFragment(tab1);
                                adapter.addFragment(tab2);
                                adapter.addFragment(tab3);
                                viewPager2.setAdapter(adapter);
                                new TabLayoutMediator(tabView, viewPager2,
                                        (tab, position) -> {
                                            switch (position) {
                                                case 0:
                                                    tab.setText("Details");
                                                    tab.setIcon(R.drawable.ic_details);
//                                        int col=ContextCompat.getColor(tab.parent.getContext(),R.color.green);
//                                        tab.getIcon().setColorFilter(col, PorterDuff.Mode.SRC_IN);
                                                    break;
                                                case 1:
                                                    tab.setText("Artist(s)");
                                                    tab.setIcon(R.drawable.ic_artist);
                                                    break;
                                                case 2:
                                                    tab.setText("Venue");
                                                    tab.setIcon(R.drawable.ic_venue);
                                                    break;
                                                default:
                                                    tab.setText("Tab");
                                                    break;
                                            }
                                        }
                                ).attach();
                            }
                        });
                    }
                });
            }
        });



        tabView.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                        int col=ContextCompat.getColor(tab.parent.getContext(),R.color.green);
                        tab.getIcon().setColorFilter(col, PorterDuff.Mode.SRC_IN);
                }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int col=ContextCompat.getColor(tab.parent.getContext(),R.color.white);
                tab.getIcon().setColorFilter(col, PorterDuff.Mode.SRC_IN);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }
    private boolean getEventDetails(final VolleyCallBack callBack) {
        eventData= new Event(null,null,null,null,null);
        Log.d("search_url", "url is " + eventDetailsUrl);
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(eventDetailsUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            eventData.setEventId(response.getString("id"));
                            eventData.setEventImageUrl(response.getJSONArray("images").getJSONObject(0).getString("url"));
                            DateFormat dateGivenFormat = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat dateFormat = new SimpleDateFormat("MMM, dd yyyy");
                            DateFormat timeGivenFormat = new SimpleDateFormat("hh:mm:ss");
                            DateFormat timeFormat = new SimpleDateFormat("h:mm a");
                            eventData.setIsFavorite(isEventFavorite);
                            eventData.setDate(dateFormat.format(dateGivenFormat.parse(response.getJSONObject("dates").getJSONObject("start").getString("localDate"))));
                            eventData.setTime(timeFormat.format(timeGivenFormat.parse(response.getJSONObject("dates").getJSONObject("start").getString("localTime"))));
                            eventData.setName(response.getString("name"));
                            try {
                                eventData.setVenue(response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name"));
                            }
                            catch (Exception e){
//                                venueLabel.setVisibility(View.GONE);
                            }
                            try {
                                eventData.setPriceRange(response.getJSONArray("priceRanges").getJSONObject(0).getString("min")
                                        + " - "
                                        + response.getJSONArray("priceRanges").getJSONObject(0).getString("max")
                                        + " " + response.getJSONArray("priceRanges").getJSONObject(0).getString("currency"));
                            }catch (Exception e){
                                eventData.setPriceRange("");
                            }
                            eventData.setTicketStatus(response.getJSONObject("dates").getJSONObject("status").getString("code"));
                            List<String> category_detail = new ArrayList<String>();

                            try{
                            category_detail.add(response.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name"));
                            category_detail.add(response.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name"));
                            category_detail.add(response.getJSONArray("classifications").getJSONObject(0).getJSONObject("subGenre").getString("name"));
                            category_detail.add(response.getJSONArray("classifications").getJSONObject(0).getJSONObject("type").getString("name"));
                            category_detail.add(response.getJSONArray("classifications").getJSONObject(0).getJSONObject("subType").getString("name"));
                            }catch (Exception e) {
                            }
                            eventData.setCategoryDetail(category_detail);
                            JSONArray artists_teams_list = new JSONArray();
                            try{
                                artists_teams_list = response.getJSONObject("_embedded").getJSONArray("attractions");
                            }catch (Exception e) {
                            }

                            List<String> artists_teams_array = new ArrayList<String>();
                            List<String> artists_spotify_array = new ArrayList<String>();
                            for(int k = 0; k < artists_teams_list.length(); ++k){
                                artists_teams_array.add(artists_teams_list.getJSONObject(k).getString("name"));
                                if(artists_teams_list.getJSONObject(k).getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name").equals("Music")){
                                    artists_spotify_array.add(artists_teams_list.getJSONObject(k).getString("name"));
                                }
                            }
                            eventData.setArtistsTeams(artists_teams_array);
                            eventData.setArtistSpotifyList(artists_spotify_array);
                            try {
                                eventData.setSeatmap(response.getJSONObject("seatmap").getString("staticUrl"));
                            }catch (Exception e){
                                eventData.setSeatmap("");
                            }
                            eventData.setTicketUrl(response.getString("url"));
                            System.out.println(eventData.getDate()+" " +response.getJSONObject("dates").getJSONObject("start").getString("localDate"));
                            callBack.onSuccess();

                        } catch (Exception e) {
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
    private boolean getArtistDetails(final VolleyCallBack callBack) {
        artistData=new ArrayList<>();
        System.out.println("artist "+eventData.getArtistTeamList());
        System.out.println("spotify "+eventData.getArtistSpotifyList());
        requestQueue = Volley.newRequestQueue(this);
        if(eventData.getArtistSpotifyList().size()!=0) {
            for (int i = 0; i < eventData.getArtistSpotifyList().size(); i++) {
                String url = artistUrl + eventData.getArtistTeamList().get(i);
                Artist currentArtist = new Artist();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    System.out.println(response);
                                    currentArtist.setName(response.getString("name"));
                                    currentArtist.setPopularity(response.getString("popularity"));
                                    long follow = Long.parseLong(response.getJSONObject("followers").getString("total"));
                                    currentArtist.setFollowers(withSuffix(follow) + " Followers");
                                    currentArtist.setArtistImg(response.getJSONArray("images").getJSONObject(0).getString("url"));
                                    currentArtist.setSpotifyLink(response.getJSONObject("external_urls").getString("spotify"));
                                    List<String> album = new ArrayList<>();
                                    if (response.has("albums")) {
                                        for (int j = 0; j < response.getJSONObject("albums").getJSONArray("items").length(); j++) {
                                            album.add(response.getJSONObject("albums").getJSONArray("items").getJSONObject(j).getJSONArray("images").getJSONObject(0).getString("url"));
                                        }
                                    }
                                    currentArtist.setAlbums(album);
                                } catch (Exception e) {
                                    System.out.println("Inside catch" + e);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
                artistData.add(currentArtist);
                requestQueue.add(jsonObjectRequest);
            }
        }
        System.out.println(artistData);
        callBack.onSuccess();
        return true;
    }
    private boolean getVenueDetails(final VolleyCallBack callBack) {
        venueData= new Venue();
        System.out.println("In venue details "+eventData.getVenue());
        requestQueue = Volley.newRequestQueue(this);
        String url=venueUrl+eventData.getVenue();
        System.out.println("-------------------"+url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                                System.out.println(url+ " " +response);
                                venueData.setName(response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name"));
                                venueData.setAddress(response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("address").getString("line1"));
                                venueData.setCityState(response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").getString("name")+", "+response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("state").getString("name"));
                                venueData.setLat(response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location").getString("latitude"));
                                venueData.setLng(response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location").getString("longitude"));
                                JSONObject venueInfo=response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);
                                if(venueInfo.has("boxOfficeInfo")){
                                    if(venueInfo.getJSONObject("boxOfficeInfo").has("phoneNumberDetail")){
                                        venueData.setPhone(venueInfo.getJSONObject("boxOfficeInfo").getString("phoneNumberDetail"));
                                    }
                                    if(venueInfo.getJSONObject("boxOfficeInfo").has("openHoursDetail")){
                                        venueData.setOpenHours(venueInfo.getJSONObject("boxOfficeInfo").getString("openHoursDetail"));
                                    }
                                }
                                if(venueInfo.has("generalInfo")){
                                    if(venueInfo.getJSONObject("generalInfo").has("generalRule")){
                                        venueData.setGenRule(venueInfo.getJSONObject("generalInfo").getString("generalRule"));
                                    }
                                    if(venueInfo.getJSONObject("generalInfo").has("childRule")){
                                        venueData.setChildRule(venueInfo.getJSONObject("generalInfo").getString("childRule"));
                                    }
                                }

                        } catch (Exception e) {

                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        requestQueue.add(jsonObjectRequest);
        callBack.onSuccess();
        return true;
    }
    public static String withSuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f%c",
                count / Math.pow(1000, exp),
                "KMGTPE".charAt(exp-1));
    }
}