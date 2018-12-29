package hw3;

//Sahib Singh
//AndrewId: sahibsin
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
 * A Java bean with private member variables as properties, and public getters and setters. It has a default constructor
that initializes all string properties to empty strings. Its non-default constructor initializes nutrientCode,
nutrientName, and nutrientUom properties.
 */
public class Nutrient {
	
	StringProperty nutrientCode = new SimpleStringProperty();
	StringProperty nutrientName = new SimpleStringProperty();
	StringProperty nutrientUom = new SimpleStringProperty();

	Nutrient(){
		this.nutrientCode.set("");
		this.nutrientName.set("");
		this.nutrientUom.set("");
	}
	
	Nutrient(String nutrientCode, String nutrientName, String nutrientUom){
		this.nutrientCode.set(nutrientCode);
		this.nutrientName.set(nutrientName);
		this.nutrientUom.set(nutrientUom);
	}

	public String getNutrientCode() {
		return nutrientCode.get();
	}

	public void setNutrientCode(StringProperty nutrientCode) {
		this.nutrientCode = nutrientCode;
	}

	public String getNutrientName() {
		return nutrientName.get();
	}

	public void setNutrientName(StringProperty nutrientName) {
		this.nutrientName = nutrientName;
	}

	public String getNutrientUom() {
		return nutrientUom.get();
	}

	public void setNutrientUom(StringProperty nutrientUom) {
		this.nutrientUom = nutrientUom;
	}
	
	public final StringProperty nutrientCodeProperty() {
		return nutrientCode;
	}
	
	public final StringProperty nutrientNameProperty() {
		return nutrientName;
	}
	
	public final StringProperty nutrientUomProperty() {
		return nutrientUom;
	}

}
