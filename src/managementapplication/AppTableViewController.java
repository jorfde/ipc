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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static managementapplication.ManagementApplicationController.APPOINTMENT_MODE;
import static managementapplication.ManagementApplicationController.DOCTOR_MODE;
import static managementapplication.ManagementApplicationController.PATIENT_MODE;
import model.Appointment;

/**
 * FXML Controller class
 *
 * @author jorge & Steph
 */
public class AppTableViewController implements Initializable {
    @FXML
    private TableView<Appointment> tableView;
    @FXML
    private TableColumn<Appointment, String> patientColumn;
    @FXML
    private TableColumn<Appointment, String> doctorColumn;
    @FXML
    private TableColumn<Appointment, String> dateColumn;
    @FXML
    private TableColumn<Appointment, String> timeColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button viewButton;
    @FXML
    private Button deleteButton;
    
    private ClinicDBAccess clinic;
    
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Initialization of the columns of the TableView
        patientColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPatient().getName() + " " +  c.getValue().getPatient().getSurname()));
        doctorColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDoctor().getName() + " " +  c.getValue().getDoctor().getSurname()));
        dateColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAppointmentDateTime().toLocalDate().toString()));
        timeColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAppointmentDateTime().toLocalTime().toString()));
    }    

    @FXML
    private void buttonHandler(ActionEvent event) throws IOException {
        int index = tableView.getSelectionModel().selectedIndexProperty().getValue();
        boolean removed = false;
        switch(((Node)event.getSource()).getId()){
            case "addButton": createAddWindow();break;
                
            case "deleteButton":appointments.remove(index);break;
        }
    }
    
    private void createAddWindow() throws IOException{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("AddAppointment.fxml"));; 
        Pane root = (Pane) myLoader.load();;
        
        AddAppointmentController addAppointmentController = myLoader.<AddAppointmentController>getController();
        addAppointmentController.initData();
      
        Scene scene = new Scene (root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add an appointment");
             
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show(); 
    }
    
    public void initData(int mode, String patientID, String doctorID){
        clinic = ClinicDBAccess.getSingletonClinicDBAccess();
        
        switch(mode){
            case PATIENT_MODE:
                    appointments = FXCollections.observableList( clinic.getPatientAppointments(patientID) );
                    break;
            case DOCTOR_MODE: 
                    appointments = FXCollections.observableList( clinic.getDoctorAppointments(doctorID) );
                    break;
                
            case APPOINTMENT_MODE:
                    appointments = FXCollections.observableList( clinic.getAppointments() );
                    break;
                
        }
        
        tableView.setItems(appointments);
    }
}
