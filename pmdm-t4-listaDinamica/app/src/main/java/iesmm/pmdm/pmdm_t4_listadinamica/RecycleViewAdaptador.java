package iesmm.pmdm.pmdm_t4_listadinamica;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleViewAdaptador extends RecyclerView.Adapter<RecycleViewAdaptador.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre, nacionalida;
        ImageView fotoFutbolista;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.textView);
            nacionalida = (TextView) itemView.findViewById(R.id.textView2);
            fotoFutbolista = (ImageView) itemView.findViewById(R.id.imgFutbolista);
        }

        public List<FutbolistaModelo> listFutbolista;
        // MIn 6:51, queda formtear bien los xml
    }
}
