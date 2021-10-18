package botaopanico.principal;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// esta classe é utilizada para criar métodos que funcionem em segundo plano
public class EnvioAlertaThread extends Service {
    private FirebaseFirestore firebaseFirestore;
    private BdSqLiteCadastroLogin bdSqLiteCadastroLogin;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    //método trabalha em segundo plano sem interferir na atividade principal
    //atráves deste método quando aciona o botão os alertas são enviados para os destinatários
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        firebaseFirestore = FirebaseFirestore.getInstance();

        bdSqLiteCadastroLogin = new BdSqLiteCadastroLogin(EnvioAlertaThread.this);
        ArrayList<Destinatario> arrayListDest = bdSqLiteCadastroLogin.consultarDestinatario();
        ArrayList<Remente> arrayListRem = bdSqLiteCadastroLogin.consultarRemetente();

        for (int i = 0; i < arrayListDest.size(); i++) {

            String numeroDestinatario = arrayListDest.get(i).getNumeroCelular();
            String nomeRementente = arrayListRem.get(0).getPrimeiroNome();
            String sobrenomeRemetente = arrayListRem.get(0).getSegundoNome();

            Map<String, String> mensagemDestinatario = new HashMap<>();
            mensagemDestinatario.put("Alerta","Emitido pedido de ajuda");
            mensagemDestinatario.put("Remetente", nomeRementente + " " + sobrenomeRemetente +
                    ", emitiu um alerta!");

            firebaseFirestore.collection("usuarios").document(numeroDestinatario)
                    .set(mensagemDestinatario, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
