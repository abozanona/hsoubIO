package com.rond.hsoub.Models;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Nullsky on 11/19/2016.
 */

public class postListItem implements Serializable {
    private String postID;          //2354
    private String topicTitle;      //how to pla pla pla
    private String userName;        //abozanona abozanona
    private String userID;          //abozanona_abozanona
    private String topicID;         //HtmlDev
    private String topicType;       //web development
    private String commentsNumber;  //15
    private String time;            //12/9/1995
    private String votes;           //16
    private String imgLink;         //www.hhh.com/img.png

    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public postListItem(String postID, String topicID, String topicTitle, String userName, String userID, String topicType, String commentsNumber, String time, Bitmap userImg, String votes, String imgLink) {

        this.postID = postID;
        this.topicID = topicID;
        this.topicTitle = topicTitle;
        this.userName = userName;
        this.userID = userID;
        this.topicType = topicType;
        this.commentsNumber = commentsNumber;
        this.time = time;
        this.votes = votes;
        this.imgLink = imgLink;

    }

    public String getTopicID() {
        return topicID;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
    }

    public String getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(String commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
