package botaopanico.principal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdicionarRemoverDestinatario extends AppCompatActivity {
    private Button btnCadastraDestinatario;
    private EditText edtCadastraDestinatario,edtApelidoDestinatario;
    private ListView lstDestinatario;
    private ArrayList<Destinatario> arrayDestinatario;
    private ArrayAdapter<Destinatario> adapterDestinatario;
    private Destinatario destinatario;
    private BdSqLiteCadastroLogin bdSqLiteCadastroLogin;

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
                destinatario = new Destinatario();
                bdSqLiteCadastroLogin = new BdSqLiteCadastroLogin(AdicionarRemoverDestinatario.this);
                destinatario.setNumeroCelular(edtCadastraDestinatario.getText().toString());
                destinatario.setNomeDest(edtApelidoDestinatario.getText().toString());
                bdSqLiteCadastroLogin.cadastrarDestinatario(destinatario);
                listaDestinatario();
            }
        });


    }
    // preenche a lista com os destinatarios que estão cadastrados no banco
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
}