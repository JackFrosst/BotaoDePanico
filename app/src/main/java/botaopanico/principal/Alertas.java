package botaopanico.principal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import java.io.Serializable;
import java.util.ArrayList;

public class Alertas extends AppCompatActivity {
   private Toolbar toolbar;
   private FirebaseFirestore firebaseFirestore;
   private BdSqLiteCadastroLogin bdSqLiteCadastroLogin;
   private ListView lstRecebeAlerta;
   private Localizacao localizacao;
   private ArrayList<Localizacao> arrayLocalizacao;
   private ArrayAdapter<Localizacao> adapterLocalizaco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertas);
        firebaseFirestore = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.toolbar);
        lstRecebeAlerta = findViewById(R.id.lstRecebeAlerta);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Alertas Recebidos");

        recebeAlertas();

        lstRecebeAlerta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Localizacao localizacao = (Localizacao) parent.getItemAtPosition(position);
                Intent intent = new Intent(Alertas.this, OpenStreetMaps.class);
                intent.putExtra("latitude",localizacao.getLatitude());
                intent.putExtra("longitude", localizacao.getLongitude());
                startActivity(intent);
            }
        });

        lstRecebeAlerta.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Alertas.this)
                        .setTitle("Deletar Alerta")
                        .setMessage("Confirma a exclusão?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bdSqLiteCadastroLogin = new BdSqLiteCadastroLogin(Alertas.this);
                                Localizacao localizacao = (Localizacao) parent.getItemAtPosition(position);
                                bdSqLiteCadastroLogin.excluirLocalizacao(localizacao);
                            }
                        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
                return false;
            }
        });

    }

    private void recebeAlertas(){
        localizacao = new Localizacao();

        bdSqLiteCadastroLogin = new BdSqLiteCadastroLogin(Alertas.this);
        ArrayList<Remente> arrayRemetente =  bdSqLiteCadastroLogin.consultarRemetente();
        String numTelefone = arrayRemetente.get(0).getNumeroCelular();

        DocumentReference docRef = firebaseFirestore.collection("usuarios").document(numTelefone);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    localizacao.setLatitude(String.valueOf(snapshot.get("Latitude")));
                    localizacao.setLongitude(String.valueOf(snapshot.get("Longitude")));
                    localizacao.setNomeRementente(String.valueOf(snapshot.get("Remetente")));

                    if(localizacao.getNomeRementente() != "null" ) {
                        bdSqLiteCadastroLogin.cadastrarLocalizacao(localizacao);
                        arrayLocalizacao = bdSqLiteCadastroLogin.consultarLocalizacao();
                        adapterLocalizaco = new ArrayAdapter<>(Alertas.this,
                                android.R.layout.simple_list_item_1, arrayLocalizacao);
                        lstRecebeAlerta.setAdapter(adapterLocalizaco);
                    }
                } else {

                }
            }
        });

    }
}
