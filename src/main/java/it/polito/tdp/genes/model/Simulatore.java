package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulatore {
	private int numIng;
	private Genes genePartenza;
	private Map<Genes, Integer> statistiche;
	private PriorityQueue<Event> queue;
	private int tMax = 36; // Numero mesi di ricerca
	private Graph<Genes, DefaultWeightedEdge> grafo;
	private List<Genes> geniStudiati;

	public void init(int num, Genes gene, Graph<Genes, DefaultWeightedEdge> grafoGenes) {
		numIng = num;
		genePartenza = gene;
		statistiche = new HashMap<>();
		queue = new PriorityQueue<>();
		geniStudiati = new ArrayList<>();
		grafo = grafoGenes;
		creaEventi();		
	}

	private void creaEventi() {
		for(int i = 0; i < numIng; i++) {
			// Parto dal mese 0 per tutti gli ingegneri
			if(grafo.containsVertex(genePartenza))
				queue.add(new Event(genePartenza, 0, i));
			
			geniStudiati.add(genePartenza);
		}
	}

	public void run() {
		while(!queue.isEmpty()) {
			Event e = queue.poll();
			processaEvento(e); // Eseguo l'evento
		}
	}

	private void processaEvento(Event e) {
		int mese = e.getMese();
		int nIngEv = e.getnIng();
		Genes geneStud = e.getGeneStudiato();
		if(mese < tMax) { // Posso processare eventi solo finchè non raggiungo i 3 anni
			double prob = Math.random();
			if(prob <= 0.3) {
				queue.add(new Event(geneStud, mese+1, nIngEv));
			}
			else {
				// Calcola la somma dei pesi degli adiacenti, S
				double S = 0 ;
				for(DefaultWeightedEdge edge: this.grafo.edgesOf(geneStud)) {
					S += this.grafo.getEdgeWeight(edge) ;
				}
				
				// Estrai numero casuale R tra 0 e S
				double R = Math.random()*S ;
				
				// confronta R con le somme parziali dei pesi
				Genes nuovo = null ;
				double somma = 0.0 ;
				for(DefaultWeightedEdge edge: this.grafo.edgesOf(geneStud)) {
					somma += this.grafo.getEdgeWeight(edge) ;
					if(somma > R) {
						nuovo = Graphs.getOppositeVertex(this.grafo, edge, geneStud) ;
						break ;
					}
				}
				
				geniStudiati.set(nIngEv, nuovo);
				
				if(nuovo != null)
					queue.add(new Event(nuovo, mese+1, nIngEv));
				else 
					queue.add(new Event(geneStud, mese+1, nIngEv));
				
			}
		}
		
	}

	public String getStatistiche() {
		for(Genes genes : geniStudiati) {
			if(statistiche.containsKey(genes)) {
				statistiche.put(genes, statistiche.get(genes)+1); // Incremento il numero di ingegneri che studiano 
																							  // quel gene
			}
			else {
				statistiche.put(genes, 1); // Per ora ho solo 1 ingegnere che studia quel gene
			}
		}
		
		String string = "\n\n";
		
		for(Genes genes : statistiche.keySet()) {
			string += "Gene studiato: " + genes.getGeneId() + ", numero ingegneri:  " + statistiche.get(genes) + "\n";
		}
		return string;
	}
	
}
