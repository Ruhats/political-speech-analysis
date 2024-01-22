# Political Speech Analysis

## Introduction
The Political Speech Analysis project is a Kotlin-based application designed to process and analyze political speeches. It focuses on parsing CSV data to generate insightful statistics on various aspects of these speeches, offering a unique tool for political analysts, journalists, and enthusiasts.

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