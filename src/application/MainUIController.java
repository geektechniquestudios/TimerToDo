package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import itemPopulation.ToDoTableItems;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/*
insert into list_items 
(
	time_due, 
	person, 
	task, 
	task_description,
	completed
) 
	values 
(
	'2020-10-29 12:00:01', 
	"Deb", 
	"Do more stuff", 
	"another longer description of stuff to do",
	false
	
);		

create table list_items
(
	time_due datetime, 
	time_started timestamp default current_timestamp on update current_timestamp, 
	person varchar(255), 
	task varchar(20), 
	task_description varchar(255),
	completed boolean default false, 
	task_id int auto_increment,
	 
	primary key (task_id)
);

*/

public class MainUIController implements Initializable
{
	static boolean hasServerConnected = false;
	private String completeString;
	private Stage primaryStage;
	private static double xOffset = 0;
	private static double yOffset = 0;
	private AnchorPane newTaskPane;

	
	@FXML private TableView<ToDoTableItems> toDoTable;
	
	@FXML private TableColumn<ToDoTableItems, String> timeDue; 
	@FXML private TableColumn<ToDoTableItems, String> timeStarted; 
	@FXML private TableColumn<ToDoTableItems, String> person; 
	@FXML private TableColumn<ToDoTableItems, String> task; 
	@FXML private TableColumn<ToDoTableItems, String> taskID; 
	@FXML private TableColumn<ToDoTableItems, String> completed; 

	@FXML private BorderPane mainBorderPane;
	@FXML private VBox descriptionBox;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		setupTables();
		while(!hasServerConnected)
		{
			getSQLTable();
			
			//try to connect to server and select all from db
			//if failed, display message with a retry button
		}	
		
		setUpSwitching();
	}
	
	private void getSQLTable()
	{
		Connection someConnection = null;
		ObservableList<ToDoTableItems> itemsToReturn = FXCollections.observableArrayList();
		
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");			
			someConnection = DriverManager.getConnection("jdbc:mysql://localhost/todolist","root","Mitbtes1991");			
			Statement queryToSend = someConnection.createStatement();
	
			ResultSet returnStatement = queryToSend.executeQuery("SELECT * FROM list_items");
			
			while(returnStatement.next())
			{
				System.out.println(returnStatement.getInt("completed"));
				if(returnStatement.getInt("completed") == 1)
				{
					completeString = "completed";
				}
				else
				{
					completeString = "incomplete";
				}
				itemsToReturn.add(new ToDoTableItems
				(
					returnStatement.getString("time_due"),
					returnStatement.getString("time_started"),
					returnStatement.getString("person"),
					returnStatement.getString("task"),
					returnStatement.getString("task_id"),
					completeString,
					
					returnStatement.getString("task_description")
				));			
			}
			
			toDoTable.setItems(itemsToReturn);
			hasServerConnected = true;
				
		}
		catch(SQLException e)
		{
			System.out.println("didn't work");
			e.printStackTrace();
			sqlFail();
			
		}	
		catch(ClassNotFoundException e)
		{
			System.out.println("didn't work 2");
			e.printStackTrace();
			sqlFail();
		}
		catch(Exception e)
		{
			System.out.println("didn't work 3");
			e.printStackTrace();
			sqlFail();
		}
	}
	
	private void sqlFail()
	{
		Stage popupStage = new Stage();//send to failed connections controller
		
		try
		{
			FXMLLoader popupRoot = new FXMLLoader(getClass().getResource("FailedConnection.fxml"));
			Parent rootParent = popupRoot.load();
			Scene rootScene = new Scene(rootParent);
			
	        FailedConnectionController failController = (FailedConnectionController) popupRoot.getController();
	        failController.setStage(popupStage);
			
			popupStage.setScene(rootScene);
			popupStage.initStyle(StageStyle.TRANSPARENT);
		
			
			
			rootParent.setOnMousePressed(new EventHandler<MouseEvent>()
			{
	            @Override
	            public void handle(MouseEvent event)
	            {
	                xOffset = event.getSceneX();
	                yOffset = event.getSceneY();
	            }
	        });
	        rootParent.setOnMouseDragged(new EventHandler<MouseEvent>()
	        {
	            @Override
	            public void handle(MouseEvent event)
	            {
	                popupStage.setX(event.getScreenX() - xOffset);
	                popupStage.setY(event.getScreenY() - yOffset);
	                popupStage.setOpacity(0.7f);
	            }
	        });
	        rootParent.setOnDragDone(e ->
	        {
	        	popupStage.setOpacity(1.0f);
	        });
	        rootParent.setOnMouseReleased(e ->
	        {
	        	popupStage.setOpacity(1.0f);
	        });
	        
	        popupStage.showAndWait();
			
		}
		catch(Exception e)
		{
			System.out.println("mistake");
			sqlFail();
		}	
	}
	
	private void setupTables()
	{
		timeDue.setCellValueFactory(new PropertyValueFactory<ToDoTableItems, String>("timeDue"));
		timeStarted.setCellValueFactory(new PropertyValueFactory<ToDoTableItems, String>("timeStarted"));
		person.setCellValueFactory(new PropertyValueFactory<ToDoTableItems, String>("person"));
		task.setCellValueFactory(new PropertyValueFactory<ToDoTableItems, String>("task"));
		taskID.setCellValueFactory(new PropertyValueFactory<ToDoTableItems, String>("taskID"));
		completed.setCellValueFactory(new PropertyValueFactory<ToDoTableItems, String>("completed"));
	}
	//firstTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);//all columns autosize, might remove
	
	private void setUpSwitching()
	{
		try
		{
			newTaskPane = (AnchorPane)FXMLLoader.load(getClass().getResource("NewTask.fxml"));
		}
		catch(Exception e)
		{
			System.out.println("another fucking test");
		}
	}
	
	public void setPrimaryStage(Stage someStage)
	{
		primaryStage = someStage;
	}
	
	
	
	
	
	//fxml below
	public void exitWasHit()
	{
		Platform.exit();
	}
	
	public void maxWasHit()
	{
		primaryStage.setMaximized(!primaryStage.isMaximized());
	}
	
	public void minWasHit()
	{
		primaryStage.setIconified(true);
	}
	
	public void mainListWasHit()
	{
		//TableView mainToDo = (TableView)mainBorderPane.getCenter();
		//mainBorderPane.setCenter(value);
		mainBorderPane.setRight(descriptionBox);
		mainBorderPane.setCenter(toDoTable);
	}
	
	public void newTaskWasHit()
	{
		mainBorderPane.setRight(null);
		mainBorderPane.setCenter(newTaskPane);
	}
	
	public void settingsWasHit()
	{
		mainBorderPane.setRight(null);
		mainBorderPane.setCenter(null);
	}
}
