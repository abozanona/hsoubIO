package com.rond.hsoub.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nullsky on 12/24/2016.
 */

public class notification {
    public static int unread_notifications_count;

    public notification(String notificationJson) {
        try {
            JSONObject obj = new JSONObject(notificationJson);

            id = obj.getString("id").substring(obj.getString("id").indexOf("-")+1);

            text = obj.getJSONObject("content").getString("text");

            // => \/.+\/([0-9]+)-
            String str = obj.getJSONObject("content").getString("url");
            Pattern p1 = Pattern.compile("\\/.+\\/([0-9]+)-");
            Matcher m1 = p1.matcher(str);
            if(m1.find())
                topicid = m1.group(1);

            Pattern p2 = Pattern.compile("#.+-([0-9]+)");
            Matcher m2 = p2.matcher(str);
            if(m2.find())
                commentid = m2.group(1);

            highlight = obj.getBoolean("highlight");

            date = obj.getString("date");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String id;
    private String text;
    private String topicid;
    private String commentid;
    private boolean highlight;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
