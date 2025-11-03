package hms;

import java.time.*;
import java.util.*;

public class AppointmentManager {
    private List<Appointment> allRecords;
    private long insertionCounter;
    private DoctorManager doctorManager;
    private PatientManager patientManager;
    private final Scanner sc = new Scanner(System.in);

    public AppointmentManager(DoctorManager doctorManager, PatientManager patientManager) {
        this.doctorManager = doctorManager;
        this.patientManager = patientManager;
        this.allRecords = new LinkedList<>();
        this.insertionCounter = 0;
    }

    /* ---------------------- 1. Schedule Appointment ---------------------- */
    public void scheduleAppointment() {

        // Show patients
        patientManager.viewAllPatients();
        System.out.print("Enter Patient ID: ");
        String patientId = sc.nextLine().trim();

        Patient patient = findPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        boolean isEmergency = patient.getPriorityLevel() == 1; // automatic
        LocalDate selectedDate = null;

        if (!isEmergency) {
            // Normal patient → ask for date
            System.out.print("Enter appointment date (YYYY-MM-DD): ");
            String dateInput = sc.nextLine().trim();
            try {
                selectedDate = LocalDate.parse(dateInput);

                // Check if date is in the past
                if (selectedDate.isBefore(LocalDate.now())) {
                    System.out.println("Cannot schedule an appointment in the past.");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Invalid date format.");
                return;
            }
        } else {
            // Emergency → today's date only
            selectedDate = LocalDate.now();
        }

        // ---------------- Filter doctors based on availability ----------------
        List<Doctor> availableDoctors = new ArrayList<>();
        for (Doctor d : doctorManager.getDoctorList()) {
            List<LocalTime> slots = d.getAvailableSlots(selectedDate);
            if (!slots.isEmpty()) {
                availableDoctors.add(d);
            }
        }

        if (availableDoctors.isEmpty()) {
            System.out.println("No doctors available on " + selectedDate + " for "
                    + (isEmergency ? "emergency." : "this date."));
            return;
        }

        // Show filtered doctors
        System.out.println("\n--- Available Doctors ---");
        for (Doctor d : availableDoctors) {
            System.out.println(d);
        }

        System.out.print("Enter Doctor ID: ");
        String docId = sc.nextLine().trim();

        Doctor doctor = findDoctorById(docId);
        if (doctor == null || !availableDoctors.contains(doctor)) {
            System.out.println("Invalid doctor selection.");
            return;
        }

        LocalDateTime dateTime = null;

        if (isEmergency) {
            // Assign earliest available slot today
            List<LocalTime> todaySlots = doctor.getAvailableSlots(selectedDate);
            dateTime = LocalDateTime.of(selectedDate, todaySlots.get(0));
        } else {
            // Normal patient → choose slot manually
            List<LocalTime> availableSlots = doctor.getAvailableSlots(selectedDate);
            System.out.println("Available slots:");
            for (int i = 0; i < availableSlots.size(); i++) {
                System.out.println((i + 1) + ". " + availableSlots.get(i));
            }

            int slotChoice = -1;
            while (slotChoice < 1 || slotChoice > availableSlots.size()) {
                System.out.print("Select a slot number: ");
                try {
                    slotChoice = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number.");
                }
            }
            dateTime = LocalDateTime.of(selectedDate, availableSlots.get(slotChoice - 1));
        }

        // Create appointment
        Appointment appt = new Appointment(
                doctor.getDoctorId(),
                patient.getPatientId(),
                dateTime,
                isEmergency,
                insertionCounter++);

        // Enqueue appointment
        doctor.enqueueAppointment(appt);
        allRecords.add(appt);

        System.out.println("Appointment scheduled successfully!");
        System.out.println("→ " + doctor.getFullName() + " | " + patient.getFullName() +
                " | " + dateTime + " | Emergency: " + (isEmergency ? "Yes" : "No"));
    }

    /* ---------------------- 2. View Next Appointment ---------------------- */
    public void viewNextAppointment() {

        System.out.print("View for (1) Doctor or (2) Patient? Enter 1 or 2: ");
        String input = sc.nextLine().trim();
        int choice;

        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter 1 or 2.");
            return;
        }

        if (choice == 1) {
            doctorManager.viewAllDoctors();
            System.out.print("Enter Doctor ID: ");
            String id = sc.nextLine();
            Doctor doctor = findDoctorById(id);

            if (doctor == null) {
                System.out.println("Doctor not found.");
                return;
            }

            Appointment next = doctor.peekNextAppointment();
            if (next == null) {
                System.out.println("No appointments in queue.");
            } else {
                int queueNum = getQueueNumber(doctor, next);
                System.out.println("Next appointment | Queue #" + queueNum + ": " + next);
            }
        } else if (choice == 2) {
            System.out.print("Enter Patient ID: ");
            String patientId = sc.nextLine();
            boolean found = false;

            for (Doctor doctor : doctorManager.getDoctorList()) {
                for (Appointment appt : doctor.getAppointments()) {
                    if (appt.getPatientId().equalsIgnoreCase(patientId)) {
                        int queueNum = getQueueNumber(doctor, appt);
                        System.out.println("Doctor: " + doctor.getFullName());
                        System.out.println("Next appointment | Queue #" + queueNum + ": " + appt);
                        found = true;
                        break;
                    }
                }
                if (found)
                    break;
            }

            if (!found) {
                System.out.println("No upcoming appointments for this patient.");
            }
        } else {
            System.out.println("Invalid choice. Please enter 1 or 2.");
        }
    }

