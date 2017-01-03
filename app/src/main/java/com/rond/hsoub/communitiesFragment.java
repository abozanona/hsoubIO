package com.rond.hsoub;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rond.hsoub.API.CustomAPI;
import com.rond.hsoub.API.CustomAPIEnum;
import com.rond.hsoub.Adapters.communityAdapter;
import com.rond.hsoub.Classes.DBHelper;
import com.rond.hsoub.Classes.Serialization;
import com.rond.hsoub.Models.community;

import java.util.ArrayList;


public class communitiesFragment extends Fragment {

    RecyclerView rcv_communities;
    RecyclerView.Adapter rcvAdapter;
    RecyclerView.LayoutManager rcvLayoutManager;
    ArrayList<community> mCommunities;


    private View mRootView;

    public communitiesFragment() {
        // Required empty public constructor
    }

    String viewedCommunities;
    static boolean isDoneLoading=false, isFetching=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_posts_list, container, false);

        final Button btnLoadMore = (Button)mRootView.findViewById(R.id.btnLoadMore);

        mCommunities = new ArrayList<>();



        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(!isDoneLoading && !isFetching) {
                String viewedCommunitiesClone = viewedCommunities;
                viewedCommunitiesClone = (!viewedCommunitiesClone.equals(""))?viewedCommunitiesClone.substring(0, viewedCommunitiesClone.length() - 1):"";
                isFetching = true;
                btnLoadMore.setEnabled(false);

                String communitySlug = "";
                if(getArguments()!=null)
                    communitySlug = getArguments().getString("community", null);
                if(communitySlug==null)
                    communitySlug="";

                new CustomAPI(CustomAPIEnum.getLatestCommunities) {
                    @Override
                    public void getLatestCommunitiesListener(ArrayList<community> communities) {
                        //// TODO: 12/30/2016 :/
                        if (communities == null) {
                            mCommunities= (ArrayList<community>)Serialization.deserializeObject(new DBHelper(mRootView.getContext()).getDictionary("community"));
                            Snackbar.make(mRootView, getResources().getString(R.string.error_connection), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                        if (communities == null) {
                            isDoneLoading = true;
                        }
                        else if (communities.size() == 0)
                            isDoneLoading = true;
                        else {
                            new DBHelper(mRootView.getContext()).setDictionary("community",Serialization.serializeObject(mCommunities));
                            for (int i = 0; i < communities.size(); i++)
                                viewedCommunities += communities.get(i).getNumber() + ",";
                            isFetching = false;
                            mCommunities.addAll(communities);
                            rcvAdapter.notifyDataSetChanged();
                        }
                        btnLoadMore.setEnabled(true);
                    }
                }.execute("{\"s\":\"\",\"search_community_slug\":\"\",\"community_ids\":[" + viewedCommunitiesClone + "]}", communitySlug);
            }
            }
        });
        viewedCommunities = "";
        rcv_communities = (RecyclerView) mRootView.findViewById(R.id.rcv_posts_list);


        rcvFirstFill();
        btnLoadMore.performClick();
        return mRootView;
    }

    private void rcvFirstFill() {
        rcvLayoutManager = new LinearLayoutManager(mRootView.getContext());
        rcv_communities.setLayoutManager(rcvLayoutManager);
        rcv_communities.setHasFixedSize(true);
        rcvAdapter = new communityAdapter(mCommunities);
        rcv_communities.setAdapter(rcvAdapter);

    }

}