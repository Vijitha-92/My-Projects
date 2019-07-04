package businesspack.viewpack;

import businesspack.MainApp;
import businesspack.modelpack.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import myconnectionsql.Myconnection;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AppointmentControl implements Initializable {

    Connection connection;
    Statement statement;
    ObservableList<String> list;
    ObservableList<String> list1;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> patientNameColumn;
    @FXML
    private TableColumn<Appointment, String> doctorNameColumn;
    @FXML
    private TableColumn<Appointment, String> starttimeColumn;
    @FXML
    private TableColumn<Appointment, String> endtimeColumn;
    @FXML
    private TableColumn<Appointment, LocalDate> appdateColumn;
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
    @FXML
    private MainApp mainApp;

    public AppointmentControl() {
        connection = Myconnection.doconnect();
        try {
            statement = connection.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        patientNameColumn.setCellValueFactory(cellData -> cellData.getValue().pfullNameProperty());
        doctorNameColumn.setCellValueFactory(cellData -> cellData.getValue().dfullNameProperty());
        appdateColumn.setCellValueFactory(cellData -> cellData.getValue().bookedDateProperty());
        starttimeColumn.setCellValueFactory(cellData ->cellData.getValue().sTimeProperty());
        endtimeColumn.setCellValueFactory(cellData -> cellData.getValue().eTimeProperty());
        appointmentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showAppointmentDetails(newValue));
    }

    private void showAppointmentDetails(Appointment appointment) {
        if (appointment != null) {
            patientChoice.setValue(appointment.getPfullName());
            doctorChoice.setValue(appointment.getDfullName());
            appDate.setValue(appointment.getBookedDate());
            starttime.setValue(appointment.getsTime());
            endtime.setValue(appointment.geteTime());
        } else {
            patientChoice.setValue("");
            doctorChoice.setValue("");
            appDate.setValue(null);
           starttime.setValue("");
            endtime.setValue("");
        }
    }

    public void handleAdd(ActionEvent actionEvent) {
        Appointment appointment = new Appointment();
        boolean okClicked;
        if (mainApp.showAppointmentEditDialog(appointment)) okClicked = true;
        else okClicked = false;
        if (okClicked) {
            String d = appointment.getDfullName();
            String did = "select EmployeeId  FROM employees where FullName = '" + d + "'";
            String p = appointment.getPfullName();
            String pid = "select PatientId from patients where FullName = '" + p + "'";
            LocalDate date = appointment.getBookedDate();
            String starttime = appointment.getsTime();
            String endtime = appointment.geteTime();
                try {
                String MaxId = "select max(AppId) EmpId FROM appointments";
                ResultSet v = statement.executeQuery(MaxId);
                if (v.next()) {
                    int sm = v.getInt("EmpId") + 1;
                    ResultSet v1 = statement.executeQuery(did);
                    if (v1.next()) {
                        int docid = v1.getInt("EmployeeId");
                        ResultSet v2 = statement.executeQuery(pid);
                        if (v2.next()) {
                            int patid = v2.getInt("PatientId");
                            appointment.setAppId(sm);
                            String time = "select distinct 'Y' from appointments where BookedDate = '" + date + "' and (('" + starttime + "' > BookedTime and '" + starttime + "' < EndTime )OR ('"+ endtime + "' >BookedTime and '" + endtime + "' <= EndTime )OR('"+starttime+"'<BookedTime and '"+endtime+"'>Endtime))and (EmployeeId = '" + docid + "'OR PatientId = '" + patid + "')";
                            ResultSet rs1 = statement.executeQuery(time);
                            if (rs1.next()) {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.initOwner(mainApp.getPrimaryStage());
                                alert.setTitle("Alert Message");
                                alert.setContentText("Please book  Appointment again ,this slot is already booked .");
                                alert.showAndWait();
                            } else {
                               // System.out.println("else");
                                String sql = "INSERT INTO appointments VALUES( '" + sm + "' , '" + docid + " ','" + patid + "','" + date + "' ,'" + starttime + "','" + endtime + "')";
                                statement.execute(sql);
                                mainApp.getAppointmentData().add(appointment);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println(" NOT FOUND" + e.toString());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Alert Message");
                alert.setHeaderText("Please book  Appointment again ,this slot is already booked.");
                    System.out.println(e.toString());
                alert.showAndWait();
            }}

    }
    public void handleDelete(ActionEvent actionEvent) {
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
        int selectedIndex = appointmentTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            try {
                int appid = appointment.getAppId();
                String sql = "DELETE  FROM appointments WHERE AppId = '" + appid + "'";
                statement.executeUpdate(sql);
                appointmentTable.getItems().remove(selectedIndex);
            } catch (SQLException e) {
                System.out.println(" NOT FOUND" + e.toString());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select a Appointment in the table.");
            alert.showAndWait();
        }
    }
    public void handleEdit(ActionEvent actionEvent) {

        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (appointment != null) {
            boolean okClicked = mainApp.showAppointmentEditDialog(appointment);
            if (okClicked) {
                String d = appointment.getDfullName();
                String p = appointment.getPfullName();
                LocalDate date = appointment.getBookedDate();
                String starttime = appointment.getsTime();
                String endtime = appointment.geteTime();
                int id = appointment.getAppId();
                    String did = "select EmployeeId  FROM employees where FullName = '" + d + "'";
                    String pid = "select PatientId from patients where FullName = '" + p + "'";
                    try {

                        ResultSet v1 = statement.executeQuery(did);
                        if (v1.next()) {
                            int docid = v1.getInt("EmployeeId");
                            ResultSet v2 = statement.executeQuery(pid);
                            if (v2.next()) {
                                int patid = v2.getInt("PatientId");
                                String time = "select distinct 'Y' from appointments where BookedDate = '" + date + "' and (('" + starttime + "' > BookedTime and '" + starttime + "' < EndTime )OR ('"+ endtime + "' >BookedTime and '" + endtime + "' <= EndTime )OR('"+starttime+"'<BookedTime and '"+endtime+"'>Endtime))and (EmployeeId = '" + docid + "'OR PatientId = '" + patid + "') and( AppId <>'"+id+"')";
                                ResultSet rs1 = statement.executeQuery(time);
                                if (rs1.next()) {
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.initOwner(mainApp.getPrimaryStage());
                                    alert.setTitle("Alert Message");
                                    alert.setContentText("Please edit  Appointment again ,this slot is already booked .");
                                    alert.showAndWait();
                                    String s = "select * from appointments WHERE AppId = '" + id + "'";
                                    ResultSet v3 = statement.executeQuery(s);
                                    if (v3.next()) {
                                        int idp = v3.getInt("PatientId");
                                        int idd = v3.getInt("EmployeeId");
                                        Date date1 = v3.getDate("BookedDate");
                                        appointment.setBookedDate(date1.toLocalDate());
                                        appointment.setsTime(v3.getString("BookedTime"));
                                        appointment.seteTime(v3.getString("EndTime"));
                                        String ip = "SELECT FullName FROM patients where PatientId = '" + idp + "'";
                                        ResultSet rs2 = statement.executeQuery(ip);
                                        if (rs2.next()) {
                                            appointment.setPfullName(rs2.getString("FullName"));
                                        }
                                        String doc = "SELECT FullName FROM employees where EmployeeId = '" + idd + "'";
                                        ResultSet s2 = statement.executeQuery(doc);
                                        if (s2.next()) {
                                            appointment.setDfullName(s2.getString("FullName"));}
                                    }

                                } else {
                                    String sql = "UPDATE appointments set  EmployeeId ='" + docid + "',PatientId = '" + patid + "',BookedDate = '" + date + "',BookedTime = '" + starttime + "',EndTime = '" + endtime + "' WHERE AppId = '" + id + "'";
                                    statement.executeUpdate(sql);
                                    showAppointmentDetails(appointment);
                                }
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println(" NOT FOUND" + e.toString());
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.initOwner(mainApp.getPrimaryStage());
                        alert.setTitle("Alert Message");
                        alert.setHeaderText("Please edit  Appointment again ,this slot is already booked.");
                        alert.setContentText(e.toString());
                        alert.showAndWait();

                        try {
                            String s = "select * from appointments WHERE AppId = '" + id + "'";
                            ResultSet v3 = statement.executeQuery(s);
                            if (v3.next()) {
                                int idp = v3.getInt("PatientId");
                                int idd = v3.getInt("EmployeeId");
                                Date date1 = v3.getDate("BookedDate");
                                appointment.setBookedDate(date1.toLocalDate());
                                appointment.setsTime(v3.getString("BookedTime"));
                                appointment.seteTime(v3.getString("EndTime"));
                                String ip = "SELECT FullName FROM patients where PatientId = '" + idp + "'";
                                ResultSet rs2 = statement.executeQuery(ip);
                                if (rs2.next()) {
                                    appointment.setPfullName(rs2.getString("FullName"));
                                }
                                String doc = "SELECT FullName FROM employees where EmployeeId = '" + idd + "'";
                                ResultSet s2 = statement.executeQuery(doc);
                                if (s2.next()) {
                                    appointment.setDfullName(s2.getString("FullName"));}

                            }

                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
               }
            }
            else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
        }
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        appointmentTable.setItems(mainApp.getAppointmentData());
    }

    public void handlefirstpage(ActionEvent actionEvent) {
        mainApp.showOverview();
    }
}