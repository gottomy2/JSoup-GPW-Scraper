package gpw.parser;

import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
	public static void main(String args[]) throws IOException {
		Document doc = Jsoup.connect("https://www.gpw.pl/spolki").get();
		Element table = doc.getElementById("lista-spolek");
		Elements rows = table.select("tr");
		
		FileWriter writer = new FileWriter("save.txt"); // create a writer to save results to a file 'save.txt'
		
		//Sort the table to show the most profitable ones:
		Element thead = doc.getElementsByTag("thead").first();
		Element kurs = thead.select("tr").select("th").get(1);
		Element change = thead.select("tr").select("th").get(2);
		kurs.attr("class","footable-visible footable-sortable");
		change.attr("class","footable-visible footable-sortable  footable-last-column footable-sorted-desc");
		
		//Scrap The table:
		for(int i = 1; i< rows.size(); i++) {
			 Element row = rows.get(i);
			 Elements cols = row.select("td");
			 
			 Element company = cols.get(0).select("a").first();
			 Element procent = cols.get(2);
			 if(!procent.text().contains("-")) {
				 try {
					 String message= 
							 "| Company: " + company.text() +" |" + " | Change: " + procent.text() + " |\n";
					 writer.write(message);
				 }
				 catch(IOException e) {
					 e.printStackTrace();
				 }
			 }
		}
		writer.close();
		System.out.println("===Search Complete!===");
		System.out.println(rows.size() + " rows has been scanned, results have been stored in save.txt file!");
	}
}
