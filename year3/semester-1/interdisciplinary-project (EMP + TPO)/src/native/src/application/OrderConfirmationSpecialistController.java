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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class OrderConfirmationSpecialistController implements Initializable {
	

	
	// MAIN CONTENT
	@FXML private Label id1, id2, id3, id4, id5, id6, id7; // parcel ID
	@FXML private Label t1,  t2,  t3,  t4,  t5,  t6,  t7;  // tracking no.
	@FXML private Label w1,  w2,  w3,  w4,  w5,  w6,  w7;  // weight
	@FXML private Label d1,  d2,  d3,  d4,  d5,  d6,  d7;  // dimensions
	@FXML private CheckBox c1, c2, c3, c4, c5, c6, c7;    // Confirmed checkbox
	@FXML private Label 		userFullName;
	@FXML private ImageView		userIcon;
	@FXML private TextField		sender, senderStreetName, senderStreetNumber, senderCityCode, senderCityName, senderCountryCode, senderID, senderPhoneNumber;
	@FXML private TextField		rec, recStreetName, recStreetNumber, recCityCode, recCityName, recCountryCode, recID, recPhoneNumber;
	@FXML private TextField		weight, height, width, depth;
	
	
	private int page = 0;
	private Data data = new Data();
	private List<String[]> parcels;
	
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
		c1.setSelected(false);
		c2.setSelected(false);
		c3.setSelected(false);
		c4.setSelected(false);
		c5.setSelected(false);
		c6.setSelected(false);
		c7.setSelected(false);
		
	}
	
	public void populateContent() {
		
		resetContent();
		data.reload();

		// fetch the data
		parcels = data.getParcelDataOrderConfirmationSpecialistByPage(page);
		int size = parcels.size();
		
		
		
		// check incase we have < 7 parcels
		if (size >= 1) {
			id1.setText(parcels.get(0)[0]);
			t1.setText( parcels.get(0)[1]);
			w1.setText( parcels.get(0)[2]);
			d1.setText( parcels.get(0)[3]); 
			if (parcels.get(0)[4].equals("completed")) c1.setSelected(true);
		}
		if (size >= 2) {
			id2.setText(parcels.get(1)[0]);
			t2.setText( parcels.get(1)[1]);
			w2.setText( parcels.get(1)[2]);
			d2.setText( parcels.get(1)[3]);
			if (parcels.get(1)[4].equals("completed")) c2.setSelected(true);
		}
		if (size >= 3) {
			id3.setText(parcels.get(2)[0]);
			t3.setText( parcels.get(2)[1]);
			w3.setText( parcels.get(2)[2]);
			d3.setText( parcels.get(2)[3]);
			if (parcels.get(2)[4].equals("completed")) c3.setSelected(true);
		}
		if (size >= 4) {
			id4.setText(parcels.get(3)[0]);
			t4.setText( parcels.get(3)[1]);
			w4.setText( parcels.get(3)[2]);
			d4.setText( parcels.get(3)[3]);
			if (parcels.get(3)[4].equals("completed")) c4.setSelected(true);
		}
		if (size >= 5) {
			id5.setText(parcels.get(4)[0]);
			t5.setText( parcels.get(4)[1]);
			w5.setText( parcels.get(4)[2]);
			d5.setText( parcels.get(4)[3]);
			if (parcels.get(4)[4].equals("completed")) c5.setSelected(true);
		}
		if (size >= 6) {
			id6.setText(parcels.get(5)[0]);
			t6.setText( parcels.get(5)[1]);
			w6.setText( parcels.get(5)[2]);
			d6.setText( parcels.get(5)[3]);
			if (parcels.get(5)[4].equals("completed")) c6.setSelected(true);
		}
		if (size >= 7) {
			id7.setText(parcels.get(6)[0]);
			t7.setText( parcels.get(6)[1]);
			w7.setText( parcels.get(6)[2]);
			d7.setText( parcels.get(6)[3]);
			if (parcels.get(6)[4].equals("completed")) c7.setSelected(true);
		}



	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		userFullName.setText(data.getUserFullName());
		userIcon.setImage(new Image(getClass().getResourceAsStream("/res/" + data.getUserProfilePicture())));

		populateContent();
		
		
	}

	public void settingsButton(MouseEvent event) {
		System.out.println("Pressed settings button!");
	}
	public void signOutAnchorPaneButton(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void orderProcessingPaneButton(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("OrderConfirmationSpecialistOrderProcessing.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void createNewAnchorPaneButton(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("OrderConfirmationSpecialistCreateNewParcel.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		}
	
	// get data from text fields and create new parcel
	public void createNewParcelAnchorPaneButton(MouseEvent event) throws IOException {

		String _sender = sender.getText();
		String _senderStreetName = senderStreetName.getText();
		String _senderStreetNumber = senderStreetNumber.getText();
		String _senderCityCode = senderCityCode.getText();
		String _senderCityName = senderCityName.getText();
		String _senderCountryCode = senderCountryCode.getText();
		String _senderID = senderID.getText();
		String _senderPhoneNumber = senderPhoneNumber.getText();
		
		String _rec = rec.getText();
		String _recStreetName = recStreetName.getText();
		String _recStreetNumber = recStreetNumber.getText();
		String _recCityCode = recCityCode.getText();
		String _recCityName = recCityName.getText();
		String _recCountryCode = recCountryCode.getText();
		String _recID = recID.getText();
		String _recPhoneNumber = recPhoneNumber.getText();
		
		String _weight = weight.getText();
		String _height = height.getText();
		String _width = width.getText();
		String _depth = depth.getText();

		data.createNewParcel(_sender, _senderStreetName, _senderStreetNumber, _senderCityCode, _senderCityName, _senderCountryCode, _senderID, _senderPhoneNumber,
							 _rec, _recStreetName, _recStreetNumber, _recCityCode, _recCityName, _recCountryCode, _recID, _recPhoneNumber,
							 _weight, _height, _width, _depth);
		
		// return to "Order processing" view
		Parent root = FXMLLoader.load(getClass().getResource("OrderConfirmationSpecialistOrderProcessing.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void switchPageLeft() {
		System.out.println("left");
		if(page > 0) {
			page--;
			populateContent();
		}
	}
	public void switchPageRight() {
		System.out.println("Right");
		int last_page = parcels.size()/7;
		if (page < last_page) {
			page++;
			populateContent();
		}
	}
	public void toggleCheckBox(ActionEvent event) {
		CheckBox box = (CheckBox) event.getSource();
		String id = box.getId();
		System.out.println(id);
		switch(id) {
		case "c1": data.deliveryDriverAction("parcel confirmed", parcels.get(0)[1], "OCS"); break;
		case "c2": data.deliveryDriverAction("parcel confirmed", parcels.get(1)[1], "OCS"); break;
		case "c3": data.deliveryDriverAction("parcel confirmed", parcels.get(2)[1], "OCS"); break;
		case "c4": data.deliveryDriverAction("parcel confirmed", parcels.get(3)[1], "OCS"); break;
		case "c5": data.deliveryDriverAction("parcel confirmed", parcels.get(4)[1], "OCS"); break;
		case "c6": data.deliveryDriverAction("parcel confirmed", parcels.get(5)[1], "OCS"); break;
		case "c7": data.deliveryDriverAction("parcel confirmed", parcels.get(6)[1], "OCS"); break;
		}


	}

}
