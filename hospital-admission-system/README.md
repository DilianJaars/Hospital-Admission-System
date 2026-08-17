# MediCare Hospital – Patient Admission System (PROG6112 Assignment 1)

Console-based, menu-driven Java application for managing patient records
and bed allocation in a 20-bed hospital ward (4 x 5 layout).

## Opening in IntelliJ IDEA

1. Unzip this project.
2. `File > Open...` and select the `hospital-admission-system` folder
   (the one containing `pom.xml`). IntelliJ will detect it as a Maven
   project and download JUnit 5 automatically.
3. Wait for indexing / Maven sync to finish.
4. Run `hospital.Main` to start the console app (right-click the file → Run).
5. Run the tests: right-click `src/test/java/hospital/HospitalAdmissionSystemTest.java` → Run.

Requires JDK 17+ (Project Structure > SDK).

## Project layout

```
src/main/java/hospital/
  Patient.java                    - base patient class
  PatientCategory.java            - enum: INPATIENT, OUTPATIENT, EMERGENCY
  Inpatient.java                  - extends Patient, adds ward/bed number
  Bed.java                        - single bed
  Ward.java                       - 20 beds, 4x5 layout, allocation logic
  HospitalAdmissionSystem.java    - core service: CRUD, allocation, reports
  Main.java                       - console menu

src/test/java/hospital/
  HospitalAdmissionSystemTest.java - JUnit 5 tests (Feature 5)
```

## What's implemented (maps to the brief)

- **Feature 1 – Patient records:** register, search by ID, update, delete,
  display all (`Patient`, `HospitalAdmissionSystem`).
- **Feature 2 – Bed allocation:** allocate/release beds, ward layout,
  available/occupied lists, blocks allocation when the ward is full
  (`Ward`, `HospitalAdmissionSystem`).
- **Feature 3 – Reports:** all-patients, available/occupied beds, totals,
  occupancy percentage (`displayReportsSummary()`).
- **Feature 4 – Patient categories:** `PatientCategory` enum; `Inpatient`
  extends `Patient`, uses `super()`, overrides `displayDetails()`.
- **Feature 5 – Unit testing:** JUnit 5 tests covering register, search,
  update, delete, allocate, release, duplicate-ID prevention, occupied-bed
  prevention, full-ward prevention, and sorting.

## Notes / assumptions carried over from the brief

- Single ward, exactly 20 beds (B01–B20).
- Only Inpatients occupy beds; Outpatients/Emergency patients don't.
- Everything is in-memory (no file/DB persistence) — data resets each run.
- This is a **draft/starting point**: adjust field validation, exception
  handling, and menu text to match your own style before submitting.
