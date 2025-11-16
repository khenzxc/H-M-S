package hms;

import java.time.*;
import java.util.*;

public class DoctorManager {
    private LinkedList<Doctor> doctorList;
    private int nextId = 1;
    private final Scanner sc = new Scanner(System.in);

    public DoctorManager() {
        doctorList = new LinkedList<>();
    }

    public void addDoctor() {
        System.out.println("\n--- Register Doctor ---");
        System.out.print("First name: ");
        String fn = sc.nextLine().trim();
        System.out.print("Last name: ");
        String ln = sc.nextLine().trim();
        System.out.print("Specialization: ");
        String spec = sc.nextLine().trim();

        if (fn.isEmpty() || ln.isEmpty() || spec.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }

        String id = String.format("D%04d", nextId++);
        Doctor doc = new Doctor(id, fn, ln, spec);

        System.out.print("Enter schedule (e.g. MON 12:00-18:00, WED 07:00-12:00, FRI 12:00-18:00): ");
        String scheduleInput = sc.nextLine();
        parseSchedule(scheduleInput, doc);

        doctorList.add(doc);
        System.out.println("Doctor registered successfully!");
        System.out.println(doc);
    }

    /*
     * PARSE SCHEDULE INPUT
     * Example input: MON 12:00-18:00, WED 07:00-12:00
     */
    private void parseSchedule(String input, Doctor doc) {
        String[] parts = input.split(",");
        for (String p : parts) {
            p = p.trim();
            if (p.isEmpty())
                continue;

            String[] dayTime = p.split(" ");
            if (dayTime.length != 2)
                continue;

            String day = dayTime[0].trim().toUpperCase();
            String[] times = dayTime[1].split("-");
            if (times.length != 2)
                continue;

            try {
                LocalTime start = LocalTime.parse(times[0]);
                LocalTime end = LocalTime.parse(times[1]);
                if (end.isBefore(start)) {
                    System.out.println("Invalid time range for " + day + " skipped.");
                    continue;
                }
                doc.addSchedule(day, start, end);
            } catch (Exception e) {
                System.out.println("Invalid time format in: " + p);
            }
        }
    }

    public void viewAllDoctors() {
        if (doctorList.isEmpty()) {
            System.out.println("No doctors found.");
            return;
        }
        System.out.println("\n--- Doctor List ---");
        for (Doctor d : doctorList) {
            System.out.println(d);
        }
    }

    public LinkedList<Doctor> getDoctorList() {
        return doctorList;
    }

    public Doctor findDoctorById(String id) {
        for (Doctor d : doctorList) {
            if (d.getDoctorId().equalsIgnoreCase(id))
                return d;
        }
        return null;
    }

    public void editDoctor() {
        System.out.print("Enter Doctor ID to edit: ");
        String id = sc.nextLine().trim();
        Doctor found = findDoctorById(id);

        if (found == null) {
            System.out.println("Doctor not found!");
            return;
        }

        System.out.println("Editing Doctor: " + found.getFullName());
        System.out.print("New First Name (leave blank to keep \"" + found.getFirstName() + "\"): ");
        String fn = sc.nextLine().trim();
        if (!fn.isEmpty()) {
            found.setFirstName(fn);
        }

        System.out.print("New Last Name (leave blank to keep \"" + found.getLastName() + "\"): ");
        String ln = sc.nextLine().trim();
        if (!ln.isEmpty()) {
            found.setLastName(ln);
        }

        System.out.print("New Specialization (leave blank to keep \"" + found.getSpecialization() + "\"): ");
        String spec = sc.nextLine().trim();
        if (!spec.isEmpty()) {
            found.setSpecialization(spec);
        }

        System.out.print("Edit schedule? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            found.clearSchedule();
            System.out.print("Enter new schedule (e.g. MON 12:00-18:00, WED 07:00-12:00): ");
            String sched = sc.nextLine();
            parseSchedule(sched, found);
        }

        System.out.println("Doctor information updated successfully!");
    }

