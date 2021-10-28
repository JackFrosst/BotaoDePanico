package botaopanico.principal;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //método trabalha em segundo plano sem interferir na atividade principal
    //atráves deste método quando aciona o botão os alertas são enviados para os destinatários
    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        LocalizacaoSingleton localizacao = LocalizacaoSingleton.getInstance(EnvioAlertaThread.this);
        Log.e("teste5",String.valueOf(localizacao.latitude));

        //métodos que buscam no banco sqlite o destinatario e o rementente
        // para indentificar quem esta enviando a mensagem e que vai receber
        bdSqLiteCadastroLogin = new BdSqLiteCadastroLogin(EnvioAlertaThread.this);
        ArrayList<Destinatario> arrayListDest = bdSqLiteCadastroLogin.consultarDestinatario();
        ArrayList<Remente> arrayListRem = bdSqLiteCadastroLogin.consultarRemetente();

        //laço percorrendo todos os remetentes que estão cadastrados no banco sqlite
        //e enviando atualizações para o firebase
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
