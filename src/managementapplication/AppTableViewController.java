/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managementapplication;

import DBAccess.ClinicDBAccess;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private Button viewButton;
    @FXML
    private Button deleteButton;
    
    private ClinicDBAccess clinic;
    
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    
    private Stage primaryStage;
    private Scene prevScene;
    private String prevTitle;
    
    @FXML
    private Button returnButton;
    
    private Alert remove = new Alert(Alert.AlertType.CONFIRMATION);

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
        
        remove.setTitle("Confirmation Dialog");
        remove.setHeaderText("Remove an element");
        remove.setContentText("Do you want to continue?");
        
        //Disable delete button
        deleteButton.disableProperty().bind(Bindings.equal(-1,tableView.getSelectionModel().selectedIndexProperty()));
        
        //Disable view details button
        viewButton.disableProperty().bind(Bindings.equal(-1,tableView.getSelectionModel().selectedIndexProperty()));
    }    

    @FXML
    private void buttonHandler(ActionEvent event) throws IOException {
        int index = tableView.getSelectionModel().selectedIndexProperty().getValue();

        switch(((Node)event.getSource()).getId()){
            case "addButton": createAddWindow();break;
                
            case "deleteButton":
                if(delete())
                appointments.remove(index);break;
                
            case "returnButton": exit();break;
        }
    }
    
    private void createAddWindow() throws IOException{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("AddAppointment.fxml"));; 
        Pane root = (Pane) myLoader.load();;
        
        AddAppointmentController addAppointmentController = myLoader.<AddAppointmentController>getController();
        addAppointmentController.initData(appointments);
      
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
                    primaryStage.setTitle("Patient's Appointments");
                    break;
            case DOCTOR_MODE: 
                    appointments = FXCollections.observableList( clinic.getDoctorAppointments(doctorID) );
                    primaryStage.setTitle("Doctor's Appointments");
                    break;
                
            case APPOINTMENT_MODE:
                    appointments = FXCollections.observableList( clinic.getAppointments() );
                    primaryStage.setTitle("List of Appointments");
                    break;
                
        }
        
        tableView.setItems(appointments);
    }

    public void initStage(Stage stage) {
        primaryStage = stage;
        prevScene = stage.getScene();
        prevTitle = stage.getTitle();
    }
    
    private void exit(){
        primaryStage.setTitle(prevTitle);
        primaryStage.setScene(prevScene);
    }
    
    private boolean delete(){
        boolean res = false;
        Optional<ButtonType> result = remove.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                res = true;
            }
            
         return res;
    }
}
