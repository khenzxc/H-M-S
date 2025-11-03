package hms;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Scanner;

public class PatientManager {
    private LinkedList<Patient> patientList;
    private int nextId = 1;
    private final Scanner sc = new Scanner(System.in);

    public PatientManager() {
        patientList = new LinkedList<>();
    }

    // Seed sample data
  public void seedData() {
    patientList.add(new Patient("P0001", "Khen", "Gabriel", LocalDate.of(2006, 3, 25)));
    patientList.add(new Patient("P0002", "Ellaine", "Romero", LocalDate.of(2006, 2, 14)));
    patientList.add(new Patient("P0003", "Kent", "Dela Cruz", LocalDate.of(2005, 5, 8)));
    patientList.add(new Patient("P0004", "Yuki", "Ablen", LocalDate.of(2006, 6, 12)));
    patientList.add(new Patient("P0005", "Andrew", "Baluyot", LocalDate.of(2005, 11, 30)));
    patientList.add(new Patient("P0006", "Paul", "Cruz", LocalDate.of(2006, 1, 20)));
    patientList.add(new Patient("P0007", "Dustin", "Sacdalan", LocalDate.of(2005, 9, 15)));
    patientList.add(new Patient("P0008", "Aira", "Marcelino", LocalDate.of(2006, 4, 5)));
    
    nextId = 9; 
}

    /* ---------------- REGISTER PATIENT ---------------- */
    public void registerPatient() {
        System.out.println("\n--- Register Patient ---");
        System.out.print("First Name: "); String fn = sc.nextLine().trim();
        System.out.print("Last Name: "); String ln = sc.nextLine().trim();
        System.out.print("Date of Birth (YYYY-MM-DD): ");
        LocalDate dob;
        try { dob = LocalDate.parse(sc.nextLine().trim()); }
        catch (Exception e) { System.out.println("Invalid date."); return; }

        // Check duplicate
        for (Patient p : patientList) {
            if (p.getFirstName().equalsIgnoreCase(fn) &&
                p.getLastName().equalsIgnoreCase(ln) &&
                p.getDob().equals(dob)) {
                System.out.println("Patient already exists!");
                return;
            }
        }

        System.out.print("Gender (M/F): "); String gender = sc.nextLine().trim().toUpperCase();
        System.out.print("Contact: "); String contact = sc.nextLine().trim();
        System.out.print("Address: "); String address = sc.nextLine().trim();
        System.out.print("Blood Type: "); String blood = sc.nextLine().trim();
        System.out.print("Priority (0=Normal, 1=Urgent): "); int prio = Integer.parseInt(sc.nextLine().trim());

        String id = String.format("P%04d", nextId++);
        Patient p = new Patient(id, fn, ln, dob, gender, contact, address, blood, null, null, null, prio);
        patientList.add(p);

        System.out.println("Patient registered successfully!");
        System.out.println(p);
    }

    /* ---------------- VIEW ALL PATIENTS ---------------- */
  public void viewAllPatientsPaginated() {
    if (patientList.isEmpty()) {
        System.out.println("No patients found.");
        return;
    }

    final int pageSize = 5;
    int total = patientList.size();
    int totalPages = total / pageSize;
    if (total % pageSize != 0) totalPages++; // extra page if remainder
    int currentPage = 1;

    while (true) {
        int start = (currentPage - 1) * pageSize;
        int end = start + pageSize;
        if (end > total) end = total;

        System.out.println("\n-- Patients (Page " + currentPage + " of " + totalPages + ") --");
        System.out.println("ID | Name | Age | Sex | Blood | Pri");
        System.out.println("------------------------------------");

        // Use toString() instead of printBrief
        for (int i = start; i < end; i++) {
            System.out.println(patientList.get(i).toString());
        }

        System.out.println("[N] Next | [P] Prev | [V <ID>] View | [Q] Quit");
        System.out.print("Choice: ");
        String line = sc.nextLine().trim();

        if (line.equalsIgnoreCase("N") && currentPage < totalPages) {
            currentPage++;
        } else if (line.equalsIgnoreCase("P") && currentPage > 1) {
            currentPage--;
        } else if (line.equalsIgnoreCase("Q")) {
            break;
        } else if (line.toUpperCase().startsWith("V ")) {
            String[] parts = line.split("\\s+");
            if (parts.length >= 2) {
                Patient selected = findPatientById(parts[1]);
                if (selected != null) {
                    // Use your existing detailed view method
                    System.out.println(selected.toFullProfileString());
                } else {
                    System.out.println("Patient not found.");
                }
                System.out.println("Press Enter to continue...");
                sc.nextLine();
            } else {
                System.out.println("Invalid view command.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }
}


    public void viewAllPatients() {
        if (patientList.isEmpty()) { System.out.println("No patients found."); return; }
        System.out.println("\n--- Patient List ---");
        for (Patient p : patientList) System.out.println(p);
    }

    /* ---------------- VIEW SINGLE PATIENT ---------------- */
    public void viewSinglePatient() {
        System.out.print("Enter Patient ID: ");
        Patient found = findPatientById(sc.nextLine().trim());
        if (found == null) { System.out.println("Patient not found!"); return; }
        System.out.println("\n--- Patient Details ---");
        System.out.println(found.toFullProfileString());
    }

    /* ---------------- UPDATE PATIENT ---------------- */
    public void updatePatient() {
        System.out.print("Enter Patient ID: ");
        Patient found = findPatientById(sc.nextLine().trim());
        if (found == null) { System.out.println("Patient not found!"); return; }

        System.out.println("Editing: " + found.getFullName());

        System.out.print("New Contact (blank to keep): "); String contact = sc.nextLine().trim();
        if (!contact.isEmpty()) found.setContact(contact);

        System.out.print("New Address (blank to keep): "); String address = sc.nextLine().trim();
        if (!address.isEmpty()) found.setAddress(address);

        System.out.print("New Priority (0=Normal,1=Urgent, blank to keep): "); String prioStr = sc.nextLine().trim();
        if (!prioStr.isEmpty()) found.setPriorityLevel(Integer.parseInt(prioStr));

        System.out.println("Patient updated!");
    }

    /* ---------------- DELETE PATIENT ---------------- */
    public void deletePatient() {
        System.out.print("Enter Patient ID: ");
        Patient found = findPatientById(sc.nextLine().trim());
        if (found == null) { System.out.println("Patient not found!"); return; }

        System.out.print("Are you sure to delete " + found.getFullName() + "? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            patientList.remove(found);
            System.out.println("Patient deleted.");
        } else {
            System.out.println("Cancelled.");
        }
    }

    /* ---------------- FIND BY ID ---------------- */
    public Patient findPatientById(String id) {
        for (Patient p : patientList) if (p.getPatientId().equalsIgnoreCase(id)) return p;
        return null;
    }

    public LinkedList<Patient> getPatientList() { return patientList; }
}
