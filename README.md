# NovaAssistant
AI Voice Assistant Android App built with Kotlin

# 🤖 Nova Assistant — AI Voice Assistant for Android
## ✨ Features

- 🎤 **Voice Commands** — Tap mic and speak naturally
- 📞 **Make Phone Calls** — "Call Mom"
- 💡 **Flashlight Control** — "Turn on flashlight"
- 🔵 **Bluetooth Toggle** — "Turn on Bluetooth"
- 📱 **Open Apps** — "Open YouTube", "Open WhatsApp"
- 💬 **Send Messages** — "Message John hello"
- 📶 **Mobile Data Settings** — "Mobile data"
- 🔊 **Text-to-Speech Responses** — Nova speaks back to you
- 🎨 **Clean Dark UI** — Minimal, modern design

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Primary language |
| Android SpeechRecognizer | Voice input |
| TextToSpeech API | Voice responses |
| CameraManager | Flashlight control |
| BluetoothAdapter | Bluetooth toggle |
| ContactsContract | Contact lookup |
| SmsManager | Send SMS |
| MVVM Architecture | App structure |
| LiveData + ViewModel | State management |
| Coroutines | Async processing |

---

## 📁 Project Structure

```
app/src/main/java/com/nova/assistant/
├── MainActivity.kt          # Main UI screen
├── AssistantViewModel.kt    # MVVM ViewModel
├── NovaIntent.kt            # Sealed intent classes
├── CommandProcessor.kt      # NLP intent parsing
├── SpeechService.kt         # STT + TTS wrapper
├── ActionExecutor.kt        # System action handler
└── NovaListenerService.kt   # Background service
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Panda 4 (2025.3.4)
- Android phone with API 24+ (Android 7.0+)
- USB cable or Wi-Fi debugging enabled

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/Nareshkumar-07/NovaAssistant.git
```

2. **Open in Android Studio**
```
File → Open → Select the NovaAssistant folder
```

3. **Sync Gradle**
```
Wait for Gradle sync to complete automatically
```

4. **Connect your Android phone**
```
Enable Developer Options → USB Debugging → Connect via USB or Wi-Fi
```

5. **Run the app**
```
Click the ▶ Run button in Android Studio
```

---

## 🎙️ Voice Commands

| Command | Action |
|---|---|
| "Call [name]" | Makes a phone call |
| "Turn on flashlight" | Turns on torch |
| "Turn off flashlight" | Turns off torch |
| "Turn on Bluetooth" | Enables Bluetooth |
| "Turn off Bluetooth" | Disables Bluetooth |
| "Open [app name]" | Launches the app |
| "Message [name] [text]" | Sends SMS |
| "Mobile data" | Opens data settings |

---

## 🔐 Permissions Required

```xml
RECORD_AUDIO        — Voice input
CALL_PHONE          — Make phone calls
READ_CONTACTS       — Look up contacts by name
SEND_SMS            — Send text messages
CAMERA              — Flashlight control
BLUETOOTH_CONNECT   — Bluetooth toggle
INTERNET            — Network access
FOREGROUND_SERVICE  — Background service
```

---

## 🏗️ Architecture

```
UI Layer        →  MainActivity + XML layouts
ViewModel       →  AssistantViewModel (LiveData)
Domain Layer    →  CommandProcessor + NovaIntent
Service Layer   →  SpeechService + ActionExecutor
```

---

## 📋 Requirements

- **Minimum SDK:** API 24 (Android 7.0)
- **Target SDK:** API 36 (Android 16)
- **Language:** Kotlin 1.9+
- **Build Tool:** Gradle 8.3

---

## 🤝 Contributing

Contributions are welcome! Feel free to:
- Fork the repository
- Create a feature branch
- Submit a pull request

---

## 👨‍💻 Developer

**Naresh Kumar Raju**
- GitHub: [@Nareshkumar-07](https://github.com/Nareshkumar-07)

---

## 📄 License

This project is licensed under the MIT License.

```
MIT License — free to use, modify and distribute
```

---

Built with ❤️ using Kotlin and Android Studio
