package botaopanico.principal;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.perfmark.Tag;

public class BdCloudFireStore extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private BdSqLiteCadastroLogin bdSqLiteCadastroLogin;
    private Remente remente;

    public void recebeAlerta() {
        firebaseFirestore = FirebaseFirestore.getInstance();

        /*
        DocumentReference docRef = firebaseFirestore.collection("usuarios").document(numeroRementente);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                }

                if (snapshot != null && snapshot.exists()) {
                }
                else {
                }
            }
        });

         */
    }

    public void enviaAlerta(Context bdSqLite ) {
        firebaseFirestore = FirebaseFirestore.getInstance();

        bdSqLiteCadastroLogin = new BdSqLiteCadastroLogin(bdSqLite);
        ArrayList<Destinatario> arrayList = new ArrayList<>();
        arrayList = bdSqLiteCadastroLogin.consultarDestinatario();

        for (int i = 0; i < arrayList.size(); i++) {

            String numeroDestinatario = arrayList.get(i).getNumeroCelular();
            Map<String, String> mensagemDestinatario = new HashMap<>();
            mensagemDestinatario.put("Alerta","Pedido de Ajuda");

            firebaseFirestore.collection("usuarios").document(numeroDestinatario)
                    .set(mensagemDestinatario, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("4343","Mensagem enviada");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }


}
