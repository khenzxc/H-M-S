package hms;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TreatmentRecord {
    private String recordId;
    private String patientId;
    private LocalDate date;
    private String diagnosis;
    private String medication;

    public TreatmentRecord(String recordId, String patientId, LocalDate date, String diagnosis, String medication) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.date = date;
        this.diagnosis = diagnosis;
        this.medication = medication;
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

    public String getMedication() {
        return medication;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "\n--- Treatment Record ---" +
               "\nRecord ID: " + recordId +
               "\nDate: " + date.format(fmt) +
               "\nDiagnosis: " + diagnosis +
               "\nMedication: " + medication + "\n";
    }
}
