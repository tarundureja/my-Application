package com.tarun.gpslocationactivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

@SuppressLint("Registered")
public class GPStrace extends Service implements LocationListener {
    private final Context context;
    boolean isGPSEnabled = false;
    boolean canGetLocation = false;
    boolean isNetworkEnabled = false;
    Location location;
    double latitude;
    double longtitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;
    protected LocationManager locationManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public GPStrace(Context context) {
        this.context = context;
        getLocation();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLocation() {
        try {
            locationManager = (LocationManager)
                    context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                }
                if(locationManager!=null){
                    location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location !=null){
                        latitude=location.getLatitude();
                        longtitude=location.getLongitude();
                    }
                }
            }
            if(isGPSEnabled){
                if(location==null){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                    if(locationManager!=null){
                        location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if(location!=null){
                            latitude=location.getLatitude();
                            longtitude=location.getLongitude();
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void stopUsingGPS(){
        if(locationManager!=null){
            locationManager.removeUpdates(GPStrace.this);
        }
    }
    public double getLatitude(){
        if(location!=null){
            latitude=location.getLatitude();
        }
        return latitude;
    }
    public double getLongtiude(){
        if(location!=null){
            longtitude=location.getLatitude();
        }
        return longtitude;
    }
    public boolean canGetLocation(){
        return this.canGetLocation;
    }
    public void showSettingAlert(){
        AlertDialog.Builder alertDialog=new
                AlertDialog.Builder(context);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled.Do you want to go to  setting menu?");
        alertDialog.setPositiveButton("settings", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        Intent intent=new
                                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS); context.startActivity(intent);
                    }});
        alertDialog.setNegativeButton("cancel", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    { // TODO Auto-generated method stub
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
    @Override
    public void onLocationChanged(Location location) { // TODO Auto-generated method stub
    }
    @Override
    public void onProviderDisabled(String provider) { // TODO Auto-generated method stub
    }
    @Override
    public void onProviderEnabled(String provider) {
// TODO Auto-generated method stub
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle  extras) {
// TODO Auto-generated method stub
    }
    @Override
    public IBinder onBind(Intent intent) {
// TODO Auto-generated method stub
        return null;
    }}




