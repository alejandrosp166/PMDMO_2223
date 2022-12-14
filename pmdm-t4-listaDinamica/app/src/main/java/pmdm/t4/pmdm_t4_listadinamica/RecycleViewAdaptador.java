package pmdm.t4.pmdm_t4_listadinamica;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleViewAdaptador extends RecyclerView.Adapter<RecycleViewAdaptador.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombre, nacionalidad;
        ImageView imgFutbolista;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.tvFutbolista);
            nacionalidad = (TextView) itemView.findViewById(R.id.tvNacionalidad);
            imgFutbolista = (ImageView) itemView.findViewById(R.id.imgFutbolista);
        }
    }

    public List<FutbolistaModelo> listFutbolista;

    public RecycleViewAdaptador(List<FutbolistaModelo> listFutbolista) {
        this.listFutbolista = listFutbolista;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_futbolista,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombre.setText(listFutbolista.get(position).getNombre());
        holder.nacionalidad.setText(listFutbolista.get(position).getNacionalidad());
        holder.imgFutbolista.setImageResource(listFutbolista.get(position).getImagen());
    }

    @Override
    public int getItemCount() {
        return listFutbolista.size();
    }
}
