package afpa.shop.entity;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Scanner;

import afpa.shop.services.ShopServices;

public class Bill implements Serializable{
	private String date;
	private String adresse;
	private LocalTime time;
	private Product[]products = new Product[10];

	public Bill() {
		this.adresse = "www.idp-JAVA_JEE.fr";
		for (int i=0; i<products.length; i++) {
			products[i]=new Product();
		}
	}
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Product[] getProducts() {
		return products;
	}

	public void setProducts(Product[] products) {
		this.products = products;
	}
	
	
	public LocalTime getTime() {
		return time;
	}
	public void setTime(LocalTime time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Bill [date=" + date + ", adresse=" + adresse + ", products=" + Arrays.toString(products) + "]";
	}

}

