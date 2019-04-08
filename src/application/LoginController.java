package application;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import itemPopulation.ResizeHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController 
{
	@FXML private JFXTextField addressField;
	@FXML private JFXTextField nameField;
	@FXML private JFXPasswordField pwField;
	
	Stage thisStage;
	
	public void exitWasHit()
	{
		//thisStage.close();
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
	        
	        thisStage.setScene(mainScene);
	        thisStage.show();
	        ResizeHelper.addResizeListener(thisStage);
			thisStage.setMinWidth(1000);
			thisStage.setMinHeight(675);
			
		}
		catch(Exception e)
		{}
		System.out.println("test");
	}
	
	public void setStage(Stage someStage)
	{
		thisStage = someStage;
	}
}
