# Android Weather App

A simple Android application that displays real-time weather information for a user-specified location. The app fetches weather data from a public API and displays it in a clean, user-friendly interface.

## Features

- Get current weather data by city name
- Displays temperature, weather description, humidity, and wind speed
- Handles invalid inputs and network errors gracefully
- Clean UI with support for dynamic updates

## Technologies Used

- Java
- Android SDK
- AndroidX Libraries
- REST API (e.g., OpenWeatherMap)
- Volley or Retrofit for HTTP requests
- JSON parsing

## Project Structure

- **`MainActivity.java`**  
  Handles user input, triggers API requests, parses JSON responses, and updates the UI.

- **`WeatherService.java`** *(optional helper class)*  
  Contains logic to build and send the API request and process the result.

- **`activity_main.xml`**  
  Defines the layout of the app including `EditText`, `Button`, and `TextView`s for displaying weather data.

## API Used

- [OpenWeatherMap API](https://openweathermap.org/api)  
  Sign up to get a free API key.

## How to Run

1. Clone this repository or copy the files into your Android Studio project.
2. Obtain an API key from [OpenWeatherMap](https://openweathermap.org/api).
3. Add your API key in the appropriate place in `WeatherService.java` or `MainActivity.java`:
   ```java
   private final String API_KEY = "YOUR_API_KEY_HERE";


## Screenshots

![Image](https://github.com/user-attachments/assets/14c09b8d-5cde-4852-a0cc-a5c6b6b99af0)
![Image](https://github.com/user-attachments/assets/9be42a8d-b925-4afd-9a56-29c37fd05ff5)
