# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

GigMatcher is an Android application built with Kotlin and Jetpack Compose that helps match bands with gigs. The app allows promoters to create gigs and find suitable bands based on genre and location preferences.

## Common Commands

### Build Commands
```bash
./gradlew build                 # Build the entire project
./gradlew app:build            # Build only the app module
./gradlew assembleDebug        # Build debug APK
./gradlew assembleRelease      # Build release APK
```

### Development Commands
```bash
./gradlew installDebug         # Install debug APK on connected device
./gradlew clean                # Clean build artifacts
./gradlew app:dependencies     # Show project dependencies
```

### Testing Commands
```bash
./gradlew test                 # Run unit tests
./gradlew connectedAndroidTest # Run instrumented tests on device
./gradlew testDebugUnitTest    # Run debug unit tests
```

## Architecture

### Project Structure
The app follows standard Android project structure with single-activity architecture:

- `MainActivity.kt` - Single activity hosting the entire app
- `GigMatcherApp.kt` - Main Compose app with navigation and state management
- `model/` - Data classes (Band, Gig) using Parcelize for navigation
- `ui/screens/` - Compose screens for different app sections
- `ui/theme/` - Material 3 theming configuration
- `data/` - Sample data and static lists

### Navigation
Uses Jetpack Navigation Compose with these routes:
- `home` - Shows created gigs
- `create_gig` - Form to create new gigs
- `bands` - List of available bands
- `band_detail/{bandId}` - Individual band details

### State Management
- Simple state management using `remember` and `mutableStateOf`
- Gig list state maintained in `GigMatcherApp` and passed to screens
- No complex state management library (Room, ViewModel, etc.) currently used

### UI Framework
- **Jetpack Compose** with Material 3 design system
- **Navigation**: Navigation Compose for screen navigation
- **Theming**: Custom Material 3 theme in `ui/theme/`
- **Architecture**: Single-activity with bottom navigation

## Key Dependencies

- **Compose BOM**: 2023.10.01
- **Navigation Compose**: 2.7.6
- **Material 3**: 1.1.0
- **Kotlin**: 1.9.24
- **Target SDK**: 35 (Android 15)
- **Min SDK**: 26 (Android 8.0)

## Development Notes

### Data Models
- `Band` and `Gig` are Parcelable data classes for easy navigation parameter passing
- Sample data is provided in `SampleData.kt` with UK cities and music genres
- Currently uses in-memory data storage (no persistence)

### UI Patterns
- Follows Material 3 design principles
- Uses Scaffold with TopAppBar and BottomNavigationBar
- Screens are implemented as Composables with navigation callbacks
- State is passed down from parent components

### Package Structure
All code is organized under `com.simonanger.gigmatcher` with clear separation of concerns:
- `model/` for data structures
- `ui/screens/` for UI components
- `ui/theme/` for theming
- `data/` for static data

## Testing
The project includes basic Android testing structure but currently has minimal test coverage. When adding tests:
- Unit tests go in `src/test/`
- Instrumented tests go in `src/androidTest/`
- Use JUnit 4 (already configured in dependencies)