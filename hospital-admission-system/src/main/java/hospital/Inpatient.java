package hospital;

/**
 * Represents an Inpatient, who additionally occupies a ward bed.
 * Extends Patient and uses super() to initialise the inherited attributes.
 */
public class Inpatient extends Patient {

    private String wardNumber;
    private String bedNumber; // e.g. "B01" .. "B20", null until a bed is allocated

    public Inpatient(String patientId, String firstName, String lastName, int age,
                      String gender, String medicalCondition, String wardNumber) {
        super(patientId, firstName, lastName, age, gender, medicalCondition, PatientCategory.INPATIENT);
        this.wardNumber = wardNumber;
        this.bedNumber = null;
    }

    public String getWardNumber() {
        return wardNumber;
    }

    public void setWardNumber(String wardNumber) {
        this.wardNumber = wardNumber;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public boolean hasBed() {
        return bedNumber != null;
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Ward Number      : " + wardNumber);
        System.out.println("Bed Number       : " + (bedNumber == null ? "Not allocated" : bedNumber));
    }
}
