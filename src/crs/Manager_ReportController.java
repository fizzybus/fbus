/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.deploy.net.HttpRequest;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.DirectoryChooserBuilder;
import javafx.stage.FileChooser;
/**
 * FXML Controller class
 *
 * @author Ralph
 */
public class Manager_ReportController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        gotoPane(pane_daily,"Manager_Report_Daily.fxml");
        
    }    
    
    
     @FXML
    private Pane main_pane;
    @FXML
    private Pane pane_daily;
    @FXML
    private Pane pane_daily_bybranch;
    @FXML
    private Pane pane_dailyreturn;
    @FXML
    private Pane pane_dailyreturn_bybranch;
    
    
    @FXML
    private Pane main_pane_sold;
   
    
    
    
     @FXML
    private void gotoDaily(ActionEvent event)  {  
        gotoPane(pane_daily,"Manager_Report_Daily.fxml");
    }
    
    @FXML
    private void gotoDailyBybranch(ActionEvent event) {
        gotoPane(pane_daily_bybranch,"Manager_Report_bybranch.fxml");       
       
    }
    
    @FXML
    private void gotoReturn(ActionEvent event) {
      gotoPane(pane_dailyreturn,"Manager_DailyReturn.fxml");
    }
    @FXML
    private void gotoReturnBybranch(ActionEvent event) {
      gotoPane(pane_dailyreturn_bybranch,"Manager_Returnbybranch.fxml");
    }

    
    private void gotoPane(Pane gotoPane,String fxml){
        try {
           
           main_pane.getChildren().remove(pane_daily);
           main_pane.getChildren().remove(pane_daily_bybranch);
           main_pane.getChildren().remove(pane_dailyreturn);
           main_pane.getChildren().remove(pane_dailyreturn_bybranch);
           
           
           
           gotoPane.getChildren().add( FXMLLoader.load( getClass().getResource(fxml) )   );    
           gotoPane.setLayoutX(0);
           gotoPane.setLayoutY(0);     
           main_pane.getChildren().add( gotoPane );  
           
        } catch (IOException ex) {
            Logger.getLogger(Manager_ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    
}
