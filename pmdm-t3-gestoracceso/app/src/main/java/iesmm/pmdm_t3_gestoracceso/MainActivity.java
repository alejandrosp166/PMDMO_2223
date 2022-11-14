package iesmm.pmdm_t3_gestoracceso;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    private final String LOGTAG = "PMDM";
    private final String FILENAME = "accesos.dat";
    TableLayout tabla;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inicalizamos la tabla y pintamos el encabezado
        inicializarTabla();

        try {
            // Registramos la entrada
            registrarEstado(getString(R.string.entrada));
            // Lo cargamos en la tabla
            cargarEstados();
        } catch (FileNotFoundException e) {
            // Controlamos la excepción en el caso de que no se encuetre el fichero
            Log.e(LOGTAG, "Fichero no encontrado");
        } catch (IOException e) {
            // Controlamos la excepión por si el flujo de lectura falla
            Log.e(LOGTAG, "Error en el flujo E/S");
        } catch (Exception e) {
            // Controlamos las excepciones inesperadas
            Log.e(LOGTAG, "Error general");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            // Registramos la salida
            registrarEstado(getString(R.string.salida));
        } catch (IOException e) {
            // Controlamos la excepción en un error de flujo
            Log.e(LOGTAG, "Error E/S");
        } catch (Exception e) {
            // Controlamos las excepciones inesperadas
            Log.e(LOGTAG, "Error general");
        }
    }

    /**
     * Guarda el estado que se acaba de realizar puede ser o ENTRADA O SALIDA
     *
     * @param estado estado que se acaba de realizar
     * @throws IOException
     */
    private void registrarEstado(String estado) throws IOException {
        // Instanciamos el objeto  calendario que nos permite adquirir fechas del sistema
        Calendar date = new GregorianCalendar();
        // convertimos en cadena la hora
        String hora = date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE);
        // Convertimos en cadena la fecha
        String fecha = date.get(Calendar.DATE) + " / " + (date.get(Calendar.MONTH) + 1) + " / " + date.get(Calendar.YEAR);
        // Crear el flujo de escritura
        DataOutputStream write = new DataOutputStream(this.openFileOutput(FILENAME, Context.MODE_APPEND));
        // Añadir al fichero
        write.writeUTF(estado + "," + fecha + "," + hora);
        // Cerrar el flujo
        write.close();
        // Mostramos en el log que se ha registrado una entrada a la app
        Log.i(LOGTAG, "Se registra la " + estado + " en la FECHA " + fecha + " en la HORA " + hora);
    }

    /**
     * @throws IOException
     */
    private void cargarEstados() throws IOException {
        // Creamos el flujo de lectura
        DataInputStream leer = new DataInputStream(this.openFileInput(FILENAME));
        // Guardamos la primera línea
        String l = leer.readUTF();
        // Recorremos las líneas del fichero mientras esta no sea null
        while (l != null) {
            // Lo mostramos en la tabla
            mostrarBox(l);
            // Pasamos a la siguiente línea
            l = leer.readUTF();
        }
        // Mostramos en el log que se ha registrado una salida a la app
        Log.i(LOGTAG, "Los datos se han cargado con éxito en la tabla");
    }

    /**
     * Inicializa la tabla, la estructura y pinta el encabezado
     */
    private void inicializarTabla() {
        // Instanciamos el layout
        layout = this.findViewById(R.id.container);
        // Intanciamos una tabla
        tabla = new TableLayout(this);
        // Separamos las cols
        tabla.setStretchAllColumns(true);
        // Añadimos la tabla al layout principal
        layout.addView(tabla);
        // Creamos la fila
        TableRow fila = new TableRow(this);
        // La centramos
        fila.setGravity(Gravity.CENTER);
        // Creamos los elementos del encabezado
        String[] temp = {"ESTADO", "FECHA", "HORA"};

        for (int i = 0; i < temp.length; i++) {
            // Creamos la celda
            TextView celda = new TextView(this);
            // Personalizamos la celda
            celda.setPadding(10, 20, 10, 10);
            celda.setGravity(Gravity.CENTER);
            celda.setTextColor(Color.WHITE);
            // Le añadimos el texto
            celda.setText(temp[i]);
            //Agregar TextView al contenedor
            fila.addView(celda);
        }
        // Añadimos a la tabla la celda
        tabla.addView(fila);
    }

    /**
     * Formatea la salida en una tabla de datos
     *
     * @param cad es la cadena que se compone del estado, la fecha y la hora
     */
    private void mostrarBox(String cad) {
        // Separamos la fila por el regex
        String[] temp = cad.split(",");
        // Creamos la fila
        TableRow fila = new TableRow(this);
        // La centramos
        fila.setGravity(Gravity.CENTER);
        for (int i = 0; i < temp.length; i++) {
            // Creamos la celda
            TextView celda = new TextView(this);
            // Personalizamos la celda
            celda.setPadding(10, 20, 10, 10);
            celda.setGravity(Gravity.CENTER);

            if (temp[i].equals("SALIDA")) {
                celda.setTextColor(Color.RED);
            } else if (temp[i].equals("ENTRADA")) {
                celda.setTextColor(Color.GREEN);
            } else {
                celda.setTextColor(Color.YELLOW);
            }
            // Le añadimos el texto correspondiente
            celda.setText(temp[i]);
            // Le ponemos un borde a la fila
            fila.setBackground(getDrawable(R.drawable.row_border));
            //Añadimos a la fila la celda
            fila.addView(celda);
        }
        // Añadimos a la tabla la fila
        tabla.addView(fila);
    }
}