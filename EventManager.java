import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

enum EventType {
    TECHNICAL, CULTURAL, SPORTS, WORKSHOP
}

class Event {
    String name;
    EventType type;
    int participants;
    int priority;

    public Event(String name, EventType type, int participants, int priority) {
        this.name = name;
        this.type = type;
        this.participants = participants;
        this.priority = priority;
    }

    public String toString() {
        return name + " (" + type + ", " + participants + " participants, priority: " + priority + ")";
    }
}

public class EventManager {

    ArrayList<Event> allEvents = new ArrayList<>();
    LinkedList<String> organizerTasks = new LinkedList<>();
    TreeMap<Integer, String> eventLeaderboard = new TreeMap<>(Collections.reverseOrder());
    LinkedHashMap<String, String> recentRegistrations = new LinkedHashMap<>(16, 0.75f, true);
    PriorityQueue<Event> eventQueue = new PriorityQueue<>(Comparator.comparingInt(e -> e.priority));
    EnumSet<EventType> allowedTypes = EnumSet.allOf(EventType.class);
    ConcurrentHashMap<String, Integer> liveRegistrations = new ConcurrentHashMap<>();
    Deque<String> adminActions = new ArrayDeque<>();

    public void createEvent(String name, EventType type, int participants, int priority) {
        if (!allowedTypes.contains(type)) {
            System.out.println("Error: Event type " + type + " is not allowed.");
            return;
        }

        Event e = new Event(name, type, participants, priority);
        allEvents.add(e);
        // Note: eventLeaderboard currently stores only one event per participant count if counts are same.
        // For a true leaderboard, you might need to handle ties differently, e.g., List<String> as value.
        eventLeaderboard.put(participants, name);
        eventQueue.add(e);
        liveRegistrations.put(name, participants); // Initialize with initial participants
        adminActions.push("Created: " + name);
        System.out.println("Event created: " + e);
    }

    public void registerUser(String userId, String eventName) {
        // Check if the event exists before registering
        if (!liveRegistrations.containsKey(eventName)) {
            System.out.println("Error: Event '" + eventName + "' does not exist. Cannot register.");
            return;
        }

        recentRegistrations.put(userId, eventName);
        liveRegistrations.merge(eventName, 1, Integer::sum); // Increment participant count by 1 for new registration
        adminActions.push("Registered: " + userId + " for " + eventName);
        System.out.println(userId + " registered for " + eventName);
    }

    public void showLeaderboard() {
        System.out.println("\n--- Event Leaderboard (by initial participants) ---");
        if (eventLeaderboard.isEmpty()) {
            System.out.println("No events on the leaderboard yet.");
            return;
        }
        for (Map.Entry<Integer, String> entry : eventLeaderboard.entrySet()) {
            System.out.println(entry.getValue() + ": " + entry.getKey() + " initial participants");
        }
    }

    public void showLiveRegistrations() {
        System.out.println("\n--- Live Registration Counts ---");
        if (liveRegistrations.isEmpty()) {
            System.out.println("No live registration data yet.");
            return;
        }
        liveRegistrations.forEach((eventName, count) -> System.out.println(eventName + ": " + count + " current participants"));
    }

    public void undoLastAction() {
        if (!adminActions.isEmpty()) {
            System.out.println("Undo: " + adminActions.pop());
            // Note: A full undo mechanism would require storing more state or specific undo logic
            // for each action (e.g., decrementing participant count if it was a registration).
            // This currently only shows what was undone from the stack.
        } else {
            System.out.println("No actions to undo.");
        }
    }

    public void printRecentRegistrations() {
        System.out.println("\n--- Recent Registrations (LRU Style) ---");
        if (recentRegistrations.isEmpty()) {
            System.out.println("No recent registrations yet.");
            return;
        }
        recentRegistrations.forEach((k, v) -> System.out.println(k + " -> " + v));
    }

    public void showEventsByPriority() {
        System.out.println("\n--- Events by Priority (Next to Process) ---");
        if (eventQueue.isEmpty()) {
            System.out.println("No events in the queue.");
            return;
        }
        // Create a temporary list to print without modifying the original queue
        List<Event> sortedEvents = new ArrayList<>(eventQueue);
        sortedEvents.sort(Comparator.comparingInt(e -> e.priority)); // Sort again just in case
        for (Event event : sortedEvents) {
            System.out.println("Priority " + event.priority + ": " + event.name);
        }
    }

    public static void main(String[] args) {
        EventManager manager = new EventManager();
        Scanner scanner = new Scanner(System.in); // Initialize Scanner for user input

        // Pre-create some events
        manager.createEvent("Hackathon", EventType.TECHNICAL, 120, 1);
        manager.createEvent("Dance Battle", EventType.CULTURAL, 80, 3);
        manager.createEvent("Code Golf", EventType.TECHNICAL, 150, 2);
        manager.createEvent("Football Tournament", EventType.SPORTS, 200, 1);

        // Initial registrations (can be hardcoded or via user input later)
        manager.registerUser("u101", "Hackathon");
        manager.registerUser("u102", "Code Golf");
        manager.registerUser("u103", "Hackathon"); // Another registration for Hackathon

        int choice;
        do {
            System.out.println("\n--- Event Management System Menu ---");
            System.out.println("1. Register for an Event");
            System.out.println("2. Show Event Leaderboard (by initial participants)");
            System.out.println("3. Show Live Registration Counts");
            System.out.println("4. Print Recent Registrations");
            System.out.println("5. Show Events by Priority");
            System.out.println("6. Undo Last Admin Action");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine()); // Read choice as a line

                switch (choice) {
                    case 1:
                        System.out.print("Enter User ID: ");
                        String userId = scanner.nextLine();
                        System.out.print("Enter Event Name: ");
                        String eventName = scanner.nextLine();
                        manager.registerUser(userId, eventName);
                        break;
                    case 2:
                        manager.showLeaderboard();
                        break;
                    case 3:
                        manager.showLiveRegistrations();
                        break;
                    case 4:
                        manager.printRecentRegistrations();
                        break;
                    case 5:
                        manager.showEventsByPriority();
                        break;
                    case 6:
                        manager.undoLastAction();
                        break;
                    case 0:
                        System.out.println("Exiting Event Management System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                choice = -1; // Set to an invalid choice to continue loop
            }

        } while (choice != 0);

        scanner.close(); // Close the scanner when done
    }
}