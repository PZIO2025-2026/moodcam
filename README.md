# 📱 MoodCam

> *Your emotions in real-time — AI-powered emotion recognition on Android*

[![Android](https://img.shields.io/badge/Android-24%2B-brightgreen.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple.svg)](https://kotlinlang.org)
[![TensorFlow Lite](https://img.shields.io/badge/TensorFlow%20Lite-2.17-orange.svg)](https://www.tensorflow.org/lite)
[![Firebase](https://img.shields.io/badge/Firebase-Enabled-yellow.svg)](https://firebase.google.com)

## 🎯 What is it?

MoodCam is an Android mobile application that uses machine learning to recognize emotions in real-time through your device's camera. The project combines a TensorFlow Lite ML model with a modern Android interface built with Jetpack Compose.

### ✨ Features

- 🎭 **7 Emotions**: Anger, Disgust, Fear, Happy, Neutral, Sad, Surprise
- 📸 **Real-time Detection**: Instant emotion recognition through camera
- 🧠 **On-device ML**: TensorFlow Lite model runs locally
- 🔥 **Firebase Integration**: Authentication and cloud storage
- 🎨 **Material Design 3**: Modern UI built with Jetpack Compose
- 📊 **Face Detection**: Google ML Kit for face detection

## 🏗️ Architecture

```
moodcam/
├── emotion-model/          # ML model and scripts
│   ├── emotion_model.tflite
│   ├── model.py
│   └── predictor.py
└── frontend-android/       # Android application
    └── app/
        └── src/
```

## 🚀 Quick Start

### Requirements

- **Android Studio** Hedgehog or newer
- **Android SDK** API 24+ (Android 7.0+)
- **Python 3.8+** (for ML model)
- **Gradle 8.0+**

### Installation

#### 1️⃣ Clone the repository

```bash
git clone https://github.com/PZIO2025-2026/moodcam.git
cd moodcam
```

#### 2️⃣ Set up ML model

```bash
cd emotion-model
pip install -r requirements.txt
```

**Dependencies:**
- TensorFlow ≥ 2.12.0
- MediaPipe ≥ 0.10.0
- OpenCV ≥ 4.8.0
- NumPy ≥ 1.24.0

#### 3️⃣ Run Android app

1. Open `frontend-android` in Android Studio
2. Sync Gradle
3. Connect a device or start an emulator
4. Click **Run** ▶️

## 🛠️ Tech Stack

### Android App
- **Kotlin** — main language
- **Jetpack Compose** — UI framework
- **CameraX** — camera integration
- **TensorFlow Lite** — ML inference
- **Firebase** — backend services
- **ML Kit** — face detection
- **Koin** — dependency injection
- **Navigation Compose** — navigation

### ML Model
- **TensorFlow** — model training
- **TensorFlow Lite** — mobile conversion
- **MediaPipe** — face processing
- **OpenCV** — computer vision

## 📱 Screenshots

*Coming soon...*

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first.

## 📄 License

This project is developed by **PZIO 2025-2026** team

## 👥 Authors

- **Vladyslav Dzhuha**
- **Oleksandr Kulbit**
- **Viacheslav Shevchenko**

---

<div align="center">
  <sub>Built with ❤️ by PZIO Team</sub>
</div>