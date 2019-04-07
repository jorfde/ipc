/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managementapplication;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Patient;
import model.Person;

/**
 * FXML Controller class
 *
 * @author Stéphane
 */
public class PatientDetailsController implements Initializable {
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField identifierField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField telephoneField;

    private ArrayList<Patient> patients;
    
    private ObservableList<Person> persons;
    
    private int index;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void buttonHandler(ActionEvent event) {
        if(((Node)event.getSource()).getId().equals("okButton") && index == -1){
            Patient p = new Patient(identifierField.getText(), nameField.getText(), surnameField.getText(),
                telephoneField.getText(), null);
            patients.add(p);
            persons.add(p);
        } 
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
    
    public void initData(ObservableList<Person> persons, ArrayList<Patient> patients, int index){
        this.index = index;
        this.patients = patients;
        this.persons = persons;
        
        if (index >= 0){
            Patient patient = patients.get(index);
            identifierField.setText(patient.getIdentifier());
            identifierField.setEditable(false);
            nameField.setText(patient.getName());
            nameField.setEditable(false);
            surnameField.setText(patient.getSurname());
            surnameField.setEditable(false);
            telephoneField.setText(patient.getTelephon());
            telephoneField.setEditable(false);
        } else {
            identifierField.setEditable(true);
            nameField.setEditable(true);
            surnameField.setEditable(true);
            telephoneField.setEditable(true);
        }
    }
    
}
