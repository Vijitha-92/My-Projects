package businesspack.viewpack;

import businesspack.modelpack.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class PatientEditForm {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField medicareField;
    @FXML
    private TextField mobileField;
    @FXML
    private TextArea addressfield;
    @FXML
    private ToggleGroup gender;
    @FXML
    private RadioButton malebtn;
    @FXML
    private RadioButton femalebtn;
    @FXML
    private RadioButton othersbtn;
    @FXML
    private DatePicker datePicker;
    private Stage dialogStage;
    private Patient patient;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        firstNameField.setText(patient.getFirstName());
        lastNameField.setText(patient.getLastName());
        medicareField.setText(patient.getMediCare());
        mobileField.setText(patient.getMobileNo());
        addressfield.setText(patient.getAddress());
        othersbtn.setToggleGroup(gender);
        femalebtn.setToggleGroup(gender);
        malebtn.setToggleGroup(gender);
        String s = patient.getGender();
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
        datePicker.setValue(patient.getDateOfBirth());
        datePicker.setPromptText("dd.mm.yyyy");
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            patient.setFirstName(firstNameField.getText());
            patient.setLastName(lastNameField.getText());
            patient.setMediCare(medicareField.getText());
            patient.setMobileNo(mobileField.getText());
            patient.setAddress(addressfield.getText());
            if (malebtn.isSelected())
                patient.setGender("M");
            else if (femalebtn.isSelected())
                patient.setGender("F");
            else
                patient.setGender("Others");
            patient.setDateOfBirth(datePicker.getValue());
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            errorMessage += "No valid first name!\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errorMessage += "No valid last name!\n";
        }
        if (medicareField.getText() == null || medicareField.getText().length() == 0||medicareField.getText().length() >10) {
            errorMessage += "No valid medicare!\n";
        }
        if (datePicker.getValue() == null) {
            errorMessage += "No valid datePicker!\n";

        }
        if (gender.getSelectedToggle() == null) {
            errorMessage += "No valid gender!\n";

        }

        if (mobileField.getText() == null || mobileField.getText().length() == 0 ||mobileField.getText().length()>10) {
            errorMessage += "No valid mobile!\n";
        }

        if (addressfield.getText() == null || addressfield.getText().length() == 0) {
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
