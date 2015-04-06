/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import static crs.CRS.getDailyRentalByBranch;
import static crs.CRS.getDailyRentalByCategory;
import static crs.CRS.getDailyReturnByBranch;
import static crs.CRS.getRentalByCategory;
import static crs.CRS.getReturnByCategory;
import static crs.Manager_Report_DailyController.Vlist;
import crs.classes.Rental;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.DirectoryChooserBuilder;

/**
 * FXML Controller class
 *
 * @author Ralph
 */
public class Manager_DailyReturnController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        pane_summary.setVisible(false);
    }    
    @FXML
    private DatePicker report_datepicker;
    @FXML
    private TableView table_vehicle;
    @FXML
    private TableView table_byloc;  
    @FXML
    private TableView table_bycategory;
    @FXML
    private BarChart barchart_dailyreport;
    @FXML
    private CategoryAxis Caxis_dailyreport;
    @FXML
    private NumberAxis Naxis_dailyreport;
    @FXML
    private Pane pane_summary;
    @FXML
    private Button button_download;
    @FXML
    private Button button_summary;
    @FXML
    private Label label_result;
    @FXML
    private TextField totalField;
    
    @FXML
    private TextField totalAmount_field;
    
    static ArrayList<Rental> bdata;
    static ArrayList<Rental> cdata;
    static ArrayList<crs.classes.Vehicle> Vlist;
    
    
    @FXML
    private void  onChangeDate(ActionEvent event){
        String date = report_datepicker.getValue().toString();
        table_vehicle.setVisible(true);
        pane_summary.setVisible(false);
        Vlist = CRS.getDailyReturn(date,-1);   
        bdata = getDailyReturnByBranch(date);       
        cdata = getReturnByCategory(date);
    }
    
    
    @FXML
    private void  onView(ActionEvent event){
        if( report_datepicker.getValue()==null ) return;
        String date = report_datepicker.getValue().toString();
        
        Vlist = CRS.getDailyReturn(date,-1);   
        bdata = getDailyReturnByBranch(date);       
        cdata = getReturnByCategory(date);
        
        table_vehicle.setVisible(true);
        pane_summary.setVisible(false);
        
        setTable(Vlist,table_vehicle);
        totalField.setText(Vlist.size()+"" );
        setTotalAmount(Vlist,totalAmount_field);

    }
    @FXML
    private void  onSummary(ActionEvent event){
        if(report_datepicker.getValue()==null) return;
        String date = report_datepicker.getValue().toString();
        
        Vlist = CRS.getDailyReturn(date,-1);   
        bdata = getDailyReturnByBranch(date);       
        cdata = getReturnByCategory(date);     
        table_vehicle.setVisible(false); 
        pane_summary.setVisible(true);
        pane_summary.setLayoutX(60);
        pane_summary.setLayoutY(100);       
       // HashMap<String, Integer>  data = getDailyRentalByCategory(  date  );
//        setChart(data,barchart_dailyreport,Caxis_dailyreport,Naxis_dailyreport);

        setTableR(bdata,table_byloc);
        setTableC(cdata,table_bycategory);
        totalField.setText(Vlist.size()+"" );
        setTotalAmount(Vlist,totalAmount_field);
        
    }
    @FXML
    private void onDownload(ActionEvent event){
        if(report_datepicker.getValue()==null) return;
        
        String date = report_datepicker.getValue().toString();
        
        Vlist = CRS.getDailyReturn(date,-1);   
        bdata = getDailyReturnByBranch(date);       
        cdata = getReturnByCategory(date);
        
        String filename = report_datepicker.getValue().toString()+"_DailyRental.pdf";
                DirectoryChooserBuilder builder = DirectoryChooserBuilder.create();
                builder.title("Hello World");
                String cwd = System.getProperty("user.dir");
                File file = new File(cwd);
                builder.initialDirectory(file);
                DirectoryChooser chooser = builder.build();
                File chosenDir = chooser.showDialog(null);
                if (chosenDir != null) {
                  System.out.println(chosenDir.getAbsolutePath());
                } else {
                 System.out.print("no directory chosen");
                }
        
                
                
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(chosenDir.getAbsolutePath()+"/"+ filename));
          
            document.open();
            Paragraph paragraph = new Paragraph();
            paragraph.add("Daily Return Report of "+ report_datepicker.getValue().toString());
            paragraph.setAlignment(Element.ALIGN_CENTER); 
            paragraph.setSpacingAfter(40); 
            document.add(paragraph);
            document.add(createPDFTableVehicle(Vlist));
            paragraph.clear();
            paragraph.add("  ");
            paragraph.setSpacingAfter(20); 
            document.add(paragraph);
            document.add(createPDFTableByCategory(cdata));
            
            paragraph.setSpacingAfter(20); 
            document.add(paragraph);
            document.add(createPDFTableByBranch(bdata));
            document.close();
            label_result.setText("Download successfully");
            label_result.setVisible(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Manager_ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(Manager_ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static PdfPTable createPDFTableVehicle(ArrayList<crs.classes.Vehicle> list){
    
     PdfPTable table = new PdfPTable(6);
   
        PdfPCell cell; 
        table.addCell("Vlicense");
        table.addCell("Vname");
        table.addCell("Branch Location");
        table.addCell("Category");
        table.addCell("Vtype name");
        table.addCell("amount");
        for( int i=0;i<list.size();i++ ){           
            table.addCell(  list.get(i).Vlicense+"" );
            table.addCell(  list.get(i).Vname+"" );
            table.addCell(  list.get(i).Branch_location+"" );
            table.addCell(  list.get(i).Vtype_name+"" );
            table.addCell(  list.get(i).category+"" );
            table.addCell(  list.get(i).initial_price+"" );
            
        }
        return table;
    }
    
    
    
    public static PdfPTable createPDFTableByCategory(ArrayList<crs.classes.Rental> list){    
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell; 
        table.addCell("Category");
        table.addCell("Number");
        table.addCell("Amount");
        for( int i=0;i<list.size();i++ ){           
            table.addCell(  list.get(i).typename );
            table.addCell(  list.get(i).number+"" );            
            table.addCell(  list.get(i).amount+"" );
        }
        return table;
    }
    
    public static PdfPTable createPDFTableByBranch(ArrayList<crs.classes.Rental> list){    
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell; 
        table.addCell("Branch");
        table.addCell("Number");
        table.addCell("Amount");
        for( int i=0;i<list.size();i++ ){           
            table.addCell(  list.get(i).branchLoc );
            table.addCell(  list.get(i).number+"" );           
            table.addCell(  list.get(i).amount+"" );
        }
        return table;
    }    
    
    private void setTable(ArrayList<crs.classes.Vehicle> list,TableView<crs.classes.Vehicle> table){
        
        TableColumn<crs.classes.Vehicle,String> VIDCol = new TableColumn<>("Vlicense");
        VIDCol.setMinWidth(90);
        TableColumn<crs.classes.Vehicle,String> VtypeCol = new TableColumn<>("category");
        VtypeCol.setMinWidth(70); 
        TableColumn<crs.classes.Vehicle,String> typeCol = new TableColumn<>("Vtype_name");
        typeCol.setMinWidth(90); 
        TableColumn<crs.classes.Vehicle,String> nameCol = new TableColumn<>("Vname");
        nameCol.setMinWidth(90); 
        TableColumn<crs.classes.Vehicle,String> LocationCol = new TableColumn<>("Branch_location");
        LocationCol.setMinWidth(200);
        TableColumn<crs.classes.Vehicle,String> amountCol = new TableColumn<>("amount");
        amountCol.setMinWidth(100);
      
        
        table.getColumns().clear();        
        table.getColumns().addAll(VIDCol,nameCol,LocationCol,VtypeCol,typeCol,amountCol);
       if(list.size()==0){
        return;
       }
        ObservableList<crs.classes.Vehicle> myData = FXCollections.observableArrayList();
        for(int i=0; i<list.size();i++){
            myData.add(list.get(i));
        }
   
        VIDCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Vehicle, String>("Vlicense"));
        VtypeCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Vehicle, String>("Vtype_name"));
        typeCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Vehicle, String>("category"));
        nameCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Vehicle, String>("Vname"));
        LocationCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Vehicle, String>("Branch_location"));     
        amountCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Vehicle, String>("initial_price"));       

        table.setItems(myData);
        
 
    }
    
    private void setTableR(ArrayList<Rental> list,TableView table){
        
        TableColumn<crs.classes.Rental,String> branchCol = new TableColumn<>("branch");
        branchCol.setMinWidth(90);
        TableColumn<crs.classes.Rental,String> numberCol = new TableColumn<>("number");
        numberCol.setMinWidth(70); 
        TableColumn<crs.classes.Rental,String> amountCol = new TableColumn<>("amount");
        amountCol.setMinWidth(70); 
      
        
        table.getColumns().clear();        
        table.getColumns().addAll(branchCol,numberCol,amountCol);
       if(list.size()==0){
        return;
       }
        ObservableList<crs.classes.Rental> myData = FXCollections.observableArrayList();
        for(int i=0; i<list.size();i++){
            myData.add(list.get(i));
        }
   
        branchCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Rental, String>("branchLoc"));
        numberCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Rental, String>("number"));    
        amountCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Rental, String>("amount"));    

        table.setItems(myData);

    }
    
    private void setTableC(ArrayList<Rental> list,TableView table){
        
        TableColumn<crs.classes.Rental,String> branchCol = new TableColumn<>("category");
        branchCol.setMinWidth(90);
        TableColumn<crs.classes.Rental,String> numberCol = new TableColumn<>("number");
        numberCol.setMinWidth(70); 
        TableColumn<crs.classes.Rental,String> amountCol = new TableColumn<>("amount");
        amountCol.setMinWidth(70); 
      
      
        
        table.getColumns().clear();        
        table.getColumns().addAll(branchCol,numberCol,amountCol);
       if(list.size()==0){
        return;
       }
        ObservableList<crs.classes.Rental> myData = FXCollections.observableArrayList();
        for(int i=0; i<list.size();i++){
            myData.add(list.get(i));
        }
   
        branchCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Rental, String>("typename"));
        numberCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Rental, String>("number"));    
        amountCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Rental, String>("amount"));  

        table.setItems(myData);

    }
    
    private void setChart(HashMap<String, Integer>  data,BarChart barchart,CategoryAxis caxis,NumberAxis naxis){
        
       //barchart_dailyreport.setTitle("");     
        //caxis.setLabel("Category");       
        naxis.setLabel("Number");
 
        XYChart.Series series1 = new XYChart.Series();
        //series1.setName("Rent number");  
        int i=0;
        for(  String temp:data.keySet()   ){
            series1.getData().add(new XYChart.Data(temp, data.get(temp)));            
            } 
        barchart_dailyreport.getData().addAll(series1);
    
    }
    
    private void setTotalAmount(ArrayList<crs.classes.Vehicle> Vlist, TextField totalAmount_field){
        double total = 0;
        for( int i=0;i<Vlist.size();i++){
            total += Vlist.get(i).initial_price;        
        }
        totalAmount_field.setText(total+"");
    
    }
    
    
}
