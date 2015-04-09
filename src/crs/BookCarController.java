/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

// Test 
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.util.ResourceBundle;
import javafx.scene.control.ComboBox;
import java.util.List;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

//import javafx.scene.layout.Pane;
//import javafx.scene.shape.Rectangle;




//import static javafx.collections.FXCollections.observableList;
/**
 * FXML Controller class
 *
 * @author User
 */
public class BookCarController implements Initializable {
    
    @FXML private ComboBox location,vehicletype,togglecombo,equipment;
    @FXML private ComboBox h1,h2,m1,m2,s1,s2,cardtype; 
    @FXML private Label location_label,date_label,estimated_cost;
    @FXML private DatePicker pickup,dropoff,card_expiry;
    @FXML private TextField license,creditcardnumber,digset1,digset2,digset3,customername;
    @FXML private Pane pane,content_pane,pane_validate;
    @FXML private Button pop_confirmation_button,list_vehicle_button;
    
    @FXML private Label pop_customername,pop_customerphone,customer_indatabase;
    @FXML private Label pop_location,pop_pickup,pop_dropoff;
    @FXML private Label pop_vehiclename,pop_vehiclelicense;
    @FXML private Label pop_cost,pop_equipment,pop_confirmation,message_custinfo;
    
    final private String user= "root";
    final private String pass= "";
    private int Vehicle_ID;
    private int Branch_ID,odometer;
    private int trans_status;  // Rent or Reserve (Reserve = 0 Rent = 1)
    private String V_Type,V_Category,V_Name,Date1,Date2,customer_phone;
    private float week,day,hour,day_raw;
    
    private Boolean isValidCustomer = false;
    private Boolean isValidVehicle = false;
    private Boolean isValidTime = false;
    private Boolean isOKprice = false;
    
    @FXML
    private void IsCustomerValid() {
        Boolean isValidPhone = true;
        Integer _ds1,_ds2,_ds3;
            
            try   {
                    _ds1 = Integer.parseInt((String)digset1.getText());
                    _ds2 = Integer.parseInt((String)digset2.getText());
                    _ds3 = Integer.parseInt((String)digset3.getText());
                    customer_indatabase.setText(" ");
                    //isValidCustomer  = true;
            }
            catch (NumberFormatException e) {customer_indatabase.setText("Invalid Phone Number"); isValidPhone  = false;}
        
            if(isValidPhone ) {
                    try {
                      Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
                      Statement myStmt = myConn.createStatement();
                      customer_phone = (String)digset1.getText()+"-"+(String)digset2.getText()+"-"+(String)digset3.getText();
                      System.out.println("select * from customer where Name='"+(String)customername.getText()+"' and phone_number='"+customer_phone+"'");
                      ResultSet myRs = myStmt.executeQuery("select * from customer where Name='"+(String)customername.getText()+"' and phone_number='"+(String)digset1.getText()+"-"+(String)digset2.getText()+"-"+(String)digset3.getText()+"'");
                      int count = 0;
                      while (myRs.next()) { count++; }

                      if(count==1) {
                          customer_indatabase.setText("Customer Exists");
                          isValidCustomer = true;
                      }
                      else
                          customer_indatabase.setText("No such customer");


                  } catch (Exception exc) {exc.printStackTrace();}  
            }
        
    }
    
    @FXML
    private void Reset() {
        customername.setText(" ");
        digset1.setText(" ");
        digset2.setText(" ");
        digset3.setText(" ");
        if(trans_status==1) {
            license.setText(" ");
            creditcardnumber.setText(" ");
        }
        customer_indatabase.setText(" ");
        message_custinfo.setText(" ");
        location_label.setText(" ");
        date_label.setText(" ");
        estimated_cost.setText(" ");
    }
    
    @FXML
    private void Toggle(ActionEvent event) {
        
        if( togglecombo.getValue()=="Reservation") { 
            license.setVisible(false);
            creditcardnumber.setVisible(false);
            cardtype.setVisible(false);
            card_expiry.setVisible(false);
            pane_validate.setTranslateY(0);
            trans_status = 0;   
            System.out.println("The trams_status :"+trans_status );
        }
        else {   
            license.setVisible(true);
            creditcardnumber.setVisible(true);
            cardtype.setVisible(true);
            card_expiry.setVisible(true);
            trans_status = 1;
             System.out.println("The trams_status :"+trans_status );
            pane_validate.setTranslateY(93);      
        }            
    }
    
