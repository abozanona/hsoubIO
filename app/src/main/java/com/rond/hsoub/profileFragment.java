package com.rond.hsoub;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsoup.Jsoup;
import com.jsoup.nodes.Document;
import com.jsoup.select.Elements;
import com.rond.hsoub.API.ConnectionMethod;
import com.rond.hsoub.API.DownloadImageTask;
import com.rond.hsoub.API.JsonLinks;
import com.rond.hsoub.API.OnWebServiceDoneListener;
import com.rond.hsoub.API.WebService;
import com.rond.hsoub.API.WebServiceResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileFragment extends Fragment {


    public profileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String userName = getArguments().getString("userName");
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        new WebService(JsonLinks.web_userInfo(userName), ConnectionMethod.GET, null, null, null, new OnWebServiceDoneListener() {
            @Override
            public void onTaskDone(WebServiceResponse responseData) {
                Pattern pattern;
                Matcher matcher;

                String userName, fullname, bio, rep, imgURL;

                //=> <span.*class.*full_name.*>(.*)<\/span>
                pattern = Pattern.compile("<span.*class.*full_name.*>(.*)<\\/span>");
                matcher = pattern.matcher(responseData.getJSONResponce());
                if(matcher.find())
                    fullname = matcher.group(1);
                else
                    fullname = "Can't get user name";

                //=> <a.*class.*username.*>(.*)<\/a>
                pattern = Pattern.compile("<a.*class.*username.*>(.*)<\\/a>");
                matcher = pattern.matcher(responseData.getJSONResponce());
                if(matcher.find())
                    userName = "(" + matcher.group(1) + ")";
                else
                    userName = " (...)";

                //=> fa fa-star.+<b>(.*)<\/b>
                pattern = Pattern.compile("fa fa-star.+<b>(.*)<\\/b>");
                matcher = pattern.matcher(responseData.getJSONResponce());
                if(matcher.find())
                    rep = matcher.group(1);
                else
                    rep = "X";

                Document doc = Jsoup.parse(responseData.getJSONResponce());

/*
                //=> <p>((.|\n)*)<\/p>
                pattern = Pattern.compile("<p>((.|\\n)*)<\\/p>");
                matcher = pattern.matcher(responseData.getJSONResponce());
                if(matcher.find())
                    bio = matcher.group(1);
                else
                    bio = "X";
*/
                Elements Ps = doc.select("p");
                bio = Ps.get(0).text();

                //=> <img.*src=['"](.+)["'].*alt="profile-image
                pattern = Pattern.compile("<img.*src=['\"](.+)[\"'].*alt=\"profile-image");
                matcher = pattern.matcher(responseData.getJSONResponce());
                if(matcher.find())
                    imgURL = matcher.group(1);
                else
                    imgURL = "";

                CircleImageView profile_image = (CircleImageView)rootView.findViewById(R.id.profile_image);
                TextView lblUserName = (TextView)rootView.findViewById(R.id.lblUserName);
                TextView lblBio = (TextView)rootView.findViewById(R.id.lblBio);
                TextView lblRep = (TextView)rootView.findViewById(R.id.lblRep);


                if(!imgURL.equals(""))
                    new DownloadImageTask(profile_image)
                            .execute(imgURL);
                lblUserName.setText(fullname + userName);
                lblBio.setText(bio);
                lblRep.setText(rep);

            }

            @Override
            public void onError(String trace) {
                Snackbar.make(rootView, getResources().getString(R.string.error_connection), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //// TODO: 12/29/2016 load user info from database,,,,,, or maybe don't, Not very important
            }
        }).execute();

        return rootView;
    }

}
