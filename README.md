# MathScanner
Use your phone's camera to solve mathematical equations!

## Build Setup
* Android Studio Flamingo
* Gradle 8
* JDK 17

## Availability
* Only support Android 8.1 and above
* Need Google Play Services available in the device for image picker

## Feature Limitation
* Currently it only recognized math expression in this format:<br />
  [Number][white_space][math operator][white_space][Number]

## Architecture
* View based system
* MVVM with UDF flavored
* UI/UX following the latest Material3 guidelines

## 3rd Party Library
* Androidx Jetpack for core functionality
* kotlinx-serialization for data serialization
* Hilt for dependency injection
* Material3 for UI components
* Google ML Kit for text recognizer
* Lottie for extra animation