    @FXML 
    private void IsAvailableTimePeriod(ActionEvent event) throws ParseException{
        
        if( (null== pickup.getValue()) || (null==h1.getValue()) || (null==m1.getValue()) || (null== dropoff.getValue()) || (null==h2.getValue()) || (null==m2.getValue())  )
        { date_label.setText("Pickup or Droff Date & Time invalid"); }
        else {
                Date1 = pickup.getValue() + " "+h1.getValue()+":"+m1.getValue()+":00";
                Date2 = dropoff.getValue() + " "+h2.getValue()+":"+m2.getValue()+":00";
                if(isValidVehicle) {
                            try {
                                    Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
                                    Statement myStmt1 = myConn.createStatement();
                                    Statement myStmt2 = myConn.createStatement();

                                    ResultSet myRs1 = myStmt1.executeQuery("select * from reservation where `Vlicense`="+Vehicle_ID+"  and " +
                                                                           "`Dropoff_time` > '"+Date1+"' and `Pickup_time` < '"+Date2+"'");
                                    ResultSet myRs2 = myStmt2.executeQuery("select * from rentalagreement where `Vlicense`="+Vehicle_ID+"  and " +
                                                                           "`Dropoff_time` > '"+Date1+"' and `Pickup_time` < '"+Date2+"'");
                                    int count1=0,count2 = 0;
                                    while (myRs1.next()) { count1++; }
                                    while (myRs2.next()) { count2++; }
                                    if(  (count1>0) || (count2>0) ){
                                         date_label.setText("Unavailable");
                                    }
                                     else {
                                        date_label.setText("Available");  
                                        isValidTime=true;
                                        HashMap<String, Long> map = TotalTime(Date1,Date2);
                                        week = map.get("week");
                                        day  = map.get("day");
                                        hour = map.get("hour");
                                        day_raw = map.get("day_raw");
                                    }   
                            } catch (Exception exc) {exc.printStackTrace();}     
                } else {date_label.setText("Please select vehicle");}
        }       
    }
    
   public HashMap<String,Long> TotalTime(String Date1,String Date2)  throws ParseException{
       
       final HashMap<String, Long> map = new HashMap<String, Long>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 
	Date d1 = null;
	Date d2 = null;
        
        d1 = format.parse(Date1);
        d2 = format.parse(Date2);
	long diff = d2.getTime() - d1.getTime();

        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        long diffWeek = diffDays/7;
        long diffdays_raw = diffDays ;

        if( diffWeek >= (long)1) 
            diffDays = diffDays%7;
        else 
            diffWeek = 0;

        map.put("week",diffWeek);
        map.put("day",diffDays);
        map.put("hour",diffHours);
        map.put("minute",diffMinutes);
        map.put("day_raw",diffdays_raw);
       
       return map;
   }
   
   
   @FXML 
   private void Load_Equipment( ) {
       
        List<String> e_list = new ArrayList<String>();
        try {
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("select equipmentName from additional_equipment where vehicleCategory='"+V_Category+"'");
            e_list.add("None");
            while (myRs.next()) { e_list.add(myRs.getString("equipmentName")); }  
        } catch (Exception exc) {exc.printStackTrace();}  
        
        ObservableList eqp_list = FXCollections.observableList(e_list);
        equipment.setItems(eqp_list);
        equipment.setValue("None");
       
       
   }
   
  
   
   @FXML
   private void ListVehicles() throws IOException {
   
       if( (null==location.getValue()) || (null==vehicletype.getValue()) )
           location_label.setText("Select Location & Category");
       else {
           location_label.setText(" ");
       try {
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
            Statement myStmt = myConn.createStatement();
             ResultSet myRs = myStmt.executeQuery("select BranchID from branch where location='"+(String)location.getValue() +"'");
            
            myRs.next();  Branch_ID =  myRs.getInt("BranchID");
        } catch (Exception exc) {exc.printStackTrace();}  
       
       Mediator0 Info = new Mediator0((String)vehicletype.getValue(),Branch_ID);
       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(getClass().getResource("ListVehicles.fxml"));
       loader.load();
       Parent p = loader.getRoot();
       Stage stage = new Stage();
       stage.setScene(new Scene(p));
       stage.initModality(Modality.APPLICATION_MODAL);
       stage.initOwner(list_vehicle_button.getScene().getWindow());
       ListVehiclesController controller = loader.getController();
        controller.setInfo(Info);
           
       stage.showAndWait();
       
       try { Vehicle_ID = controller.V_ID(); isValidVehicle=true;}
       catch (NullPointerException | NumberFormatException e) {location_label.setText("Please select vehicle"); isValidVehicle=false;}
       
       if(isValidVehicle) {
       
                try {
                     Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
                     Statement myStmt = myConn.createStatement();
                      ResultSet myRs = myStmt.executeQuery("select category,Vtype_name,Vname,odometer from vehicle where Vlicense="+Vehicle_ID+"");

                     myRs.next();  odometer = myRs.getInt("odometer");  V_Name = myRs.getString("Vname"); V_Category =  myRs.getString("category");  V_Type = myRs.getString("Vtype_name");
                 } catch (Exception exc) {exc.printStackTrace();}  

                Load_Equipment();
       
       }
       
       }
   }
   
