package com.example.kkk.drawword;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.kkk.drawword.Okhttp.OkhttpFriend;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by KKK on 2017-09-24.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "TEST";



    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d("testttttt","asdf----------------------");

// Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

// Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Handler handler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                        Toast.makeText(MyFirebaseMessagingService.this, remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
                    showNotification("asdf",remoteMessage.getNotification().getBody());
                    new OkhttpFriend().execute("2","123","134","asdfasdg");
//                    Dialog as = new Dialog(MyFirebaseMessagingService.this);
//                    as.Show(remoteMessage.getNotification().getBody());
                }
            };
            handler.sendEmptyMessage(0);
        }
    }



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
