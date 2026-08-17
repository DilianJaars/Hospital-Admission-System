package hospital;

import java.util.List;
import java.util.Scanner;

/**
 * Console-based, menu-driven entry point for the MediCare Hospital
 * Patient Admission System.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final HospitalAdmissionSystem system = new HospitalAdmissionSystem();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> registerPatient();
                case "2" -> searchPatient();
                case "3" -> updatePatient();
                case "4" -> deletePatient();
                case "5" -> system.displayAllPatients();
                case "6" -> allocateBed();
                case "7" -> releaseBed();
                case "8" -> system.getWard().displayWardLayout();
                case "9" -> printBedList("Available beds", system.getWard().getAvailableBeds());
                case "10" -> printBedList("Occupied beds", system.getWard().getOccupiedBeds());
                case "11" -> system.displayReportsSummary();
                case "12" -> displaySorted();
                case "0" -> {
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n===== MediCare Hospital - Patient Admission System =====");
        System.out.println(" 1. Register a new patient");
        System.out.println(" 2. Search for a patient by ID");
        System.out.println(" 3. Update patient details");
        System.out.println(" 4. Delete a patient");
        System.out.println(" 5. Display all registered patients");
        System.out.println(" 6. Allocate a bed to an inpatient");
        System.out.println(" 7. Release a bed (discharge inpatient)");
        System.out.println(" 8. Display ward layout");
        System.out.println(" 9. Display available beds");
        System.out.println("10. Display occupied beds");
        System.out.println("11. Display ward report summary");
        System.out.println("12. Display patients sorted (surname / ID)");
        System.out.println(" 0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void registerPatient() {
        System.out.print("Patient ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("First name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine().trim();
        int age = readInt("Age: ");
        System.out.print("Gender: ");
        String gender = scanner.nextLine().trim();
        System.out.print("Medical condition: ");
        String condition = scanner.nextLine().trim();

        PatientCategory category = readCategory();

        try {
            Patient patient;
            if (category == PatientCategory.INPATIENT) {
                System.out.print("Ward number (e.g. W1): ");
                String wardNumber = scanner.nextLine().trim();
                patient = new Inpatient(id, firstName, lastName, age, gender, condition, wardNumber);
            } else {
                patient = new Patient(id, firstName, lastName, age, gender, condition, category);
            }
            system.registerPatient(patient);
            System.out.println("Patient registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void searchPatient() {
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine().trim();
        Patient patient = system.searchPatient(id);
        if (patient == null) {
            System.out.println("No patient found with ID " + id);
        } else {
            patient.displayDetails();
        }
    }

    private static void updatePatient() {
        System.out.print("Enter Patient ID to update: ");
        String id = scanner.nextLine().trim();
        if (system.searchPatient(id) == null) {
            System.out.println("No patient found with ID " + id);
            return;
        }
        System.out.print("New first name (leave blank to keep current): ");
        String firstName = scanner.nextLine().trim();
        System.out.print("New last name (leave blank to keep current): ");
        String lastName = scanner.nextLine().trim();
        System.out.print("New age (leave blank to keep current): ");
        String ageStr = scanner.nextLine().trim();
        Integer age = ageStr.isBlank() ? null : Integer.parseInt(ageStr);
        System.out.print("New gender (leave blank to keep current): ");
        String gender = scanner.nextLine().trim();
        System.out.print("New medical condition (leave blank to keep current): ");
        String condition = scanner.nextLine().trim();

        boolean updated = system.updatePatient(id, firstName, lastName, age, gender, condition);
        System.out.println(updated ? "Patient updated successfully." : "Update failed.");
    }

    private static void deletePatient() {
        System.out.print("Enter Patient ID to delete: ");
        String id = scanner.nextLine().trim();
        boolean deleted = system.deletePatient(id);
        System.out.println(deleted ? "Patient deleted successfully." : "No patient found with that ID.");
    }

    private static void allocateBed() {
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Bed ID (e.g. B01): ");
        String bedId = scanner.nextLine().trim().toUpperCase();
        try {
            system.allocateBed(id, bedId);
            System.out.println("Bed " + bedId + " allocated to patient " + id + ".");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void releaseBed() {
        System.out.print("Enter Patient ID being discharged: ");
        String id = scanner.nextLine().trim();
        boolean released = system.releaseBedForPatient(id);
        System.out.println(released ? "Bed released successfully." : "No bed was allocated to that patient.");
    }

    private static void displaySorted() {
        System.out.print("Sort by (1) Surname or (2) Patient ID? ");
        String choice = scanner.nextLine().trim();
        List<Patient> sorted = choice.equals("2")
                ? system.getPatientsSortedById()
                : system.getPatientsSortedBySurname();
        if (sorted.isEmpty()) {
            System.out.println("No patients registered.");
        } else {
            sorted.forEach(Patient::displayDetails);
        }
    }

    private static void printBedList(String title, List<String> beds) {
        System.out.println(title + ": " + (beds.isEmpty() ? "None" : beds));
    }

    private static PatientCategory readCategory() {
        while (true) {
            System.out.print("Category (1=Inpatient, 2=Outpatient, 3=Emergency): ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": return PatientCategory.INPATIENT;
                case "2": return PatientCategory.OUTPATIENT;
                case "3": return PatientCategory.EMERGENCY;
                default: System.out.println("Invalid choice, please enter 1, 2, or 3.");
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }
}
