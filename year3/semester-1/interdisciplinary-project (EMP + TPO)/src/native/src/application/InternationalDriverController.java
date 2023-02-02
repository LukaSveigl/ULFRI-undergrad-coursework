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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.stage.Stage;


public class InternationalDriverController implements Initializable {

	// MAIN CONTENT
	@FXML private Label id1, id2, id3, id4, id5, id6, id7; // shipment ID
	@FXML private Label n1,  n2, n3,  n4,  n5,  n6,  n7;  // No. of parcels
	@FXML private Label w1,  w2,  w3,  w4,  w5,  w6,  w7;  // net weight
	@FXML private CheckBox c1, c2, c3, c4, c5, c6, c7;    // Completed checkbox
	@FXML private Label 		userFullName;
	@FXML private ImageView		userIcon;
	
	private int page = 0;
	private Data data = new Data();
	private List<String[]> shipments;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	static String view = "Cargo Departing Info";

	public void resetContent() {
		id1.setText(""); n1.setText(""); w1.setText("");
		id2.setText(""); n2.setText(""); w2.setText("");
		id3.setText(""); n3.setText(""); w3.setText("");
		id4.setText(""); n4.setText(""); w4.setText("");
		id5.setText(""); n5.setText(""); w5.setText("");
		id6.setText(""); n6.setText(""); w6.setText("");
		id7.setText(""); n7.setText(""); w7.setText("");
		c1.setSelected(false);
		c2.setSelected(false);
		c3.setSelected(false);
		c4.setSelected(false);
		c5.setSelected(false);
		c6.setSelected(false);
		c7.setSelected(false);
	}

	public void populateContent() {
		userFullName.setText(data.getUserFullName());
		userIcon.setImage(new Image(getClass().getResourceAsStream("/res/" + data.getUserProfilePicture())));

		resetContent();
		data.reload();

		String status;
		if (view.equals("Cargo Departing Info")) {
			status = "Cargo departing confirmation";
		} else {
			status = "Cargo arrival confirmation";
		}

		// fetch the data
		if (view.equals("Cargo Departing Info") || view.equals("Cargo Arrival Info")) {
			shipments = data.getShipmentDataByTypeByPage(status, page);
		} else {
			shipments = data.getShipmentDataByPage(page);
		}

		int size = shipments.size();


		// check incase we have < 7 shipments
		if (size >= 1) {
			id1.setText(shipments.get(0)[0]);
			n1.setText(shipments.get(0)[1]);
			w1.setText(shipments.get(0)[2]);
			if (shipments.get(0)[3].equals("completed")) {
				c1.setSelected(true);
			}
		}
		if (size >= 2) {
			id2.setText(shipments.get(1)[0]);
			n2.setText(shipments.get(1)[1]);
			w2.setText(shipments.get(1)[2]);
			if (shipments.get(1)[3].equals("completed")) {
				c2.setSelected(true);
			}
		}
		if (size >= 3) {
			id3.setText(shipments.get(2)[0]);
			n3.setText(shipments.get(2)[1]);
			w3.setText(shipments.get(2)[2]);
			if (shipments.get(2)[3].equals("completed")) {
				c3.setSelected(true);
			}
		}
		if (size >= 4) {
			id4.setText(shipments.get(3)[0]);
			n4.setText(shipments.get(3)[1]);
			w4.setText(shipments.get(3)[2]);
			if (shipments.get(3)[3].equals("completed")) {
				c4.setSelected(true);
			}
		}
		if (size >= 5) {
			id5.setText(shipments.get(4)[0]);
			n5.setText(shipments.get(4)[1]);
			w5.setText(shipments.get(4)[2]);
			if (shipments.get(4)[3].equals("completed")) {
				c5.setSelected(true);
			}
		}
		if (size >= 6) {
			id6.setText(shipments.get(5)[0]);
			n6.setText(shipments.get(5)[1]);
			w6.setText(shipments.get(5)[2]);
			if (shipments.get(5)[3].equals("completed")) {
				c6.setSelected(true);
			}
		}
		if (size >= 7) {
			id7.setText(shipments.get(6)[0]);
			n7.setText(shipments.get(6)[1]);
			w7.setText(shipments.get(6)[2]);
			if (shipments.get(6)[3].equals("completed")) {
				c7.setSelected(true);
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		populateContent();
	}

	public void handoverButton(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("DeliveryDriverHandover.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void parcelHandoverButton(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("DeliveryDriverParcelHandover.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void deliveryCargoInfoButton(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("DeliveryDriverDeliveryCargoInfo.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void cargoDepartingInfoButton(MouseEvent event) throws IOException {
		view = "Cargo Departing Info";
		Parent root = FXMLLoader.load(getClass().getResource("InternationalDriverCargoDepartingInfo.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void cargoArrivalInfoButton(MouseEvent event) throws IOException {
		view = "Cargo Arrival Info";
		Parent root = FXMLLoader.load(getClass().getResource("InternationalDriverCargoArrivalInfo.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void settingsButton(MouseEvent event) {
		System.out.println("Pressed settings button!");
	}

	public void signOutAnchorPaneButton(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void switchPageLeft() {
		if (page > 0) {
			page--;
			populateContent();
		}
	}

	public void switchPageRight() {
		int last_page = shipments.size() / 7;
		if (page < last_page) {
			page++;
			populateContent();
		}
	}

	public void toggleCheckBox(ActionEvent event) {
		CheckBox box = (CheckBox) event.getSource();
		String id = box.getId();
		System.out.println(id);
		switch (id) {
			case "c1" -> data.deliveryDriverAction("shipment completed", shipments.get(0)[0], "International");
			case "c2" -> data.deliveryDriverAction("shipment completed", shipments.get(1)[0], "International");
			case "c3" -> data.deliveryDriverAction("shipment completed", shipments.get(2)[0], "International");
			case "c4" -> data.deliveryDriverAction("shipment completed", shipments.get(3)[0], "International");
			case "c5" -> data.deliveryDriverAction("shipment completed", shipments.get(4)[0], "International");
			case "c6" -> data.deliveryDriverAction("shipment completed", shipments.get(5)[0], "International");
			case "c7" -> data.deliveryDriverAction("shipment completed", shipments.get(6)[0], "International");
		}
	}

}
