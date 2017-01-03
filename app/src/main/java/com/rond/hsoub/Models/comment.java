package com.rond.hsoub.Models;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Nullsky on 11/20/2016.
 */

public class comment implements Serializable, Cloneable {
    //public String postId;//the topic ID
    private String commentId;//the current comment ID
    private String parentId;//ID of parent comment
    private String userName;
    private String userLink;
    private String time;
    private String votesNumber;
    private int level;
    private boolean isMainPost;
    private String userImg;
    private String content;
    private ArrayList<comment> comments;//replies on this comment

    @Override
    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch( CloneNotSupportedException e )
        {
            return null;
        }
    }
    public comment(String commentId, String parentId, String userName, String userLink, String time, String votesNumber, ArrayList<comment> comments, boolean isMainPost, String userImg, String content, int level) {
        this.commentId = commentId;
        this.parentId = parentId;
        this.userName = userName;
        this.userLink = userLink;
        this.time = time;
        this.votesNumber = votesNumber;
        this.comments = comments;
        this.isMainPost = isMainPost;
        this.userImg = userImg;
        this.content = content;
        this.level = level;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLink() {
        return userLink;
    }

    public void setUserLink(String userLink) {
        this.userLink = userLink;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVotesNumber() {
        return votesNumber;
    }

    public void setVotesNumber(String votesNumber) {
        this.votesNumber = votesNumber;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isMainPost() {
        return isMainPost;
    }

    public void setMainPost(boolean mainPost) {
        isMainPost = mainPost;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<comment> comments) {
        this.comments = comments;
    }
}
