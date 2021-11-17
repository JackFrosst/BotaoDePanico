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
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// esta classe é utilizada para criar métodos que funcionem em segundo plano
public class RecebeAlertaThread extends Service {
    private FirebaseFirestore firebaseFirestore;
    private BdSqLiteCadastroLogin bdSqLiteCadastroLogin;

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
        // se tiver alguma informação de alteração, a variável snapshot
        //recebe a alteração e a cada vez que acontece uma atualização é criado uma notificação
        DocumentReference docRef = firebaseFirestore.
                collection("usuarios").document(numTelefone);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    //strings alimentadas pela variavel snapshot que retorna do servidor firebase
                    String tituloAlerta = snapshot.getString("Alerta");
                    String remetente = snapshot.getString("Remetente");
                    String statusNotificacao = "1";
                    //verifica se existe o statusNotificacao antes de preencher a váriavel
                    if (snapshot.getString("statusNotificacao") != null) {
                        statusNotificacao = snapshot.getString("statusNotificacao");
                    }
                    //verifica se a necessidade de criar um canal de notificação de acordo com a versão do SDK
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String id_canal = "Alertas";
                        CharSequence nome = "Pedido de Ajuda" ;
                        int importancia = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel channel = new NotificationChannel(id_canal, nome, importancia);
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                    }
                    //Cria a notificação
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(RecebeAlertaThread.this, "Alertas")
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle(tituloAlerta)
                            .setContentText(remetente)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);
                    //Exibe a notificação que foi criada acima
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(RecebeAlertaThread.this);
                    //é realizado uma verificação caso o valor da variavel statusNotificacao seja "0", é exibido
                    //a noticação e é alterado o valor no banco para "1", para que não seja exibido mais a notificação
                    if(statusNotificacao.equals("0")) {
                        Map<String, String> mudaStatusNoti = new HashMap<>();
                        mudaStatusNoti.put("statusNotificacao","1");
                        notificationManager.notify(1, builder.build());
                        firebaseFirestore.collection("usuarios")
                                .document(numTelefone)
                                .set(mudaStatusNoti, SetOptions.merge());
                    }else{

                    }

                }
            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent("chamandoRecebeAlertaThread"));
    }

}
