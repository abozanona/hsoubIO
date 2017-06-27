package com.rond.hsoub.API;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nullsky on 12/23/2016.
 */

public class WebService extends AsyncTask<Void, Void, Void> {
    private final String COOKIES_HEADER = "Set-Cookie";

    public static DefaultHttpClient httpClient;

    private OnWebServiceDoneListener mOnTaskDoneListener;
    private String mURLStr = "";
    private ConnectionMethod mConnectionMethod;
    private CookieManager mCookieManager;
    private String mJSONResponce;
    private HashMap<String, String> mRequestProperties;
    private boolean mIsRequestSucceded;
    private String mPOSTContent;

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    static
    {
        WebService.httpClient = new DefaultHttpClient();


        HttpGet httpget = new HttpGet(JsonLinks.web_hsoub);
        try {
            HttpResponse response = WebService.httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            InputStream instream = null;
            if (entity != null) {
                instream = entity.getContent();
            }
        } catch (IOException e) {
            //todo show error snack here
            e.printStackTrace();
        }
    }
    public WebService(String url, ConnectionMethod connectionMethod, CookieManager cookieManager, HashMap<String, String> RequestProperties, String POSTContent, OnWebServiceDoneListener onTaskDoneListener) {
        mURLStr = url;
        mOnTaskDoneListener = onTaskDoneListener;
        mConnectionMethod = connectionMethod;
        /*
        if(cookieManager==null) {
            mCookieManager = new CookieManager();
            mCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        }
        else
            mCookieManager = cookieManager;
        */
        if(RequestProperties == null)
            mRequestProperties = new HashMap<>();
        else
            mRequestProperties = RequestProperties;

        mIsRequestSucceded = true;
        if(POSTContent==null)
            mPOSTContent = "";
        else
            mPOSTContent = POSTContent;
    }


    private HttpResponse getRedirectUrl(String url){
        HttpGet req = new HttpGet(url);
        req.addHeader("charset", "utf-8");
        req.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0");
        HttpResponse response;
        try {
            response = WebService.httpClient.execute(req);
            if(!req.getURI().toString().equals(url)){
                return getRedirectUrl(req.getURI().toString());
            }
            return response;
        } catch (IOException e) {
            Log.getStackTraceString(e);
            return null;
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpResponse response = null;
        try {

            if (mConnectionMethod.equalsName(ConnectionMethod.GET.toString())) {
                HttpGet req = new HttpGet(mURLStr);
                if(mRequestProperties!=null)
                    for (Map.Entry<String, String> entry : mRequestProperties.entrySet())
                        if(entry.getKey()!=null && entry.getValue()!=null)
                            req.addHeader(entry.getKey(), entry.getValue());
                //req.addHeader("Content-Type", "application/json; charset=utf-8");
                req.addHeader("charset", "utf-8");
                req.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0");
                response = WebService.httpClient.execute(req);
                if(!req.getURI().toString().equals(mURLStr)){
                    response = getRedirectUrl(req.getURI().toString());
                }
                HttpEntity entity = response.getEntity();
                InputStream instream;
                if (entity != null) {
                    instream = entity.getContent();
                    mJSONResponce = convertStreamToString(instream);
                    mIsRequestSucceded = true;
                }
                else{
                    mJSONResponce = "";
                    mIsRequestSucceded = false;
                }
            }
            else if(mConnectionMethod.equalsName(ConnectionMethod.POST.toString())){
                HttpPost req = new HttpPost(mURLStr);
                if(mRequestProperties!=null)
                    for (Map.Entry<String, String> entry : mRequestProperties.entrySet())
                        if(entry.getKey()!=null && entry.getValue()!=null)
                            req.addHeader(entry.getKey(), entry.getValue());
                req.addHeader("Content-Type", "application/json; charset=utf-8");
                req.addHeader("charset", "utf-8");
                req.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0");
                req.setEntity(new StringEntity(mPOSTContent));
                response = WebService.httpClient.execute(req);
                HttpEntity entity = response.getEntity();
                InputStream instream;
                if (entity != null) {
                    instream = entity.getContent();
                    mJSONResponce = convertStreamToString(instream);
                    mIsRequestSucceded = true;
                }
                else{
                    mJSONResponce = "";
                    mIsRequestSucceded = false;
                }
            }
            else if(mConnectionMethod.equalsName(ConnectionMethod.PUT.toString())){
                HttpPut req = new HttpPut(mURLStr);
                if(mRequestProperties!=null)
                    for (Map.Entry<String, String> entry : mRequestProperties.entrySet())
                        if(entry.getKey()!=null && entry.getValue()!=null)
                            req.addHeader(entry.getKey(), entry.getValue());
                req.addHeader("Content-Type", "application/json; charset=utf-8");
                req.addHeader("charset", "utf-8");
                req.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0");
                req.setEntity(new StringEntity(mPOSTContent));
                response = WebService.httpClient.execute(req);
                HttpEntity entity = response.getEntity();
                InputStream instream;
                if (entity != null) {
                    instream = entity.getContent();
                    mJSONResponce = convertStreamToString(instream);
                    mIsRequestSucceded = true;
                }
                else{
                    mJSONResponce = "";
                    mIsRequestSucceded = false;
                }
            }
            else if(mConnectionMethod.equalsName(ConnectionMethod.DELETE.toString())){
                HttpDelete req = new HttpDelete(mURLStr);
                if(mRequestProperties!=null)
                    for (Map.Entry<String, String> entry : mRequestProperties.entrySet())
                        if(entry.getKey()!=null && entry.getValue()!=null)
                            req.addHeader(entry.getKey(), entry.getValue());
                req.addHeader("Content-Type", "application/json; charset=utf-8");
                req.addHeader("charset", "utf-8");
                req.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0");
                response = WebService.httpClient.execute(req);
                HttpEntity entity = response.getEntity();
                InputStream instream;
                if (entity != null) {
                    instream = entity.getContent();
                    mJSONResponce = convertStreamToString(instream);
                    mIsRequestSucceded = true;
                }
                else{
                    mJSONResponce = "";
                    mIsRequestSucceded = false;
                }


            }
            int no = response.getStatusLine().getStatusCode();
            no++;
            /*
            HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
            connection.setRequestMethod(mConnectionMethod.toString());
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0");
            connection.setFollowRedirects(true);
            connection.setInstanceFollowRedirects(true);
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
                    connection.setRequestProperty("Cookie",
                            TextUtils.join(";",  mCookieManager.getCookieStore().getCookies()));

            for (Map.Entry<String, String> entry : mRequestProperties.entrySet())
                if(entry.getKey()!=null && entry.getValue()!=null)
                    connection.setRequestProperty(entry.getKey(), entry.getValue());

            //Map<String, List<String>> x = connection.getHeaderFields();
            //Map<String, List<String>> y = connection.getRequestProperties();

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

            //x = connection.getHeaderFields();
            //y = connection.getRequestProperties();

            //String Location = connection.getHeaderField("Location");;
            //if(Location!=null)
            //    Log.d("Location", Location);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //Get cookies
                Map<String, List<String>> headerFields = connection.getHeaderFields();
                List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
                if (cookiesHeader != null)
                    for (String cookie : cookiesHeader)
                        mCookieManager.getCookieStore().add(new URI(mUrl.getHost()), HttpCookie.parse(cookie).get(0));

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
            else {
                mIsRequestSucceded = false;
            }
*/
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
