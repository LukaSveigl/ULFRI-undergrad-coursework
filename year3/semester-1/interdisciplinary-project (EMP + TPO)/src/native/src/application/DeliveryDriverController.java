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


public class DeliveryDriverController implements Initializable {
	// MAIN CONTENT
	@FXML private Label id1, id2, id3, id4, id5, id6, id7; // parcel ID
	@FXML private Label t1,  t2,  t3,  t4,  t5,  t6,  t7;  // tracking no.
	@FXML private Label w1,  w2,  w3,  w4,  w5,  w6,  w7;  // weight
	@FXML private Label d1,  d2,  d3,  d4,  d5,  d6,  d7;  // dimensions
	@FXML private CheckBox h1, h2, h3, h4, h5, h6, h7;    // Handover checkbox
	@FXML private CheckBox c1, c2, c3, c4, c5, c6, c7;    // Cancelled checkbox

	@FXML private Label 		userFullName;
	@FXML private ImageView		userIcon;
	
	private int page = 0;
	private Data data = new Data();
	private List<String[]> parcels;
	private String view = "Handover";
	
	private Stage stage;
	private Scene scene;
	private Parent root;

	public void resetContent() {
		id1.setText(""); t1.setText(""); w1.setText(""); d1.setText(""); 
		id2.setText(""); t2.setText(""); w2.setText(""); d2.setText("");
		id3.setText(""); t3.setText(""); w3.setText(""); d3.setText("");
		id4.setText(""); t4.setText(""); w4.setText(""); d4.setText("");
		id5.setText(""); t5.setText(""); w5.setText(""); d5.setText("");
		id6.setText(""); t6.setText(""); w6.setText(""); d6.setText("");
		id7.setText(""); t7.setText(""); w7.setText(""); d7.setText("");
		h1.setSelected(false); c1.setSelected(false);
		h2.setSelected(false); c2.setSelected(false);
		h3.setSelected(false); c3.setSelected(false);
		h4.setSelected(false); c4.setSelected(false);
		h5.setSelected(false); c5.setSelected(false);
		h6.setSelected(false); c6.setSelected(false);
		h7.setSelected(false); c7.setSelected(false);
		
	}

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
			if (parcels.get(0)[4].equals("completed")) {
				h1.setSelected(true);
			}
			if (parcels.get(0)[4].equals("cancelled")) {
				c1.setSelected(true);
			}
		}
		if (size >= 2) {
			id2.setText(parcels.get(1)[0]);
			t2.setText(parcels.get(1)[1]);
			w2.setText(parcels.get(1)[2]);
			d2.setText(parcels.get(1)[3]);
			if (parcels.get(1)[4].equals("completed")) {
				h2.setSelected(true);
			}
			if (parcels.get(1)[4].equals("cancelled")) {
				c2.setSelected(true);
			}
		}
		if (size >= 3) {
			id3.setText(parcels.get(2)[0]);
			t3.setText(parcels.get(2)[1]);
			w3.setText(parcels.get(2)[2]);
			d3.setText(parcels.get(2)[3]);
			if (parcels.get(2)[4].equals("completed")) {
				h3.setSelected(true);
			}
			if (parcels.get(2)[4].equals("cancelled")) {
				c3.setSelected(true);
			}
		}
		if (size >= 4) {
			id4.setText(parcels.get(3)[0]);
			t4.setText(parcels.get(3)[1]);
			w4.setText(parcels.get(3)[2]);
			d4.setText(parcels.get(3)[3]);
			if (parcels.get(3)[4].equals("completed")) {
				h4.setSelected(true);
			}
			if (parcels.get(3)[4].equals("cancelled")) {
				c4.setSelected(true);
			}
		}
		if (size >= 5) {
			id5.setText(parcels.get(4)[0]);
			t5.setText(parcels.get(4)[1]);
			w5.setText(parcels.get(4)[2]);
			d5.setText(parcels.get(4)[3]);
			if (parcels.get(4)[4].equals("completed")) {
				h5.setSelected(true);
			}
			if (parcels.get(4)[4].equals("cancelled")) {
				c5.setSelected(true);
			}
		}
		if (size >= 6) {
			id6.setText(parcels.get(5)[0]);
			t6.setText(parcels.get(5)[1]);
			w6.setText(parcels.get(5)[2]);
			d6.setText(parcels.get(5)[3]);
			if (parcels.get(5)[4].equals("completed")) {
				h6.setSelected(true);
			}
			if (parcels.get(5)[4].equals("cancelled")) {
				c6.setSelected(true);
			}
		}
		if (size >= 7) {
			id7.setText(parcels.get(6)[0]);
			t7.setText(parcels.get(6)[1]);
			w7.setText(parcels.get(6)[2]);
			d7.setText(parcels.get(6)[3]);
			if (parcels.get(6)[4].equals("completed")) {
				h7.setSelected(true);
			}
			if (parcels.get(6)[4].equals("cancelled")) {
				c7.setSelected(true);
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		userFullName.setText(data.getUserFullName());
		userIcon.setImage(new Image(getClass().getResourceAsStream("/res/" + data.getUserProfilePicture())));

		populateContent();
	}

	public void handoverButton(MouseEvent event) throws IOException {
		view = "Handover";
		Parent root = FXMLLoader.load(getClass().getResource("DeliveryDriverHandover.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void parcelHandoverButton(MouseEvent event) throws IOException {
		view = "Parcel Handover";
		Parent root = FXMLLoader.load(getClass().getResource("DeliveryDriverParcelHandover.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void deliveryCargoInfoButton(MouseEvent event) throws IOException {
		view = "Delivery Cargo Info";
		Parent root = FXMLLoader.load(getClass().getResource("DeliveryDriverDeliveryCargoInfo.fxml"));
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

	public void toggleCheckBox(ActionEvent event) {
		CheckBox box = (CheckBox) event.getSource();
		String id = box.getId();
		System.out.println(id);
		switch (id) {
			case "c1" -> data.deliveryDriverAction(view + " cancelled", parcels.get(0)[1], "");
			case "c2" -> data.deliveryDriverAction(view + " cancelled", parcels.get(1)[1], "");
			case "c3" -> data.deliveryDriverAction(view + " cancelled", parcels.get(2)[1], "");
			case "c4" -> data.deliveryDriverAction(view + " cancelled", parcels.get(3)[1], "");
			case "c5" -> data.deliveryDriverAction(view + " cancelled", parcels.get(4)[1], "");
			case "c6" -> data.deliveryDriverAction(view + " cancelled", parcels.get(5)[1], "");
			case "c7" -> data.deliveryDriverAction(view + " cancelled", parcels.get(6)[1], "");
			case "h1" -> data.deliveryDriverAction(view + " handover", parcels.get(0)[1], "");
			case "h2" -> data.deliveryDriverAction(view + " handover", parcels.get(1)[1], "");
			case "h3" -> data.deliveryDriverAction(view + " handover", parcels.get(2)[1], "");
			case "h4" -> data.deliveryDriverAction(view + " handover", parcels.get(3)[1], "");
			case "h5" -> data.deliveryDriverAction(view + " handover", parcels.get(4)[1], "");
			case "h6" -> data.deliveryDriverAction(view + " handover", parcels.get(5)[1], "");
			case "h7" -> data.deliveryDriverAction(view + " handover", parcels.get(6)[1], "");
		}

		switch (id) {
			case "h1":
				if (c1.isSelected()) {
					c1.setSelected(false);
				}
				break;
			case "c1":
				if (h1.isSelected()) {
					h1.setSelected(false);
				}
				break;
			case "h2":
				if (c2.isSelected()) {
					c2.setSelected(false);
				}
				break;
			case "c2":
				if (h2.isSelected()) {
					h2.setSelected(false);
				}
				break;
			case "h3":
				if (c3.isSelected()) {
					c3.setSelected(false);
				}
				break;
			case "c3":
				if (h3.isSelected()) {
					h3.setSelected(false);
				}
				break;
			case "h4":
				if (c4.isSelected()) {
					c4.setSelected(false);
				}
				break;
			case "c4":
				if (h4.isSelected()) {
					h4.setSelected(false);
				}
				break;
			case "h5":
				if (c5.isSelected()) {
					c5.setSelected(false);
				}
				break;
			case "c5":
				if (h5.isSelected()) {
					h5.setSelected(false);
				}
				break;
			case "h6":
				if (c6.isSelected()) {
					c6.setSelected(false);
				}
				break;
			case "c6":
				if (h6.isSelected()) {
					h6.setSelected(false);
				}
				break;
			case "h7":
				if (c7.isSelected()) {
					c7.setSelected(false);
				}
				break;
			case "c7":
				if (h7.isSelected()) {
					h7.setSelected(false);
				}
				break;
		}
	}

}
