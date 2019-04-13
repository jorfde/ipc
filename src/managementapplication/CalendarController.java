/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managementapplication;

import DBAccess.ClinicDBAccess;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Days;
import model.Doctor;
import model.Patient;
import static utils.SlotAppointmentsWeek.getAppointmentsWeek;
import utils.SlotWeek;

/**
 * FXML Controller class
 *
 * @author jorge
 */
public class CalendarController implements Initializable {
    @FXML
    private Button beforeWeek;
    @FXML
    private Label weekNumberLabel;
    @FXML
    private Button nextWeek;
    @FXML
    private TableView<SlotWeek> tableView;
    @FXML
    private TableColumn<SlotWeek, LocalTime> timeColumn;
    @FXML
    private TableColumn<SlotWeek, String> mondayColumn;
    @FXML
    private TableColumn<SlotWeek, String> tuesdayColumn;
    @FXML
    private TableColumn<SlotWeek, String> wednesdayColumn;
    @FXML
    private TableColumn<SlotWeek, String> thursdayColumn;
    @FXML
    private TableColumn<SlotWeek, String> fridayColumn;
    @FXML
    private TableColumn<SlotWeek, String> saturdayColumn;
    @FXML
    private TableColumn<SlotWeek, String> sundayColumn;
    @FXML
    private TextField timeField;
    @FXML
    private TextField dateField;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    
    private ArrayList<SlotWeek> doctorWeek;
    
    private int currentWeek;
    
    private int numDayNow;
    
    private ClinicDBAccess clinic;
    
    private int week;
    
    private IntegerProperty weekProperty;
    
    private ObservableList<SlotWeek> slots;
    
    private Doctor doctor;
    
    private ArrayList<Days> days;
    
    private LocalTime start;
    
    private LocalTime end;
    
    private String id;
    
    private ArrayList<Appointment> appointments;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         //Initialization of the columns of the TableView
        timeColumn.setCellValueFactory(new PropertyValueFactory<SlotWeek, LocalTime>("slot"));
        mondayColumn.setCellValueFactory(new PropertyValueFactory<SlotWeek, String>("mondayAvailability"));
        tuesdayColumn.setCellValueFactory(new PropertyValueFactory<SlotWeek, String>("tuesdayAvailability"));
        wednesdayColumn.setCellValueFactory(new PropertyValueFactory<SlotWeek, String>("wednesdayAvailability"));
        thursdayColumn.setCellValueFactory(new PropertyValueFactory<SlotWeek, String>("thursdayAvailability"));
        fridayColumn.setCellValueFactory(new PropertyValueFactory<SlotWeek, String>("fridayAvailability"));
        saturdayColumn.setCellValueFactory(new PropertyValueFactory<SlotWeek, String>("saturdayAvailability"));
        sundayColumn.setCellValueFactory(new PropertyValueFactory<SlotWeek, String>("sundayAvailability"));
        
        timeField.setEditable(false);
        dateField.setEditable(false);
        
        //Disable before week
        //beforeWeek.disableProperty().bind(Bindings.equal(currentWeek, weekProperty));
        
        //Disable next week
        //nextWeek.disableProperty().bind(Bindings.equal(12, weekProperty));
    }    

    @FXML
    private void buttonHandler(ActionEvent event) {
        switch(((Node) event.getSource()).getId()){
            case "beforeWeek":
                if(week != currentWeek) {
                    doctorWeek = getAppointmentsWeek(--week, days, start, 
                        end, appointments);
                    slots = FXCollections.observableList( doctorWeek );
                    tableView.setItems(slots);
                }
            
            case "nextWeek": 
                if(week != 12) {
                    doctorWeek = getAppointmentsWeek(++week, days, start, 
                            end, appointments);
                    slots = FXCollections.observableList( doctorWeek );
                    tableView.setItems(slots);
                }
        }
    }
    
    public void initData(Patient p, Doctor d) {
        clinic = ClinicDBAccess.getSingletonClinicDBAccess();
        
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        currentWeek = LocalDate.now().get(weekFields.weekOfWeekBasedYear());
        week = currentWeek;
        numDayNow =LocalDate.now().get(weekFields.dayOfWeek());
        
        days = d.getVisitDays();
        start = d.getVisitStartTime();
        end = d.getVisitEndTime();
        id = d.getIdentifier();
        appointments = clinic.getDoctorAppointments(id);
        
        doctorWeek = getAppointmentsWeek(currentWeek, days, start, 
                end, appointments);
        
        slots = FXCollections.observableList( doctorWeek );
        tableView.setItems(slots);
        
        //weekProperty.set(week);
    }
    
}
