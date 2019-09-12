package com.cms.callmanager.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.cms.callmanager.R;
import com.cms.callmanager.adapter.CallListAdapter;
import com.cms.callmanager.dto.CallDTO;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Vishnu on 02-06-2019.
 */

public class BroadcastManager extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {




        String rec_date = intent.getStringExtra("date");


        String atmid = intent.getStringExtra("atmid");
//        String rec_date = "2019-06-03 16:27:00";

//        Log.i("rec_date_original",rec_date_original+""); // 2019-05-31 15:15:00
      //  Log.i("rec_date",rec_date); // 2019-05-31 15:15:00

        Log.d("", "onReceive_atmid: "+rec_date);



   /*     Intent it = new Intent(context, CallListAdapter.class);
        createNotification(context, it, "CMS!!", atmid , "Missed Time");*/


        try {

            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            final String received_date ="2019/06/02 21:34";


            Calendar c = Calendar.getInstance();
            c.set(Calendar.SECOND, 0);
            System.out.println("Current time => "+c.getTime());

            String current_date = sdf.format(c.getTime());
            Log.i("current_date",current_date+""); // 2019-05-31 15:15:00

            if(rec_date.equals(current_date)){
                Log.i("campare","matched"); // 2019-05-31 15:15:00
//                        Toast.makeText(this.ge, "matched", Toast.LENGTH_SHORT).show();
             //   Toast.makeText(context, "matched", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(context, CallListAdapter.class);
                createNotification(context, it, "CMS Message !!", atmid , "Missed Time");
            }else{
//                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
            }

//
        } catch (Exception e) {
            Log.i("date", "error == " + e.getMessage());
        }
    }


    public void createNotification(Context context, Intent intent, CharSequence ticker, CharSequence title, CharSequence descricao) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(descricao);
        builder.setSound(uri);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(p);
        builder.setAutoCancel(true);
        Notification n = builder.build();
        //create the notification
        n.vibrate = new long[]{150, 300, 150, 400};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.mipmap.ic_launcher, n);
        //create a vibration
        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        } catch (Exception e) {
        }


    }


}