    // Helper to calculate queue number
    private int getQueueNumber(Doctor doctor, Appointment appt) {
        int pos = 1;
        for (Appointment a : doctor.getEmergencyQueue()) {
            if (a.equals(appt))
                return pos;
            pos++;
        }
        for (Appointment a : doctor.getNormalQueue()) {
            if (a.equals(appt))
                return pos;
            pos++;
        }
        return -1; // not found
    }

    /* ---------------------- 3. Serve Appointment ---------------------- */
    public void serveAppointment() {
        doctorManager.viewAllDoctors();
        System.out.print("Enter Doctor ID to serve: ");
        String id = sc.nextLine();

        Doctor doctor = findDoctorById(id);
        if (doctor == null) {
            System.out.println("Doctor not found.");
            return;
        }

        Appointment next = doctor.dequeueNextAppointment();
        if (next == null) {
            System.out.println("No pending appointments for this doctor.");
        } else {
            System.out.println("Now serving: " + next);
        }
    }

    /* ---------------------- 4. Cancel Appointment ---------------------- */
    public void cancelAppointment() {
        System.out.print("Enter Appointment ID to cancel: ");
        String appointmentId = sc.nextLine().trim();

        Iterator<Appointment> it = allRecords.iterator();
        boolean found = false;

        while (it.hasNext()) {
            Appointment a = it.next();
            if (a.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                Doctor d = findDoctorById(a.getDoctorId());
                if (d != null)
                    d.removeAppointment(a); // remove from doctor's queues
                it.remove(); // remove from global records
                found = true;
                System.out.println("Appointment cancelled for Appointment ID: " + appointmentId);
                break; // appointment IDs are unique, so we can stop here
            }
        }

        if (!found)
            System.out.println("No appointment found with Appointment ID: " + appointmentId);
    }

    /* ---------------------- 5. Reschedule Appointment ---------------------- */
    public void rescheduleAppointment() {

        // Step 1: Ask for Appointment ID
        System.out.print("Enter Appointment ID to reschedule: ");
        String apptId = sc.nextLine();

        Appointment target = null;
        for (Appointment a : allRecords) {
            if (a.getAppointmentId().equalsIgnoreCase(apptId)) {
                target = a;
                break;
            }
        }

        if (target == null) {
            System.out.println("No appointment found with that ID.");
            return;
        }

        Doctor doctor = findDoctorById(target.getDoctorId());
        if (doctor == null) {
            System.out.println("Doctor not found.");
            return;
        }

        // Step 2: Remove the appointment temporarily
        doctor.removeAppointment(target);

        // Step 3: Ask for new date
        System.out.print("Enter new appointment date (YYYY-MM-DD): ");
        String dateInput = sc.nextLine();
        LocalDate newDate;
        try {
            newDate = LocalDate.parse(dateInput);
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            // Re-add the original appointment before returning
            doctor.enqueueAppointment(target);
            return;
        }

        // Step 4: Show available slots for that date
        List<LocalTime> availableSlots = doctor.getAvailableSlots(newDate);
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots on this date.");
            // Re-add the original appointment
            doctor.enqueueAppointment(target);
            return;
        }

        System.out.println("Available slots for " + doctor.getFullName() + " on " + newDate + ":");
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.println((i + 1) + ". " + availableSlots.get(i));
        }

        System.out.print("Select slot number: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice < 1 || choice > availableSlots.size()) {
            System.out.println("❌ Invalid choice.");
            // Re-add the original appointment
            doctor.enqueueAppointment(target);
            return;
        }

        // Step 5: Set new datetime and re-add to doctor queue
        LocalTime newTime = availableSlots.get(choice - 1);
        LocalDateTime newDateTime = LocalDateTime.of(newDate, newTime);
        target.setDateTime(newDateTime);
        doctor.enqueueAppointment(target);

        System.out.println("Appointment rescheduled successfully!");
        System.out.println("Doctor: " + doctor.getFullName() + " | Patient: " + target.getPatientId()
                + " | Date & Time: " + target.getDateTime());
    }

    /*
     * ---------------------- View All Scheduled Appointments ----------------------
     */
    public void viewAllAppointments() {
        System.out.println("\n=== All Scheduled Appointments ===");

        for (Doctor doctor : doctorManager.getDoctorList()) {
            List<Appointment> allAppts = new ArrayList<>();
            allAppts.addAll(doctor.getEmergencyQueue());
            allAppts.addAll(doctor.getNormalQueue());

            if (allAppts.isEmpty())
                continue;

            System.out.println("\n--- Doctor: " + doctor.getFullName() + " ---");
            int counter = 1;
            for (Appointment appt : allAppts) {
                Patient patient = findPatientById(appt.getPatientId());
                System.out.println("Queue #" + counter
                        + " | Appointment ID: " + appt.getAppointmentId()
                        + " | Patient: " + (patient != null ? patient.getFullName() : "Unknown")
                        + " | Date & Time: " + appt.getDateTime()
                        + " | Emergency: " + (appt.isPriority() ? "Yes" : "No"));
                counter++;
            }
        }
    }

    /* ---------------------- Helper Methods ---------------------- */
    private Doctor findDoctorById(String id) {
        for (Doctor d : doctorManager.getDoctorList()) {
            if (d.getDoctorId().equalsIgnoreCase(id))
                return d;
        }
        return null;
    }

    private Patient findPatientById(String id) {
        for (Patient p : patientManager.getPatientList()) {
            if (p.getPatientId().equalsIgnoreCase(id))
                return p;
        }
        return null;
    }

}
