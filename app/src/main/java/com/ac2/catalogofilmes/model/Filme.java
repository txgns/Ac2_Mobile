package com.ac2.catalogofilmes.model;

public class Filme {
	private int id;
	private String titulo;
	private String diretor;
	private int ano;
	private int nota;
	private String genero;
	private boolean viuNoCinema;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDiretor() {
		return diretor;
	}

	public void setDiretor(String diretor) {
		this.diretor = diretor;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public boolean isViuNoCinema() {
		return viuNoCinema;
	}

	public void setViuNoCinema(boolean viuNoCinema) {
		this.viuNoCinema = viuNoCinema;
	}
}
