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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Doctor;
import model.Patient;
import model.Person;

/**
 * FXML Controller class
 *
 * @author jorge
 */
public class AddAppointmentController implements Initializable {

    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, String> patientColumn;
    @FXML
    private TableView<Doctor> doctorTable;
    @FXML
    private TableColumn<Doctor, String> doctorColumn;
    @FXML
    private Button setTimeButton;
    @FXML
    private TextField timeField;
    @FXML
    private TextField dateField;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    
    private ClinicDBAccess clinic;
    
    private ObservableList<Patient> patients = FXCollections.observableArrayList();
    
    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();
    
    private int patientIndex;
    
    private int doctorIndex;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        patientColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName() + " " +  c.getValue().getSurname()));
        doctorColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName() + " " +  c.getValue().getSurname()));
    }    

    void initData() {
        clinic = ClinicDBAccess.getSingletonClinicDBAccess();
        
        patients = FXCollections.observableList( clinic.getPatients() );
        patientTable.setItems(patients);
        
        doctors = FXCollections.observableList( clinic.getDoctors() );
        doctorTable.setItems(doctors);
    }

    @FXML
    private void buttonHandle(ActionEvent event) throws IOException {
        patientIndex = patientTable.getSelectionModel().selectedIndexProperty().getValue();
        doctorIndex = doctorTable.getSelectionModel().selectedIndexProperty().getValue();

        switch(((Node)event.getSource()).getId()){
            case "setTimeButton": createTimeWindow(); break;
                
            case "okButton":break;
                
            case "cancelButton": break;
        }
    }
    
     private void createTimeWindow() throws IOException{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("Calendar.fxml"));; 
        Pane root = (Pane) myLoader.load();;
        
        CalendarController calendarController = myLoader.<CalendarController>getController();
        calendarController.initData(clinic.getPatients().get(patientIndex), clinic.getDoctors().get(doctorIndex));
      
        Scene scene = new Scene (root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Set Time and Date");
             
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show(); 
    }
    
}
