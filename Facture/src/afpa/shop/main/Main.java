package afpa.shop.main;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.google.zxing.WriterException;

import com.itextpdf.text.DocumentException;


import afpa.shop.main.*;
import afpa.shop.entity.*;
import afpa.shop.services.*;

public class Main {

	public static void main(String[] args) throws IOException, AddressException, MessagingException, DocumentException, URISyntaxException, WriterException {
		// TODO Auto-generated method stub
		ShopServices fs = new ShopServices();
		fs.ajouterProduit();
		//FacturationServices.envoieMail();
		//FacturationServices.creationPdfTab();
	}

}


