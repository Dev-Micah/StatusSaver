# StatusSaver - WhatsApp Status Saver App

**StatusSaver** is an Android application built with **Kotlin** and **Jetpack Compose** that enables users to view, save, and manage **WhatsApp statuses** (images and videos) directly from their devices. Designed for performance and simplicity, it uses modern Android tools and patterns to support both older and newer versions of Android — including Scoped Storage and the Storage Access Framework (SAF).

---
# ScreenShots


| Splash Screen | Image Screen | Settings Screen | 
|----------------|-------------|--------------|
| ![SplashScreen](![Splash](https://github.com/user-attachments/assets/6f7b57c5-bf17-409f-a6f7-26142ebf6d05)) | ![Home](![Images](https://github.com/userattachments/assets/e9521472-61a5-4642-b318-a0a99914a00b)
) | ![Settings](![Settings](https://github.com/user-attachments/assets/1adbbdbd-aca7-41e0-b69f-285a59f70771)
) |


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

Kotlin – A modern, concise, and type-safe programming language for Android development.

Jetpack Compose – Android’s declarative UI toolkit for building native interfaces faster with less boilerplate.

MVVM Architecture – Separates business logic from UI for clean, testable, and maintainable code.

Hilt (Dependency Injection) – Simplifies dependency injection and reduces boilerplate in managing ViewModels and repositories.

Storage Access Framework (SAF) – Enables safe access to user-selected folders (like WhatsApp .Statuses) in Android 10+.

ExoPlayer – A robust and customizable media player library used to preview video statuses.

Coil – A lightweight and fast Kotlin-first image loading library designed for Jetpack Compose.

Navigation Compose – Handles screen navigation in a simple, declarative, and type-safe way.

StateFlow + Coroutines – Used for managing UI state reactively and performing asynchronous operations smoothly.

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
