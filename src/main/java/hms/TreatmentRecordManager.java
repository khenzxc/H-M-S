package hms;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TreatmentRecordManager {
    private List<TreatmentRecord> records = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private PatientManager patientManager;

    public TreatmentRecordManager(PatientManager patientManager) {
        this.patientManager = patientManager;
    }

    // helper: find patient by id using PatientManager's list
    private Patient findPatientById(String patientId) {
        List<Patient> list = patientManager.getPatientList();
        if (list == null)
            return null;
        for (Patient p : list) {
            if (p != null && p.getPatientId().equalsIgnoreCase(patientId)) {
                return p;
            }
        }
        return null;
    }

    public void addTreatmentRecord() {
        System.out.print("Enter Patient ID: ");
        String patientId = sc.nextLine().trim();

        Patient patient = findPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient not found (ID: " + patientId + ")");
            return;
        }

        System.out.print("Enter Diagnosis: ");
        String diagnosis = sc.nextLine().trim();

        System.out.print("Enter Medication/Prescription: ");
        String medication = sc.nextLine().trim();

        String recordId = "TR" + (records.size() + 1);
        LocalDate date = LocalDate.now();

        // Create treatment record
        TreatmentRecord record = new TreatmentRecord(recordId, patientId, date, diagnosis, medication);
        records.add(record);

        // --- ✅ Update patient history ---
        patient.getTreatmentHistory().add(record); // add record to treatment history
        patient.addToMedicalHistory(diagnosis); // append diagnosis to medical history

        System.out.println("✅ Treatment record added for " + patient.getFullName() +
                " (Record ID: " + recordId + ")");
    }

    // View history for a patient (newest first)
    public void viewPatientHistory() {
        System.out.print("Enter Patient ID: ");
        String patientId = sc.nextLine().trim();

        Patient patient = findPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient not found (ID: " + patientId + ")");
            return;
        }

        boolean found = false;
        System.out.println("\n=== Treatment History for " + patient.getFullName() + " ===");

        // iterate in reverse to show newest first
        for (int i = records.size() - 1; i >= 0; i--) {
            TreatmentRecord r = records.get(i);
            if (r.getPatientId().equalsIgnoreCase(patientId)) {
                System.out.println(r);
                found = true;
            }
        }

        if (!found) {
            System.out.println("ℹ No treatment records found for this patient.");
        }
    }

    // getter for all records (if you need it)
    public List<TreatmentRecord> getAllRecords() {
        return records;
    }
}
