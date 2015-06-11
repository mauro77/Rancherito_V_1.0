package com.mauriciohincapie.rancherito;

import android.content.IntentSender;
import android.database.Cursor;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

 public class Mapa extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    public static final String TAG = Mapa.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap mMap;
    private CameraUpdate cameraUpdate;

// Might be null if Google Play services APK is not available.

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final LatLng LOCATION_CITY = new LatLng(6.247899,-75.576239);
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        setUpMapIfNeeded();

        LatLng latLngu = new LatLng(6.286142, -75.567662);

        MarkerOptions options1 = new MarkerOptions()
                .position(latLngu)
                .title("Ubicacion actual");
        mMap.addMarker(options1);



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        LoadMarkers();
    }

    public void LoadMarkers(){
        DataBaseManager Manager = Base.getManager();
        cursor = Manager.cargarCursorContactos();
        if (cursor.moveToFirst()){
            do{
                String nombre = cursor.getString(cursor.getColumnIndex(Manager.CN_NAME)).toString();
                String latitud = cursor.getString(cursor.getColumnIndex(Manager.CN_LATITUD)).toString();
                String longitud = cursor.getString(cursor.getColumnIndex(Manager.CN_LONGITUD)).toString();

                float latNum = Float.parseFloat(latitud);
                float longNum = Float.parseFloat(longitud);
                final LatLng LOCATION_VAR = new LatLng(latNum,longNum);
                Toast.makeText(getApplicationContext(), nombre, Toast.LENGTH_SHORT).show();
                mMap.addMarker(new MarkerOptions()
                        .position(LOCATION_VAR)
                        .title(nombre)
                        .snippet(latitud+", "+longitud)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }while (cursor.moveToNext());

            LatLng latLngu = new LatLng(6.286142, -75.567662);

            MarkerOptions options1 = new MarkerOptions()
                    .position(latLngu)
                    .title("Ubicacion actual");
            mMap.addMarker(options1);

        }
        else{
            Toast.makeText(getApplicationContext(), "No se ha ingresado ningún restaurante ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(LOCATION_CITY, 10);
            mMap.animateCamera(cameraUpdate);


            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

    }

    @Override
    public void onConnected(Bundle bundle) {
        // Create the LocationRequest object
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options1 = new MarkerOptions()
                .position(latLng)
                .title("Ubicacion ");
        mMap.addMarker(options1);


    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }
}//main}