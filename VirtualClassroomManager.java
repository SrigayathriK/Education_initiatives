// VirtualClassroomManager.java
// Console-based Virtual Classroom Manager

import java.util.*;
import java.util.logging.*;

// ======== Student Class ========
class Student {
    private final String id;
    private final String name;
    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    @Override
    public String toString() {
        return id + " - " + name;
    }
}

// ======== Assignment Class ========
class Assignment {
    private final String id;
    private final String title;
    private final String dueDate;
    private boolean submitted;

    public Assignment(String id, String title, String dueDate) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.submitted = false;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDueDate() { return dueDate; }
    public boolean isSubmitted() { return submitted; }
    public void submit() { submitted = true; }

    @Override
    public String toString() {
        return id + " - " + title + " (Due: " + dueDate + ") " +
               (submitted ? "[Submitted]" : "[Pending]");
    }
}

// ======== Classroom Class ========
class Classroom {
    private final String name;
    private final Map<String, Student> students = new LinkedHashMap<>();
    private final Map<String, Assignment> assignments = new LinkedHashMap<>();

    public Classroom(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public boolean addStudent(Student s) {
        if (students.containsKey(s.getId())) return false;
        students.put(s.getId(), s);
        return true;
    }

    public boolean addAssignment(Assignment a) {
        if (assignments.containsKey(a.getId())) return false;
        assignments.put(a.getId(), a);
        return true;
    }

    public Student getStudent(String id) { return students.get(id); }
    public Assignment getAssignment(String id) { return assignments.get(id); }

    public Collection<Student> getAllStudents() { return students.values(); }
    public Collection<Assignment> getAllAssignments() { return assignments.values(); }
}

// ======== ClassroomManager Singleton ========
class ClassroomManager {
    private static final ClassroomManager INSTANCE = new ClassroomManager();
    private final Map<String, Classroom> classrooms = new LinkedHashMap<>();
    private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("VCM");

    private ClassroomManager() {
        try {
            LogManager.getLogManager().reset();
            logger.setLevel(Level.INFO);
            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(Level.INFO);
            logger.addHandler(ch);
        } catch (Exception ignored) {}
    }

    public static ClassroomManager getInstance() {
        return INSTANCE;
    }

    public boolean addClassroom(String name) {
        if (classrooms.containsKey(name)) {
            logger.warning("Classroom already exists: " + name);
            return false;
        }
        classrooms.put(name, new Classroom(name));
        logger.info("Classroom " + name + " created.");
        return true;
    }

    public boolean removeClassroom(String name) {
        if (classrooms.remove(name) != null) {
            logger.info("Classroom " + name + " removed.");
            return true;
        }
        logger.warning("Classroom " + name + " not found.");
        return false;
    }

    public Classroom getClassroom(String name) {
        return classrooms.get(name);
    }

    public Collection<Classroom> getAllClassrooms() {
        return classrooms.values();
    }
}

// ======== Main Application ========
public class VirtualClassroomManager {
    private static final Scanner sc = new Scanner(System.in);
    private static final ClassroomManager manager = ClassroomManager.getInstance();

