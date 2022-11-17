package pmdm.pmdm_t4_01;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import pmdm.pmdm_t4_01.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity implements GestorApp {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_marcador_telefonico:
                abrirMarcadorLlamada();
                break;
            case R.id.action_llamada_numero:
                marcarLlamada("123");
                break;
            case R.id.action_navegador_web:
                cargarNavegadorWeb("https://moodle.iesmartinezm.es/");
                break;
            case R.id.action_llamada:
                realizarLlamada("123");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void cargarNavegadorWeb(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        this.startActivity(i);
    }

    @Override
    public void abrirMarcadorLlamada() {
        this.startActivity(new Intent(Intent.ACTION_DIAL));
    }

    @Override
    public void marcarLlamada(String tlfn) {
        if (confirmarPermisoLlamada()) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + tlfn));
            this.startActivity(i);
        }

        /**
         * OPC 2:
         * Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tlfn));
         * this.startActivity(i);
         */
    }

    @Override
    public void realizarLlamada(String tlfn) {
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + tlfn));
        this.startActivity(i);
    }

    @Override
    public void mandarSMS(String contenido) {

    }

    @Override
    public void mandarSMS(String contenido, String tlfn) {

    }

    private boolean confirmarPermisoLlamada() {
        boolean confirmado = false;

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, 0);
        } else
            confirmado = true;

        return confirmado;
    }
}