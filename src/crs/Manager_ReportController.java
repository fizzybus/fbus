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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        
    }    
    
    
    @FXML
    private void onReport(ActionEvent event){
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
            PdfWriter.getInstance(document, new FileOutputStream(chosenDir.getAbsolutePath()+"/report322.pdf"));
          
            document.open();
            Paragraph paragraph = new Paragraph();
            paragraph.add("update content");
            document.add(paragraph);
            document.add(createTable());
            document.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Manager_ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(Manager_ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        
    }
    
    private static PdfPTable createTable(){
    
     PdfPTable table = new PdfPTable(3);
        // the cell object
        PdfPCell cell;
        // we add a cell with colspan 3
        cell = new PdfPCell(new Phrase("Cell with colspan 3"));
        cell.setColspan(3);
        table.addCell(cell);
        // now we add a cell with rowspan 2
        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
        cell.setRowspan(2);
        table.addCell(cell);
        // we add the four remaining cells with addCell()
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");
        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");
        return table;
    }
    
    
}
