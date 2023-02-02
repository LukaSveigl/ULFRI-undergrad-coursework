package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The entry point of the application.
 */
public class Main extends Application {  

	public static void main(String[] args) {  
		launch(args);
	}
	
	// Scenes:
	// 		Scene 1: Warehouse Agent
	//			Scene 1.1: Dashboard
	//  	Scene 2: Warehouse Manager
	//   		Scene 2.1: All Parcels
	//			Scene 2.2: Filter Parcels
	//			Scene 2.3: Stats
	//			Scene 2.4: Employees
	//  	Scene 3: Delivery Driver
	//			Scene 3.1: Handover
	//			Scene 3.2: Parcel Handover
	//			Scene 3.3: Delivery Cargo Info
	//  	Scene 4: International Driver
	//			Scene 4.1: Cargo Departing Info
	//			Scene 4.2: Cargo Arrival Info
	//  	Scene 5: Order Confirmation Specialist
	//			Scene 5.1: Order Processing
	//  	Scene 6: Logistics Agent
	//			Scene 6.1: Branch Stats

	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
