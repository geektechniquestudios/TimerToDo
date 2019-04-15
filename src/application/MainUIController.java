package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import itemPopulation.ResizeHelper;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainUIController implements Initializable
{
	static boolean hasServerConnected = false;
	boolean hasLoginShown = false;
	private String completeString;
	private Stage primaryStage;
	private AnchorPane newTaskPane;
	private AnchorPane settingsPane;
	private HBox bottomHBox;
	private VBox bottomEditPane;
	private String lastRowClicked;
	private String lastDescriptionClicked;
	
	private static String databaseAddress;
	private static String username;
	private static String password;
	
	private int lastIndex;
	private EditController edit;
	
	@FXML private TableView<ToDoTableItems> toDoTable;
	
	@FXML private TableColumn<ToDoTableItems, String> timeDue; 
	@FXML private TableColumn<ToDoTableItems, String> timeStarted; 
	@FXML private TableColumn<ToDoTableItems, String> person; 
	@FXML private TableColumn<ToDoTableItems, String> task; 
	@FXML private TableColumn<ToDoTableItems, String> taskID; 
	@FXML private TableColumn<ToDoTableItems, String> completed; 

	@FXML private BorderPane mainBorderPane;
	@FXML private VBox descriptionBox;
	@FXML private JFXButton editButton;
	@FXML private JFXButton deleteButton;
	@FXML private Label descriptionLabel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		setupTables();
		while(!hasServerConnected)
		{
			getSQLTable();
		}	
		
		
		setUpNewTask();
		Platform.runLater(()->
		{
			mainBorderPane.requestFocus();
		});
		
		bottomHBox = (HBox)mainBorderPane.getBottom();
		setupBottomEditPane();
		setupSettingsPane();
	}
	
	public void getSQLTable()
	{
		Connection someConnection = null;
		ObservableList<ToDoTableItems> itemsToReturn = FXCollections.observableArrayList();
		
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");			
			someConnection = DriverManager.getConnection("jdbc:mysql:" + databaseAddress,username,password);			
			Statement queryToSend = someConnection.createStatement();
	
			ResultSet returnStatement = queryToSend.executeQuery("SELECT * FROM list_items");
			
			while(returnStatement.next())
			{
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
			popup("SuccessPopup.fxml");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			sqlFail();		
		}	
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
			sqlFail();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			sqlFail();
		}
	}
	
	private void sqlFail()//@todo: make a bool for once the user has logged in. If they have, show "there was a problem" dialog
	{
		if(hasServerConnected)
		{
			popup("ConnectionFailedPopup.fxml");
		}
		else
		{
			Stage popupStage = new Stage();
				
			try
			{
				FXMLLoader popupRoot = new FXMLLoader(getClass().getResource("FailedConnection.fxml"));
				Parent rootParent = popupRoot.load();
				Scene rootScene = new Scene(rootParent);
	
				FailedConnectionController failController = (FailedConnectionController) popupRoot.getController();
		        failController.setStage(popupStage);
	
				popupStage.setScene(rootScene);
				popupStage.initStyle(StageStyle.TRANSPARENT);
				ResizeHelper.addResizeListener(popupStage);
	
		        rootParent.setOnMouseDragged(new EventHandler<MouseEvent>()
		        {
		            @Override
		            public void handle(MouseEvent event)
		            {
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
				e.printStackTrace();
			}	
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
		
		toDoTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}
	
	private void setUpNewTask()
	{
		try
		{
			FXMLLoader newTaskLoader = new FXMLLoader(getClass().getResource("NewTask.fxml"));
			Parent newTaskParent = newTaskLoader.load();
			
			newTaskPane = (AnchorPane)newTaskParent;
			
			NewTaskController.setDatabaseAddress(databaseAddress);
			NewTaskController.setUsername(username);
			NewTaskController.setPassword(password);
			
			NewTaskController task = (NewTaskController)newTaskLoader.getController();
			//task.setToDoTable(toDoTable);
			task.setMainUI(this);
		}
		catch(Exception e)
		{
			System.out.println("another test");
		}
	}
	

	private void setupBottomEditPane()
	{
		try
		{
			FXMLLoader editLoader = new FXMLLoader(getClass().getResource("EditData.fxml"));
			Parent editParent = editLoader.load();
			
			bottomEditPane = (VBox)editParent;
			
			edit = (EditController) editLoader.getController();
			edit.setMainUIController(this);
			edit.setDatabaseAddress(databaseAddress);
			edit.setUsername(username);
			edit.setPassword(password);
			edit.setDescriptionLabel(descriptionLabel);


		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void setupSettingsPane()
	{
		try 
		{
			FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("Settings.fxml"));
			Parent settingsParent = settingsLoader.load();
			
			SettingsController settingsController = (SettingsController)settingsLoader.getController();
			settingsController.setMainUIController(this);
			
			settingsPane = (AnchorPane)settingsParent;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	//fxml below
	@FXML private void exitWasHit()
	{
		Platform.exit();
	}
	
	@FXML private void maxWasHit()
	{
		primaryStage.setMaximized(!primaryStage.isMaximized());
	}
	
	@FXML private void minWasHit()
	{
		primaryStage.setIconified(true);
	}
	
	public void mainListWasHit()//also @FXML
	{
		mainBorderPane.setRight(descriptionBox);
		mainBorderPane.setCenter(toDoTable);
		mainBorderPane.setBottom(bottomHBox);
	}
	
	@FXML private void newTaskWasHit()
	{
		mainBorderPane.setRight(null);
		mainBorderPane.setCenter(newTaskPane);
		mainBorderPane.setBottom(null);
		
	}
	
	@FXML private void settingsWasHit()
	{
		mainBorderPane.setRight(null);
		mainBorderPane.setCenter(settingsPane);//set settings as database menu
		mainBorderPane.setBottom(null);
	}
	
	@FXML private void deleteButtonHit()
	{
		Connection someConnection = null;
		
		String deleteQuery =  "delete from list_items where task_id = " + lastRowClicked; 
		 
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");			
			someConnection = DriverManager.getConnection("jdbc:mysql:" + databaseAddress,username,password);			
			Statement queryToSend = someConnection.createStatement();
	
			queryToSend.executeUpdate(deleteQuery);
			
			//update todotable
			getSQLTable();
			descriptionLabel.setText("");
		
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			popup("ConnectionFailedPopup.fxml");
		}	
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
			popup("ConnectionFailedPopup.fxml");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			popup("ConnectionFailedPopup.fxml");
		}
	}
	
	@FXML private void editButtonHit()
	{
		mainBorderPane.setBottom(bottomEditPane);
		mainBorderPane.requestFocus();
	}
		
	@FXML private void tableWasHit()
	{
		String lastPersonClicked = toDoTable.getSelectionModel().getSelectedItem().getPerson(); 
		String lastTaskClicked = toDoTable.getSelectionModel().getSelectedItem().getTask();
		String lastTimeDueClicked = toDoTable.getSelectionModel().getSelectedItem().getTimeDue();
		
		lastRowClicked = toDoTable.getSelectionModel().getSelectedItem().getTaskID();
		lastDescriptionClicked = toDoTable.getSelectionModel().getSelectedItem().getTaskDescription();
		descriptionLabel.setText(lastDescriptionClicked);
		
		edit.setDescription(lastDescriptionClicked);
		edit.setPerson(lastPersonClicked);
		edit.setTask(lastTaskClicked);
		EditController.setTaskNum(lastRowClicked);
		
		lastIndex = toDoTable.getSelectionModel().getSelectedIndex();
	
		LocalDate dateToPass = LocalDate.parse(lastTimeDueClicked.substring(0, 10));
		LocalTime timeToPass = LocalTime.parse(lastTimeDueClicked.substring(11, 19));
	
		edit.setDatePicker(dateToPass);
		edit.setTimePicker(timeToPass);
	}
	
	@FXML private void markComplete()
	{
		int isComplete;
		try 
		{	
			
			if(toDoTable.getSelectionModel().getSelectedItem().getCompleted().equals("incomplete"))
			{
				isComplete = 1;
			}
			else
			{
				isComplete = 0;
			}
		
			Connection  someConnection = null;
		

			Class.forName("com.mysql.cj.jdbc.Driver");			
			someConnection = DriverManager.getConnection("jdbc:mysql:" + databaseAddress,username,password);			
			Statement queryToSend = someConnection.createStatement();
	
			queryToSend.executeUpdate
			(			
				"update list_items " 																		+
					"set completed = \"" + isComplete + "\" "												+
				"where task_id = " + lastRowClicked + ";"
			);
			
			getSQLTable();
			highlightIndex();
			//@todo: if no exception, success window popup, if catch, error window.		
		}
		catch(SQLException e)
		{
			System.out.println("test1");
			e.printStackTrace();
			popup("ConnectionFailedPopup.fxml");
		} 
		catch (ClassNotFoundException e) 
		{
			System.out.println("test2");
			e.printStackTrace();
			popup("ConnectionFailedPopup.fxml");
		}	
	}
	
	public void highlightIndex()
	{
		Platform.runLater(() ->
		{
			toDoTable.getSelectionModel().select(lastIndex);
		});
	}
	
	void popup(String fxmlFile)
	{
		try
		{
			Stage popupStage = new Stage();
			popupStage.initStyle(StageStyle.TRANSPARENT);
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setResizable(false);
			
			FXMLLoader popupRoot = new FXMLLoader(getClass().getResource(fxmlFile));
			Parent popupParent = popupRoot.load();
			Scene popupScene = new Scene(popupParent);
			
			popupStage.setX
			(
				primaryStage.getX() + (primaryStage.getWidth()/2) - 155		
			);
			popupStage.setY
			(
				primaryStage.getY() + (primaryStage.getHeight()/2)- 75
			);
			
			popupStage.setScene(popupScene);
			popupStage.show();
			
			if(fxmlFile.equals("ConnectionFailedPopup.fxml"))
			{
				ConnectionFailedPopup failController = (ConnectionFailedPopup)popupRoot.getController();		
				failController.setPopupStage(popupStage);
			}
			else
			{
				SuccessPopupController successController = (SuccessPopupController)popupRoot.getController();
				successController.setPopupStage(popupStage);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("This should fire once intentionally");
		}
	}
	
	//setters
	public static void setHasServerConnected(boolean hasIt)
	{
		hasServerConnected = hasIt;
	}
	
	public static void setDatabaseAddress(String newDatabaseAddress)
	{
		databaseAddress = newDatabaseAddress;
	}
	
	public static void setUsername(String someUsername)
	{
		username = someUsername;
	}
	
	public static void setPassword(String somePassword)
	{
		password = somePassword;
	}
	
	public void setPrimaryStage(Stage someStage)
	{
		primaryStage = someStage;
	}
}