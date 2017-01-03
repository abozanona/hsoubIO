package com.rond.hsoub;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rond.hsoub.API.CustomAPI;
import com.rond.hsoub.API.CustomAPIEnum;
import com.rond.hsoub.Adapters.commentAdapter;
import com.rond.hsoub.Classes.DBHelper;
import com.rond.hsoub.Models.comment;

import java.util.ArrayList;

public class postFragment extends Fragment {

    RecyclerView rcv_comments_list;
    RecyclerView.Adapter rcvAdapter;
    RecyclerView.LayoutManager rcvLayoutManager;
    ArrayList<comment> mCommentsItem;

    SwipeRefreshLayout lytSwipeRefresh;
    private View mRootView;

    public postFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView =  inflater.inflate(R.layout.fragment_post, container, false);
        rcv_comments_list = (RecyclerView) mRootView.findViewById(R.id.rcv_post);
        rcvLayoutManager = new LinearLayoutManager(mRootView.getContext());
        rcv_comments_list.setLayoutManager(rcvLayoutManager);
        rcv_comments_list.setHasFixedSize(true);

        mCommentsItem = new ArrayList<>();

        String communityID = null, postID = null;
        if(getArguments()!=null) {
            communityID = getArguments().getString("community", null);
            postID = getArguments().getString("post", null);
        }
        if(communityID==null || postID==null) {
            return mRootView;
        }
        lytSwipeRefresh = (SwipeRefreshLayout)mRootView.findViewById(R.id.lytSwipeRefresh);


        final String finalCommunityID = communityID;
        final String finalPostID = postID;
        lytSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadContent(finalCommunityID, finalPostID);
            }
        });
        loadContent(communityID, postID);

        return mRootView;
    }

    private void loadContent(final String communityID, final String finalPostID){
        new CustomAPI(CustomAPIEnum.getPost){
            @Override
            public void getPostListener(String title, ArrayList<comment> comments, final ArrayList<comment> normalComments) {
                lytSwipeRefresh.setRefreshing(false);
                ((TextView)mRootView.findViewById(R.id.lblTitle)).setText(title);
                if(comments==null){
                    Snackbar.make(mRootView, getResources().getString(R.string.error_connection), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    comments=new DBHelper(mRootView.getContext()).getPost(finalPostID);

                } else{
                    new DBHelper(mRootView.getContext()).setPost(finalPostID,comments);
                }
                if(comments==null)
                    return;
                mCommentsItem = comments;


                //// TODO: 1/1/2017
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                rcvAdapter = new commentAdapter(normalComments, false, normalComments.get(0).getCommentId());
                                rcv_comments_list.setAdapter(rcvAdapter);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                rcvAdapter = new commentAdapter(mCommentsItem, true, mCommentsItem.get(0).getCommentId());
                                rcv_comments_list.setAdapter(rcvAdapter);
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext());
                builder.setMessage("عرض التعليقات بالوضع الأفقي أم المتداخل؟").setPositiveButton("أفقي", dialogClickListener)
                        .setNegativeButton("متداخل", dialogClickListener).show();
            }
        }.execute(communityID, finalPostID);
    }
}
