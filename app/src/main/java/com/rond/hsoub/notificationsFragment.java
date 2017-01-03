package com.rond.hsoub;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rond.hsoub.API.ConnectionMethod;
import com.rond.hsoub.API.CustomAPI;
import com.rond.hsoub.API.CustomAPIEnum;
import com.rond.hsoub.API.JsonLinks;
import com.rond.hsoub.API.OnWebServiceDoneListener;
import com.rond.hsoub.API.WebService;
import com.rond.hsoub.API.WebServiceResponse;
import com.rond.hsoub.Adapters.notificationAdapter;
import com.rond.hsoub.Classes.GeneralInstances;
import com.rond.hsoub.Models.notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class notificationsFragment extends Fragment {

    RecyclerView rcv_notifications_list;
    RecyclerView.Adapter rcvAdapter;
    RecyclerView.LayoutManager rcvLayoutManager;
    ArrayList<notification> mNotifications;

    public notificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        Snackbar.make(rootView, getResources().getString(R.string.error_connection), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        if(1==1)
            return rootView;
        mNotifications = new ArrayList<>();


        CustomAPI getNot = new CustomAPI(CustomAPIEnum.getNewCSRF_Tocken_Patters){
            @Override
            public void getNewCSRF_TockenListner(final String CSRF) {
                if(CSRF==null || CSRF.equals(""))
                    return;
                HashMap<String, String> hm = new HashMap<>();
                hm.put("X-CSRF-Token", CSRF);
                new WebService(JsonLinks.notifications, ConnectionMethod.GET, GeneralInstances.user.getCookies(), hm, null, new OnWebServiceDoneListener() {
                    @Override
                    public void onTaskDone(WebServiceResponse responseData) {

                        try {
                            JSONObject obj = new JSONObject(responseData.getJSONResponce());

                            JSONArray arr = obj.getJSONArray("notifications_data");
                            for(int i=0;i<arr.length();i++)
                                mNotifications.add( new notification(arr.get(i).toString()));

                            rcv_notifications_list = (RecyclerView) rootView.findViewById(R.id.rcv_notifications_list);
                            rcvLayoutManager = new LinearLayoutManager(rootView.getContext());
                            rcv_notifications_list.setLayoutManager(rcvLayoutManager);
                            rcv_notifications_list.setHasFixedSize(true);
                            rcvAdapter = new notificationAdapter(mNotifications);
                            rcv_notifications_list.setAdapter(rcvAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(rootView, e.getMessage(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }

                    @Override
                    public void onError(String trace) {
                        Snackbar.make(rootView, getResources().getString(R.string.error_connection), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }).execute();
            }
        };
        //getNot.setmCookies(GeneralInstances.user.getCookies());
        getNot.execute();


        return rootView;
    }

}
