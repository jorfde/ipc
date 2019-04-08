/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managementapplication;

import java.net.URL;
import java.time.LocalTime;
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
import model.Days;
import model.Doctor;
import model.ExaminationRoom;
import model.Person;

/**
 * FXML Controller class
 *
 * @author Stéphane
 */
public class DoctorDetailsController implements Initializable {
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
    @FXML
    private TextField visitDaysField;
    @FXML
    private Label visitDaysError;
    @FXML
    private TextField startingTimeField;
    @FXML
    private Label startingTimeError;
    @FXML
    private TextField endingTimeField;
    @FXML
    private Label endingTimeError;
    @FXML
    private TextField numberRoomField;
    @FXML
    private Label roomError;
    @FXML
    private TextField equipmentField;
    @FXML
    private Label equipmentError;

    private ArrayList<Doctor> doctors;
    
    private ObservableList<Person> persons;
    
    private int index;
    
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    
    private boolean error = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void initData(ObservableList<Person> persons, ArrayList<Doctor> doctors, int index){
        this.index = index;
        this.doctors = doctors;
        this.persons = persons;
        
        if (index >= 0){
            Doctor doctor = doctors.get(index);
            identifierField.setText(doctor.getIdentifier());
            identifierField.setEditable(false);
            nameField.setText(doctor.getName());
            nameField.setEditable(false);
            surnameField.setText(doctor.getSurname());
            surnameField.setEditable(false);
            telephoneField.setText(doctor.getTelephon());
            telephoneField.setEditable(false);
            ArrayList<Days> visitDays = doctor.getVisitDays();
            String res = "";
            for(int i=0;i<visitDays.size();i++) {
                res += visitDays.get(i).toString()+" ";
            } 
            visitDaysField.setText(res);
            visitDaysField.setEditable(false);
            startingTimeField.setText(""+doctor.getVisitStartTime());
            startingTimeField.setEditable(false);
            endingTimeField.setText(""+doctor.getVisitEndTime());
            endingTimeField.setEditable(false);
            numberRoomField.setText(""+doctor.getExaminationRoom().getIdentNumber());
            numberRoomField.setEditable(false);
            equipmentField.setText(doctor.getExaminationRoom().getEquipmentDescription());
            equipmentField.setEditable(false);
        }
    }    

    @FXML
    private void buttonHandler(ActionEvent event) {
        boolean errorIdentifier = false;
        boolean errorTelephone = false;
        boolean errorName = false;
        boolean errorSurname = false;
        boolean errorVisitDays = false;
        
        
        if(((Node) event.getSource()).getId().equals("okButton") && index == -1){
            String identifier = identifierField.getText();
            String name = nameField.getText();
            String surname = surnameField.getText();
            String telephone = telephoneField.getText();
            String startingTime = startingTimeField.getText();
            
            String contentText = "";
            
            if(!identifier.matches("[0-9A-Z]+")){
                errorIdentifier = true;
                identifierError.setText("Not valid");
                contentText+="You can only use numbers and capital letters in the IDENTIFIER field" + "\n";
            } else errorIdentifier = false;
            
            if(!name.matches("[a-zA-z]+")){
                nameError.setText("Not valid");
                contentText+="You can only use letters in the NAME field" + "\n";
            } else errorName = false;
            if(!surname.matches("[a-zA-z]+")){
                surnameError.setText("Not valid");
                contentText+="You can only use letters in the SURNAME field";
            }  else errorSurname = false;
            
            if(!telephone.matches("[0-9]+")){
                errorTelephone = true;
                telephoneError.setText("Not valid");
                contentText+="You can only use numbers TELEPHONE field" + "\n";
            } else errorTelephone = false;
            
            ArrayList<Days> days = new ArrayList<Days>();
            String visitDays = visitDaysField.getText();
            String day = "";
            for(int i = 0;i < visitDays.length() && errorVisitDays;i++){
                char c = visitDays.charAt(i);
                if (c == ','){
                    switch(day){
                        case "Monday": days.add(Days.Monday);break;
                        case "Tuesday": days.add(Days.Tuesday);break;
                        case "Wednesday": days.add(Days.Wednesday);break;
                        case "Thursday": days.add(Days.Thursday);break;
                        case "Friday": days.add(Days.Friday);break;
                        case "Saturday": days.add(Days.Saturday);break;
                        case "Sunday": days.add(Days.Sunday);break;    
                        default: errorVisitDays = true; 
                            visitDaysError.setText("Not valid");
                            contentText+="You can't use spaces and the days must be separeted by commas" + "\n" +
                                    "Example: Monday,Tuesday,Wednesday" + "\n";
                    }
                }
            }
            error = errorIdentifier || errorTelephone || errorName || errorSurname || errorVisitDays;
            if(error){
                errorAlert.setContentText(contentText);
                errorAlert.showAndWait();
            }
            
            
            
            
            
            
            
            ExaminationRoom e = new ExaminationRoom(Integer.parseInt(numberRoomField.getText()), equipmentField.getText());
            Doctor d = new Doctor(e, null, LocalTime.parse(startingTimeField.getText()), LocalTime.parse(endingTimeField.getText()),
                identifierField.getText(), nameField.getText(), surnameField.getText(), telephoneField.getText(), null);
            d.setVisitDays(days);
            doctors.add(d);
            persons.add(d);
            
            alert.setTitle("Information");
            alert.setHeaderText("You have added a doctor");
            alert.setContentText("The doctor was succesfully added to the data base.");
            
            alert.showAndWait();
        } 
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
    
}
