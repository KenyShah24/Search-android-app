package com.example.eventfinder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistFragment extends Fragment {

    private List<Artist> artistData;
    private ArtistListAdapter myArtistAdapter;
    private TextView noArtists;
    private RecyclerView artistRv;

    public ArtistFragment() {
        // Required empty public constructor
    }
    public ArtistFragment(List<Artist> artistData) {
        this.artistData=artistData;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistFragment newInstance(String param1, String param2) {
        ArtistFragment fragment = new ArtistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myArtistAdapter = new ArtistListAdapter(getContext(), artistData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        artistRv = view.findViewById(R.id.artist_rv);
        noArtists=view.findViewById(R.id.no_artists);
        if(artistData.size()==0){
            noArtists.setVisibility(View.VISIBLE);
            artistRv.setVisibility(View.GONE);
        }
        else{
            noArtists.setVisibility(View.GONE);
            artistRv.setVisibility(View.VISIBLE);
        }
        artistRv.setLayoutManager(new GridLayoutManager(getContext(),1));
        artistRv.setAdapter(myArtistAdapter);
    }
}