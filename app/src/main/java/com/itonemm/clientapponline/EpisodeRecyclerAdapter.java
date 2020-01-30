package com.itonemm.clientapponline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class EpisodeRecyclerAdapter extends RecyclerView.Adapter<EpisodeRecyclerAdapter.EpisodeHolder> {
    RewardedAd rewardedAd;
    ArrayList<MovieModel> movieModels=new ArrayList<MovieModel>();
    Context context;
    static Activity activity;


    public EpisodeRecyclerAdapter(ArrayList<MovieModel> movieModels, Context context) {
        this.movieModels = movieModels;
        this.context = context;
        MobileAds.initialize(context,context.getResources().getString(R.string.app_id));
        rewardedAd = new RewardedAd(context,context.getResources().getString(R.string.rewareded_id));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    @NonNull
    @Override
    public EpisodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.eplist,parent,false);
        return new EpisodeHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeHolder holder, final int position) {
    holder.sr.setText((position+1)+"");
    holder.name.setText(movieModels.get(position).movieName);
    holder.play.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (rewardedAd.isLoaded())
            {
                RewardedAdCallback adCallback = new RewardedAdCallback() {
                    @Override
                    public void onRewardedAdOpened() {
                        // Ad opened.
                    }

                    @Override
                    public void onRewardedAdClosed() {
                        // Ad closed.
                    }

                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem reward) {
                        Intent intent = new Intent(context, PlayVideoActivity.class);
                        try {
                            PlayVideoActivity.videourl = MediaFireConnect.getVideoFileLink(movieModels.get(position).movieVideo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onRewardedAdFailedToShow(int errorCode) {
                        // Ad failed to display.
                    }
                };
                rewardedAd.show(activity, adCallback);
            }else {
                Intent intent=new Intent(context,PlayVideoActivity.class);
                try {
                    PlayVideoActivity.videourl=MediaFireConnect.getVideoFileLink(movieModels.get(position).movieVideo);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }

        }
    });
    }

    @Override
    public int getItemCount() {
        return movieModels.size();
    }

    public class EpisodeHolder extends RecyclerView.ViewHolder{

        TextView sr,name;
        ImageView play;
        public EpisodeHolder(@NonNull View itemView) {
            super(itemView);
            sr=itemView.findViewById(R.id.txt_ep_sr);
            name=itemView.findViewById(R.id.txt_ep_name);
            play=itemView.findViewById(R.id.btnplay);
        }
    }
}
