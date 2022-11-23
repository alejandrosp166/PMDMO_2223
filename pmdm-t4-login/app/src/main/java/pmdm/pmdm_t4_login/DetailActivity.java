package pmdm.pmdm_t4_login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Recuperar datos enviados desde la otra pantalla
        Bundle bundle = this.getIntent().getExtras();

        if(bundle != null) {
            String email = bundle.getString("email");
            TextView textView = this.findViewById(R.id.welcome);
            textView.setText("Bienvenido\n" + email);
        }
    }
}