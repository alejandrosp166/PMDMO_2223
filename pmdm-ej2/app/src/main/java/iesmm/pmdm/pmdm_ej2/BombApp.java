package iesmm.pmdm.pmdm_ej2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BombApp extends AppCompatActivity implements View.OnClickListener {
    Button btnEmpezar;
    EditText numero;
    Bundle bundle = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mostratMensaje("El número máximo de segundos permitido es 20 segs");

        numero = this.findViewById(R.id.numero);
        btnEmpezar = this.findViewById(R.id.button);
        btnEmpezar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String num = numero.getText().toString();

        if (validarDato(num)) {
            Intent i = new Intent(getApplicationContext(), CronoBombaApp.class);
            bundle.putString("segundos", num);
            i.putExtras(bundle);
            startActivity(i);
        }
    }

    private boolean validarDato(String num) {
        try {
            int seg = Integer.parseInt(num);
            if(seg < 1 || seg > 20) {
                mostratMensaje("El rango de segundos permitido es: 1 - 20");
                return false;
            }
        } catch (Exception e) {
            mostratMensaje("El dato debe ser un número");
            return false;
        }
        return true;
    }

    private void mostratMensaje(String mensaje){
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}