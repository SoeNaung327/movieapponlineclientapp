package com.itonemm.clientapponline;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class SeriesRecyclerAdapter extends RecyclerView.Adapter<SeriesRecyclerAdapter.SeriesHolder> {

    private InterstitialAd mInterstitialAd;
    ArrayList<SeriesModel> seriesModels = new ArrayList<SeriesModel>();
    Context context;
    LayoutInflater inflater;

    public SeriesRecyclerAdapter(ArrayList<SeriesModel> SeriesModels, Context context, LayoutInflater inflater) {
        this.seriesModels = SeriesModels;
        this.context = context;
        this.inflater = inflater;
        MobileAds.initialize(context, context.getResources().getString(R.string.app_id));
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.interstial_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @NonNull
    @Override
    public SeriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new SeriesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesHolder holder, final int position) {

        Glide.with(context)
                .load(seriesModels.get(position).seriesImage)
                .into(holder.itemimage);
        holder.itemname.setText(seriesModels.get(position).seriesName);
        holder.itemimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {

                    mInterstitialAd.show();
                } else {
                    SeriesDetailActivity.model = seriesModels.get(position);
                    Intent intent = new Intent(context, SeriesDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return seriesModels.size();
    }

    public class SeriesHolder extends RecyclerView.ViewHolder {

        ImageView itemimage;
        TextView itemname;

        public SeriesHolder(@NonNull View itemView) {
            super(itemView);
            itemimage = itemView.findViewById(R.id.imagev);
            itemname = itemView.findViewById(R.id.movie_name);
        }
    }
}
