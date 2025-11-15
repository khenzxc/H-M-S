package hms;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Patient {
    private String patientId;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String contact;
    private String address;
    private String bloodType;
    private List<String> allergies;
    private List<String> currentMedications;
    private List<String> medicalHistory;
    private int priorityLevel;
    private boolean active = true;
    private List<TreatmentRecord> treatmentHistory = new ArrayList<>();

    private final LocalDate createdAt;
    private LocalDate updatedAt;

    public Patient(String patientId, String firstName, String lastName, LocalDate dob,
            String gender, String contact, String address,
            String bloodType, List<String> allergies,
            List<String> currentMedications, List<String> medicalHistory,
            int priorityLevel) {

        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.contact = contact;
        this.address = address;
        this.bloodType = bloodType;
        this.allergies = allergies != null ? allergies : new ArrayList<>();
        this.currentMedications = currentMedications != null ? currentMedications : new ArrayList<>();
        this.medicalHistory = medicalHistory != null ? medicalHistory : new ArrayList<>();
        this.priorityLevel = (priorityLevel == 0 || priorityLevel == 1) ? priorityLevel : 0;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    public Patient(String patientId, String firstName, String lastName, LocalDate dob) {
        this(patientId, firstName, lastName, dob, "", "", "", null, null, null, null, 0);
    }

    public String getPatientId() {
        return patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public int getAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    public String getGender() {
        return gender;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    public String getBloodType() {
        return bloodType;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public List<String> getCurrentMedications() {
        return currentMedications;
    }

    public List<String> getMedicalHistory() {
        return medicalHistory;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public List<TreatmentRecord> getTreatmentHistory() {
        return treatmentHistory;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateTimestamp();
    }

    public void setActive(boolean active) {
        this.active = active;
        updateTimestamp();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateTimestamp();
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
        updateTimestamp();
    }

    public void setGender(String gender) {
        this.gender = gender;
        updateTimestamp();
    }

    public void setContact(String contact) {
        this.contact = contact;
        updateTimestamp();
    }

    public void setAddress(String address) {
        this.address = address;
        updateTimestamp();
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
        updateTimestamp();
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
        updateTimestamp();
    }

    public void setCurrentMedications(List<String> meds) {
        this.currentMedications = meds;
        updateTimestamp();
    }

    public void setMedicalHistory(List<String> history) {
        this.medicalHistory = history;
        updateTimestamp();
    }

    public void addTreatmentRecord(TreatmentRecord record) {
        treatmentHistory.add(record);
    }

    public void addToMedicalHistory(String diagnosis) {
        if (diagnosis != null && !diagnosis.isBlank()) {
            if (medicalHistory == null) {
                medicalHistory = new ArrayList<>();
            }
            medicalHistory.add(diagnosis);
            updatedAt = LocalDate.now();
        }
    }

    public void setPriorityLevel(int priorityLevel) {
        if (priorityLevel == 0 || priorityLevel == 1)
            this.priorityLevel = priorityLevel;
        updateTimestamp();
    }

    public void deactivate() {
        this.active = false;
        updateTimestamp();
    }

    private void updateTimestamp() {
        this.updatedAt = LocalDate.now();
    }

    @Override
    public String toString() {
        String prio = (priorityLevel == 1) ? "URGENT" : "NORMAL";
        String status = active ? "ACTIVE" : "INACTIVE";

        return String.format(
                "%-6s | %-20s | %-3d | %-6s | %-6s | %-7s | %-8s",
                patientId,
                getFullName(),
                getAge(),
                gender,
                (bloodType != null ? bloodType : "N/A"),
                prio,
                status);
    }

    public String toFullProfileString() {
        String prio = (priorityLevel == 1) ? "URGENT" : "NORMAL";
        String status = active ? "ACTIVE" : "INACTIVE";

        return String.format(
                "Patient ID   : %s\n" +
                        "Full Name    : %s\n" +
                        "Age          : %d\n" +
                        "Gender       : %s\n" +
                        "Contact      : %s\n" +
                        "Address      : %s\n" +
                        "Blood Type   : %s\n" +
                        "Priority     : %s\n" +
                        "Status       : %s\n" +
                        "Date of Birth: %s\n" +
                        "Allergies    : %s\n" +
                        "Medications  : %s\n" +
                        "Medical Hist.: %s\n" +
                        "Created      : %s\n" +
                        "Updated      : %s",
                patientId,
                getFullName(),
                getAge(),
                gender,
                contact,
                address,
                (bloodType != null ? bloodType : "N/A"),
                prio,
                status,
                dob,
                (allergies.isEmpty() ? "None" : allergies),
                (currentMedications.isEmpty() ? "None" : currentMedications),
                (medicalHistory.isEmpty() ? "None" : medicalHistory),
                createdAt,
                updatedAt);
    }

}
