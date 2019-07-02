package businesspack.viewpack;

import businesspack.modelpack.Doctors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EmployeeEditform {
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
    private Stage dialogStage;
    private Doctors employee;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEmployee(Doctors employee) {
        this.employee = employee;
        firstNameLabel.setText(employee.getFirstName());
        lastNameLabel.setText(employee.getLastName());
        contactNo.setText(employee.getMobileNo());
        workingdays.setText(Integer.toString(employee.getWorkingDays()));
        emptype.setText("Doctor");
        address.setText(employee.getAddress());
        othersbtn.setToggleGroup(gender);
        femalebtn.setToggleGroup(gender);
        malebtn.setToggleGroup(gender);
        String s = employee.getGender();
        if (s != null) {
            if (s.equals("M") || s.equals("m"))
                gender.selectToggle(malebtn);
            else if (s.equals("F") || s.equals("f"))
                gender.selectToggle(femalebtn);
            else
                gender.selectToggle(othersbtn);
        } else {
            othersbtn.setToggleGroup(gender);
            femalebtn.setToggleGroup(gender);
            malebtn.setToggleGroup(gender);
            gender.selectToggle(null);
        }
        dateOfBrithDp.setValue(employee.getDateOfBirth());
        dateOfBrithDp.setPromptText("dd.mm.yyyy");
        emptype.setText(employee.getEmptype());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void handleOk(ActionEvent actionEvent) {
        if (isInputValid()) {
            employee.setFirstName(firstNameLabel.getText());
            employee.setLastName(lastNameLabel.getText());
            employee.setMobileNo(contactNo.getText());
            employee.setWorkingDays(Integer.parseInt(workingdays.getText()));
            employee.setAddress(address.getText());
            if (malebtn.isSelected()) {
                employee.setGender("M");
            } else if (femalebtn.isSelected())
                employee.setGender("F");
            else
                employee.setGender("Others");
            employee.setDateOfBirth(dateOfBrithDp.getValue());
            okClicked = true;
            dialogStage.close();
        }
    }

    public void handleCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (firstNameLabel.getText() == null || firstNameLabel.getText().length() == 0) {
            errorMessage += "No valid first name!\n";
        }
        if (lastNameLabel.getText() == null || lastNameLabel.getText().length() == 0) {
            errorMessage += "No valid last name!\n";
        }
        if (dateOfBrithDp.getValue() == null) {
            errorMessage += "No valid datePicker!\n";
        }
        if (gender.getSelectedToggle() == null) {
            errorMessage += "No valid gender!\n";
        }
        if (contactNo.getText() == null || contactNo.getText().length() == 0||contactNo.getText().length() >10) {
            errorMessage += "No valid mobile!\n";
        }
        if (address.getText() == null || address.getText().length() == 0) {
            errorMessage += "No valid address!\n";
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

}
