package afpa.shop.entity;

import java.io.Serializable;

public class Product implements Serializable {
	@Override
	public String toString() {
		int taille=nom.length()+10;
		String temp= " ";
		for (int i=0; i<(30-nom.length()); i++) {
			temp+=" ";
		}
		return ""+nom+""+temp+quantite+ "                        " + prix;
	}
	
	public String toStringWrite() {
		
		return ""+id+ ","+nom+ ","+prix+","+TVA+","+quantite+"";
	}

	private String id;
	private String nom;
	private double TVA;
	private double prix;
	private int quantite;
	
	public Product() {
		id="";
		nom="";
		prix=0;
		TVA=0;
		quantite=0;
		
	}
	public Product(String id, String nom, double prix, double TVA, int quantite) {
		this.id = id;
		this.nom = nom;
		this.TVA = TVA;
		this.prix = prix;
		this.quantite = quantite;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public double getTVA() {
		return TVA;
	}

	public void setTVA(double d) {
		TVA = d;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double d) {
		this.prix = d;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantité) {
		this.quantite = quantité;
	}
	
	
}
