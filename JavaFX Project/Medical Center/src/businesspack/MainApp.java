package businesspack;

import businesspack.modelpack.Appointment;
import businesspack.modelpack.Doctors;
import businesspack.modelpack.Patient;
import businesspack.viewpack.*;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import myconnectionsql.Myconnection;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class MainApp extends Application {
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
    private Stage primarystage;
    private BorderPane rootlayout;
    private int x, y, z;
    private ObservableList<Patient> patientData = FXCollections.observableArrayList();
    private ObservableList<Doctors> doctorsData = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointmentData = FXCollections.observableArrayList();


    public MainApp() {
        connection = Myconnection.doconnect();
        String sql = "SELECT * FROM `patients` ";
        try {
            statement = connection.createStatement();
            String count = "select count(*) num FROM patients";
            ResultSet vse = statement.executeQuery(count);
            if (vse.next()) {
                int sm = vse.getInt("num");
                setX(sm);
            }
            ResultSet resultSet = statement.executeQuery(sql);
            for (int i = 0; i < x; i++)
                if (resultSet.next()) {
                    String f = resultSet.getString("FirstName");
                    String l = resultSet.getString("LastName");
                    Date date = resultSet.getDate("DateofBirth");
                    LocalDate d = date.toLocalDate();
                    String mobile = resultSet.getString("Mobile");
                    String add = resultSet.getString("Address");
                    int pid = resultSet.getInt("PatientId");
                    String medi = String.valueOf(resultSet.getString("MedicareNo"));
                    String gender = resultSet.getString("Gender");
                    patientData.add(new Patient(pid, f, l, d, mobile, add, gender, medi));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            String count = "select count(*) num FROM employees";
            ResultSet vse = statement.executeQuery(count);
            if (vse.next()) {
                int sm = vse.getInt("num");
                setY(sm);
            }
            String query = "SELECT * FROM employees ";
            ResultSet resultSet1 = statement.executeQuery(query);
            for (int i = 0; i < y; i++)
                if (resultSet1.next()) {
                    String f = resultSet1.getString("FirstName");
                    String l = resultSet1.getString("LastName");
                    String mobile = resultSet1.getString("Mobile");
                    String address = resultSet1.getString("Address");
                    String gender = resultSet1.getString("Gender");
                    Date date = resultSet1.getDate("DateofBirth");
                    LocalDate d = date.toLocalDate();
                    int eid = resultSet1.getInt("EmployeeId");
                    int wd = resultSet1.getInt("Workingdays");
                    doctorsData.add(new Doctors(f, l, eid, mobile, address, gender, d, wd));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            String count = "select count(*) num FROM appointments";
            ResultSet vse = statement.executeQuery(count);
            if (vse.next()) {
                int sm = vse.getInt("num");
                setZ(sm);
            }
            String s1 = "SELECT * FROM appointments";
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
                    appointmentData.add(new Appointment( timeList.get(i), endtimeList.get(i), appidList.get(i), patientList.get(i), doctorList.get(i),dateList.get(i)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primarystage = primaryStage;
        primaryStage.setTitle("Medical Center");
        initRootLayout();
        showOverview();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("viewpack/Rootlayout.fxml"));
            rootlayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootlayout);
            primarystage.setScene(scene);
            primarystage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTodaysAppOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("viewpack/TodaysAppointment.fxml"));
            AnchorPane view = (AnchorPane) loader.load();
            rootlayout.setCenter(view);
            TodaysAppointment controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPatientOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("viewpack/NewPatient.fxml"));
            AnchorPane view = (AnchorPane) loader.load();
            rootlayout.setCenter(view);
            NewPatient controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("viewpack/FirstPage.fxml"));
            AnchorPane view = (AnchorPane) loader.load();
            StackPane root = new StackPane();
            javafx.scene.shape.Rectangle rect = new Rectangle(300, 100, 30, 30);
            rect.setArcHeight(10);
            rect.setArcWidth(10);
            rect.setFill(Color.MAROON);
            RotateTransition rottr = new RotateTransition(Duration.millis(2000), rect);
            rottr.setByAngle(180);
            rottr.setCycleCount(2);
            rottr.setAutoReverse(true);
            ScaleTransition sctr = new ScaleTransition(Duration.millis(2000), rect);
            sctr.setByX(2);
            sctr.setByY(2);
            sctr.setCycleCount(2);
            sctr.setAutoReverse(true);
            FillTransition fltr = new FillTransition(Duration.millis(2000), rect, Color.RED, javafx.scene.paint.Color.GREENYELLOW);
            fltr.setCycleCount(2);
            fltr.setAutoReverse(true);
            ParallelTransition ptr = new ParallelTransition();
            ptr.getChildren().addAll(rottr, sctr, fltr);
            ptr.play();
            view.getChildren().add(rect);
            rootlayout.setCenter(view);
            FirstPage controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primarystage;
    }

    public ObservableList<Patient> getPatientData() {
        return patientData;
    }

    public ObservableList<Doctors> getDoctorsData() {
        return doctorsData;
    }

    public ObservableList<Appointment> getAppointmentData() {
        return appointmentData;
    }

    public boolean showPersonEditDialog(Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("viewpack/PatientEditform.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Patient");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primarystage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            PatientEditForm controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPatient(patient);
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showEmployeeOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("viewpack/Employee.fxml"));
            AnchorPane employeeview = (AnchorPane) loader.load();
            rootlayout.setCenter(employeeview);
            EmployeeControl controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showEmployeeEditDialog(Doctors employee) {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("viewpack/EmployeeEditform.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Doctor");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primarystage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            EmployeeEditform controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setEmployee(employee);
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void showApplicationOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("viewpack/Appointment.fxml"));
            AnchorPane view = (AnchorPane) loader.load();
            rootlayout.setCenter(view);
            AppointmentControl controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showAppointmentEditDialog(Appointment appointment) {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("viewpack/AppointmentEdit.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Appointment");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primarystage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            AppointmentEdit controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setAppointment(appointment);
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}

