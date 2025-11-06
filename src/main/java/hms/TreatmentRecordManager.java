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

        System.out.print("Enter Medications (comma-separated): ");
        String medsInput = sc.nextLine().trim();

        // Convert comma-separated string into a list
        List<String> medications = new ArrayList<>();
        if (!medsInput.isBlank()) {
            for (String med : medsInput.split(",")) {
                medications.add(med.trim());
            }
        }

        String recordId = "TR" + (records.size() + 1);
        LocalDate date = LocalDate.now();

        //Create treatment record with list of meds
        TreatmentRecord record = new TreatmentRecord(recordId, patientId, date, diagnosis, medications);
        records.add(record);

        // Update patient data
        patient.getTreatmentHistory().add(record);
        patient.addToMedicalHistory(diagnosis);

        // Replace patient’s current meds with new list (clear old)
        patient.setCurrentMedications(new ArrayList<>(medications));

        System.out.println("\nTreatment record added successfully!");
        System.out.println("Patient: " + patient.getFullName() + " (ID: " + patientId + ")");
        System.out.println("Diagnosis: " + diagnosis);
        System.out.println(
                "New Current Medications: " + (medications.isEmpty() ? "None" : String.join(", ", medications)));
        System.out.println("Record ID: " + recordId + " | Date: " + date);
    }

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

    public List<TreatmentRecord> getAllRecords() {
        return records;
    }
}
