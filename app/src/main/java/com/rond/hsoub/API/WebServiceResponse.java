package com.rond.hsoub.API;

import java.net.CookieManager;

/**
 * Created by Nullsky on 12/23/2016.
 */

public class WebServiceResponse {
    private CookieManager mCookieManager;
    private String mJSONResponce;

    WebServiceResponse(CookieManager CookieManager, String JSONResponce) {
        this.mCookieManager = CookieManager;
        this.mJSONResponce = JSONResponce;
    }

    CookieManager getCookieManager() {
        return mCookieManager;
    }

    public String getJSONResponce() {
        return mJSONResponce;
    }
}
