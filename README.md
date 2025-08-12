# iOS 18 Photos for Android

A complete Android application that replicates the iOS 18 Photos app experience with modern Material Design and iOS-like styling.

## 🚀 Features

### Core Functionality
- ✅ **Photo & Video Library**: Load and display all photos and videos from device storage
- ✅ **Album Management**: Create, view, and organize custom albums
- ✅ **Search & Filter**: Search by filename, date, album, or content
- ✅ **Multiple View Modes**: Years, Months, Days, and All Photos views
- ✅ **Favorites System**: Mark and manage favorite photos and videos

### iOS-Inspired Design
- ✅ **iOS 18 Styling**: Soft rounded corners, San Francisco-like fonts
- ✅ **Bottom Tab Navigation**: Photos, Albums, Search, For You
- ✅ **Gaussian Blur Effects**: Navigation bars and overlays
- ✅ **Dark/Light Mode**: Automatic theme switching
- ✅ **Smooth Animations**: iOS-like transitions and interactions

### Technical Features
- ✅ **Modern Architecture**: MVVM with LiveData and ViewModel
- ✅ **Efficient Image Loading**: Glide with caching and optimization
- ✅ **Responsive Grid Layouts**: Adaptive layouts for different screen sizes
- ✅ **Permission Management**: Proper storage access permissions
- ✅ **Performance Optimized**: Efficient loading of large photo collections

## 🛠 Technologies Used

- **Language**: Kotlin 1.9.10
- **UI Framework**: Android Jetpack (Navigation, ViewModel, LiveData)
- **Image Loading**: Glide 4.16.0
- **Material Design**: Material 3 Components
- **Architecture**: MVVM Pattern
- **Minimum SDK**: Android 8.0+ (API 26)
- **Target SDK**: Android 14 (API 34)

## 📱 Setup & Installation

### Prerequisites
- Android Studio Flamingo or newer
- JDK 17 or higher
- Android SDK 34
- Device or emulator running Android 8.0+ (API 26+)

### Building on Replit
1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd iOS18Photos
   ```

2. **Install dependencies**
   The project uses Gradle to manage dependencies automatically.

3. **Grant Storage Permissions**
   The app will request storage permissions on first launch to access photos and videos.

4. **Build the project**
   ```bash
   ./gradlew build
   ```

5. **Run on device/emulator**
   ```bash
   ./gradlew installDebug
   ```

### Building Locally
1. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the project folder

2. **Sync Project**
   - Android Studio will automatically sync Gradle dependencies

3. **Run the App**
   - Connect your Android device or start an emulator
   - Click the "Run" button or press Shift+F10

## 🏗 Project Structure

```
app/
├── src/main/
│   ├── java/com/ios18photos/app/
│   │   ├── data/
│   │   │   ├── model/          # Data models (MediaItem, Album)
│   │   │   └── repository/     # Data access layer
│   │   ├── ui/
│   │   │   ├── adapter/        # RecyclerView adapters
│   │   │   ├── photos/         # Photos fragment & viewmodel
│   │   │   ├── albums/         # Albums fragment & viewmodel
│   │   │   ├── search/         # Search fragment & viewmodel
│   │   │   └── foryou/         # For You fragment & viewmodel
│   │   └── MainActivity.kt     # Main activity
│   └── res/
│       ├── layout/             # XML layouts
│       ├── drawable/           # Icons and drawables
│       ├── values/             # Strings, colors, themes
│       └── navigation/         # Navigation graph
└── build.gradle               # App-level Gradle configuration
```

## 🎨 Design System

### Colors
- **Primary**: iOS Blue (#007AFF)
- **Background**: System backgrounds with light/dark mode support
- **Text**: iOS-style label hierarchy (primary, secondary, tertiary)
- **Accent**: iOS system colors (red, green, orange, etc.)

### Typography
- **System Font**: San Francisco-like styling using system sans-serif
- **Hierarchy**: Display, Headline, Title, Body, Label variants
- **Weight**: Regular, Medium, Bold as appropriate

### Components
- **Bottom Navigation**: iOS-style tab bar with blur effects
- **Cards**: Rounded corners with subtle shadows
- **Buttons**: iOS-style button styling and interactions
- **Grid**: Adaptive photo grid with proper spacing

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### UI Tests  
```bash
./gradlew connectedAndroidTest
```

### Lint Checks
```bash
./gradlew lint
```

## 🚀 CI/CD Pipeline

The project includes a complete GitHub Actions workflow:

### Pipeline Steps
1. **Code Analysis**: Lint checks with error reporting
2. **Testing**: Unit tests with coverage reports
3. **Build**: Debug and release APK generation
4. **Signing**: APK signing with keystore (production only)
5. **Artifacts**: Upload built APKs for download

### Required Secrets
Add these secrets to your GitHub repository:
- `SIGNING_KEY`: Base64 encoded release keystore
- `ALIAS`: Keystore key alias
- `KEY_STORE_PASSWORD`: Keystore password
- `KEY_PASSWORD`: Key password

### Keystore Management
- Debug keystore included for development
- Release keystore management guide in `/keystore/README.md`
- Never commit production keystores to version control

## 📋 Permissions

The app requires these permissions:
- `READ_MEDIA_IMAGES` (Android 13+)
- `READ_MEDIA_VIDEO` (Android 13+)  
- `READ_EXTERNAL_STORAGE` (Android 12 and below)

All permissions are requested at runtime with proper user experience.

## 🔧 Configuration

### Build Variants
- **Debug**: Development build with debug keystore
- **Release**: Production build with release keystore and optimizations

### Gradle Configuration
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34
- **Java/Kotlin**: Target Java 8 bytecode

## 📈 Performance Optimizations

- **Image Loading**: Glide with disk caching and memory optimization
- **RecyclerView**: ViewHolder pattern with DiffUtil for efficient updates
- **Background Processing**: Coroutines for non-blocking operations
- **Memory Management**: Proper lifecycle handling and cleanup

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## ⚡ Quick Start on Replit

1. **Fork this Repl**
2. **Install Android SDK** (automatically handled)
3. **Build the project**: `./gradlew build`
4. **Test on device**: Download APK from artifacts

## 🐛 Known Issues

- Photo editing features are basic implementations
- Live Photos require additional platform support
- Some iOS-specific blur effects approximated with Material alternatives

## 🔄 Version History

### v1.0.0 (Current)
- Initial release
- Complete photo library browsing
- Album management
- Search functionality
- iOS 18-inspired design
- CI/CD pipeline

## 📞 Support

If you encounter any issues or have questions:
1. Check the [Issues](../../issues) page
2. Create a new issue with detailed description
3. Include device information and logs if applicable

---

**Made with ❤️ for Android developers who love iOS design**