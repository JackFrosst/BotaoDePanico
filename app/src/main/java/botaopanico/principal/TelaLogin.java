package botaopanico.principal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TelaLogin extends AppCompatActivity {
    private Button btnCadastrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        btnCadastrar = findViewById(R.id.btnCadastrar);

        BdSqLiteCadastroLogin bdSqLiteCadastroLogin = new BdSqLiteCadastroLogin(TelaLogin.this);

        // verifica se a dados n banco de dados e se tiver encaminha para tela principal
        if(bdSqLiteCadastroLogin.consultarLoginRemetente() >= 1){
            startActivity(new Intent(TelaLogin.this,MainActivity.class));
        }
        //encaminha para tela de cadastro apos apertar o botão
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaLogin.this,TelaCadastro.class));
            }
        });




    }


}