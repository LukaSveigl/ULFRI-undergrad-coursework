package application;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;


public class WarehouseManagerController implements Initializable {
	
	// LEFT NAVBAR
	@FXML private AnchorPane 	allcontentAnchorPane;
	@FXML private AnchorPane 	filtercontentAnchorPane;
	@FXML private AnchorPane 	statsAnchorPane;
	@FXML private AnchorPane 	employeesAnchorPane;
	@FXML private AnchorPane 	contentAnchorPane;
	
	@FXML private SVGPath 		allcontentIcon;
	@FXML private SVGPath 		filtercontentIcon1;
	@FXML private SVGPath 		filtercontentIcon2;
	@FXML private SVGPath 		statsIcon1;
	@FXML private SVGPath 		statsIcon2;
	@FXML private SVGPath 		employeesIcon1;
	@FXML private SVGPath 		employeesIcon2;
	@FXML private SVGPath 		settingsIcon;
	
	@FXML private Label 		allcontentLabel;
	@FXML private Label 		filtercontentLabel;
	@FXML private Label 		statsLabel;
	@FXML private Label 		employeesLabel;
	@FXML private Label 		settingsLabel;
	
	@FXML private Label 		allPackagesLabel;
	@FXML private Label 		pendingPackagesLabel;
	@FXML private Label 		processedPackagesLabel;
	
	@FXML private Label 		userFullName;
	@FXML private ImageView		userIcon;
	
	// MAIN CONTENT
	@FXML private Label id1, id2, id3, id4, id5, id6, id7; // parcel ID
	@FXML private Label t1,  t2,  t3,  t4,  t5,  t6,  t7;  // tracking no.
	@FXML private Label w1,  w2,  w3,  w4,  w5,  w6,  w7;  // weight
	@FXML private Label d1,  d2,  d3,  d4,  d5,  d6,  d7;  // dimensions
	@FXML private Button b1, b2, b3, b4, b5, b6, b7;    // Status
	@FXML private Label j1,  j2,  j3,  j4,  j5,  j6,  j7;  // job type
	@FXML private ChoiceBox<String> choiceBoxFilter;    // Filter
	
	

	private int page = 0;
	static String view = "All Parcels";
	private Data data = new Data();
	private List<String[]> content;
	
	private Stage stage;
	private Scene scene;
	private Parent root;

	// delete default values (as set in SceneBuilder)
	public void resetContent() {
		id1.setText(""); t1.setText(""); w1.setText(""); d1.setText(""); j1.setText("");
		id2.setText(""); t2.setText(""); w2.setText(""); d2.setText(""); j2.setText("");
		id3.setText(""); t3.setText(""); w3.setText(""); d3.setText(""); j3.setText("");
		id4.setText(""); t4.setText(""); w4.setText(""); d4.setText(""); j4.setText("");
		id5.setText(""); t5.setText(""); w5.setText(""); d5.setText(""); j5.setText("");
		id6.setText(""); t6.setText(""); w6.setText(""); d6.setText(""); j6.setText("");
		id7.setText(""); t7.setText(""); w7.setText(""); d7.setText(""); j7.setText("");
		contentAnchorPane.getChildren().removeAll(b1, b2, b3, b4, b5, b6, b7);
	}

