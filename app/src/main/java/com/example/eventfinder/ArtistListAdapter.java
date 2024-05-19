package com.example.eventfinder;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ArtistListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Artist> mData;
    public ArtistListAdapter(Context mContext, List<Artist> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_card, parent, false);
        return new ArtistHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ArtistHolder artistHolder = (ArtistHolder) holder;
        artistHolder.name.setText(mData.get(position).getName());
        artistHolder.followers.setText(mData.get(position).getFollowers());
        Glide.with(this.mContext).load(mData.get(position).getArtistImg()).into(artistHolder.artistImg);
        try {
            Glide.with(this.mContext).load(mData.get(position).getAlbums().get(0)).into(artistHolder.albumImg1);
            Glide.with(this.mContext).load(mData.get(position).getAlbums().get(1)).into(artistHolder.albumImg2);
            Glide.with(this.mContext).load(mData.get(position).getAlbums().get(2)).into(artistHolder.albumImg3);
        }catch (Exception e){
            System.out.println(mData.get(position).getAlbums());
        }
        artistHolder.popularity.setProgress(Integer.parseInt(mData.get(position).getPopularity()));
        artistHolder.progressText.setText(mData.get(position).getPopularity());
        artistHolder.spotiyLink.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString text = new SpannableString("Check out on spotify");
        text.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        artistHolder.spotiyLink.setText(text);
        artistHolder.spotiyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(position).getSpotifyLink()));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.getApplicationContext().startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class ArtistHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView followers;
        public TextView spotiyLink,progressText;
        public ProgressBar popularity;
        public ImageView artistImg,albumImg1,albumImg2,albumImg3;
        public CardView cardView;

        public ArtistHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.artist_card);
            name = itemView.findViewById(R.id.artist_name);
            progressText = itemView.findViewById(R.id.progress_text);
            followers = itemView.findViewById(R.id.artist_follow);
            spotiyLink = itemView.findViewById(R.id.spotify);
            popularity = itemView.findViewById(R.id.progress_ring);
            artistImg = itemView.findViewById(R.id.artist_image);
            albumImg1 = itemView.findViewById(R.id.album_img_1);
            albumImg2 = itemView.findViewById(R.id.album_img_2);
            albumImg3 = itemView.findViewById(R.id.album_img_3);
        }

    }
}
