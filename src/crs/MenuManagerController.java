/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class MenuManagerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML private Pane content_pane,menu_clerk,menu_manager;
    
    @FXML
    private void LoadProcessReturn(ActionEvent event)throws IOException  {   
        content_pane.getChildren().clear();
        content_pane.getChildren().add(FXMLLoader.load(getClass().getResource("ProcessReturn.fxml")));
    }
    
    @FXML
    private void LoadRentReserver(ActionEvent event)throws IOException  {
       
        content_pane.getChildren().clear();
   content_pane.getChildren().add(FXMLLoader.load(getClass().getResource("BookCar.fxml")));
    }
    
     @FXML
    private void LoadMangeCustomer(ActionEvent event)throws IOException  {
       
        content_pane.getChildren().clear();
   content_pane.getChildren().add(FXMLLoader.load(getClass().getResource("ManageCustomer.fxml")));
    }
    
     public void setInfo(Integer Login_user) throws IOException{
       switch(Login_user) {
        case 2:
            menu_clerk.setVisible(false);
            menu_manager.setVisible(true);
            menu_manager.setTranslateY(-480); 
            content_pane.getChildren().clear();
            content_pane.getChildren().add(FXMLLoader.load(getClass().getResource("Manager_Vehicle.fxml")));
        break;
        case 3:
             menu_clerk.setVisible(true);
             menu_manager.setVisible(true);
             menu_clerk.setTranslateY(-22); 
             menu_manager.setTranslateY(-480); 
        break;
        default:
            menu_clerk.setVisible(true);
            menu_manager.setVisible(false);
    }
   }
    
    @FXML
    private void LoadClerkReports(ActionEvent event)throws IOException  {
       
        content_pane.getChildren().clear();
   content_pane.getChildren().add(FXMLLoader.load(getClass().getResource("clerk_reports.fxml")));
    }
    
    @FXML
    private void LoadReports(ActionEvent event)throws IOException  {
       
        content_pane.getChildren().clear();
   content_pane.getChildren().add(FXMLLoader.load(getClass().getResource("Manager_Report.fxml")));
    }
    
    @FXML
    private void LoadManageReservation(ActionEvent event)throws IOException  {
       
        content_pane.getChildren().clear();
   content_pane.getChildren().add(FXMLLoader.load(getClass().getResource("ManageReservation.fxml")));
    }
    @FXML
    private void LoadRate(ActionEvent event)throws IOException  {
       
        content_pane.getChildren().clear();
   content_pane.getChildren().add(FXMLLoader.load(getClass().getResource("Manager_Setrate.fxml")));
    }
    @FXML
    private void LoadManageVehicle(ActionEvent event)throws IOException  {
       
        content_pane.getChildren().clear();
   content_pane.getChildren().add(FXMLLoader.load(getClass().getResource("Manager_Vehicle.fxml")));
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            content_pane.getChildren().add(FXMLLoader.load(getClass().getResource("Manager_Vehicle.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(MenuClerkController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
