package pmdm.t4.pmdm_t4_listadinamica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFutbolista;
    private RecycleViewAdaptador adaptadorFutbolista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewFutbolista = (RecyclerView)findViewById(R.id.recyclerFutbolista);
        recyclerViewFutbolista.setLayoutManager(new LinearLayoutManager(this));
        adaptadorFutbolista = new RecycleViewAdaptador(obtenerFutbolista());
        recyclerViewFutbolista.setAdapter(adaptadorFutbolista);
    }

    public List<FutbolistaModelo> obtenerFutbolista() {
        List<FutbolistaModelo> futbolista = new ArrayList<>();
        futbolista.add(new FutbolistaModelo("Cristiano Ronaldo", "Portugal", R.drawable.cristiano));
        futbolista.add(new FutbolistaModelo("Lionel Messi", "Argentina", R.drawable.messi));
        futbolista.add(new FutbolistaModelo("Zinedine Zidane", "Francia", R.drawable.zidane));
        futbolista.add(new FutbolistaModelo("Neymar", "Brasil", R.drawable.neymar));
        return futbolista;
    }
}