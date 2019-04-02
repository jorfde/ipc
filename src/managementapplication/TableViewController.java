/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managementapplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    
    private ObservableList<Person> patients = FXCollections.observableArrayList();
    private ObservableList<Person> doctors = FXCollections.observableArrayList();
    
    private int mode = 0;
    private final int PATIENTS = 0;
    private final int DOCTORS = 1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Initialization of the columns of the TableView
        nameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("surname"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("idemtifier"));
        
        //Disable delete button
        deleteButton.disableProperty().bind(Bindings.equal(-1,tableView.getSelectionModel().selectedIndexProperty()));
        
        //Disable view details button
        viewButton.disableProperty().bind(Bindings.equal(-1,tableView.getSelectionModel().selectedIndexProperty()));
    }
    
    private void createListWindow(int index) throws IOException{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("FXMLAddEditView.fxml"));
        Pane root = (Pane) myLoader.load();
        
        Scene scene = new Scene (root);
        Stage stage = new Stage();
        stage.setScene(scene);
            
        //Get the controller of the UI
        switch(mode){
            case 0: PatientDetailsController patientController = myLoader.<PatientDetailsController>getController();
                    //patientController.initData(patients, index);
                    break;
            case 1: DoctorDetailsController doctorController = myLoader.<DoctorDetailsController>getController();
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
    
}
