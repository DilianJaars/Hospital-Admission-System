package hospital;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Core service class that manages patient records and bed allocation
 * for the single 20-bed ward.
 */
public class HospitalAdmissionSystem {

    private final Map<String, Patient> patients = new LinkedHashMap<>();
    private final Ward ward = new Ward();

    public Ward getWard() {
        return ward;
    }

    // ---------------------------------------------------------------
    // Patient record management
    // ---------------------------------------------------------------

    /**
     * Registers a new patient.
     * @throws IllegalArgumentException if a patient with the same ID already exists.
     */
    public void registerPatient(Patient patient) {
        if (patients.containsKey(patient.getPatientId())) {
            throw new IllegalArgumentException(
                    "A patient with ID " + patient.getPatientId() + " already exists.");
        }
        patients.put(patient.getPatientId(), patient);
    }

    /** Returns the patient with the given ID, or null if not found. */
    public Patient searchPatient(String patientId) {
        return patients.get(patientId);
    }

    /**
     * Updates the mutable fields of an existing patient.
     * Pass null for any field that should stay unchanged.
     * @return true if the patient was found and updated, false otherwise.
     */
    public boolean updatePatient(String patientId, String firstName, String lastName,
                                  Integer age, String gender, String medicalCondition) {
        Patient patient = patients.get(patientId);
        if (patient == null) {
            return false;
        }
        if (firstName != null && !firstName.isBlank()) patient.setFirstName(firstName);
        if (lastName != null && !lastName.isBlank()) patient.setLastName(lastName);
        if (age != null) patient.setAge(age);
        if (gender != null && !gender.isBlank()) patient.setGender(gender);
        if (medicalCondition != null && !medicalCondition.isBlank()) patient.setMedicalCondition(medicalCondition);
        return true;
    }

    /**
     * Deletes a patient. If the patient is an inpatient occupying a bed,
     * the bed is released automatically.
     * @return true if the patient was found and removed, false otherwise.
     */
    public boolean deletePatient(String patientId) {
        Patient patient = patients.get(patientId);
        if (patient == null) {
            return false;
        }
        if (patient instanceof Inpatient inpatient && inpatient.hasBed()) {
            ward.releaseBed(inpatient.getBedNumber());
        }
        patients.remove(patientId);
        return true;
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    public List<Patient> getPatientsSortedBySurname() {
        List<Patient> list = getAllPatients();
        list.sort(Comparator.comparing(Patient::getLastName, String.CASE_INSENSITIVE_ORDER));
        return list;
    }

    public List<Patient> getPatientsSortedById() {
        List<Patient> list = getAllPatients();
        list.sort(Comparator.comparing(Patient::getPatientId));
        return list;
    }

    public int getTotalRegisteredPatients() {
        return patients.size();
    }

    // ---------------------------------------------------------------
    // Bed allocation (Inpatients only)
    // ---------------------------------------------------------------

    /**
     * Allocates the given bed to the given inpatient.
     * @throws IllegalArgumentException if the patient does not exist or is not an Inpatient
     * @throws IllegalStateException if the bed does not exist, is already occupied,
     *                                or no beds are available at all
     */
    public void allocateBed(String patientId, String bedId) {
        Patient patient = patients.get(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("No patient found with ID " + patientId);
        }
        if (!(patient instanceof Inpatient inpatient)) {
            throw new IllegalArgumentException("Only inpatients may be allocated a bed.");
        }
        if (inpatient.hasBed()) {
            throw new IllegalStateException("Patient " + patientId + " already occupies bed "
                    + inpatient.getBedNumber() + ".");
        }
        if (!ward.hasAvailableBed()) {
            throw new IllegalStateException("No beds are available.");
        }
        if (!ward.bedExists(bedId)) {
            throw new IllegalArgumentException("Bed " + bedId + " does not exist.");
        }
        if (!ward.isBedAvailable(bedId)) {
            throw new IllegalStateException("Bed " + bedId + " is already occupied.");
        }
        ward.allocateBed(bedId, patientId);
        inpatient.setBedNumber(bedId);
    }

    /**
     * Releases the bed occupied by the given inpatient (i.e. discharges them from the bed).
     * @return true if a bed was released, false if the patient had no bed / does not exist.
     */
    public boolean releaseBedForPatient(String patientId) {
        Patient patient = patients.get(patientId);
        if (!(patient instanceof Inpatient inpatient) || !inpatient.hasBed()) {
            return false;
        }
        boolean released = ward.releaseBed(inpatient.getBedNumber());
        if (released) {
            inpatient.setBedNumber(null);
        }
        return released;
    }

    /** Releases a bed directly by its bed ID (e.g. "B05"). */
    public boolean releaseBedById(String bedId) {
        for (Patient p : patients.values()) {
            if (p instanceof Inpatient inpatient && bedId.equals(inpatient.getBedNumber())) {
                inpatient.setBedNumber(null);
                break;
            }
        }
        return ward.releaseBed(bedId);
    }

    // ---------------------------------------------------------------
    // Reports
    // ---------------------------------------------------------------

    public void displayAllPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients registered.");
            return;
        }
        for (Patient p : patients.values()) {
            p.displayDetails();
        }
    }

    public void displayReportsSummary() {
        System.out.println("\n===== Ward Report =====");
        System.out.println("Total registered patients : " + getTotalRegisteredPatients());
        System.out.println("Total occupied beds        : " + ward.getOccupiedBedCount() + " / " + Ward.TOTAL_BEDS);
        System.out.printf("Ward occupancy percentage   : %.2f%%%n", ward.getOccupancyPercentage());
        System.out.println("Available beds              : " + ward.getAvailableBeds());
        System.out.println("Occupied beds                : " + ward.getOccupiedBeds());
    }
}
