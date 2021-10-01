package botaopanico.principal;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.perfmark.Tag;

public class BdCloudFireStore {

    private FirebaseFirestore firebaseFirestore;
    private BdSqLiteCadastroLogin bdSqLiteCadastroLogin;
    private ArrayList<Remente> arrayRemetente;

    public void recebeAlerta() {
        firebaseFirestore = FirebaseFirestore.getInstance();

        arrayRemetente = bdSqLiteCadastroLogin.consultarRemetente();
        String numeroRementente = arrayRemetente.get(0).getNumeroCelular();

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

    public void enviaAlerta(){

    }
}
