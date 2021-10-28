package botaopanico.principal;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//esta classe única fica sendo alimentada com a posição do GPS atual, para que outras classes possam
//utilizar esses valores
public class LocalizacaoSingleton extends Service implements LocationListener {

    private volatile static LocalizacaoSingleton instance;
    private LocationManager locationManager;
    double latitude;
    double longitude;

    @SuppressLint("MissingPermission")
    private LocalizacaoSingleton(Context context){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static LocalizacaoSingleton getInstance(Context context){
        if( instance == null ){

            synchronized( LocalizacaoSingleton.class ){

                if( instance == null ) {

                    instance = new LocalizacaoSingleton(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.e("teste6",String.valueOf(location.getLongitude()));
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
