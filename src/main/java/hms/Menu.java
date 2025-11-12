package hms;

import java.util.Scanner;

public class Menu {
    private Scanner sc = new Scanner(System.in);
    private DoctorManager doctorManager;
    private PatientManager patientManager;
    private AppointmentManager appointmentManager;
    private TreatmentRecordManager treatmentManager;

    public Menu(DoctorManager doctorManager, PatientManager patientManager, AppointmentManager appointmentManager,
            TreatmentRecordManager treatmentManager) {
        this.doctorManager = doctorManager;
        this.patientManager = patientManager;
        this.appointmentManager = appointmentManager;
        this.treatmentManager = treatmentManager;
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n===== HOSPITAL MANAGEMENT SYSTEM =====");
            System.out.println("[1] Patient Management");
            System.out.println("[2] Doctor Management");
            System.out.println("[3] Appointment Scheduling");
            System.out.println("[4] Treatment Records & History");
            System.out.println("[5] Summary and Reports");
            System.out.println("[6] Search & Sort Patients/Doctors");
            System.out.println("[0] Exit");
            System.out.print("Select option: ");
            String choice = sc.nextLine();

            switch (choice) {

                case "1" -> patientMenu();
                case "2" -> doctorMenu();
                case "3" -> appointmentMenu();
                case "4" -> treatmentMenu();
                case "5" -> reportsMenu();
                case "6" -> searchSortMenu();
                case "0" -> {
                    System.out.println("Exiting system... Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    /* ===================== PATIENT MENU ===================== */
    private void patientMenu() {
        while (true) {
            System.out.println("\n--- PATIENT MANAGEMENT ---");
            System.out.println("[1] Register Patient");
            System.out.println("[2] View Patients (Paginated)");
            System.out.println("[3] View Patient Profile");
            System.out.println("[4] Update Patient Info");
            System.out.println("[5] Delete Patient");
            System.out.println("[0] Back to Main Menu");
            System.out.print("Enter choice: ");
            String c = sc.nextLine();

            switch (c) {
                case "1" -> patientManager.registerPatient();
                case "2" -> patientManager.viewAllPatientsPaginated();
                case "3" -> patientManager.viewSinglePatient();
                case "4" -> patientManager.updatePatient();
                case "5" -> patientManager.deletePatient();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid input!");
            }
        }
    }

    /* ===================== DOCTOR MENU ===================== */
    private void doctorMenu() {
        while (true) {
            System.out.println("\n--- DOCTOR MANAGEMENT ---");
            System.out.println("[1] Register Doctor");
            System.out.println("[2] View All Doctors");
            System.out.println("[3] Edit Doctor");
            System.out.println("[4] Delete Doctor");
            System.out.println("[0] Back to Main Menu");
            System.out.print("Enter choice: ");
            String c = sc.nextLine();

            switch (c) {
                case "1" -> doctorManager.addDoctor();
                case "2" -> doctorManager.viewAllDoctors();
                case "3" -> doctorManager.editDoctor();
                case "4" -> doctorManager.deleteDoctor();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid input!");
            }
        }
    }

    /* ===================== APPOINTMENT MENU ===================== */
    private void appointmentMenu() {
        while (true) {
            System.out.println("\n--- APPOINTMENT SCHEDULING ---");
            System.out.println("[1] Schedule New Appointment");
            System.out.println("[2] View Next Appointment");
            System.out.println("[3] Serve / Attend Appointment");
            System.out.println("[4] View All Scheduled Appointments");
            System.out.println("[5] Cancel Appointment");
            System.out.println("[6] Reschedule Appointment");
            System.out.println("[0] Back to Main Menu");
            System.out.print("Enter choice: ");
            String c = sc.nextLine();

            switch (c) {
                case "1" -> appointmentManager.scheduleAppointment();
                case "2" -> appointmentManager.viewNextAppointment();
                case "3" -> appointmentManager.serveAppointment();
                case "4" -> appointmentManager.viewAllAppointments();
                case "5" -> appointmentManager.cancelAppointment();
                case "6" -> appointmentManager.rescheduleAppointment();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid input!");
            }
        }
    }

    private void reportsMenu() {
        while (true) {
            System.out.println("\n--- SUMMARY AND REPORTS ---");
            System.out.println("[1] Patient Masterlist (Filterable)");
            System.out.println("[2] Appointments Daily Schedule");
            System.out.println("[3] Appointments Upcoming (Next N Days)");
            System.out.println("[4] Treatment History per Patient");
            System.out.println("[0] Back");
            System.out.print("Enter choice: ");
            String c = sc.nextLine();

            switch (c) {
                case "1" -> patientManager.filterPatientMasterlist();
                case "2" -> appointmentManager.viewDailySchedule();
                case "3" -> appointmentManager.viewUpcomingAppointments();
                case "4" -> treatmentManager.viewPatientHistory();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid input!");
            }
        }
    }

    private void searchSortMenu() {
        while (true) {
            System.out.println("\n--- SEARCH & SORT ---");
            System.out.println("[1] Patients");
            System.out.println("[2] Doctors");
            System.out.println("[0] Back to Main Menu");
            System.out.print("Select option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> patientSearchSortMenu();
                case "2" -> doctorSearchSortMenu();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid input! Please select 0, 1, or 2.");
            }
        }
    }

    private void patientSearchSortMenu() {
        while (true) {
            System.out.println("\n--- PATIENT SEARCH & SORT ---");
            System.out.println("[1] Linear Search by Name");
            System.out.println("[2] Binary Search by ID");
            System.out.println("[3] Bubble Sort by ID");
            System.out.println("[0] Back");
            System.out.print("Select option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.print("Enter patient name to search: ");
                    String name = sc.nextLine();
                    long start = System.nanoTime();
                    SearchingSorting.linearSearchPatientsByName(patientManager.getPatientList(), name);
                    long end = System.nanoTime();
                    double timeMs = (end - start) / 1_000_000.0;
                    System.out.println("Seatch Time: " + timeMs + " ms");
                }
                case "2" -> {
                    SearchingSorting.bubbleSortPatientsById(patientManager.getPatientList()); 
                    System.out.print("Enter patient ID to search: ");
                    String id = sc.nextLine();
                    long start = System.nanoTime();
                    Patient p = SearchingSorting.binarySearchPatientById(patientManager.getPatientList(), id);
                    long end = System.nanoTime();
                    double timeMs = (end - start) / 1_000_000.0;
                    System.out.println(p != null ? p : "Patient not found with ID: " + id);
                    System.out.println("Seatch Time: " + timeMs + " ms");
                }
                case "3" -> {
                    SearchingSorting.bubbleSortPatientsById(patientManager.getPatientList());
                    System.out.println("Patients after Bubble Sort by ID:");
                    patientManager.viewAllPatients();
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid input! Try again.");
            }
        }
    }

    private void doctorSearchSortMenu() {
        while (true) {
            System.out.println("\n--- DOCTOR SEARCH & SORT ---");
            System.out.println("[1] Linear Search by Name");
            System.out.println("[2] Binary Search by ID");
            System.out.println("[3] Bubble Sort by ID");
            System.out.println("[0] Back");
            System.out.print("Select option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.print("Enter doctor name to search: ");
                    String name = sc.nextLine();
                    SearchingSorting.linearSearchDoctorsByName(doctorManager.getDoctorList(), name);
                }
                case "2" -> {
                    SearchingSorting.bubbleSortDoctorsById(doctorManager.getDoctorList()); 
                    System.out.print("Enter doctor ID to search: ");
                    String id = sc.nextLine();
                    long start = System.nanoTime();
                    Doctor d = SearchingSorting.binarySearchDoctorById(doctorManager.getDoctorList(), id);
                    long end = System.nanoTime();
                    double timeMs = (end - start) / 1_000_000.0;
                    System.out.println(d != null ? d : "Doctor not found with ID: " + id);
                    System.out.println("Searching Time: " + timeMs + " ms");
                }
                case "3" -> {
                    SearchingSorting.bubbleSortDoctorsById(doctorManager.getDoctorList());
                    System.out.println("Doctors after Bubble Sort by ID:");
                    doctorManager.viewAllDoctors();
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid input! Try again.");
            }
        }
    }

    private void treatmentMenu() {
        while (true) {
            System.out.println("\n--- TREATMENT RECORDS ---");
            System.out.println("[1] Add Treatment Record");
            System.out.println("[2] View Patient Treatment History");
            System.out.println("[0] Back");
            System.out.print("Enter choice: ");
            String c = sc.nextLine();

            switch (c) {
                case "1" -> treatmentManager.addTreatmentRecord();
                case "2" -> treatmentManager.viewPatientHistory();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid input!");
            }
        }
    }

}
