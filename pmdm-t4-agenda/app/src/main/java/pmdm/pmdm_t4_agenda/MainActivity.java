package pmdm.pmdm_t4_agenda;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    private ArrayAdapter adaptador;
    ArrayList<Contacto> contactos = new ArrayList<>();
    boolean permisoLlamada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Cargamos los datos
        cargarDatos();
        addItemInListView(contactos);
        // Obtenemos el valor actual del permiso de llamadas
        permisoLlamada = confirmarPermisoLlamada();
    }

    /**
     * Instancia el adaptador, se vincula con la lista y permite interactuar con los elementos del ListView
     *
     * @param elementosListView List de todos los elementos que hay en ese momento dento de la lista
     */
    private void addItemInListView(ArrayList elementosListView) {
        // Localizar el listView dentro del layout
        ListView lista = this.findViewById(R.id.listViewAgenda);
        // Ordenamos la lista por nombre antes de ser mostrada
        elementosListView.sort(Comparator.comparing(Contacto::getNombre));
        // Instanciamos el adaptador de datos y vincular los datos que vamos a presentar en el listView
        adaptador = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, elementosListView);
        // Cambiamos el adaptador del listView
        lista.setAdapter(adaptador);
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
     * Carga los datos del fichero al ListView
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
                    // Leer fichero línea por línea
                    BufferedReader leer = new BufferedReader(new FileReader(f));
                    String linea = leer.readLine();
                    String[] sep;

                    while (linea != null) {
                        sep = linea.split(";");
                        contactos.add(new Contacto(sep[0], Long.parseLong(sep[1]), sep[2]));
                        linea = leer.readLine();
                    }
                    // Cerramos el flujo
                    leer.close();
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
     * Muestra un Toast en la pantalla
     *
     * @param mensaje cadena que muestra el Toast
     */
    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}