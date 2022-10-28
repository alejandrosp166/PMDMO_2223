package iesmm.pmdm.pmdm_t2_xilofono;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    // Declaramos el objeto sintetizador
    TextToSpeech objSintetizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cambiamos la vista a la del xilófono
        setContentView(R.layout.activity_main);
        // Usamos este método para que la vista del xilófono siempre sea en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Instanciamos el objeto sintetizador
        objSintetizador = new TextToSpeech(this,this);
    }

    @Override
    protected void onDestroy() {
        // Cerramos el objeto sintetizador
        objSintetizador.shutdown();
        super.onDestroy();
    }

    public void pulsaBoton(View v){
        // Cogemos el ID del botón que estamos pulsando y dependiendo del botón que pulsemos
        switch (v.getId()){
            case R.id.b1:
                sonido("DO");
                break;
            case R.id.b2:
                sonido("RE");
                break;
            case R.id.b3:
                sonido("MI");
                break;
            case R.id.b4:
                sonido("FA");
                break;
            case R.id.b5:
                sonido("SOL");
                break;
            case R.id.b6:
                sonido("LA");
                break;
            case R.id.b7:
                sonido("SI");
                break;
            default:
                System.err.println("ERROR");
                break;
        }
    }

    public void sonido(String s){
        // Le pasamos la cadena de texto y ejecutamos el método speak
        objSintetizador.speak(s, TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void onInit(int i) {
        objSintetizador.setLanguage(Locale.getDefault()); // Idioma
        objSintetizador.setPitch(2.5f); // Entonación
        objSintetizador.setSpeechRate(1); // Velocidad
    }
}