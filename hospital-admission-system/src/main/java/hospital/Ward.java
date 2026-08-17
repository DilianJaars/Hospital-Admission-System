package hospital;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the single hospital ward containing 20 beds arranged in a 4 x 5 layout
 * (B01..B20, read left-to-right, top-to-bottom).
 */
public class Ward {

    public static final int ROWS = 4;
    public static final int COLUMNS = 5;
    public static final int TOTAL_BEDS = ROWS * COLUMNS;

    private final Map<String, Bed> beds = new LinkedHashMap<>();

    public Ward() {
        for (int i = 1; i <= TOTAL_BEDS; i++) {
            String bedId = String.format("B%02d", i);
            beds.put(bedId, new Bed(bedId));
        }
    }

    public boolean bedExists(String bedId) {
        return beds.containsKey(bedId);
    }

    public boolean isBedAvailable(String bedId) {
        Bed bed = beds.get(bedId);
        return bed != null && !bed.isOccupied();
    }

    public boolean hasAvailableBed() {
        return beds.values().stream().anyMatch(b -> !b.isOccupied());
    }

    /**
     * Allocates the given bed to the given patient id.
     * Returns true on success, false if the bed does not exist or is already occupied.
     */
    public boolean allocateBed(String bedId, String patientId) {
        Bed bed = beds.get(bedId);
        if (bed == null || bed.isOccupied()) {
            return false;
        }
        bed.occupy(patientId);
        return true;
    }

    /**
     * Releases the given bed. Returns true on success, false if the bed does not
     * exist or was already free.
     */
    public boolean releaseBed(String bedId) {
        Bed bed = beds.get(bedId);
        if (bed == null || !bed.isOccupied()) {
            return false;
        }
        bed.release();
        return true;
    }

    /** Finds the bed id currently occupied by the given patient, or null if none. */
    public String findBedByPatientId(String patientId) {
        for (Bed bed : beds.values()) {
            if (bed.isOccupied() && bed.getPatientId().equals(patientId)) {
                return bed.getBedId();
            }
        }
        return null;
    }

    public List<String> getAvailableBeds() {
        List<String> available = new ArrayList<>();
        for (Bed bed : beds.values()) {
            if (!bed.isOccupied()) {
                available.add(bed.getBedId());
            }
        }
        return available;
    }

    public List<String> getOccupiedBeds() {
        List<String> occupied = new ArrayList<>();
        for (Bed bed : beds.values()) {
            if (bed.isOccupied()) {
                occupied.add(bed.getBedId());
            }
        }
        return occupied;
    }

    public int getOccupiedBedCount() {
        return getOccupiedBeds().size();
    }

    public double getOccupancyPercentage() {
        return (getOccupiedBedCount() * 100.0) / TOTAL_BEDS;
    }

    /** Prints the ward as a 4 x 5 grid, marking each bed as free [ ] or occupied [X]. */
    public void displayWardLayout() {
        System.out.println("\n===== Ward Layout (4 x 5) =====");
        int bedNumber = 1;
        for (int row = 0; row < ROWS; row++) {
            StringBuilder line = new StringBuilder();
            for (int col = 0; col < COLUMNS; col++) {
                String bedId = String.format("B%02d", bedNumber);
                Bed bed = beds.get(bedId);
                line.append(bedId).append(bed.isOccupied() ? "[X] " : "[ ] ");
                bedNumber++;
            }
            System.out.println(line);
        }
        System.out.println("Legend: [ ] = Available   [X] = Occupied");
    }
}
