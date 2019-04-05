package application;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	private static double xOffset = 0;
	private static double yOffset = 0;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//primaryStage.getIcons().add(new Image("/imageAssets/icon.png"));

			
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("MainUI.fxml"));
			
			
			
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			
			primaryStage.show();
			
			
			root.setOnMousePressed(new EventHandler<MouseEvent>()
			{
	            @Override
	            public void handle(MouseEvent event)
	            {
	                xOffset = event.getSceneX();
	                yOffset = event.getSceneY();
	            }
	        });
	        root.setOnMouseDragged(new EventHandler<MouseEvent>()
	        {
	            @Override
	            public void handle(MouseEvent event)
	            {
	                primaryStage.setX(event.getScreenX() - xOffset);
	                primaryStage.setY(event.getScreenY() - yOffset);
	                primaryStage.setOpacity(0.7f);
	            }
	        });
	        root.setOnDragDone(e ->
	        {
	        	primaryStage.setOpacity(1.0f);
	        });
	        root.setOnMouseReleased(e ->
	        {
	        	primaryStage.setOpacity(1.0f);
	        });
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
