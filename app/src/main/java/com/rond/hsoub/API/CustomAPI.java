package com.rond.hsoub.API;

import android.os.AsyncTask;

import com.jsoup.Jsoup;
import com.jsoup.nodes.Document;
import com.jsoup.nodes.Element;
import com.jsoup.select.Elements;
import com.rond.hsoub.API.APIModules.User;
import com.rond.hsoub.Models.comment;
import com.rond.hsoub.Models.community;
import com.rond.hsoub.Models.postListItem;
import com.rond.hsoub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nullsky on 12/23/2016.
 */

public abstract class CustomAPI extends AsyncTask<String, Void, String> {

    private final String CSRF_Tocken_Patters = "<meta name=\"csrf-token\" content=\"([^\"]+)\"";
    protected CustomAPI(CustomAPIEnum customAPIEnum){
        mCustomAPIEnum = customAPIEnum;
    }

    private CookieManager mCookies;

    //// TODO: 12/26/2016 check this later
    //There's an error with the latest release version of jsoup
    //https://github.com/jhy/jsoup/issues/211
    //moved to earlier version
    //// TODO: 12/26/2016 jar file didn't work, check it later

    private CustomAPIEnum mCustomAPIEnum;
    private void login(final String username, final String password){
        final boolean[] loginSucceeded = {true};
        final User[] user = new User[1];

        new WebService(JsonLinks.web_hsoub_accounts, ConnectionMethod.GET, null, null, null, new OnWebServiceDoneListener(){
            @Override
            public void onTaskDone(WebServiceResponse responseData) {
                String loginJson = "{\"user\":{\"email\":\"" + username + "\",\"password\":\"" + password + "\"}}";
                new WebService(JsonLinks.login, ConnectionMethod.POST, responseData.getCookieManager(), null, loginJson, new OnWebServiceDoneListener(){
                    @Override
                    public void onTaskDone(WebServiceResponse responseData) {
                        try {
                            JSONObject userJson = new JSONObject(responseData.getJSONResponce());
                            if(userJson.has("error")){
                                loginSucceeded[0] = false;
                                loginListener(null, "-0");
                                return;
                            }
                            user[0] = new User(userJson);
                        } catch (JSONException e) {
                            loginSucceeded[0] = false;
                            loginListener(null, "1");
                            return;
                        }

                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("Authorization", "Bearer " + user[0].getToken());
                        if(!loginSucceeded[0]){
                            loginListener(null, "2");
                            return;
                        }
                        new WebService(JsonLinks.remember, ConnectionMethod.POST, responseData.getCookieManager(), hm, null, new OnWebServiceDoneListener(){
                            @Override
                            public void onTaskDone(WebServiceResponse responseData) {
                                JSONObject rememberJson;
                                try {
                                    rememberJson = new JSONObject(responseData.getJSONResponce());
                                    if(rememberJson.has("error")){
                                        loginSucceeded[0] = false;
                                        loginListener(null, "3");
                                        return;
                                    }
                                } catch (JSONException e) {
                                    loginSucceeded[0] = false;
                                    loginListener(null, "4");
                                    return;
                                }

                                if(!loginSucceeded[0]){
                                    loginListener(null, "5");
                                    return;
                                }
                                new WebService(JsonLinks.login_token(user[0].getLogin_token()), ConnectionMethod.GET, responseData.getCookieManager(), null, null, new OnWebServiceDoneListener(){
                                    @Override
                                    public void onTaskDone(WebServiceResponse responseData) {
                                        user[0].setCookies(responseData.getCookieManager());

                                        Pattern pattern = Pattern.compile(CSRF_Tocken_Patters);
                                        Matcher matcher = pattern.matcher(responseData.getJSONResponce());
                                        if(matcher.find())
                                            user[0].setX_CSRF_Token(matcher.group(1));

                                        // => url: '\/u\/(.+)',
                                        pattern = Pattern.compile("url: '\\/u\\/(.+)',");
                                        matcher = pattern.matcher(responseData.getJSONResponce());
                                        if(matcher.find())
                                            user[0].setUserLink(matcher.group(1));


                                        if(loginSucceeded[0])
                                            loginListener(user[0], "");
                                        else
                                            loginListener(null, "");
                                    }

                                    @Override
                                    public void onError(String x) {
                                        loginListener(null, "6");
                                    }
                                }).execute();
                            }

                            @Override
                            public void onError(String x) {
                                loginListener(null, "7");
                            }
                        }).execute();

                    }

                    @Override
                    public void onError(String x) {
                        loginListener(null, "8->" + x);
                    }
                }).execute();
            }
            @Override
            public void onError(String trace) {
                loginListener(null, "0.00");
            }
        }).execute();

    }
    public void loginListener(User user, String trace){}

