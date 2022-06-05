package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Adiacenza;
import it.polito.tdp.genes.model.Genes;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Genes> getVertici() {
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM genes "
				+ "WHERE Essential = 'Essential'";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Adiacenza getAdiacenza(Genes g1, Genes g2, Map<String, Genes> idMap) {
		String sql = "SELECT GeneID1 AS g1, GeneID2 AS g2, Expression_Corr AS peso "
				+ "FROM interactions "
				+ "WHERE (GeneID1 = ? OR GeneID2 = ?) "
				+ "AND (GeneID1 = ? OR GeneID2 = ?)";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, g1.getGeneId());
			st.setString(2, g1.getGeneId());
			st.setString(3, g2.getGeneId());
			st.setString(4, g2.getGeneId());
			ResultSet res = st.executeQuery();
			Adiacenza adiacenza = null;
			
			while (res.next()) {
				adiacenza = new Adiacenza(idMap.get(res.getString("g1")), idMap.get(res.getString("g2")), res.getDouble("peso")); 				
			}
			
			
			res.close();
			st.close();
			conn.close();
			return adiacenza;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Adiacenza> getArchi(Map<String, Genes> idMap) {
		String sql = "SELECT DISTINCT i.GeneID1 AS g1, i.GeneID2 AS g2, i.Expression_Corr AS peso "
				+ "FROM interactions AS i "
				+ "WHERE i.GeneID1 <> i.GeneID2 "
				+ "AND i.GeneID1 IN (SELECT DISTINCT GeneID "
				+ "						FROM genes "
				+ "						WHERE Essential = 'Essential') "
				+ "AND i.GeneID2 IN (SELECT DISTINCT GeneID "
				+ "						FROM genes "
				+ "						WHERE Essential = 'Essential')";
		
		Connection conn = DBConnect.getConnection() ;
		
		List<Adiacenza> result = new ArrayList<>();
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Genes gene1 = idMap.get(res.getString("g1")) ;
				Genes gene2 = idMap.get(res.getString("g2")) ;
				
				if( gene1!=null && gene2!=null && !gene1.equals(gene2) ) {
					result.add(new Adiacenza(gene1, gene2,res.getDouble("peso"))) ;
				}
			}
			conn.close();
			return result ;
		} catch(SQLException ex) {
			throw new RuntimeException("Database error", ex) ;
		}
	}
}
