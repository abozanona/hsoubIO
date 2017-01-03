package com.rond.hsoub.API;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nullsky on 12/23/2016.
 */

public class WebService extends AsyncTask<Void, Void, Void> {
    private final String COOKIES_HEADER = "Set-Cookie";

    private OnWebServiceDoneListener mOnTaskDoneListener;
    private String mURLStr = "";
    private ConnectionMethod mConnectionMethod;
    private CookieManager mCookieManager;
    private String mJSONResponce;
    private HashMap<String, String> mRequestProperties;
    private boolean mIsRequestSucceded;
    private Object mPOSTContent;

    public WebService(String url, ConnectionMethod connectionMethod, CookieManager cookieManager, HashMap<String, String> RequestProperties, Object POSTContent, OnWebServiceDoneListener onTaskDoneListener) {
        mURLStr = url;
        mOnTaskDoneListener = onTaskDoneListener;
        mConnectionMethod = connectionMethod;
        if(cookieManager==null) {
            mCookieManager = new CookieManager();
            mCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        }
        else
            mCookieManager = cookieManager;
        if(RequestProperties == null)
            mRequestProperties = new HashMap<>();
        else
            mRequestProperties = RequestProperties;
        mIsRequestSucceded = true;
        if(POSTContent==null)
            mPOSTContent="";
        else
            mPOSTContent = POSTContent;
    }


    @Override
    protected Void doInBackground(Void... params) {
        try {

            URL mUrl = new URL(mURLStr);
            HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
            connection.setRequestMethod(mConnectionMethod.toString());
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("charset", "utf-8");
            if (mConnectionMethod.equalsName(ConnectionMethod.POST.toString())){
                connection.setDoOutput(true);
                connection.setDoInput(true);
            }
            //connection.setUseCaches(false);
            //connection.setAllowUserInteraction(false);
            //connection.setDoInput(true);
            //connection.setDoOutput(true);
            connection.setConnectTimeout(100000);
            connection.setReadTimeout(100000);

                //How to handle cookies in httpUrlConnection using cookieManager
                //http://stackoverflow.com/a/16171708/4614264
                if (mCookieManager.getCookieStore().getCookies().size() > 0)
                    // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                    connection.setRequestProperty("Cookie",
                            TextUtils.join(";",  mCookieManager.getCookieStore().getCookies()));

            for (Map.Entry<String, String> entry : mRequestProperties.entrySet())
                if(entry.getKey()!=null && entry.getValue()!=null)
                    connection.setRequestProperty(entry.getKey(), entry.getValue());

            connection.connect();

            trace = 1 + "";

            //http://stackoverflow.com/a/13912226/4614264
            if(mConnectionMethod.equalsName(ConnectionMethod.POST.toString())) {
                OutputStream os = connection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(mPOSTContent.toString());
                osw.flush();
                osw.close();
            }

            trace = 2 + "";

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                //Get cookies
                Map<String, List<String>> headerFields = connection.getHeaderFields();
                List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
                if (cookiesHeader != null)
                    for (String cookie : cookiesHeader)
                        mCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));

                //Get Response JSON
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                mJSONResponce = sb.toString();
                //return sb.toString();
                trace = 3 + "";
            }
            else
                mIsRequestSucceded = false;

            //trace = 4;
        } catch (Exception ex) {
            mIsRequestSucceded = false;
            //ex.printStackTrace();
            trace = 5 + " ->" + ex.getMessage() + "<-";
        }
        return null;
    }


    private String trace = 200 + "";
    @Override
    protected void onPostExecute(Void s) {
        super.onPostExecute(s);
    if(mOnTaskDoneListener == null )
        return;

        if (mIsRequestSucceded)
            mOnTaskDoneListener.onTaskDone(new WebServiceResponse(mCookieManager, mJSONResponce));
        else
            mOnTaskDoneListener.onError(trace + "");
    }
}
