package botaopanico.principal;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class TelaLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);
        BdSqLiteCadastroLogin bdSqLiteCadastroLogin = new BdSqLiteCadastroLogin(TelaLogin.this);

        if(bdSqLiteCadastroLogin.consultarLogin() == 1){
            startActivity(new Intent(TelaLogin.this,MainActivity.class));
        }else{
            startActivity(new Intent(TelaLogin.this,TelaCadastro.class));
        }

    }


}