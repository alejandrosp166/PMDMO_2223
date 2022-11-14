package iesmm.pmdm_t3_configapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // Variable que cuenta el número de veces que se ha cambiado el usuario y la contraseña
    private int cambiosUsuarios;
    private TextView cambiosUsuarioLabel;
    // Declaramos los cuadros de texto
    private EditText textFieldUser;
    private EditText textFieldPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        try {
            // Intentamos cargar de primeras las veces que se ha cambiado el usuario y la contraseña
            SharedPreferences prefs = this.getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE);
            cambiosUsuarios = Integer.valueOf(prefs.getString("cambiosUsuario", null));
        } catch (Exception e) {
            // Si no salta una excepción significa que no hay datos que cargar, por lo que inicializamos la variable contadora a 0
            cambiosUsuarios = 0;
        }
        // Mostramos los datos de las veces que se ha cambiado el usuario y la contraseña
        cambiosUsuarioLabel = findViewById(R.id.cambios_Usuario);
        cambiosUsuarioLabel.setText(getString(R.string.numero_veces_cambio_usuario) + " " +Integer.toString(cambiosUsuarios) + " veces");
        // Instanciamos los cuadros de texto
        textFieldUser = (EditText) findViewById(R.id.campo_email);
        textFieldPass = (EditText) findViewById(R.id.campo_password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true; /** true -> el menú ya está visible */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.reset_settings:
                // Resetear el archivo de preferencias
                resetUserAndPass();
                break;
            case R.id.save_settings:
                // Guardamos datos en el archivo de preferencias
                saveUserAndPass();
                break;
            case R.id.load_settings:
                // Cargamos datos del archivo de preferencias
                loadUserAndPass();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Cargar el usuario y contraseña de las preferencias
     */
    private void loadUserAndPass() {
        // Instanciamos el archivo preferencias
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE);
        // Cargamos los valores guardados del usuario y contraseña
        String user = prefs.getString(getString(R.string.key_user), null);
        String pass = prefs.getString(getString(R.string.key_pass), null);

        if (user != null && pass != null) {
            // Si hay datos que guardar los cargamos en los textField
            textFieldUser.setText(user);
            textFieldPass.setText(pass);
        } else {
            // Si no hay datos que cargar se lo avisamos al usuario
            mostrarMensaje(getString(R.string.no_datos_cargar));
        }
    }

    /**
     * Guarda el usuario, contraseña y el número de veces que se ha cambiado de usuario y contraseña en las preferencias
     */
    private void saveUserAndPass() {

        // Cargamos los datos que hay dentro de los textFields
        String campoUser = textFieldUser.getText().toString();
        String campoPass = textFieldPass.getText().toString();

        if (!campoUser.equals("") && !campoPass.equals("")) {
            // Si los dos campos tienen algún contenido que guardar, guardamos los datos
            // Instanciamos el archivo preferencias y su editor
            SharedPreferences prefs = this.getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            // Suma 1 a la variable que contabiliza los cambios de usuarios
            cambiosUsuarios++;
            // Lo muestra por pantalla en un textView
            cambiosUsuarioLabel.setText(getString(R.string.numero_veces_cambio_usuario) + " " +Integer.toString(cambiosUsuarios) + " veces");
            // guardamos en el archivo de preferencias los datos asociándolo a una key
            editor.putString(getString(R.string.key_user), campoUser);
            editor.putString(getString(R.string.key_pass), campoPass);
            editor.putString(getString(R.string.key_cambiosUsuario), String.valueOf(cambiosUsuarios));
            // Guardamos los cambios
            editor.commit();
            // Avisamos al usuario de que se ha realizado con éxito la operación
            mostrarMensaje(getString(R.string.datos_guardados));
        } else {
            // Avisamos al usuario de que los campos deben tener datos antes de guardar datos
            mostrarMensaje(getString(R.string.campos_vacios));
        }
    }

    /**
     * Resetea el usuario, contraseña y el número de veces que se ha cambiado de usuario y contraseña en las preferencias
     */
    private void resetUserAndPass() {
        // Instanciamos el archivo preferencias y su editor
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // Limpiamos las preferencias
        editor.clear();
        // Guardamos los cambios
        editor.commit();
        // Avisamos al ususario de que se han cambiado los datos con éxito
        mostrarMensaje(getString(R.string.datos_reseteados));
    }

    /**
     * Muestra mensajes por pantalla en un toast
     *
     * @param mensaje que se muestra por pantalla
     */
    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}