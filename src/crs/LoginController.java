/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;
import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.sql.*;
import javafx.scene.control.TextField;

/**
 *
 * @author User
 */
public class LoginController implements Initializable {
    
    @FXML
    private Label fail_login_message;
    @FXML
    private TextField username1;
    @FXML
    private TextField password1;
    final private String user= "root";
    final private String pass= "";
    
    @FXML
    private void goToScreen2(ActionEvent event)throws IOException  {
       
        if(true /* ValidLogin() */) // Commented for testing
            change_scene("menu.fxml",event);
        else
          fail_login_message.setText(" Login Failed, please try again !");
    }
    
    @FXML
    private void goToScreen3(ActionEvent event) {
        System.out.println("You clicked me!");
        //label.setText("Hello World!");
    }
    public boolean ValidLogin() {
        boolean valid = true;
        try {
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("select * from employees where username='"+  username1.getText()+"' and password='"+password1.getText()+"'");
            int count = 0;
            while (myRs.next()) { count++; }
            if(count==0) valid = false;
        } catch (Exception exc) {
            exc.printStackTrace();
        }  
        return valid;
    }
    public void change_scene(String fxml,ActionEvent event) throws IOException {
        Parent clerk =  FXMLLoader.load(getClass().getResource(fxml));
        Scene home_page_scene = new Scene(clerk);
        Stage app_stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        app_stage.setScene(home_page_scene);
        app_stage.show(); 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
