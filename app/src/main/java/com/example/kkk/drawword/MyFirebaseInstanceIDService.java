package com.example.kkk.drawword;

import android.app.Service;
import android.os.Handler;
import android.util.Log;

import com.example.kkk.drawword.Activity.GameActivity;
import com.example.kkk.drawword.Okhttp.OkhttpToken;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by KKK on 2017-09-24.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "TOKEN___";
//    Database database = new Database(getApplicationContext(), "user_db", null,1);
//    IntentClass intentClass = new IntentClass();
//    String iden = database.show_id("select * from user_token", 0, 1);
    String iden = GameActivity.user_iden_static;
    String id = GameActivity.user_name_static;
    String refreshedToken;
    OkhttpToken okhttpToken = new OkhttpToken();
    @Override
    public void onTokenRefresh() {
// Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token~~~~: " + refreshedToken);
        Log.d("iden1111~~!","asdf");
        Log.d("iden~~!",String.valueOf(iden)+"asdf");
        Log.d("TOKEN~~!",refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }
    private void sendRegistrationToServer(String token) {
// TODO: Implement this method to send token to your app server.

        okhttpToken.execute("1", iden,id,token);
    }
}
