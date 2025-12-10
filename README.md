# UAS Project - Event Management (Android)

This is a simple Android app (Jetpack Compose) to manage music festival events. It connects to a PHP-based REST API located at `http://104.248.153.158/event-api/api.php`.

Features:

- List events (from API)
- View event details
- Create / Edit / Delete events (using the API)
- Simple polished UI with Material3 and Coil image loading

How to run:

1. Open the project in Android Studio.
2. Ensure the device/emulator has internet access and can reach `104.248.153.158`.
3. Build and install:

```powershell
cd 'c:\Users\lenovo\AndroidStudioProjects\uas_project'
.\gradlew.bat clean assembleDebug installDebug
```

Notes:

- If using HTTP (cleartext), the app allows cleartext only for `104.248.153.158` via `network_security_config.xml`.
- The API expects and returns JSON with an envelope: `{ status, message, data, timestamp }`.
- If your API provides images, include the `image_url` field in the event records to display them in the app.
