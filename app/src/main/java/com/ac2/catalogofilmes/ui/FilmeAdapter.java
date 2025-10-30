package com.ac2.catalogofilmes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ac2.catalogofilmes.R;
import com.ac2.catalogofilmes.model.Filme;

import java.util.ArrayList;
import java.util.List;

public class FilmeAdapter extends RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder> {
	public interface OnItemClickListener {
		void onItemClick(Filme filme);
		void onItemLongClick(Filme filme);
	}

	private final List<Filme> filmes = new ArrayList<>();
	private final OnItemClickListener listener;

	public FilmeAdapter(OnItemClickListener listener) {
		this.listener = listener;
	}

	public void submitList(List<Filme> novaLista) {
		filmes.clear();
		if (novaLista != null) {
			filmes.addAll(novaLista);
		}
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public FilmeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filme, parent, false);
		return new FilmeViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull FilmeViewHolder holder, int position) {
		Filme f = filmes.get(position);
		holder.txtTitulo.setText(f.getTitulo());
		holder.txtAno.setText(String.valueOf(f.getAno()));
		holder.txtGenero.setText(f.getGenero());
		holder.txtNota.setText("Nota: " + f.getNota() + "/5");

		holder.itemView.setOnClickListener(v -> listener.onItemClick(f));
		holder.itemView.setOnLongClickListener(v -> {
			listener.onItemLongClick(f);
			return true;
		});
	}

	@Override
	public int getItemCount() {
		return filmes.size();
	}

	static class FilmeViewHolder extends RecyclerView.ViewHolder {
		TextView txtTitulo;
		TextView txtAno;
		TextView txtGenero;
		TextView txtNota;

		FilmeViewHolder(@NonNull View itemView) {
			super(itemView);
			txtTitulo = itemView.findViewById(R.id.txtTitulo);
			txtAno = itemView.findViewById(R.id.txtAno);
			txtGenero = itemView.findViewById(R.id.txtGenero);
			txtNota = itemView.findViewById(R.id.txtNota);
		}
	}
}
