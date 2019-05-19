package com.example.j.abandonedskiareas;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class DetailView extends AppCompatActivity{

    int position = 0;
    TextView nameTxt;
    TextView descripTxt;
    public String names[] = {
            "Alden's Hill", "Bald Mountain", "Bauneg Beg", "Beaver Hill", "Belfast Ski Area", "Bell Slope", "Biddeford Rotary Park", "Big Hill", "Cumberland Ski Slope",
            "Deer Hill", "Dundee Heights", "Dutton Hill Ski Area", "Eastport Ski Area", "Essex Street Hill", "Gorham Kiwanis", "Hillside Ski Area",
            "Hurricane Ski Slope", "Kimball's Hill", "King's Mountain Slope", "Maggie's Mountain", "Maple Grove", "McFarland's Practice Slope", "Mt. Agamenticus",
            "Mt. Gile Ski Area", "Oak Grove School", "Paradise Park", "Pine Haven"};
    public String descrip[] = {
        "Opened 1939, closed 1955. Located in Gorham, the hill is still there and cleared, though no skiing has happened for a while. Go check it out!",
        "Opened during the 1930s, closed in 1976. Located in Dedham, Bald Mountain has a rich history among those who skied it. It had a T-Bar, rope tow, and even a chairlift! Though the lifts are long gone, the ski area is still visible and maybe even still skiable to the brave!",
        "Opened in 1938, closed 1958. Located in North Berwick, Bauneg Beg was a fun place started by locals for locals. It's ski club is well-known, and the mountain is famous for it's fun ski jumping! Though reforested now, the hill and it's memory still live on.",
        "Opened in the 1960s, closed in 1983. Used by Nasson College, which isn't around anymore. There is an old observatory at the top - go check it out!",
        "Opened in 1967, closed in 1973. A community project, only closed because of problems with the one rope tow. A beautiful place, trails are still visible.",
        "Opened in the late 1960s, closed around 1974. Bell Slope was located on Bell Farms, which is still around today. The family loved skiing and opened up a fun place for everyone to go. Pretty cool place!",
        "It is unknown when Biddeford Rotary Park was operational, but it did exist as a rope tow in the center of what is still Rotary Park. It's really unknown which hill in the park the tow was on, adding to the mystery!",
        "Operating during the early 1970s, another 'just for fun' rope tow existed in East Holden. That's all we know!",
        "Open in the late 1930s, the rope tow was located near portland, though almost no info is known about it other than an entry in a 1939 ski map.",
        "It is unknown when Deer Hill was operational, but still fondly remembered. It was a simple rope tow in Westbrook, with lights for night skiing.",
        "One of my personal favorites, Dundee Hill operated around the 1950s in North Gorham. It had a rope tow, lights, and went all the way down to Dundee Pond. A farm stands there now. Cool!",
        "From the 1930s until 1963, this old ski area ran in Windham. Lift line is still visible!",
        "Super cool place open from 1949 to 1951, but with a dark past. It closed when a bulldozer clearing trees at the top tipped over, killing the owner. A lovely ski area nonetheless; a nice community place where many learned to ski.",
        "Open from the 1960s until the 1970s, this slope is still used for sled racing. It had a lift and a T-Bar. You can find some remains of them with a hike up the hill! ",
        "Open from 1961 until the 1970s, this slope was another classic. Many learned to ski here from Gorham and the surrounding communities. The hill is now houses, but you can go take a look!",
        "Open in the 1970s, not much is known about this ski area. It was advertised as having a rope tow, and that's about all we know about it.",
        "Open from 1946 to 1973, this hill was popular and closed only due to insurance premiums. It is now a horse farm, and small pieces of its skiing past can be seen if you go see it!",
        "Open during the 1960s, the hill had a rope tow that went down to the Mousam River. Locals came to ski and sled, sounds like a blast!",
        "Open at least in 1949, this hill was advertised as having a rope tow and looked cute, expecially for beginners.",
        "1962 to 1970, cute little place. Opened by a woman who very much wanted her own business. Was quite successful during its time.",
        "Was advertised in 1969, other than that, not much is known! It had a rope tow and a little slope.",
        "A set of two rope tows, open from the 1930s to the 1970s, McFarland's had a few trails and was a great place for the local beginners.",
        "Ski the Big A! Mt. Agamenticus, or the Big A as it's known, was open from 1966 to 1974. It was beloved to many. One of the more successful hills to close on this list, it included a rope tow, a T-Bar, and a chairlift. Remains of these can all be found at the Big A to this day.",
        "Open in the 1960s, I did my best to pinpoint this place. It's quite grown over, tough to find. Residents of Auburn got to ski for free, how cool!",
        "Oak Grove School was a private school in Vassalboro, having their own slope when they were open. I've heard stories of old skis and equipment still being found in the old school buildings, though it is now a state police academy.",
        "A tiny place advertised in 1939, it offered a rope tow. That's about all we know.",
        "Another one rough to find... open during the 1960s, it became a snow tubing park, but has since changed. My best guess is that it is now on the site of a construction company, which you can see on the map."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        nameTxt = (TextView)findViewById(R.id.nameTxt);
        descripTxt = (TextView)findViewById(R.id.detailTxt);
        nameTxt.setText(names[position]);
        descripTxt.setText(descrip[position]);





    }
    protected void mapClick(View v){
        Intent intent = new Intent(this,MapsActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("fromView", true);
        startActivity(intent);
    }

    }

