package iesmm.pmdm.t2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main); // Layout autogenerado inicial: Hello World
        // LINEAR LAYOUT
        //setContentView(R.layout.linear_layout_horizontal);
        // setContentView(R.layout.linear_layout_vertical_1);
        // setContentView(R.layout.linear_layout_vertical_2);
        // setContentView(R.layout.linear_layout_vertical_3);
        // setContentView(R.layout.linear_layout_vertical_4);
        // setContentView(R.layout.linear_layout_vertical_5);

        // TABLE LAYOUT
        // setContentView(R.layout.table_layout_1);
        setContentView(R.layout.table_layout_2);

        // SCROLLVIEW LAYOUT
        // setContentView(R.layout.scrollview_layout_1);
        // setContentView(R.layout.scrollview_layout_2);
    }
    // Responde a la pulsación de botón
    public void pulsaBoton(View v){
        switch (v.getId()){
            case R.id.b1:
                Toast.makeText(this, "Se ha pulsado el botón 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.b2:
                Toast.makeText(this, "Se ha pulsado el botón 2", Toast.LENGTH_SHORT).show();
                break;
            default:
                System.err.println("ERROR");
        }
    }

    public void pulsaImagen(View v){
        Toast.makeText(this, "Se ha pulsado la imagen", Toast.LENGTH_SHORT).show();
    }

}