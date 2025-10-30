package com.ac2.catalogofilmes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ac2.catalogofilmes.model.Filme;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "catalogo_filmes.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_FILME = "Filme";
	public static final String COL_ID = "id";
	public static final String COL_TITULO = "titulo";
	public static final String COL_DIRETOR = "diretor";
	public static final String COL_ANO = "ano";
	public static final String COL_NOTA = "nota";
	public static final String COL_GENERO = "genero";
	public static final String COL_VIU_CINEMA = "viuNoCinema";

	private static final String CREATE_TABLE_FILME =
			"CREATE TABLE " + TABLE_FILME + " (" +
				COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COL_TITULO + " TEXT NOT NULL, " +
				COL_DIRETOR + " TEXT, " +
				COL_ANO + " INTEGER, " +
				COL_NOTA + " INTEGER, " +
				COL_GENERO + " TEXT, " +
				COL_VIU_CINEMA + " INTEGER" +
			")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_FILME);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILME);
		onCreate(db);
	}

	public long inserirFilme(Filme filme) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COL_TITULO, filme.getTitulo());
		values.put(COL_DIRETOR, filme.getDiretor());
		values.put(COL_ANO, filme.getAno());
		values.put(COL_NOTA, filme.getNota());
		values.put(COL_GENERO, filme.getGenero());
		values.put(COL_VIU_CINEMA, filme.isViuNoCinema() ? 1 : 0);
		return db.insert(TABLE_FILME, null, values);
	}

	public int atualizarFilme(Filme filme) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COL_TITULO, filme.getTitulo());
		values.put(COL_DIRETOR, filme.getDiretor());
		values.put(COL_ANO, filme.getAno());
		values.put(COL_NOTA, filme.getNota());
		values.put(COL_GENERO, filme.getGenero());
		values.put(COL_VIU_CINEMA, filme.isViuNoCinema() ? 1 : 0);
		return db.update(TABLE_FILME, values, COL_ID + "=?", new String[]{String.valueOf(filme.getId())});
	}

	public int excluirFilme(int id) {
		SQLiteDatabase db = getWritableDatabase();
		return db.delete(TABLE_FILME, COL_ID + "=?", new String[]{String.valueOf(id)});
	}

	public List<Filme> listarFilmes() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_FILME, null, null, null, null, null, COL_TITULO + " ASC");
		return cursorParaLista(c);
	}

	public List<Filme> filtrarPorGenero(String genero) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_FILME, null, COL_GENERO + "=?", new String[]{genero}, null, null, COL_TITULO + " ASC");
		return cursorParaLista(c);
	}

	private List<Filme> cursorParaLista(Cursor c) {
		List<Filme> lista = new ArrayList<>();
		if (c == null) return lista;
		try {
			while (c.moveToNext()) {
				Filme f = new Filme();
				f.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
				f.setTitulo(c.getString(c.getColumnIndexOrThrow(COL_TITULO)));
				f.setDiretor(c.getString(c.getColumnIndexOrThrow(COL_DIRETOR)));
				f.setAno(c.getInt(c.getColumnIndexOrThrow(COL_ANO)));
				f.setNota(c.getInt(c.getColumnIndexOrThrow(COL_NOTA)));
				f.setGenero(c.getString(c.getColumnIndexOrThrow(COL_GENERO)));
				f.setViuNoCinema(c.getInt(c.getColumnIndexOrThrow(COL_VIU_CINEMA)) == 1);
				lista.add(f);
			}
		} finally {
			c.close();
		}
		return lista;
	}
}
