package com.example.eventfinder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VenueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VenueFragment extends Fragment implements OnMapReadyCallback {
    private Venue venueData;
    LinearLayout nameLayout,addressLayout,phoneLayout,cityLayout,extraInfo;
    private GoogleMap mMap;

    TextView openLabel,childLabel,genLabel;
    TextView nameValue,addressValue,phoneValue,cityValue,openValue,genValue;

    TextView childValue;

    public VenueFragment(Venue venueData) {
        this.venueData=venueData;
        // Required empty public constructor
    }
    public VenueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VenueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VenueFragment newInstance(String param1, String param2) {
        VenueFragment fragment = new VenueFragment();
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_venue, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("again "+venueData.getName());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        nameLayout = view.findViewById(R.id.name_layout);
        addressLayout = view.findViewById(R.id.address_layout);
        cityLayout = view.findViewById(R.id.city_layout);
        phoneLayout = view.findViewById(R.id.phone_layout);
        openLabel = view.findViewById(R.id.open_label);
        genLabel = view.findViewById(R.id.gen_label);
        childLabel = view.findViewById(R.id.child_label);
        extraInfo = view.findViewById(R.id.extra_info);


        nameValue = view.findViewById(R.id.name_value);
        addressValue = view.findViewById(R.id.address_value);
        cityValue = view.findViewById(R.id.city_value);
        phoneValue = view.findViewById(R.id.phone_value);
        openValue = view.findViewById(R.id.open_value);
        genValue = view.findViewById(R.id.gen_value);
        childValue = view.findViewById(R.id.child_value);

        nameValue.setText(venueData.getName());
        nameValue.setSelected(true);
            addressValue.setText(venueData.getAddress());
            addressValue.setSelected(true);

            phoneValue.setText(venueData.getPhone());
            phoneValue.setSelected(true);

            cityValue.setText(venueData.getCityState());
            cityValue.setSelected(true);

        if(venueData.getOpenHours()!=null){
            openValue.setText(venueData.getOpenHours());
            openLabel.setVisibility(View.VISIBLE);
            openValue.setVisibility(View.VISIBLE);
        }
        if(venueData.getGenRule()!=null){
            genValue.setText(venueData.getGenRule());
            genLabel.setVisibility(View.VISIBLE);
            genValue.setVisibility(View.VISIBLE);
        }
        if(venueData.getChildRule()!=null){
            childValue.setText(venueData.getChildRule());
            childLabel.setVisibility(View.VISIBLE);
            childValue.setVisibility(View.VISIBLE);
        }
        if(venueData.getChildRule()==null && venueData.getGenRule()==null && venueData.getOpenHours()==null){
            extraInfo.setVisibility(View.GONE);
        }
        childValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childValue.setMaxLines(100);
            }
        });
        genValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genValue.setMaxLines(100);
            }
        });
        openValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openValue.setMaxLines(100);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng locate = new LatLng(Double.parseDouble(venueData.getLat()),Double.parseDouble(venueData.getLng()));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(venueData.getName());
        markerOptions.position(locate);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locate, 15));
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(markerOptions);


        mMap.getUiSettings().setZoomControlsEnabled(true);

    }
}