package hms;

public class Main {
    public static void main(String[] args) {

        DoctorManager doctorManager = new DoctorManager();
        PatientManager patientManager = new PatientManager();
        AppointmentManager appointmentManager = new AppointmentManager(doctorManager, patientManager);
        TreatmentRecordManager treatmentManager = new TreatmentRecordManager(patientManager); 

        doctorManager.seedDoctors();
        patientManager.seedData(); 

        Menu menu = new Menu(doctorManager, patientManager, appointmentManager, treatmentManager); 
                                                                                        
        menu.showMainMenu();
    }
}
