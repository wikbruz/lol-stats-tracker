# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [1.1.0-alpha] - 2026-07-24

### Added
- CS/min, vision score, control wards bought, wards placed, and wards destroyed to per-match stats
- Kill Participation (KP%) calculation, sourced directly from Riot's `challenges.killParticipation`
- Full Rune & Mastery breakdown: Primary Tree, Keystone selections, Secondary Tree, and Stat Shards, resolved via a Data Dragon-backed lookup service

## [1.0.0-alpha] - 2026-07-23

### Added
- Initial project scaffold (Spring Boot, Maven, Java 17)
- Riot account lookup, match ID retrieval, match detail fetching
- Basic player stats summary (average K/D/A across recent games)