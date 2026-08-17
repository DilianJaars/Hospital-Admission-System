package hospital;

import java.util.Objects;

/**
 * Base class representing a patient record.
 * Outpatients and Emergency patients are represented directly by this class.
 * Inpatients are represented by the Inpatient subclass.
 */
public class Patient {

    protected String patientId;
    protected String firstName;
    protected String lastName;
    protected int age;
    protected String gender;
    protected String medicalCondition;
    protected PatientCategory category;

    public Patient(String patientId, String firstName, String lastName, int age,
                   String gender, String medicalCondition, PatientCategory category) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.medicalCondition = medicalCondition;
        this.category = category;
    }

    // ---------- Getters ----------
    public String getPatientId() {
        return patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }

    public PatientCategory getCategory() {
        return category;
    }

    // ---------- Setters (used for "update patient" functionality) ----------
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }

    /**
     * Prints the patient's details to the console.
     * Subclasses (e.g. Inpatient) override this to add extra fields.
     */
    public void displayDetails() {
        System.out.println("-------------------------------------------");
        System.out.println("Patient ID       : " + patientId);
        System.out.println("Name             : " + firstName + " " + lastName);
        System.out.println("Age              : " + age);
        System.out.println("Gender           : " + gender);
        System.out.println("Medical Condition: " + medicalCondition);
        System.out.println("Category         : " + category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(patientId, patient.patientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId);
    }
}
