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


public class Main extends Application {
	
	private static double xOffset = 0;
	private static double yOffset = 0;
	
	@Override
	public void start(Stage primaryStage)
	{
		try 
		{
			//primaryStage.getIcons().add(new Image("/imageAssets/icon.png"));
			
			FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("MainUI.fxml"));
	        Parent mainParent = mainLoader.load();
	        Scene mainScene = new Scene(mainParent);
			
	        MainUIController minPaneController = (MainUIController) mainLoader.getController();
	        minPaneController.setPrimaryStage(primaryStage);

			mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(mainScene);
			
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			//primaryStage.setResizable(true); doesn't work
			ResizeHelper.addResizeListener(primaryStage);

			primaryStage.show();
			
//			mainParent.setOnMousePressed(new EventHandler<MouseEvent>()
//			{
//	            @Override
//	            public void handle(MouseEvent event)
//	            {
//	                xOffset = event.getSceneX();
//	                yOffset = event.getSceneY();
//	            }
//	        });
	        mainParent.setOnMouseDragged(new EventHandler<MouseEvent>()
	        {
	            @Override
	            public void handle(MouseEvent event)
	            {
//	                primaryStage.setX(event.getScreenX() - xOffset);
//	                primaryStage.setY(event.getScreenY() - yOffset);
	                primaryStage.setOpacity(0.7f);
	            }
	        });
	        mainParent.setOnDragDone(e ->
	        {
	        	primaryStage.setOpacity(1.0f);
	        });
	        mainParent.setOnMouseReleased(e ->
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
