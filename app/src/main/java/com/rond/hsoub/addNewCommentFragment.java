package com.rond.hsoub;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class addNewCommentFragment  extends DialogFragment {


    public addNewCommentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().setTitle(getResources().getString(R.string.add_reply));
        return inflater.inflate(R.layout.fragment_add_new_comment, container, false);
    }

}