    public static void main(String[] args) {
        System.out.println("=== Virtual Classroom Manager ===");
        boolean running = true;
        while (running) {
            showMenu();
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": addClassroom(); break;
                    case "2": listClassrooms(); break;
                    case "3": removeClassroom(); break;
                    case "4": addStudent(); break;
                    case "5": listStudents(); break;
                    case "6": scheduleAssignment(); break;
                    case "7": submitAssignment(); break;
                    case "8": listAssignments(); break;
                    case "0": running = false; break;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        System.out.println("Exiting... Bye!");
    }

    private static void showMenu() {
        System.out.println("\n1) Add Classroom");
        System.out.println("2) List Classrooms");
        System.out.println("3) Remove Classroom");
        System.out.println("4) Add Student to Classroom");
        System.out.println("5) List Students in Classroom");
        System.out.println("6) Schedule Assignment for Classroom");
        System.out.println("7) Submit Assignment");
        System.out.println("8) List Assignments in Classroom");
        System.out.println("0) Exit");
    }

    private static void addClassroom() {
        System.out.print("Enter classroom name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("Name cannot be empty."); return; }
        if (manager.addClassroom(name)) {
            System.out.println("Classroom [" + name + "] has been created.");
        } else {
            System.out.println("Classroom already exists.");
        }
    }

    private static void listClassrooms() {
        if (manager.getAllClassrooms().isEmpty()) {
            System.out.println("No classrooms available.");
            return;
        }
        System.out.println("Classrooms:");
        for (Classroom c : manager.getAllClassrooms()) {
            System.out.println("- " + c.getName());
        }
    }

    private static void removeClassroom() {
        System.out.print("Enter classroom name to remove: ");
        String name = sc.nextLine().trim();
        if (!manager.removeClassroom(name)) {
            System.out.println("Classroom not found.");
        } else {
            System.out.println("Classroom removed.");
        }
    }

    private static void addStudent() {
        System.out.print("Enter classroom name: ");
        String cname = sc.nextLine().trim();
        Classroom c = manager.getClassroom(cname);
        if (c == null) { System.out.println("Classroom not found."); return; }
        System.out.print("Enter student ID: ");
        String sid = sc.nextLine().trim();
        System.out.print("Enter student name: ");
        String sname = sc.nextLine().trim();
        if (c.addStudent(new Student(sid, sname))) {
            System.out.println("Student [" + sid + "] enrolled in " + cname);
        } else {
            System.out.println("Student ID already exists in this class.");
        }
    }

    private static void listStudents() {
        System.out.print("Enter classroom name: ");
        String cname = sc.nextLine().trim();
        Classroom c = manager.getClassroom(cname);
        if (c == null) { System.out.println("Classroom not found."); return; }
        if (c.getAllStudents().isEmpty()) {
            System.out.println("No students enrolled.");
            return;
        }
        System.out.println("Students in " + cname + ":");
        for (Student s : c.getAllStudents()) {
            System.out.println("- " + s);
        }
    }

    private static void scheduleAssignment() {
        System.out.print("Enter classroom name: ");
        String cname = sc.nextLine().trim();
        Classroom c = manager.getClassroom(cname);
        if (c == null) { System.out.println("Classroom not found."); return; }
        System.out.print("Enter assignment ID: ");
        String aid = sc.nextLine().trim();
        System.out.print("Enter assignment title: ");
        String atitle = sc.nextLine().trim();
        System.out.print("Enter due date (e.g. 2025-10-10): ");
        String adue = sc.nextLine().trim();
        if (c.addAssignment(new Assignment(aid, atitle, adue))) {
            System.out.println("Assignment scheduled for " + cname);
        } else {
            System.out.println("Assignment ID already exists.");
        }
    }

    private static void submitAssignment() {
        System.out.print("Enter classroom name: ");
        String cname = sc.nextLine().trim();
        Classroom c = manager.getClassroom(cname);
        if (c == null) { System.out.println("Classroom not found."); return; }
        System.out.print("Enter student ID: ");
        String sid = sc.nextLine().trim();
        if (c.getStudent(sid) == null) { System.out.println("Student not found."); return; }
        System.out.print("Enter assignment ID: ");
        String aid = sc.nextLine().trim();
        Assignment a = c.getAssignment(aid);
        if (a == null) { System.out.println("Assignment not found."); return; }
        a.submit();
        System.out.println("Assignment submitted by Student [" + sid + "] in [" + cname + "]");
    }

    private static void listAssignments() {
        System.out.print("Enter classroom name: ");
        String cname = sc.nextLine().trim();
        Classroom c = manager.getClassroom(cname);
        if (c == null) { System.out.println("Classroom not found."); return; }
        if (c.getAllAssignments().isEmpty()) {
            System.out.println("No assignments scheduled.");
            return;
        }
        System.out.println("Assignments in " + cname + ":");
        for (Assignment a : c.getAllAssignments()) {
            System.out.println("- " + a);
        }
    }
}

