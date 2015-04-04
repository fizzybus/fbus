/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Bhupinder
 */
public class AvailableVehicle {
    
     private final SimpleStringProperty location;
    private final SimpleStringProperty name;
    private final SimpleStringProperty type;
    
    public AvailableVehicle (String location,String name, String type) {
        this.location = new SimpleStringProperty(location);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);  
        //System.out.println("License:"+this.vlicense+" Name:"+this.name+" Year:"+this.year);
    }
    
    public String getLocation() {
        return location.get();
    }
    public String getName() {
        return name.get();
    }
    public String getType() {
        return  type.get();
    }
    
}
