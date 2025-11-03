package hms;

import java.util.List;

public class SearchingSorting {

    /* ==================== LINEAR SEARCH ==================== */
    public static void linearSearchPatientsByName(List<Patient> patients, String name) {
        name = name.toLowerCase();
        boolean found = false;

        for (Patient p : patients) {
            if (p.getFullName().toLowerCase().contains(name)) {
                System.out.println(p);
                found = true;
            }
        }

        if (!found) System.out.println("No patients found with name containing: " + name);
    }

    public static void linearSearchDoctorsByName(List<Doctor> doctors, String name) {
        name = name.toLowerCase();
        boolean found = false;

        for (Doctor d : doctors) {
            if (d.getFullName().toLowerCase().contains(name)) {
                System.out.println(d);
                found = true;
            }
        }

        if (!found) System.out.println("No doctors found with name containing: " + name);
    }

    /* ==================== BINARY SEARCH ==================== */
    public static Patient binarySearchPatientById(List<Patient> patients, String id) {
        int left = 0, right = patients.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String midId = patients.get(mid).getPatientId();
            int cmp = midId.compareTo(id);
            if (cmp == 0) return patients.get(mid);
            else if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }

    public static Doctor binarySearchDoctorById(List<Doctor> doctors, String id) {
        int left = 0, right = doctors.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String midId = doctors.get(mid).getDoctorId();
            int cmp = midId.compareTo(id);
            if (cmp == 0) return doctors.get(mid);
            else if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }

    /* ==================== BUBBLE SORT ==================== */
    public static void bubbleSortPatientsById(List<Patient> patients) {
        int n = patients.size();
        int comparisons = 0;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                comparisons++;
                if (patients.get(j).getPatientId().compareTo(patients.get(j + 1).getPatientId()) > 0) {
                    Patient temp = patients.get(j);
                    patients.set(j, patients.get(j + 1));
                    patients.set(j + 1, temp);
                }
            }
        }

        System.out.println("Bubble sort patients completed with " + comparisons + " comparisons.");
    }

    public static void bubbleSortDoctorsById(List<Doctor> doctors) {
        int n = doctors.size();
        int comparisons = 0;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                comparisons++;
                if (doctors.get(j).getDoctorId().compareTo(doctors.get(j + 1).getDoctorId()) > 0) {
                    Doctor temp = doctors.get(j);
                    doctors.set(j, doctors.get(j + 1));
                    doctors.set(j + 1, temp);
                }
            }
        }

        System.out.println("Bubble sort doctors completed with " + comparisons + " comparisons.");
    }

    /* ==================== QUICK SORT ==================== */
    public static void quickSortPatientsByName(List<Patient> patients) {
        quickSortPatientsByName(patients, 0, patients.size() - 1);
    }

    private static void quickSortPatientsByName(List<Patient> patients, int low, int high) {
        if (low < high) {
            int pi = partitionPatients(patients, low, high);
            quickSortPatientsByName(patients, low, pi - 1);
            quickSortPatientsByName(patients, pi + 1, high);
        }
    }

    private static int partitionPatients(List<Patient> patients, int low, int high) {
        String pivot = patients.get(high).getFullName().toLowerCase();
        int i = low - 1;
        int comparisons = 0;

        for (int j = low; j < high; j++) {
            comparisons++;
            if (patients.get(j).getFullName().toLowerCase().compareTo(pivot) <= 0) {
                i++;
                Patient temp = patients.get(i);
                patients.set(i, patients.get(j));
                patients.set(j, temp);
            }
        }

        Patient temp = patients.get(i + 1);
        patients.set(i + 1, patients.get(high));
        patients.set(high, temp);

        System.out.println("Quick sort partition comparisons (patients): " + comparisons);
        return i + 1;
    }

    public static void quickSortDoctorsByName(List<Doctor> doctors) {
        quickSortDoctorsByName(doctors, 0, doctors.size() - 1);
    }

    private static void quickSortDoctorsByName(List<Doctor> doctors, int low, int high) {
        if (low < high) {
            int pi = partitionDoctors(doctors, low, high);
            quickSortDoctorsByName(doctors, low, pi - 1);
            quickSortDoctorsByName(doctors, pi + 1, high);
        }
    }

    private static int partitionDoctors(List<Doctor> doctors, int low, int high) {
        String pivot = doctors.get(high).getFullName().toLowerCase();
        int i = low - 1;
        int comparisons = 0;

        for (int j = low; j < high; j++) {
            comparisons++;
            if (doctors.get(j).getFullName().toLowerCase().compareTo(pivot) <= 0) {
                i++;
                Doctor temp = doctors.get(i);
                doctors.set(i, doctors.get(j));
                doctors.set(j, temp);
            }
        }

        Doctor temp = doctors.get(i + 1);
        doctors.set(i + 1, doctors.get(high));
        doctors.set(high, temp);

        System.out.println("Quick sort partition comparisons (doctors): " + comparisons);
        return i + 1;
    }
}
