# StatusSaver - WhatsApp Status Saver App

**StatusSaver** is an Android application built with **Kotlin** and **Jetpack Compose** that enables users to view, save, and manage **WhatsApp statuses** (images and videos) directly from their devices. Designed for performance and simplicity, it uses modern Android tools and patterns to support both older and newer versions of Android — including Scoped Storage and the Storage Access Framework (SAF).

---

## Core Features

- View WhatsApp image and video statuses
- Save statuses locally with a single tap
- Delete saved statuses from the app
- Preview videos using ExoPlayer
- Dark/Light theme support
- Pull-to-refresh for status updates
- Support for Android 10+ Scoped Storage using SAF

---

## Tech Stack 

| Tech / Library        
|-----------------------|-------------|
| **Kotlin**            | A modern, expressive, and safe programming language for Android development. |
| **Jetpack Compose**   | Android's modern UI toolkit for building declarative, reactive UIs with less code. |
| **MVVM Architecture** | Separates UI logic, business logic, and data for clean and testable architecture. |
| **Hilt (DI)**         | A dependency injection library that simplifies injecting and managing dependencies like ViewModels and repositories. |
| **Storage Access Framework (SAF)** | Allows users to grant access to specific folders (like `.Statuses`) while respecting Android 10+ Scoped Storage restrictions. |
| **ExoPlayer**         | A powerful media player library for video playback within the app. |
| **Coil**              | An image loading library optimized for Kotlin and Jetpack Compose. |
| **Navigation Compose**| Manages screen-to-screen navigation using a simple and type-safe route system. |
| **StateFlow + Coroutines** | Used to handle UI state and asynchronous data loading cleanly and reactively. |

---

## Project Structure

```text
com.statussaver
│
├── data
│   ├── model        # Data classes for statuses
│   └── repository   # Handles file access and media loading
│
├── di               # Hilt modules for dependency injection
│
├── ui
│   ├── screens
│   │   ├── home     # Displays statuses from WhatsApp
│   │   ├── saved    # Shows user's saved statuses
│   │   └── viewer   # Video/image preview screen
│   └── components   # Reusable UI elements
│
├── utils            # Constants, formatters, and file helpers
│
└── MainActivity.kt  # App entry point and navigation setup
