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
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    private final String LOGTAG = "PMDM";
    private final String FILENAME = "accesos.dat";
    private Calendar calendario = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cargarEstados();

        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY) + ":" + String.valueOf(calendario.get(Calendar.MINUTE)));
        String fecha = String.valueOf(calendario.get(Calendar.DATE)) + "/" + String.valueOf(calendario.get(Calendar.MONTH) + "/" + String.valueOf(calendario.get(Calendar.YEAR)));

        registrarEstado("ENTRADA", fecha, hora);
    }

    @Override
    protected void onDestroy() {
        // convertimos en cadena la hora
        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY) + ":" + String.valueOf(calendario.get(Calendar.MINUTE)));
        // Convertimos en cadena la fecha
        String fecha = String.valueOf(calendario.get(Calendar.DATE)) + "/" + String.valueOf(calendario.get(Calendar.MONTH) + "/" + String.valueOf(calendario.get(Calendar.YEAR)));
        registrarEstado("SALIDA", fecha, hora);
        super.onDestroy();
    }

    private void registrarEstado(String estado, String fecha, String hora) {
        Log.i(LOGTAG, "Se registra la entrada");

        try {
            // Crear el flujo de escritura
            DataOutputStream write = new DataOutputStream(this.openFileOutput(FILENAME, Context.MODE_APPEND));
            // Añadir al fichero
            write.writeUTF(estado + "," + fecha + "," + hora);
            // Cerrar el flujo
            write.close();

        } catch (FileNotFoundException e) {
            Log.e(LOGTAG, "Fichero no encontrado");
        } catch (IOException e) {
            Log.e(LOGTAG, "Error en el flujo E/S");
        } catch (Exception e) {
            Log.e(LOGTAG, "Error general");
        }
    }

    private void cargarEstados() {

        try {
            DataInputStream leer = new DataInputStream(this.openFileInput(FILENAME));
            String l = leer.readLine();
            while (l != null) {

                String[] temp = l.split(",");
                mostrarBox(temp[0] + " " +temp[1] +" "+ temp[2]);
            }


        } catch (IOException e) {
            Log.e(LOGTAG, "Error en el flujo E/S");
       // } catch (ClassNotFoundException e) {
            Log.e(LOGTAG, "Error, no se encontró la clase");
        } catch (Exception e) {
            Log.e(LOGTAG, "Error general");
        }
    }

    private void mostrarBox(String cad) {
        // Identificar el contenedor del layout
        LinearLayout layout = this.findViewById(R.id.container);
        // Peronalizar el objeto textView
        TextView box = new TextView(this);
        box.setPadding(10, 20, 10, 10);
        box.setGravity(Gravity.CENTER);
        box.setTextColor(Color.RED);
        box.setText(cad);

        //Agregar TextView al contenedor
        layout.addView(box);
    }
}