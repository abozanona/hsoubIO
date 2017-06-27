package com.rond.hsoub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.rond.hsoub.API.APIModules.User;
import com.rond.hsoub.API.ConnectionMethod;
import com.rond.hsoub.API.CustomAPI;
import com.rond.hsoub.API.CustomAPIEnum;
import com.rond.hsoub.API.JsonLinks;
import com.rond.hsoub.API.OnWebServiceDoneListener;
import com.rond.hsoub.API.WebService;
import com.rond.hsoub.API.WebServiceResponse;
import com.rond.hsoub.Classes.GeneralInstances;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText txtUserName;
    private EditText txtPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        //// TODO: 12/24/2016 must store login info in database
        /*
        SharedPreferences sharedPref = getSharedPreferences("hsoub", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("login", false);
        if(isLoggedIn){
            Intent intent=new Intent(getApplicationContext(), Main2Activity.class);
            startActivity(intent);
        }
        */

        // Address the email and password field
        txtUserName = (EditText) findViewById(R.id.txtusername);
        txtPassWord = (EditText) findViewById(R.id.txtpassword);

        findViewById(R.id.lblSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = JsonLinks.register;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        findViewById(R.id.lblForgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = JsonLinks.resetPassword;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        findViewById(R.id.viewMainFragnt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user is not logged in
                SharedPreferences sharedPref = getSharedPreferences("hsoub", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("login", false);
                editor.apply();

                Intent intent=new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void checkLogin(View arg0) {


        txtUserName.setError(null);
        txtPassWord.setError(null);
        final String email = txtUserName.getText().toString();
        if (!isValidEmail(email)) {
            //Set error message for email field
            txtUserName.setError(getResources().getString(R.string.error_email));
            return;
        }

        final String pass = txtPassWord.getText().toString();
        if (!isValidPassword(pass)) {
            //Set error message for password field
            txtPassWord.setError(getResources().getString(R.string.error_password));
            return;
        }

        final ProgressDialog loginProgress = ProgressDialog.show(this, getResources().getString(R.string.dialog_login_title),getResources().getString(R.string.dialog_login_body), true);
        new CustomAPI(CustomAPIEnum.Login){
            @Override
            public void loginListener(User user, String trace) {
                loginProgress.dismiss();
                if(user == null){
                    txtUserName.setError(getResources().getString(R.string.error_login));
                    txtPassWord.setError(getResources().getString(R.string.error_login));
                } else {


                    /*
                    // TODO: 12/24/2016 Complete debugging here
                    // TODO: 12/28/2016 Check for login session cookies
                    if (user.getCookies().getCookieStore().getCookies().size() > 0) {
                        String str = TextUtils.join(";", user.getCookies().getCookieStore().getCookies());
                    }
                    */
                    HashMap<String,String>hm = new HashMap<>();
                    hm.put("Accept", "*;q=0.5, text/javascript, application/javascript, application/ecmascript, application/x-ecmascript");
                    hm.put("X-CSRF-Token", user.getX_CSRF_Token());
                    hm.put("X-Requested-With", "XMLHttpRequest");
                    new WebService("https://io.hsoub.com/posts/54117/upvote.json", ConnectionMethod.POST, user.getCookies(), hm, null, new OnWebServiceDoneListener() {
                        @Override
                        public void onTaskDone(WebServiceResponse responseData) {
                            String str = responseData.getJSONResponce();
                            Log.d("errrrrT", str);;
                        }

                        @Override
                        public void onError(String trace) {
                            Log.d("errrrrF", trace);
                        }
                    }).execute();


                    GeneralInstances.user = user;

                    GeneralInstances.account_state = new Handler();
                    GeneralInstances.account_state.postDelayed(new Runnable(){
                        public void run(){
                            //// TODO: 12/24/2016 Read account state from API here
                            GeneralInstances.account_state.postDelayed(this, 1000*60*2);
                        }
                    }, 1000*60*2);

                    Intent intent=new Intent(getApplicationContext(), Main2Activity.class);
                    //user is not logged in
                    SharedPreferences sharedPref = getSharedPreferences("hsoub", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("login", true);
                    editor.apply();

                    //// TODO: 12/24/2016 When login succ.. and exiting app, this activity doesn't finish
                    startActivity(intent);
                    finish();
                }
            }
        }.execute(email, pass);

    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    // validating password
    private boolean isValidPassword(String pass) {
        return pass != null && pass.length() >= 4;
    }



}