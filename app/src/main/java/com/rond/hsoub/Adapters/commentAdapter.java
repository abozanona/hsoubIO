package com.rond.hsoub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rond.hsoub.API.DownloadImageTask;
import com.rond.hsoub.Main2Activity;
import com.rond.hsoub.Models.comment;
import com.rond.hsoub.R;
import com.rond.hsoub.profileFragment;

import java.util.ArrayList;

/**
 * Created by Nullsky on 11/20/2016.
 */

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.commentHolder> {
    private ArrayList<comment> mcomment = new ArrayList<>();

    // TODO: 1/1/2017 temp
    private boolean mNestedMode;
    private String mPostId;

    public commentAdapter(ArrayList<comment> comment, boolean nestedMode, String postId){
        mcomment=comment;
        mNestedMode=nestedMode;
        mPostId=postId;
    }

    @Override
    public commentAdapter.commentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new commentHolder(view);
    }

    @Override
    public void onBindViewHolder(final commentAdapter.commentHolder holder, final int position) {


        final comment obj = mcomment.get(position);
        holder.lblVotes.setText(obj.getVotesNumber());
        holder.lblUserName.setText(obj.getUserName());
        holder.lblTime.setText(obj.getTime());
        holder.lblContent.setText(obj.getContent());
        if(obj.isMainPost())
            holder.lblShowComments.setVisibility(View.GONE);
        new DownloadImageTask(holder.imgUserImg).execute(obj.getUserImg());

        //colors
        //0 => #FFFFFF
        //1 => #EFEFEF
        //2 => #E7E7E7
        //3 => #EFEFEF
        int color = 0xFFFFFF;
        if(obj.getLevel()%4 == 0)
            color = holder.lytChangingColor.getContext().getResources().getColor(R.color.postLevel0);
        else if(obj.getLevel()%4 == 1)
            color = holder.lytChangingColor.getContext().getResources().getColor(R.color.postLevel1);
        else if(obj.getLevel()%4 == 2)
            color = holder.lytChangingColor.getContext().getResources().getColor(R.color.postLevel2);
        else if(obj.getLevel()%4 == 3)
            color = holder.lytChangingColor.getContext().getResources().getColor(R.color.postLevel3);
        holder.lytChangingColor.setBackgroundColor(color);

        if(!mNestedMode) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(obj.getLevel(), 0, 15*obj.getLevel(), 0);
            holder.lytChangingColor.setLayoutParams(lp);
        }

        //add inner list of comments even if it's size is zero
        RecyclerView rcv_comments_list;
        RecyclerView.Adapter rcvAdapter;
        rcvAdapter = new commentAdapter(obj.getComments(),mNestedMode,mPostId);
        RecyclerView.LayoutManager rcvLayoutManager;//// TODO: 12/30/2016 add something to enable comments for main post
        // TODO: 12/30/2016 Error in nested layouts
        // TODO: 12/30/2016 http://stackoverflow.com/a/9950339/4614264
        // TODO: 12/30/2016 http://stackoverflow.com/questions/2762924/java-lang-stackoverflow-error-suspected-too-many-views
        if(mNestedMode) {
            if (obj.getComments().size() > 0 /*&& obj.getLevel()!=-1*/) {
                rcv_comments_list = holder.rcv_inner_comments;
                rcvLayoutManager = new LinearLayoutManager(holder.rcv_inner_comments.getContext());
                rcv_comments_list.setLayoutManager(rcvLayoutManager);
                rcv_comments_list.setHasFixedSize(true);
                //rcvAdapter = new commentAdapter(obj.getComments());
                rcv_comments_list.setAdapter(rcvAdapter);
            }
        }
        holder.lblShowComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.rcv_inner_comments.getVisibility()==View.GONE)
                    holder.rcv_inner_comments.setVisibility(View.VISIBLE);
                else
                    holder.rcv_inner_comments.setVisibility(View.GONE);
            }
        });
        View.OnClickListener openProfileClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main2Activity.instance.setFragment(new profileFragment(),new Pair<>("userName", obj.getUserLink()));
            }
        };
        holder.lblUserName.setOnClickListener(openProfileClickListener);
        holder.imgUserImg.setOnClickListener(openProfileClickListener);

        final RecyclerView.Adapter finalRcvAdapter = rcvAdapter;
        holder.lytChangingColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //// TODO: 12/24/2016 Check with large lines of text
                LayoutInflater inflater = (LayoutInflater) holder.lytChangingColor.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View popupView = inflater.inflate(R.layout.fragment_add_new_comment, null);

                final TextView txtReply = (TextView) popupView.findViewById(R.id.btnSendReply);
                Button btnSendReply = (Button) popupView.findViewById(R.id.btnSendReply);

                btnSendReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //// TODO: 1/2/2017 temp solution
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://io.hsoub.com/go/" + mPostId + "/" + obj.getCommentId()));
                        holder.lytChangingColor.getContext().startActivity(intent);
                        if(1==1)
                            return;
                        comment newComment = new comment("", "", "abozanona", "abozanona", "12/9/1995", "0", new ArrayList<comment>(),false, "", txtReply.getText().toString(), obj.getLevel() + 1);
                        if(!obj.isMainPost() && mNestedMode) {
                            mcomment.get(position).getComments().add(newComment);
                            finalRcvAdapter.notifyDataSetChanged();
                        }
                        else {
                            //// TODO: 12/30/2016 add public members for the post's rcvAdapter & comments ArrayList
                            //mMainComments.get(position).getComments().add(newComment);
                            //mMainRcvAdapter.notifyDataSetChanged();
                        }
                    }
                });

                PopupWindow popupWindow = new PopupWindow(popupView, Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT, true);

                // If the PopupWindow should be focusable
                popupWindow.setFocusable(true);
                // If you need the PopupWindow to dismiss when when touched outside
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x99CCCCCC));

                // Using location, the PopupWindow will be displayed right under anchorView
                popupWindow.showAtLocation(holder.lytChangingColor, Gravity.CENTER, 0, 0);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mcomment.size();
    }

    static class commentHolder extends RecyclerView.ViewHolder{

        TextView lblVotes, lblUserName, lblTime, lblContent, lblShowComments;
        ImageView imgUserImg;
        RecyclerView rcv_inner_comments;
        View lytChangingColor;
        commentHolder(View itemView) {

            super(itemView);
            lblVotes = (TextView)itemView.findViewById(R.id.lblVotes);
            lblUserName = (TextView)itemView.findViewById(R.id.lblUserName);
            lblTime = (TextView)itemView.findViewById(R.id.lblTime);
            lblContent = (TextView)itemView.findViewById(R.id.lblContent);
            lblShowComments = (TextView)itemView.findViewById(R.id.lblShowComments);
            imgUserImg = (ImageView)itemView.findViewById(R.id.imgUserImg);
            rcv_inner_comments = (RecyclerView)itemView.findViewById(R.id.rcv_inner_comments);
            lytChangingColor = itemView.findViewById(R.id.lytChangingColor);

        }
    }
}
