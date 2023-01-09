package iesmm.pmdm.pmdm_t4_sensores_02;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private SensorManager sensorManager; // Gestor de sensores
    private Sensor acelerometro; // Objeto sensor a medir
    private Sensor luxometro;
    private View layout;
    private TextView ejeX, ejeY, ejeZ, time;
    private Button start, stop;
    private final String LOGTAG = "PMDM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Objetos del layout
        layout = (View) this.findViewById(R.id.layout);
        ejeX = (TextView) this.findViewById(R.id.ejeX);
        ejeY = (TextView) this.findViewById(R.id.ejeY);
        ejeZ = (TextView) this.findViewById(R.id.ejeZ);
        time = (TextView) this.findViewById(R.id.time);
        start = (Button) this.findViewById(R.id.start);
        stop = (Button) this.findViewById(R.id.stop);
        // Asociación de eventos escuchadores
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        // Inicio por defecto
        stop();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.start) {
            start();
        } else {
            stop();
        }
    }

    /* Informa el sensor de un valor nuevo recogido: Un objeto SensorEvent contiene información sobre los nuevos datos del sensor
    , por ejemplo, la exactitud de los datos, el sensor que generó los datos, la marca de tiempo en la que se generaron los datos
    y los nuevos datos que registró el sensor. */
    // Comprensión de unidades y valores: https://developer.android.com/guide/topics/sensors/sensors_motion?hl=es-419
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                ejeX.setText(String.valueOf(formatearValor(sensorEvent.values[0])));
                ejeY.setText(String.valueOf(formatearValor(sensorEvent.values[1])));
                ejeZ.setText(String.valueOf(formatearValor(sensorEvent.values[2])));
                break;
            case Sensor.TYPE_LIGHT:
                int lx = (int) sensorEvent.values[0];
                if(lx > 5000 && lx < 10000) {
                    layout.setBackgroundColor(Color.RED);
                } else if(lx > 10000 && lx < 20000) {
                    layout.setBackgroundColor(Color.GREEN);
                } else if(lx > 20000 && lx < 30000) {
                    layout.setBackgroundColor(Color.BLUE);
                }
                break;
        }

        time.setText(hora());
    }

    /*
    Método para definir el cambio de exactitud y precisión de un sensor.
    La exactitud está representada por una de las cuatro constantes de estado:
    SENSOR_STATUS_ACCURACY_LOW, SENSOR_STATUS_ACCURACY_MEDIUM, SENSOR_STATUS_ACCURACY_HIGH o SENSOR_STATUS_UNRELIABLE.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /* Métodos de control de estado en la app para vincular y desvincular el escuchador del sensor */

    /* Cancela el registro de los objetos de escucha del sensor cuando se ha terminado de usar el sensor
    o cuando se detenga la actividad del sensor */
    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    public void start() {
        // Obtener disponibilidad del sensor
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        luxometro = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // Registrar sensores y disponibilidad
        if(acelerometro != null && luxometro != null) {
            sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, luxometro, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "NO EXISTE ALGUNO DE LOS SENSORES", Toast.LENGTH_SHORT).show();
        }
    }

    public void stop() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            ejeX.setText("Eje X");
            ejeY.setText("Eje Y");
            ejeZ.setText("Eje Z");
            time.setText("HH:MM:SS");
		}
    }

    public String formatearValor(float num) {
        return String.valueOf(Math.floor(num * 100) / 100);
    }

    public String hora() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}