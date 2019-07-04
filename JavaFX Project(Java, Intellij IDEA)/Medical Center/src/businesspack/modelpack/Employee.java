package businesspack.modelpack;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Employee {

    private StringProperty firstName;
    private StringProperty lastName;
    private IntegerProperty empId;
    private StringProperty mobileNo;
    private StringProperty address;
    private StringProperty gender;
    private ObjectProperty<LocalDate> dateOfBirth;
    private IntegerProperty workingDays;

    public Employee() {
        this(null, null, 0, null, null, null, null, 0);
    }

    public Employee(String firstName, String lastName, Integer empId, String mobileNo, String address, String gender, LocalDate dateOfBirth, Integer workingDays, Integer emptpeid, String empType) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.empId = new SimpleIntegerProperty(empId);
        this.mobileNo = new SimpleStringProperty(mobileNo);
        this.address = new SimpleStringProperty(address);
        this.gender = new SimpleStringProperty(gender);
        this.dateOfBirth = new SimpleObjectProperty<LocalDate>(dateOfBirth);
        this.workingDays = new SimpleIntegerProperty(workingDays);
    }

    public Employee(String firstName, String lastName, Integer empId, String mobileNo, String address, String gender, LocalDate dateOfBirth, Integer workingDays) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.empId = new SimpleIntegerProperty(empId);
        this.mobileNo = new SimpleStringProperty(mobileNo);
        this.address = new SimpleStringProperty(address);
        this.gender = new SimpleStringProperty(gender);
        this.dateOfBirth = new SimpleObjectProperty<LocalDate>(dateOfBirth);
        this.workingDays = new SimpleIntegerProperty(workingDays);

    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public int getWorkingDays() {
        return workingDays.get();
    }

    public void setWorkingDays(int workingDays) {
        this.workingDays.set(workingDays);
    }

    public IntegerProperty workingDaysProperty() {
        return workingDays;
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

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public Integer getEmpId() {
        return empId.get();
    }

    public void setEmpId(int empId) {
        this.empId.set(empId);
    }

    public IntegerProperty empIdProperty() {
        return empId;
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

    public ObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }

}
