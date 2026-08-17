package hospital;

/**
 * Represents a single bed in the ward.
 */
public class Bed {

    private final String bedId; // e.g. "B01"
    private boolean occupied;
    private String patientId; // id of the inpatient occupying this bed, null if free

    public Bed(String bedId) {
        this.bedId = bedId;
        this.occupied = false;
        this.patientId = null;
    }

    public String getBedId() {
        return bedId;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public String getPatientId() {
        return patientId;
    }

    public void occupy(String patientId) {
        this.occupied = true;
        this.patientId = patientId;
    }

    public void release() {
        this.occupied = false;
        this.patientId = null;
    }
}
