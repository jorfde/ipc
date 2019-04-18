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
import javafx.scene.control.MenuItem;
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
    @FXML
    private Button returnButton;
    @FXML
    private Button deleteButton;
    
    private ClinicDBAccess clinic;
    
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    
    private Stage primaryStage;
    
    private int mode;
    
    private int index;
    
    private Alert remove = new Alert(Alert.AlertType.CONFIRMATION);  
            
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
    
    private ManagementApplicationController mac;
    
    private TableViewController tvc;
    
    private Scene prevScene;

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
    
    public void createAddWindow() throws IOException{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("AddAppointment.fxml"));; 
        Pane root = (Pane) myLoader.load();;
        
        AddAppointmentController addAppointmentController = myLoader.<AddAppointmentController>getController();
        addAppointmentController.initData(mode, appointments, index);
      
        Scene scene = new Scene (root);
        Stage stage = new Stage();
        stage.setScene(scene);
             
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show(); 
    }
    
    public void initData(int mode, String patientID, String doctorID, int index){
        clinic = ClinicDBAccess.getSingletonClinicDBAccess();
        this.mode = mode;
        this.index = index;
        
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

    public void initStage(Stage stage) {
        primaryStage = stage;
        prevScene = primaryStage.getScene();
    }
    
    private void exit(){
        if(mode == APPOINTMENT_MODE)
            primaryStage.setScene(mac.getMainScene());
        else
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
    
    @FXML
    private void menuHandler(ActionEvent event) throws IOException {
        switch(((MenuItem)event.getSource()).getId()){
            case "closeMenu": mac.exit();break;
                
            case "doctorsMenu": mac.createTable(DOCTOR_MODE, null, null, -1);break;
                
            case "patientsMenu": mac.createTable(PATIENT_MODE, null, null, -1);break;
                
            case "appointmentsMenu": mac.createTable(APPOINTMENT_MODE, null, null, -1);break;
            
            case "addDoctorMenu": tvc.initData(DOCTOR_MODE);tvc.initStage(primaryStage);tvc.createDetailsWindow(-1, DOCTOR_MODE);break;
            
            case "addPatientMenu": tvc.initData(PATIENT_MODE);tvc.initStage(primaryStage);tvc.createDetailsWindow(-1, PATIENT_MODE);break;
                
            case "addAppMenu": createAddWindow();break;
        }
    }
    
    public void passControllers(ManagementApplicationController mac, TableViewController tvc){
        this.mac = mac;
        this.tvc = tvc;
    }
}
