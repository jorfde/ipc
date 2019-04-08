/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managementapplication;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Patient;
import model.Person;

/**
 * FXML Controller class
 *
 * @author Stéphane & Jorge
 */
public class PatientDetailsController implements Initializable {
    @FXML
    private Label errorLabel;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField identifierField;
    @FXML
    private Label identifierError;
    @FXML
    private TextField nameField;
    @FXML
    private Label nameError;
    @FXML
    private TextField surnameField;
    @FXML
    private Label surnameError;
    @FXML
    private TextField telephoneField;
    @FXML
    private Label telephoneError;

    private ArrayList<Patient> patients;
    
    private ObservableList<Person> persons;
    
    private int index;
    
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    
    private boolean error = false;
    
    private String contentText = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("A field is not valid");
    }    

    @FXML
    private void buttonHandler(ActionEvent event) {
        boolean errorIdentifier = false;
        boolean errorTelephone = false;
        boolean errorName = false;
        boolean errorSurname = false;
        
        if(((Node)event.getSource()).getId().equals("okButton") && index == -1){
            
            String identifier = identifierField.getText();
            String name = nameField.getText();
            String surname = surnameField.getText();
            String telephone = telephoneField.getText();
            
            errorIdentifier = checkField(identifier, "[0-9A-Z]+", 
                    "You can only use numbers and capital letters in the IDENTIFIER field" + "\n" + "\n", identifierError);
            
            errorName = checkField(name, "[^0-9]+", "You can only use letters in the NAME field" + "\n" + "\n", nameError);;
            
            errorSurname = checkField(surname, "[^0-9]+", "You can only use letters in the SURNAME field" + "\n" + "\n", surnameError);
            
            errorTelephone = checkField(telephone, "[0-9]+", "You can only use numbers in the TELEPHONE field" + "\n" + "\n", telephoneError);
            
            error = errorIdentifier || errorTelephone || errorName || errorSurname;
            
            if(error){
                errorAlert.setContentText(contentText);
                errorAlert.showAndWait();
            } else {
                Patient p = new Patient(identifierField.getText(), nameField.getText(), surnameField.getText(),
                    telephoneField.getText(), null);
                patients.add(p);
                persons.add(p);

                alert.setTitle("Information");
                alert.setHeaderText("You have added a patient");
                alert.setContentText("The patient was succesfully added to the data base.");

                alert.showAndWait();
                ((Node) event.getSource()).getScene().getWindow().hide();
            }
        } else {
            ((Node) event.getSource()).getScene().getWindow().hide();
        }
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
        }
    }
    
    private boolean checkField(String data, String regex, String content, Label error){
        
        boolean res = false;
        
        if(!data.matches(regex)){
            error.setText("Not valid");
            contentText += content;
            res = true;
        } else {
            error.setText("");
        }
        
        return res;
    }
}
