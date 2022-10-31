package iesmm.pmdm.t3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cargar valores de configuración local
        cargarConfiguracion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true; /** true -> el menú ya está visible */
    }
    //findViewById(R.id.principal).setBackgroundColor(Color.RED);
    //Toast.makeText(this, "Acerca de...", Toast.LENGTH_LONG).show();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.fondo:
                almacenarConfiguracion();
                break;
            default:
                Toast.makeText(this, item.getTitle().toString(), Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void cargarConfiguracion() {

        // Recuperar preferencias
        SharedPreferences prefs = this.getSharedPreferences("misPreferencias", Context.MODE_PRIVATE);

        // Recuperar valor
        String color_p = prefs.getString("color", null);

        if (color_p != null) {
            // Establecer color de fondo
            ConstraintLayout layout = (ConstraintLayout) this.findViewById(R.id.container);
            layout.setBackgroundColor(Integer.valueOf(color_p));
        }
    }

    private void almacenarConfiguracion() {

        int color = Color.RED;

        ConstraintLayout layout = (ConstraintLayout) this.findViewById(R.id.container);
        layout.setBackgroundColor(color);

        // Almacenar el objeto de las preferencias
        SharedPreferences prefs = this.getSharedPreferences("misPreferencias", Context.MODE_PRIVATE);

        // Editar preferencias compartidas
        SharedPreferences.Editor editor = prefs.edit();

        // Almacenar por clave valor
        editor.putString("color", String.valueOf(color));

        // Confirmar los cambios
        editor.commit();
    }
}