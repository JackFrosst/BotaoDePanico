package botaopanico.principal;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    ImageView botaoAlerta;
    BdCloudFireStore bdCloudFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        botaoAlerta = findViewById(R.id.imageView13);

        botaoAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BdCloudFireStore bdCloudFireStore = new BdCloudFireStore();
                bdCloudFireStore.enviaAlerta(MainActivity.this);
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

}