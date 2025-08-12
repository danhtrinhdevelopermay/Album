# Android Keystore Guide

This directory contains instructions for creating and managing Android keystores for app signing.

## Creating a Keystore

### For Development (Debug)
Android Studio automatically creates a debug keystore located at:
- **Windows**: `C:\Users\[username]\.android\debug.keystore`
- **Mac/Linux**: `~/.android/debug.keystore`

### For Release
To create a release keystore, use the following command:

```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
```

You'll be prompted to enter:
- Keystore password
- Key password
- Personal information (name, organization, etc.)

## Keystore Information

### Sample Debug Keystore (FOR DEVELOPMENT ONLY)
- **File**: `debug.keystore`
- **Password**: `android`
- **Key Alias**: `androiddebugkey`
- **Key Password**: `android`
- **Validity**: 30 years

**⚠️ NEVER USE DEBUG KEYSTORE FOR PRODUCTION APPS**

## GitHub Actions Secrets

To use the keystore in GitHub Actions CI/CD, you need to add these secrets to your repository:

1. **SIGNING_KEY**: Base64 encoded keystore file
   ```bash
   base64 -i your-keystore.jks | pbcopy  # macOS
   base64 -i your-keystore.jks           # Linux
   ```

2. **ALIAS**: Your key alias name

3. **KEY_STORE_PASSWORD**: Keystore password

4. **KEY_PASSWORD**: Key password

## Security Best Practices

1. **Never commit keystores to version control**
2. **Use different keystores for debug and release**
3. **Store release keystores securely**
4. **Backup your release keystore and passwords**
5. **Use strong passwords**
6. **Limit access to release keystores**

## Signing Configuration

Add this to your `app/build.gradle`:

```gradle
android {
    signingConfigs {
        debug {
            storeFile file('debug.keystore')
            storePassword 'android'
            keyAlias 'androiddebugkey'
            keyPassword 'android'
        }
        release {
            storeFile file('release.keystore')  // Update path
            storePassword System.getenv("KEYSTORE_PASSWORD")
            keyAlias System.getenv("KEY_ALIAS")
            keyPassword System.getenv("KEY_PASSWORD")
        }
    }
    
    buildTypes {
        debug {
            signingConfig signingConfigs.debug
        }
        release {
            signingConfig signingConfigs.release
        }
    }
}
```

## Troubleshooting

### Common Issues:
1. **"Keystore was tampered with"**: Wrong password
2. **"Certificate expired"**: Create new keystore
3. **"Key alias not found"**: Check alias name

### Verification Commands:
```bash
# List keystore contents
keytool -list -v -keystore your-keystore.jks

# Verify APK signing
apksigner verify --verbose your-app.apk
```