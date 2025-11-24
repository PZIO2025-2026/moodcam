# MoodCam - Technical Documentation

**Project:** MoodCam - AI-Powered Emotion Recognition Mobile Application  
**Team:** PZIO 2025-2026  
**Version:** 1.0  
**Date:** November 24, 2025

---

## Table of Contents

1. [System Architecture](#1-system-architecture)
2. [Technology Stack](#2-technology-stack)
3. [Project Structure](#3-project-structure)
4. [Core Components](#4-core-components)
5. [Machine Learning Model](#5-machine-learning-model)
6. [Database Schema](#6-database-schema)
7. [API Reference](#7-api-reference)
8. [Development Guide](#8-development-guide)
9. [Testing](#9-testing)
10. [Performance Optimization](#10-performance-optimization)

---

## 1. System Architecture

### 1.1 High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     MoodCam Application                      │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │
│  │  UI Layer    │    │  ViewModel   │    │  Repository  │  │
│  │  (Compose)   │◄──►│   Layer      │◄──►│    Layer     │  │
│  └──────────────┘    └──────────────┘    └──────────────┘  │
│         │                    │                    │          │
│         │                    │                    │          │
│  ┌──────▼────────────────────▼────────────────────▼──────┐  │
│  │              Business Logic Layer                      │  │
│  │  • Authentication   • Image Processing                 │  │
│  │  • Emotion Detection • Profile Management              │  │
│  └────────────────────────────────────────────────────────┘  │
│         │                    │                    │          │
│  ┌──────▼──────┐      ┌──────▼──────┐     ┌──────▼──────┐  │
│  │  CameraX    │      │  TFLite     │     │  ML Kit     │  │
│  │  (Camera)   │      │  (Emotion)  │     │  (Face Det) │  │
│  └─────────────┘      └─────────────┘     └─────────────┘  │
│                                                               │
└───────────────────────────────┬───────────────────────────────┘
                                │
                    ┌───────────▼───────────┐
                    │   Firebase Backend    │
                    │  • Authentication     │
                    │  • Firestore Database │
                    │  • Analytics          │
                    └───────────────────────┘
```

### 1.2 MVVM Architecture Pattern

The application follows **Model-View-ViewModel (MVVM)** architecture:

- **View (UI)**: Jetpack Compose components
- **ViewModel**: State management and business logic
- **Model**: Data entities and repositories
- **Repository**: Data access abstraction layer

### 1.3 Key Design Patterns

- **Singleton**: FaceDetectorProvider, Firebase instances
- **Repository Pattern**: Data access abstraction
- **Dependency Injection**: Koin framework
- **Observer Pattern**: StateFlow for reactive UI updates
- **Factory Pattern**: ViewModel creation

---

## 2. Technology Stack

### 2.1 Android Frontend

| Technology | Version | Purpose |
|------------|---------|---------|
| Kotlin | 1.9+ | Primary programming language |
| Jetpack Compose | 2025.01.00 | Modern declarative UI framework |
| Android SDK | API 24-36 | Android platform support |
| CameraX | 1.5.1 | Camera integration |
| Navigation Compose | 2.8.2 | In-app navigation |

### 2.2 Machine Learning

| Technology | Version | Purpose |
|------------|---------|---------|
| TensorFlow Lite | 2.17+ | On-device ML inference |
| ML Kit Face Detection | 16.1.7 | Face detection |
| OpenCV | 4.9.0 | Image processing utilities |

### 2.3 Backend Services

| Service | Purpose |
|---------|---------|
| Firebase Authentication | User authentication (email/password) |
| Cloud Firestore | NoSQL database for user profiles and emotion history |
| Firebase Analytics | Usage analytics |

### 2.4 Development Tools

- **Gradle 8.0+**: Build system
- **Koin**: Dependency injection
- **Doxygen**: Technical documentation generation
- **Git**: Version control

---

## 3. Project Structure

```
pzio/
├── emotion-model/              # ML model and training scripts
│   ├── emotion_model.tflite    # TensorFlow Lite model (48x48 grayscale)
│   ├── emotion_model.ipynb     # Model training notebook
│   ├── model.py                # Model utilities
│   ├── predictor.py            # Prediction utilities
│   └── requirements.txt        # Python dependencies
│
├── frontend-android/           # Android application
│   └── app/
│       └── src/
│           └── main/
│               ├── assets/
│               │   └── emotion_model.tflite  # Bundled ML model
│               ├── java/com/moodcam/frontend_android/
│               │   ├── auth/              # Authentication
│               │   │   ├── vm/            # AuthViewModel
│               │   │   └── ui/            # Login/Signup UI
│               │   ├── db/                # Data layer
│               │   │   ├── entities/      # User, EmotionRecord
│               │   │   ├── UserRepository.kt
│               │   │   └── EmotionHistoryRepository.kt
│               │   ├── viewmodel/         # ViewModels
│               │   │   ├── EmotionClassifierViewModel.kt
│               │   │   └── ProfileViewModel.kt
│               │   ├── helpers/           # Utilities
│               │   │   ├── TFLiteHelper.kt
│               │   │   └── images/        # Image processing
│               │   ├── di/                # Dependency injection
│               │   │   └── AppModule.kt
│               │   ├── ui/                # UI components
│               │   │   ├── camera/        # Camera screen
│               │   │   ├── profile/       # Profile screens
│               │   │   ├── statistics/    # Statistics screen
│               │   │   └── components/    # Reusable components
│               │   └── navigation/        # Navigation setup
│               └── res/                   # Resources
│
├── docs/                       # Documentation
│   └── TECHNICAL_DOCUMENTATION.md
├── Doxyfile                    # Doxygen configuration
├── README.md                   # Project overview
└── Karta_Projektu_GR4.pdf     # Project requirements
```

---

## 4. Core Components

### 4.1 Authentication System

**Component:** `AuthViewModel`  
**Location:** `auth/vm/AuthViewModel.kt`

#### Responsibilities:
- User registration (signup)
- User login with email/password
- Session management
- Authentication state monitoring

#### States:
```kotlin
sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String): AuthState()
}
```

#### Key Methods:
- `login(email: String, password: String)` - Authenticate user
- `signup(email: String, password: String)` - Register new user
- `signout()` - Log out current user
- `checkAuthStatus()` - Check current auth state
- `getUserId()` - Get current user ID

### 4.2 Emotion Classification

**Component:** `EmotionClassifierViewModel`  
**Location:** `viewmodel/EmotionClassifierViewModel.kt`

#### Responsibilities:
- Real-time emotion detection from camera feed
- Frame processing optimization (every 10th frame)
- Emotion state management

#### Pipeline:
1. Receive ImageProxy from CameraX
2. Frame skipping logic (process 1/10 frames)
3. Delegate to `processImageProxy()` helper
4. Update UI with detected emotion

#### Supported Emotions:
- Angry
- Disgust
- Fear
- Happy
- Neutral
- Sad
- Surprise

### 4.3 Image Processing Pipeline

**Component:** Image Processing Helpers  
**Location:** `helpers/images/`

#### Processing Steps:

```
Camera Frame (ImageProxy)
    │
    ├──► Face Detection (ML Kit)
    │         │
    │         ├──► No Face → Return "NoFace"
    │         │
    │         └──► Face Found
    │                  │
    │                  ├──► Convert to Grayscale Bitmap
    │                  │
    │                  ├──► Crop Face Region
    │                  │
    │                  ├──► Resize to 48x48
    │                  │
    │                  ├──► Normalize (÷255)
    │                  │
    │                  ├──► Convert to ByteBuffer
    │                  │
    │                  └──► TensorFlow Lite Inference
    │                           │
    │                           └──► Emotion Label
```

#### Key Functions:

**`detectLargestFace()`**
- Uses ML Kit Face Detection API
- Returns largest face by bounding box area
- Asynchronous callback-based

**`cropAndResizeFace()`**
- Crops face using bounding box
- Resizes to 48x48 pixels
- Applies boundary checks

**`bitmapToGrayByteBuffer()`**
- Extracts grayscale pixel values
- Normalizes to [0, 1] range
- Creates ByteBuffer for TFLite

**`processImageProxy()`**
- Complete emotion detection pipeline
- Integrates all processing steps
- Returns emotion label via callback

### 4.4 User Profile Management

**Component:** `ProfileViewModel`  
**Location:** `viewmodel/ProfileViewModel.kt`

#### Responsibilities:
- Load user profile from Firestore
- Update profile information
- Onboarding flow management

#### States:
```kotlin
sealed class ProfileState {
    object Loading : ProfileState()
    object Unauthenticated : ProfileState()
    data class Loaded(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
}
```

### 4.5 Emotion History

**Component:** `EmotionHistoryRepository`  
**Location:** `db/EmotionHistoryRepository.kt`

#### Responsibilities:
- Store emotion detection events
- Retrieve emotion history
- Support pagination

#### Methods:
- `addEmotion(uid, emotion, callbacks)` - Save detection
- `getRecent(uid, anchorDate, limit, callback)` - Fetch history

---

## 5. Machine Learning Model

### 5.1 Model Specifications

| Property | Value |
|----------|-------|
| **Model Type** | Convolutional Neural Network (CNN) |
| **Framework** | TensorFlow / TensorFlow Lite |
| **Input Shape** | (1, 48, 48, 1) - Grayscale 48x48 |
| **Output Shape** | (1, 7) - 7 emotion classes |
| **Model Size** | ~1.5 MB |
| **Format** | .tflite (TensorFlow Lite) |

### 5.2 Model Architecture

```
Input Layer (48x48x1)
    │
Conv2D → ReLU → MaxPool
    │
Conv2D → ReLU → MaxPool
    │
Conv2D → ReLU → MaxPool
    │
Flatten
    │
Dense → ReLU → Dropout
    │
Dense → Softmax (7 classes)
    │
Output (Emotion Probabilities)
```

### 5.3 Training Dataset

- **Dataset**: FER2013 (Facial Expression Recognition)
- **Training Samples**: ~28,000 images
- **Validation Samples**: ~3,500 images
- **Image Size**: 48x48 grayscale
- **Classes**: 7 emotions (balanced)

### 5.4 Model Performance

| Metric | Value |
|--------|-------|
| Training Accuracy | ~65% |
| Validation Accuracy | ~60% |
| Inference Time (mobile) | ~20-50ms per frame |
| Memory Usage | ~10MB RAM |

### 5.5 Preprocessing Pipeline

**Python Training:**
```python
face_resized = cv2.resize(face_gray, (48, 48))
face_norm = face_resized / 255.0
```

**Kotlin Inference:**
```kotlin
val normalized = grayValue / 255f
```

⚠️ **Critical**: Preprocessing must match training pipeline exactly!

### 5.6 Model Integration

**Loading Model:**
```kotlin
val buffer = loadModelFile(context, "emotion_model.tflite")
val interpreter = Interpreter(buffer)
```

**Running Inference:**
```kotlin
val inputBuffer = ByteBuffer.allocateDirect(48 * 48 * 4)
val output = Array(1) { FloatArray(7) }
interpreter.run(inputBuffer, output)
val emotionIndex = output[0].indices.maxByOrNull { output[0][it] }
```

---

## 6. Database Schema

### 6.1 Firestore Collections

#### **users** Collection

```
users/
└── {userId}/
    ├── id: String (userId)
    ├── email: String
    ├── name: String? (nullable)
    ├── photoUrl: String? (nullable)
    ├── userStartAge: Int? (nullable)
    └── createdAt: Timestamp
    
    └── emotions/ (subcollection)
        └── {emotionId}/
            ├── emotion: String
            └── createdAt: Timestamp
```

#### User Document Structure

```json
{
  "id": "abc123xyz",
  "email": "user@example.com",
  "name": "John Doe",
  "photoUrl": null,
  "userStartAge": 25,
  "createdAt": "2025-11-24T10:00:00Z"
}
```

#### Emotion Record Structure

```json
{
  "emotion": "Happy",
  "createdAt": "2025-11-24T14:30:00Z"
}
```

### 6.2 Firestore Security Rules

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can only read/write their own data
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
      
      // Emotion history subcollection
      match /emotions/{emotionId} {
        allow read, write: if request.auth != null && request.auth.uid == userId;
      }
    }
  }
}
```

### 6.3 Firestore Indexes

**Composite Index for Emotion History:**
- Collection: `users/{userId}/emotions`
- Fields: `createdAt` (Descending)
- Purpose: Efficient chronological queries

---

## 7. API Reference

### 7.1 AuthViewModel API

```kotlin
class AuthViewModel(userRepository: UserRepository, auth: FirebaseAuth)

// Authentication Methods
fun login(email: String, password: String)
fun signup(email: String, password: String)
fun signout()
fun checkAuthStatus()
fun getUserId(): String?

// Observable State
val authState: LiveData<AuthState>
```

### 7.2 EmotionClassifierViewModel API

```kotlin
class EmotionClassifierViewModel(tflite: Interpreter)

// Detection Method
fun predict(image: ImageProxy)

// Observable State
val currentEmotion: State<String>  // "Happy", "Sad", etc.
```

### 7.3 UserRepository API

```kotlin
class UserRepository(db: FirebaseFirestore)

// Profile Operations
suspend fun createUserProfile(uid: String, email: String, authCreationTime: Long)
fun saveProfile(uid: String, name: String, age: Int)
fun updateName(uid: String, name: String)
fun getProfile(uid: String, onResult: (User?) -> Unit)
```

### 7.4 EmotionHistoryRepository API

```kotlin
class EmotionHistoryRepository(db: FirebaseFirestore)

// History Operations
fun addEmotion(
    uid: String, 
    emotion: String, 
    onComplete: (() -> Unit)? = null,
    onError: ((Exception) -> Unit)? = null
)

fun getRecent(
    uid: String, 
    anchorDate: Date?, 
    limit: Long = 50, 
    onResult: (List<EmotionRecord>) -> Unit
)
```

### 7.5 Image Processing API

```kotlin
// Face Detection
fun detectLargestFace(
    imageProxy: ImageProxy,
    onFaceDetected: (Face?) -> Unit
)

// Face Preprocessing
fun cropAndResizeFace(
    bitmap: Bitmap, 
    face: Face, 
    size: Int = 48
): Bitmap

fun bitmapToGrayByteBuffer(bitmap: Bitmap): ByteBuffer

// Complete Pipeline
fun processImageProxy(
    image: ImageProxy,
    tflite: Interpreter,
    onEmotionDetected: (String) -> Unit
)
```

---

## 8. Development Guide

### 8.1 Prerequisites

- **Android Studio**: Hedgehog (2023.1.1) or newer
- **JDK**: 11 or higher
- **Android SDK**: API 24-36
- **Gradle**: 8.0+
- **Git**: For version control

### 8.2 Project Setup

1. **Clone Repository:**
```bash
git clone https://github.com/PZIO2025-2026/moodcam.git
cd moodcam
```

2. **Open in Android Studio:**
   - Open `frontend-android` folder
   - Wait for Gradle sync

3. **Configure Firebase:**
   - Place `google-services.json` in `app/` directory
   - Ensure Firebase project is configured

4. **Build Project:**
```bash
./gradlew build
```

### 8.3 Running the App

**Debug Build:**
```bash
./gradlew installDebug
```

**Run on Device/Emulator:**
- Click Run ▶️ in Android Studio
- Select target device
- Grant camera permissions

### 8.4 Code Style

- **Language**: Kotlin
- **Naming**: camelCase for functions/variables, PascalCase for classes
- **Documentation**: Doxygen-style comments
- **Formatting**: Follow Kotlin official style guide

### 8.5 Adding New Features

**Example: Adding a new emotion:**

1. Update ML model to include new class
2. Update `EmotionLabels.LABELS` array
3. Redeploy `emotion_model.tflite` to assets
4. Update UI components if needed

---

## 9. Testing

### 9.1 Unit Tests

Located in `app/src/test/`

**Running Tests:**
```bash
./gradlew test
```

### 9.2 Instrumentation Tests

Located in `app/src/androidTest/`

**Running Tests:**
```bash
./gradlew connectedAndroidTest
```

### 9.3 Manual Testing Checklist

- [ ] User registration with valid email
- [ ] User login with correct credentials
- [ ] Camera permission request
- [ ] Real-time emotion detection
- [ ] Emotion history saving
- [ ] Statistics visualization
- [ ] Profile editing
- [ ] Logout functionality

---

## 10. Performance Optimization

### 10.1 Implemented Optimizations

**Frame Skipping:**
- Process only every 10th camera frame
- Reduces CPU usage by ~90%
- Maintains responsive UI

**Lazy Initialization:**
- TFLite model loaded on demand
- Face detector singleton pattern
- Firebase instances cached

**Memory Management:**
- Bitmap recycling after use
- ByteBuffer reuse where possible
- Proper ImageProxy closing

**Background Processing:**
- Face detection runs asynchronously
- Firestore operations are non-blocking
- Coroutines for suspending operations

### 10.2 Performance Metrics

| Operation | Average Time |
|-----------|--------------|
| Face Detection | 20-40ms |
| Emotion Inference | 20-50ms |
| Total Pipeline | 50-100ms |
| Frame Rate | 10-15 FPS (effective) |
| Memory Usage | 100-150MB |

### 10.3 Future Optimizations

- [ ] Model quantization (INT8) for faster inference
- [ ] GPU acceleration using GPU delegate
- [ ] Batch processing multiple faces
- [ ] Caching recent detections
- [ ] Progressive image loading

---

## Generating Documentation

### Using Doxygen

1. **Install Doxygen:**
   - Windows: Download from [doxygen.nl](https://www.doxygen.nl/)
   - macOS: `brew install doxygen`
   - Linux: `sudo apt-get install doxygen`

2. **Generate Documentation:**
```bash
cd pzio/
doxygen Doxyfile
```

3. **View Documentation:**
```bash
# Open in browser
open docs/doxygen/html/index.html  # macOS
start docs/doxygen/html/index.html # Windows
```

---

## Additional Resources

- **Jetpack Compose**: https://developer.android.com/jetpack/compose
- **CameraX**: https://developer.android.com/training/camerax
- **TensorFlow Lite**: https://www.tensorflow.org/lite
- **Firebase**: https://firebase.google.com/docs
- **Kotlin**: https://kotlinlang.org/docs/home.html

---

## Contact & Support

**Team:** PZIO 2025-2026

**Team Members:**
- Vladyslav Dzhuha (Team Lead)
- Viacheslav Shevchenko (Frontend)
- Oleksandr Kulbit (Backend)

**Repository:** https://github.com/PZIO2025-2026/moodcam  
**Jira:** https://moodcam.atlassian.net/

---

**Last Updated:** November 24, 2025  
**Document Version:** 1.0
