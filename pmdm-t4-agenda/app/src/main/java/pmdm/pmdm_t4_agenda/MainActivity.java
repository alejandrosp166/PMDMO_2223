package pmdm.pmdm_t4_agenda;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    ArrayList<Contacto> contactos;
    boolean permisoLlamada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtenemos el permiso de llamadas
        permisoLlamada = confirmarPermisoLlamada();
        // Instanciamos el Array de contactos
        contactos = new ArrayList<>();
        // Cargamos los datos al ArrayList
        cargarDatos();
        // Creamos la clase adaptador y le pasamos el contexto y los nombres
        Adaptador adaptador = new Adaptador(this, obtenerArrayNombres());
        // instanciamos la lista
        ListView lista = this.findViewById(R.id.listViewAgenda);
        // le cambiamos el adaptador a la clase
        lista.setAdapter(adaptador);

        // Creamos el evento Onclick para cada elemento de la lista
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mostrarCuadroOpciones(String.valueOf(contactos.get(i).getNumero()));
            }
        });
    }

    /**
     * Muestra el cuadro de opciones al interactuar con el elemento de la lista
     */
    private void mostrarCuadroOpciones(String tel) {
        // Creamos el alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Cambiamos el títle y los items dentro
        builder.setTitle("OPCIONES").setItems(new String[]{"Llamar", "Escribir Whatsapp", "Cancelar"}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Llamar al contacto
                        if (permisoLlamada) {
                            try {
                                // Comenzamos la actividad para llamar
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel)));
                            } catch (android.content.ActivityNotFoundException ex) {
                                // Controlamos por si hay un error en la llamada
                                mostrarToast("Error en la llamada");
                            } catch (Exception e) {
                                // Controlamos excepciones inesperadas
                                mostrarToast("Error general");
                            }
                        }
                        break;
                    case 1:
                        // Enviar Whatsapp
                        try {
                            // Creamos el Intent
                            Intent sendWhatsapp = new Intent(Intent.ACTION_SEND);
                            // Cambiamos el tipo del texto
                            sendWhatsapp.setType("text/plain");
                            // Elegimos la app donde enviamos el mensaje
                            sendWhatsapp.setPackage("com.whatsapp");
                            // Ponemos el contenido extra que almacena el mensaje
                            sendWhatsapp.putExtra(Intent.EXTRA_TEXT, "Mensaje");
                            // Comenzamos la actividad
                            startActivity(sendWhatsapp);
                        } catch (android.content.ActivityNotFoundException ex) {
                            // Controlamos la excepción por si la app de mensajería no existe
                            mostrarToast("No existe Whatsapp en el dispositivo");
                        } catch (Exception e) {
                            // Controlamos excepciones inesperadas
                            mostrarToast("Error general");
                        }
                        break;
                    case 2:
                        // cancelar
                        dialog.dismiss();
                        break;
                }
            }
        }).show();
    }

    /**
     * Carga los datos del fichero al arrayList
     */
    private void cargarDatos() {
        // Guardamos la ruta de la memoria externa
        File dir = this.getExternalFilesDir(null);
        // Se comprueba si la app tiene los permisos necesarios
        if (dir.canRead()) {
            // Apuntamos al fichero
            File f = new File(dir, "contactos.csv");
            try {
                // Comprobamos si el fichero existe o no
                if (f.exists()) {
                    // Crear flujo de lectura
                    BufferedReader leer = new BufferedReader(new FileReader(f));
                    String linea = leer.readLine();
                    String[] sep;
                    // Leer fichero línea por línea y volcarlo en el array
                    while (linea != null) {
                        sep = linea.split(";");
                        contactos.add(new Contacto(sep[0], Long.parseLong(sep[1]), sep[2]));
                        linea = leer.readLine();
                    }
                    // Cerramos el flujo
                    leer.close();
                    // Ordenamos el array
                    contactos.sort(Comparator.comparing(Contacto::getNombre));
                } else {
                    mostrarToast("El fichero no existe");
                }
            } catch (IOException e) {
                // Controlamos el error en el flujo de lectura
                mostrarToast("Error de E/S");
            } catch (Exception e) {
                // Controlamos excepciones inesperadas
                mostrarToast("Error general");
            }
        }
    }

    /**
     * Pregunta al usuario sobre los permisos de llamada
     *
     * @return true si le damos a allow y false si lo rechazamos
     */
    private boolean confirmarPermisoLlamada() {
        boolean confirmado = false;

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, 0);
        } else {
            confirmado = true;
        }
        return confirmado;
    }

    /**
     * Transfiere los nombre que hay en el ArrayList contactos a un array de Strings
     *
     * @return un array de String con todos los nombres de los contactos
     */
    private String[] obtenerArrayNombres() {
        String[] nombre = new String[contactos.size()];
        int i = 0;
        for (Contacto c : contactos) {
            nombre[i] = c.getNombre();
            i++;
        }
        return nombre;
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