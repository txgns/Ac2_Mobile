package com.ac2.catalogofilmes.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ac2.catalogofilmes.R;
import com.ac2.catalogofilmes.data.DatabaseHelper;
import com.ac2.catalogofilmes.model.Filme;

public class FilmeFormActivity extends AppCompatActivity {
	private EditText edtTitulo, edtDiretor, edtAno, edtNota;
	private Spinner spinnerGenero;
	private CheckBox checkCinema;
	private Button btnSalvar;

	private DatabaseHelper db;
	private Integer filmeId = null;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filme_form);

		db = new DatabaseHelper(this);

		edtTitulo = findViewById(R.id.edtTitulo);
		edtDiretor = findViewById(R.id.edtDiretor);
		edtAno = findViewById(R.id.edtAno);
		edtNota = findViewById(R.id.edtNota);
		spinnerGenero = findViewById(R.id.spinnerGenero);
		checkCinema = findViewById(R.id.checkCinema);
		btnSalvar = findViewById(R.id.btnSalvar);

		ArrayAdapter<CharSequence> generoAdapter = ArrayAdapter.createFromResource(
				this, R.array.generos, android.R.layout.simple_spinner_item);
		generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerGenero.setAdapter(generoAdapter);

		if (getIntent() != null && getIntent().hasExtra("id")) {
			setTitle(R.string.editar_filme);
			filmeId = getIntent().getIntExtra("id", -1);
			edtTitulo.setText(getIntent().getStringExtra("titulo"));
			edtDiretor.setText(getIntent().getStringExtra("diretor"));
			int ano = getIntent().getIntExtra("ano", 0);
			edtAno.setText(ano == 0 ? "" : String.valueOf(ano));
			int nota = getIntent().getIntExtra("nota", 0);
			edtNota.setText(nota == 0 ? "" : String.valueOf(nota));
			String genero = getIntent().getStringExtra("genero");
			if (!TextUtils.isEmpty(genero)) {
				int pos = generoAdapter.getPosition(genero);
				if (pos >= 0) spinnerGenero.setSelection(pos);
			}
			checkCinema.setChecked(getIntent().getBooleanExtra("viu", false));
		} else {
			setTitle(R.string.adicionar_filme);
		}

		btnSalvar.setOnClickListener(v -> salvar());
	}

	private void salvar() {
		String titulo = edtTitulo.getText().toString().trim();
		String diretor = edtDiretor.getText().toString().trim();
		String anoStr = edtAno.getText().toString().trim();
		String notaStr = edtNota.getText().toString().trim();
		int ano = TextUtils.isEmpty(anoStr) ? 0 : Integer.parseInt(anoStr);
		int nota = TextUtils.isEmpty(notaStr) ? 0 : Integer.parseInt(notaStr);
		String genero = spinnerGenero.getSelectedItem() != null ? spinnerGenero.getSelectedItem().toString() : "";
		boolean viu = checkCinema.isChecked();

		if (TextUtils.isEmpty(titulo)) {
			Toast.makeText(this, R.string.erro_titulo_obrigatorio, Toast.LENGTH_SHORT).show();
			return;
		}
		if (nota < 1 || nota > 5) {
			Toast.makeText(this, "A nota deve ser um número de 1 a 5.", Toast.LENGTH_SHORT).show();
			return;
		}

		Filme f = new Filme();
		f.setTitulo(titulo);
		f.setDiretor(diretor);
		f.setAno(ano);
		f.setNota(nota);
		f.setGenero(genero);
		f.setViuNoCinema(viu);

		if (filmeId == null) {
			long id = db.inserirFilme(f);
			if (id > 0) {
				Toast.makeText(this, R.string.filme_salvo, Toast.LENGTH_SHORT).show();
				finish();
			}
		} else {
			f.setId(filmeId);
			int linhas = db.atualizarFilme(f);
			if (linhas > 0) {
				Toast.makeText(this, R.string.filme_atualizado, Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}
}
