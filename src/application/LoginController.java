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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LoginController implements Initializable
{
	@FXML private JFXTextField addressField;
	@FXML private JFXTextField nameField;
	@FXML private JFXPasswordField pwField;
	@FXML private BorderPane mainBorder;
	
	Stage thisStage;
	
	public void exitWasHit()
	{
		Platform.exit();
	}
	
	public void submitWasHit()
	{         
		MainUIController.setDatabaseAddress(addressField.getText());
		MainUIController.setUsername(nameField.getText());
		MainUIController.setPassword(pwField.getText());
		
		thisStage.close();
		try
		{
			FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("MainUI.fxml"));
	        Parent mainParent = mainLoader.load();
	        Scene mainScene = new Scene(mainParent);
	        
	        //set primary stage
	        MainUIController minPaneController = (MainUIController) mainLoader.getController();
	        minPaneController.setPrimaryStage(thisStage);
	        
	        thisStage.setScene(mainScene);
	        thisStage.show();
	        ResizeHelper.addResizeListener(thisStage);
			thisStage.setMinWidth(1000);
			thisStage.setMinHeight(675);
			
			 mainParent.setOnMouseDragged(new EventHandler<MouseEvent>()
	        {
	            @Override
	            public void handle(MouseEvent event)
	            {
	                thisStage.setOpacity(0.7f);
	            }
	        });
	        mainParent.setOnDragDone(e ->
	        {
	        	thisStage.setOpacity(1.0f);
	        });
	        mainParent.setOnMouseReleased(e ->
	        {
	        	thisStage.setOpacity(1.0f);
	        });
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setStage(Stage someStage)
	{
		thisStage = someStage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		pwField.setOnAction(addResourceHandler);
		nameField.setOnAction(addResourceHandler);
		addressField.setOnAction(addResourceHandler);
		
		Platform.runLater(() ->
		{
			mainBorder.requestFocus();

		});
	}
	
	EventHandler<ActionEvent> addResourceHandler = event -> 
	{
	    submitWasHit();
	};

	
}
