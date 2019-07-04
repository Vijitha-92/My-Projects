package businesspack.viewpack;

import businesspack.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FirstPage {
    private MainApp mainApp;
    @FXML
    private Label fLabel;

    public void handlepatient(ActionEvent actionEvent) {
        mainApp.showPatientOverview();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void handledoctors(ActionEvent actionEvent) {
        mainApp.showEmployeeOverview();
    }

    public void handleappointments(ActionEvent actionEvent) {
        mainApp.showApplicationOverview();
    }

    public void handletodayap(ActionEvent actionEvent) {
        mainApp.showTodaysAppOverview();
    }
}
