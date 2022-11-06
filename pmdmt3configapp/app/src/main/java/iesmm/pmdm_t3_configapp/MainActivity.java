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
    private int cambiosUsuarios = 0;
    private TextView cambiosUsuarioLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        cambiosUsuarioLabel = findViewById(R.id.cambios_Usuario);
        cambiosUsuarioLabel.setText(Integer.toString(cambiosUsuarios));
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
     * Cargar el usuario, contraseña y el número de veces que se ha cambiado de usuario y contraseña del archivo preferencias
     */
    private void loadUserAndPass() {
        // Inicializamos los cuadros de texto
        EditText textFieldUser = (EditText) findViewById(R.id.campo_email);
        EditText textFieldPass = (EditText) findViewById(R.id.campo_password);
        // Instanciamos el archivo preferencias
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE);
        // Cargamos los valores guardados del usuario y contraseña
        String user = prefs.getString("user", null);
        String pass = prefs.getString("pass", null);
        // Cargamos el valor que contabiliza las veces que hemos cambiado de usuario y contraseña
        cambiosUsuarios = Integer.valueOf(prefs.getString("cambiosUsuario", null));
        // Y lo cambiamos en el textView
        cambiosUsuarioLabel.setText(Integer.toString(cambiosUsuarios));

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
     * Guarda el usuario, contraseña y el número de veces que se ha cambiado de usuario y contraseña en el archivo de preferencias
     */
    private void saveUserAndPass() {
        // Inicializamos los cuadros de texto
        EditText textFieldUser = (EditText) findViewById(R.id.campo_email);
        EditText textFieldPass = (EditText) findViewById(R.id.campo_password);
        // Cargamos los datos que hay dentro de los textFields
        String campoUser = textFieldUser.getText().toString();
        String campoPass = textFieldPass.getText().toString();

        if (!campoUser.equals("") && !campoPass.equals("")) {
            // Si los dos campos tienen algún contenido que guardar, guardamos los datos
            // Instanciamos el archivo preferencias
            SharedPreferences prefs = this.getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE);
            // Suma 1 a la variable que contabiliza los cambios de usuarios
            cambiosUsuarios++;
            // Lo muestra por pantalla en un textView
            cambiosUsuarioLabel.setText(Integer.toString(cambiosUsuarios));
            SharedPreferences.Editor editor = prefs.edit();
            // guardamos en el archivo de preferencias los datos asociándolo a una key
            editor.putString("user", campoUser);
            editor.putString("pass", campoPass);
            editor.putString("cambiosUsuario", String.valueOf(cambiosUsuarios));
            // Avisamos al usuario de que se ha realizado con éxito la operación
            mostrarMensaje(getString(R.string.datos_guardados));
            // Guardamos los cambios
            editor.commit();
        } else {
            // Avisamos al usuario de que los campos deben tener datos antes de guardar datos
            mostrarMensaje(getString(R.string.campos_vacios));
        }
    }

    /**
     * Resetea el usuario, contraseña y el número de veces que se ha cambiado de usuario y contraseña que hay guardado en el archivo de preferencias
     */
    private void resetUserAndPass() {
        // Instanciamos el archivo preferencias
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // Reiniciamos el valor guardado dentro del archivo de preferencias y lo cambiamos por un valor null
        editor.putString("user", null);
        editor.putString("pass", null);
        editor.putString("cambiosUsuarios", null);
        // Avisamos al usuario de que se ha realizado con éxito la operación
        mostrarMensaje(getString(R.string.datos_reseteados));
        // Guardamos los cambios
        editor.commit();
    }

    /**
     * Muestra mensajes por pantalla
     *
     * @param mensaje que se muestra por pantalla
     */
    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}