package application;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;


public class WarehouseAgentController implements Initializable {
	
	// LEFT NAVBAR
	@FXML private AnchorPane 	dashboardAnchorPane;
	@FXML private AnchorPane 	settingsAnchorPane;
	@FXML private AnchorPane 	contentAnchorPane;
	@FXML private SVGPath 		dashboardIcon;
	@FXML private SVGPath 		settingsIcon;
	@FXML private Label 		dashboardLabel;
	@FXML private Label 		settingsLabel;
	@FXML private Label 		userFullName;
	@FXML private ImageView		userIcon;
	
	// MAIN CONTENT
	@FXML private Label id1, id2, id3, id4, id5, id6, id7; // parcel ID
	@FXML private Label t1,  t2,  t3,  t4,  t5,  t6,  t7;  // tracking no.
	@FXML private Label w1,  w2,  w3,  w4,  w5,  w6,  w7;  // weight
	@FXML private Label d1,  d2,  d3,  d4,  d5,  d6,  d7;  // dimensions
	@FXML private Label j1,  j2,  j3,  j4,  j5,  j6,  j7;  // job type
	@FXML private ChoiceBox<String> c1, c2, c3, c4, c5, c6, c7;    // Action
	
	private String[] choices = { "Completed", "Cancelled" };

	private int page = 0;
	private Data data = new Data();
	private List<String[]> parcels;
	
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
		contentAnchorPane.getChildren().removeAll(c1, c2, c3, c4, c5, c6, c7);
		c1.getItems().removeAll(choices); c1.setOnAction(null);
		c2.getItems().removeAll(choices); c2.setOnAction(null);
		c3.getItems().removeAll(choices); c3.setOnAction(null);
		c4.getItems().removeAll(choices); c4.setOnAction(null);
		c5.getItems().removeAll(choices); c5.setOnAction(null);
		c6.getItems().removeAll(choices); c6.setOnAction(null);
		c7.getItems().removeAll(choices); c7.setOnAction(null);
	}

	// populate the table of parcels with actual data
	public void populateContent() {
		resetContent();
		data.reload();
		// fetch the data
		parcels = data.getParcelDataByPage(page);
		int size = parcels.size();


		// check incase we have < 7 parcels
		if (size >= 1) {
			id1.setText(parcels.get(0)[0]);
			t1.setText(parcels.get(0)[1]);
			w1.setText(parcels.get(0)[2]);
			d1.setText(parcels.get(0)[3]);
			j1.setText(parcels.get(0)[5]);
			c1.getSelectionModel().select(parcels.get(0)[4]);
			contentAnchorPane.getChildren().add(c1);
		}
		if (size >= 2) {
			id2.setText(parcels.get(1)[0]);
			t2.setText(parcels.get(1)[1]);
			w2.setText(parcels.get(1)[2]);
			d2.setText(parcels.get(1)[3]);
			j2.setText(parcels.get(1)[5]);
			c2.getSelectionModel().select(parcels.get(1)[4]);
			contentAnchorPane.getChildren().add(c2);
		}
		if (size >= 3) {
			id3.setText(parcels.get(2)[0]);
			t3.setText(parcels.get(2)[1]);
			w3.setText(parcels.get(2)[2]);
			d3.setText(parcels.get(2)[3]);
			j3.setText(parcels.get(2)[5]);
			c3.getSelectionModel().select(parcels.get(2)[4]);
			contentAnchorPane.getChildren().add(c3);
		}
		if (size >= 4) {
			id4.setText(parcels.get(3)[0]);
			t4.setText(parcels.get(3)[1]);
			w4.setText(parcels.get(3)[2]);
			d4.setText(parcels.get(3)[3]);
			j4.setText(parcels.get(3)[5]);
			c4.getSelectionModel().select(parcels.get(3)[4]);
			contentAnchorPane.getChildren().add(c4);
		}
		if (size >= 5) {
			id5.setText(parcels.get(4)[0]);
			t5.setText(parcels.get(4)[1]);
			w5.setText(parcels.get(4)[2]);
			d5.setText(parcels.get(4)[3]);
			j5.setText(parcels.get(4)[5]);
			c5.getSelectionModel().select(parcels.get(4)[4]);
			contentAnchorPane.getChildren().add(c5);
		}
		if (size >= 6) {
			id6.setText(parcels.get(5)[0]);
			t6.setText(parcels.get(5)[1]);
			w6.setText(parcels.get(5)[2]);
			d6.setText(parcels.get(5)[3]);
			j6.setText(parcels.get(5)[5]);
			c6.getSelectionModel().select(parcels.get(5)[4]);
			contentAnchorPane.getChildren().add(c6);
		}
		if (size >= 7) {
			id7.setText(parcels.get(6)[0]);
			t7.setText(parcels.get(6)[1]);
			w7.setText(parcels.get(6)[2]);
			d7.setText(parcels.get(6)[3]);
			j7.setText(parcels.get(6)[5]);
			c7.getSelectionModel().select(parcels.get(6)[4]);
			contentAnchorPane.getChildren().add(c7);
		}

		// add the choices for parcels & set onAction function
		c1.getItems().addAll(choices);
		c1.setOnAction(this::c1action);
		c2.getItems().addAll(choices);
		c2.setOnAction(this::c2action);
		c3.getItems().addAll(choices);
		c3.setOnAction(this::c3action);
		c4.getItems().addAll(choices);
		c4.setOnAction(this::c4action);
		c5.getItems().addAll(choices);
		c5.setOnAction(this::c5action);
		c6.getItems().addAll(choices);
		c6.setOnAction(this::c6action);
		c7.getItems().addAll(choices);
		c7.setOnAction(this::c7action);
	}

	public void c1action(ActionEvent event) {
		if (c1.getValue() != null) {
			data.statusChanged(parcels.get(0)[1], c1.getValue());
		}
	}

	public void c2action(ActionEvent event) {
		if (c2.getValue() != null) {
			data.statusChanged(parcels.get(1)[1], c2.getValue());
		}
	}

	public void c3action(ActionEvent event) {
		if (c3.getValue() != null) {
			data.statusChanged(parcels.get(2)[1], c3.getValue());
		}
	}

	public void c4action(ActionEvent event) {
		if (c4.getValue() != null) {
			data.statusChanged(parcels.get(3)[1], c4.getValue());
		}
	}

	public void c5action(ActionEvent event) {
		if (c5.getValue() != null) {
			data.statusChanged(parcels.get(4)[1], c5.getValue());
		}
	}

	public void c6action(ActionEvent event) {
		if (c6.getValue() != null) {
			data.statusChanged(parcels.get(5)[1], c6.getValue());
		}
	}

	public void c7action(ActionEvent event) {
		if (c7.getValue() != null) {
			data.statusChanged(parcels.get(6)[1], c7.getValue());
		}
	}

	public void settingsButton(MouseEvent event) {
		System.out.println("Pressed settings button!");
	}

	public void dashboardButton(MouseEvent event) {
		this.populateContent();
		System.out.println("Pressed dashboard button!");
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
		int last_page = parcels.size() / 7;
		if (page < last_page) {
			page++;
			populateContent();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		userFullName.setText(data.getUserFullName());
		userIcon.setImage(new Image(getClass().getResourceAsStream("/res/" + data.getUserProfilePicture())));
		populateContent();
	}
}


























