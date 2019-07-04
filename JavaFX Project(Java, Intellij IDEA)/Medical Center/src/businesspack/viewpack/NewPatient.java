package businesspack.viewpack;

import businesspack.MainApp;
import businesspack.modelpack.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import myconnectionsql.Myconnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class NewPatient {

    Connection connection;
    Statement statement;
    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, String> firstNameColumn;
    @FXML
    private TableColumn<Patient, String> lastNameColumn;
    private Stage dialogStage;
    @FXML
    private TextField firstNameLabel;
    @FXML
    private TextField lastNameLabel;
    @FXML
    private TextField contactNo;
    @FXML
    private TextField medicare;
    @FXML
    private TextField patientid;
    @FXML
    private TextArea address;
    @FXML
    private RadioButton malebtn;
    @FXML
    private RadioButton femalebtn;
    @FXML
    private RadioButton othersbtn;
    @FXML
    private DatePicker dateOfBrithDp;
    @FXML
    private ToggleGroup gender;
    private MainApp mainApp;

    public NewPatient() {
        connection = Myconnection.doconnect();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        patientTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPatientDetails(newValue));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        patientTable.setItems(mainApp.getPatientData());
    }

    private void showPatientDetails(Patient patient) {
        if (patient != null) {
            firstNameLabel.setText(patient.getFirstName());
            lastNameLabel.setText(patient.getLastName());
            address.setText(patient.getAddress());
            contactNo.setText(patient.getMobileNo());
            medicare.setText(patient.getMediCare());
            othersbtn.setToggleGroup(gender);
            femalebtn.setToggleGroup(gender);
            malebtn.setToggleGroup(gender);
            String s = patient.getGender();
            if (s.equals("M") || s.equals("m"))
                gender.selectToggle(malebtn);
            else if (s.equals("F") || s.equals("f"))
                gender.selectToggle(femalebtn);
            else
                gender.selectToggle(othersbtn);
            patientid.setText(Integer.toString(patient.getPatientid()));
            dateOfBrithDp.setValue(patient.getDateOfBirth());
        } else {
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            medicare.setText("");
            address.setText("");
            contactNo.setText("");
            dateOfBrithDp.setValue(null);
            patientid.setText("");
            othersbtn.setToggleGroup(gender);
            femalebtn.setToggleGroup(gender);
            malebtn.setToggleGroup(gender);
            gender.selectToggle(null);
        }
    }

    @FXML
    private void handleNewPatient() {
        Patient tempPatient = new Patient();
        boolean okClicked;
        if (mainApp.showPersonEditDialog(tempPatient)) okClicked = true;
        else okClicked = false;
        if (okClicked) {
            String f = tempPatient.getFirstName();
            String l = tempPatient.getLastName();
            String fl = f + " " + l;
            String mobile = tempPatient.getMobileNo();
            String medicare = tempPatient.getMediCare();
            String address = tempPatient.getAddress();
            String MaxId = "select max(PatientId) PId from patients";
            String gender = tempPatient.getGender();
            LocalDate date = tempPatient.getDateOfBirth();
            try {
                ResultSet v = statement.executeQuery(MaxId);
                if (v.next()) {
                    int sm = v.getInt("PId") + 1;
                    tempPatient.setPatientid(sm);
                    String sql = "INSERT INTO patients VALUES( '" + sm + "', '" + f + "' , '" + l + " ', '" + fl + " ','" + date + "' , '" + mobile + "', '" + address + "','" + gender + "','" + medicare + "')";
                    statement.execute(sql);
                }
                mainApp.getPatientData().add(tempPatient);
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Alert Message");
                alert.setHeaderText("Please add the Patient again .");
                alert.setContentText(e.toString());
                alert.showAndWait();
            }

        }
    }

    @FXML
    private void handleEditPatient() {
        Patient patient = patientTable.getSelectionModel().getSelectedItem();
        if (patient != null) {

            boolean okClicked = mainApp.showPersonEditDialog(patient);
            if (okClicked) {
                try {
                    String f = patient.getFirstName();
                    String l = patient.getLastName();
                    String fl = f + " " + l;
                    String m = patient.getMobileNo();
                    String address = patient.getAddress();
                    String medicare = patient.getMediCare();
                    int p = patient.getPatientid();
                    String gender = patient.getGender();
                    LocalDate date = patient.getDateOfBirth();
                    String sql = "UPDATE patients SET FirstName = '" + f + "' , LastName = '" + l + " ',FullName = '" + fl + "' ,`DateofBirth` = '" + date + "', `Mobile` = '" + m + "', `Address` = '" + address + "', `Gender` = '" + gender + "' , `MedicareNo` = '" + medicare + "' WHERE `patients`.`PatientId` = '" + p + "';";
                    statement.executeUpdate(sql);
                } catch (SQLException e) {
                    System.out.println(" NOT FOUND" + e.toString());
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(mainApp.getPrimaryStage());
                    alert.setTitle("Alert Message");
                    alert.setHeaderText("Please edit the Patient again ."+e.toString());
                    alert.setContentText(e.toString());
                    alert.showAndWait();
                }
                showPatientDetails(patient);
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeletePatient(ActionEvent actionEvent) {
        Patient patient = patientTable.getSelectionModel().getSelectedItem();
        int selectedIndex = patientTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            int p = patient.getPatientid();
            String count = "SELECT  count(*) num FROM patients WHERE PatientId = '" + p + "'";
            String sql = "DELETE  FROM  patients  WHERE  patients. PatientId = '" + p + "'";
            String sql1 = "DELETE  FROM appointments WHERE PatientId = '" + p + "'";
            try {
                ResultSet vse = statement.executeQuery(count);
                if (vse.next()) {
                    int sm = vse.getInt("num");
                    if (sm > 0)
                        statement.executeUpdate(sql);
                    statement.executeUpdate(sql1);
                    patientTable.getItems().remove(selectedIndex);
                }
            } catch (SQLException e) {
                System.out.println(" NOT FOUND" + e.toString());
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Patient Selected");
            alert.setContentText("Please select a Patient in the table.");
            alert.showAndWait();
        }
    }

    public void handleFirstpage(ActionEvent actionEvent) {
        mainApp.showOverview();
    }
}
