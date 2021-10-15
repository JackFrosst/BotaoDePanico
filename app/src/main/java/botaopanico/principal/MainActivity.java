package botaopanico.principal;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    Button botaoAlerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        botaoAlerta = findViewById(R.id.btnbotaoAlerta);

        startService(new Intent(getBaseContext(), RecebeAlertaThread.class));

        verificapermissoes();

        startActivity(new Intent(this,OpenStreetMaps.class));


        // inicia a classe EnvioAlertaThread, fazendo que a mensagem seja enviada até o banco
        // firestore
        botaoAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(getBaseContext(), EnvioAlertaThread.class));
            }
        });


        //Define um ouvinte que será notificado quando um item de menu for selecionado
        navigationView.setNavigationItemSelectedListener(this);

        //adicionando o botão para acionar o menu gaveta
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.abrir,R.string.fechar);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();


    }
    //metodo para bloquear o botão de voltar quando o usuario esta na tela inicial, para evitar
    // que volte até a tela de cadastro
    @Override
    public void onBackPressed() {

    }

    //permite escolher os itens do menu
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.adicionarRemoverDestinatario:
                fechaMenuGaveta();
                chamaAtividade(AdicionarRemoverDestinatario.class);
                break;
            case R.id.alerta:
                chamaAtividade(Alertas.class);
                fechaMenuGaveta();
                break;
            case R.id.duvida:
                fechaMenuGaveta();
                break;
            case R.id.configuracao:
                fechaMenuGaveta();
                break;
            default:
                break;
        }

        return true;
    }

    //fecha o menu gaveta
    private void fechaMenuGaveta() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    //chama atividades atráves do Intent
    private void chamaAtividade(Class classe){
        Intent intent = new Intent(this,classe);
        startActivity(intent);
    }

    public void verificapermissoes(){
        final int CODIGO_PERMISSOES_REQUERIDAS = 1;

        if((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.INTERNET) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_NETWORK_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODIGO_PERMISSOES_REQUERIDAS);
            }
        }
    }

}