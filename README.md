# 🚀 ExoPlayer-ClearAudioBooster
### 200% High-Fidelity Audio Booster for Android Media3/ExoPlayer

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Platform: Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg)]()
[![Media3: 1.1.0+](https://img.shields.io/badge/Media3-1.1.0+-orange.svg)]()

**ExoPlayer-ClearAudioBooster** is a professional-grade Digital Signal Processing (DSP) utility for Android. It allows developers to implement a **200% volume boost** (up to +15dB gain) in video and music players without the "cracking," "clipping," or "static distortion" commonly found in standard software amplifiers.

---

## 🛠️ The Problem: Why Standard Boosters Fail
Most Android volume boosters simply multiply the audio signal using basic gain APIs. If the source audio is already near peak levels (0dB), this causes **Digital Clipping**—the waveform hits the hardware "ceiling," resulting in harsh static noise that ruins the user experience and can damage device speakers.

## ✨ The Solution: Dynamic Range Compression
This library uses the Android **`DynamicsProcessing`** API (introduced in Android 9 Pie) to implement a **Hard Limiter**. 
* **Intelligent Gain:** Boosts quiet sections of the audio significantly.
* **Safety Ceiling:** Mathematically "squashes" loud peaks (explosions, high notes) right before they hit the clipping point.
* **Result:** Incredibly loud, crystal-clear audio even at 200% slider levels.

---

## 📦 Installation & Setup

### 1. Add the Helper Class
Copy `ClearAudioBooster.kt` into your project's `utils` or `player` package.

### 2. Implementation in Activity/Fragment/Composable
Initialize the booster by linking it to your existing `ExoPlayer` instance.

```kotlin
// Initialize the booster
val audioBooster = ClearAudioBooster(exoPlayer)

// Release when finished to prevent memory leaks
override fun onDestroy() {
    super.onDestroy()
    audioBooster.release()
}
