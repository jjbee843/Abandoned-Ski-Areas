package com.example.j.abandonedskiareas;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ExpandableListAdapter adaptList;
    ExpandableListView listView;
    List<String> dataHeader;
    HashMap<String, List<String>> dataChild;
    int position;
    int mId;
    public String names[] = {
            "Alden's Hill", "Bald Mountain", "Bauneg Beg", "Beaver Hill", "Belfast Ski Area", "Bell Slope", "Biddeford Rotary Park", "Big Hill", "Cumberland Ski Slope",
            "Deer Hill", "Dundee Heights", "Dutton Hill Ski Area", "Eastport Ski Area", "Essex Street Hill", "Gorham Kiwanis", "Hillside Ski Area",
            "Hurricane Ski Slope", "Kimball's Hill", "King's Mountain Slope", "Maggie's Mountain", "Maple Grove", "McFarland's Practice Slope", "Mt. Agamenticus",
            "Mt. Gile Ski Area", "Oak Grove School", "Paradise Park", "Pine Haven"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ExpandableListView) findViewById(R.id.areaList);

        populateList();

        adaptList = new ExpandableListAdapter(this, dataHeader, dataChild);

        listView.setAdapter(adaptList);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                position = childPosition;
                Go(parent, v, id);
                return false;
            }
        });
        Log.v("Listener", "Position:" + position);
    }


    private void populateList() {
        dataHeader = new ArrayList<String>();
        dataChild = new HashMap<String, List<String>>();

        dataHeader.add("Eastern Maine");
        //dataHeader.add("Area Maps");
        List<String> easme = new ArrayList<String>();

        for(int i=0;i<names.length;i++){
            easme.add(names[i]);
        }

        dataChild.put(dataHeader.get(0), easme);
        /*List<String> maps = new ArrayList<>();
        maps.add("All Areas");
        dataChild.put(dataHeader.get(1), maps);
        */
    }

    public void Go(ExpandableListView listView, View v, long id){
        Intent intent = new Intent(this,DetailView.class);
        intent.putExtra("position", position);
        intent.putExtra("Id", id);
        startActivity(intent);
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
// mId allows you to update the notification later on.
        mNotificationManager.notify(mId, builder.build());
        Log.v("Notification", "You should have been notified");
    }
    */

}


