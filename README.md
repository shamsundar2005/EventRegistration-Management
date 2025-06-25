# Event Management System (Java Collections Edition)

This project demonstrates the use of advanced **Java Collections Framework** in building a simple **Event Management System**.
It's designed to be easy to understand, yet realistic enough to show how various collections solve real-world problems efficiently.

---

##  Features
- Create and manage events of different types
- Register users to events with live count tracking
- Maintain sorted leaderboards by participant count
- Track recent user registrations (like LRU Cache)
- Priority queue to handle event execution
- Thread-safe registration counts
- Undo admin actions using stack

---

##  Technologies Used
- Java 8+
- Java Collections Framework

---

##  Collections Used and Their Purpose

| Collection             | Use Case                                               |
|------------------------|--------------------------------------------------------|
| `ArrayList`            | Store all event objects                                |
| `LinkedList`           | Manage organizer task list                             |
| `TreeMap`              | Maintain sorted event leaderboard                      |
| `LinkedHashMap`        | Track recent registrations in access order (LRU style) |
| `PriorityQueue`        | Prioritize event execution                             |
| `EnumSet`              | Efficiently manage valid event categories              |
| `ConcurrentHashMap`    | Thread-safe registration counter per event             |
| `Deque` (ArrayDeque)   | Implement Undo stack for admin actions                 |

---

##  How to Run

1. Clone this repository:
```bash
git clone https://github.com/shamsundar2005/eventRegistration-Management.git
cd eventRegistration-Management
```

2. Compile and run:
```bash
javac EventManager.java
java EventManager
```

---

##  Sample Output
```
Event created: Hackathon (TECHNICAL, 120 participants, priority: 1)
Event created: Dance Battle (CULTURAL, 80 participants, priority: 3)
Event created: Code Golf (TECHNICAL, 150 participants, priority: 2)
Stu_123 registered for Hackathon
Stu_456 registered for Code Golf

Leaderboard:
Code Golf: 150 participants
Hackathon: 120 participants
Dance Battle: 80 participants

Recent Registrations (LRU Style):
Stu_123 -> Hackathon
Stu_456 -> Code Golf

Undo: Registered: Stu_456 for Code Golf
```

---

##  Author
**Sham Sundar K**  
AI & ML Engineer | Software Developer  
[shamsundarak2005@gmail.com](mailto:shamsundarak2005@gmail.com)  
[GitHub](https://github.com/shamsundar2005)


