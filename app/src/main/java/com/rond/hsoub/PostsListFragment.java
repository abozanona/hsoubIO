package com.rond.hsoub;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rond.hsoub.API.CustomAPI;
import com.rond.hsoub.API.CustomAPIEnum;
import com.rond.hsoub.API.JsonLinks;
import com.rond.hsoub.Adapters.postListItemAdapter;
import com.rond.hsoub.Classes.DBHelper;
import com.rond.hsoub.Models.postListItem;

import java.util.ArrayList;


public class PostsListFragment extends Fragment {

    RecyclerView rcv_posts_list;
    RecyclerView.Adapter rcvAdapter;
    RecyclerView.LayoutManager rcvLayoutManager;
    ArrayList<postListItem> mPostListItem;

    SwipeRefreshLayout lytSwipeRefresh;

    private View mRootView;
    private String link;

    private String searchPrefix = "{\"s\":\"\",\"search_community_slug\":\"\",\"post_ids\":[";

    public PostsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        isDoneLoading=false;
        isFetching=false;
        super.onResume();
    }

    String viewedPosts;
    static boolean isDoneLoading=false, isFetching=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_posts_list, container, false);

        final Button btnLoadMore = (Button)mRootView.findViewById(R.id.btnLoadMore);

        mPostListItem = new ArrayList<>();

        if(getArguments()!=null/* && link==null*/) {
            link = getArguments().getString("link", null);
            if(getArguments().getString("searchPrefix", null)!=null)
                searchPrefix = getArguments().getString("searchPrefix", null);
        }
        if(link==null)
            link=JsonLinks.get_more_posts("");


        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(!isDoneLoading && !isFetching) {
                String viewedPostsClone = viewedPosts;
                viewedPostsClone = (!viewedPostsClone.equals(""))?viewedPostsClone.substring(0, viewedPostsClone.length() - 1):"";
                isFetching = true;
                btnLoadMore.setEnabled(false);

                new CustomAPI(CustomAPIEnum.getLatestPostsDefault) {
                    public void getLatestPostsDefaultListener(ArrayList<postListItem> PostListItem) {
                        if (PostListItem == null){
                            Snackbar.make(mRootView, getResources().getString(R.string.error_connection), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            PostListItem=new DBHelper(mRootView.getContext()).getPostList(link);
                        }

                        if (PostListItem == null) {

                        } else if (PostListItem.size() == 0) {
                            isDoneLoading = true;
                        }
                        else {
                            //Update database
                            if(PostListItem.size()<=DBHelper.MAXIMUM_NUMBER_OF_STORED_ITEMS)
                                new DBHelper(mRootView.getContext()).setPostList(link,PostListItem);
                            else
                                new DBHelper(mRootView.getContext()).setPostList(link,new ArrayList<>(PostListItem.subList(0, DBHelper.MAXIMUM_NUMBER_OF_STORED_ITEMS)));
                            for (int i = 0; i < PostListItem.size(); i++)
                                viewedPosts += PostListItem.get(i).getPostID() + ",";
                            isFetching = false;
                            if(lytSwipeRefresh.isEnabled()) {
                                mPostListItem.clear();
                                rcvAdapter.notifyDataSetChanged();
                            }

                            mPostListItem.addAll(PostListItem);
                            rcvAdapter.notifyDataSetChanged();
                        }
                        btnLoadMore.setEnabled(true);
                        lytSwipeRefresh.setRefreshing(false);
                    }
                }.execute(searchPrefix + viewedPostsClone + "]}", link);
            }
            }
        });
        viewedPosts = "";
        rcv_posts_list = (RecyclerView) mRootView.findViewById(R.id.rcv_posts_list);

        rcvFirstFill();
        btnLoadMore.performClick();

        lytSwipeRefresh = (SwipeRefreshLayout)mRootView.findViewById(R.id.lytSwipeRefresh);
        lytSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 viewedPosts = "";
                 btnLoadMore.performClick();
             }
        });

        return mRootView;
    }

    private void rcvFirstFill() {
        rcvLayoutManager = new LinearLayoutManager(mRootView.getContext());
        rcv_posts_list.setLayoutManager(rcvLayoutManager);
        rcv_posts_list.setHasFixedSize(true);
        rcvAdapter = new postListItemAdapter(mPostListItem);
        rcv_posts_list.setAdapter(rcvAdapter);

    }
}
