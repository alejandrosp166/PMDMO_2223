package pmdm.pmdm_t4_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    //https://medium.com/android-beginners/android-snackbar-example-tutorial-a40aae0fc620
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Instanciamos el botón
        Button b = this.findViewById(R.id.boton_iniciar_sesion);
        File dir = this.getExternalFilesDir(null);
        if (copyFile("\\files\\users.csv", dir.getAbsolutePath())) {
            mostrarToast("Hemos escrito en el fichero reinicia la app");
        }
        // Le ponemos un  Listener al botón
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Guardamos el correos del TextView
                String correo = ((TextView) findViewById(R.id.input_usuario)).getText().toString();
                // Guardamos la contraseña del TextView
                String pass = ((TextView) findViewById(R.id.input_contrasena)).getText().toString();

                if (getAcces(correo, pass)) {
                    Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                } else {
                    // Mostramos un snackbar en caso de error
                    Snackbar.make(view, "Error de acceso", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Devuelve cierto si se confirma el email pass son correctos
     *
     * @param correo correo del usuario a validar
     * @param pass contraseña a validar
     * @return Devuelve true si los datos están dentro del fichero y false si no
     */
    private boolean getAcces(String correo, String pass) {
        // Guardamos la ruta de la memoria externa
        File dir = this.getExternalFilesDir(null);
        // Se comprueba si la app tiene los permisos necesarios
        if (dir.canRead()) {
            // Apuntamos al fichero
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
                            return true;
                        }
                        linea = leer.readLine();
                    }
                    // Cerramos el flujo
                    leer.close();
                }
            } catch (IOException e) {
                // Controlamos el error en el flujo de lectura
                mostrarToast("Error de E/S");
                return false;
            } catch (Exception e) {
                // Controlamos excepciones inesperadas
                mostrarToast("Error general");
                return false;
            }
        }
        return false;
    }

    public boolean copyFile(String fromFile, String toFile) {
        File origin = new File(fromFile);
        File destination = new File(toFile);
        if (origin.exists()) {
            try {
                InputStream in = new FileInputStream(origin);
                OutputStream out = new FileOutputStream(destination);
                // We use a buffer for the copy (Usamos un buffer para la copia).
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                return true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Muestra un Toast en la pantalla
     *
     * @param mensaje cadena que muestra el Toast
     */
    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}