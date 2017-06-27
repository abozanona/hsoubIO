package com.rond.hsoub.API;

/**
 * Created by Nullsky on 11/23/2016.
 */

public class JsonLinks {
    static String web_hsoub="https://io.hsoub.com/";
    public static String web_about_hsoub="https://io.hsoub.com/";
    public static String web_account="https://io.hsoub.com/account";

    static String web_hsoub_accounts="https://accounts.hsoub.com/login?source=io";
    public static String web_userInfo(String username){
        return "https://io.hsoub.com/u/" + username;
    }

    static String web_post_link(String community, String postId){
        return "https://io.hsoub.com/" + community + "/" + postId;
    }


    public static String register = "https://accounts.hsoub.com/register";
    public static String resetPassword = "https://accounts.hsoub.com/reset_password";
    static String login = "https://accounts.hsoub.com/api/login";
    static String remember = "https://accounts.hsoub.com/api/remember";
    static String login_token(String token){
        return "https://io.hsoub.com/login?t=" + token;
    }
    static String postUpvote(String postId){
        return "https://io.hsoub.com/posts/" + postId + "/upvote.json";
    }
    static String postDownvote(String postId){
        return "https://io.hsoub.com/posts/" + postId + "/downvote.json";
    }
    static String commentUpvote(String postId, String commentId){
        return "https://io.hsoub.com/posts/" + postId + "/comments/" + commentId + "/upvote.json";
    }
    static String commentDownvote(String postId, String commentId){
        return "https://io.hsoub.com/posts/" + postId + "/comments/" + commentId + "/downvote.json";
    }

    public static String notifications = "https://io.hsoub.com/u/account_stats.json";
    public static String get_more_posts(String community){
        if(community==null || community.equals(""))
            return "https://io.hsoub.com/load_more.json";
        else
            return "https://io.hsoub.com/" + community + "/load_more.json";
    }
    public static String mostPopular = "https://io.hsoub.com/load_more.json";
    public static String recent = "https://io.hsoub.com/new/load_more.json";
    public static String topDay = "https://io.hsoub.com/top/day/load_more.json";
    public static String topWeek = "https://io.hsoub.com/top/week/load_more.json";
    public static String topMonth = "https://io.hsoub.com/top/month/load_more.json";
    public static String topYear = "https://io.hsoub.com/top/year/load_more.json";
    public static String discover = "https://io.hsoub.com/top/discover/load_more.json";
    //// TODO: 12/30/2016 seek for it's link
    public static String explore = "";
    static String get_more_communities="https://io.hsoub.com/communities/load_more.json";
    public static String searchLink(String query){
        return "https://io.hsoub.com/search?utf8=%E2%9C%93&s=" + query;
    }
}
