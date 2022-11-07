package iesmm.pmdm_t3_gestoracceso;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    private final String LOGTAG = "PMDM";
    private final String FILENAME = "accesos.dat";
    // Instanciamos el objeto  calendario que nos permite adquirir fechas del sistema
    private Calendar date = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Registramos la entrada
        registrarEstado("ENTRADA");
        // Lo cargamos en la tabla
        cargarEstados();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Registramos la salida
        registrarEstado("SALIDA");
    }

    /**
     * Guarda el estado que se acaba de realizar puede ser o ENTRADA O SALIDA
     *
     * @param estado estado que se acaba de realizar
     */
    private void registrarEstado(String estado) {

        // convertimos en cadena la hora
        String hora = String.valueOf(date.get(Calendar.HOUR_OF_DAY) + ":" + String.valueOf(date.get(Calendar.MINUTE)));
        // Convertimos en cadena la fecha
        String fecha = String.valueOf(date.get(Calendar.DATE)) + " / " + String.valueOf(date.get(Calendar.MONTH) + " / " + String.valueOf(date.get(Calendar.YEAR)));

        try {
            // Crear el flujo de escritura
            DataOutputStream write = new DataOutputStream(this.openFileOutput(FILENAME, Context.MODE_APPEND));
            // Añadir al fichero
            write.writeUTF(estado + "," + fecha + "," + hora);
            // Cerrar el flujo
            write.close();
            // Mostramos en el log que se ha registrado una entrada a la app
            Log.i(LOGTAG, "Se registra la " + estado + " en la FECHA " + fecha + " en la HORA " + hora);
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

    /**
     * Carga los estados anteriores en el caso de que los haya
     */
    private void cargarEstados() {
        try {
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
        } catch (IOException e) {
            // Controlamos la excepión por si el flujo de lectura falla
            Log.e(LOGTAG, "Error en el flujo E/S");
        } catch (Exception e) {
            // Controlamos las excepciones inesperadas
            Log.e(LOGTAG, "Error general");
        }
    }

    /**
     * Formatea la salida en una tabla de datos
     *
     * @param cad es la cadena que se compone del estado, la fecha y la hora
     */
    private void mostrarBox(String cad) {
        // Identificar el contenedor del layout
        String[] temp = cad.split(",");
        LinearLayout layout = this.findViewById(R.id.container);
        View box = new View(this);

        for (int i = 0; i < 3; i++) {
            TextView celda = new TextView(this);
            // Peronalizar el objeto textView
            celda.setPadding(10, 20, 10, 10);
            celda.setGravity(Gravity.CENTER);
            celda.setTextColor(Color.RED);
            celda.setText(temp[i]);
            //Agregar TextView al contenedor
            
        }
        layout.addView(box);
    }
}