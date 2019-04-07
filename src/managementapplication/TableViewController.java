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
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Doctor;
import model.Patient;
import model.Person;

/**
 * FXML Controller class
 *
 * @author Stéphane
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
    
    private ObservableList<Patient> patients = FXCollections.observableArrayList();
    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();
    private ObservableList<Person> persons = FXCollections.observableArrayList();
    
    private ClinicDBAccess clinic;
    
    private int mode = 0;

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
    }
    
    private void createListWindow(int index) throws IOException{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("TableView.fxml"));
        Pane root = (Pane) myLoader.load();
        
        Scene scene = new Scene (root);
        Stage stage = new Stage();
        stage.setScene(scene);
            
        //Get the controller of the UI
        switch(mode){
            case ManagementApplicationController.PATIENT_MODE: PatientDetailsController patientController = myLoader.<PatientDetailsController>getController();
                    //patientController.initData(patients, index);
                    break;
            case ManagementApplicationController.DOCTOR_MODE: DoctorDetailsController doctorController = myLoader.<DoctorDetailsController>getController();
                    //patientController.initData(patients, index);
                    break;
        }
        
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
            case ManagementApplicationController.PATIENT_MODE: 
                    persons = FXCollections.observableList( changeClass(clinic.getPatients()) );
                    tableView.setItems(persons);
                    break;
            case ManagementApplicationController.DOCTOR_MODE: 
                    doctors = FXCollections.observableList(clinic.getDoctors());
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
    private void buttonHandler(ActionEvent event) {
    }
    
}
