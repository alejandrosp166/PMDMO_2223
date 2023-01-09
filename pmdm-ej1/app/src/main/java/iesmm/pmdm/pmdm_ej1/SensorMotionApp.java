package iesmm.pmdm.pmdm_ej1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class SensorMotionApp extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnCalcularEmocion;
    ImageView imgEmocion;
    TextView[] emociones;
    TextView tvPorcentajeEmocion;
    TextView tvEmocionBasePorcentaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgEmocion = this.findViewById(R.id.imgvEmocion);
        tvPorcentajeEmocion = this.findViewById(R.id.tvPorcentaje);
        tvEmocionBasePorcentaje = this.findViewById(R.id.tvEmocion);

        emociones = new TextView[5];
        emociones[0] = this.findViewById(R.id.tvEnfadadoScore);
        emociones[1] = this.findViewById(R.id.tvRelajadoScore);
        emociones[2] = this.findViewById(R.id.tvFrustradoScore);
        emociones[3] = this.findViewById(R.id.tvNerviosoScore);
        emociones[4] = this.findViewById(R.id.tvToleranteScore);

        btnCalcularEmocion = this.findViewById(R.id.btnCalcularEmocion);
        btnCalcularEmocion.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        double dMedia = 0;

        for (int i = 0; i < 5; i++) {
            double numEmocion = formatearNumero(aleatorio());
            dMedia += numEmocion;
            emociones[i].setText(String.valueOf(numEmocion));
        }

        int iMedia = (int) Math.round((dMedia/5)*100);

        if (iMedia <= 30) {
            modificarResultado(R.drawable.triste, "TRISTE", Color.BLUE);
        } else if(iMedia > 30 && iMedia <= 60) {
            modificarResultado(R.drawable.neutro, "NEUTRO", Color.GREEN);
        } else if(iMedia > 60 && iMedia <= 100) {
            modificarResultado(R.drawable.alterado, "ALTERADO", Color.RED);
        }

        tvPorcentajeEmocion.setText(iMedia + "%");
    }

    private void modificarResultado(int img, String msg, int color) {
        imgEmocion.setImageResource(img);
        tvEmocionBasePorcentaje.setText(msg);
        tvEmocionBasePorcentaje.setTextColor(color);
    }

    public double aleatorio() {
        return new Random().nextDouble();
    }

    public double formatearNumero(double num) {
        return Math.floor(num * 100) / 100;
    }
}