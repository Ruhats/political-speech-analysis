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

## Testing
* Unit Tests: Ensure individual components function correctly based on possible real life scenarios.
* Integration Tests: Verify the integration of different components and the overall workflow.

## Error Handling
* DownloadException: Handles errors related to CSV file downloading.
* CsvParsingException: Catches and manages exceptions during CSV.