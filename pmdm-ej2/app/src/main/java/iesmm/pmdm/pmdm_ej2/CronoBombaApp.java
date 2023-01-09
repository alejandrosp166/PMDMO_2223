package iesmm.pmdm.pmdm_ej2;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CronoBombaApp extends AppCompatActivity {
    private TextView cronometro;
    private Bundle bundle;
    private ImageView bomba;
    private MediaPlayer sonidoExplosion;
    private String numVecesCronometro;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bundle = this.getIntent().getExtras();
        cronometro = this.findViewById(R.id.cronometer);
        bomba = this.findViewById(R.id.imageView);
        sonidoExplosion = MediaPlayer.create(this, R.raw.bomb);

        if (bundle != null) {

            try {
                SharedPreferences preferences = this.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                numVecesCronometro = preferences.getString("numVecesCronometro",null);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("numVecesCronometro", String.valueOf(Integer.parseInt(numVecesCronometro) + 1));
            } catch (Exception e) {
                numVecesCronometro = String.valueOf(0);
            }

            new cronoBomba().execute();
        }
    }

    private class cronoBomba extends AsyncTask<Void, Integer, Void> {
        int segundos;
        int segundosInicial;

        @Override
        protected void onPreExecute() {
            segundos = Integer.parseInt(bundle.getString("segundos"));
            segundosInicial = segundos;
            cronometro.setText(String.valueOf(segundos));
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (segundos <= segundosInicial && segundos != 0) {
                SystemClock.sleep(1000);
                publishProgress(segundos--);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            cronometro.setText(String.valueOf(segundos));
        }

        @Override
        protected void onPostExecute(Void unused) {
            bomba.setImageResource(R.drawable.explotion);
            sonidoExplosion.start();
            Toast.makeText(getApplicationContext(), "¡LA BOMBA EXPLOTÓ!", Toast.LENGTH_LONG).show();
        }
    }
}
