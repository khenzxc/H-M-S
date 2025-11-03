package hms;

public class Main {
    public static void main(String[] args) {
        // Managers
        DoctorManager doctorManager = new DoctorManager();
        PatientManager patientManager = new PatientManager();
        AppointmentManager appointmentManager = new AppointmentManager(doctorManager, patientManager);

        // Seed demo data
        doctorManager.seedDoctors();   // Pre-registered doctors with schedules
        patientManager.seedData(); // Pre-registered patients

        // Launch menu
        Menu menu = new Menu(doctorManager, patientManager, appointmentManager);
        menu.showMainMenu();
    }
}
