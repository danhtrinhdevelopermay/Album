# Keystore Management

This directory contains keystores for signing Android APKs and tools for managing them.

## Files

- `debug.keystore` - Debug keystore for development builds
- `generate_release_keystore.sh` - Script to generate production keystore
- `release.keystore` - **NOT INCLUDED** - Production keystore for release builds

## Debug Keystore

The debug keystore is included for development convenience with default Android debug credentials:

- **Keystore Password**: android
- **Key Alias**: androiddebugkey
- **Key Password**: android
- **Validity**: 30 years from creation

## Release Keystore Generation

### Automated Generation

Use the provided script to generate a properly formatted release keystore:

```bash
cd keystore
./generate_release_keystore.sh
```

This script will:
- Generate a PKCS12 format keystore (compatible with modern Android tools)
- Create RSA 2048-bit keys with SHA256 signature algorithm
- Set 70-year validity period
- Output all required information for GitHub Actions setup
- Provide base64 encoded keystore for CI/CD

### Manual Generation

If you prefer manual generation:

```bash
keytool -genkeypair \
    -storetype PKCS12 \
    -keystore release.keystore \
    -alias ios18photos \
    -keyalg RSA \
    -keysize 2048 \
    -validity 25550 \
    -sigalg SHA256withRSA \
    -dname "CN=iOS18Photos App, OU=Mobile Development, O=iOS18Photos, L=San Francisco, ST=California, C=US"
```

### Security Guidelines

1. **Never commit the release keystore to version control**
2. Store the keystore in a secure location
3. Use strong passwords
4. Keep backup copies
5. Document the keystore details securely

## GitHub Actions Setup

### Required Secrets

Add these secrets to your GitHub repository settings:

1. **SIGNING_KEY**: Base64 encoded keystore file
   ```bash
   base64 -i release.keystore | pbcopy  # macOS
   base64 -i release.keystore           # Linux
   ```

2. **ALIAS**: Key alias name (default: `ios18photos`)

3. **KEY_STORE_PASSWORD**: Keystore password

4. **KEY_PASSWORD**: Key password

### Setting Up Secrets

1. Go to your GitHub repository
2. Navigate to Settings → Secrets and variables → Actions
3. Click "New repository secret" for each secret
4. Use the exact names above (case-sensitive)
5. Paste the corresponding values

## Troubleshooting

### Fixed Issues

✅ **"Tag number over 30 is not supported"**
- **Cause**: Old keystore format or corrupted keystore
- **Solution**: Use the provided generation script which creates PKCS12 format
- **Status**: Resolved in updated CI/CD pipeline

✅ **Third-party signing action failures**
- **Cause**: Outdated r0adkll/sign-android-release@v1 action
- **Solution**: Replaced with native jarsigner and apksigner tools
- **Status**: Resolved with direct Android SDK tools

### Common Issues

1. **"keystore was tampered with"**
   - Check that base64 encoding/decoding was done correctly
   - Verify the keystore password is correct
   - Ensure no line breaks in base64 string

2. **"Alias not found"**
   - Verify the alias name matches exactly
   - List keystore contents: `keytool -list -v -keystore release.keystore`

3. **APK not signed properly**
   - Check that all four secrets are set correctly
   - Verify keystore format is PKCS12
   - Ensure passwords match keystore configuration

## Verification

After setting up the keystore, verify it works:

```bash
# Check keystore contents
keytool -list -v -keystore release.keystore

# Test signing (dry run)
jarsigner -verify -verbose -certs your-app.apk
```