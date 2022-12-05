package iesmm.pmdm.pmdm_t4_progressbarnvl1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * ALEJANDRO SECO PINEDA 2ºDAM
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Instanciación de componentes visuales para su control
        Button start = (Button) this.findViewById(R.id.button);

        // 2. Vinculamos el control del escuchador de eventos con el componente
        start.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                new TareaProgressBar().execute();
                break;
        }
    }

    /* Muestra un cuadro de diálogo con barra de progreso */
    private void showProgress() {
        // OTRA OPCIÓN: Método estático de inicio: ProgressDialog.show(this, titulo, mensaje);
        progress = new ProgressDialog(this);
        progress.setTitle("Descarga simulada"); // Titulo
        progress.setMessage("Cargando"); // Contenido

        // Propiedades de configuración
        progress.setMax(100); // Valor máximo de la barra de progreso
        progress.setCancelable(true); // Permitir que se cancele por el usuario

        // Tipo de barra de progreso: ProgressDialog.STYLE_SPINNER / STYLE_HORIZONTAL
        // progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        // Mostrar cuadro de diálogo
        progress.show();

        // Incrementar valor del progreso en 10 unidades cada 5 segundos
        // progress.incrementProgressBy(10);

        // Obtener valor de la barra de progreso
        // progress.getProgress();

        // Obtener valor máximo de la barra de progreso
        //progress.getMax();

        // Finalización del cuadro de diálogo
        // progress.cancel();
    }

    private class TareaProgressBar extends AsyncTask<Void, Integer, Void> {
        private final int DELAY = 1000;
        private final int PROGRESS_JUMP = 10;

        @Override
        protected void onPreExecute() {
            showProgress();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (progress.getProgress() < progress.getMax()) {
                // Primera opción
                // progress.incrementProgressBy(PROGRESS_JUMP);

                // Segunnda opción
                publishProgress(PROGRESS_JUMP);
                SystemClock.sleep(DELAY);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress.incrementProgressBy(values[0]);
        }

        @Override
        protected void onPostExecute(Void unused) {
            progress.cancel();
        }
    }
}