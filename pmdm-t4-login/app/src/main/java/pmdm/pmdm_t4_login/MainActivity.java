package pmdm.pmdm_t4_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    // Objeto bundle para alamecenar los datos necesarios para la siguiente ventana
    Bundle bundle = new Bundle();
    // Vista que guarda el objeto View que se pulsa en el evento onclick
    View vista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instanciamos el botón
        Button b = this.findViewById(R.id.boton_iniciar_sesion);
        // Le ponemos un  Listener al botón
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Guardamos el valor de view para los snackbars
                vista = view;
                // Guardamos el correos del TextView
                String correo = ((TextView) findViewById(R.id.input_usuario)).getText().toString();
                // Guardamos la contraseña del TextView
                String pass = ((TextView) findViewById(R.id.input_contrasena)).getText().toString();
                // Comprobamos el acceso
                if (getAcces(correo, pass)) {
                    // Le pasamos los datos a la siguiente ventana
                    Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            }
        });
    }

    /**
     * Devuelve cierto si se confirma que el email pass son correctos, también controla todas
     * las excepciones posibles en forma de snackbars
     *
     * @param correo correo a validar
     * @param pass   contraseña a validar
     * @return Devuelve true si los datos están dentro del fichero y false si no
     */
    private boolean getAcces(String correo, String pass) {
        // Variable que se devuelve
        boolean valido = false;
        // Guardamos la ruta de la memoria externa
        File dir = this.getExternalFilesDir(null);
        // Se comprueba si la app tiene los permisos necesarios
        if (dir.canRead()) {
            // Creamos un Objeto fichero que apunta al csv
            File f = new File(dir, "users.csv");
            try {
                // Comprobamos si el fichero existe o no
                if (f.exists()) {
                    // Leer fichero línea por línea
                    BufferedReader leer = new BufferedReader(new FileReader(f));
                    String linea = leer.readLine();
                    String[] sep;

                    while (linea != null) {
                        sep = linea.split(":");
                        // Comprobamos si las creedenciales son correctas
                        if (sep[2].equals(correo) && sep[1].equals(pass)) {
                            // Guardamos en el bundle los datos necesarios
                            bundle.putString("nombre", sep[0]);
                            bundle.putString("email", sep[2]);
                            bundle.putString("telefono", sep[3]);
                            valido = true;
                        }
                        linea = leer.readLine();
                    }
                    // Cerramos el flujo
                    leer.close();

                    if (!valido) {
                        mostrarSnackBar(vista, "Creedenciales incorrectas");
                    }

                } else {
                    mostrarSnackBar(vista, "El fichero no existe en la ruta: " + f.getAbsolutePath());
                }
            } catch (IOException e) {
                // Controlamos el error en el flujo de lectura
                mostrarSnackBar(vista, "Error de E/S");
            } catch (Exception e) {
                // Controlamos excepciones inesperadas
                mostrarSnackBar(vista, "Error general");
            }
        } else {
            mostrarSnackBar(vista, "Faltan permisos de lectura");
        }
        return valido;
    }

    /**
     * Muestra un mensaje por pantalla
     *
     * @param view    la vista del botón
     * @param mensaje mensaje que se muestra por pantalla
     */

    private void mostrarSnackBar(View view, String mensaje) {
        // Mostramos un snackbar en caso de error
        Snackbar snackbar = Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG);
        snackbar.setTextColor(Color.RED);
        snackbar.show();
    }
}