package application;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import itemPopulation.ResizeHelper;
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

	public void retryWasHit()
	{
		MainUIController.setDatabaseAddress(addressField.getText());
		MainUIController.setUsername(nameField.getText());
		MainUIController.setPassword(pwField.getText());
		
		thisStage.close();	
	}
	
	public void exitWasHit()
	{
		thisStage.close();
		System.exit(0);//Platform.exit(); didn't work, this throws fatal error. Def a better way
	}

	public void setStage(Stage someStage)
	{
		thisStage = someStage;
	} 
	
	EventHandler<ActionEvent> addResourceHandler = event -> 
	{
	    retryWasHit();
	};
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		//ResizeHelper.addResizeListener(thisStage);
		Platform.runLater(() ->
		{
			mainBorder.requestFocus();

		});
		
		pwField.setOnAction(addResourceHandler);
		nameField.setOnAction(addResourceHandler);
		addressField.setOnAction(addResourceHandler);
	
	}
}
