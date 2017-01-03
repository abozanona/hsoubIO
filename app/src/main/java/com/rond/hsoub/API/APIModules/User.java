package com.rond.hsoub.API.APIModules;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.CookieManager;

/**
 * Created by Nullsky on 12/23/2016.
 */

public class User implements Serializable {
    public User(JSONObject user){
        try {
            if (user.has("email"))
                email = user.getString("email");
            if (user.has("birth_date"))
                birth_date = user.getString("birth_date");
            if (user.has("gender"))
                gender = user.getString("gender");
            if (user.has("first_name"))
                first_name = user.getString("first_name");
            if (user.has("last_name"))
                last_name = user.getString("last_name");
            if (user.has("token"))
                token = user.getString("token");
            if (user.has("avatar"))
                avatar = user.getString("avatar");
            if (user.has("login_token"))
                login_token = user.getString("login_token");

        }
        catch (JSONException e) {
            //Exception should never occur, or Should it??
            e.printStackTrace();
        }
    }
    private String email;
    private String userLink;
    private String birth_date;
    private String gender;
    private String first_name;
    private String last_name;
    private String token;
    private String avatar;
    private String login_token;
    private CookieManager mCookieManager;
    private String X_CSRF_Token;

    public String getX_CSRF_Token() {
        return X_CSRF_Token;
    }

    public void setX_CSRF_Token(String x_CSRF_Token) {
        X_CSRF_Token = x_CSRF_Token;
    }

    public CookieManager getCookies() {
        return mCookieManager;
    }

    public void setCookies(CookieManager mCookieManager) {
        this.mCookieManager = mCookieManager;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserLink() {
        return userLink;
    }

    public void setUserLink(String userLink) {
        this.userLink = userLink;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLogin_token() {
        return login_token;
    }

    public void setLogin_token(String login_token) {
        this.login_token = login_token;
    }
}
