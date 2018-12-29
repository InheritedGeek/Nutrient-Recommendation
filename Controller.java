package hw3;

// Sahib Singh
// AndrewId: sahibsin
import java.io.File;
import java.util.Map.Entry;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


//A class to hold all the event handlers
public class Controller {

	/*Takes all the data from the GUI controls (genderComboBox,
	ageTextField, etc.) to create a profile, and then populate the recommendedNutrientsTableView. */
	class RecommendNutrientsButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			//write your code here

			try {
				if(NutriByte.view.genderComboBox.getValue() == null ) 
					throw new InvalidProfileException("Missing gender information");

				if(NutriByte.view.ageTextField.getText().isEmpty())
					throw new InvalidProfileException("Missing age information");
				
				try {
					Float.parseFloat(NutriByte.view.ageTextField.getText());
				}catch(NumberFormatException e){
					throw new InvalidProfileException("Incorrect age input. Must be a number");
				}
				
				if(Float.parseFloat(NutriByte.view.ageTextField.getText().trim() ) <0)
					throw new InvalidProfileException("Age must be a positive number");

				if(NutriByte.view.weightTextField.getText().isEmpty()) 
					throw new InvalidProfileException("Missing weight information");

				try {
					Float.parseFloat(NutriByte.view.weightTextField.getText());
				}
				catch(NumberFormatException e){
					throw new InvalidProfileException("Incorrect weight input. Must be a number");
				}
				
				if(Float.parseFloat(NutriByte.view.weightTextField.getText().trim()) < 0)
					throw new InvalidProfileException("Weight must be a positive number");


				if(NutriByte.view.heightTextField.getText().isEmpty())
					throw new InvalidProfileException("Missing height information");
				
				try {
					Float.parseFloat(NutriByte.view.heightTextField.getText());
				} catch(NumberFormatException e){
					throw new InvalidProfileException("Incorrect height input. Must be a number");
				}
				
				if(Float.parseFloat(NutriByte.view.heightTextField.getText().trim())<0)
					throw new InvalidProfileException("Height must be a positive number");

				String gender = NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem();				
				float age;
				float weight;
				float height;
				String physicalAct = NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedItem();
				float physicalActLevel = 1;
				String ingredients = NutriByte.view.ingredientsToWatchTextArea.getText();


				if(NutriByte.view.ageTextField.getText().isEmpty())
					age = 0;
				else
					age = Float.parseFloat(NutriByte.view.ageTextField.getText());

				if(NutriByte.view.weightTextField.getText().isEmpty())
					weight = 0;
				else
					weight = Float.parseFloat(NutriByte.view.weightTextField.getText());

				if(NutriByte.view.heightTextField.getText().isEmpty())
					height = 0;
				else
					height = Float.parseFloat(NutriByte.view.heightTextField.getText());


				if (!(physicalAct ==null || physicalAct.isEmpty())) {
					for (NutriProfiler.PhysicalActivityEnum PAenum : NutriProfiler.PhysicalActivityEnum.values()) {
						if(physicalAct.equalsIgnoreCase(PAenum.getName())) {
							physicalActLevel = PAenum.getPhysicalActivityLevel();
							break;
						}
					}
				}

				if (!(gender == null || gender.isEmpty())) {
					if (gender.equalsIgnoreCase("male")) {
						NutriByte.person = new Male(age,weight,height,physicalActLevel,ingredients);
					}else {
						NutriByte.person = new Female(age,weight,height,physicalActLevel,ingredients);
					}
					NutriProfiler.createNutriProfile(NutriByte.person);
				}
				NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);

			} catch(InvalidProfileException | NullPointerException e) {};
		}
	}

	/*
	 *  Displays the profile data in the
	GUI. Finally, invokes NutriProfiler’s createNutriProfile() method to populate the recommendedNutrientsList.
	 * */
	class OpenMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			//write your code here
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select file");
			fileChooser.setInitialDirectory(new File (NutriByte.NUTRIBYTE_PROFILE_PATH));
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV Files (*.csv)","*.csv"),
					new ExtensionFilter("XML Files (*.xml)","*.xml"));
			File file= null;
			
			//ToDo Update chart to start with 0 incase of missing gender file
			if ((file = fileChooser.showOpenDialog(NutriByte.view.root.getScene().getWindow())) !=null) {
				String Path = file.getAbsolutePath();
				NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
				NutriByte.view.initializePrompts();
				
				if (NutriByte.view.recommendedNutrientsTableView.getItems() != null)
					NutriByte.view.recommendedNutrientsTableView.getItems().clear();
				NutriByte.person = null;
				NutriByte.view.nutriChart.clearChart();
				
				if (NutriByte.view.productNutrientsTableView.getItems() != null)
					NutriByte.view.productNutrientsTableView.getItems().clear();
				
				NutriByte.view.productIngredientsTextArea.clear();
				NutriByte.view.servingSizeLabel.setText("");
				NutriByte.view.householdSizeLabel.setText("");
				NutriByte.view.dietServingUomLabel.setText("");
				NutriByte.view.dietHouseholdUomLabel.setText("");
				NutriByte.view.searchResultSizeLabel.setText("");
				NutriByte.view.servingSizeLabel.setText("0.00");
				NutriByte.view.householdSizeLabel.setText("0.00");
				
				if (NutriByte.view.productsComboBox.getItems() != null)
					NutriByte.view.productsComboBox.getItems().clear();
				NutriByte.model.dietProductsList.clear();
				NutriByte.model.readProfiles(Path);
				
				if (NutriByte.person != null) {
					if (NutriByte.person instanceof Male) 
						NutriByte.view.genderComboBox.setValue("Male");
					else
						NutriByte.view.genderComboBox.setValue("Female");
					
					if (NutriByte.person.ingredientsToWatch !=null) {
						NutriByte.view.ingredientsToWatchTextArea.setText(NutriByte.person.ingredientsToWatch);
					}
					
					String PhysicalLevel = null;
					for (NutriProfiler.PhysicalActivityEnum level :NutriProfiler.PhysicalActivityEnum.values()){
						if(level.getPhysicalActivityLevel() == NutriByte.person.physicalActivityLevel) {
							PhysicalLevel = level.getName();
						}
					}
	
					if (NutriByte.person.physicalActivityLevel != 0)
						NutriByte.view.physicalActivityComboBox.setValue(PhysicalLevel);

					NutriByte.view.ageTextField.setText(String.format("%.2f", NutriByte.person.age));
					NutriByte.view.weightTextField.setText(String.format("%.2f", NutriByte.person.weight));
					NutriByte.view.heightTextField.setText(String.format("%.2f", NutriByte.person.height));

					NutriProfiler.createNutriProfile(NutriByte.person);
					NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);
					
				}
				
				if(NutriByte.model.dietProductsList.size() > 0) {			
					NutriByte.view.dietProductsTableView.setItems(NutriByte.model.dietProductsList);
					NutriByte.view.dietServingSizeTextField.setText("");
					NutriByte.view.dietHouseholdSizeTextField.setText("");
					NutriByte.model.searchResultsList.clear();
					
					
					NutriByte.model.searchResultsList.addAll(NutriByte.model.dietProductsList); //search for dietProductsList and add using productMap 
					NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResultsList);            
					NutriByte.view.productsComboBox.setValue(NutriByte.model.searchResultsList.get(0));
					NutriByte.view.productNutrientsTableView.setItems(FXCollections.observableArrayList(NutriByte.model.searchResultsList.get(0).getProductNutrients().values()));
					NutriByte.view.productIngredientsTextArea.setText("Product ingredients: " + NutriByte.model.searchResultsList.get(0).getIngredients());				
					NutriByte.view.servingSizeLabel.setText(String.format("%.2f",Model.productsMap.get(NutriByte.model.searchResultsList.get(0).getNdbNumber()).getServingSize()) + " " + NutriByte.model.searchResultsList.get(0).getServingUom());
					NutriByte.view.householdSizeLabel.setText(String.format("%.2f",Model.productsMap.get(NutriByte.model.searchResultsList.get(0).getNdbNumber()).getHouseholdSize()) + " " + NutriByte.model.searchResultsList.get(0).getHouseholdUom());
					NutriByte.view.dietServingUomLabel.setText(NutriByte.model.searchResultsList.get(0).getServingUom());
					NutriByte.view.dietHouseholdUomLabel.setText(NutriByte.model.searchResultsList.get(0).getHouseholdUom());
					NutriByte.view.searchResultSizeLabel.setText(NutriByte.model.searchResultsList.size() + " product(s) found");
				}
				
				
				if(NutriByte.person != null && NutriByte.model.dietProductsList.size() > 0) {
					NutriByte.person.populateDietNutrientsMap();
					NutriByte.view.nutriChart.updateChart();
				}
			}
		}
	}

	class SearchButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			//write your code here

			String productSearch = null;
			String nutrientSearch = null;
			String ingredientSearch = null;
			String nutrientCode = null;

			NutriByte.model.searchResultsList.clear();

			// if nutrient is not null, search using nutrient keyword in nutrientMap.values
			// From the match get the key (nutrient code) of the matching nutrient
			// Now check in productNutreint using code if that nutrient is required or not

			if (!NutriByte.view.productSearchTextField.getText().isEmpty() && !NutriByte.view.nutrientSearchTextField.getText().isEmpty() && !NutriByte.view.ingredientSearchTextField.getText().isEmpty() ) {
				productSearch = NutriByte.view.productSearchTextField.getText().trim();
				ingredientSearch = NutriByte.view.ingredientSearchTextField.getText().trim();
				for (Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet()) {
					if (prodName.getValue().getProductName().toLowerCase().contains(productSearch.toLowerCase())) {
						nutrientSearch = NutriByte.view.nutrientSearchTextField.getText().trim();
						for (Entry<String, Nutrient> nutName: NutriByte.model.nutrientsMap.entrySet()) {
							if (nutName.getValue().getNutrientName().toLowerCase().contains(nutrientSearch.toLowerCase())) {
								nutrientCode = nutName.getValue().getNutrientCode();
								if (prodName.getValue().productNutrients.containsKey(nutrientCode)) {
									if (prodName.getValue().getIngredients().toLowerCase().contains(ingredientSearch.toLowerCase()))
										NutriByte.model.searchResultsList.add(prodName.getValue());
								}
							}	
						}	
					}
				}
			}

			else if (!NutriByte.view.productSearchTextField.getText().isEmpty() && !NutriByte.view.nutrientSearchTextField.getText().isEmpty() ) {
				productSearch = NutriByte.view.productSearchTextField.getText().trim();
				for (Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet()) {
					if (prodName.getValue().getProductName().toLowerCase().contains(productSearch.toLowerCase())) {
						nutrientSearch = NutriByte.view.nutrientSearchTextField.getText().trim();
						for (Entry<String, Nutrient> nutName: NutriByte.model.nutrientsMap.entrySet()) {
							if (nutName.getValue().getNutrientName().toLowerCase().contains(nutrientSearch.toLowerCase())) {
								nutrientCode = nutName.getValue().getNutrientCode();
								if (prodName.getValue().productNutrients.containsKey(nutrientCode)) {
									NutriByte.model.searchResultsList.add(prodName.getValue());
								}
							}
						}
					}
				}
			}

			else if (!NutriByte.view.productSearchTextField.getText().isEmpty() && !NutriByte.view.ingredientSearchTextField.getText().isEmpty() ) {
				productSearch = NutriByte.view.productSearchTextField.getText().trim();
				ingredientSearch = NutriByte.view.ingredientSearchTextField.getText().trim();
				for (Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet()) {
					if (prodName.getValue().getProductName().toLowerCase().contains(productSearch.toLowerCase())) {
						if (prodName.getValue().getIngredients().toLowerCase().contains(ingredientSearch.toLowerCase()))
							NutriByte.model.searchResultsList.add(prodName.getValue());
					}
				}
			}

			else if (!NutriByte.view.nutrientSearchTextField.getText().isEmpty() && !NutriByte.view.ingredientSearchTextField.getText().isEmpty() ) {
				ingredientSearch = NutriByte.view.ingredientSearchTextField.getText().trim();
				for (Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet()) {		
					nutrientSearch = NutriByte.view.nutrientSearchTextField.getText().trim();
					for (Entry<String, Nutrient> nutName: NutriByte.model.nutrientsMap.entrySet()) {
						if (nutName.getValue().getNutrientName().toLowerCase().contains(nutrientSearch.toLowerCase())) {
							nutrientCode = nutName.getValue().getNutrientCode();
							if (prodName.getValue().productNutrients.containsKey(nutrientCode)) {
								if (prodName.getValue().getIngredients().toLowerCase().contains(ingredientSearch.toLowerCase()))
									NutriByte.model.searchResultsList.add(prodName.getValue());
							}
						}

					}		
				}				
			}

			else if (!NutriByte.view.productSearchTextField.getText().isEmpty()) {
				productSearch = NutriByte.view.productSearchTextField.getText().trim();
				for (Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet()) {
					if (prodName.getValue().getProductName().toLowerCase().contains(productSearch.toLowerCase())) {			
						NutriByte.model.searchResultsList.add(prodName.getValue());
					}
				}
			}

			else if (!NutriByte.view.nutrientSearchTextField.getText().isEmpty() ) {
				for (Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet()) {				
					nutrientSearch = NutriByte.view.nutrientSearchTextField.getText().trim();
					for (Entry<String, Nutrient> nutName: NutriByte.model.nutrientsMap.entrySet()) {
						if (nutName.getValue().getNutrientName().toLowerCase().contains(nutrientSearch.toLowerCase())) {
							nutrientCode = nutName.getValue().getNutrientCode();
							if (prodName.getValue().productNutrients.containsKey(nutrientCode)) {
								NutriByte.model.searchResultsList.add(prodName.getValue());
							}
						}	
					}
				}
			}

			else if (!NutriByte.view.ingredientSearchTextField.getText().isEmpty() ) {
				ingredientSearch = NutriByte.view.ingredientSearchTextField.getText().trim();
				for (Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet()) {
					if (prodName.getValue().getIngredients().toLowerCase().contains(ingredientSearch.toLowerCase()))
						NutriByte.model.searchResultsList.add(prodName.getValue());
				}
			}

			else {
				for (Entry<String, Product> prodName: NutriByte.model.productsMap.entrySet())
					NutriByte.model.searchResultsList.add(prodName.getValue());
			}

			if (NutriByte.model.searchResultsList.size()>0) {
				NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResultsList);
				Product result = NutriByte.model.searchResultsList.get(0);
				NutriByte.view.productsComboBox.setValue(result);
				NutriByte.view.servingSizeLabel.setText(String.format("%.2f",Model.productsMap.get(result.getNdbNumber()).getServingSize()) + " " + result.getServingUom());
				NutriByte.view.householdSizeLabel.setText(String.format("%.2f",Model.productsMap.get(result.getNdbNumber()).getHouseholdSize()) + " " + result.getHouseholdUom());
				NutriByte.view.dietServingUomLabel.setText(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingUom());
				NutriByte.view.dietHouseholdUomLabel.setText(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdUom());
				Product product = NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem();
				NutriByte.view.searchResultSizeLabel.setText(String.valueOf(NutriByte.model.searchResultsList.size())+" product(s) found");
				NutriByte.view.productIngredientsTextArea.setText("Product ingredients: "+product.getIngredients());
				NutriByte.view.productNutrientsTableView.setItems(FXCollections.observableArrayList(NutriByte.model.searchResultsList.get(0).getProductNutrients().values()));
			}
		}


	}
	
	//Button handler to implement adding of Diet Details
	class AddDietButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			//write your code here
			
			if (NutriByte.view.productsComboBox.getSelectionModel().isEmpty() == false) {
				Product prod = NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem();				
				float ssHHratio = (prod.getServingSize() / prod.getHouseholdSize());

				NutriByte.view.dietServingUomLabel.setText(prod.getServingUom());
				NutriByte.view.dietHouseholdUomLabel.setText(prod.getHouseholdUom());

				NutriByte.view.servingSizeLabel.setText(String.format("%.2f",prod.getServingSize()) + " " + prod.getServingUom());  
				NutriByte.view.householdSizeLabel.setText(String.format("%.2f",prod.getHouseholdSize()) + " " + prod.getHouseholdUom());

				if (NutriByte.view.dietServingSizeTextField.getText().trim() != null && NutriByte.view.dietServingSizeTextField.getText().trim().length() > 0) {
					prod.setServingSize(Float.parseFloat(NutriByte.view.dietServingSizeTextField.getText().trim()));
					prod.setHouseholdSize(prod.getServingSize()/ssHHratio);
					NutriByte.model.dietProductsList.add(prod);
					
				} else if(NutriByte.view.dietHouseholdSizeTextField.getText().trim() != null && NutriByte.view.dietHouseholdSizeTextField.getText().trim().length() > 0) {
					prod.setHouseholdSize(Float.parseFloat(NutriByte.view.dietHouseholdSizeTextField.getText().trim()));			
					prod.setServingSize(ssHHratio * prod.getHouseholdSize());
					NutriByte.model.dietProductsList.add(prod);					
				} else {
					NutriByte.model.dietProductsList.add(prod);
				}
			}
			NutriByte.view.dietProductsTableView.setItems(NutriByte.model.dietProductsList);

			if (NutriByte.person != null && NutriByte.model.dietProductsList.size() > 0) {
				NutriByte.person.populateDietNutrientsMap();
				NutriByte.view.nutriChart.updateChart();
			}
		}
	}
	
	// Implementing Remove button for handling diet
	class RemoveDietButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			//write your code here

			if(NutriByte.view.dietProductsTableView.getSelectionModel().isEmpty() == false) {
				Product productRemove = NutriByte.view.dietProductsTableView.getSelectionModel().getSelectedItem();
				NutriByte.model.dietProductsList.remove(productRemove);
//				NutriByte.person.populateDietNutrientsMap(); ToDo Fix issue with this
				if(NutriByte.model.dietProductsList.size() > 0) {
					NutriByte.view.nutriChart.updateChart();
				} else {
					NutriByte.view.nutriChart.clearChart();
				}
			}
			
			if (NutriByte.person != null) {
				NutriByte.person.populateDietNutrientsMap();
				if (NutriByte.model.dietProductsList.size() > 0) {
					NutriByte.view.nutriChart.updateChart();
				} else {
					NutriByte.view.nutriChart.clearChart();
				}
			}
		}		
	}

	// Clears the necessary Pane views
	class ClearButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			//write your code here
			NutriByte.view.productSearchTextField.clear();
			NutriByte.view.ingredientSearchTextField.clear();
			NutriByte.view.nutrientSearchTextField.clear();
			NutriByte.view.productIngredientsTextArea.clear();
			NutriByte.view.searchResultSizeLabel.setText("");
			NutriByte.view.servingSizeLabel.setText("0.00");
			NutriByte.view.householdSizeLabel.setText("0.00");
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");
			NutriByte.view.productsComboBox.setItems(null);	
		}
	}
	
	
	//Saves File in CSV format which can be opened later
	class SaveMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			//write your code here
			String physicalAct = NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedItem();
			float physicalActLevel = 1.0f;

			
			try {
				if (NutriByte.view.genderComboBox.getValue() == null) {
					throw new InvalidProfileException("Missing gender information");
				}
				
				if (NutriByte.view.ageTextField.getText().isEmpty()) {
					throw new InvalidProfileException("Missing age information");
				}
				
				try {
					Float.parseFloat(NutriByte.view.ageTextField.getText().trim());
				}catch(NumberFormatException e){
					throw new InvalidProfileException("Incorrect age input. Must be a number");
				}
				
				if (Float.parseFloat(NutriByte.view.ageTextField.getText().trim() ) <0) {
					throw new InvalidProfileException("Age must be a positive number");
				}
				
				if (NutriByte.view.weightTextField.getText().isEmpty()) {
					throw new InvalidProfileException("Missing weight information");
				}
				
				try {
					Float.parseFloat(NutriByte.view.weightTextField.getText().trim());
				}
				catch(NumberFormatException e){
					throw new InvalidProfileException("Incorrect weight input. Must be a number");
				}
				
				if (Float.parseFloat(NutriByte.view.weightTextField.getText().trim()) < 0) {
					throw new InvalidProfileException("Weight must be a positive number");
				}

				if (NutriByte.view.heightTextField.getText().isEmpty()) {
					throw new InvalidProfileException("Missing height information");
				}
				
				try {
					Float.parseFloat(NutriByte.view.heightTextField.getText().trim());
				} catch(NumberFormatException e){
					throw new InvalidProfileException("Incorrect height input. Must be a number");
				}
				
				if (Float.parseFloat(NutriByte.view.heightTextField.getText().trim()) < 0) {
					throw new InvalidProfileException("Height must be a positive number");
				}
				
				StringBuilder sb = new StringBuilder();

				sb.append(NutriByte.view.genderComboBox.getValue().toString() + ","+ NutriByte.view.ageTextField.getText().toString()+ ","
				+ NutriByte.view.weightTextField.getText().toString() + ","+ NutriByte.view.heightTextField.getText().toString() + ",");
				
				if (!(physicalAct ==null || physicalAct.isEmpty())) {
					for (NutriProfiler.PhysicalActivityEnum PAenum : NutriProfiler.PhysicalActivityEnum.values()) {
						if(physicalAct.equalsIgnoreCase(PAenum.getName())) {
							physicalActLevel = PAenum.getPhysicalActivityLevel();
							break;
						}
					}
				}

				sb.append(physicalActLevel + ","+NutriByte.view.ingredientsToWatchTextArea.getText());
				
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select file");
				fileChooser.setInitialDirectory(new File (NutriByte.NUTRIBYTE_PROFILE_PATH));
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV Files (*.csv)","*.csv"),
						new ExtensionFilter("XML Files (*.xml)","*.xml"));
				File file= null;
				
				//ToDo Update chart to start with 0 incase of missing gender file
				if ((file = fileChooser.showSaveDialog(NutriByte.view.root.getScene().getWindow())) !=null) {
					String Path = file.getAbsolutePath();
					NutriByte.model.writeProfile(sb.toString(), Path);
				}
				
			} catch ( NullPointerException | NumberFormatException | InvalidProfileException e) {
				// TODO: handle exception
//				e.printStackTrace();
			}

		}		
	}
	
	// Closes the file and opens the welcome screen
	class CloseMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			NutriByte.view.root.setCenter(NutriByte.view.setupWelcomeScene());
		}
		
	}

	/* 
	 * Switches the screen from welcome screen to NutriByte.view.nutriTrackerPane.
	Invokes initializePrompts() method of View class. Clears the recommended nutrients TableView.
	 * */
	class NewMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			//write your code here	
			NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
			NutriByte.view.initializePrompts();
			NutriByte.view.productSearchTextField.clear();
			NutriByte.view.ingredientSearchTextField.clear();
			NutriByte.view.nutrientSearchTextField.clear();
			NutriByte.view.productIngredientsTextArea.clear();
			NutriByte.view.dietProductsTableView.getItems().clear();
			NutriByte.view.productNutrientsTableView.getItems().clear();
			NutriByte.view.searchResultSizeLabel.setText("");
			NutriByte.view.servingSizeLabel.setText("0.00");
			NutriByte.view.householdSizeLabel.setText("0.00");
			NutriByte.view.recommendedNutrientsTableView.getItems().clear();
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");
			NutriByte.view.productsComboBox.setItems(null);
//			NutriByte.view.genderComboBox.setValue("Gender");
			NutriByte.view.nutriChart.clearChart();

			//        	NutriByte.person.recommendedNutrientsList.clear();
		}
	}

	// About tab in the application
	class AboutMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("NutriByte");
			alert.setContentText("Version 3.0 \nRelease 1.0\nCopyleft Java Nerds\nThis software is designed purely for educational purposes.\nNo commercial use intended");
			Image image = new Image(getClass().getClassLoader().getResource(NutriByte.NUTRIBYTE_IMAGE_FILE).toString());
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(300);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			alert.setGraphic(imageView);
			alert.showAndWait();
		}
	}
}
