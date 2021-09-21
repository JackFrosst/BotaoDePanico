package botaopanico.principal;

import android.util.Log;

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

import java.util.HashMap;
import java.util.Map;

import io.perfmark.Tag;

public class BdCloudFireStore {

    FirebaseFirestore firebaseFirestore;

    public void cadastroRemetente(RementeDestinatario telRementente) {
        firebaseFirestore = FirebaseFirestore.getInstance();

        Map<String, String> data = new HashMap<>();

        firebaseFirestore.collection("usuario").document(telRementente.getNumeroCelular()).set(data);

    }

    public void enviarAlerta() {
        firebaseFirestore = FirebaseFirestore.getInstance();

        WriteBatch batch = firebaseFirestore.batch();

        Map<String, String> data = new HashMap<>();
        data.put("alerta","Ajuda");
        // Set the value of 'NYC'
        DocumentReference Ref1 = firebaseFirestore.collection("usuario")
                .document("11957980351");
        batch.set(Ref1,data);

        DocumentReference Ref2 = firebaseFirestore.collection("usuario")
                .document("11965085125");
        batch.set(Ref2,data);

        DocumentReference Ref3 = firebaseFirestore.collection("usuario")
                .document("119786573432");
        batch.set(Ref3,data);

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // ...
            }
        });

    }
}
