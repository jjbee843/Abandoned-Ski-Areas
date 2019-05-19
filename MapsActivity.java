package com.example.j.abandonedskiareas;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.android.gms.location.Geofence.NEVER_EXPIRE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleApiClient.ConnectionCallbacks, ResultCallback<Status> {

    private GoogleMap mMap;
    int position = 0;
    boolean fromView = false;
    LatLng area;
    public double currLat;
    public double currLong;
    private Location currentLocation;
    private GoogleApiClient apiClient;
    private GoogleApiClient locApiClient;
    private Location lkl;
    private LocationRequest locReq;
    private boolean sendUpdate = true;
    protected PendingIntent geoPendIntent;
    private String lastUpdateTime;
    //protected Geofence geofences[];
    protected List<Geofence> geofences = new ArrayList<Geofence>();

    public int mId;

    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    //LatLng area;
    public String names[] = {
            "Alden's Hill", "Bald Mountain", "Bauneg Beg", "Beaver Hill", "Belfast Ski Area", "Bell Slope", "Biddeford Rotary Park", "Big Hill", "Cumberland Ski Slope",
            "Deer Hill", "Dundee Heights", "Dutton Hill Ski Area", "Eastport Ski Area", "Essex Street Hill", "Gorham Kiwanis", "Hillside Ski Area",
            "Hurricane Ski Slope", "Kimball's Hill", "King's Mountain Slope", "Maggie's Mountain", "Maple Grove", "McFarland's Practice Slope", "Mt. Agamenticus",
            "Mt. Gile Ski Area", "Oak Grove School", "Paradise Park", "Pine Haven"}; //27
    public double locations[] = {43.682371, -70.457550,//0
            43.844206, -70.736799,//2
            43.384958, -70.784879,//4
            43.476057, -70.783937,//6
            44.443138, -69.028308,//8
            44.031815, -70.178605,//10
            43.499432, -70.477528,//12
            44.699010, -68.562479,//14
            43.65, -70.32, //unsure, 16
            43.680642, -70.337838,//18
            43.795710, -70.456889,
            43.829529, -70.382231,
            44.952076, -67.117580,
            44.826509, -68.768961,
            43.681790, -70.440645,
            44.610102, -69.032213, //unsure
            43.798504, -70.329284,
            43.400072, -70.569286,
            44.709977, -68.758238,
            43.861975, -70.153508,
            47.159482, -68.258791,
            44.380842, -68.265375,//unsure
            43.223521, -70.692574,
            44.103300, -70.256274,//approximation, need to look around
            44.467227, -69.678329,
            45.023872, -68.867791, //complete approximation
            44.055102, -70.131278,
            };
    //27

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        fromView = intent.getBooleanExtra("fromView", false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (apiClient == null) {
            apiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */,
                            this /* OnConnectionFailedListener */)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .build();

        }
        if (locApiClient == null) {
            locApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        initGeofences();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v("View", "fromView: " + fromView);
        requestLocation();
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        for(int i=0;i<locations.length - 2;i=i+2){
            LatLng location = new LatLng(locations[i], locations[i+1]);
                mMap.addMarker(new MarkerOptions().position(location));

        }
        if(fromView) {
            position = position + position;
            area = new LatLng(locations[position], locations[position + 1]);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(area,15));
            fromView = false;
        }
        else{
            area = new LatLng(45.216416, -69.123862);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(area,5));
        }
        //LatLng area = new LatLng(44.666928, -70.148310);
        //mMap.addMarker(new MarkerOptions().position(area).title(names[position]));
