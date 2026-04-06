# Survey & Feedback Analysis System

A dynamic, Java-based Desktop Application for querying, authoring, and analyzing surveys with built-in analytical sentiment integration.

## 🚀 Features

* **Dynamic User Portal Form Binding**: Native JavaFX TextFields dynamically bind to SQLite Question databases. It parses input strings, classifies queries based on "RATING" or "TEXT" variables, and immediately flushes answers via native JDBC connectors directly into the active relational map.
* **Administrative Sentiment Intelligence Engine**: Generates live analytical tracking via the `AnalysisService`. Retrieves data strings in real-time, splits textual boundaries to grade tones as Positive/Negative/Neutral using internal Lexicon parameters, and calculates numerical integer average margins.
* **Instant Survey Authoring**: Inline GUI Question creation templates. Modifying database tables explicitly binds in real-time instantly without server restarts!

## 💻 Tech Stack

* **Language**: Java 23
* **GUI Engine**: OpenJFX (JavaFX 23)
* **Build System**: Maven (`javafx-maven-plugin`)
* **Relational Database**: SQLite-JDBC
* **Testing Library**: JUnit 5

## ⚙️ Installation & Usage

### 1. Prerequisites 
Ensure you have the following installed:
* [Java Development Kit (JDK 23 or compatible)](https://jdk.java.net/)
* [Apache Maven](https://maven.apache.org/)

### 2. Clone the Repository
```bash
git clone https://github.com/Abhaygithub7/Survey-Java-Project.git
cd Survey-Java-Project
```

### 3. Build & Run locally

To compile the latest binary files and boot the `.jar` GUI execution wrapper via Maven, run:
```bash
mvn clean compile javafx:run
```

### 4. Running Unit Tests

To run the localized regression suite validating database transaction logic and the mathematical formula engines:
```bash
mvn test
```

## 🔐 System Architecture Flow

1. **`MainApp` (Router Controller)**: Acts as the Application state manager, safely destroying and constructing active JavaFX `Scene` models via Java Lambda execution pointers.
2. **`DatabaseManager` (SQLite ORM)**: Auto-provisions schemas strictly conforming to relational dependencies and provides localized CRUD interactions.
3. **`UserPortalView` / `AdminDashboardView` (Display Nodes)**: Generates DOM GUI nodes and attaches functional ActionEvent triggers pushing to the backend systems.
4. **`AnalysisService` (Business Logic)**: Houses the complex Lexical processing logic outside the view boundaries for clean MVC Separation.

## 🤝 Project Roadmap 

- [x] V1 Backend Logic implementation
- [x] Phase 2: OpenJFX GUI Shell construction 
- [x] Phase 3: `Scene` Navigation Router 
- [x] Phase 4: Dynamic form answering binding and Admin analytics processing
- [x] Phase 5: Inline Survey Creation interfaces
- [ ] V2 Cloud connectivity sync mapping (Planned)
