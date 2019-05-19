package com.example.j.abandonedskiareas;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

/**
 * Created by Jess on 12/8/16.
 */

public class GeofenceTransitionsIntentService extends IntentService {
    int geoTransition;
    GeofencingEvent geoEvent;
    public static boolean playzone  = false;
    public int mId;


    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    protected void onHandleIntent(Intent intent) {
        //GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        Log.v("intent", "intent is being handled");
        geoEvent = GeofencingEvent.fromIntent(intent);
        if (geoEvent.hasError()) {
            Log.e("String", "Something went wrong!");
            return;
        }
        geoTransition = geoEvent.getGeofenceTransition();
        if (geoTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            playzone = true;
            //inArea();
            //Toast.makeText(this,"You've found a special location!",Toast.LENGTH_SHORT).show();
            Log.v("String", "You're in the geofence");
            Log.v("Handler", "You're absolutly positivley in the Geofence");
        } else if (geoTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            playzone = false;
            //Toast.makeText(this,"You've left the playzone!",Toast.LENGTH_SHORT).show();
            Log.v("String", "You're not in the geofence");
        }
        Log.v("Handler", "playzone=" + playzone);
    }

   /* public void inArea() {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                        .setContentTitle("You're within range of an abandoned ski area!")
                        .setContentText("Click to see details!");
        Log.v("Notification", "This:" + this + "Maps:" + MapsActivity.class);
        Intent resultIntent = new Intent(this, MapsActivity.class);
        // The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MapsActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
// mId allows you to update the notification later on.
        mNotificationManager.notify(mId, builder.build());
        Log.v("Notification", "You should have been notified");
    }
    */
}
