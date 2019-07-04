package businesspack.modelpack;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.Date;

public class Appointment {


    private final StringProperty sTime;
    private final StringProperty eTime;
    private final IntegerProperty appId;
    private final StringProperty pfullName;
    private final StringProperty dfullName;
    private final ObjectProperty<LocalDate> bookedDate;

    public Appointment(String startBookedTime, String endbookedTime, Integer appId, String pfullName, String dfullName, LocalDate bookedDate) {
        this.sTime = new SimpleStringProperty(startBookedTime);
        this.eTime = new SimpleStringProperty(endbookedTime);
        this.appId = new SimpleIntegerProperty(appId);
        this.pfullName = new SimpleStringProperty(pfullName);
        this.dfullName = new SimpleStringProperty(dfullName);
        this.bookedDate = new SimpleObjectProperty<LocalDate>(bookedDate);
    }
    public Appointment(){
        this(null,null,0,null,null,null);

    }
    public int getAppId() {
        return appId.get();
    }

    public IntegerProperty appIdProperty() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId.set(appId);
    }

    public String getPfullName() {
        return pfullName.get();
    }

    public StringProperty pfullNameProperty() {
        return pfullName;
    }

    public void setPfullName(String pfullName) {
        this.pfullName.set(pfullName);
    }

    public String getDfullName() {
        return dfullName.get();
    }

    public StringProperty dfullNameProperty() {
        return dfullName;
    }

    public void setDfullName(String dfullName) {
        this.dfullName.set(dfullName);
    }

    public LocalDate getBookedDate() {
        return bookedDate.get();
    }

    public ObjectProperty<LocalDate> bookedDateProperty() {
        return bookedDate;
    }

    public void setBookedDate(LocalDate bookedDate) {
        this.bookedDate.set(bookedDate);
    }

    public String getsTime() {
        return sTime.get();
    }

    public StringProperty sTimeProperty() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime.set(sTime);
    }

    public String geteTime() {
        return eTime.get();
    }

    public StringProperty eTimeProperty() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime.set(eTime);
    }

}


