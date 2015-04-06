/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class RegisterCustomerController implements Initializable {

    @FXML private TextField cust_name,location,city;
    @FXML private TextField ds1,ds2,ds3;
    @FXML private ComboBox roadstar;
    @FXML private Label registration_status;
    private Integer road_star;
    final private String user= "root";
    final private String pass= "";
    
    @FXML private void ToggleRoadStar() {
        if("Yes"==roadstar.getValue())
            road_star=1;
        else
            road_star=0;
    }
    
     @FXML private void Reset() {
        road_star = 0;
        roadstar.setValue("No");
        cust_name.setText("");
        location.setText("");
        city.setText("");
        ds1.setText("");
        ds2.setText("");
        ds3.setText("");
        registration_status.setText("");
        
    }
    
    @FXML private void Submit() {
        String Address = (String)location.getText()+", "+(String)city.getText();
        String phone =(String)ds1.getText() +"-"+ (String)ds2.getText() +"-"+ (String)ds3.getText();
         try {
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
            
            Statement myStmt = myConn.createStatement();
            String sql = "INSERT INTO customer (phone_number,Name,address,roadstar) " +
                         "VALUES ('"+phone+"','"  + (String)cust_name.getText()+ "','"+Address+"',"+road_star+")";
            myStmt.executeUpdate(sql);
            registration_status.setText("Customer added !");
        } catch (Exception exc) { registration_status.setText("Registration failed!"); exc.printStackTrace();}  
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        road_star=0;
        List  rs = new ArrayList();
              rs.add("Yes");
              rs.add("No");
        ObservableList RS = FXCollections.observableList(rs);
        roadstar.setItems(RS);
        roadstar.setValue("No");
        
              
    }    
    
}
