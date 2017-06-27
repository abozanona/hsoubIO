package com.rond.hsoub.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rond.hsoub.API.DownloadImageTask;
import com.rond.hsoub.Main2Activity;
import com.rond.hsoub.Models.postListItem;
import com.rond.hsoub.R;
import com.rond.hsoub.postFragment;
import com.rond.hsoub.profileFragment;
import com.rond.hsoub.topicFragment;

import java.util.ArrayList;

/**
 * Created by Nullsky on 11/19/2016.
 */

public class postListItemAdapter extends RecyclerView.Adapter<postListItemAdapter.postListItemHolder> {
    private ArrayList<postListItem> mpostListItem = new ArrayList<>();

    public postListItemAdapter(ArrayList<postListItem> postListItem){
        mpostListItem=postListItem;
    }

    @Override
    public postListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view_post_list, parent, false);
        return new postListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final postListItemHolder holder, int position) {

        final postListItem obj = mpostListItem.get(position);
        holder.lblTopicTitle.setText(obj.getTopicTitle());
        holder.lblUserName.setText(obj.getUserName());
        holder.lblTopicType.setText(obj.getTopicType());
        holder.lblCommentsNumber.setText(obj.getCommentsNumber());
        holder.lblTime.setText(obj.getTime());
        new DownloadImageTask(holder.imgUserImg).execute(obj.getImgLink());
        //holder.imgUserImg.setImageBitmap(obj.getUserImg());

        holder.lblTopicTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //// TODO: 12/29/2016 Error when loading links from the database, Solved temporarly with a try, catch statement
            if(obj.getLink()!=null && obj.getLink()!=""){
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(obj.getLink()));
                    holder.lblTopicTitle.getContext().startActivity(intent);
                } catch (Exception e){
                    Main2Activity.instance.setFragment(new postFragment(), new Pair<>("community", obj.getTopicID()), new Pair<>("post", obj.getPostID()));
                }
            }
            else
                Main2Activity.instance.setFragment(new postFragment(), new Pair<>("community", obj.getTopicID()), new Pair<>("post", obj.getPostID()));
            }
        });
        holder.lblUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Main2Activity.instance.setFragment(new profileFragment(), new Pair<String, String>("userName", obj.getUserID()));
            }
        });
        holder.imgUserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Main2Activity.instance.setFragment(new profileFragment(), new Pair<String, String>("userName", obj.getUserID()));
            }
        });
        holder.lblTopicType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Main2Activity.instance.setFragment(new topicFragment());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mpostListItem.size();
    }

    static class postListItemHolder extends RecyclerView.ViewHolder{

        TextView lblTopicTitle,lblUserName,lblTopicType,lblCommentsNumber,lblTime;
        ImageView imgUserImg;
        postListItemHolder(View itemView) {

            super(itemView);
            lblTopicTitle = (TextView)itemView.findViewById(R.id.lblTopicTitle);
            lblUserName = (TextView)itemView.findViewById(R.id.lblUserName);
            lblTopicType = (TextView)itemView.findViewById(R.id.lblTopicType);
            lblCommentsNumber = (TextView)itemView.findViewById(R.id.lblCommentsNumber);
            lblTime = (TextView)itemView.findViewById(R.id.lblTime);
            imgUserImg = (ImageView)itemView.findViewById(R.id.imgUserImg);


        }
    }
}
