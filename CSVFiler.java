package hw3;

//Sahib Singh
//AndrewId: sahibsin

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/*
DataFiler’s child class that implements the two abstract methods of DataFiler. 
*/
public class CSVFiler extends DataFiler{
	
	
	Person validatePersonData(String data) {
		
		String[] details = data.split(",");

		// check for male/female
		String gender = null;
		float age = 0;
		float weight = 0;
		boolean status = false;
		boolean check = true;
        StringBuilder ingredientsToWatch = new StringBuilder();

		
        try {
        	gender = details[0].trim();
            if(!(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female")))
                throw new InvalidProfileException("The profile must have gender: Female or Male as first word");
        } catch (Exception e){
            new InvalidProfileException("Could not read profile data");
            check = false;
        }
		
		
        if (check) {
            try {
                age = Float.parseFloat(details[1].trim());
            } catch (NumberFormatException n) {
                new InvalidProfileException("Invalid data for Age: " + details[1] + "\nAge must be a number");
                new InvalidProfileException("Could not read profile data");
                check = false;
            }
        }
 
        if (check) {
            try {
                weight = Float.parseFloat(details[2].trim());
            } catch (NumberFormatException n) {
                new InvalidProfileException("Invalid data for weight: " + details[2] + "\nWeight must be a number");
                new InvalidProfileException("Could not read profile data");
                check = false;
            }
        }      
 
        
        
        if (check) {
            for (NutriProfiler.PhysicalActivityEnum physicalenum : NutriProfiler.PhysicalActivityEnum.values()){
                if (Float.parseFloat(details[4].trim())==physicalenum.getPhysicalActivityLevel()) {
                    status = true;
                }
            }
            if (status == false) {
                new InvalidProfileException("Invalid physical activity level: " + details[4] + "\nMust be 1.0, 1.1, 1.25, or 1.48");
                new InvalidProfileException("Could not read profile data");
                check = false;
            }
        }
       
        if (check) {
            try {
                Float.parseFloat(details[3].trim());
            } catch(NumberFormatException n) {
                new InvalidProfileException("Invalid data for height: " + details[3] + "\nHeight must be a number");
                new InvalidProfileException("Could not read profile data");
                check =false;
            }
        }      
 
   
 
        if (check) {
 
            for (int i = 5;i < details.length;i++){
                ingredientsToWatch.append(details[i].trim()+ ", ");
            }
 
            if (ingredientsToWatch.length() > 0)
                ingredientsToWatch.replace(ingredientsToWatch.toString().length()-2, ingredientsToWatch.toString().length()-1, "");
            
            
            if (age != 0 && weight !=0 && gender != null) {
	            switch (gender.toLowerCase()) {
					case("female"):
						NutriByte.person = new Female(age, weight, Float.parseFloat(details[3].trim()),Float.parseFloat(details[4].trim()), ingredientsToWatch.toString());
						return NutriByte.person;
					case("male"):					
						NutriByte.person = new Male(age, weight, Float.parseFloat(details[3].trim()),Float.parseFloat(details[4].trim()), ingredientsToWatch.toString());
						return NutriByte.person;
				default:
					break;
				}
	        }
        }
		return null;
        
    }
		
	
	
	Product validateProductData(String data) {
		float servingSize = 0;
		float householdSize = 0;
		String prodIdentifier = null;
		
		try {
			
			if(data.split(",").length < 3) {
				throw new InvalidProfileException("Cannot Read: " + data + "\n"+ "The data must be - String, number, number - for ndb number,"+"\n"+"serving size, household size");
			}
			
			if(!Model.productsMap.containsKey(data.split(",")[0].trim())) {
				throw new InvalidProfileException("No product found with this code: " + data.split(",")[0]);
			}
			prodIdentifier = data.split(",")[0].trim();
			
			try {
				servingSize = Float.parseFloat(data.split(",")[1].trim());
			} 
			catch (NumberFormatException e) {
				throw new InvalidProfileException("Cannot Read: " + data + "\n" + 
						"The data must be - String, number, number - for ndb number, serving size, household size");
			}
			
			if(servingSize <= 0) {
				throw new InvalidProfileException("Serving size must be greater than or equal to 0");
			}
			
			try {
				householdSize = Float.parseFloat(data.split(",")[2].trim());
			} catch (NumberFormatException e) {
				throw new InvalidProfileException("Cannot Read: " + data + "\n" + 
						"The data must be - String, number, number - for ndb number, serving size, household size");
			}
			
			if(householdSize <= 0) {
				throw new InvalidProfileException("Household size must be greater than or equal to 0");
			}
			
			Product UpdatedProd = new Product(Model.productsMap.get(prodIdentifier));
			UpdatedProd.setServingSize(servingSize);
			UpdatedProd.setHouseholdSize(householdSize);
			return UpdatedProd;
			
		} catch(InvalidProfileException e) {
			return null;
		}	
	}
	
	/*
 	creates a Male or Female object and
	assigns it to NutriByte.person. Returns true if file read successfully. Returns false otherwise.
	 */
	@Override
	public boolean readFile(String filename) {
		// TODO Auto-generated method stub
	
		CSVFormat csvFormat = CSVFormat.DEFAULT;
		
		try {
			int line = 1;

			CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			
			for(CSVRecord data : csvParser) {
				StringBuilder sb1 = new StringBuilder();

				if(line == 1) {
					for(int i = 0; i < data.size(); i++) {
						sb1.append(data.get(i));
						sb1.append(",");
					}
					
					int sb1length = sb1.length();
					int requiredLength1 = sb1.length()-1;
					
					if(sb1length > 0) {
						sb1.setLength(requiredLength1);
						NutriByte.person = validatePersonData(sb1.toString());
						if(NutriByte.person == null) {return false;}
					}		
					line++;
				} else {
					StringBuilder sb2 = new StringBuilder();

					for(int i = 0; i < data.size(); i++) {
						sb2.append(data.get(i));
						sb2.append(",");
					}
					
					int sb2length =sb2.length();
					int requiredLength2 = sb2.length()-1;
					
					if(sb2length > 0) {
						sb2.setLength(requiredLength2);
						
						Product prod = validateProductData(sb2.toString());
	
						if(prod != null) {NutriByte.model.dietProductsList.add(prod);}
					}					
				}				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	
	@Override
	public void writeFile(String filename) {
		// TODO Auto-generated method stub
		//Not Required
	}

}
