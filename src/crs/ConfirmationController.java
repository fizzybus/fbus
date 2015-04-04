/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class ConfirmationController implements Initializable {
    @FXML
    private Label pop_customername;
    @FXML
    private Label pop_customerphone;
    @FXML
    private Label pop_location;
    @FXML
    private Label pop_pickup;
    @FXML
    private Label pop_dropoff;
    @FXML
    private Label pop_vehiclename;
    @FXML
    private Label pop_vehiclelicense;
    @FXML
    private Label pop_equipment;
    @FXML
    private Label pop_cost;
    @FXML
    private Label pop_confirmation;
    @FXML Label rent_reserve;

    /**
     * Initializes the controller class.
     */
    
    @FXML
    public void setInfo(Mediator1 Info) {
        if(0==Info.transaction_type())
            rent_reserve.setText("Confirmation Number");
        else
            rent_reserve.setText("Rental Id");
        
        pop_confirmation.setText(Info.latest_entry().toString());
        pop_customername.setText(Info.customer_name());
        pop_customerphone.setText(Info.customer_phone());
        pop_vehiclename.setText(Info.vehicle_name());
        pop_vehiclelicense.setText(Info.vehicle_id().toString());
        pop_location.setText(Info.transaction_location());
        pop_pickup.setText(Info.transaction_pickup());
        pop_dropoff.setText(Info.transaction_dropoff());
        pop_equipment.setText(Info.equipment());
        pop_cost.setText(Info.cost());   
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
