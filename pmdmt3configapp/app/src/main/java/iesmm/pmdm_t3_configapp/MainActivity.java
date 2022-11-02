package iesmm.pmdm_t3_configapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // CONTABILIZAR LAS VECES QUE SE HA CAMBIADO DE USUARIO O CONTRASEÑA
    // PERMITIR RESTABLECER DATOS SOLO SI EXISTEN
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true; /** true -> el menú ya está visible */
    }
    //findViewById(R.id.principal).setBackgroundColor(Color.RED);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.reset_settings:
                almacenarConfiguracion(true);
                break;
            case R.id.save_settings:
                almacenarConfiguracion(false);
                break;
            case R.id.load_settings:
                cargarConfiguracion();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void cargarConfiguracion() {

        EditText textFieldUser = (EditText) findViewById(R.id.campo_email);
        EditText textFieldPass = (EditText) findViewById(R.id.campo_password);

        // Recuperar preferencias
        SharedPreferences prefs = this.getSharedPreferences("misPreferencias", Context.MODE_PRIVATE);

        // Recuperar valor
        String user = prefs.getString("user", null);
        String pass = prefs.getString("pass", null);

        if (user != null && pass != null) {
            textFieldUser.setText(user);
            textFieldPass.setText(pass);
        } else {
            mostrarMensaje(getString(R.string.no_datos_cargar));
        }
    }

    private void almacenarConfiguracion(boolean resetear) {

        EditText textFieldUser = (EditText) findViewById(R.id.campo_email);
        EditText textFieldPass = (EditText) findViewById(R.id.campo_password);

        String campoUser = textFieldUser.getText().toString();
        String campoPass = textFieldPass.getText().toString();

        if (campoUser != null && campoPass != null) {
            // Reseteamos los datos en el caso de que el usuario lo haga
            if (resetear) {
                campoUser = null;
                campoPass = null;
            }

            // Almacenar el objeto de las preferencias
            SharedPreferences prefs = this.getSharedPreferences("misPreferencias", Context.MODE_PRIVATE);

            // Editar preferencias compartidas
            SharedPreferences.Editor editor = prefs.edit();

            // Almacenar por clave valor
            editor.putString("user", campoUser);
            editor.putString("pass", campoPass);

            // Confirmar los cambios
            editor.commit();
        } else {
            mostrarMensaje(getString(R.string.campos_vacios));
        }
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}