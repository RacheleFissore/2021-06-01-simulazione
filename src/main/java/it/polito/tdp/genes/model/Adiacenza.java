package it.polito.tdp.genes.model;

public class Adiacenza implements Comparable<Adiacenza> {
	private Genes g1;
	private Genes g2;
	private Double peso;
	
	public Adiacenza(Genes g1, Genes g2, Double peso) {
		super();
		this.g1 = g1;
		this.g2 = g2;
		this.peso = peso;
	}

	public Genes getG1() {
		return g1;
	}

	public void setG1(Genes g1) {
		this.g1 = g1;
	}

	public Genes getG2() {
		return g2;
	}

	public void setG2(Genes g2) {
		this.g2 = g2;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(Adiacenza o) {
		// TODO Auto-generated method stub
		return o.peso.compareTo(this.peso);
	}

	@Override
	public String toString() {
		return g2 + " " + peso + "\n";
	}
	
	
	
}
