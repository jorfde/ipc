/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managementapplication;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Person;

/**
 * FXML Controller class
 *
 * @author Stéphane
 */
public class AppointmentsTableViewController implements Initializable {
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Initialization of the columns of the TableView
        //nameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        //surnameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("surname"));
        //dniColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("identifier"));
    }    
    
}
