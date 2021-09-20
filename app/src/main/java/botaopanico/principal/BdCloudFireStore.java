package botaopanico.principal;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

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
}