//        mMap.moveCamera(CameraUpdateFactory.zoomBy(15));
    }

    //Geofence and location stuff

    private void checkForUserPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {

        }
    }
    private void updateUI() {
//        latTxt.setText(String.valueOf(currentLocation.getLatitude()));
//        lonTxt.setText(String.valueOf(currentLocation.getLongitude()));
        currLat = currentLocation.getLatitude();
        currLong = currentLocation.getLongitude();
//        lastTime.setText(lastUpdateTime);
        Log.v("String", "LOCATION TEST" + currLat + "," + currLong);
    }

    protected void onPause() {
        super.onPause();
        if (locApiClient != null) {
            stopLocationUpdates();
        }
    }

    protected void stopLocationUpdates() {
        if(locApiClient!=null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(locApiClient, this);
        }
    }

    public void onResume() {
        super.onResume();
        if(apiClient.isConnected() && !sendUpdate){
            sendLocUpdate();
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, sendUpdate);
        savedInstanceState.putParcelable(LOCATION_KEY, currentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, lastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }
    protected void initGeofencing() {
        checkForUserPermission();
        LocationServices.GeofencingApi.addGeofences(
                locApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);

    }
    protected void initGeofences() {

        for(int i=0;i<locations.length;i=i+2){
            geofences.add(
             new Geofence.Builder()
                    .setRequestId("Geofence: " + (i - i*.5))
                    .setCircularRegion(
                            locations[i],
                            locations[i+1],
                            100
                    )
                    .setExpirationDuration(NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT).build());

        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        for (int i = 0; i < 27; i++) {
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
            builder.addGeofence(geofences.get(i));
        }


        return builder.build();
    }
    private PendingIntent getGeofencePendingIntent() {
        Log.v("Intent","Pending intent is called");
        if(geoPendIntent != null) {
            Log.v("Intent", "intent");
            return geoPendIntent;
        }
        else {
            Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
            Log.v("Intent", ":" + geoPendIntent);
            return PendingIntent.getService(this, 0, intent, geoPendIntent.FLAG_UPDATE_CURRENT);
        }
        //GTIS.onHandleIntent(geoPendIntent);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            if(savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)){
                sendUpdate = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY);

            }
            if(savedInstanceState.keySet().contains(LOCATION_KEY)){
                currentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }
            if(savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)){
                lastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //error message "could not connect to google api"
        Log.e("Error", "Unable to connect to Google API");
    }
    @Override
    public void onConnectionSuspended(int n) {
        ;
    }


    protected void requestLocation() {
        locReq = new LocationRequest();
        locReq.setInterval(100);
        locReq.setFastestInterval(500);
        locReq.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder buildIt = new LocationSettingsRequest.Builder()
                .addLocationRequest(locReq);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(locApiClient,buildIt.build());
        //implement option for user to change settings so that location works
    }

    @Override
    public void onConnected(Bundle connectionHint){
        Log.v("connected","HERE");
        checkForUserPermission();
        lkl = LocationServices.FusedLocationApi.getLastLocation(locApiClient);
        if(lkl != null){
            //latTxt.setText(String.valueOf(lkl.getLatitude()));
            //lonTxt.setText(String.valueOf(lkl.getLongitude()));
            currLong = lkl.getLongitude();
            currLat = lkl.getLatitude();
            Log.v("LOOK", "Location: " + currLong + "," + currLat);
        }
        if(sendUpdate){
            sendLocUpdate();
            Log.v("LOOK", "LocUpdate sent");
        }
        initGeofencing();
        if(GeofenceTransitionsIntentService.playzone = true){
            inArea();
            Toast.makeText(this,"You're near a ski area!",Toast.LENGTH_SHORT).show();
        }
    }
    protected void sendLocUpdate(){
        Log.v("LocReq", "LocReq: " + locReq);
        checkForUserPermission();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                locApiClient, locReq, this);//location listener??);

        //Toast.makeText(this,"requesting location",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onLocationChanged(Location location){
        //Toast.makeText(this,"changed loc",Toast.LENGTH_SHORT).show();
        currentLocation = location;
        lastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        Log.v("ACTUAL Location:", "Location: " + currentLocation);

        LatLng currentLocation = new LatLng(currLat, currLong);

        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        if(GeofenceTransitionsIntentService.playzone = true){
            inArea();
            Toast.makeText(this,"You're near a ski area!",Toast.LENGTH_SHORT).show();
        }
        updateUI();
    }



    protected void onStart() {
        apiClient.connect();
        locApiClient.connect();
        super.onStart();
    }
    protected void onStop() {
        apiClient.disconnect();
        locApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onResult(@NonNull Status status) {

    }

    public void inArea() {
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
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
// mId allows you to update the notification later on.
        mNotificationManager.notify(mId, builder.build());
        Log.v("Notification", "You should have been notified");
    }

}





