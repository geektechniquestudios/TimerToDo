package application;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
			
//			FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("MainUI.fxml"));
//	        Parent mainParent = mainLoader.load();
//	        Scene mainScene = new Scene(mainParent);
			
	        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
	        Parent loginParent = loginLoader.load();
	        Scene loginScene = new Scene(loginParent);
	        
	        LoginController login = (LoginController) loginLoader.getController();
	        login.setStage(primaryStage);
	        
//	        MainUIController minPaneController = (MainUIController) mainLoader.getController();
//	        minPaneController.setPrimaryStage(primaryStage); 	

			loginScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(loginScene);
//			primaryStage.setMinWidth(1000);
//			primaryStage.setMinHeight(675);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			ResizeHelper.addResizeListener(primaryStage);

			primaryStage.show();
			
//	        mainParent.setOnMouseDragged(new EventHandler<MouseEvent>()
//	        {
//	            @Override
//	            public void handle(MouseEvent event)
//	            {
////	                
//	                primaryStage.setOpacity(0.7f);
//	            }
//	        });
//	        mainParent.setOnDragDone(e ->
//	        {
//	        	primaryStage.setOpacity(1.0f);
//	        });
//	        mainParent.setOnMouseReleased(e ->
//	        {
//	        	primaryStage.setOpacity(1.0f);
//	        });
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
