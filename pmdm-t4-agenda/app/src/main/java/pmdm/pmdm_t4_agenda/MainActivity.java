package pmdm.pmdm_t4_agenda;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter adaptador;
    ArrayList<String> contactos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // CARGAR LOS DATOS
        addItemInListView(contactos);

        try {
            FileOutputStream f = this.openFileOutput("aa", Context.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(f);
            dos.writeUTF("asdasdasdas");
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Instancia el adaptador, se vincula con la lista y permite interactuar con los elementos del ListView
     *
     * @param elementosListView List de todos los elementos que hay en ese momento dento de la lista
     */
    private void addItemInListView(ArrayList elementosListView) {
        // Localizar el listView dentro del layout
        ListView lista = this.findViewById(R.id.listViewAgenda);
        // Instanciamos el adaptador de datos y vincular los datos que vamos a presentar en el listView
        adaptador = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, elementosListView);
        lista.setAdapter(adaptador);
        cargarDatos();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mostrarCuadroOpciones();
            }
        });
    }

    /**
     * Muestra el cuadro de opciones al interactuar con el elemento de la lista
     */
    private void mostrarCuadroOpciones() {

        String[] opciones = {"Llamar", "Escribir wasap", "cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("OPCIONES").setItems(opciones, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // llamar
                        break;
                    case 1:
                        // escribir wasap
                        break;
                    case 2:
                        // cancelar
                        dialog.dismiss();
                        break;
                }
            }
        }).show();
    }

    private void cargarDatos() {
        File f = this.getFileStreamPath("contactos.csv");

        try {
            if (f.exists()) {
                InputStreamReader isr = new InputStreamReader(openFileInput("contactos.csv"));
                BufferedReader leer = new BufferedReader(isr);
                String linea = leer.readLine();

                while (linea != null) {
                    contactos.add(linea);
                    linea = leer.readLine();
                }

                leer.close();
            } else {
                // CREAR EL FICHERO Y GUARDAR LOS DATOS
            }
        } catch (Exception e) {
            e.printStackTrace();
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