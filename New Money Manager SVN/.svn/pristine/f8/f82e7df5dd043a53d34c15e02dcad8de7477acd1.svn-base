package com.asisdroid.moneymanager.utility;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.asisdroid.moneymanager.MainActivity;
import com.asisdroid.moneymanager.R;
import com.asisdroid.moneymanager.database.ExpenseAccountDBAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by ashishkumarpolai on 9/18/2017.
 */

public class AlarmreceiverOnReboot extends BroadcastReceiver {
    Context thisContext ;
    private SharedPreferences remainderType, remindMePreference;
    private Calendar cal= Calendar.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        thisContext = context;
        remainderType = thisContext.getSharedPreferences("remainderData", Context.MODE_PRIVATE);
        remindMePreference = thisContext.getSharedPreferences("remindMeData", Context.MODE_PRIVATE);
        if(remainderType.getAll().size()!=0 && (!(remainderType.getString("alertType","noalert").equalsIgnoreCase("noalert")))) {
            startAlarmOnReboot(remainderType.getString("alertType", "noalert"), remainderType.getString("alertTime", "0:0:0"));
        }
        if(remindMePreference.getAll().size()!=0){
            startRemindMeAlert();
        }
        Log.d("asisi", "Alarmstarton reboot event");
    }

    public void startAlarmOnReboot(String alertCatg, String alertTime){
        int remainderHour, remainderMin, spinnerIndex, intervalMins = 24;
        remainderHour = Integer.parseInt(alertTime.split(":")[1]);
        remainderMin = Integer.parseInt(alertTime.split(":")[2]);
        spinnerIndex = Integer.parseInt(alertTime.split(":")[0]);

        if(alertCatg.equalsIgnoreCase("noalert")){
            Intent intent = new Intent(thisContext, Alarmreceiver.class);
            PendingIntent pintent = PendingIntent.getBroadcast(thisContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarm = (AlarmManager) thisContext.getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(pintent);
        }
        else {
            //CALLING THE ALARM MANAGER FOR REPEATED NOTIFICATIONS
            Intent intent = new Intent(thisContext, Alarmreceiver.class);
            // intent.putExtra("type","day");
            cal.set(Calendar.HOUR_OF_DAY,remainderHour);
            cal.set(Calendar.MINUTE,remainderMin);
            cal.set(Calendar.SECOND,0);
            PendingIntent pintent = PendingIntent.getBroadcast(thisContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarm = (AlarmManager) thisContext.getSystemService(Context.ALARM_SERVICE);
            if(alertCatg.equalsIgnoreCase("weekly")){  //SETTING REPEATING ALARM FOR WEEK
                if(spinnerIndex==0){
                    cal.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
                }
                else{
                    cal.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
                }
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pintent);
            }
            else if(alertCatg.equalsIgnoreCase("daily")){   //SETTING REPEATING ALARM FOR day
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
            }
            else if(alertCatg.equalsIgnoreCase("monthly")){  //SETTING REPEATING ALARM FOR MONTH
                int currentMonth = cal.get(Calendar.MONTH);
                int currentYear = cal.get(Calendar.YEAR);

                if(spinnerIndex == 0){
                    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                }
                else if(spinnerIndex == 1){
                    cal.set(Calendar.DAY_OF_MONTH,28);
                }
                else if(spinnerIndex == 2){
                    cal.set(Calendar.DAY_OF_MONTH,27);
                }
                else if(spinnerIndex == 3){
                    cal.set(Calendar.DAY_OF_MONTH,26);
                }
                else{
                    cal.set(Calendar.DAY_OF_MONTH,25);
                }

                if (currentMonth == Calendar.JANUARY || currentMonth == Calendar.MARCH || currentMonth == Calendar.MAY || currentMonth == Calendar.JULY
                        || currentMonth == Calendar.AUGUST || currentMonth == Calendar.OCTOBER || currentMonth == Calendar.DECEMBER){
                    alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 31, pintent);
                }
                if (currentMonth == Calendar.APRIL || currentMonth == Calendar.JUNE || currentMonth == Calendar.SEPTEMBER
                        || currentMonth == Calendar.NOVEMBER){
                    alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 30, pintent);
                }


                if  (currentMonth == Calendar.FEBRUARY){//for feburary month)
                    GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
                    if(cal.isLeapYear(currentYear)){//for leap year feburary month
                        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 29, pintent);
                    }
                    else{ //for non leap year feburary month
                        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 28, pintent);
                    }
                }
            }
        }
    }

    public void startRemindMeAlert() {
         if(remindMePreference.getBoolean("isRemindMe",false)==true){
             Intent intentRemindMe = new Intent(thisContext, RemindMeReceiver.class);
             Calendar remindMeTempCal = Calendar.getInstance();
             remindMeTempCal.set(Calendar.HOUR_OF_DAY,22);
             remindMeTempCal.set(Calendar.MINUTE,0);
             remindMeTempCal.set(Calendar.SECOND,0);
             PendingIntent remindmepintent = PendingIntent.getBroadcast(thisContext, 1, intentRemindMe, PendingIntent.FLAG_CANCEL_CURRENT);
             AlarmManager alarm = (AlarmManager) thisContext.getSystemService(Context.ALARM_SERVICE);
             alarm.setRepeating(AlarmManager.RTC_WAKEUP, remindMeTempCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY , remindmepintent);
         }
         else{
             Intent intentRemindMe = new Intent(thisContext, RemindMeReceiver.class);
             PendingIntent remindmePendingIntent = PendingIntent.getBroadcast(thisContext, 1, intentRemindMe, PendingIntent.FLAG_CANCEL_CURRENT);
             AlarmManager alarm = (AlarmManager) thisContext.getSystemService(Context.ALARM_SERVICE);
             alarm.cancel(remindmePendingIntent);
         }
    }
}
