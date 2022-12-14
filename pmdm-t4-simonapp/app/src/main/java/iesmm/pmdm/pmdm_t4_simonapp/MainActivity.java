package iesmm.pmdm.pmdm_t4_simonapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;
// ALEJANDRO SECO PINEDA 2ºDAM
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttons[]; // Botones
    private int colors[]; // Colores asociados
    List<Color> secuencia;
    private TextView countScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Definición estática de botones y colores
        buttons = new Button[4];
        buttons[0] = this.findViewById(R.id.b1);
        buttons[1] = this.findViewById(R.id.b2);
        buttons[2] = this.findViewById(R.id.b3);
        buttons[3] = this.findViewById(R.id.b4);

        // Colores iniciales
        /*
        buttons[0].setBackgroundColor(Color.GREEN);
        buttons[1].setBackgroundColor(Color.RED);
        buttons[2].setBackgroundColor(Color.YELLOW);
        buttons[3].setBackgroundColor(Color.BLUE);
        */

        colors = new int[4];
        colors[0] = Color.GREEN;
        colors[1] = Color.RED;
        colors[2] = Color.YELLOW;
        colors[3] = Color.BLUE;

        countScore = this.findViewById(R.id.countScore);

        Button btnGo = this.findViewById(R.id.bCentral);
        btnGo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bCentral:
                new TareaSimon().execute();
                break;
        }
    }

    private class TareaSimon extends AsyncTask<Void, Integer, Void> {
        int dificultad = 5;

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "COMIENZA EL JUEGO", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int i = 0;
            while (i < dificultad) {
                int num = (int) (Math.random() * 4);
                publishProgress(num, 0);
                SystemClock.sleep(2000);
                publishProgress(num, -1);
                i++;
            }
            publishProgress(0, -2);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            if (values[1] >= 0) {
                buttons[values[0]].setBackgroundColor(colors[values[0]]);
            } else if (values[1] == -1) {
                buttons[values[0]].setBackgroundColor(Color.BLACK);
            } else if (values[1] == -2) {
                int score = Integer.parseInt((String) countScore.getText()) + 1;
                countScore.setText(String.valueOf(score));
            }
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(getApplicationContext(), "FIN DEL JUEGO", Toast.LENGTH_SHORT).show();
        }
    }
}