    private void getLatestCommunities(final String json){
        new WebService(JsonLinks.web_hsoub, ConnectionMethod.GET, null, null, null, new OnWebServiceDoneListener() {
            @Override
            public void onTaskDone(WebServiceResponse responseData) {
                Pattern pattern = Pattern.compile(CSRF_Tocken_Patters);
                Matcher matcher = pattern.matcher(responseData.getJSONResponce());
                if(!matcher.find()){
                    getLatestCommunitiesListener(null);
                    return;
                }
                HashMap<String, String> hm = new HashMap<>();
                hm.put("X-CSRF-Token", matcher.group(1));
                new WebService(JsonLinks.get_more_communities, ConnectionMethod.POST, responseData.getCookieManager(), hm, json, new OnWebServiceDoneListener() {
                    @Override
                    public void onTaskDone(WebServiceResponse responseData) {
                        String posts;
                        try {
                            JSONObject postsObj = new JSONObject(responseData.getJSONResponce());
                            posts = postsObj.getString("content");
                        } catch (JSONException e) {
                            getLatestCommunitiesListener(null);
                            return;
                        }

                        ArrayList<community> Communities = new ArrayList<>();
                        try {
                            Document doc = Jsoup.parse(posts);
                            Elements postListItem = doc.select(".communityCard");
                            for (int i = 0; i < postListItem.size(); i++) {
                                try {
                                    Element e = postListItem.get(i);
                                    String name = e.select("a").text();
                                    String ID = e.select("a").attr("href").substring(1);
                                    String description = e.select("p").text();
                                    String followers = e.select(".communityFollower").text();
                                    String number = e.attr("id").substring(10);

                                    community com = new community(name, ID, description, false, followers, number);
                                    Communities.add(com);
                                } catch (Exception e){
                                    //Silence is golden
                                }
                            }
                        } catch (Exception e){
                            //Silence is golden
                        }
                        getLatestCommunitiesListener(Communities);
                    }

                    @Override
                    public void onError(String trace) {
                        getLatestPostsDefaultListener(null);
                    }
                }).execute();
            }

            @Override
            public void onError(String trace) {
                getLatestCommunitiesListener(null);
            }
        }).execute();
    }
    public void getLatestCommunitiesListener(ArrayList<community> Communities){}

    private void getLatestPostsDefault(final String json, final String url){
        new WebService(JsonLinks.web_hsoub, ConnectionMethod.GET, null, null, null, new OnWebServiceDoneListener() {
            @Override
            public void onTaskDone(WebServiceResponse responseData) {

                Pattern pattern = Pattern.compile(CSRF_Tocken_Patters);
                Matcher matcher = pattern.matcher(responseData.getJSONResponce());
                if(!matcher.find()){
                    getLatestPostsDefaultListener(null);
                    return;
                }
                HashMap<String, String> hm = new HashMap<>();
                hm.put("X-CSRF-Token", matcher.group(1));
                new WebService(url, ConnectionMethod.POST, responseData.getCookieManager(), hm, json, new OnWebServiceDoneListener() {
                    @Override
                    public void onTaskDone(WebServiceResponse responseData) {
                        String posts;
                        try {
                            JSONObject postsObj = new JSONObject(responseData.getJSONResponce());
                            posts = postsObj.getString("content");
                        } catch (JSONException e) {
                            getLatestPostsDefaultListener(null);
                            return;
                        }

                        //// TODO: 12/26/2016 remove this later
                        ArrayList<postListItem> PostListItem = new ArrayList<>();
                        try {
                            Document doc = Jsoup.parse(posts);
                            Elements postListItem = doc.select(".listItem");
                            for (int i = 0; i < postListItem.size(); i++) {
                                try {
                                    Element e = postListItem.get(i);
                                    Elements ee = e.select("li");
                                    String postID = e.attr("id").substring(5);
                                    String link =(e.select(".post_image")==null)?"":e.select(".post_image").select("a").attr("href");
                                    String votes = e.select(".post_points").text();
                                    String topicTitle = e.select("h2").text();
                                    String userName = e.select("a.usr26").attr("slug");
                                    String userID = e.select("a.usr26").attr("href").substring(3);
                                    String topicID = ee.get(1).select("a").attr("href").substring(1);
                                    String topicType = ee.get(1).text();
                                    String commentsNumber = e.select(".commentsCounter").text();
                                    String time = ee.get(2).text();
                                    String userImg = e.select(".usr26 img").attr("src").replaceAll("([0-9]+)$", "240");
                                    postListItem post = new postListItem(postID, topicID, topicTitle, userName, userID, topicType, commentsNumber, time, null, votes, userImg);
                                    post.setLink(link);
                                    PostListItem.add(post);
                                } catch (Exception e){
                                    //Silence is golden
                                }
                            }
                        } catch (Exception e){
                            //Silence is golden
                        }
                        getLatestPostsDefaultListener(PostListItem);
                    }

                    @Override
                    public void onError(String trace) {
                        getLatestPostsDefaultListener(null);
                    }
                }).execute();
            }

            @Override
            public void onError(String trace) {
                getLatestPostsDefaultListener(null);
            }
        }).execute();
    }
    public void getLatestPostsDefaultListener(ArrayList<postListItem> PostListItem){}

