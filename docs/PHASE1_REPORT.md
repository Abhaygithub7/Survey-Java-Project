# Phase 1: Survey Analysis System Report

## 1. How to Run the Application (Phase 1 Backend)

Currently, we have completed **Phase 1**, which includes the foundational backend data logic, SQLite database integration, and the analytics engine (Ratings and Sentiment calculation). The UI (JavaFX) will be connected in Phase 2.

At this stage, the project is purely a library/backend that is verifiable via its test suite.

### Requirements
- **Java 17** or higher
- **Maven** (installed locally via Homebrew)

### Running the Test Suite
The simplest way to verify the entire system works is by executing the automated test suite. 

Open your terminal in the root of the project and run:
```bash
mvn clean test
```
This command compiles the source code, initializes an in-memory SQLite database connection, calculates sentiments, and runs all predefined tests. You should see a `BUILD SUCCESS` message.

---

## 2. Technical Report & Tools Used

### Architecture
The Survey Analysis System uses a modular, Test-Driven Development (TDD) approach. The backend is deliberately decoupled from the future JavaFX UI, allowing the `Service` classes to run independently.

### Tools & Stack Used
- **Java 17:** The core programming language.
- **Maven:** Used for dependency management and lifecycle execution. We created a `pom.xml` to automatically import external libraries (like JUnit and SQLite-JDBC).
- **SQLite-JDBC (v3.45.2.0):** Used to power the `DatabaseManager`. SQLite provides a fully functional SQL database that lives inside a single local file (`test_survey.db`), requiring zero server setup or maintenance.
- **JUnit 5 (Jupiter):** The testing framework. We used strict TDD, meaning we wrote tests for the database connection and the sentiment analysis before writing the actual implementations.

### How the Components Work

**1. DatabaseManager (`com.surveyapp.db`)**
- Establishes a JDBC connection to the SQLite database.
- Automatically creates three normalized tables upon instantiation if they don't exist: `surveys`, `questions`, and `responses`.
- Ensures that data from user surveys can be safely written to disk.

**2. AnalysisService (`com.surveyapp.analysis`)**
- **Sentiment Engine:** Uses predefined `HashSet` dictionaries containing positive and negative keywords. It tokenizes user feedback, strips punctuation, normalizes the text, and computes a score.
  - *Example:* "This app is good, fast, and great!" equates to `Positive`.
- **Rating Computation:** Parses numeric string values (e.g., from a 1-5 slider), accumulates the total, and safely divides by the element count while trapping and ignoring irregular formatting.

### Next Steps (Phase 2)
1. Build the JavaFX `MainWrapper` shell.
2. Connect the `DatabaseManager` to forms where users can input survey data visually.
3. Wire the `AnalysisService` to the Admin Dashboard to visualize the insights.
