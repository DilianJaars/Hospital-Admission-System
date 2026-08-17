package hospital;

/**
 * Represents the three categories of patients handled by the ward.
 * Only INPATIENT patients may be allocated a hospital bed.
 */
public enum PatientCategory {
    INPATIENT,
    OUTPATIENT,
    EMERGENCY
}
