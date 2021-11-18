package botaopanico.principal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

        listarAlertas();
        recebeAlertas();

        lstRecebeAlerta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Localizacao localizacao = (Localizacao) parent.getItemAtPosition(position);
                if(localizacao.getLatitude().equals("null") && localizacao.getLongitude().equals("null")){
                    Toast.makeText(Alertas.this, "Remetente não enviou a localização",
                            Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(Alertas.this, OpenStreetMaps.class);
                    intent.putExtra("latitude", localizacao.getLatitude());
                    intent.putExtra("longitude", localizacao.getLongitude());
                    startActivity(intent);
                }
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
                                listarAlertas();
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
                    //variaveis sendo preenchidas com os valores que estão no FireBase
                    arrayLocalizacao = bdSqLiteCadastroLogin.consultarLocalizacao();
                    localizacao.setLatitude(String.valueOf(snapshot.get("Latitude")));
                    localizacao.setLongitude(String.valueOf(snapshot.get("Longitude")));
                    localizacao.setNomeRementente(String.valueOf(snapshot.get("Remetente")));
                    localizacao.setCodAlerta(String.valueOf(snapshot.get("codAlerta")));
                    String codAlertaFireBase = String.valueOf(snapshot.get("codAlerta"));
                    String codAlertaLocal = "";
                    // verifica o tamanho da variável
                    int tamanho = arrayLocalizacao.size();
                    //se o tamanho for > 0 a variável recebe os valores
                    if(tamanho > 0){
                        codAlertaLocal = arrayLocalizacao.get(arrayLocalizacao.size() - 1).getCodAlerta();
                    }

                    //verficações para evitar que na lista entre null, cadastrar o primeiro alerta,
                    // evitar alertas repitidos e por último cadastrar novo alertasa
                    if(codAlertaFireBase == "null"){

                    }else if(tamanho == 0){
                        bdSqLiteCadastroLogin.cadastrarLocalizacao(localizacao);
                        arrayLocalizacao = bdSqLiteCadastroLogin.consultarLocalizacao();
                        adapterLocalizaco = new ArrayAdapter<>(Alertas.this,
                                R.layout.design_lista, arrayLocalizacao);
                        lstRecebeAlerta.setAdapter(adapterLocalizaco);
                    }else if(tamanho > 0 && codAlertaFireBase.equals(codAlertaLocal)){

                    }else{
                        bdSqLiteCadastroLogin.cadastrarLocalizacao(localizacao);
                        arrayLocalizacao = bdSqLiteCadastroLogin.consultarLocalizacao();
                        adapterLocalizaco = new ArrayAdapter<>(Alertas.this, R.layout.design_lista, arrayLocalizacao);
                        lstRecebeAlerta.setAdapter(adapterLocalizaco);
                    }

                }
            }

        });

    }

    public void listarAlertas(){
       bdSqLiteCadastroLogin = new BdSqLiteCadastroLogin(Alertas.this);
       arrayLocalizacao = bdSqLiteCadastroLogin.consultarLocalizacao();
       bdSqLiteCadastroLogin.close();

        if(arrayLocalizacao != null){
            adapterLocalizaco = new ArrayAdapter<>(this,
                    R.layout.design_lista,arrayLocalizacao);
            lstRecebeAlerta.setAdapter(adapterLocalizaco);
        }
    }

}
