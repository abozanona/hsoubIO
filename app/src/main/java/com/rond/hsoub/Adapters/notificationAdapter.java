package com.rond.hsoub.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rond.hsoub.Models.notification;
import com.rond.hsoub.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nullsky on 12/24/2016.
 */

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.notificationHolder> {
    private ArrayList<notification> mNotification = new ArrayList<>();

    public notificationAdapter(ArrayList<notification> notification){
        mNotification=notification;
    }

    @Override
    public notificationAdapter.notificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new notificationHolder(view);
    }

    @Override
    public void onBindViewHolder(final notificationAdapter.notificationHolder holder, int position) {

        notification obj = mNotification.get(position);
        holder.lblDate.setText(obj.getDate());
        holder.lblText.setText(obj.getText());
        holder.lytHighlight.setBackgroundColor(obj.isHighlight()? Color.parseColor("#CCCCCC"):Color.parseColor("#FFFFFF"));

        holder.lytHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    static class notificationHolder extends RecyclerView.ViewHolder{

        TextView lblText, lblDate;
        CircleImageView imgUser;
        View lytHighlight;
        notificationHolder(View itemView) {

            super(itemView);
            lblText = (TextView)itemView.findViewById(R.id.lblText);
            lblDate = (TextView)itemView.findViewById(R.id.lblDate);
            imgUser = (CircleImageView)itemView.findViewById(R.id.imgUser);
            lytHighlight = itemView.findViewById(R.id.lytHighlight);
        }
    }
}
