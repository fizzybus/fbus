/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;


import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class ClubMemberController implements Initializable {

    @FXML private TextField cust_name_m;
    @FXML private TextField location_m,city_m;
    @FXML private Label membership_status;
    @FXML DatePicker date_membership;
    final private String user= "root";
    final private String pass= "";
    
    @FXML private void Reset() {
        
        cust_name_m.setText("");
        location_m.setText("");
        city_m.setText("");
        membership_status.setText("");
        //date_membership.
    }
    
    @FXML private void Submit() {
        String Address = (String)location_m.getText()+", "+(String)city_m.getText();
        
         try {
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
            
            Statement myStmt = myConn.createStatement();
            String sql = "SELECT * from customer where Name='"+(String)cust_name_m.getText()+"' and address='"+Address+"'";
            ResultSet myRs = myStmt.executeQuery(sql);
            int count = 0;
            while (myRs.next()) { count++; }
            if(count==0) {membership_status.setText("Please verify info");}
            else {
                myRs.previous();
                String Phone = myRs.getString("phone_number");
                sql = "INSERT INTO clubmember (phone_number,Mem_date) " +
                         "VALUES ('"+Phone+"','"+date_membership.getValue()+"')";
                myStmt.executeUpdate(sql);
                membership_status.setText("Membership done !");
            }
        } catch (Exception exc) { membership_status.setText("Registration failed!"); exc.printStackTrace();}  
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
