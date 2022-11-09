package iesmm.pmdm.pmdm_t3_listview;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter adaptador;
    private final String FILE_NAME = "listaFichero.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
    }

    private void loadData() {
        // En android no hace falta indicar el tipo de dato
        // Pueden ser arrays, list, collections...
        ArrayList cadenas = new ArrayList();
        // Cargar los datos en el listView
        addItemInListView(cadenas);
    }

    private void addItemInListView(ArrayList cadenas) {
        // 1. Localizar el listView dentro del layout
        ListView lista = this.findViewById(R.id.listView1);
        // 2. Instanciamos el adaptador de datos y vincular los datos que vamos a presentar en el listView
        adaptador = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, cadenas);
        lista.setAdapter(adaptador);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String txt = "Nº de la posición de la lista: " + i + "\n de elementos de lista: " + adaptador.getCount();
                txt += "\n valor del elemento: " + adaptador.getItem(i).toString();
                Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void putItem(View view) {

        EditText textField = findViewById(R.id.editText);

        // 2. Obtener el texto e introducirlo en la interfaz
        String cadena = textField.getText().toString();
        if (!cadena.equals("")) {
            adaptador.add(cadena);
            adaptador.sort(Comparator.naturalOrder());
            textField.setText("");
        } else {
            Toast.makeText(this, "Induce un elemento!", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearItems(View view) {
        // 1. Volcar el contenido del listView a un fichero de memoria externa
        escribirFichero();
        // 2. Vacíar el listView
        adaptador.clear();
    }

    private void escribirFichero() {
        // 1. Obtener la ruta inicial del directorio del punto de montaje de la memoria externa
        File dir = this.getExternalFilesDir(null);

        if (dir.canWrite()) {
            File f = new File(dir, FILE_NAME);
            Toast.makeText(this, f.getAbsolutePath(), Toast.LENGTH_SHORT).show();


            try {
                FileWriter fout = new FileWriter(f);

                for (int i = 0; i < adaptador.getCount(); i++) {
                    fout.write(adaptador.getItem(i).toString() + "\n");
                }

                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "ERROR E/S", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarDni(String dni) {
        char[] letrasDNI = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
        int numDni;
        char letra;

        if(dni.length() == 9) {

            for (int i =0;i<adaptador.getCount();i++) {
                if(dni.equalsIgnoreCase(adaptador.getItem(i).toString())) {
                    return false;
                }
            }

            try {
                numDni = 0; // los 8 primeros números
                letra = 'a'; // La última letra (pasar a mayúsculas)
            } catch (Exception e) {
                return false;
            }

            for (int i = 0; i < letrasDNI.length; i++) {
                if (numDni % 23 == i && letra == letrasDNI[i]) {
                    return true;
                }
            }
        }

        return false;
    }
}