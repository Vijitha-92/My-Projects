package businesspack.viewpack;

import businesspack.MainApp;
import businesspack.modelpack.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import myconnectionsql.Myconnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AppointmentEdit implements Initializable {
    Connection connection;
    Statement statement;
    ObservableList<String> list;
    ObservableList<String> list1;
    @FXML
    private ChoiceBox patientChoice;
    @FXML
    private ChoiceBox doctorChoice;
    @FXML
    private DatePicker appDate;
    @FXML
    private ChoiceBox starttime;
    @FXML
    private ChoiceBox endtime;
    private Stage dialogStage;
    private Appointment appointment;
    private boolean okClicked = false;
    @FXML
    private MainApp mainApp;


    public AppointmentEdit() {
        connection = Myconnection.doconnect();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
        patientChoice.setValue(appointment.getPfullName());
        doctorChoice.setValue(appointment.getDfullName());
        appDate.setValue(appointment.getBookedDate());
        appDate.setPromptText("dd.mm.yyyy");
        starttime.setValue(appointment.getsTime());
        endtime.setValue(appointment.geteTime());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        starttime.getItems().addAll("9.00", "9.30", "10.00", "10.30", "11.00", "11.30", "12.00", "12.30", "13.30", "14.00", "14.30", "15.00", "15.30", "16.00", "16.30");
        endtime.getItems().addAll( "9.30", "10.00", "10.30", "11.00", "11.30", "12.00", "12.30", "13.00", "14.00", "14.30", "15.00", "15.30", "16.00", "16.30", "17.00");
        String d = "select FullName from employees";
        try {
            ResultSet v = statement.executeQuery(d);
            while (v.next()) {
                String doc = v.getString("FullName");
                list = FXCollections.observableArrayList(doc);
                doctorChoice.getItems().addAll(list);
            }
        } catch (SQLException e) {
            System.out.println(" NOT FOUND" + e.toString());
        }
        String p = "select FullName from   patients";
        try {
            ResultSet v = statement.executeQuery(p);
            while (v.next()) {
                String patient = v.getString("FullName");
                list1 = FXCollections.observableArrayList(patient);
                patientChoice.getItems().addAll(list1);
            }
        } catch (SQLException e) {
            System.out.println(" NOT FOUND" + e.toString());

        }
    }


    public void handleOk(ActionEvent actionEvent) {
        if (isInputValid()) {
            //System.out.println(starttime.getSelectionModel().getSelectedItem().toString());
           // System.out.println(endtime.getSelectionModel().getSelectedItem().toString());
          //  System.out.println(compareVersions(starttime.getSelectionModel().getSelectedItem().toString(),endtime.getSelectionModel().getSelectedItem().toString()));
            if(compareVersions(starttime.getSelectionModel().getSelectedItem().toString(),endtime.getSelectionModel().getSelectedItem().toString()) == -1) {
                appointment.setPfullName(patientChoice.getSelectionModel().getSelectedItem().toString());
                appointment.setDfullName(doctorChoice.getSelectionModel().getSelectedItem().toString());
                appointment.setsTime(starttime.getSelectionModel().getSelectedItem().toString());
                appointment.seteTime(endtime.getSelectionModel().getSelectedItem().toString());
                appointment.setBookedDate(appDate.getValue());
                okClicked = true;
                dialogStage.close();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(dialogStage);
                    alert.setTitle("Alert Message");
                    alert.setHeaderText("Please book  Appointment again .");
                    alert.setContentText("End Time should be greater Start Time");
                    alert.showAndWait();
                     dialogStage.close();}
        }
    }
    public  int compareVersions(String vers1, String vers2) {
        double d1 =Double.valueOf(vers1);
        double d2 =Double.valueOf(vers2);
        int retval = Double.compare(d1, d2);
        return retval;

    }

    public void handleCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (patientChoice.getItems() == null) {
            errorMessage += "No valid Patient Name!\n";
        }
        if (doctorChoice.getItems() == null) {
            errorMessage += "No valid Doctor!\n";
        }

        if (appDate.getValue() == null) {
            errorMessage += "No valid date!\n";
        }
        if (starttime.getValue() == null) {
            errorMessage += "No valid start time!\n";
        }
        if (endtime.getValue() == null) {
            errorMessage += "No valid end time!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}
