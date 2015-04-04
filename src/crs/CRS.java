/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import crs.classes.Branch;
import crs.classes.Price;
import crs.classes.User;
import crs.classes.Vehicle;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author User
 */
public class CRS extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("menu_clerk.fxml"));  
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/inc/screen1.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static DBConn dbc = new DBConn();
    
    /*hannah*/
    public static ArrayList<Branch> getLocationList() {
        
        ArrayList<Branch> result = new ArrayList();
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        data = dbc.getResult("SELECT * FROM Branch");
        for( HashMap<String, String> record : data ){
            Branch branch = new Branch();
            branch.BranchID = Integer.parseInt( record.get("branchid") ) ;
            branch.location = record.get("location");
            branch.City = record.get("city");
            result.add(branch);
            
        }
        return result;
    
    
    }
    
    
    
    /*hannah*/
    public static ArrayList<String> getCategoryList(String category) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        data = dbc.getResult("SELECT DISTINCT Vtype_name  FROM Vehicle WHERE category='"+category+"'");
        //data = dbc.getResult("SELECT Type FROM price WHERE VType = '"+vtype+"'");
        for( HashMap<String, String> record : data ){
            String str = new String(); 
            str = record.get("vtype_name");           
            result.add(str);
            
        }
        return result;
    
    }
    
     /*  Manage vehicles  */
    
      /*****vicky**********/
    public static ArrayList<Vehicle> getVehicleListForRentOrSell(int saleFlag){
        
        ArrayList<Vehicle> result = new ArrayList<Vehicle>();
        
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        data = dbc.getResult("SELECT * FROM Vehicle WHERE forRentFlag = "+saleFlag +" ORDER BY vtype_name,category,branchid");
        for( HashMap<String, String> record : data ){
            Vehicle vc = new Vehicle(); 
            vc.Vlicense = Integer.parseInt( record.get("vlicense") ) ;
            vc.Vname = record.get("vname");
            vc.BranchID = Integer.parseInt( record.get("branchid"));
            vc.Vtype_name =record.get("category");
            vc.category =record.get("vtype_name"); 
            vc.Year = Integer.parseInt( record.get("year"));
            vc.initial_price = Integer.parseInt( record.get("initial_price"));
            vc.forRentFlag = Integer.parseInt( record.get("forrentflag"));
            vc.status = Integer.parseInt( record.get("status"));
            vc.Branch_location = getBranchLocationById(vc.BranchID);
            vc.selling_price = getSellingPrice(vc.Vlicense);
            result.add(vc);          
        }
       
        return result;  
    }
    
    public static double getSellingPrice(int vlicense){
        double price = 0;
    
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        data = dbc.getResult("SELECT saleprice FROM forsalevehicles WHERE Vlicense="+vlicense);
        for( HashMap<String, String> record : data ){
            price = Double.parseDouble(  record.get("saleprice") ) ;
        }
        return price;
    
    }
        
    
    
    /*****vicky**********/
    public static Vehicle getVehicleByID(String license_id){
        
        Vehicle vc = new Vehicle();    
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        data = dbc.getResult("SELECT * FROM Vehicle WHERE Vlicense="+license_id);
        for( HashMap<String, String> record : data ){
            vc.Vlicense = Integer.parseInt( record.get("vlicense") ) ;
            vc.Vname = record.get("vname");
            vc.BranchID = Integer.parseInt( record.get("branchid"));
            vc.Vtype_name =record.get("category");
            vc.category = record.get("vtype_name");
            vc.Year = Integer.parseInt( record.get("year"));
            vc.initial_price = Integer.parseInt( record.get("initial_price"));
            vc.forRentFlag = Integer.parseInt( record.get("forrentflag"));
            vc.status = Integer.parseInt( record.get("status"));
            vc.Branch_location = getBranchLocationById(vc.BranchID);          
        }
        return vc;
    }
    

 
 
    
    /*****hannah**********/
    public static ArrayList<Vehicle> getVehicleListByCLY(String vtypename,int locationid,int year){
        
        ArrayList<Vehicle> result = new ArrayList<Vehicle>();
        
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        String sql = "SELECT * FROM Vehicle WHERE forRentFlag=1 AND Year<"+year+" ";
        if( vtypename=="" && locationid<=0 ){
            //both not set
            System.out.println("only year set year:"+year);
        }else if(  vtypename!="" && locationid>0   ){
            //both set
            sql += " AND BranchID="+locationid+" AND vtype_name='"+vtypename+"'";
        }else if(  vtypename=="" && locationid>0   ){
            //only location set
            sql += " AND BranchID="+locationid;
        }else if(  vtypename!="" && locationid<=0   ){
            //only category set
            sql += " AND Vtype_name='"+vtypename+"'";            
        }
        sql += "  ORDER BY vtype_name,category,branchid";
        data = dbc.getResult(sql);
        for( HashMap<String, String> record : data ){
            Vehicle vc = new Vehicle(); 
            vc.Vlicense = Integer.parseInt( record.get("vlicense") ) ;
            vc.Vname = record.get("vname");       
            vc.Vtype_name = record.get("category");
            vc.category = record.get("vtype_name");
            vc.initial_price = Double.parseDouble( record.get("initial_price") );
            vc.BranchID = Integer.parseInt( record.get("branchid"));    
            vc.status = Integer.parseInt( record.get("status"));
            vc.Year = Integer.parseInt( record.get("year"));
            vc.forRentFlag = Integer.parseInt( record.get("forrentflag")); 
            vc.Branch_location = getBranchLocationById(vc.BranchID);
            result.add(vc);
            
        }
        
        
        return result;  
    
    }
        /**hannah**/
    public static String getBranchLocationById(int bid){
            ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
            data = dbc.getResult("SELECT location FROM branch WHERE BranchID = "+bid);
            String location="";
            for( HashMap<String, String> record : data ){
                location = record.get("location")  ;           
                //result.add(str);   
            }
        return location;  
    
    
    }
    
 
    
     /*****vicky**********/
       public static int getBranchid(String branch_address) {
        
            //ArrayList<String> result = new ArrayList<String>();
            ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
            data = dbc.getResult("SELECT BranchID FROM Branch WHERE location = '"+branch_address+"'");
            int BranchID = -1;
            for( HashMap<String, String> record : data ){
                BranchID = Integer.parseInt( record.get("branchid") ) ;           
                //result.add(str);   
            }
        return BranchID;  
        }
       
       
       

    /*****vicky**********/
       public static int addNewVehicle(Vehicle vc) {
           ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
            data = dbc.getResult("SELECT COUNT(*) AS number FROM vehicle WHERE vlicense = "+vc.Vlicense);
            int count = -1;
            for( HashMap<String, String> record : data ){
                count = Integer.parseInt( record.get("number") ) ;        
                
            }
            if(count==1) return -2;
            
            count = dbc.itemInsert("INSERT INTO Vehicle (BranchID,Vlicense,Vname,Vtype_name,category,Year,initial_price,status,forRentFlag ) VALUES ("
                +vc.BranchID+","+vc.Vlicense+",'"+vc.Vname+"','"+vc.category +"','"+vc.Vtype_name+"',"+vc.Year+","+vc.initial_price+",1,1)"); 
            return count;
       }

    /***hannah**/
       public static int setToSell(int VID,double price){
            int count = dbc.itemUpdate("UPDATE Vehicle SET forRentFlag = 0,STATUS=0 WHERE Vlicense = "+VID); 
            count+= dbc.itemInsert("INSERT INTO `forsalevehicles`(Vlicense,SalePrice,SoldFlag) VALUES("+VID+","+price+",0)");
            return count;
        }
       
       /**hannah****/
        public static int setSold(int VID,String date,double price,String soldTo) {
        //delete this vehicle from table
            int count = dbc.itemUpdate("UPDATE Forsalevehicles SET SoldFlag=1,SoldPrice="+price+",SoldDate='"+date+"',SoldTo='"+soldTo+"' WHERE Vlicense="+VID);       
            count += dbc.itemUpdate("UPDATE `vehicle` SET forRentFlag=2 WHERE Vlicense="+VID);
            
            return  count;
        }
       
 
       
    
     /*****vicky**********/
       public static Price getPrice(String type){
       //getprice and return
            Price price = new Price();  
            ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
            data = dbc.getResult("SELECT * FROM Vehicletype WHERE vtype_name='"+type+"'");
            for( HashMap<String, String> record : data ){
                price.vtype_name = record.get("vtype_name");
                price.daily_rate = Integer.parseInt( record.get("daily_rate") ) ;   
                price.weekly_rate = Integer.parseInt( record.get("weekly_rate") ) ; 
                price.hourly_rate = Integer.parseInt( record.get("hourly_rate") ) ; 
                price.km_rate = Integer.parseInt( record.get("km_rate") ) ; 
                price.ins_wrate = Integer.parseInt( record.get("ins_wrate") ) ; 
                price.ins_drate = Integer.parseInt( record.get("ins_drate") ) ; 
                price.ins_hrate = Integer.parseInt( record.get("ins_hrate") ) ;
            }
        //return vc;  
           return price;
       }
       
       
    /*****vicky**********/
    public static int setPrice(Price price){
           //update price of a type
          int count =  dbc.itemUpdate("UPDATE Vehicletype SET vtype_name ='"+price.vtype_name+"'"+
                   ",daily_rate="+price.daily_rate+
                   ",weekly_rate="+price.weekly_rate+
                   ",hourly_rate="+price.hourly_rate+
                   ",km_rate="+price.km_rate+
                   ",ins_wrate="+price.ins_wrate+
                   ",ins_drate="+price.ins_drate+
                   ",ins_hrate="+price.ins_hrate+" WHERE vtype_name = '"+price.vtype_name+"'");
          return count;
          
       }

    
    /* manage users************************/
    
    /*****vicky**********/
    public static int addUser(User user){
        return 0;
        //to finish
    }
    
    /*****vicky**********/
    public static ArrayList<User> getUserList(String type){
        return null;
        //to finish
    }
    
    
    /*****vicky**********/
    public static int updateUser(User user){
        return 0;
    
    
    //to finish
    }
    
   public static ArrayList<Vehicle> getDailyRental(String vtypename,int locationid,int year){
        
        ArrayList<Vehicle> result = new ArrayList<Vehicle>();
        
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        String sql = "SELECT branchid,COUNT(*) FROM `rentalagreement` A,`vehicle` B WHERE A.`Vlicense` = B.`Vlicense`  AND DATE(Pickup_time)='2015-01-02'   GROUP BY branchid,vtype_name";
            String sql2 = "SELECT A.`Vlicense`vlicense,branchid,vtype_name,DATE(Pickup_time) FROM `rentalagreement` A,`vehicle` B  WHERE A.`Vlicense` = B.`Vlicense`  AND DATE(Pickup_time)='2015-01-02'  GROUP BY branchid,vtype_name";
        if( vtypename=="" && locationid<=0 ){
            //both not set
            System.out.println("only year set year:"+year);
        }else if(  vtypename!="" && locationid>0   ){
            //both set
            sql += " AND BranchID="+locationid+" AND vtype_name='"+vtypename+"'";
        }else if(  vtypename=="" && locationid>0   ){
            //only location set
            sql += " AND BranchID="+locationid;
        }else if(  vtypename!="" && locationid<=0   ){
            //only category set
            sql += " AND Vtype_name='"+vtypename+"'";            
        }
        sql += "  ORDER BY vtype_name,category,branchid";
        data = dbc.getResult(sql);
        for( HashMap<String, String> record : data ){
            Vehicle vc = new Vehicle(); 
            vc.Vlicense = Integer.parseInt( record.get("vlicense") ) ;
            vc.Vname = record.get("vname");       
            vc.Vtype_name = record.get("category");
            vc.category = record.get("vtype_name");
            vc.initial_price = Double.parseDouble( record.get("initial_price") );
            vc.BranchID = Integer.parseInt( record.get("branchid"));    
            vc.status = Integer.parseInt( record.get("status"));
            vc.Year = Integer.parseInt( record.get("year"));
            vc.forRentFlag = Integer.parseInt( record.get("forrentflag")); 
            vc.Branch_location = getBranchLocationById(vc.BranchID);
            result.add(vc);
            
        }
        
        
        return result;  
    
    }
    
       
}
