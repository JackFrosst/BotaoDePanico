package botaopanico.principal;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TelaCadastro extends AppCompatActivity {

    private EditText edtNome, edtSobrenome, edtTelefone;
    Button btnEnviar, btnVerificaNumero;
    RementeDestinatario rementeDestinatario = new RementeDestinatario();

    private final int PERMISSAO_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        edtNome = findViewById(R.id.edtNome);
        edtSobrenome = findViewById(R.id.edtSobrenome);
        edtTelefone = findViewById(R.id.edtNumTelefone);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnVerificaNumero = findViewById(R.id.btnVerificaNumero);

        if (ActivityCompat.checkSelfPermission(TelaCadastro.this, Manifest.permission.READ_SMS) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TelaCadastro.this, Manifest.permission.READ_PHONE_NUMBERS) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TelaCadastro.this, Manifest.permission.READ_PHONE_STATE) !=
                PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(TelaCadastro.this,
                    Manifest.permission.READ_PHONE_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(TelaCadastro.this, Manifest.permission.READ_PHONE_NUMBERS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(TelaCadastro.this, Manifest.permission.READ_SMS)) {

            } else {
                ActivityCompat.requestPermissions(TelaCadastro.this, new String[]{
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_PHONE_NUMBERS,
                        Manifest.permission.READ_SMS}, PERMISSAO_REQUEST);
            }
        }

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String number = telephonyManager.getLine1Number();
        Toast.makeText(this, "Número: " + number, Toast.LENGTH_LONG).show();


        btnVerificaNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });


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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSAO_REQUEST:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                }  else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
        }
    }
}
