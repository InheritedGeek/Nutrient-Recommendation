
package hw3;

//Sahib Singh
//AndrewId: sahibsin
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/*
A Java bean. It has a default constructor that initializes all string properties to empty strings. Its non-default
constructor initializes ndbNumber, productName, manufacturer, and ingredients.
Its member variable productNutrients is a hash map to store nutrients in the product. The key is nutrientCode and the
value is of type ProductNutrient. 
 */
public class Product {
	
	StringProperty ndbNumber = new SimpleStringProperty();
	StringProperty productName = new SimpleStringProperty();
	StringProperty manufacturer = new SimpleStringProperty();
	StringProperty ingredients = new SimpleStringProperty();
	StringProperty householdUom = new SimpleStringProperty();
	StringProperty servingUom = new SimpleStringProperty();
	FloatProperty servingSize = new SimpleFloatProperty();
	FloatProperty householdSize = new SimpleFloatProperty();
	ObservableMap<String, ProductNutrient> productNutrients = FXCollections.observableHashMap();

	public Product(){
		ndbNumber.set("");
		productName.set("");
		manufacturer.set("");
		ingredients.set("");
		householdUom.set("");
		servingUom.set("");
		servingSize.set(0);
		householdSize.set(0);
	}	
	
	//Non Default Constructor initializing parameters
	public Product(String ndbNumber, String productName, String manafacturer, String ingredients) {
		// TODO Auto-generated constructor stub
		this.ndbNumber.set(ndbNumber);
		this.productName.set(productName);
		this.manufacturer.set(manafacturer);
		this.ingredients.set(ingredients);
	}

	// Non Default Constructor taking Product Object
	public Product(Product product) {
		this.ndbNumber.set(product.ndbNumber.get());
		this.servingSize.set(product.servingSize.get());
		this.servingUom.set(product.servingUom.get());
		this.manufacturer.set(product.manufacturer.get());
		this.ingredients.set(product.ingredients.get());
		this.productName.set(product.productName.get());
		this.householdSize.set(product.householdSize.get());
		this.householdUom.set(product.householdUom.get());
		this.productNutrients.putAll(product.getProductNutrients());
	}

	//Defining Getter and Setters for its attributes
	public String getNdbNumber() {
		return ndbNumber.get();
	}
	public void setNdbNumber(String ndbNumber) {
		this.ndbNumber.set(ndbNumber);
	}
	public String getProductName() {
		return productName.get();
	}
	public void setProductName(String productName) {
		this.productName.set(productName);
	}
	public String getManafacturer() {
		return manufacturer.get();
	}
	public void setManafacturer(String manafacturer) {
		this.manufacturer.set(manafacturer);
	}
	public String getIngredients() {
		return ingredients.get();
	}
	public void setIngredients(String ingredients) {
		this.ingredients.set(ingredients);
	}
	public String getHouseholdUom() {
		return householdUom.get();
	}
	public void setHouseholdUom(String householdUom) {
		this.householdUom.set(householdUom);
	}
	public String getServingUom() {
		return servingUom.get();
	}
	public void setServingUom(String servingUom) {
		this.servingUom.set(servingUom);
	}
	public float getServingSize() {
		return servingSize.get();
	}
	public void setServingSize(float servingSize) {
		this.servingSize.set(servingSize);
	}
	public float getHouseholdSize() {
		return householdSize.get();
	}
	public void setHouseholdSize(float householdSize) {
		this.householdSize.set(householdSize);
	}
	public ObservableMap<String, ProductNutrient> getProductNutrients() {
		return productNutrients;
	}
	
	public void setProductNutrients(ObservableMap<String, ProductNutrient> productNutrients) {
		this.productNutrients = productNutrients;
	}
		
	@Override
	public String toString() {
		return productName.get() + " by "+ getManafacturer();
	}

	/*
	 * ProductNutrient is a Java bean defined as an inner class in Product class. It has nutrientCode and nutrientQuantity as
	its two properties. Its default constructor initializes nutrientCode to empty string. Its non-default constructor
	initializes nutrientCode and nutrientQuantity. 
	 */
	class ProductNutrient {
		StringProperty nutrientCode = new SimpleStringProperty();
		FloatProperty nutrientQuantity = new SimpleFloatProperty();
		
		public ProductNutrient() {
			// TODO Auto-generated constructor stub
			this.nutrientCode.set("");
			this.nutrientQuantity.set(0);
		}

		//Non Default Constructor initializing parameters
		public ProductNutrient(String nutrientCode, float nutrientQuantity) {
			this.nutrientCode.set(nutrientCode);
			this.nutrientQuantity.set(nutrientQuantity);
		}

		public String getNutrientCode() {
			return nutrientCode.get();
		}

		public void setNutrientCode(String nutrientCode) {
			this.nutrientCode.set(nutrientCode);
		}

		public float getNutrientQuantity() {
			return nutrientQuantity.get();
		}

		public void setNutrientQuantity(Float nutrientQuantity) {
			this.nutrientQuantity.set(nutrientQuantity);
		}


	}


}
