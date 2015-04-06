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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class ProcessReturnController implements Initializable {

    @FXML private ComboBox choice_rental_id,hh,mm;
    @FXML private Pane content_processreturn_section1;
    @FXML private Button button_rid;
    @FXML private TextField rental_id,ds1,ds2,ds3;
    @FXML private Label message1;
    @FXML private DatePicker from,to;
    private Integer choice;
    private Integer Odometer,Vlicense;
    private String Pickup_DateTime,customer_phone;
    final private String user= "root";
    final private String pass= "";
    
    
     
   @FXML public void Toggle() {
       if("Phone & Dates"==choice_rental_id.getValue()) {
            content_processreturn_section1.setTranslateY(-35); 
            content_processreturn_section1.setVisible(true);
            rental_id.setVisible(false);
            button_rid.setTranslateX(-60);
            button_rid.setTranslateY(140); 
            choice = 1;
        }
       else { 
           button_rid.setTranslateY(0);
           rental_id.setVisible(true);
           content_processreturn_section1.setTranslateY(0); 
           button_rid.setTranslateX(0);
           content_processreturn_section1.setVisible(false);
          choice = 0;
       }
   }
    
   @FXML public void LocateRecord() throws SQLException {
       
       Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
       Statement myStmt = myConn.createStatement();
       ResultSet rs;
       
       if(choice==1) {
           
           Integer _ds1,_ds2,_ds3;
            Boolean isvalid_phone = true;
            try   {
                    _ds1 = Integer.parseInt((String)ds1.getText());
                    _ds2 = Integer.parseInt((String)ds2.getText());
                    _ds3 = Integer.parseInt((String)ds3.getText());
                    message1.setText(" ");
            }
            catch (NumberFormatException e) {message1.setText("Invalid Phone Number"); isvalid_phone = false;}
            
            if(isvalid_phone) { 
                
                customer_phone = ds1.getText()+"-"+ds2.getText()+"-"+ds3.getText();  
                String sql = "Select * from rentalagreement WHERE Phone_number='"+customer_phone+"' and DATE(Pickup_time)='"+from.getValue() +"' and DATE(Dropoff_time)='"+to.getValue() +"'";
                System.out.println(sql);
                rs = myStmt.executeQuery(sql);
                int count=0;
                while(rs.next()){
                    count++;
                    Odometer        = rs.getInt("Odometer");
                    Pickup_DateTime = rs.getString("Pickup_time");
                    Vlicense        = rs.getInt("Vlicense");
                }
                if(count==1) message1.setText("Record Located");
                else message1.setText("No record found");
            }
       }
       else {
                Integer rentalid=0;
                Boolean isvalid_confirmation = true;
           
                try {
                        rentalid = Integer.parseInt((String)rental_id.getText());
                        message1.setText(" ");
                }
                catch (NumberFormatException e) {message1.setText("Invalid Confirmation number"); isvalid_confirmation = false;}
                
                if(isvalid_confirmation)  {
                    String sql = "SELECT * from rentalagreement WHERE RentId="+rentalid+"";
                    System.out.println(sql);
                        rs = myStmt.executeQuery(sql);
                        int count=0;
                        while(rs.next()){
                            count++;
                            Odometer        = rs.getInt("Odometer");
                            Pickup_DateTime = rs.getString("Pickup_time");
                            Vlicense        = rs.getInt("Vlicense");
                        }
                        if(count==1) message1.setText("Record Located");
                        else message1.setText("No record or multiple records");
                }      
       }         
   }
   
   
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        content_processreturn_section1.setVisible(false);
        
        List hour= new ArrayList();
        List minute= new ArrayList();
        
         for(int i=0;i<24;i++) {  
             hour.add(String.format("%02d",i)); 
         }
         for(int i=0;i<60;i++) { 
             minute.add(String.format("%02d",i));       
         }
         
        ObservableList hours = FXCollections.observableList(hour);
        ObservableList minutes= FXCollections.observableList(minute);
        
        hh.setItems(hours);
        mm.setItems(minutes);
        
        List _choice = new ArrayList();
             _choice.add("Phone & Dates");
             _choice.add("Rental Id");
              
         ObservableList RID = FXCollections.observableList(_choice);
         choice_rental_id.setItems(RID);
         choice_rental_id.setValue("Rental Id");      
         choice =0;  
              
    }    
    
}
