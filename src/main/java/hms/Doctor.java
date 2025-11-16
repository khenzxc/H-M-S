package hms;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Doctor {
    private String doctorId;
    private String firstName;
    private String lastName;
    private String specialization;
    private List<String> schedule = new ArrayList<>(); 
    private boolean active = true;

    private Queue<Appointment> normalQueue;
    private PriorityQueue<Appointment> emergencyQueue;

    private List<Appointment> appointments;

    public Doctor(String id, String fn, String ln, String spec) {
        this.doctorId = id;
        this.firstName = fn;
        this.lastName = ln;
        this.specialization = spec;

        //kailangan nya ng comparator kase custom object yung Appointment para ma sort
        this.emergencyQueue = new PriorityQueue<>(Comparator.comparing(Appointment::getDateTime));
        this.normalQueue = new PriorityQueue<>(Comparator.comparing(Appointment::getDateTime));

        this.appointments = new ArrayList<>();
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public List<String> getSchedule() {
        return schedule;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public Queue<Appointment> getNormalQueue() {
        return normalQueue;
    }

    public PriorityQueue<Appointment> getEmergencyQueue() {
        return emergencyQueue;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setFirstName(String fn) {
        this.firstName = fn;
    }

    public void setLastName(String ln) {
        this.lastName = ln;
    }

    public void setSpecialization(String spec) {
        this.specialization = spec;
    }

    public void clearSchedule() {
        schedule.clear();
    }

    public void addSchedule(String scheduleText) {
        schedule.clear();
        String[] parts = scheduleText.split(",");
        for (String s : parts) {
            schedule.add(s.trim().toUpperCase());
        }
    }

    public void addSchedule(String day, LocalTime start, LocalTime end) {
        String entry = day.toUpperCase() + " " + start.toString() + "-" + end.toString();
        schedule.add(entry);
    }

    public List<LocalTime> getAvailableSlots(LocalDate date) {
        List<LocalTime> available = new ArrayList<>();
        DayOfWeek day = date.getDayOfWeek();
        String dayStr = day.name().substring(0, 3);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");

        for (String entry : schedule) {
            if (!entry.startsWith(dayStr))
                continue;

            String[] times = entry.split(" ")[1].split("-");
            LocalTime start = LocalTime.parse(times[0], fmt);
            LocalTime end = LocalTime.parse(times[1], fmt);

            LocalTime t = start;
            while (!t.plusMinutes(45).isAfter(end)) {
                boolean booked = false;
                for (Appointment a : appointments) {
                    if (a.getDateTime().toLocalDate().equals(date)
                            && a.getDateTime().toLocalTime().equals(t)) {
                        booked = true;
                        break;
                    }
                }
                if (!booked)
                    available.add(t);
                t = t.plusMinutes(60); // 45 min + 15 min break
            }
        }

        return available;
    }

    public void enqueueAppointment(Appointment a) {
        appointments.add(a);
        if (a.isPriority())
            emergencyQueue.add(a);
        else
            normalQueue.add(a);
    }

    public Appointment dequeueNextAppointment() {
        if (!emergencyQueue.isEmpty())
            return emergencyQueue.poll();
        return normalQueue.poll();
    }

    public Appointment peekNextAppointment() {
        if (!emergencyQueue.isEmpty())
            return emergencyQueue.peek();
        return normalQueue.peek();
    }

    public boolean hasPendingAppointments() {
        return !(normalQueue.isEmpty() && emergencyQueue.isEmpty());
    }

    public boolean removeAppointment(Appointment a) {
        if (a == null)
            return false;

        boolean removed = false;
        if (normalQueue.contains(a)) {
            normalQueue.remove(a);
            removed = true;
        }
        if (emergencyQueue.contains(a)) {
            emergencyQueue.remove(a);
            removed = true;
        }
        if (appointments.contains(a)) {
            appointments.remove(a);
            removed = true;
        }

        return removed;
    }

    public boolean removeAppointmentById(String id) {
        boolean removed = false;
        // Normal Queue
        int normalSize = normalQueue.size();
        for (int i = 0; i < normalSize; i++) {
            Appointment a = normalQueue.poll();
            if (!a.getAppointmentId().equals(id))
                normalQueue.add(a);
            else
                removed = true;
        }

        // Emergency Queue
        int emergencySize = emergencyQueue.size();
        for (int i = 0; i < emergencySize; i++) {
            Appointment a = emergencyQueue.poll();
            if (!a.getAppointmentId().equals(id))
                emergencyQueue.add(a);
            else
                removed = true;
        }

        // Appointments list
        for (int i = 0; i < appointments.size(); i++) {
            Appointment a = appointments.get(i);
            if (a.getAppointmentId().equals(id)) {
                appointments.remove(i);
                removed = true;
                break;
            }
        }

        return removed;
    }

    @Override
    public String toString() {
        return doctorId + " | " + getFullName() + " | " + specialization +
                " | Schedule: " + schedule +
                " | Appointments: " + appointments.size() +
                " | Queue -> Normal: " + normalQueue.size() +
                ", Emergency: " + emergencyQueue.size();
    }
  
}
