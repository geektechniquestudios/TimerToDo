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
	private HBox bottomHBox;
	private VBox bottomEditPane;
	private String lastRowClicked;
	private String lastDescriptionClicked;
	
	private static String databaseAddress;
	private static String username;
	private static String password;
	
	private int lastIndex;
	private EditController login;
	
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
		
		
		setUpSwitching();
		Platform.runLater(()->
		{
			mainBorderPane.requestFocus();
		});
		
		bottomHBox = (HBox)mainBorderPane.getBottom();
		setupBottomEditPane();
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
			errorPopup();
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
	
	private void setUpSwitching()
	{
		try
		{
			FXMLLoader newTaskLoader = new FXMLLoader(getClass().getResource("NewTask.fxml"));
			Parent newTaskParent = newTaskLoader.load();
			
			newTaskPane = (AnchorPane)newTaskParent;
			
			NewTaskController task = (NewTaskController)newTaskLoader.getController();
			task.setDatabaseAddress(databaseAddress);
			task.setUsername(username);
			task.setPassword(password);
			task.setToDoTable(toDoTable);
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
			
			login = (EditController) editLoader.getController();
			login.setMainUIController(this);
			login.setDatabaseAddress(databaseAddress);
			login.setUsername(username);
			login.setPassword(password);
			login.setDescriptionLabel(descriptionLabel);


		}
		catch(Exception e)
		{
			e.printStackTrace();
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
		mainBorderPane.setRight(descriptionBox);
		mainBorderPane.setCenter(toDoTable);
		mainBorderPane.setBottom(bottomHBox);
	}
	
	public void newTaskWasHit()
	{
		mainBorderPane.setRight(null);
		mainBorderPane.setCenter(newTaskPane);
		mainBorderPane.setBottom(null);
		
	}
	
	public void settingsWasHit()
	{
		mainBorderPane.setRight(null);
		mainBorderPane.setCenter(null);
		mainBorderPane.setBottom(null);
	}
	
	public void deleteButtonHit()
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
			errorPopup();
		}	
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
			errorPopup();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			errorPopup();
		}
	}
	
	public void editButtonHit()
	{
		mainBorderPane.setBottom(bottomEditPane);
		mainBorderPane.requestFocus();
	}
		
	public void tableWasHit()
	{
		String lastPersonClicked = toDoTable.getSelectionModel().getSelectedItem().getPerson(); 
		String lastTaskClicked = toDoTable.getSelectionModel().getSelectedItem().getTask();
		String lastTimeDueClicked = toDoTable.getSelectionModel().getSelectedItem().getTimeDue();
		
		lastRowClicked = toDoTable.getSelectionModel().getSelectedItem().getTaskID();
		lastDescriptionClicked = toDoTable.getSelectionModel().getSelectedItem().getTaskDescription();
		descriptionLabel.setText(lastDescriptionClicked);
		
		login.setDescription(lastDescriptionClicked);
		login.setPerson(lastPersonClicked);
		login.setTask(lastTaskClicked);
		EditController.setTaskNum(lastRowClicked);
		
		lastIndex = toDoTable.getSelectionModel().getSelectedIndex();
	
		LocalDate dateToPass = LocalDate.parse(lastTimeDueClicked.substring(0, 10));
		LocalTime timeToPass = LocalTime.parse(lastTimeDueClicked.substring(11, 19));
	
		login.setDatePicker(dateToPass);
		login.setTimePicker(timeToPass);
	}
	
	public void markComplete()
	{
		int isComplete;
		
		if(toDoTable.getSelectionModel().getSelectedItem().getCompleted().equals("incomplete"))
		{
			isComplete = 1;
		}
		else
		{
			isComplete = 0;
		}
		
		Connection  someConnection = null;
		
		try
		{
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
			e.printStackTrace();
			errorPopup();
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			errorPopup();
		}
		
		
	}
	
	public void highlightIndex()
	{
		Platform.runLater(() ->
		{
			toDoTable.getSelectionModel().select(lastIndex);
		});
	}
	
	
	void errorPopup()
	{
		try
		{
			Stage popupStage = new Stage();
			popupStage.initStyle(StageStyle.TRANSPARENT);
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setResizable(false);
			
			FXMLLoader popupRoot = new FXMLLoader(getClass().getResource("ConnectionFailedPopup.fxml"));
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
			
			ConnectionFailedPopup failController = (ConnectionFailedPopup)popupRoot.getController();		
			failController.setPopupStage(popupStage);
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
}