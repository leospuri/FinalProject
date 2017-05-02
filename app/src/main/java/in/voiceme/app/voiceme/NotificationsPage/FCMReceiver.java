package in.voiceme.app.voiceme.NotificationsPage;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import in.voiceme.app.voiceme.DTO.MessagePojo;
import in.voiceme.app.voiceme.DTO.UserPojo;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.chat.DialogDetailsActivity;
import in.voiceme.app.voiceme.chat.MessageActivity;
import io.realm.Realm;
import timber.log.Timber;

public class FCMReceiver extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String chatMesssage;
    private ChatTextPojo chatTextPojo;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        System.out.println("FCM Message recieved>"+remoteMessage);
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
           Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Timber.d("message id : %s", remoteMessage.getMessageId());
            Timber.d("message notification body : %s", remoteMessage.getNotification().getBody());
            Timber.d("message data : %s", remoteMessage.getData());
            Timber.d("message from : %s", remoteMessage.getFrom());
            Timber.d("message to : %s", remoteMessage.getTo());
            Timber.d("message type : %s", remoteMessage.getMessageType());
            Timber.d("message sent time : %s", remoteMessage.getSentTime());
            Timber.d("message notification title : %s", remoteMessage.getNotification().getTitle());

            List<String> notificationData =
                    Arrays.asList(remoteMessage.getData().toString().replace("{", "").replace("}", "").replace(" ", "").replace(" ", "").split(","));

            //   Set<Map.Entry<String, String>> values = remoteMessage.getData().entrySet();

            if (remoteMessage.getData().containsKey("chat")){
                if (MessageActivity.mThis != null){
                    Timber.e("got message logged");
                    String chatMessage = getJsonFromList(notificationData, remoteMessage);
                    Timber.e(chatMessage);
                    Gson gson = new Gson();
                    try {
                        synchronized (this) {
                            chatTextPojo = gson.fromJson(chatMessage, ChatTextPojo.class);
                        }
                    } catch (Exception ex){
                        ex.printStackTrace();
                    } finally {
                        if (MessageActivity.messageActivityuserId == null){
                            showChatNotification(remoteMessage.getNotification().getTitle());
                        } else if (MessageActivity.messageActivityuserId.equals(chatTextPojo.getSenderId())){
                            startingUp(chatTextPojo);
                        }
                    }
                }
                else {
                    //   return;
                    showChatNotification(remoteMessage.getNotification().getTitle());
                }
            } else {
                saveNotificationObject(getJsonFromList(notificationData, remoteMessage));
            }

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See showNotification method below.
    }
    // [END receive_message]

    private void startingUp(ChatTextPojo chatTextPojo) {

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(500);
                        if (MessageActivity.mThis != null) {
                            MessageActivity.mThis.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MessageActivity.mThis.adapter.addToStart(new
                                            MessagePojo(chatTextPojo.getSenderId(), chatTextPojo.getChatText(),
                                            String.valueOf(System.currentTimeMillis()), new UserPojo(chatTextPojo.getSenderId(),
                                            chatTextPojo.getSenderName(), "", String.valueOf(true))), true);
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        };
        thread.start();
    }

    private void saveNotificationObject(String json) {

        /*
         * Creates a realm object and sets the remote message data and stores it in db
         */
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        NotificationPost post = realm.createObjectFromJson(NotificationPost.class, json);
        realm.copyToRealm(post);
        realm.commitTransaction();

        showNotification(post.initWithNotification(), post);

        Timber.d("saved notification object to db : %s", json);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */

    private void showChatNotification(String messageBody) {
    /*
    Creates pending intent
     */


    /*##
        Intent intent = new Intent(this, DialogDetailsActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("fromNotification", true);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 1 /* Request code *//*##, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                .setContentTitle(messageBody)
                .setContentText("View the message inside Voiceme")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long time = System.currentTimeMillis();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        int notificationId = Integer.valueOf(last4Str);
        notificationManager.notify(notificationId /* ID of notification *//*##,
               /* notificationBuilder.build());


        ##*/


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(messageBody)
                        .setContentText("View the message inside Voiceme")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setSmallIcon(R.drawable.ic_stat_name);


        Intent notificationIntent = new Intent(this, DialogDetailsActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.putExtra("fromNotification", true);
        notificationIntent.putExtra("notification_code", "chat");



        PendingIntent contentIntent = PendingIntent.getActivity(this, 1, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);
       // PendingIntent contentIntent = PendingIntent.getActivity(this, 1, notificationIntent,
        //        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(contentIntent);

        // Add as notification
       // NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       // manager.notify(0, builder.build());

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long time = System.currentTimeMillis();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        int notificationId = Integer.valueOf(last4Str);
        notificationManager.notify(notificationId /* ID of notification */,
                builder.build());
    }


    private void showNotification(String messageBody, NotificationPost post) {
    /*
    Creates pending intent
     */
    /*
        Intent intent = new Intent(this, NotificationsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("fromNotification", true);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0 /* Request code *//*, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                .setContentTitle(post.getSenderName() + " " + "interacted with your post")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long time = System.currentTimeMillis();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        int notificationId = Integer.valueOf(last4Str);
        notificationManager.notify(notificationId /* ID of notification *//*,
                notificationBuilder.build());

        */

        //***************New


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(post.getSenderName() + " " + "interacted with your post")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setSmallIcon(R.drawable.ic_launcher);


        Intent notificationIntent = new Intent(this, NotificationsActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.putExtra("fromNotification", true);
        notificationIntent.putExtra("notification_code", "notification");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);
        //PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
              //  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        // NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // manager.notify(0, builder.build());

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long time = System.currentTimeMillis();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        int notificationId = Integer.valueOf(last4Str);
        notificationManager.notify(notificationId /* ID of notification */,
                builder.build());

    }




    private String getJsonFromList(List<String> notificationData, RemoteMessage remoteMessage) {
        String json = "{";

        json += "\"sentTime\":\"" + remoteMessage.getSentTime() + "\"";
        json += ",";

        for (int i = 0; i < notificationData.size(); i++) {
            Timber.d("Split string %s : %s", i, notificationData.get(i));
            String[] keyValuePair = notificationData.get(i).split("=");
            json += "\"";
            json += keyValuePair[0];
            json += "\"";
            json += ":";
            json += "\"";
            json += keyValuePair[1];
            json += "\"";
            if (i != notificationData.size() - 1) json += ",";
        }

        json += "}";
        Timber.d("json data : %s", json);

        return json;
    }
}

