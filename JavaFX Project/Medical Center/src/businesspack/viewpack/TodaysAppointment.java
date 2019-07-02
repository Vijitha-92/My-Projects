package businesspack.viewpack;

import businesspack.MainApp;
import businesspack.modelpack.Appointment;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import myconnectionsql.Myconnection;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TodaysAppointment implements Initializable {
    Connection connection;
    Statement statement;
    ArrayList<Integer> appidList = new ArrayList<Integer>();
    ArrayList<Integer> patidList = new ArrayList<Integer>();
    ArrayList<Integer> docidList = new ArrayList<Integer>();
    ArrayList<LocalDate> dateList = new ArrayList<LocalDate>();
    ArrayList<String> timeList = new ArrayList<String>();
    ArrayList<String> endtimeList = new ArrayList<String>();
    ArrayList<String> patientList = new ArrayList<String>();
    ArrayList<String> doctorList = new ArrayList<String>();
    @FXML
    Circle circle, circle2;
    @FXML
    Sphere sphere;
    @FXML
    private TableView<Appointment> appointmenttoTable;
    @FXML
    private TableColumn<Appointment, String> patientNameColumn;
    @FXML
    private TableColumn<Appointment, String> doctorNameColumn;
    @FXML
    private TableColumn<Appointment, String> timeColumn;
    @FXML
    private TableColumn<Appointment, String> endtimeColumn;
    @FXML
    private TableColumn<Appointment, LocalDate> appdateColumn;
    private MainApp mainApp;
    private int z;

    public TodaysAppointment() {
        connection = Myconnection.doconnect();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(Duration.seconds(4));
        translateTransition.setNode(circle);
        translateTransition.setToY(-300);
        translateTransition.setToX(100);
        translateTransition.setAutoReverse(true);
        translateTransition.setCycleCount(5);
        translateTransition.play();
        TranslateTransition translate = new TranslateTransition();
        translate.setDuration(Duration.seconds(4));
        translate.setNode(circle2);
        translate.setToY(400);
        translate.setToX(-250);
        translate.setAutoReverse(true);
        translate.setCycleCount(5);
        translate.play();
        TranslateTransition translat = new TranslateTransition();
        translat.setDuration(Duration.seconds(4));
        translat.setNode(sphere);
        translat.setToY(400);
        translat.setToX(-250);
        translat.setAutoReverse(true);
        translat.setCycleCount(5);
        translate.play();
        patientNameColumn.setCellValueFactory(cellData -> cellData.getValue().pfullNameProperty());
        doctorNameColumn.setCellValueFactory(cellData -> cellData.getValue().dfullNameProperty());
        appdateColumn.setCellValueFactory(cellData -> cellData.getValue().bookedDateProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().sTimeProperty());
        endtimeColumn.setCellValueFactory(cellData -> cellData.getValue().eTimeProperty());
        try {
            String count = "select count(*) num FROM appointments where bookeddate = CURRENT_DATE";
            ResultSet vse = statement.executeQuery(count);
            if (vse.next()) {
                int sm = vse.getInt("num");
                setZ(sm);
            }
            String s1 = "SELECT   * FROM appointments where bookeddate = CURRENT_DATE";
            ResultSet rs1 = statement.executeQuery(s1);
            for (int i = 0; i < z; i++) {
                if (rs1.next()) {
                    appidList.add(rs1.getInt("AppId"));
                    docidList.add(rs1.getInt("EmployeeId"));
                    patidList.add(rs1.getInt("PatientId"));
                    Date date = rs1.getDate("BookedDate");
                    dateList.add(date.toLocalDate());
                    timeList.add(rs1.getString("BookedTime"));
                    endtimeList.add(rs1.getString("EndTime"));
                }
            }
            for (int i = 0; i < z; i++) {

                String p = "SELECT FullName FROM patients where PatientId = '" + patidList.get(i) + "'";
                ResultSet rs2 = statement.executeQuery(p);
                if (rs2.next()) {
                    patientList.add(rs2.getString("FullName"));
                }
            }
            for (int i = 0; i < z; i++) {
                String p = "SELECT FullName FROM employees where EmployeeId = '" + docidList.get(i) + "'";
                ResultSet rs2 = statement.executeQuery(p);
                if (rs2.next()) {
                    doctorList.add(rs2.getString("FullName"));
                    appointmenttoTable.getItems().add(new Appointment( timeList.get(i), endtimeList.get(i), appidList.get(i), patientList.get(i), doctorList.get(i),dateList.get(i)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handlefirstpage(ActionEvent actionEvent) {
        mainApp.showOverview();
    }
}



