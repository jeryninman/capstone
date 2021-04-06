/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capstone;

import Model.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jeryn
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // create user objects, load them into User array
        users = generateUsers();
        
    }    
    
    private ObservableList<User> users = FXCollections.observableArrayList();
    
    //Controls
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    
    //methods
    @FXML
    private void login(){
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        User currentUser = new User(username, password);
        Boolean successfulLogin = false;
        for (User user : users){
            if (user.getUsername().equals(username) && user.getPassword().equals(password)){
                successfulLogin = true;
                //log login
                String login  = currentUser.getUsername() + " User has logged in.";
                DashboardController.log(login);
        
            }
        }
        if (!successfulLogin){
            Alert a = new Alert(AlertType.WARNING);
            a.setContentText("Login failed, invalid username / password combination");
            a.show();
            DashboardController.log("Failed Log in attempt for user: " + username);
        }
        else {
            try{
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                DashboardController controller = new DashboardController(currentUser);
                loader.setController(controller);
                Parent root = loader.load();
                Scene modify = new Scene(root);
                Stage primaryStage = (Stage)txtUsername.getScene().getWindow();
                primaryStage.setScene(modify);
                primaryStage.setResizable(false);
                primaryStage.show();
            }catch (Exception e){
                DashboardController.log("Failed Dashboard load");
                System.out.print("Failed dashboard load");
                e.printStackTrace();
            }
        }
        
    }
    private ObservableList<User> generateUsers(){
        String userFile = "src/Model/createdusers.txt";
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        String user;
        String pass;
        String line;
        
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))){
            //skip header
            br.readLine();
            
            while ((line = br.readLine()) != null) {
                String[] login = line.split(" ");
                user = login[0];
                pass = login[1];
                User newUser = new User(user, pass);
                allUsers.add(newUser);
                
            }
        }catch (Exception e){
            DashboardController.log("Failed load of users");
        }
        return allUsers;
    }
}
