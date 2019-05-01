package application;

import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FailedConnectionController implements Initializable
{
	private Stage thisStage;
	@FXML private BorderPane mainBorder;
	@FXML private JFXTextField addressField;
	@FXML private JFXTextField nameField;
	@FXML private JFXPasswordField pwField;
 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		Platform.runLater(() ->
		{
			mainBorder.requestFocus();
		});
		
		pwField.setOnAction(addResourceHandler);
		nameField.setOnAction(addResourceHandler);
		addressField.setOnAction(addResourceHandler);
	}
	
	@FXML private void retryWasHit()
	{
		MainUIController.setDatabaseAddress(addressField.getText());
		MainUIController.setUsername(nameField.getText());
		MainUIController.setPassword(pwField.getText());
		
		thisStage.close();	
	}
	
	@FXML private void exitWasHit()
	{
		Platform.exit();
	}

	public void setStage(Stage someStage)
	{
		thisStage = someStage;
	} 
	
	EventHandler<ActionEvent> addResourceHandler = event -> //this makes ENTER do stuff
	{
	    retryWasHit();
	};
	

}