   @FXML
   private void Load_Confirmation(ActionEvent event)throws IOException {
       
       /*
             private Boolean isValidCustomer = false;
    private Boolean isValidVehicle = false;
    private Boolean isValidTime = false;
    private Boolean isOKprice = false;
               
        */     
       Boolean _License=true,_cardtype=true,_creditcardnumber=true,_card_expiry=true;
       if(trans_status==1) {
           try {int _ds1 = Integer.parseInt(license.getText()); } catch (NumberFormatException e) {message_custinfo.setText("Invalid License"); _License=false;}
           try {String _ds1 = (String)cardtype.getValue(); } catch (NullPointerException e) {message_custinfo.setText("Select Card Type"); _cardtype=false;}      
           try {int _ds2 = Integer.parseInt(creditcardnumber.getText()); } catch (NumberFormatException e) {message_custinfo.setText("Invalid Credit Card Number"); _License=false;}
           if(null==card_expiry.getValue()) {message_custinfo.setText("Select Card Expiry"); _card_expiry=false;}      

       }
       
       String Equip= (String)equipment.getValue();
       if("None"== ((String)equipment.getValue()))
       {  Equip = null; }
       else {
           String Tick = "'";
           Equip = Tick+Equip+Tick;
       }
       
       if(_License && _cardtype && _creditcardnumber && _card_expiry && isValidCustomer && isValidVehicle && isValidTime && isOKprice) {
       Integer latest_entry_number=0;
       
       
       
       try {
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
            Statement myStmt = myConn.createStatement();
            ResultSet myRs;
            String sql = "";
            if(trans_status==0) {
                 sql = "INSERT INTO Reservation (Phone_number,Vtype_name,Vlicense,BranchID,Pickup_time,Dropoff_time,Equipment) " +
                         "VALUES ('"+customer_phone+"','"  + (String)vehicletype.getValue()+ "',"+Vehicle_ID+","+Branch_ID+",'"+ Date1+"','"+Date2+"',"+Equip+")";
            System.out.println(sql);
            }
            else {
               System.out.println(sql);
                sql = "INSERT INTO Rentalagreement (Phone_number,Vlicense,CardNo,ExpiryDate,CardType,Odometer,Pickup_time,Dropoff_time,Equipment) " +
                         "VALUES ('"+customer_phone+"',"+Vehicle_ID+","+Integer.parseInt((String)creditcardnumber.getText())+",'"+card_expiry.getValue()+"','"+(String)cardtype.getValue()+"',"+odometer+",'"+Date1+"','"+Date2+"',"+Equip +" )";
               System.out.println(sql); 
            }
            
            myStmt.executeUpdate(sql);
            if(trans_status==0)
                myRs = myStmt.executeQuery("select MAX(Confno) AS Latest_Entry FROM reservation");
            else
                myRs = myStmt.executeQuery("select MAX(RentId) AS Latest_Entry FROM rentalagreement");
            
            myRs.next();
            latest_entry_number = myRs.getInt("Latest_Entry");
        } catch (Exception exc) {exc.printStackTrace();}  
       
       
       Mediator1 Info = new Mediator1((String)customername.getText(),customer_phone,V_Name,Vehicle_ID,V_Type,trans_status,(String)location.getValue(),Date1,Date2,(String)equipment.getValue(),estimated_cost.getText(),latest_entry_number);
       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(getClass().getResource("Confirmation.fxml"));
       loader.load();
       Parent p = loader.getRoot();
       Stage stage = new Stage();
       stage.setScene(new Scene(p));
       stage.initModality(Modality.APPLICATION_MODAL);
       stage.initOwner(pop_confirmation_button.getScene().getWindow());
       ConfirmationController controller = loader.getController();
       controller.setInfo(Info);
           
       stage.showAndWait();
        
       }
       else {
           if(!isValidCustomer) customer_indatabase.setText("Please validate customer");
           if(!isValidVehicle) location_label.setText("Select vehicle");
           if(!isValidTime) date_label.setText("Validate vehicle timeperiod");
           if(!isOKprice) estimated_cost.setText("Cost estimate missing");
       }
   }
   
