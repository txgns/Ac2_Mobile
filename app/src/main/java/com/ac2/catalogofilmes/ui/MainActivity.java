package com.ac2.catalogofilmes.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ac2.catalogofilmes.R;
import com.ac2.catalogofilmes.data.DatabaseHelper;
import com.ac2.catalogofilmes.model.Filme;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FilmeAdapter.OnItemClickListener {

	private RecyclerView recyclerFilmes;
	private Spinner spinnerFiltro;
	private Button btnAdicionar;
	private FilmeAdapter adapter;
	private DatabaseHelper db;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new DatabaseHelper(this);

		recyclerFilmes = findViewById(R.id.recyclerFilmes);
		spinnerFiltro = findViewById(R.id.spinnerFiltro);
		btnAdicionar = findViewById(R.id.btnAdicionar);

		recyclerFilmes.setLayoutManager(new LinearLayoutManager(this));
		adapter = new FilmeAdapter(this);
		recyclerFilmes.setAdapter(adapter);

		ArrayAdapter<CharSequence> filtroAdapter = ArrayAdapter.createFromResource(
				this, R.array.filtro_generos, android.R.layout.simple_spinner_item);
		filtroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerFiltro.setAdapter(filtroAdapter);

		btnAdicionar.setOnClickListener(v -> abrirForm(null));

		spinnerFiltro.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
				atualizarLista();
			}

			@Override
			public void onNothingSelected(android.widget.AdapterView<?> parent) {
				atualizarLista();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		atualizarLista();
	}

	private void atualizarLista() {
		String generoSelecionado = spinnerFiltro.getSelectedItem() != null ? spinnerFiltro.getSelectedItem().toString() : getString(R.string.todos);
		List<Filme> filmes;
		if (generoSelecionado.equals(getString(R.string.todos))) {
			filmes = db.listarFilmes();
		} else {
			filmes = db.filtrarPorGenero(generoSelecionado);
		}
		adapter.submitList(filmes);
	}

	private void abrirForm(@Nullable Filme filme) {
		Intent i = new Intent(this, FilmeFormActivity.class);
		if (filme != null) {
			i.putExtra("id", filme.getId());
			i.putExtra("titulo", filme.getTitulo());
			i.putExtra("diretor", filme.getDiretor());
			i.putExtra("ano", filme.getAno());
			i.putExtra("nota", filme.getNota());
			i.putExtra("genero", filme.getGenero());
			i.putExtra("viu", filme.isViuNoCinema());
		}
		startActivity(i);
	}

	@Override
	public void onItemClick(Filme filme) {
		abrirForm(filme);
	}

	@Override
	public void onItemLongClick(Filme filme) {
		new AlertDialog.Builder(this)
				.setTitle(R.string.excluir)
				.setMessage(R.string.confirmar_exclusao)
				.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int linhas = db.excluirFilme(filme.getId());
						if (linhas > 0) {
							Toast.makeText(MainActivity.this, R.string.filme_excluido, Toast.LENGTH_SHORT).show();
							atualizarLista();
						}
					}
				})
				.setNegativeButton(R.string.nao, null)
				.show();
	}
}
