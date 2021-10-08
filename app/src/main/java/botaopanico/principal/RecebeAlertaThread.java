package botaopanico.principal;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

// esta classe é utilizada para criar métodos que funcionem em segundo plano
public class RecebeAlertaThread extends Service {
    FirebaseFirestore firebaseFirestore;
    BdSqLiteCadastroLogin bdSqLiteCadastroLogin;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //método que realiza as atividades em segundo plano, ficando aberto o tempo inteiro
    // para receber atualizações que acontecem no firestore e gerar notificações como forma de alerta
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        firebaseFirestore = FirebaseFirestore.getInstance();

        bdSqLiteCadastroLogin = new BdSqLiteCadastroLogin(RecebeAlertaThread.this);
        ArrayList<Remente> arrayRemetente = new ArrayList<>();
        arrayRemetente = bdSqLiteCadastroLogin.consultarRemetente();
        String numTelefone = arrayRemetente.get(0).getNumeroCelular();

        final DocumentReference docRef = firebaseFirestore.
                collection("usuarios").document(numTelefone);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("4343", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("4343", "Current data: " + snapshot.getData());


                } else {
                    Log.d("4343", "Current data: null");
                }
            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
