package application.logIn_signUp;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import application.AlertMessage;
import application.Choices;
import application.DB_Connection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LogIn_Controller extends DB_Connection implements Initializable {

	 Parent root;
     Scene scene;
     Stage stage;
	
    @FXML
    private TextField email;
    @FXML
    private TextField password;
	@FXML
	private ComboBox<String> user;
	
	 // Method to display the user type options
    public void userChoices() {
        Choices choice = new Choices(); 
        choice.userChoices(user);
    }
    
    
    // confirms if the email is a valid email by verifying its format
    // this is regular expression for email validation provided by the RFC (Request for comments) standards.
    // A Request for Comments (RFC) is a formal document from the Internet Engineering Task Force (IETF) that contains specifications and organizational notes about topics related to the Internet and computer networking
    public static boolean isEmailValid(TextField email) 
    { 
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        
        Pattern pat = Pattern.compile(regexPattern); 
        if (email == null || email.getText().isEmpty()) 
            return false; 
        return  pat.matcher(email.getText()).matches();
    }

    
    public void logIn(ActionEvent event) throws IOException {
    	connection = connect();
    	AlertMessage alert = new AlertMessage();
    	
    	if (email.getText().isEmpty() || password.getText().isEmpty())
    		alert.errorMessage("Please fill up all details.");
    	else if (isEmailValid(email) == false ) // Check if email format is valid
    		alert.errorMessage("Invalid email format.");
    	else {
    		
        	         // Login successful
        	        userChoices();
        	        	
            	       String userType = user.getValue();
            	       String promptText = user.getPromptText();

            	  if ("Employee".equals(userType) || "Employee".equals(promptText)) {
            	        	
            	     // Query the database to check if email and password match
            		 String queryEmployee = "SELECT Email, Password FROM hotel_coordinator WHERE email = ? AND password = ?";
                     try {
                    	   prepare = connection.prepareStatement(queryEmployee);
                    	   prepare.setString(1, email.getText());
                    	   prepare.setString(2, password.getText());
                    	   result = prepare.executeQuery();
                    	        
                    	 if (result.next()) {
            	        	// Load the FXML file of the selected page
            	            root = FXMLLoader.load(getClass().getResource("/application/hotelCoord/HotelCoordPage.fxml"));
            	            scene = new Scene(root);
            	            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            	         // Set the scene in the stage
                	        stage.setScene(scene);
                	        stage.show();
            	     
                    	  } else {
                    	     // Login failed
                    	     alert.errorMessage("Incorrect email or password.");
                    	 }
                   } 
	               catch (SQLException e) {
	            	   e.printStackTrace();
	                // Handle database error
	            	   alert.errorMessage("An error occurred. Please try again later.");
	                	    }
            	        }else {
            	        	// Query the database to check if email and password match
                    	    String queryGuest = "SELECT Email, Password FROM guest WHERE email = ? AND password = ?";
                    	    try {
                    	        prepare = connection.prepareStatement(queryGuest);
                    	        prepare.setString(1, email.getText());
                    	        prepare.setString(2, password.getText());
                    	        result = prepare.executeQuery();
                    	        
                    	        if (result.next()) {
            	        	
            	        	// Load the FXML file of the selected page
            	            root = FXMLLoader.load(getClass().getResource("/application/hotelCoord/HotelCoordPage.fxml"));
            	            scene = new Scene(root);
            	            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            	         // Set the scene in the stage
                	        stage.setScene(scene);
                	        stage.show();
            	        }else {
            	            // Login failed
            	            alert.errorMessage("Incorrect email or password.");
            	        }    
                    	    }catch (SQLException e) {
	                	        e.printStackTrace();
	                	        // Handle database error
	                	        alert.errorMessage("An error occurred. Please try again later.");
	                	    }	        
        	        }     
    		}
    }
    	
    	

    // this method is applied to the sign up link
    public void openSignUpPage (ActionEvent event) throws IOException {
    		 // Load the FXML file of log in page
            root = FXMLLoader.load(getClass().getResource("/application/logIn_signUp/Sign Up Page.fxml"));
            scene = new Scene(root);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene in the stage
            stage.setScene(scene);
            stage.show();	
    }
    
    

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		userChoices();
	}
	
	

	
}

/* ///////// ABSTRACTION    ////////////

//Abstract class for the account
abstract class Account {
	private String username;
	private String password;

 // Constructor
 public Account(String username, String password) {
     this.username = username;
     this.password = password;
 }
}

*/
