package com.example.eventfinder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String name;
    private String eventId;
    private String venue;
    private String date;
    private String genre;
    private String time;
    private Boolean isFavorite;
    private String artistTeam;
    private String priceRange;
    private String ticketStatus;
    private String ticketUrl;
    private String seatmap;
    private CardView statusCard;

    LinearLayout artistLabel,venueLabel,dateLabel,timeLabel,ticketLabel,priceLabel,buyTicketLabel,genreLabel;
    TextView artistValue,venueValue,dateValue,timeValue,priceValue,buyTicketValue,ticketValue,genreValue;

    ImageView seatmapImg;


    public DetailsFragment() {
        // Required empty public constructor
    }
    public DetailsFragment(
    String name,
    String eventId,
    String venue,
    String date,
    String genre,
    String time,
    Boolean isFavorite,
    String artistTeam,
    String priceRange,
    String ticketStatus,
    String ticketUrl,
    String seatmap) {
         this.name=name;
        this.eventId=eventId;
        this.venue=venue;
        this.date=date;
        this.genre=genre;
        this.time=time;
        this.isFavorite=isFavorite;
        this.artistTeam=artistTeam;
        this.priceRange=priceRange;
        this.ticketStatus=ticketStatus;
        this.ticketUrl=ticketUrl;
        this.seatmap=seatmap;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        artistLabel = view.findViewById(R.id.artist_layout);
        venueLabel = view.findViewById(R.id.venue_layout);
        dateLabel = view.findViewById(R.id.date_layout);
        timeLabel = view.findViewById(R.id.time_layout);
        priceLabel = view.findViewById(R.id.price_layout);
        ticketLabel = view.findViewById(R.id.ticket_layout);
        buyTicketLabel = view.findViewById(R.id.buy_ticket_layout);
        genreLabel = view.findViewById(R.id.genre_layout);
        statusCard = view.findViewById(R.id.status_card);

        artistValue = view.findViewById(R.id.artist_value);
        venueValue = view.findViewById(R.id.venue_value);
        dateValue = view.findViewById(R.id.date_value);
        timeValue = view.findViewById(R.id.time_value);
        priceValue = view.findViewById(R.id.price_value);
        ticketValue = view.findViewById(R.id.ticket_value);
        buyTicketValue = view.findViewById(R.id.buy_ticket_value);
        genreValue = view.findViewById(R.id.genre_value);
        seatmapImg=view.findViewById(R.id.seatmap_image);
        System.out.println(artistTeam+" "+venue+" "+isFavorite+ " "+priceRange+ " " + seatmap+ " " +time + " " +ticketStatus+ " " +ticketUrl+" " +genre);
            artistValue.setText(artistTeam);
            artistValue.setSelected(true);
            venueValue.setText(venue);
            priceValue.setText(priceRange);
            ticketValue.setText(ticketStatus);
            genreValue.setText(genre);
            genreValue.setSelected(true);
        if(seatmap!=null){
            Picasso.get().load(seatmap).into(seatmapImg);
        }
        if(ticketStatus!=null) {
            switch (ticketStatus) {
                case "onsale":
                    statusCard.setBackgroundColor(R.color.green);
                    ticketValue.setText("On Sale");
                    break;
                case "offsale":
                    statusCard.setBackgroundColor(Color.rgb(255, 0, 0));
                    ticketValue.setText("Off Sale");
                    break;
                case "cancelled":
                    statusCard.setBackgroundColor(R.color.black);
                    ticketValue.setText("Cancelled");
                    break;
                case "rescheduled":
                    statusCard.setBackgroundColor(R.color.orange);
                    ticketValue.setText("Rescheduled");
                    break;
                case "postponed":
                    statusCard.setBackgroundColor(R.color.orange);
                    ticketValue.setText("Postponed");
                    break;
                default:
                    break;
            }
        }
        dateValue.setText(date);
        timeValue.setText(time);
        SpannableString text = new SpannableString(ticketUrl);
        if(text!=null) {
            text.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        }
        buyTicketValue.setText(text);
        buyTicketValue.setSelected(true);
        buyTicketValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ticketUrl));
                startActivity(browserIntent);
            }
        });
    }
}