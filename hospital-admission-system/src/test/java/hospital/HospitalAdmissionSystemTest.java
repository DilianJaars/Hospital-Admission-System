package hospital;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HospitalAdmissionSystemTest {

    private HospitalAdmissionSystem system;

    @BeforeEach
    void setUp() {
        system = new HospitalAdmissionSystem();
    }

    private Inpatient sampleInpatient(String id) {
        return new Inpatient(id, "John", "Smith", 40, "Male", "Flu", "W1");
    }

    private Patient sampleOutpatient(String id) {
        return new Patient(id, "Jane", "Doe", 30, "Female", "Checkup", PatientCategory.OUTPATIENT);
    }

    @Test
    void testRegisterPatient() {
        Patient p = sampleOutpatient("P001");
        system.registerPatient(p);
        assertEquals(1, system.getTotalRegisteredPatients());
        assertEquals(p, system.searchPatient("P001"));
    }

    @Test
    void testPreventDuplicatePatientIds() {
        system.registerPatient(sampleOutpatient("P001"));
        Patient duplicate = sampleOutpatient("P001");
        assertThrows(IllegalArgumentException.class, () -> system.registerPatient(duplicate));
        assertEquals(1, system.getTotalRegisteredPatients());
    }

    @Test
    void testSearchPatient() {
        system.registerPatient(sampleOutpatient("P001"));
        assertNotNull(system.searchPatient("P001"));
        assertNull(system.searchPatient("P999"));
    }

    @Test
    void testUpdatePatientDetails() {
        system.registerPatient(sampleOutpatient("P001"));
        boolean updated = system.updatePatient("P001", "Janet", null, 31, null, "Follow-up");
        assertTrue(updated);
        Patient p = system.searchPatient("P001");
        assertEquals("Janet", p.getFirstName());
        assertEquals("Doe", p.getLastName()); // unchanged
        assertEquals(31, p.getAge());
        assertEquals("Follow-up", p.getMedicalCondition());
    }

    @Test
    void testUpdateNonExistentPatientReturnsFalse() {
        assertFalse(system.updatePatient("P999", "A", "B", 20, "M", "None"));
    }

    @Test
    void testDeletePatient() {
        system.registerPatient(sampleOutpatient("P001"));
        assertTrue(system.deletePatient("P001"));
        assertNull(system.searchPatient("P001"));
        assertEquals(0, system.getTotalRegisteredPatients());
    }

    @Test
    void testDeleteNonExistentPatientReturnsFalse() {
        assertFalse(system.deletePatient("P999"));
    }

    @Test
    void testDeletingInpatientReleasesBed() {
        system.registerPatient(sampleInpatient("P001"));
        system.allocateBed("P001", "B01");
        assertTrue(system.deletePatient("P001"));
        assertTrue(system.getWard().isBedAvailable("B01"));
    }

    @Test
    void testAllocateBedToInpatient() {
        system.registerPatient(sampleInpatient("P001"));
        system.allocateBed("P001", "B01");
        Inpatient patient = (Inpatient) system.searchPatient("P001");
        assertEquals("B01", patient.getBedNumber());
        assertFalse(system.getWard().isBedAvailable("B01"));
    }

    @Test
    void testCannotAllocateBedToOutpatient() {
        system.registerPatient(sampleOutpatient("P001"));
        assertThrows(IllegalArgumentException.class, () -> system.allocateBed("P001", "B01"));
    }

    @Test
    void testPreventAllocatingAnOccupiedBed() {
        system.registerPatient(sampleInpatient("P001"));
        system.registerPatient(sampleInpatient("P002"));
        system.allocateBed("P001", "B01");
        assertThrows(IllegalStateException.class, () -> system.allocateBed("P002", "B01"));
    }

    @Test
    void testPreventBedAllocationWhenAllBedsAreOccupied() {
        // Fill all 20 beds.
        for (int i = 1; i <= 20; i++) {
            String id = "P" + i;
            system.registerPatient(sampleInpatient(id));
            system.allocateBed(id, String.format("B%02d", i));
        }
        // 21st inpatient should be rejected because no beds remain.
        system.registerPatient(sampleInpatient("P21"));
        assertThrows(IllegalStateException.class, () -> system.allocateBed("P21", "B01"));
    }

    @Test
    void testReleaseBed() {
        system.registerPatient(sampleInpatient("P001"));
        system.allocateBed("P001", "B01");
        boolean released = system.releaseBedForPatient("P001");
        assertTrue(released);
        assertTrue(system.getWard().isBedAvailable("B01"));
        Inpatient patient = (Inpatient) system.searchPatient("P001");
        assertNull(patient.getBedNumber());
    }

    @Test
    void testReleaseBedWhenPatientHasNoBedReturnsFalse() {
        system.registerPatient(sampleInpatient("P001"));
        assertFalse(system.releaseBedForPatient("P001"));
    }

    @Test
    void testSortPatientsBySurname() {
        system.registerPatient(new Patient("P1", "A", "Zebra", 20, "M", "-", PatientCategory.OUTPATIENT));
        system.registerPatient(new Patient("P2", "B", "Apple", 22, "F", "-", PatientCategory.OUTPATIENT));
        List<Patient> sorted = system.getPatientsSortedBySurname();
        assertEquals("Apple", sorted.get(0).getLastName());
        assertEquals("Zebra", sorted.get(1).getLastName());
    }

    @Test
    void testSortPatientsById() {
        system.registerPatient(new Patient("P2", "A", "Smith", 20, "M", "-", PatientCategory.OUTPATIENT));
        system.registerPatient(new Patient("P1", "B", "Jones", 22, "F", "-", PatientCategory.OUTPATIENT));
        List<Patient> sorted = system.getPatientsSortedById();
        assertEquals("P1", sorted.get(0).getPatientId());
        assertEquals("P2", sorted.get(1).getPatientId());
    }

    @Test
    void testWardOccupancyPercentage() {
        system.registerPatient(sampleInpatient("P001"));
        system.allocateBed("P001", "B01");
        assertEquals(5.0, system.getWard().getOccupancyPercentage(), 0.001); // 1 of 20 beds
    }
}
