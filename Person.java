package hw3;

//Sahib Singh
//AndrewId: sahibsin

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import hw3.NutriProfiler.AgeGroupEnum;
import hw3.Product.ProductNutrient;

/*
 * An abstract class that captures the profile data
 */
public abstract class Person {

	float age, weight, height, physicalActivityLevel; //age in years, weight in kg, height in cm
	String ingredientsToWatch;
	float[][] nutriConstantsTable = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT][NutriProfiler.AGE_GROUP_COUNT];
	ObservableList<RecommendedNutrient> recommendedNutrients =FXCollections.observableArrayList();;
	ObservableMap<String, RecommendedNutrient> dietNutrientsMap = FXCollections.observableHashMap();
	AgeGroupEnum ageGroup;
	
	ObservableList<RecommendedNutrient> recommendedNutrientsList = FXCollections.observableArrayList();


	abstract void initializeNutriConstantsTable();
	abstract float calculateEnergyRequirement();
	
	//remove this default constructor once you have defined the child's constructor
	//	Person() {}

	/*
	One non-default constructor that initializes age, weight, height, physicalActivityLevel, ingredientsToWatch, and
	ageGroup.
	*/
	public Person(float age, float weight, float height, float physicalActivityLevel, String ingredientsToWatch) {
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.physicalActivityLevel = physicalActivityLevel;
		this.ingredientsToWatch = ingredientsToWatch;
		
		for (AgeGroupEnum ageEnum : AgeGroupEnum.values()) {
			if(age<=ageEnum.getAge()) {
				ageGroup = ageEnum;
				break;
			}
		}

	}
	
	//returns an array of nutrient values of size NutriProfiler.RECOMMENDED_NUTRI_COUNT. 
	//Each value is calculated as follows:
	//For Protein, it multiples the constant with the person's weight.
	//For Carb and Fiber, it simply takes the constant from the 
	//nutriConstantsTable based on NutriEnums' nutriIndex and the person's ageGroup
	//For others, it multiples the constant with the person's weight and divides by 1000.
	//Try not to use any literals or hard-coded values for age group, nutrient name, array-index, etc. 
	
	float[] calculateNutriRequirement() {
		//write your code here
		float[] nutriRequirement = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT];
		
		for (NutriProfiler.NutriEnum nutriEnum : NutriProfiler.NutriEnum.values()) {
			
			if (nutriEnum.equals(NutriProfiler.NutriEnum.PROTEIN)) {
				nutriRequirement[nutriEnum.getNutriIndex()] = weight * nutriConstantsTable[nutriEnum.getNutriIndex()][ageGroup.getAgeGroupIndex()];
			}
			
			else if (nutriEnum.equals(NutriProfiler.NutriEnum.CARBOHYDRATE) || nutriEnum.equals(NutriProfiler.NutriEnum.FIBER)) {
				nutriRequirement[nutriEnum.getNutriIndex()] = nutriConstantsTable[nutriEnum.getNutriIndex()][ageGroup.getAgeGroupIndex()];
			}
			
			else {
				nutriRequirement[nutriEnum.getNutriIndex()] = weight/1000 *nutriConstantsTable[nutriEnum.getNutriIndex()][ageGroup.getAgeGroupIndex()];
			}
		}
		return nutriRequirement;
	}
	
	//Populating DietNutrientMap
	void populateDietNutrientsMap() {
		
		dietNutrientsMap.clear();

		for(Product p : NutriByte.model.dietProductsList) {
			for(ProductNutrient pn : p.getProductNutrients().values()) {
				if(!dietNutrientsMap.containsKey(pn.getNutrientCode())) {	
					dietNutrientsMap.put(pn.getNutrientCode(), new RecommendedNutrient(pn.getNutrientCode(),
							p.getServingSize() * pn.getNutrientQuantity()/100));
				} else {
					dietNutrientsMap.put(pn.getNutrientCode(), new RecommendedNutrient(pn.getNutrientCode(), 
							dietNutrientsMap.get(pn.getNutrientCode()).getNutrientQuantity() + (p.getServingSize() * pn.getNutrientQuantity()/100)));
				}
			}
		}
	}	
	
}
