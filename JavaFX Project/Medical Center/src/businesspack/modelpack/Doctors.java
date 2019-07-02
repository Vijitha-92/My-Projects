package businesspack.modelpack;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Doctors extends Employee {
    public final StringProperty emptype;
    public final IntegerProperty emptpeid;
    public Doctors(String firstName, String lastName, Integer empId, String mobileNo, String address, String gender, LocalDate dateOfBirth, Integer workingDays) {
        super(firstName, lastName, empId, mobileNo, address, gender, dateOfBirth, workingDays);
        this.emptype = new SimpleStringProperty("Doctor");
        this.emptpeid = new SimpleIntegerProperty(1);
    }
    public Doctors() {
        super(null, null, 0, null, null, null, null, 0);
        this.emptype = new SimpleStringProperty("Doctor");
        this.emptpeid = new SimpleIntegerProperty(1);
    }
    public int getEmptpeid() {
        return emptpeid.get();
    }
    public void setEmptpeid(int emptpeid) {
        this.emptpeid.set(1);
    }
    public IntegerProperty emptpeidProperty() {
        return emptpeid;
    }
    public String getEmptype() {
        return emptype.get();
    }
    public StringProperty emptypeProperty() {
        return emptype;
    }

}
