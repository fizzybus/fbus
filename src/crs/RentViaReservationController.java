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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class RentViaReservationController implements Initializable {
    
    @FXML private Button list_reservations_button;
    @FXML private TextField ds1,ds2,ds3,License_no,Creditcardnumber,confirmation_number;
    @FXML private ComboBox cardtype,phone_confno;
    @FXML private Label message,phone_label;
    @FXML Pane pane_confirmation;
    @FXML DatePicker expirydate;
    private Integer Confno;
    private Integer selection; // Did the user selcted phone or give confirmation number (phone =0) (Confirmation =1)
    private String customer_phone;
    private Boolean valid_confirmation_number;
    final private String user= "root";
    final private String pass= "";
    /**
     * Initializes the controller class.
     */
    
    @FXML public void Reset() {
         if(selection==1) confirmation_number.setText("");
         else {ds1.setText(""); ds2.setText(""); ds3.setText("");}
         License_no.setText("");
        Creditcardnumber.setText("");
    }
    @FXML public void Toggle() {
        if("Phone"==phone_confno.getValue()) {
            pane_confirmation.setVisible(false);
            list_reservations_button.setVisible(true);
            phone_label.setVisible(true);
            selection=0;
        }
        else {
            pane_confirmation.setVisible(true);
            list_reservations_button.setVisible(false);
            phone_label.setVisible(false);
            selection=1;
        }
    }
    
    
    @FXML
    public void LoadListReservations() throws IOException {
        Integer _ds1,_ds2,_ds3;
        Boolean isvalid_phone = true;
        try   {
                _ds1 = Integer.parseInt((String)ds1.getText());
                _ds2 = Integer.parseInt((String)ds2.getText());
                _ds3 = Integer.parseInt((String)ds3.getText());
                message.setText(" ");
        }
        catch (NumberFormatException e) {message.setText("Invalid Phone Number"); isvalid_phone = false;}
        if(isvalid_phone) {
       customer_phone = ds1.getText()+"-"+ds2.getText()+"-"+ds3.getText();
       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(getClass().getResource("ListReservations.fxml"));
       loader.load();
       Parent p = loader.getRoot();
       Stage stage = new Stage();
       stage.setScene(new Scene(p));
       stage.initModality(Modality.APPLICATION_MODAL);
       stage.initOwner(list_reservations_button.getScene().getWindow());
       ListReservationsController controller = loader.getController();
       controller.setInfo(customer_phone);        
       stage.showAndWait();
      Confno = controller.ConfNo();   
      valid_confirmation_number= true;
        }
    }
    
    @FXML 
    public void Proceed() {
        Integer Vlicense;
        Integer Dlicense=0;
        Integer CardNumber=0;
        String Pickup_time,Dropoff_time;
        Integer Odometer;
        
        Boolean valid_Dlicense=true,valid_CardNumber=true;
        /* Vailate License Number */
        try   {Dlicense = Integer.parseInt((String)License_no.getText());message.setText("");}
        catch (NumberFormatException e) {message.setText("Enter valid license");valid_Dlicense=false;}
        
        /* Vailate Credit Card Number */
        try   {CardNumber = Integer.parseInt((String)Creditcardnumber.getText());message.setText("");}
        catch (NumberFormatException e) {message.setText("Enter card number");valid_CardNumber=false;}
        
        if(selection==1) {
            try   {Confno = Integer.parseInt((String)confirmation_number.getText());message.setText(""); valid_confirmation_number=true;}
        catch (NumberFormatException e) {message.setText("Invalid confrimation");valid_confirmation_number=false;}
        }
        
        
        if(valid_Dlicense && valid_CardNumber && valid_confirmation_number)  {
            try {
                Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
                Statement myStmt = myConn.createStatement();
                ResultSet myRs = myStmt.executeQuery("select * from reservation where Confno="+Confno+"");
                myRs.next();
                Vlicense = myRs.getInt("Vlicense");
                if(selection==1) customer_phone = myRs.getString("Phone_number");
                Pickup_time = myRs.getString("Pickup_time");
                Dropoff_time = myRs.getString("Dropoff_time");
                myRs = myStmt.executeQuery("select odometer from vehicle where Vlicense="+Vlicense+"");
                myRs.next();
                Odometer = myRs.getInt("odometer");
                
                String sql = "INSERT INTO rentalagreement (ConfNo,Phone_number,Vlicense,CardNo,ExpiryDate,CardType,Odometer,Pickup_time,Dropoff_time,Dlicense) " +
                         "VALUES ("+Confno+",'"+customer_phone+"',"+Vlicense+","+CardNumber+",'"+expirydate.getValue()+"','"+(String)cardtype.getValue()+"',"+Odometer+",'"+Pickup_time+"','"+Dropoff_time+"',"+Dlicense+")";
                myStmt.executeUpdate(sql);
                message.setText("Rental Agreement completed");
            } 
            catch (Exception exc) 
                {exc.printStackTrace();}  
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        List card_type = new ArrayList();
              card_type.add("American");
              card_type.add("Master");
              card_type.add("Visa");
              
         List confo_phone = new ArrayList();
              confo_phone.add("Phone");
              confo_phone.add("Confirmation");
          
         ObservableList creditcard = FXCollections.observableList(card_type);
         ObservableList rent_phone_confo = FXCollections.observableList(confo_phone);
         cardtype.setItems(creditcard);
         phone_confno.setItems(rent_phone_confo);
         cardtype.setValue("Master");
         phone_confno.setValue("Phone");
         pane_confirmation.setVisible(false);
         selection =0;
    }    
    
}
