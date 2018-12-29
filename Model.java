package hw3;

//Sahib Singh
//AndrewId: sahibsin

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

// Class storing the layout of the application
public class Model {

	public static ObservableMap<String, Product> productsMap = FXCollections.observableHashMap();
	public static ObservableMap<String, Nutrient> nutrientsMap = FXCollections.observableHashMap();
	ObservableList<Product> searchResultsList = FXCollections.observableArrayList();
	ObservableList<Product> dietProductsList = FXCollections.observableArrayList();;

	/*
	Reads NutriByte.PRODUCT_FILE file to load product objects in the productsMap
	 */
	public void readProducts(String filename) {
		// TODO Auto-generated method stub
			
			CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();			
			try {
				CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
				for (CSVRecord csvRecord : csvParser) {
					String ndbNo = csvRecord.get(0);
					Product product = new Product(ndbNo, csvRecord.get(1), csvRecord.get(4), csvRecord.get(7));
					productsMap.put(ndbNo, product);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace(); 
			}
		
	}
	
	/*
	 * Reads NutriByte.NUTRIENT_FILE to load objects in the nutrients and products maps. Note that
	the nutrientsMap will hold only unique nutrient objects.
	 */
	public void readNutrients(String filename) {
		// TODO Auto-generated method stub
		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		
		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			for (CSVRecord csvRecord : csvParser) {

				String ndbNo = csvRecord.get(1);
				Nutrient nutrient = new Nutrient(ndbNo, csvRecord.get(2), csvRecord.get(5));
				nutrientsMap.put(ndbNo, nutrient);
				
				Product prod = productsMap.get(csvRecord.get(0));
				
				if (!prod.productNutrients.containsKey(ndbNo)){
					if(Float.parseFloat(csvRecord.get(4))>0) {
						Product product = new Product();	
						Product.ProductNutrient pn =  product.new ProductNutrient(ndbNo, Float.parseFloat(csvRecord.get(4))); 
						prod.productNutrients.put(ndbNo, pn);
					}
				}
				
			}			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * :Reads NutriByte.SERVING_SIZE_FILE to populate four fields – servingSize, servingUom,
	householdSize, householdUom - in each product object in the productsMaps.
	 */
	public void readServingSizes(String filename) {
		// TODO Auto-generated method stub
		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		String ndbNo = null;
		float servingSize = 0;
		String servingUom = null;
		float hhSize = 0;
		String hhUom = null;
		
		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			for (CSVRecord csvRecord : csvParser) {
				
				if (!(csvRecord.get(0).isEmpty())||(csvRecord.get(0) ==null))
					ndbNo= csvRecord.get(0);

				if (!(csvRecord.get(1).isEmpty())||(csvRecord.get(1) ==null))
					servingSize= Float.parseFloat(csvRecord.get(1));

				if (!(csvRecord.get(2).isEmpty())||(csvRecord.get(2) ==null))
					servingUom= csvRecord.get(2);

				if (!(csvRecord.get(3).isEmpty())||(csvRecord.get(3) ==null))
					hhSize= Float.parseFloat(csvRecord.get(3));
				
				if (!(csvRecord.get(4).isEmpty())||(csvRecord.get(4) ==null))
					hhUom= csvRecord.get(4);


		    	if (!(ndbNo.isEmpty())) {
		    		productsMap.get(ndbNo).servingSize.set(servingSize);
			    	productsMap.get(ndbNo).servingUom.set(servingUom);
			    	productsMap.get(ndbNo).householdSize.set(hhSize);
			    	productsMap.get(ndbNo).householdUom.set(hhUom);			    	
			    }
			}
							
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
			}
		catch (IOException e) { 
			e.printStackTrace();
			}
		
	}
	
	//reads the profile file chosen by the user
	public boolean readProfiles(String filename) {
		// TODO Auto-generated method stub
		String fileExt = filename.substring(filename.length()-3, filename.length());
		
		if(fileExt.equals("csv")) {
			CSVFiler file = new CSVFiler();
			return file.readFile(filename);
		}
		else if(fileExt.equals("xml")) {
			XMLFiler file = new XMLFiler();
			return file.readFile(filename);
		}
		
		return false;
	}
	
	//Writing the output to csv file
	void writeProfile(String data, String filename) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(new File(filename));
	        StringBuilder sb = new StringBuilder();
	        
	        String[] dataArray = data.split(",");
	        String gender = dataArray[0];

	    	float age = Float.parseFloat(dataArray[1]);
	    	float weight = Float.parseFloat(dataArray[2]);
	    	float height = Float.parseFloat(dataArray[3]);
	    	float physicalActivityLevel = Float.parseFloat(dataArray[4]);
	    	
	        sb.append(gender+",");
	        sb.append(age+",");
	        sb.append(weight+",");
	        sb.append(height+",");
	        sb.append(physicalActivityLevel+",");
	    	for (int i =5;i<dataArray.length;i++) //Appending Interests
	    		sb.append(dataArray[i]+",");

	        
	        sb.append('\n');
	        
	        if (dietProductsList.size() > 0)
	        	for (int i=0;i<dietProductsList.size();i++) {
	        		sb.append(dietProductsList.get(i).ndbNumber.get()+",");
	        		sb.append(dietProductsList.get(i).getServingSize()+",");
	        		sb.append(dietProductsList.get(i).getHouseholdSize());
	    	        sb.append('\n');
	        	}
	        
	        sb.append('\n');
	        pw.write(sb.toString());
	        pw.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
