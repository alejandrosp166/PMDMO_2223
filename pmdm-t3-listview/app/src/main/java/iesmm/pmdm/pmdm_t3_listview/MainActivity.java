package iesmm.pmdm.pmdm_t3_listview;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
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

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    // FECHAS MAL AL PINTARSE EN EL FICHERO EL MES ES -1

    private ArrayAdapter adaptador;
    private final String FILE_NAME = "listaFichero";
    private final String EXTENSION = ".txt";

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
                mostrarCuadroOpciones(i, adaptador.getItem(i).toString());
            }
        });
    }

    private void mostrarCuadroOpciones(int indice, String dni) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("¡ ATENCIÓN !");
        builder.setMessage("¿Qué quieres hacer con el DNI: " + dni +" ?").setPositiveButton("Eliminar de la lista", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mostrarToast("Eliminado...");
                adaptador.remove(adaptador.getItem(indice));
                adaptador.sort(Comparator.naturalOrder());
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
    
    public void putItem(View view) {

        EditText textField = findViewById(R.id.editText);

        // 2. Obtener el texto e introducirlo en la interfaz
        String cadena = textField.getText().toString();
        if (!cadena.equals("")) {
            if (validarDni(cadena)) {
                if(!dniRepetido(cadena)) {
                    adaptador.add(cadena);
                    adaptador.sort(Comparator.naturalOrder());
                    textField.setText("");
                } else {
                    mostrarToast(getString(R.string.dni_repetido));
                }
            } else {
                mostrarToast(getString(R.string.dni_no_valido));
            }
        } else {
            mostrarToast(getString(R.string.campo_vacio));
        }
    }

    /**
     * Escribe los datos dentro del fichero
     */
    private void escribirFichero() {
        Calendar date = new GregorianCalendar();
        // Convertimos en cadena la fecha
        String fecha = String.valueOf(date.get(Calendar.DATE)) + "-" + String.valueOf(date.get(Calendar.MONTH) + "-" + String.valueOf(date.get(Calendar.YEAR)));
        // 1. Obtener la ruta inicial del directorio del punto de montaje de la memoria externa
        File dir = this.getExternalFilesDir(null);

        if (dir.canWrite()) {
            // Se comprueba si la app tiene los permisos necesarios
            File f = new File(dir, FILE_NAME + "-" + fecha + EXTENSION);
            mostrarToast(f.getAbsolutePath());

            try {
                FileWriter fout = new FileWriter(f);

                for (int i = 0; i < adaptador.getCount(); i++) {
                    fout.write(adaptador.getItem(i).toString() + "\n");
                }

                fout.close();
            } catch (IOException e) {
                mostrarToast(getString(R.string.error_ES));
            }
        } else {
            mostrarToast(getString(R.string.no_permisos_escritura));
        }
    }

    /**
     * Vuelca los datos dentro del fichero y limpia la lista
     *
     * @param view
     */
    public void clearItems(View view) {
        // 1. Volcar el contenido del listView a un fichero de memoria externa
        escribirFichero();
        // 2. Vacíar el listView
        adaptador.clear();
    }

    /**
     * Comprueba si el DNI no es válido
     *
     * @param dni el dni a validar
     * @return true si el DNI está repetido y false si no
     */
    private boolean dniRepetido(String dni) {
        for (int i = 0; i < adaptador.getCount(); i++) {
            if (dni.equalsIgnoreCase(adaptador.getItem(i).toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprueba si el DNI es válido o no
     *
     * @param dni el dni a validar
     * @return true si el DNI es válido y flase si el DNI no es válido
     */
    private boolean validarDni(String dni) {
        char[] letrasDNI = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
        // Se comprueba si tiene el tamaño mínimo para ser válido
        if (dni.length() == 9) {
            try {
                // Se guarda el número y la letra en variables diferentes
                String numDni = dni.substring(0,8);
                char letra = dni.substring(8).toUpperCase().charAt(0);
                // Se le hace el algoritmo necesario para comprobar si funciona
                for (int i = 0; i < letrasDNI.length; i++) {
                    if (Integer.valueOf(numDni) % 23 == i && letra == letrasDNI[i]) {
                        return true;
                    }
                }
            } catch (Exception e) {
                // Excepción que controla Integer.ValueOf() (Por si metemos algo que no sea un número entre los 8 primeros dígitos)
                return false;
            }
        }
        return false;
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje , Toast.LENGTH_SHORT).show();
    }
}