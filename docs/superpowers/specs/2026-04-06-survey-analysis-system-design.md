# Design Specification: Smart Survey & Feedback Analysis System (Version 1)

## Overview
A lightweight, JavaFX-based application to collect, manage, and analyze survey responses. It features a "Shared Workspace" approach where a single application provides access to both the "Admin Portal" (survey creation and analytics) and the "User Portal" (survey taking). The system operates on an embedded SQLite database for zero-config data persistence.

## 1. Application Flow & User Interface
The UI will be built with JavaFX, featuring a central wrapper window (`MainWrapper`) that swaps out scenes dynamically.

**Landing Dashboard:**
- Focuses on simplicity: displays "Enter as Admin" and "Enter as User" buttons.

**Admin Portal:**
- **Admin Dashboard:** Displays a high-level summary of all active surveys.
- **Create Survey View:** Allows the creation of a survey title/description and appending questions. Question types supported:
  - `MULTIPLE_CHOICE`
  - `RATING` (1-5 scale)
  - `TEXT` (Open-ended textual feedback)
- **Results & Analysis View:**
  - Displays raw survey responses in a data table.
  - Generates insights using the Analysis Engine (see Section 2).
  - Provides a basic export utility to save the results dataset locally as `.csv` or `.txt`.

**User Portal:**
- **Survey Selection:** Presents a clean list of available surveys to take.
- **Survey Execution:** Renders the questionnaire dynamically using standard JavaFX controls (e.g., RadioButtons for multiple choice, Sliders for ratings, TextAreas for open-ended text).
- **Completion Screen:** A confirmation view that redirects the user back to the Landing Dashboard upon success.

## 2. Data Storage & Schema
A local SQLite database connects via JDBC (`sqlite-jdbc`). It handles data natively, requiring no background services.

**Schema:**
- **`surveys` table:**
  - `id` (INTEGER PRIMARY KEY AUTOINCREMENT)
  - `title` (TEXT)
  - `description` (TEXT)
  - `created_at` (DATETIME)
- **`questions` table:**
  - `id` (INTEGER PRIMARY KEY AUTOINCREMENT)
  - `survey_id` (INTEGER FOREIGN KEY references surveys)
  - `question_text` (TEXT)
  - `question_type` (TEXT)
  - `options` (TEXT) - Stored as comma-separated or JSON formatted values for multiple-choice options.
- **`responses` table:**
  - `id` (INTEGER PRIMARY KEY AUTOINCREMENT)
  - `survey_id` (INTEGER FOREIGN KEY references surveys)
  - `question_id` (INTEGER FOREIGN KEY references questions)
  - `response_value` (TEXT) - Unified string storage, adaptable to numeric ratings, option text, or written text input.
  - `submitted_at` (DATETIME)

## 3. Analysis Engine
Driven by a `Service` layer, the Analytics component aggregates insights on-demand when accessed via the Admin Portal.

- **Rating Computation:** Sums all numeric string values from `RATING` type questions and calculates the average out of 5.
- **Sentiment Analysis:**
  - Operates on `TEXT` response types.
  - Runs a basic keyword-density check against two static `HashSet` dictionaries (`positive_words`, `negative_words`).
  - Classifies each string response as Positive, Negative, or Neutral depending on which dictionary achieves the higher density count.
  - Summarizes as an overall percentage breakdown for the survey.
- **Keyword Extraction:**
  - Filters out standard English "stop words" ("the", "is", "at", etc.).
  - Tallies frequency of the remaining tokens using a `HashMap`.
  - Sorts and extracts the top 3-5 keywords globally to highlight common praises or recurring issues.
