/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managementapplication;

import DBAccess.ClinicDBAccess;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Patient;

/**
 * FXML Controller class
 *
 * @author Stéphane & Jorge
 */
public class ManagementApplicationController implements Initializable {
    @FXML
    private Button patientButton;
    @FXML
    private Button doctorButton;
    @FXML
    private Button appointmentButton;
    
    private ClinicDBAccess clinic;
    
    public static final int PATIENT_MODE = 0;
    public static final int DOCTOR_MODE = 1;
    public static final int APPOINTMENT_MODE = 2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clinic = ClinicDBAccess.getSingletonClinicDBAccess();
        clinic.setClinicName("IPC Medical Services Clinic");
        String url2 = System.getProperty("user.dir")+File.separator+ "src"+ File.separator+ "images"+ File.separator+ "men2.PNG";
        Image avatar = null;
        try {
            avatar = new Image(new FileInputStream(url2));
        } catch (FileNotFoundException ex) {
            System.out.print("Tonto");
        }
        Patient patient = new Patient("5307867J", "Juan", "Cafe Grandes", "9376543", avatar);
        clinic.getPatients().add(patient);
    }    

    @FXML
    private void buttonHandler(ActionEvent event) throws IOException{
        switch(((Node)event.getSource()).getId()){
            case "patientButton": createTable(PATIENT_MODE);break;
            case "doctorButton": createTable(DOCTOR_MODE);break;
            case "appointmentButton": createTable(APPOINTMENT_MODE);break;
        }
    }
    
    private void createTable(int mode) throws IOException {     
        FXMLLoader myLoader = null; 
        Pane root = null;
        
        switch(mode){
            case PATIENT_MODE: 

            case DOCTOR_MODE: 
                myLoader = new FXMLLoader(getClass().getResource("TableView.fxml"));
                root = (Pane) myLoader.load();
                TableViewController tableViewController = myLoader.<TableViewController>getController();
                tableViewController.initData(mode, clinic);
                break;
                
            case APPOINTMENT_MODE: 
                myLoader = new FXMLLoader(getClass().getResource("AppTableView.fxml"));
                root = (Pane) myLoader.load();
                AppTableViewController appTableViewController = myLoader.<AppTableViewController>getController();
                appTableViewController.initData(mode, null, null);
                break;
        }
        
        Scene scene = new Scene (root);
        Stage stage = new Stage();
        stage.setScene(scene);
        
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        
    }
}
