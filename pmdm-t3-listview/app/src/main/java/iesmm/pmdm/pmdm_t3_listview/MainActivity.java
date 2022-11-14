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

    // Declaramos el objeto adaptador
    private ArrayAdapter adaptador;
    // Declaramos el nombre y la ext del fichero
    private final String FILE_NAME = "listaFichero";
    private final String EXTENSION = ".txt";
    ArrayList cadenas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cadenas = new ArrayList();
        addItemInListView(cadenas);
    }

    /**
     * Instancia el adaptador, se vincula con la lista y permite interactuar con los elementos del ListView
     *
     * @param elementosListView List de todos los elementos que hay en ese momento dento de la lista
     */
    private void addItemInListView(ArrayList elementosListView) {
        // Localizar el listView dentro del layout
        ListView lista = this.findViewById(R.id.listView1);
        // Instanciamos el adaptador de datos y vincular los datos que vamos a presentar en el listView
        adaptador = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, elementosListView);
        lista.setAdapter(adaptador);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mostrarCuadroOpciones(i, adaptador.getItem(i).toString());
            }
        });
    }

    /**
     * Muestra el cuadro de opciones al interactuar con el elemento de la lista
     *
     * @param indice el índice del elemento que acabamos de seleccionar
     * @param dni    la cadena del DNI que acabamos de seleccionar
     */
    private void mostrarCuadroOpciones(int indice, String dni) {
        // Creamos el objeto alerDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Le cambiamos el title
        builder.setTitle(getString(R.string.antencion));
        // Le cambiamos el mensaje y creamos el onclickListener para la opción número 1
        builder.setMessage("¿Qué quieres hacer con el DNI: " + dni + " ?").setPositiveButton(getString(R.string.eliminar_elemento), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Mostramos mensaje al usuario
                mostrarToast(getString(R.string.eliminando));
                // Eliminamos el elemento
                adaptador.remove(adaptador.getItem(indice));
                // Ordenamos la lista
                adaptador.sort(Comparator.naturalOrder());
            }
            // Creamos el onclicklistener para la opción número 2
        }).setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Mostramos mensaje al usuario
                mostrarToast(getString(R.string.cancelando));
                // Invocamos al método "cancelar"
                dialogInterface.dismiss();
            }
        }).show();
    }

    /**
     * Introduce el DNI dentro de la lista
     *
     * @param view
     */
    public void putItem(View view) {
        // Creamos el cuadro de texto para interactuar con el
        EditText cuadroTexto = findViewById(R.id.editText);

        // Obtener el texto
        String dni = cuadroTexto.getText().toString();
        // Comprobamos si el cuadro de texto está vacío
        if (!dni.equals("")) {
            // Comprobamos si es un DNI válido
            if (validarDni(dni)) {
                // Comprobamos si el DNI está repetido
                if (!dniRepetido(dni)) {
                    // Añadimos el DNI al ListView
                    adaptador.add(dni);
                    // Ordenamos los DNI
                    adaptador.sort(Comparator.naturalOrder());
                    // Reiniciamos el valor del cuadro de texto
                    cuadroTexto.setText("");
                } else {
                    // Si está repetido avisamos al usuario
                    mostrarToast(getString(R.string.dni_repetido));
                }
            } else {
                // Si el dni no es válido avisamos al usuario
                mostrarToast(getString(R.string.dni_no_valido));
            }
        } else {
            // Si el campo está vacío avisamos al usuario
            mostrarToast(getString(R.string.campo_vacio));
        }
    }

    /**
     * Vuelca la lista dentro del fichero
     */
    private void escribirFichero() {
        Calendar date = new GregorianCalendar();
        // Convertimos en cadena la fecha
        String fecha = date.get(Calendar.DATE) + "-" + date.get(Calendar.MONTH + 1) + "-" + date.get(Calendar.YEAR);
        // Obtener la ruta inicial del directorio del punto de montaje de la memoria externa
        File dir = this.getExternalFilesDir(null);
        // Se comprueba si la app tiene los permisos necesarios
        if (dir.canWrite()) {

            File f = new File(dir, FILE_NAME + "-" + fecha + EXTENSION);
            mostrarToast("El fichero se escribió en: " + f.getAbsolutePath());

            try {
                // Creamos el flujo y ponemos a true la opción de append para concatenar los datos
                FileWriter fout = new FileWriter(f);
                // Escribimos los datos dentro del fichero
                for (int i = 0; i < adaptador.getCount(); i++) {
                    fout.write(adaptador.getItem(i).toString() + ",");
                }
                // Cerramos el flujo
                fout.close();
            } catch (IOException e) {
                // Excepción que controla un error en el flujo de datos
                mostrarToast(getString(R.string.error_ES));
            }
        } else {
            // Si la app no tiene los permisos de escritura avisamos al usuario
            mostrarToast(getString(R.string.no_permisos_escritura));
        }
    }

    /**
     * Vuelca los datos dentro del fichero y limpia la lista
     *
     * @param view
     */
    public void clearItems(View view) {
        if(adaptador.getCount() > 0) {
            // Volcar el contenido del listView a un fichero de memoria externa
            escribirFichero();
            // Vacíar el listView
            adaptador.clear();
        } else {
            mostrarToast(getString(R.string.no_hay_elementos_guardar));
        }
    }

    /**
     * Comprueba si el DNI está repetido dentro de la lista
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
                String numDni = dni.substring(0, 8);
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

    /**
     * Muestra un Toast en la pantalla
     *
     * @param mensaje cadena que muestra el Toast
     */
    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}