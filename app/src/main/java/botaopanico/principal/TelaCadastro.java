package botaopanico.principal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TelaCadastro extends AppCompatActivity {

    private EditText edtNome, edtSobrenome, edtTelefone;
    Button btnEnviar;
    RementeDestinatario rementeDestinatario = new RementeDestinatario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        edtNome = findViewById(R.id.edtNome);
        edtSobrenome = findViewById(R.id.edtSobrenome);
        edtTelefone = findViewById(R.id.edtNumTelefone);
        btnEnviar = findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rementeDestinatario.setPrimeiroNome(edtNome.getText().toString());
                rementeDestinatario.setSegundoNome(edtSobrenome.getText().toString());
                rementeDestinatario.setNumeroCelular(edtTelefone.getText().toString());

                BdSqLiteCadastroLogin bdSqLiteCadastroLogin = new BdSqLiteCadastroLogin(TelaCadastro.this);
                bdSqLiteCadastroLogin.cadastrar(rementeDestinatario);

            }
        });
    }
}