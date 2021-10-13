package botaopanico.principal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdicionarRemoverDestinatario extends AppCompatActivity {
    private Button btnCadastraDestinatario;
    private EditText edtCadastraDestinatario,edtApelidoDestinatario;
    private ListView lstDestinatario;
    private ArrayList<Destinatario> arrayDestinatario;
    private ArrayAdapter<Destinatario> adapterDestinatario;
    private Destinatario destinatario;
    private BdSqLiteCadastroLogin bdSqLiteCadastroLogin;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_remover_destinatario);

        btnCadastraDestinatario = findViewById(R.id.btnCadastraDestinatario);
        edtCadastraDestinatario = findViewById(R.id.edtCadastraDestinatario);
        edtApelidoDestinatario = findViewById(R.id.edtApelidoDestinatario);
        lstDestinatario = findViewById(R.id.lstDestinatario);


        //chamado do metodo para preencher a lista com os destinatarios que estiverem no banco
        listaDestinatario();
        // ação de clique no botão que realiza o cadastro dos numeros do destinatario no banco
        btnCadastraDestinatario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultaCadastraDestinatario();
            }
        });

        //metodo para quando ouver um clique longo na lista, vai aparecer uma caixa de dialogo confirmando se o usuario
        //deseja confirmar a exclusão
        lstDestinatario.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdicionarRemoverDestinatario.this)
                        .setTitle("Deletar contato")
                        .setMessage("Confirma a exclusão?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bdSqLiteCadastroLogin = new BdSqLiteCadastroLogin(AdicionarRemoverDestinatario.this);
                                destinatario = (Destinatario) parent.getItemAtPosition(position);
                                Log.e("4343",String.valueOf(destinatario.getId()));
                                Log.e("4343",String.valueOf(position));

                                bdSqLiteCadastroLogin.excluirDestinatario(destinatario);
                                listaDestinatario();
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
    // preenche a lista com os destinatarios que estão cadastrados no banco sqlite
    public void listaDestinatario(){
        BdSqLiteCadastroLogin bdSqLiteCadastroLogin =
                new BdSqLiteCadastroLogin(AdicionarRemoverDestinatario.this);
        arrayDestinatario = bdSqLiteCadastroLogin.consultarDestinatario();
        bdSqLiteCadastroLogin.close();

        if(arrayDestinatario != null){
            adapterDestinatario = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1,arrayDestinatario);
            lstDestinatario.setAdapter(adapterDestinatario);
        }

    }
    //metodo para verificar se o destinatario existe no banco do firebase antes de cadastrar na lista do sqlite
    public void consultaCadastraDestinatario(){
        firebaseFirestore = FirebaseFirestore.getInstance();

        String numeroDestinatario = edtCadastraDestinatario.getText().toString();

        firebaseFirestore.collection("usuarios")
                .document(numeroDestinatario)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                        destinatario = new Destinatario();
                        bdSqLiteCadastroLogin = new BdSqLiteCadastroLogin(AdicionarRemoverDestinatario.this);
                        destinatario.setNumeroCelular(edtCadastraDestinatario.getText().toString());
                        destinatario.setNomeDest(edtApelidoDestinatario.getText().toString());
                        bdSqLiteCadastroLogin.cadastrarDestinatario(destinatario);
                        listaDestinatario();
                }else{
                    Toast.makeText(AdicionarRemoverDestinatario.this,
                            "Esse número não tem cadastro", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdicionarRemoverDestinatario.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}