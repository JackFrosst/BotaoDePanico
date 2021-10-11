package botaopanico.principal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

        // metodo rodando buscas no banco de dados do firebase
        // se tiver alguma informação de alteração a variável snapshot
        //recebe a alteração e a cada vez que acontece uma atualização é criado uma notificação
        DocumentReference docRef = firebaseFirestore.
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

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = getString(R.string.alerta);
                        String description = getString(R.string.descricaoAlerta);
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel channel = new NotificationChannel("Alertas", name, importance);
                        channel.setDescription(description);
                        // Register the channel with the system; you can't change the importance
                        // or other notification behaviors after this
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(RecebeAlertaThread.this, "Alertas")
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle("Titulo")
                            .setContentText("Isso é um teste")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(RecebeAlertaThread.this);
                    notificationManager.notify(R.string.alerta, builder.build());

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
