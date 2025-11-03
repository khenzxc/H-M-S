package hms;

import java.time.LocalDateTime;

public class Appointment {
    private static int counter = 1;
    private final String appointmentId;
    private String doctorId;
    private String patientId;
    private LocalDateTime dateTime;
    private boolean priority; // true = emergency
    private long insertionOrder; // for FIFO tiebreak

    public Appointment(String doctorId, String patientId, LocalDateTime dateTime, boolean priority, long insertionOrder) {
        this.appointmentId = String.format("A%04d", counter++);
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.dateTime = dateTime;
        this.priority = priority;
        this.insertionOrder = insertionOrder;
    }

    // ======= GETTERS =======
    public String getAppointmentId() { return appointmentId; }
    public String getDoctorId() { return doctorId; }
    public String getPatientId() { return patientId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public boolean isPriority() { return priority; }
    public long getInsertionOrder() { return insertionOrder; }

    // ======= SETTERS =======
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    // ======= TO STRING =======
  @Override
public String toString() {
    return "AppointmentID: " + appointmentId +
           " | Doctor: " + doctorId +
           " | Patient: " + patientId +
           " | DateTime: " + dateTime +
           (priority ? " | EMERGENCY" : "");
}

}
