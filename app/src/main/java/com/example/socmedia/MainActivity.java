package com.example.socmedia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private LoginButton fb;
    private CallbackManager cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv=findViewById(R.id.tv);
        fb=findViewById(R.id.login_buttonfb);
        cb= CallbackManager.Factory.create();
        fb.setPermissions(Arrays.asList("email","user_birthday"));
        fb.registerCallback(cb, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        cb.onActivityResult(requestCode,resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    AccessTokenTracker t = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if( currentAccessToken == null){
                tv.setText("");
                Toast.makeText(MainActivity.this,"logout", Toast.LENGTH_SHORT).show();
            }
            else{
                loaduserProfile(currentAccessToken);
            }
        }
    };

    private void loaduserProfile( AccessToken newAccessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if(object != null)
                {
                    try{
                        String email = object.getString("email");
                        String id=  object.getString("id");
                        fb.setText(email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields","email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }
}