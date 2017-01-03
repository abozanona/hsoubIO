package com.rond.hsoub.Adapters;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rond.hsoub.API.JsonLinks;
import com.rond.hsoub.Main2Activity;
import com.rond.hsoub.Models.community;
import com.rond.hsoub.PostsListFragment;
import com.rond.hsoub.R;

import java.util.ArrayList;

/**
 * Created by Nullsky on 12/29/2016.
 */

public class communityAdapter extends RecyclerView.Adapter<communityAdapter.communityHolder> {
    private ArrayList<community> mcommunity = new ArrayList<>();

    public communityAdapter(ArrayList<community> community){
        mcommunity=community;
    }

    @Override
    public communityAdapter.communityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community, parent, false);
        return new communityHolder(view);
    }

    @Override
    public void onBindViewHolder(final communityAdapter.communityHolder holder, int position) {

        //// TODO: 12/29/2016 Add click functionality
        final community obj = mcommunity.get(position);
        holder.lblCommunityName.setText(obj.getName());
        holder.lblFollowCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.lblFollowers.setText(obj.getFollowers());
        holder.lblDescription.setText(obj.getDescription());
        holder.lblCommunityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main2Activity.instance.setFragment(new PostsListFragment(),new Pair<>("link", JsonLinks.get_more_posts(obj.getID())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mcommunity.size();
    }

    static class communityHolder extends RecyclerView.ViewHolder{

        TextView lblCommunityName, lblFollowCommunity, lblFollowers, lblDescription;
        communityHolder(View itemView) {

            super(itemView);
            lblCommunityName = (TextView)itemView.findViewById(R.id.lblCommunityName);
            lblFollowCommunity = (TextView)itemView.findViewById(R.id.lblFollowCommunity);
            lblFollowers = (TextView)itemView.findViewById(R.id.lblFollowers);
            lblDescription = (TextView)itemView.findViewById(R.id.lblDescription);
        }
    }
}
