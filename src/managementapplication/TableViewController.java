/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managementapplication;

import DBAccess.ClinicDBAccess;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import static managementapplication.ManagementApplicationController.DOCTOR_MODE;
import static managementapplication.ManagementApplicationController.PATIENT_MODE;
import model.Doctor;
import model.Patient;
import model.Person;

/**
 * FXML Controller class
 *
 * @author Stéphane & Jorge
 */
public class TableViewController implements Initializable {
    @FXML
    private TableView<Person> tableView;
    @FXML
    private TableColumn<Person, String> nameColumn;
    @FXML
    private TableColumn<Person, String> surnameColumn;
    @FXML
    private TableColumn<Person, String> dniColumn;
    @FXML
    private Button viewButton;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button appointmentButton;
    @FXML
    private Button returnButton;
    
    private ArrayList<Patient> patients;
    private ArrayList<Doctor> doctors;
    private ObservableList<Person> persons = FXCollections.observableArrayList();
    
    private ClinicDBAccess clinic;
    
    private int mode = 0;
    
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private Alert remove = new Alert(Alert.AlertType.CONFIRMATION);
    
    private Stage primaryStage;
    private Scene prevScene;
    private String prevTitle;
    
    @FXML
    private MenuItem closeMenu1;
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

    
    private int index;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Initialization of the columns of the TableView
        nameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("surname"));
        dniColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("identifier"));
        
        //Disable delete button
        deleteButton.disableProperty().bind(Bindings.equal(-1,tableView.getSelectionModel().selectedIndexProperty()));
        
        //Disable view details button
        viewButton.disableProperty().bind(Bindings.equal(-1,tableView.getSelectionModel().selectedIndexProperty()));
        
        //Disable appointments button
        appointmentButton.disableProperty().bind(Bindings.equal(-1,tableView.getSelectionModel().selectedIndexProperty()));
        
        remove.setTitle("Confirmation Dialog");
        remove.setHeaderText("Remove an element");
        remove.setContentText("Do you want to continue?");
    }
    
    private void createDetailsWindow(int index) throws IOException{
        FXMLLoader myLoader = null; 
        Pane root = null;
        
        switch(mode){
            case PATIENT_MODE: 
                myLoader = new FXMLLoader(getClass().getResource("PatientDetails.fxml"));
                root = (Pane) myLoader.load();
                PatientDetailsController patientController = myLoader.<PatientDetailsController>getController();
                patientController.initData(persons, patients, index);
                break;
            case DOCTOR_MODE: 
                myLoader = new FXMLLoader(getClass().getResource("DoctorDetails.fxml"));
                root = (Pane) myLoader.load();
                DoctorDetailsController doctorController = myLoader.<DoctorDetailsController>getController();
                doctorController.initData(persons, doctors, index);
                break;
        }
        
        Scene scene = new Scene (root);
        Stage stage = new Stage();
        stage.setScene(scene);
        
        //Retocar
        if (index >=0){
            stage.setTitle("Edit person details");
        } else {
            stage.setTitle("Add a new person");
        }
        
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
  
    }
    
    public void initData(int mode, ClinicDBAccess clinic){
        this.mode = mode;
        this.clinic = clinic;
        switch(mode){
            case PATIENT_MODE:
                    patients = clinic.getPatients();
                    persons = FXCollections.observableList( changeClass(patients) );
                    tableView.setItems(persons);
                    break;
            case DOCTOR_MODE: 
                    doctors = clinic.getDoctors();
                    persons = FXCollections.observableList( changeClass(doctors) );
                    tableView.setItems(persons);
                    break;
                
        }
    }
    
    private ArrayList<Person> changeClass(ArrayList list){
        ArrayList<Person> persons = new ArrayList<Person>();
        for(int i = 0;i < list.size();i++){
            persons.add((Person) list.get(i));
        }
        
        return persons;
    }
    
    @FXML
    private void buttonHandler(ActionEvent event) throws IOException {
        index = tableView.getSelectionModel().selectedIndexProperty().getValue();
        boolean removed = false;
        boolean continueRemove = false;
        switch(((Node)event.getSource()).getId()){
            case "addButton": createDetailsWindow(-1);break;
            case "viewButton": createDetailsWindow(index);break;
            case "deleteButton": 
                
                if(mode == PATIENT_MODE && !clinic.hasAppointments(clinic.getPatients().get(index))){
                    if(delete()){
                        patients.remove(index);
                        removed = true;
                    }
                }
                else if(mode == DOCTOR_MODE && !clinic.hasAppointments(clinic.getPatients().get(index))) {
                    if(delete()){
                        doctors.remove(index);
                        removed = true;
                    }
                } else {
                    alert.setTitle("Error");
                    if(mode == PATIENT_MODE){
                        alert.setHeaderText("You cannot remove this patient");
                        alert.setContentText("This patient has some appointments.");
                    }
                    else {
                        alert.setHeaderText("You cannot remove this doctor");
                        alert.setContentText("This doctor has some appointments.");
                    }
                    alert.showAndWait();
                }
                if(removed){
                    persons.remove(index);
                }
                break;
            case "appointmentButton": 
                if(mode == PATIENT_MODE) createAppointmentWindow(PATIENT_MODE, patients.get(index).getIdentifier(), null);
                else createAppointmentWindow(DOCTOR_MODE, null, doctors.get(index).getIdentifier());
                break;
                
            case "returnButton": exit();break;
        }
        
    }
    
    private void createAppointmentWindow(int mode, String patientID, String doctorID) throws IOException{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("AppTableView.fxml"));; 
        Pane root = (Pane) myLoader.load();;
        
        AppTableViewController appTableViewController = myLoader.<AppTableViewController>getController();
        appTableViewController.initStage(primaryStage);
        appTableViewController.initData(mode, patientID, doctorID, index);
      
        Scene scene = new Scene (root);
        primaryStage.setScene(scene);
        primaryStage.show(); 
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
    
    @FXML
    private void menuHandler(ActionEvent event) {
    
    }
}
