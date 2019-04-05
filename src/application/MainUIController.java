package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import itemPopulation.ToDoTableItems;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
	
	@FXML private TableView<ToDoTableItems> toDoTable;
	
	@FXML private TableColumn<ToDoTableItems, String> timeDue; 
	@FXML private TableColumn<ToDoTableItems, String> timeStarted; 
	@FXML private TableColumn<ToDoTableItems, String> person; 
	@FXML private TableColumn<ToDoTableItems, String> task; 
	@FXML private TableColumn<ToDoTableItems, String> taskID; 
	@FXML private TableColumn<ToDoTableItems, String> completed; 

	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		setupTables();
		//while(!hasServerConnected)
		//{
			getSQLTable();
			//try to connect to server and select all from db
			//if failed, display message with a retry button
		//}	
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
					completeString//returnStatement.getBoolean("complete")
				));
//			System.out.print(returnStatement.getString("time_due") + " ");
//			System.out.print(returnStatement.getString("time_started") + " ");
//			System.out.print(returnStatement.getString("person") + " ");
//			System.out.print(returnStatement.getString("task") + " ");
//			System.out.print(returnStatement.getString("task_description") + " ");


			//System.out.println(returnStatement.next());				
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
		}
	}
	
	private void sqlFail()
	{
		//prompt user to make sure server is running or something 
		
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
}
