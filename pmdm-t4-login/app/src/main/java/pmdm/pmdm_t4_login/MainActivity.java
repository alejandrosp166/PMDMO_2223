package pmdm.pmdm_t4_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    //https://medium.com/android-beginners/android-snackbar-example-tutorial-a40aae0fc620
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = this.findViewById(R.id.boton_iniciar_sesion);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = ((TextView) findViewById(R.id.input_usuario)).getText().toString();
                String pass = ((TextView) findViewById(R.id.input_contrasena)).getText().toString();

                if (getAcces(correo, pass)) {
                    // Construcción del conjunto de datos
                    Bundle bundle = new Bundle();
                    bundle.putString("email", correo);
                    bundle.putString("pass", pass);

                    Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                } else {
                    Snackbar.make(view, "Error de acceso", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Devuelve cierto si se confirma el email pass son correctos
     *
     * @param correo
     * @param pass
     * @return
     */
    private boolean getAcces(String correo, String pass) {
        return true;
    }
}