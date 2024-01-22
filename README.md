# Political Speech Analysis

## Introduction
The Political Speech Analysis project is a Kotlin-based application designed to process and analyze political speeches. It focuses on parsing CSV data to generate statistics on most speeches in a particular year, most speeches about "homeland security" and least wordy politician.

## Setup and Installation
To set up the project on your local machine:
1. Clone the repository from GitHub.
2. Ensure you have Kotlin and Gradle installed.
3. Navigate to the project directory and run `gradle build` to build the project.
4. To start the server, execute `gradle run`.

## Usage
### Evaluating Speeches
The application exposes a GET endpoint `/evaluation` that accepts CSV file URLs as query parameters (e.g., `/evaluation?url1=link_to_csv1&url2=link_to_csv2`). It processes these CSV files to answer questions like which politician gave the most speeches in a specific year, who spoke most about homeland security, and who was the least wordy.

[Postman workspace containing request with public csv urls](https://www.postman.com/ruhats/workspace/fashion-digital/collection/5575618-b69449f0-3b19-4e8e-890a-89c688f9dd24?action=share&creator=5575618)

## Architecture
* HttpClient: Custom client for handling HTTP requests to download CSV files.
* CsvParser: Parses CSV data into Speech objects.
* SpeechService: Analyzes the speeches and computes the statistics.
* SpeechRoute: Manages the API route for handling requests to the /evaluation endpoint.

## Thought Process and Decisions
* Modular Architecture: The project adopts a modular design for ease of maintenance and scalability. Each module, like HttpClient, CsvParser, and SpeechService, is responsible for a specific aspect of the application, ensuring separation of concerns.
* Custom HTTP Client: Instead of using a third-party library, a custom HTTP client was implemented using Ktor's CIO engine. This decision was made to have finer control over the HTTP requests and to tailor error handling specific to the application's needs, especially for CSV file downloads.
* CSV Parsing Strategy: The CsvParser was designed to transform CSV data into Speech objects. This approach was chosen for its simplicity and efficiency, allowing the application to directly process the structured CSV data.

## Algorithms
* Speech Analysis: The core analysis algorithms reside in the SpeechService. These algorithms focus on grouping and aggregating speech data to derive meaningful statistics.
* Most Speeches: Aggregates speeches by speaker and year, then identifies the speaker with the highest count.
* Most Security Talks: Filters speeches on the topic of "homeland security" and performs similar aggregation to find the top speaker.
* Least Wordy: Calculates the total word count for each speaker across all speeches, finding the one with the minimum word count.
* Unique Max/Min Finder: A specialized algorithm was developed to ensure that the results for the most and least are unique. If multiple politicians share the top or bottom spot, the algorithm returns null, adhering to the requirement for a unique answer.

## Error Handling
* Custom Exceptions: DownloadException and CsvParsingException were introduced to handle specific error scenarios. This ensures that the application provides clear and actionable error messages, enhancing the robustness of the system.
* Status Pages Configuration: Ktor's StatusPages feature is utilized to catch these exceptions and respond with appropriate HTTP status codes and messages, ensuring a user-friendly API experience.

## Testing
* Unit Tests: Ensure individual components function correctly based on possible real life scenarios.
* Integration Tests: Verify the integration of different components and the overall workflow.
