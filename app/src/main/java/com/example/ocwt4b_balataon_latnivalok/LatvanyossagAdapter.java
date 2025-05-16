package com.example.ocwt4b_balataon_latnivalok;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.AbstractCollection;
import java.util.List;

public class LatvanyossagAdapter extends RecyclerView.Adapter<LatvanyossagAdapter.LatvanyossagViewHolder> {

    private List<Latvanyossag> lista;

    public LatvanyossagAdapter(List<Latvanyossag> lista) {
        this.lista = lista;
    }

    public void updateList(List<Latvanyossag> ujLista) {
        this.lista.clear();
        this.lista.addAll(ujLista);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LatvanyossagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_latvanyossag, parent, false);
        return new LatvanyossagViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LatvanyossagViewHolder holder, int position) {
        Latvanyossag l = lista.get(position);
        holder.nevText.setText(l.getNev());
        holder.leirasText.setText(l.getLeiras());

        Glide.with(holder.itemView.getContext())
                .load(l.getKepUrl())
                .into(holder.kepImage);

        Animation fadeIn = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
        holder.itemView.startAnimation(fadeIn);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class LatvanyossagViewHolder extends RecyclerView.ViewHolder {

        private List<Latvanyossag> items;
        TextView nevText, leirasText;
        ImageView kepImage;

        public LatvanyossagViewHolder(View itemView) {
            super(itemView);
            nevText = itemView.findViewById(R.id.textViewNev);
            leirasText = itemView.findViewById(R.id.textViewLeiras);
            kepImage = itemView.findViewById(R.id.imageView);
        }


    }
}