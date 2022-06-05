package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	private Graph<Genes, DefaultWeightedEdge> grafo;
	private Map<String, Genes> idMap;
	private GenesDao dao;
	private List<Adiacenza> archi;
	private Simulatore simulatore;
	
	public Model() {
		dao = new GenesDao();
		idMap = new HashMap<>();
		simulatore = new Simulatore();
		
		for(Genes g : dao.getAllGenes()) {
			idMap.put(g.getGeneId(), g);
		}
	}
	
	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici());
		archi = dao.getArchi(idMap);
		for (Adiacenza arco : archi) {
			if(grafo.containsVertex(arco.getG1()) && grafo.containsVertex(arco.getG2())) {
				if (arco.getG1().getChromosome() == arco.getG2().getChromosome()) {
					Graphs.addEdge(this.grafo, arco.getG1(), arco.getG2(), Math.abs(arco.getPeso() * 2.0));
				} else {
					Graphs.addEdge(this.grafo, arco.getG1(), arco.getG2(), Math.abs(arco.getPeso()));
				}
			}
		}
	}
	
	public int nVertici() {
		return grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return grafo.edgeSet().size();
	}
	
	public Set<Genes> getVertici() {
		return grafo.vertexSet();
	}
	
	public List<Adiacenza> getAdiacenze(Genes gP) {
		List<Adiacenza> result = new ArrayList<>();
		
		for(Genes g : Graphs.neighborListOf(grafo, gP)) {
			DefaultWeightedEdge edge = grafo.getEdge(gP, g);
			if(grafo.containsEdge(edge)) {
				Adiacenza adiacenza = new Adiacenza(gP, g, grafo.getEdgeWeight(edge));
				result.add(adiacenza);
			}
		}
		
		Collections.sort(result);
		
		return result;
	}

	public String getStatistiche(int num, Genes gene) {
		simulatore.init(num, gene, grafo);
		simulatore.run();
		return simulatore.getStatistiche();
	}
}
