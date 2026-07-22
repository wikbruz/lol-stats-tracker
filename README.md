# League of Legends Stats Tracker
**Project State:** Version 1.0.0-Alpha

A Java and Spring Boot application that interacts with the Riot Games API to retrieve player profiles, match histories, and compute performance metrics.

Currently in extremely early stages of development.

##Features & Architecture

* **Spring Framework:** Utilized to eliminate boilerplate HTTP management and structure the backend service cleanly.
* **Riot API Mapping:** Seamlessly maps complex, nested JSON payloads directly into strongly-typed Java DTO structures.
* **Security-First Design:** Implements externalised configuration using environment variables to keep developer API credentials out of source control.
* **Data Synthesis:** Orchestrates continuous data streams (Summoner info > Match IDs > Match Details) to compile performance summaries.

## How to Run Locally

### Prerequisites
* Java 17 or higher
* A development API key from the [Riot Developer Portal](https://riotgames.com)

### Configuration
This project uses secure environment variables to protect the API key used. Before launching the Spring Boot application, you must expose your Riot API key to your system:

* **Windows (CMD):**
  ```cmd
  set RIOT_API_KEY=your_rgapi_key_here
  ```
* **Mac / Linux:**
  ```bash
  export RIOT_API_KEY="your_rgapi_key_here"
  ```

### Running the Application
1. Run the Spring Boot application from your IDE or using `./mvnw spring-boot:run`.
2. The server will start up on [http://localhost:8080](http://localhost:8080).

### Local Access & Architecture
During local development, this project runs as a decoupled full-stack application:
* **Backend API:** Runs on `http://localhost:8080` (handles Riot API orchestration and data processing).
* **Frontend UI (React):** Runs on `http://localhost:3000` (consumes the Spring endpoints via cross-origin requests).

## Project Roadmap
- [ ] Implement Spring WebClient for parallel, non-blocking asynchronous match fetching.
- [ ] Add Spring Cache abstraction to store static game assets and prevent hitting API rate limits
- [ ] Provide deeper match insights (creep score, vision score, etc)
- [ ] Transition from hardcoded account data to a fully dynamic search engine for any Riot ID (`GameName#TagLine`).
- [ ] Build a responsive, clean, and modern user interface using **React (JavaScript)**.
