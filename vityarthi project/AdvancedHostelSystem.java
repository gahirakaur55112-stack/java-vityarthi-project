import java.util.*;

enum RoomType {
    STANDARD(6000.0),
    DELUXE(1000.0),
    SUITE(15000.0);

    private final double monthlyRate;
    RoomType(double rate) { this.monthlyRate = rate; }
    public double getMonthlyRate() { return monthlyRate; }
}

class HostelException extends Exception {
    public HostelException(String message) { super(message); }
}

class Student {
    private final String id;
    private final String name;
    private int assignedRoomNumber;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.assignedRoomNumber = -1;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAssignedRoomNumber() { return assignedRoomNumber; }
    public void setAssignedRoomNumber(int roomNumber) { this.assignedRoomNumber = roomNumber; }

    @Override
    public String toString() {
        return String.format("Student ID: %s | Name: %-15s | Room: %s", 
                id, name, (assignedRoomNumber == -1 ? "Unassigned" : assignedRoomNumber));
    }
}

class Room {
    private final int roomNumber;
    private final RoomType type;
    private final int capacity;
    private final List<Student> occupants;

    public Room(int roomNumber, RoomType type, int capacity) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.capacity = capacity;
        this.occupants = new ArrayList<>();
    }

    public int getRoomNumber() { return roomNumber; }
    public RoomType getType() { return type; }
    public boolean isFull() { return occupants.size() >= capacity; }

    public void addOccupant(Student student) throws HostelException {
        if (isFull()) throw new HostelException("Room " + roomNumber + " is already at full capacity.");
        occupants.add(student);
        student.setAssignedRoomNumber(roomNumber);
    }

    public void removeOccupant(Student student) {
        occupants.remove(student);
        student.setAssignedRoomNumber(-1);
    }

    @Override
    public String toString() {
        return String.format("Room %d [%s] | Occupancy: %d/%d | Price: $%.2f/mo", 
                roomNumber, type, occupants.size(), capacity, type.getMonthlyRate());
    }
}

class HostelService {
    private final Map<Integer, Room> rooms = new HashMap<>();
    private final Map<String, Student> students = new HashMap<>();

    public void addRoom(int roomNumber, RoomType type, int capacity) throws HostelException {
        if (rooms.containsKey(roomNumber)) {
            throw new HostelException("Room number " + roomNumber + " already exists.");
        }
        rooms.put(roomNumber, new Room(roomNumber, type, capacity));
    }

    public void registerAndAllocateStudent(String studentId, String name, int durationMonths) throws HostelException {
        if (students.containsKey(studentId)) {
            throw new HostelException("Student ID " + studentId + " is already registered.");
        }

        Room availableRoom = rooms.values().stream()
                .filter(r -> !r.isFull())
                .findFirst()
                .orElseThrow(() -> new HostelException("Allocation failed: No vacant rooms available."));

        Student student = new Student(studentId, name);
        availableRoom.addOccupant(student);
        students.put(studentId, student);

        double totalFee = availableRoom.getType().getMonthlyRate() * durationMonths;
        System.out.printf("Success: %s allocated to Room %d (%s). Total Fee (%d months): $%.2f%n",
                name, availableRoom.getRoomNumber(), availableRoom.getType(), durationMonths, totalFee);
    }

    public void checkoutStudent(String studentId) throws HostelException {
        Student student = students.get(studentId);
        if (student == null) throw new HostelException("Student ID " + studentId + " not found.");

        int roomNumber = student.getAssignedRoomNumber();
        if (roomNumber != -1 && rooms.containsKey(roomNumber)) {
            rooms.get(roomNumber).removeOccupant(student);
        }

        students.remove(studentId);
        System.out.println("Checkout complete. Student " + student.getName() + " checked out of Room " + roomNumber);
    }

    public void displayDashboard() {
        System.out.println("\n--- ROOM STATUS ---");
        rooms.values().forEach(System.out::println);

        System.out.println("\n--- REGISTERED STUDENTS ---");
        if (students.isEmpty()) System.out.println("No active students.");
        else students.values().forEach(System.out::println);
    }
}

public class AdvancedHostelSystem {
    public static void main(String[] args) {
        HostelService service = new HostelService();
        Scanner scanner = new Scanner(System.in);

        try {
            service.addRoom(101, RoomType.STANDARD, 2);
            service.addRoom(201, RoomType.DELUXE, 1);
            service.addRoom(301, RoomType.SUITE, 1);
        } catch (HostelException e) {
            System.err.println("Setup error: " + e.getMessage());
        }

        while (true) {
            System.out.println("\n=== ADVANCED HOSTEL MANAGEMENT ===");
            System.out.println("1. Allocate Room to Student");
            System.out.println("2. Checkout Student");
            System.out.println("3. View Dashboard");
            System.out.println("4. Exit");
            System.out.print("Select choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.print("Enter Student ID: ");
                        String id = scanner.nextLine();
                        System.out.print("Enter Student Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Stay Duration (Months): ");
                        int months = Integer.parseInt(scanner.nextLine());
                        service.registerAndAllocateStudent(id, name, months);
                        break;
                    case 2:
                        System.out.print("Enter Student ID for Checkout: ");
                        String outId = scanner.nextLine();
                        service.checkoutStudent(outId);
                        break;
                    case 3:
                        service.displayDashboard();
                        break;
                    case 4:
                        System.out.println("Exiting system.");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (HostelException e) {
                System.out.println("Operational Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Input Error: Please enter numeric values where requested.");
            }
        }
    }
}