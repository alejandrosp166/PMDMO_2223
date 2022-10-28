package pmdm.t2.estados;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    // Declaramos las contastes
    private final String TAG = "PMDM";
    private final String onPause = "Se ejecuta onPause";
    private final String onStart = "Se ejecuta onStart";
    private final String onPostResume = "Se ejecuta onPostResume";
    private final String onStop = "Se ejecuta onStop";
    private final String onRestart = "Se ejecuta onRestart";
    private final String onDestroy = "Se ejecuta onDestroy";
    // Declaramos la variable que contara el número de reinicios
    private int numeroRestart = 0;
    // Declaramos el objeto sintetizador
    TextToSpeech objSintetizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instanciamos el objeto sintetizador
        objSintetizador = new TextToSpeech(this, this);
        hablar("Se crea la app");
    }

    @Override
    protected void onPause() {
        //hablar(onPause);
        //Log.i(TAG, onPause);
        //showToast(onPause);
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        //Log.i(TAG, onPostResume);
        //showToast(onPostResume);
        //hablar(onPostResume);
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        //Log.i(TAG, onStart);
        //showToast(onStart);
        //hablar(onStart);
        super.onStart();
    }

    @Override
    protected void onStop() {
        //Log.i(TAG, onStop);
        //showToast(onStop);
        //hablar(onStop);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        numeroRestart++;
        hablar(Integer.toString(numeroRestart));
        //Log.i(TAG, onRestart);
        //showToast(onRestart);
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        // No se habla cuando se ejecutaOndestroy en el device manager de android pero en mi télefono físico si
        hablar(Integer.toString(numeroRestart));
        //Log.i(TAG, onDestroy);
        //showToast(onDestroy);
        objSintetizador.shutdown();
        super.onDestroy();
    }

    private void showToast(String msg) {
        // Creamos el objeto toast y lo mostramos por pantalla
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    // MÉTODO PARA QUE EL SINTETIZADOR PUEDA "HABLAR"
    private void hablar(String h) {
        objSintetizador.speak(h, TextToSpeech.QUEUE_FLUSH, null);
    }

    // CONFIGURACIÓN DE PARÁMETRO DE SINTETIZADOR
    @Override
    public void onInit(int i) {
        objSintetizador.setLanguage(Locale.getDefault()); // Idioma
        objSintetizador.setPitch(1.5f); // Entonación de voz
        objSintetizador.setSpeechRate(1); // Velocidad de voz
    }
}