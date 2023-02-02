package application;

import java.io.IOException;
import java.net.URL;
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
import javafx.stage.Stage;

public class LogisticsAgentController implements Initializable {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Data data = new Data();
	String branch = "None";
	
	@FXML private ChoiceBox<String> branchChoiceBox;
	@FXML private ChoiceBox<String> employeeChoiceBox;
	@FXML private Label p1, p2, p3, p4; // Inbound, Outbound, Branch Load, Avg. Load
	@FXML private Label d1, d2, d3;		// All jobs for drivers, No. of drivers, Avg. no. of jobs per driver
	@FXML private Label employeeNoOfJobsLabel;
	@FXML private Label warningLabel;
	@FXML private ImageView warningIcon;
	@FXML private Label 		userFullName;
	@FXML private ImageView		userIcon;

	public void hideMainContent() {
		// hide all data until branch is selected
		p1.setVisible(false);
		p2.setVisible(false);
		p3.setVisible(false);
		p4.setVisible(false);
		d1.setVisible(false);
		d2.setVisible(false);
		d3.setVisible(false);
		warningLabel.setVisible(false);
		warningIcon.setVisible(false);
		employeeChoiceBox.setVisible(false);
		employeeNoOfJobsLabel.setVisible(false);
	}

	public void showMainContent() {
		p1.setVisible(true);
		p2.setVisible(true);
		p3.setVisible(true);
		p4.setVisible(true);
		d1.setVisible(true);
		d2.setVisible(true);
		d3.setVisible(true);
		employeeChoiceBox.setVisible(true);

	}

	public void setUpBranchChoiceBox() {
		branchChoiceBox.getItems().addAll(data.getBranchesArray());
		branchChoiceBox.setOnAction(this::branchChoiceBoxFunction);
	}

	public void setUpEmployeeChoiceBox() {
		employeeChoiceBox.getItems().addAll(data.getEmployeesByBranchArray(branch));
		employeeChoiceBox.setOnAction(this::employeeChoiceBoxFunction);
	}

	public void employeeChoiceBoxFunction(ActionEvent event) {
		String noOfJobsOfEmployee = data.getNoOfJobsOfEmployee(employeeChoiceBox.getValue(), branch);
		employeeNoOfJobsLabel.setText("⚫ No. of jobs: " + noOfJobsOfEmployee);
		employeeNoOfJobsLabel.setVisible(true);
	}

	// fires when branch is selected from the ChoiceBox
	public void branchChoiceBoxFunction(ActionEvent event) {

		//if (branch == "None") setUpEmployeeChoiceBox();

		branch = branchChoiceBox.getValue();
		System.out.println("Changed filter! - " + branch);
		String[] branchData = data.getBranchData(branch);
		p1.setText("⚫ Inbound: " + branchData[0]);
		p2.setText("⚫ Outbound: " + branchData[1]);
		p3.setText("⚫ Branch Load: " + branchData[2]);
		p4.setText("⚫ Average Load: " + branchData[3]);
		d1.setText("⚫ All jobs for drivers: " + branchData[4]);
		d2.setText("⚫ No. of drivers: " + branchData[5]);
		d3.setText("⚫ Avg. no. of jobs per driver: " + branchData[6]);

		// if there isnt enough drivers in this branch, show the warning
		if (!data.isEnoughDriversInThisBranch(branch)) {
			warningLabel.setVisible(true);
			warningIcon.setVisible(true);
		}

		// display the content
		showMainContent();
	}

	public void settingsButton(MouseEvent event) {
		System.out.println("Pressed button!");
	}

	public void signOutAnchorPaneButton(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		userFullName.setText(data.getUserFullName());
		userIcon.setImage(new Image(getClass().getResourceAsStream("/res/" + data.getUserProfilePicture())));
		setUpBranchChoiceBox();
		hideMainContent();
	}
}
