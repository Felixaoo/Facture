package afpa.shop.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Authenticator;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Stream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import afpa.shop.entity.*;



public class ShopServices implements Serializable {
	private Bill fac;
	private String FICHIER = "C:\\filRouge\\Caisse\\listProduit.txt";
	private static String FICHIERFAC = "C:\\filRouge\\Caisse\\";
	private String FICHIERTRANSAC = "C:\\filRouge\\Caisse\\Transaction\\";
	private String FICHIERCOPIETRANSAC = "C:\\filRouge\\Central\\";
	private Product[] ProductClients;
	private int count;

	public ShopServices() {
		super();
		fac = new Bill();
		count = 0;
		initializeProductsInShop();
		readProductInShop();
		this.ProductClients = new Product[20];
		for (int i = 0; i < ProductClients.length; i++) {
			ProductClients[i] = new Product();
		}
	}

	public void initializeProductsInShop( ) {
		try {
			FileOutputStream f = new FileOutputStream(FICHIER,true);
			ObjectOutputStream oos = new ObjectOutputStream(f); 
			Product p = new Product("00001111", "coca   ", 0.2, 1.5, 0); count ++;
			Product p2 = new Product("00001112", "Meo expresso", 0.2, 1, 0); count ++;
			Product p3 = new Product("00001113", "Meo Senseo", 0.2, 1, 0); count ++;
			Product p4 = new Product("00001114", "DVD", 0.3, 15, 0); count ++;
			Product p5 = new Product("00001115", "Nouilles", 0.196, 3, 0); count ++;
			oos.writeObject(p); oos.writeObject(p2); oos.writeObject(p3); oos.writeObject(p4); oos.writeObject(p5);
			oos.close();
		}
		catch (Exception e) {
			System.out.println("Erreur " +e);
		}
	}

	public void readProductInShop( ) {
		try {
			FileInputStream f = new FileInputStream(FICHIER);
			ObjectInputStream ois = new ObjectInputStream(f);
			for (int i = 0; i < count; i ++) {
				fac.getProducts()[i] = (Product) ois.readObject();
			}
			ois.close();

		}
		catch (Exception e) {
			System.out.println("Erreur " + e);
		}
	}


