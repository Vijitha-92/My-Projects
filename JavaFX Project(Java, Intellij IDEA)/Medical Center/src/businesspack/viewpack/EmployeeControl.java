package businesspack.viewpack;

import businesspack.MainApp;
import businesspack.modelpack.Doctors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import myconnectionsql.Myconnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class EmployeeControl {
    @FXML
    Connection connection;
    Statement statement;
    @FXML
    private TableView<Doctors> doctorTable;
    @FXML
    private TableColumn<Doctors, String> firstNameColumn;
    @FXML
    private TableColumn<Doctors, String> lastNameColumn;
    @FXML
    private TextField firstNameLabel;
    @FXML
    private TextField lastNameLabel;
    @FXML
    private TextField contactNo;
    @FXML
    private TextField emptype;
    @FXML
    private TextArea address;
    @FXML
    private TextField workingdays;
    @FXML
    private ToggleGroup gender;
    @FXML
    private RadioButton malebtn;
    @FXML
    private RadioButton femalebtn;
    @FXML
    private RadioButton othersbtn;
    @FXML
    private DatePicker dateOfBrithDp;
    private MainApp mainApp;

    public EmployeeControl() {
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
        doctorTable.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> showEmployeeDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        doctorTable.setItems(mainApp.getDoctorsData());
    }

    private void showEmployeeDetails(Doctors doctors) {
        if (doctors != null) {
            firstNameLabel.setText(doctors.getFirstName());
            lastNameLabel.setText(doctors.getLastName());
            address.setText(doctors.getAddress());
            contactNo.setText(doctors.getMobileNo());
            emptype.setText(doctors.getEmptype());
            workingdays.setText(Integer.toString(doctors.getWorkingDays()));
            othersbtn.setToggleGroup(gender);
            femalebtn.setToggleGroup(gender);
            malebtn.setToggleGroup(gender);
            String s = doctors.getGender();
            if (s.equals("M") || s.equals("m"))
                gender.selectToggle(malebtn);
            else if (s.equals("F") || s.equals("f"))
                gender.selectToggle(femalebtn);
            else
                gender.selectToggle(othersbtn);

            emptype.setText(doctors.getEmptype());
            dateOfBrithDp.setValue(doctors.getDateOfBirth());

        } else {
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            address.setText("");
            contactNo.setText("");
            emptype.setText("");
            workingdays.setText("");
            dateOfBrithDp.setValue(null);
            othersbtn.setToggleGroup(gender);
            femalebtn.setToggleGroup(gender);
            malebtn.setToggleGroup(gender);
            gender.selectToggle(null);
        }
    }

    public void handleAddemp(ActionEvent actionEvent) {
        Doctors tempEmployee = new Doctors();
        boolean okClicked;
        if (mainApp.showEmployeeEditDialog(tempEmployee)) okClicked = true;
        else okClicked = false;
        if (okClicked) {
            String f = tempEmployee.getFirstName();
            String l = tempEmployee.getLastName();
            String fl = f + " " + l;
            String m = tempEmployee.getMobileNo();
            String address = tempEmployee.getAddress();
            int wd = tempEmployee.getWorkingDays();
            String gender = tempEmployee.getGender();
            String type = tempEmployee.getEmptype();
            LocalDate date = tempEmployee.getDateOfBirth();
            try {
                String MaxId = "select max(EmployeeId) EmpId FROM employees";
                ResultSet v = statement.executeQuery(MaxId);
                if (v.next()) {
                    int sm = v.getInt("EmpId") + 1;
                    tempEmployee.setEmpId(sm);
                    String sql = "INSERT INTO employees VALUES( '" + sm + "' , '" + f + " ','" + l + "','" + fl + "','" + m + "' ,'" + address + "','" + gender + "','" + date + "','1','" + wd + "','" + type + "')";
                    boolean b = statement.execute(sql);
                    mainApp.getDoctorsData().add(new Doctors(f, l, sm, m, address, gender, date, wd));
                }
            } catch (SQLException e) {
                System.out.println(" NOT FOUND" + e.toString());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Alert Message");
                alert.setHeaderText("Please add the Doctor again ."+e.toString());
                alert.setContentText(e.toString());
                alert.showAndWait();

           }
        }
    }

    public void handleEditemp(ActionEvent actionEvent) {
        Doctors doctor = doctorTable.getSelectionModel().getSelectedItem();
        if (doctor != null) {
            boolean okClicked = mainApp.showEmployeeEditDialog(doctor);
            if (okClicked) {
                String f = doctor.getFirstName();
                String l = doctor.getLastName();
                String fl = f + " " + l;
                String m = doctor.getMobileNo();
                String address = doctor.getAddress();
                int wd = doctor.getWorkingDays();
                String gender = doctor.getGender();
                String type = doctor.getEmptype();
                int tid = doctor.getEmpId();
                LocalDate date = doctor.getDateOfBirth();
                try {
                    String sql = "UPDATE employees SET `FirstName` = '" + f + "' , `LastName` = '" + l + " ',FullName = '" + fl + "',`Mobile` = '" + m + "',`Address` = '" + address + "',Gender = '" + gender + "', DateofBirth = '" + date + "', Workingdays = '" + wd + "' WHERE EmployeeId = '" + tid + "'";
                    statement.executeUpdate(sql);
                } catch (SQLException e) {
                    System.out.println(" NOT FOUND" + e.toString());
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(mainApp.getPrimaryStage());
                    alert.setTitle("Alert Message");
                    alert.setHeaderText("Please edit the Doctor again ."+e.toString());
                    alert.setContentText(e.toString());
                    alert.showAndWait();
                }
                showEmployeeDetails(doctor);
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

    public void handleDeleteemp(ActionEvent actionEvent) {
        int selectedIndex = doctorTable.getSelectionModel().getSelectedIndex();
        Doctors doctors = doctorTable.getSelectionModel().getSelectedItem();
        if (selectedIndex >= 0) {
            try {
                int id = doctors.getEmpId();
                String count = "SELECT  count(*) num FROM employees WHERE EmployeeId= '" + id + "'";

                String sql = "DELETE  FROM employees WHERE EmployeeId= '" + id + "'";
                String sql1 = "DELETE  FROM appointments WHERE EmployeeId = '" + id + "'";
                ResultSet vse = statement.executeQuery(count);
                if (vse.next()) {
                    int sm = vse.getInt("num");
                    if (sm > 0)
                        statement.executeUpdate(sql);

                    statement.executeUpdate(sql1);
                    doctorTable.getItems().remove(selectedIndex);
                }
            } catch (SQLException e) {
                System.out.println(" NOT FOUND" + e.toString());
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Employee Selected");
            alert.setContentText("Please select a Employee in the table.");
            alert.showAndWait();
        }
    }

    public void handlefirstpage(ActionEvent actionEvent) {
        mainApp.showOverview();
    }
}