   @FXML
   private void CalculateEstimatedCost(ActionEvent event) throws SQLException{
       
       
    
       if(isValidTime  && isValidVehicle  ) {
       Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
       Statement myStmt = myConn.createStatement();
       ResultSet myRs = myStmt.executeQuery("select * from vehicletype where vtype_name='"+V_Type +"'");
       myRs.next();
       System.out.println("Week :"+week+ " Day :"+day+" Hour :"+hour);
       float price1 = day*(myRs.getFloat("daily_rate")+myRs.getFloat("ins_drate"))  +  
                     week*(myRs.getFloat("weekly_rate")+myRs.getFloat("ins_wrate"))  + 
                     hour*(myRs.getFloat("hourly_rate")+myRs.getFloat("ins_hrate"))  ;
       float price2 = 0.00f;
       if(equipment.getValue()!="None") {
           System.out.println("select * from additional_equipment where equipmentName='"+(String)equipment.getValue()+"'");
           myRs = myStmt.executeQuery("select * from additional_equipment where equipmentName='"+(String)equipment.getValue()+"'");
           myRs.next();
        //   System.out.println("Daily Rate "+myRs1.getFloat("daily_rate"));
        //   System.out.println("Hourly Rate "+myRs1.getFloat("hourly_rate"));
           price2 = day_raw*(myRs.getFloat("daily_rate")) + hour*(myRs.getFloat("hourly_rate"));    
       }
       estimated_cost.setText(String.format("%.2f",price1+price2)+"  CAD"); 
       isOKprice=true;
       }
       else
           estimated_cost.setText("Time-Period or Vehicle Invalid");
   }
            
    @FXML
    private void IsAvailableLocation(ActionEvent event) {
         String VehicleType = (String) vehicletype.getValue();
         try {
                Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
                Statement myStmt = myConn.createStatement();
                ResultSet myRs = myStmt.executeQuery("select BranchID from branch where location='"+(String)location.getValue() +"'");
                myRs.next();
                int BranchID =  myRs.getInt("BranchID");
                myRs =  myStmt.executeQuery("Select * from vehicle where BranchID="+BranchID+" and Vtype_name='"+VehicleType+"' and Status=1 order by Vlicense LIMIT 1" );
                int count = 0;
                while (myRs.next()) { count++; }
                if(count==1){
                    myRs.previous();
                    location_label.setText("Available");
                    Vehicle_ID = myRs.getInt("Vlicense");
                    Branch_ID  = myRs.getInt("BranchID");
                    V_Type     = myRs.getString("Vtype_name");
                    V_Category = myRs.getString("category");
                    V_Name     = myRs.getString("Vname");
                    odometer   = myRs.getInt("odometer");
                }
                else
                   location_label.setText("Unavailable");           
             } catch (Exception exc) {exc.printStackTrace();}     
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
       license.setVisible(false);
        creditcardnumber.setVisible(false);
        cardtype.setVisible(false);
        card_expiry.setVisible(false);
 
        trans_status = 0;
        List<String> list = new ArrayList<String>();
        List<String> listvehicletype = new ArrayList<String>();
        try {
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("select location from branch");
            while (myRs.next()) { list.add(myRs.getString("location")); }  
            myRs = myStmt.executeQuery("select vtype_name from vehicletype");
            while (myRs.next()) { listvehicletype.add(myRs.getString("vtype_name")); }  
        } catch (Exception exc) {exc.printStackTrace();}  
         
        ObservableList<String> observableList = FXCollections.observableList(list);
        location.setItems(observableList);

         ObservableList<String> observableVehicleList = FXCollections.observableList(listvehicletype);
         vehicletype.setItems(observableVehicleList);
        
         List hour1= new ArrayList();
         List hour2= new ArrayList();
         List minute1= new ArrayList();
         List minute2= new ArrayList();
     //    List second1 = new ArrayList();
     //    List second2 = new ArrayList();
         List card_type = new ArrayList();
              card_type.add("American");
              card_type.add("Master");
              card_type.add("Visa");
         List enquiry = new ArrayList();
              enquiry.add("Reservation");
              enquiry.add("Rent");
         for(int i=0;i<24;i++) {  
             hour1.add(String.format("%02d",i)); 
             hour2.add(String.format("%02d",i));
         }
         for(int i=0;i<60;i++) { 
             minute1.add(String.format("%02d",i)); 
           //  second1.add(String.format("%02d", i)); 
             minute2.add(String.format("%02d",i)); 
           //  second2.add(String.format("%02d", i)); 
         }
        ObservableList hours1 = FXCollections.observableList(hour1);
        ObservableList minutes1= FXCollections.observableList(minute1);
    //    ObservableList seconds1 = FXCollections.observableList(second1);
        ObservableList hours2 = FXCollections.observableList(hour2);
        ObservableList minutes2= FXCollections.observableList(minute2);
    //    ObservableList seconds2 = FXCollections.observableList(second2);
        ObservableList creditcard = FXCollections.observableList(card_type);
        ObservableList enq_type = FXCollections.observableList(enquiry);
        h1.setItems(hours1);
        m1.setItems(minutes1);
//        s1.setItems(seconds1);
        h2.setItems(hours2);
        m2.setItems(minutes2);
 //       s2.setItems(seconds2);
        togglecombo.setItems(enq_type);
        cardtype.setItems(creditcard);   
        
        
        
    }
    
    
  
    
    
    
    
    
}
