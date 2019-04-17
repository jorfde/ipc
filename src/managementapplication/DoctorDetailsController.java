/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managementapplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.Days;
import model.Doctor;
import model.ExaminationRoom;
import model.Person;

/**
 * FXML Controller class
 *
 * @author Stéphane & Jorge
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
    @FXML
    private Button searchButton;
    @FXML
    private ImageView imageView;

    private ArrayList<Doctor> doctors;
    
    private ObservableList<Person> persons;
    
    private int index;
    
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    
    private boolean error = false;
    
    private String contentText = "";

    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void initData(ObservableList<Person> persons, ArrayList<Doctor> doctors, int index) throws FileNotFoundException{
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
            int size = visitDays.size();
            for(int i=0;i<size;i++) {
                res += visitDays.get(i).toString();
                if(i != size-1) res += ", ";
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
            Image image = doctor.getPhoto();
            imageView.imageProperty().setValue(image);
            searchButton.setVisible(false);
        }
    }    

    @FXML
    private void buttonHandler(ActionEvent event) throws FileNotFoundException {
        boolean errorIdentifier = false;
        boolean errorTelephone = false;
        boolean errorName = false;
        boolean errorSurname = false;
        boolean errorVisitDays = false;
        boolean errorStartingTime = false;
        boolean errorEndingTime = false;
        boolean errorNumberRoom = false;  
        
        switch(((Node) event.getSource()).getId()){
            
            case "searchButton":
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open file");    
                fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg") );    
                File selectedFile = fileChooser.showOpenDialog( ((Node)event.getSource()).getScene().getWindow());    
                if (selectedFile != null) {
                    String path = selectedFile.getAbsolutePath();
                    Image image = new Image ( new FileInputStream (path));
                    imageView.imageProperty().setValue( image );
                } break;
            
            case "okButton":
                
                if(index == -1){
                    String identifier = identifierField.getText();
                    String name = nameField.getText();
                    String surname = surnameField.getText();
                    String telephone = telephoneField.getText();
                    String startingTime = startingTimeField.getText();
                    String endingTime = endingTimeField.getText();
                    String numberRoom = numberRoomField.getText();

                    errorIdentifier = checkField(identifier, "[0-9A-Z]+", 
                            "You can only use numbers and capital letters in the IDENTIFIER field" + "\n" + "\n", identifierError);

                    errorName = checkField(name, "[^0-9]+", "You can only use letters in the NAME field" + "\n" + "\n", nameError);;

                    errorSurname = checkField(surname, "[^0-9]+", "You can only use letters in the SURNAME field" + "\n" + "\n", surnameError);

                    errorTelephone = checkField(telephone, "[0-9]+", "You can only use numbers in the TELEPHONE field" + "\n" + "\n", telephoneError);

                    ArrayList<Days> days = new ArrayList<Days>();
                    String visitDays = visitDaysField.getText() + " ";
                    String day = "";
                    for(int i = 0;i < visitDays.length() && !errorVisitDays;i++){
                        char c = visitDays.charAt(i);
                        if (c != ',' && c != ' ')
                            day += "" + c;
                        else {
                            switch(day.toLowerCase()){
                                case "monday": days.add(Days.Monday);errorVisitDays = false;break;
                                case "tuesday": days.add(Days.Tuesday);errorVisitDays = false;break;
                                case "wednesday": days.add(Days.Wednesday);errorVisitDays = false;break;
                                case "thursday": days.add(Days.Thursday);errorVisitDays = false;break;
                                case "friday": days.add(Days.Friday);errorVisitDays = false;break;
                                case "saturday": days.add(Days.Saturday);errorVisitDays = false;break;
                                case "sunday": days.add(Days.Sunday);errorVisitDays = false;break;    
                                default: 
                                    errorVisitDays = true; 
                                    visitDaysError.setText("Not valid");
                                    contentText+="The days must be separeted by commas or spaces and must be valid" + "\n" +
                                            "Example: Monday, Tuesday, Wednesday" + "\n" + "\n";
                            }
                            day="";
                        }
                    }

                    errorStartingTime = checkField(startingTime, "([0-1][0-9]|2[0-3]):([03]0|[14]5)",
                            "You can only use times ended in 00, 15, 30, 45 in the STARTING TIME field" + "\n" + "\n", startingTimeError);

                    errorEndingTime = checkField(endingTime, "([0-1][0-9]|2[0-3]):([03]0|[14]5)", 
                            "You can only use times ended in 00, 15, 30, 45 in the ENDING TIME field" + "\n" + "\n", endingTimeError);
                    if(!errorEndingTime && !errorStartingTime && !LocalTime.parse(endingTime).isAfter(LocalTime.parse(startingTime))){
                        errorEndingTime = false;
                        contentText += "The ENDING TIME must be after the STARTING TIME" + "\n" + "\n"; 
                        endingTimeError.setText("Not valid");
                    }


                    errorNumberRoom = checkField(numberRoom, "[0-9]+", "You can only use numbers in the NºROOM field" + "\n" + "\n", roomError);



                    error = errorIdentifier || errorTelephone || errorName || errorSurname || errorVisitDays || errorStartingTime ||
                            errorEndingTime || errorNumberRoom;

                    if(error){
                        errorAlert.setContentText(contentText);
                        errorAlert.showAndWait();
                        contentText = "";
                    } else {
                        ExaminationRoom e = new ExaminationRoom(Integer.parseInt(numberRoom), equipmentField.getText());
                        Doctor d = new Doctor(e, null, LocalTime.parse(startingTime), LocalTime.parse(endingTime),
                            identifier, name, surname, telephone, imageView.getImage());
                        d.setVisitDays(days);
                        doctors.add(d);
                        persons.add(d);

                        alert.setTitle("Information");
                        alert.setHeaderText("You have added a doctor");
                        alert.setContentText("The doctor was succesfully added to the data base.");

                        alert.showAndWait();
                        exit(event);
                    }
                } else {
                    exit(event);
                }
            
            case "cancelButton":
                exit(event);
                break;              
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
    
    private void exit(ActionEvent event){
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
    
}
