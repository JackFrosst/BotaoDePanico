package botaopanico.principal;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class TelaCadastro extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private EditText edtNome, edtSobrenome, edtTelefone;
    Button btnEnviar, btnVerificaNumero;
    RementeDestinatario rementeDestinatario = new RementeDestinatario();

    private GoogleApiClient googleApiClient_telefone;

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

                BdCloudFireStore bdCloudFireStore = new BdCloudFireStore();
                BdSqLiteCadastroLogin bdSqLiteCadastroLogin = new BdSqLiteCadastroLogin(TelaCadastro.this);

                bdCloudFireStore.cadastroRemetente(rementeDestinatario);

                long sucessoCadastro = bdSqLiteCadastroLogin.cadastrar(rementeDestinatario);

                if(sucessoCadastro > 0){
                    startActivity(new Intent(TelaCadastro.this,MainActivity.class));
                    Toast.makeText(TelaCadastro.this, "Cadastrado com Sucesso", Toast.LENGTH_SHORT).show();
                }

            }
        });

        googleApiClient_telefone = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).enableAutoManage(this,this)
                .addApi(Auth.CREDENTIALS_API).build();

        obterNumeroContato();

    }

    //----------------------- METODOS DO GOOGLE API PARA CONSEGUIR O NÚMERO DO USUÁRIO ---------------------------------

    private void obterNumeroContato(){
        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder().setShowCancelButton(false).build())
                .setPhoneNumberIdentifierSupported(true).build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient_telefone,hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(),123,null,0,0,0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 123){

            if(resultCode == RESULT_OK){
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);

                    if(!credential.getId().isEmpty()){
                        edtTelefone.setText(credential.getId());
                    }else{
                        Toast.makeText(TelaCadastro.this, "Escolha um número de contato",
                                Toast.LENGTH_SHORT).show();
                    }
            }else{
                caixaDialogAlerta();
            }
        }
    }

    private void caixaDialogAlerta(){
        AlertDialog.Builder builder = new AlertDialog.Builder(TelaCadastro.this)
                .setTitle("Escolha obrigatória")
                .setCancelable(false)
                .setMessage("Escolha um número de telefone para cadastro!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        obterNumeroContato();
                    }
                }).setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
