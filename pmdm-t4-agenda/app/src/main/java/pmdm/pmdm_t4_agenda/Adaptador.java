package pmdm.pmdm_t4_agenda;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adaptador extends ArrayAdapter<String> {
    private final Activity contexto;
    private final String[] nombres;

    public Adaptador(Activity contexto, String[] nombres) {
        super(contexto, R.layout.elementos_lista, nombres);
        this.contexto = contexto;
        this.nombres = nombres;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = contexto.getLayoutInflater();
        // Instanciamos el objeto fila
        View fila = inflater.inflate(R.layout.elementos_lista, null, true);
        // Instanciamos el TextView para el nombre
        TextView txtNombre = (TextView) fila.findViewById(R.id.name);
        // Instanciamos la vista de la imagen
        ImageView imgContacto = (ImageView) fila.findViewById(R.id.img);
        // Obtenemos el nombre
        txtNombre.setText(nombres[position]);
        // Cambiamos la imagen
        imgContacto.setImageResource(R.drawable.anonimo);
        // Devolvemos la fila
        return fila;
    }
}
