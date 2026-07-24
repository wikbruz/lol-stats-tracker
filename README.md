# League of Legends Stats Tracker
![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Version](https://img.shields.io/badge/version-1.1.0--alpha-blue)
![License](https://img.shields.io/badge/license-MIT-green)

See [CHANGELOG.md](./CHANGELOG.md) for release history.

A Java and Spring Boot application that interacts with the Riot Games API to retrieve player profiles, match histories, and compute performance metrics.

Currently in extremely early stages of development.

## Features & Architecture

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
  > **Tip:** You can also create an `.env` file in the root directory using the provided `.env.example` template if you prefer not to set system-wide variables.

### Running the Application
1. Run the Spring Boot application from your IDE or using `./mvnw spring-boot:run`.
2. The server will start up on [http://localhost:8080](http://localhost:8080).

### Local Access & Architecture
During local development, this project runs as a decoupled full-stack application:
* **Backend API:** Runs on `http://localhost:8080` (handles Riot API orchestration and data processing).
* **Frontend UI (React):** Runs on `http://localhost:3000` (consumes the Spring endpoints via cross-origin requests).

## Project Roadmap

### Milestone: Transitioning to Beta
- [ ] **Automated Testing Suite:** Build out unit and integration tests using **JUnit 5** and **Mockito** (mocking Riot API responses).
- [ ] **Smart Caching & Database Persistence:** Store static match data and player profiles in a database to prevent duplicate Riot API calls and stay well clear of rate limits.
- [ ] **Rate Limit & Cooldown Protection:** Implement rate-limiting logic/cooldowns on user requests to handle API throttling gracefully.
- [ ] **Seamless Account Switching:** Optimize state management to smoothly transition between different accounts when viewing full match histories.
- [ ] **Frontend & Visualization:** Build a modern, responsive **React UI** for data charts and match breakdowns.
---

### Core Analytics & Feature Expansion
- [ ] **Dynamic Summoner Search:** Query any player using the full Riot ID system (`GameName#TagLine`).
- [ ] **In-Depth Performance Metrics:**
  - [x] Vision score, Creep Score (CS).
  - [x] Kill Participation (KP%) calculations.
  - [ ] Skill upgrade orders and item build timelines over the course of a match.
- [x] **Rune & Masteries Breakdown:** Map Riot API rune IDs to static Data Dragon assets (Primary Tree, Keystone, Secondary Tree, Stat Shards).
- [ ] **Custom Match Rating Engine (Proprietary "Performance Score"):**
  - Design a weighted, role-normalized algorithm (0.0 – 10.0) evaluating Combat, Economy, Map Control, and Objective Pressure.
  - Research role-specific adjustments (e.g., weighing Vision higher for Supports, CS/min for ADCs).
- [ ] **Contextual Match Performance Badges (Green / Amber / Red):**
  - Implement visual badge triggers based on key performance thresholds (e.g., *CS Machine*, *Vision Blind*, *Heavy Carry*, *Unlucky*).
- [ ] **Database & User Auth:** Implement user accounts with secure password hashing (`bcrypt`/`Argon2`).
- [ ] **Discord Bot Integration:** Expose stats lookups via Discord slash commands (JDA), reusing the existing `RiotService`/`RunesService` layer directly rather than going through HTTP.
- [ ] **Pro Builds & Guides:** Aggregate high-ELO match data to generate item/skill build recommendations.
