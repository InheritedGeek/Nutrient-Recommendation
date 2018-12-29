package hw3;

//Sahib Singh
//AndrewId: sahibsin
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
 * A Java bean with nutrientCode and nutrientQuantity as private properties, and public getters and setters. It has a
default constructor that initializes nutrientCode to empty string. Its non-default constructor initializes nutrientCode,
and nutrientQuantity using the values passed to it. 
 */
public class RecommendedNutrient {
	
	StringProperty nutrientCode = new SimpleStringProperty();
	FloatProperty nutrientQuantity = new SimpleFloatProperty();
	
	public RecommendedNutrient() {
		// TODO Auto-generated constructor stub
		this.nutrientCode.set("");
//		this.nutrientQuantity.set(0);
	}
	
	public RecommendedNutrient(String nutrientCode, float nutrientQuantity) {
		this.nutrientCode.set(nutrientCode);
		this.nutrientQuantity.set(nutrientQuantity);
	}

	public String getNutrientCode() {
		return nutrientCode.get();
	}

	public void setNutrientCode(StringProperty nutrientCode) {
		this.nutrientCode = nutrientCode;
	}

	public Float getNutrientQuantity() {
		return nutrientQuantity.get();
	}

	public void setNutrientQuantity(FloatProperty nutrientQuantity) {
		this.nutrientQuantity = nutrientQuantity;
	}

}
