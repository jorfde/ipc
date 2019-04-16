/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managementapplication;

import DBAccess.ClinicDBAccess;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
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
 * @author jorge & steph
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
    
    private ObservableList<SlotWeek> slots;
    
    private Doctor doctor;
    
    private ArrayList<Days> days;
    
    private LocalTime start;
    
    private LocalTime end;
    
    private String id;
    
    private ArrayList<Appointment> appointments;
    
    private LocalDate date;
    
    private int weekDay;
    
    private LocalTime time;
    
    private boolean dateValid = false;
    
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    
    AddAppointmentController controller;
    
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
        
        //Not editable fields
        timeField.setEditable(false);
        dateField.setEditable(false);
        
        //Selection by cells
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        
        //Add a listener to the cell of the TableView
        ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells() ;
        selectedCells.addListener((ListChangeListener.Change<? extends TablePosition> change) -> {
            if (selectedCells.size() > 0) {
                TablePosition selectedCell = selectedCells.get(0);
                TableColumn column = selectedCell.getTableColumn();
                int columnIndex = selectedCell.getColumn();
                int rowIndex = selectedCell.getRow();
                
                time = slots.get(rowIndex).getSlot();
                timeField.setText(time.toString());
                
                if(columnIndex > 0 && !column.getCellData(rowIndex).equals("Not Available")){
                    int sumDays = columnIndex - weekDay;
                    weekDay = columnIndex;
                    if(sumDays > 0)
                        date = date.plusDays(sumDays);
                    if(sumDays < 0)
                        date = date.minusDays(sumDays * -1);
                                                                                                                        
                    dateField.setText(date.toString());
                    
                    if(date.isAfter(LocalDate.now())){
                        dateValid = true;
                    } else {
                        dateValid = false;
                    }
                } else {
                    dateField.setText("");
                }  
            }
        });
    }    

    @FXML
    private void buttonHandler(ActionEvent event) throws IOException {
        switch(((Node) event.getSource()).getId()){
            case "beforeWeek":
                if(week != currentWeek) {
                    doctorWeek = getAppointmentsWeek(--week, days, start, 
                        end, appointments);
                    slots = FXCollections.observableList( doctorWeek );
                    tableView.setItems(slots);
                    date = date.minusWeeks(1);
                }
            break;
            
            case "nextWeek": 
                doctorWeek = getAppointmentsWeek(++week, days, start, 
                        end, appointments);
                slots = FXCollections.observableList( doctorWeek );
                tableView.setItems(slots);
                date = date.plusWeeks(1);
                break;
                
            case "okButton":
                if(dateValid) {
                    controller.getData(LocalDateTime.of(date, time));
                    ((Node) event.getSource()).getScene().getWindow().hide();
                } else {
                    errorAlert.setContentText("Please select a free day");
                    errorAlert.showAndWait();
                }
                break;
                
            case "cancelButton": ((Node) event.getSource()).getScene().getWindow().hide();
        }
        
        weekNumberLabel.setText("Week of the year: "+ week +" Month: " + date.getMonth() +  " Year: " + date.getYear() );
    }
    
    public void initData(Patient p, Doctor d, AddAppointmentController before) {
        controller = before;
        
        clinic = ClinicDBAccess.getSingletonClinicDBAccess();
        
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        currentWeek = LocalDate.now().get(weekFields.weekOfWeekBasedYear());
        week = currentWeek;
        numDayNow = LocalDate.now().get(weekFields.dayOfWeek());
        
        days = d.getVisitDays();
        start = d.getVisitStartTime();
        end = d.getVisitEndTime();
        id = d.getIdentifier();
        appointments = clinic.getDoctorAppointments(id);
        
        doctorWeek = getAppointmentsWeek(currentWeek, days, start, 
                end, appointments);
        
        slots = FXCollections.observableList( doctorWeek );
        tableView.setItems(slots);
        
        date = LocalDate.now();
        
        weekNumberLabel.setText("Week of the year: "+ week +" Month: " + date.getMonth() +  " Year: " + date.getYear() );
        
        weekDay = numDayNow;
    }
}