    private void getNewCSRF_Tocken(){
        if(mCookies==null)
            getNewCSRF_TockenListner(null);
        new WebService(JsonLinks.web_hsoub, ConnectionMethod.GET, mCookies, null, null, new OnWebServiceDoneListener() {
            @Override
            public void onTaskDone(WebServiceResponse responseData) {
                Pattern pattern = Pattern.compile(CSRF_Tocken_Patters);
                Matcher matcher = pattern.matcher(responseData.getJSONResponce());
                if(matcher.find())
                    getNewCSRF_TockenListner(matcher.group(1));
                getNewCSRF_TockenListner(null);
            }

            @Override
            public void onError(String trace) {
                getNewCSRF_TockenListner(null);
            }
        }).execute();
    }
    public void getNewCSRF_TockenListner(String CSRF){

    }

    private void getPost(String communityId, String postId){
        new WebService(JsonLinks.web_post_link(communityId, postId), ConnectionMethod.GET, null, null, null, new OnWebServiceDoneListener() {
            @Override
            public void onTaskDone(WebServiceResponse responseData) {
                ArrayList<comment> allComments = new ArrayList<>();
                String title = "";
                try {
                    Document doc = Jsoup.parse(responseData.getJSONResponce());
                    //Elements comments = doc.select(".comment");
                    Elements comments = doc.select(".idthing");
                    title = doc.select(".articleTitle").text();
                    for (int i = 0; i < comments.size(); i++) {
                        try {
                            Element e = comments.get(i);

                            String commentId = e.attr("id").substring(8);

                            String parentId = "";

                            String userName = e.select(".usr26").get(0).text();

                            String userLink = e.select(".usr26").get(0).attr("href").substring(3);

                            String time = e.select(".pull-right").select("span").get(0).text();

                            String votesNumber = e.select(".post_points").get(0).text();

                            String userImgLink = e.select(".usr26").get(0).select("img").attr("src").replaceAll("s=([0-9]+)", "s=240");

                            String content;
                            /*
                            if(e.select(".commentContent")!=null)
                                content = e.select(".commentContent").get(0).text();
                            else
                                content = e.select(".articleBody").get(0).text();
                            */
                            content = e.select(".post_content").get(0).text();

                            int level = 0;
                            String _level = e.attr("class");

                            Pattern pattern = Pattern.compile(" lvl-([0-9]+) ");
                            Matcher matcher = pattern.matcher(_level);
                            if(matcher.find())
                                level = Integer.parseInt(matcher.group(1));


                            comment com = new comment(commentId, parentId, userName, userLink, time, votesNumber, new ArrayList<comment>(), false, userImgLink, content, level);
                            allComments.add(com);
                        } catch (Exception e){
                            // TODO: 12/29/2016
                            //Silence is golden
                        }
                    }
                } catch (Exception e){
                    // TODO: 12/29/2016
                    //Silence is golden
                }
                if(allComments.size()<2){
                    getPostListener(title, allComments, allComments);
                    return;
                }
                if(allComments.size()!=0)
                    allComments.get(0).setMainPost(true);

                ArrayList<comment>tempArrangement = new ArrayList<>();
                for(int i=0;i<allComments.size();i++) {
                    tempArrangement.add((comment) allComments.get(i).clone());
                }

                ArrayList<comment>finalArrangement = new ArrayList<>();
                finalArrangement.add(allComments.get(0));
                finalArrangement.add(allComments.get(1));
                for(int i=2;i<allComments.size();i++){
                    comment temp =allComments.get(i);
                    if(temp.getLevel()==0)
                        finalArrangement.add(temp);
                    else
                        for(int j=i-1;j>=0;j--)
                            if(allComments.get(j).getLevel()<temp.getLevel()){
                                allComments.get(j).getComments().add(temp);
                                break;
                            }
                }
                getPostListener(title, finalArrangement, tempArrangement);
            }

            @Override
            public void onError(String trace) {
                getPostListener("لا يمكن عرض المحتوى", null, null);
            }
        }).execute();
    }
    public void getPostListener(String title, ArrayList<comment> nestedComments, ArrayList<comment> normalComments){

    }


    @Override
    protected String doInBackground(String... params) {
        // TODO: 12/29/2016 handle array length exceptions
        if(mCustomAPIEnum == CustomAPIEnum.Login) {
            login(params[0], params[1]);
        }
        else if(mCustomAPIEnum == CustomAPIEnum.getLatestPostsDefault) {
            getLatestPostsDefault(params[0], params[1]);
        }
        else if(mCustomAPIEnum == CustomAPIEnum.getNewCSRF_Tocken_Patters) {
            getNewCSRF_Tocken();
        }
        else if(mCustomAPIEnum == CustomAPIEnum.getLatestCommunities) {
            getLatestCommunities(params[0]);
        }
        else if(mCustomAPIEnum == CustomAPIEnum.getPost){
            getPost(params[0], params[1]);
        }
        return "";
    }

    public void setmCookies(CookieManager mCookies) {
        this.mCookies = mCookies;
    }
}
