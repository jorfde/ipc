/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managementapplication;

import DBAccess.ClinicDBAccess;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    
    private Stage mainStage;
    
    private Alert alert;
    
    @FXML
    private MenuItem closeMenu;
    @FXML
    private MenuItem doctorsMenu;
    @FXML
    private MenuItem patientsMenu;
    @FXML
    private MenuItem appointmentsMenu;
    @FXML
    private MenuItem addDoctorMenu;
    @FXML
    private MenuItem addPatientMenu;
    @FXML
    private MenuItem addAppMenu;
    
    private Scene scene1;
    
    private Scene scene2;
    
    private TableViewController tableViewController;
    
    private AppTableViewController appTableViewController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clinic = ClinicDBAccess.getSingletonClinicDBAccess();
        clinic.setClinicName("IPC Medical Services Clinic");
        
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(clinic.getClinicName());
        alert.setHeaderText("Saving data in DB");
        alert.setContentText("The application is saving the changes in the data into the database. This action can expend some minutes.");
        
        try{
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("TableView.fxml"));
            Pane root1 = (Pane) myLoader.load();
            tableViewController = myLoader.<TableViewController>getController();
            scene1 = new Scene (root1);
            
            myLoader = new FXMLLoader(getClass().getResource("AppTableView.fxml"));
            Pane root2 = (Pane) myLoader.load();
            appTableViewController = myLoader.<AppTableViewController>getController();
            scene2 = new Scene (root2);
        } catch(IOException ioe) {}
    }    

    @FXML
    private void buttonHandler(ActionEvent event) throws IOException{
        switch(((Node)event.getSource()).getId()){
            case "patientButton": createTable(PATIENT_MODE);break;
            case "doctorButton": createTable(DOCTOR_MODE);break;
            case "appointmentButton": createTable(APPOINTMENT_MODE);break;
        }
    }
    
    public void createTable(int mode) throws IOException {     
        switch(mode){
            case PATIENT_MODE: 

            case DOCTOR_MODE: 
                tableViewController.initStage(mainStage);
                tableViewController.initData(mode, clinic);
                mainStage.setScene(scene1);
                break;
                
            case APPOINTMENT_MODE: 
                appTableViewController.initStage(mainStage);
                appTableViewController.initData(mode, null, null, -1);
                mainStage.setScene(scene2);
                break;
        }
    }
    
    public void initStage(Stage s){
        mainStage = s;
    }

    @FXML
    private void menuHandler(ActionEvent event) throws IOException {
        switch(((MenuItem)event.getSource()).getId()){
            case "closeMenu": exit();break;
                
            case "doctorsMenu": createTable(DOCTOR_MODE);break;
                
            case "patientsMenu": createTable(PATIENT_MODE);break;
                
            case "appointmentsMenu": createTable(APPOINTMENT_MODE);break;
            
            case "addDoctorMenu": 
            
            case "addPatientMenu": 
                
            case "addAppMenu": 
        }
    }
    
    public void exit(){
        alert.show();clinic.saveDB();mainStage.close();
    }
}
