package com.example.kkk.drawword;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.kkk.drawword.Activity.GameActivity;
import com.example.kkk.drawword.Okhttp.OkhttpFriend;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KKK on 2017-09-24.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "TEST";
    Database database = new Database(this, "user_db", null,1);
    String fcm_ment = "";
    String user_name = "";
    String room_num = "";
    String room_name = "";
    int idx = 0;
    String type = "";
    String getData = "";
    String noti_string = "";
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d("testttttt","asdf----------------------");
//        Log.d("output", remoteMessage.getNotification().getBody());
//        fcm_ment = remoteMessage.getNotification().getBody();
        noti_string = String.valueOf(remoteMessage.getData());
/*        if (noti_string.contains("《")) {
            idx = noti_string.indexOf("《");
            type = noti_string.substring(0,idx);
            getData = noti_string.substring(idx + 1);
            Log.d(TAG, "Message data choice: " + remoteMessage.getData());
            if (type.equals("1")) {
                GetJson("[" + noti_string + "]");
                handler.sendEmptyMessage(0);
            }
            else if (type.equals("2")){

            }
        }*/
        GetJson("[" + noti_string + "]");
        if (type.equals("1")) {
            handler.sendEmptyMessage(0);
        }
        else if (type.equals("2")){
            socket.sendEmptyMessage(0);
        }
// Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

// Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            handler.sendEmptyMessage(0);
        }
    }
    void GetJson(String fcm_list){
        JSONArray json = null;
        try {
            json = new JSONArray(fcm_list);

            Log.d("json_length", String.valueOf(json.length()));
            JSONObject jsonObject = json.getJSONObject(0);
            user_name = jsonObject.getString("user_name");
            room_num = jsonObject.getString("room_num");
            room_name = jsonObject.getString("room_name");
            type = jsonObject.getString("type");
            Log.d("id_num", user_name + "|||" + room_num +"||"+ type);

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }


    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MyFirebaseMessagingService.this, "초대메세지가 왔습니다.", Toast.LENGTH_SHORT).show();
//remoteMessage.getNotification().getBody()
//                    Dialog as = new Dialog(MyFirebaseMessagingService.this);
//                    as.Show(remoteMessage.getNotification().getBody());
            Log.d("aaa_________________","testestestestest");
            ;
            GameActivity.modify(user_name, room_num, room_name);

            Log.d("aaa11111111","testestestestest");
        }
    };
    Handler socket = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MyFirebaseMessagingService.this, "소켓!", Toast.LENGTH_SHORT).show();

        }
    };


    private void showNotification(String title, String message) {
        /*Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(message,"message");
        startActivity(intent);*/
        /*PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
*/

       /* AlertDialog.Builder dialog =
                new AlertDialog.Builder(FirebaseMessagingService);
        dialog.setTitle("씨가 친구를 추가 했습니다.");
        dialog.setMessage("친구를 추가하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                okhttpFriend.execute();

            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "아니오", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();*/
    }




}
