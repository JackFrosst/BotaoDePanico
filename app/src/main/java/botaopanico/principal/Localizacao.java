package botaopanico.principal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
//esta classe serve para pegar a localização atual do usuario ou a ultima localização
public class Localizacao {

     private LocationRequest locationRequest;
     private FusedLocationProviderClient fusedLocationProviderClient;
     private LocationCallback locationCallback;
     private String latitude;
     private String longitude;

     // metodo construtor da classe, através dele são realizadas algumas configurações de variáveis
     Localizacao(Context context){
         locationRequest = new LocationRequest();
         locationRequest.setInterval(10000);
         locationRequest.setFastestInterval(5000);
         locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
         fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
     }

    private void ultimaLocalizacao(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                   if(location != null){
                       latitude = String.valueOf(location.getLatitude());
                       longitude = String.valueOf(location.getLongitude());
                       Log.e("4343",String.valueOf(location.getLongitude()));
                   }else{
                       Log.e("4343","failed");
                   }
                }
            });
        }
     }

     private void chamaLocalizacaoAtual(Context context){
         locationCallback = new LocationCallback() {
             @Override
             public void onLocationResult(LocationResult locationResult) {
                 super.onLocationResult(locationResult);
                 if (locationResult != null) {
                     latitude = String.valueOf(locationResult.getLastLocation().getLatitude());
                     longitude = String.valueOf(locationResult.getLastLocation().getLongitude());
                     Log.e("onLocationResult", latitude + longitude);
                 }else{
                     ultimaLocalizacao(context);
                 }
             }
         };
     }

    public void localizacaoAtual(Context context){
         chamaLocalizacaoAtual(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        }
     }

     public String retornaLocalizacao(){

         return longitude + latitude;
     }
}