	public void ajouterProduit() throws IOException, DocumentException, URISyntaxException, AddressException, MessagingException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Merci de scanner votre article ou entrez e pour finaliser votre facture");
		String scan = sc.nextLine();
		int i = 1;
		boolean alreadyInBasket = false;
		while (! "e".equals(scan) && i < ProductClients.length) {
			boolean found = false;
			for (Product p: fac.getProducts()) {
				if (scan.equals(p.getId())) {
					found = true;
					for (Product p2: ProductClients) {
						if (! "".equals(p2.getId())) {
							if (p2.getId().equals(p.getId())) {
								alreadyInBasket = true;
								p2.setQuantite(p2.getQuantite() +1);
								break;
							}
							else {
								continue;
							}
						}
						else if (i == ProductClients.length) {
							System.out.println("Vous avez atteint le maximum d'ajout de nouveau produit. "
									+ " Vous pouvez modifier la quantité d'un produit ou editer la facture");
							break;
						}		
						if (! alreadyInBasket) {
							p2.setId(scan);
							p2.setNom(p.getNom());
							p2.setPrix(p.getPrix());
							p2.setQuantite(1);
							p2.setTVA(p.getTVA());
							i++;
							break;
						}
					}
					alreadyInBasket = false;
				}

			}
			if (! found) {
				System.err.println("Ce produit n'est pas connu");
			}

			Scanner sc2 = new Scanner(System.in);
			System.out.println("Merci de scanner votre article ou entrez e pour finaliser votre facture");
			scan = sc.nextLine();
		}
		facturation();

	}

	public double somme() {
		double somme = 0;
		for (Product p: ProductClients) {
			if (p.getId() != null) {
				somme += p.getPrix() * p.getQuantite();
			}
		}
		return somme;
	}

	public double taxe() {
		double somme = 0;
		for (Product p: ProductClients) {
			if (p.getId() != null) {
				somme += (((p.getPrix() * p.getQuantite() * p.getTVA()) * 100) / 100);
			}
		}
		return somme;
	}

	public int getTotalQuanity() {
		int qte = 0; 
		for (Product p: ProductClients) {
			if (p.getId() != null) {
				qte += p.getQuantite();
			}
		}
		return qte;
	}

	public String getProduct(String produit) {
		for (Product p: ProductClients) {
			if (! "".equals(p.getId())) {
				produit += p + "\r\n";
			}
		}
		return produit;
	}


	public void facturation() throws IOException, DocumentException, URISyntaxException, AddressException, MessagingException {
		String concat = "";
		String produit = "";
		double total = somme();
		DecimalFormat df = new DecimalFormat("#.##");
		double taxe = taxe();
		double totalTTC = total + taxe;
		int qte = getTotalQuanity();
		concat +=  "                    AFPA" + "\r\n\r\n" + "             " + fac.getAdresse() + "\r\n\r\n" + "                03 00 00 00 00" + "\r\n\r\n" + "Article    Quantité    Prix " + "\r\n"
				+ getProduct(produit) + "_______________________________________________________"
				+ "\r\n" + "Total                   " + total + " Euros" + "\r\n" + "_______________________________________________________" 
				+ "\r\n" + "TVA                     " + df.format(taxe) + "\r\n" + "Total TTC:              " + df.format(totalTTC)
				+ "\r\n" + "_______________________________________________________" + "\r\n" + "                " +  qte + " Articles" 
				+ "\r\n" + "_______________________________________________________" + "\r\n" 
				+ "Date : " + fac.getDate() + "    " + fac.getTime(); 
		String fileCreatedName = creationPdf(concat);
		System.out.println("Merci d'indiquer une adresse email pour l'envoi de la facture:");
		Scanner sc = new Scanner(System.in);
		String email = sc.nextLine();
		
		fichierTransaction(totalTTC);
		envoieMail(email, fileCreatedName);
	}


	public void fichierTransaction(double totalTTC) throws IOException {
		Scanner sc=new Scanner(System.in);
		System.out.println("Veuillez entrer le numéro de carte bancaire du client" );
		String numCb=sc.nextLine();
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"); // reformatage de la date
		String dateDef = date.format(formatters);
		LocalDate parsedDate = LocalDate.parse(dateDef, formatters);
		FileWriter fw=new FileWriter(FICHIERTRANSAC+"transaction"+"_"+numCb+"_"+dateDef+".txt");
		BufferedWriter bw=new BufferedWriter(fw);
		bw.write("D; Carrouf; "+String.format(Locale.ROOT, "%.2f", totalTTC)+"; "+dateDef); 
		bw.newLine();
		bw.close();
	}

	public static void envoieMail(String email, String fileCreatedName) throws AddressException, MessagingException {
		String from = "idpjava2019@gmail.com";
		String password = "@Afpa2019";
		String to = email;

		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		message.setSubject("Caisse Invoice");

		MimeBodyPart messageBodyPart = new MimeBodyPart();

        Multipart multipart = new MimeMultipart();

        messageBodyPart = new MimeBodyPart();        
        String file = fileCreatedName;
        String fileName = "Facture_Caisse.pdf";
        DataSource source = new FileDataSource(file);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);
        messageBodyPart.setText("Bonjour, vous trouverez en pièce jointe la facture de Caisse du " + fileCreatedName.substring(27, 35));
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);

		
		
		
		Transport.send(message);
	}

	public static String creationPdf(String concat) throws DocumentException, IOException  {
		String fileCreatedName = "";
		Document document = new Document( PageSize.A5, 10, 10, 10, 0 );
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
		String text = date.format(formatters);
		
		try {
			PdfWriter.getInstance(document, new FileOutputStream(FICHIERFAC+"invoice_"+text+".pdf"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		document.open();
		BaseFont courier = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.EMBEDDED);
        Font myfont = new Font(courier);
		Paragraph p = new Paragraph(concat, myfont);

		try {
			
		    document.add( new Paragraph(p) );
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		document.close();
		fileCreatedName  = FICHIERFAC+"invoice_"+text+".pdf";
		return fileCreatedName;
	}
	
}

