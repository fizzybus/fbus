/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

/**
 *
 * @author Bhupinder
 */
public class Mediator1 {
    
    private String customer_name;
    private String customer_phone;
    private String vehicle_name;
    private Integer vehicle_id;
    private String vehicle_type;
    private String transaction_location;
    private Integer transaction_type; // Rent or Reserve (0=Reserve 1=Rent)
    private String transaction_pickup;
     
    private String transaction_dropoff;
    private String equipment;
    private String cost;
    private Integer latest_entry;
    
    public Mediator1 (String customer_name,String customer_phone,String vehicle_name,Integer vehcile_id,String vehicle_type,Integer transaction_type,String trans_location,String trans_pickup,String trans_dropoff,String equipment, String cost,Integer latest_entry) {
            this.customer_name = customer_name;
            this.customer_phone = customer_phone;
            this.vehicle_name = vehicle_name;
            this.vehicle_id=vehcile_id;
            this.vehicle_type=vehicle_type;
            this.transaction_type= transaction_type;
            this.transaction_location=trans_location;
            this.transaction_pickup=trans_pickup;
            this.transaction_dropoff=trans_dropoff;
            this.equipment=equipment;
            this.cost=cost;
            this.latest_entry=latest_entry;
    }
     public Integer latest_entry(){return latest_entry;}
     public Integer transaction_type(){return transaction_type;}
     public String customer_name(){return customer_name;}
     public String customer_phone() {return customer_phone;}
     public String vehicle_name() {return vehicle_name;}
     public String vehicle_type() {return vehicle_type;}
     public Integer vehicle_id() {return vehicle_id;}
     public String transaction_location() {return transaction_location;}
     public String transaction_pickup() {return transaction_pickup;}
     public String transaction_dropoff() {return transaction_dropoff;}
     public String equipment() {return equipment;}
     public String cost() {return cost;}
     

    
}