	// populate the table of content with actual data
	public void populateContent() {
		resetContent();

		data.reload();

		// fetch the data
		if (Objects.equals(view, "All Parcels") || Objects.equals(view,
																											"Stats")) {
			//content = data.getParcelDataByPageByEmployee(choiceBoxFilter.getSelectionModel().getSelectedItem(), page);
			content = data.getParcelDataByPage(page);

		}
		else if ( Objects.equals(view, "Filter Parcels")) {
			if (choiceBoxFilter.getSelectionModel().getSelectedItem().equals("All")) {
				content = data.getParcelDataByPage(page);
			}
			else {
				content = data.getParcelDataByPageByEmployee(choiceBoxFilter.getSelectionModel().getSelectedItem(), page);
			}
		}
		else if (Objects.equals(view, "Employees")) {
			content = data.getEmployees();
		}
		int size = content.size();

		// check incase we have < 7 content
		if (size >= 1) {
			id1.setText(content.get(0)[0]);
			t1.setText(content.get(0)[1]);
			w1.setText(content.get(0)[2]);
			d1.setText(content.get(0)[3]);
			b1.setText(content.get(0)[4]);
			j1.setText(content.get(0)[5]);
			if (content.get(0)[4].equals("completed")) {
				b1.setStyle(b1.getStyle() + "-fx-background-color: #00B512;");
			}
			contentAnchorPane.getChildren().add(b1);
		}
		if (size >= 2) {
			id2.setText(content.get(1)[0]);
			t2.setText(content.get(1)[1]);
			w2.setText(content.get(1)[2]);
			d2.setText(content.get(1)[3]);
			b2.setText(content.get(1)[4]);
			j2.setText(content.get(1)[5]);
			if (content.get(1)[4].equals("completed")) {
				b2.setStyle(b2.getStyle() + "-fx-background-color: #00B512;");
			}
			contentAnchorPane.getChildren().add(b2);
		}
		if (size >= 3) {
			id3.setText(content.get(2)[0]);
			t3.setText(content.get(2)[1]);
			w3.setText(content.get(2)[2]);
			d3.setText(content.get(2)[3]);
			b3.setText(content.get(2)[4]);
			j3.setText(content.get(2)[5]);
			if (content.get(2)[4].equals("completed")) {
				b3.setStyle(b3.getStyle() + "-fx-background-color: #00B512;");
			}
			contentAnchorPane.getChildren().add(b3);
		}
		if (size >= 4) {
			id4.setText(content.get(3)[0]);
			t4.setText(content.get(3)[1]);
			w4.setText(content.get(3)[2]);
			d4.setText(content.get(3)[3]);
			b4.setText(content.get(3)[4]);
			j4.setText(content.get(3)[5]);
			if (content.get(3)[4].equals("completed")) {
				b4.setStyle(b4.getStyle() + "-fx-background-color: #00B512;");
			}
			contentAnchorPane.getChildren().add(b4);
		}
		if (size >= 5) {
			id5.setText(content.get(4)[0]);
			t5.setText(content.get(4)[1]);
			w5.setText(content.get(4)[2]);
			d5.setText(content.get(4)[3]);
			b5.setText(content.get(4)[4]);
			j5.setText(content.get(4)[5]);
			if (content.get(4)[4].equals("completed")) {
				b5.setStyle(b5.getStyle() + "-fx-background-color: #00B512;");
			}
			contentAnchorPane.getChildren().add(b5);
		}
		if (size >= 6) {
			id6.setText(content.get(5)[0]);
			t6.setText(content.get(5)[1]);
			w6.setText(content.get(5)[2]);
			d6.setText(content.get(5)[3]);
			b6.setText(content.get(5)[4]);
			j6.setText(content.get(5)[5]);
			if (content.get(5)[4].equals("completed")) {
				b6.setStyle(b6.getStyle() + "-fx-background-color: #00B512;");
			}
			contentAnchorPane.getChildren().add(b6);
		}
		if (size >= 7) {
			id7.setText(content.get(6)[0]);
			t7.setText(content.get(6)[1]);
			w7.setText(content.get(6)[2]);
			d7.setText(content.get(6)[3]);
			b7.setText(content.get(6)[4]);
			j7.setText(content.get(6)[5]);
			if (content.get(6)[4].equals("completed")) {
				b7.setStyle(b7.getStyle() + "-fx-background-color: #00B512;");
			}
			contentAnchorPane.getChildren().add(b7);
		}
	}

	public void allParcelsButton(MouseEvent event) throws IOException {
		view = "All Parcels";
		Parent root = FXMLLoader.load(getClass().getResource("WarehouseManagerAllParcels.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void filterParcelsButton(MouseEvent event) throws IOException {
		view = "Filter Parcels";
		Parent root = FXMLLoader.load(getClass().getResource("WarehouseManagerFilterParcels.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void statsButton(MouseEvent event) throws IOException {
		view = "Stats";
		Parent root = FXMLLoader.load(getClass().getResource("WarehouseManagerStats.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void employeesButton(MouseEvent event) throws IOException {
		view = "Employees";
		Parent root = FXMLLoader.load(getClass().getResource("WarehouseManagerEmployees.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void settingsButton(MouseEvent event) {
		view = "Settings";
		System.out.println("Pressed button!");
	}

	public void signOutAnchorPaneButton(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void switchPageLeft() {
		System.out.println("left");
		if (page > 0) {
			page--;
			populateContent();
		}
	}

	public void switchPageRight() {
		System.out.println("Right");
		int last_page = content.size() / 7;
		if (page < last_page) {
			page++;
			populateContent();
		}
	}

	// Function fires when new filter is selected
	public void choiceBoxFilterAction(ActionEvent event) {
		content = data.getParcelDataByPageByEmployee(choiceBoxFilter.getValue(), page);
		populateContent();
		System.out.println("Changed filter! - " + choiceBoxFilter.getValue());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		userFullName.setText(data.getUserFullName());
		userIcon.setImage(new Image(getClass().getResourceAsStream("/res/" + data.getUserProfilePicture())));

		choiceBoxFilter.getItems().add("All");
		choiceBoxFilter.getItems().addAll(data.getEmployeesArray());
		choiceBoxFilter.getSelectionModel().select(0);
		choiceBoxFilter.setOnAction(this::choiceBoxFilterAction);
		allPackagesLabel.setText(data.getNumberOfAllPackages());
		pendingPackagesLabel.setText(data.getNumberOfPendingPackages());
		processedPackagesLabel.setText(data.getNumberOfProcessedPackages());
		populateContent();
	}
}


























