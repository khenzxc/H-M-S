package hms;

public class Main {
    public static void main(String[] args) {
        // Managers
        DoctorManager doctorManager = new DoctorManager();
        PatientManager patientManager = new PatientManager();
        AppointmentManager appointmentManager = new AppointmentManager(doctorManager, patientManager);
        TreatmentRecordManager treatmentManager = new TreatmentRecordManager(patientManager); // ✅ new manager

        // Seed demo data
        doctorManager.seedDoctors(); // Pre-registered doctors with schedules
        patientManager.seedData(); // Pre-registered patients

        // Launch menu
        Menu menu = new Menu(doctorManager, patientManager, appointmentManager, treatmentManager); // ✅ updated
                                                                                                   // constructor
        menu.showMainMenu();
    }
}
