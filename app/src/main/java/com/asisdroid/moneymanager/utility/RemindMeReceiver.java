package com.asisdroid.moneymanager.utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.asisdroid.moneymanager.MainActivity;
import com.asisdroid.moneymanager.R;
import com.asisdroid.moneymanager.database.ExpenseAccountDBAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by ashishkumarpolai on 9/18/2017.
 */

public class RemindMeReceiver extends BroadcastReceiver {
    NotificationManager  mNotificationManager;
    Context thisContext ;

    private String  notificationHead, notificationContent, notificationSubcontent;

    @Override
    public void onReceive(Context context, Intent intent) {
        thisContext = context;
         displayNotification(1);
        Log.d("asisi", "The Remindme onStart() event");
    }

    private void displayNotification(int notifyid)
    {
        notificationHead = "Monyger - Remind Me";
        notificationContent = "Hei, have you added your expense for today.";
        notificationSubcontent = "If not, tap to add now and keep tracking...";
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(thisContext);
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
            mBuilder.setSmallIcon(R.drawable.icon_notifi);
        } else {
            // Implement this feature without material design
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        Intent intent = new Intent(thisContext, MainActivity.class);
        intent.putExtra(BATTERY_SERVICE, ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(thisContext, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(thisContext.getResources(), R.mipmap.ic_launcher));
        mBuilder.setContentTitle(notificationHead);
        mBuilder.setContentText(notificationContent);
        mBuilder.setSubText(notificationSubcontent);
        mBuilder.setAutoCancel(true);
        Uri uri3= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(uri3);
        mNotificationManager = (NotificationManager) thisContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(notifyid);
        mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
           /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(notifyid, mBuilder.build());
    }
}
