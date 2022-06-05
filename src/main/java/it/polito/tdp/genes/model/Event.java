package it.polito.tdp.genes.model;

public class Event implements Comparable<Event> {
	private Genes geneStudiato;
	private int mese;
	private int nIng;
	
	public Event(Genes geneStudiato, int mese, int nIng) {
		super();
		this.geneStudiato = geneStudiato;
		this.mese = mese;
		this.nIng = nIng;
	}

	public Genes getGeneStudiato() {
		return geneStudiato;
	}

	public void setGeneStudiato(Genes geneStudiato) {
		this.geneStudiato = geneStudiato;
	}

	public int getMese() {
		return mese;
	}

	public void setMese(int mese) {
		this.mese = mese;
	}

	public int getnIng() {
		return nIng;
	}

	public void setnIng(int nIng) {
		this.nIng = nIng;
	}

	@Override
	public int compareTo(Event o) {
		return this.mese-o.mese;
	}
}
