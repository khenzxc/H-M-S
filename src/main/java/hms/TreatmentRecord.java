package hms;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TreatmentRecord {
    private String recordId;
    private String patientId;
    private LocalDate date;
    private String diagnosis;
    private List<String> medications; 

    public TreatmentRecord(String recordId, String patientId, LocalDate date, String diagnosis, List<String> medications) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.date = date;
        this.diagnosis = diagnosis;
        this.medications = medications;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getPatientId() {
        return patientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public List<String> getMedications() {
        return medications;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "\n--- Treatment Record ---" +
               "\nRecord ID: " + recordId +
               "\nDate: " + date.format(fmt) +
               "\nDiagnosis: " + diagnosis +
               "\nMedications: " + (medications == null || medications.isEmpty() ? "None" : String.join(", ", medications)) +
               "\n";
    }
}
