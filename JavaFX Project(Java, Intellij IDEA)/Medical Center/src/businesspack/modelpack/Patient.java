package businesspack.modelpack;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Patient {
    private final IntegerProperty patientid;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final ObjectProperty<LocalDate> dateOfBirth;
    private final StringProperty mobileNo;
    private final StringProperty address;
    private final StringProperty gender;
    private final StringProperty mediCare;

    public Patient() {
        this(0, null, null, null, null, null, null, null);
    }

    public Patient(Integer patientid, String firstName, String lastName, LocalDate dateOfBirth, String mobileNo, String address, String gender, String mediCare) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.mediCare = new SimpleStringProperty(mediCare);
        this.mobileNo = new SimpleStringProperty(mobileNo);
        this.address = new SimpleStringProperty(address);
        this.gender = new SimpleStringProperty(gender);
        this.dateOfBirth = new SimpleObjectProperty<LocalDate>(dateOfBirth);
        this.patientid = new SimpleIntegerProperty(patientid);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getMediCare() {
        return mediCare.get();
    }

    public void setMediCare(String mediCare) {
        this.mediCare.set(mediCare);
    }

    public StringProperty mediCareProperty() {
        return mediCare;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public ObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }

    public int getPatientid() {
        return patientid.get();
    }

    public void setPatientid(int patientid) {
        this.patientid.set(patientid);
    }

    public IntegerProperty patientidProperty() {
        return patientid;
    }

    public String getMobileNo() {
        return mobileNo.get();
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo.set(mobileNo);
    }

    public StringProperty mobileNoProperty() {
        return mobileNo;
    }

}