    public void deleteDoctor() {
        System.out.print("Enter Doctor ID to delete: ");
        String id = sc.nextLine().trim();
        Doctor found = findDoctorById(id);

        if (found == null) {
            System.out.println("Doctor not found!");
            return;
        }

        System.out.print("Are you sure you want to delete Dr. " + found.getFullName() + "? (y/n): ");
        String confirm = sc.nextLine().trim();
        if (confirm.equalsIgnoreCase("y")) {
            doctorList.remove(found);
            System.out.println("Doctor deleted successfully!");
        } else {
            System.out.println("Deletion canceled.");
        }
    }

    public void apptPerDoctor() {
        System.out.print("Enter Doctor ID to view appointments: ");
        String id = sc.nextLine().trim();
        Doctor found = findDoctorById(id);

        if (found == null) {
            System.out.println("Doctor not found!");
            return;
        }

        List<Appointment> apps = found.getAppointments();
        if (apps.isEmpty()) {
            System.out.println("No appointments found for Dr. " + found.getFullName());
            return;
        }

        System.out.println("\n--- Appointments for Dr. " + found.getFullName() + " ---");
        for (Appointment a : apps) {
            System.out.println(a);
        }
    }

    public void seedDoctors() {
        Doctor d1 = new Doctor(String.format("D%04d", nextId++), "Alice", "Santos", "General Medicine");
        d1.addSchedule("MON", LocalTime.of(9, 0), LocalTime.of(17, 0));

        Doctor d2 = new Doctor(String.format("D%04d", nextId++), "Mark", "Reyes", "Pediatrics");
        d2.addSchedule("TUE", LocalTime.of(10, 0), LocalTime.of(16, 0));
        d2.addSchedule("THU", LocalTime.of(12, 0), LocalTime.of(18, 0));

        Doctor d3 = new Doctor(String.format("D%04d", nextId++), "Lea", "Cruz", "Neurology");
        d3.addSchedule("FRI", LocalTime.of(9, 0), LocalTime.of(17, 0));
        d3.addSchedule("SAT", LocalTime.of(9, 0), LocalTime.of(12, 0));

        Doctor d4 = new Doctor(String.format("D%04d", nextId++), "Jean Eren", "Fajardo", "Dermatology");
        d4.addSchedule("MON", LocalTime.of(10, 0), LocalTime.of(15, 0));
        d4.addSchedule("THU", LocalTime.of(9, 0), LocalTime.of(13, 0));

        Doctor d5 = new Doctor(String.format("D%04d", nextId++), "Rose", "Reyes", "Ophthalmology");
        d5.addSchedule("TUE", LocalTime.of(9, 0), LocalTime.of(14, 0));
        d5.addSchedule("FRI", LocalTime.of(12, 0), LocalTime.of(17, 0));

        Doctor d6 = new Doctor(String.format("D%04d", nextId++), "Evelyn", "Samson", "Orthopedics");
        d6.addSchedule("WED", LocalTime.of(10, 0), LocalTime.of(16, 0));
        d6.addSchedule("SAT", LocalTime.of(8, 0), LocalTime.of(12, 0));

        Doctor d7 = new Doctor(String.format("D%04d", nextId++), "Josephine", "Bayonito", "ENT");
        d7.addSchedule("MON", LocalTime.of(9, 0), LocalTime.of(12, 0));
        d7.addSchedule("THU", LocalTime.of(13, 0), LocalTime.of(17, 0));

        Doctor d8 = new Doctor(String.format("D%04d", nextId++), "Jonathan", "Tomacruz", "Gastroenterology");
        d8.addSchedule("TUE", LocalTime.of(10, 0), LocalTime.of(16, 0));
        d8.addSchedule("FRI", LocalTime.of(9, 0), LocalTime.of(15, 0));

        doctorList.addAll(Arrays.asList(d1, d2, d3, d4, d5, d6, d7, d8));
    }

}
