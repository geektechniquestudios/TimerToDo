package application;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import itemPopulation.*;

public class Main extends Application 
{
	@Override
	public void start(Stage primaryStage)
	{
		try 
		{
			//primaryStage.getIcons().add(new Image("/imageAssets/icon.png"));
					
	        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
	        Parent loginParent = loginLoader.load();
	        Scene loginScene = new Scene(loginParent);
	        
	        LoginController login = (LoginController) loginLoader.getController();
	        login.setStage(primaryStage);

			loginScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(loginScene);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			ResizeHelper.addResizeListener(primaryStage);

			primaryStage.show();
			
	        loginParent.setOnMouseDragged(new EventHandler<MouseEvent>()//makes transparent on drag
	        {
	            @Override
	            public void handle(MouseEvent event)
	            {       
	                primaryStage.setOpacity(0.7f);
	            }
	        });
	        loginParent.setOnDragDone(e ->
	        {
	        	primaryStage.setOpacity(1.0f);
	        });
	        loginParent.setOnMouseReleased(e ->
	        {
	        	primaryStage.setOpacity(1.0f);
	        });
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
