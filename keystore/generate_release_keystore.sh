#!/bin/bash

# Generate Release Keystore for iOS18Photos Android App
# This script creates a properly formatted PKCS12 keystore for GitHub Actions

set -e

KEYSTORE_NAME="release.keystore"
ALIAS="ios18photos"
VALIDITY_DAYS=25550  # ~70 years

echo "🔐 Generating Release Keystore for iOS18Photos App"
echo "================================================="

# Generate the keystore with proper PKCS12 format
keytool -genkeypair \
    -storetype PKCS12 \
    -keystore "$KEYSTORE_NAME" \
    -alias "$ALIAS" \
    -keyalg RSA \
    -keysize 2048 \
    -validity $VALIDITY_DAYS \
    -sigalg SHA256withRSA \
    -dname "CN=iOS18Photos App, OU=Mobile Development, O=iOS18Photos, L=San Francisco, ST=California, C=US" \
    -storepass "ios18photos2025" \
    -keypass "ios18photos2025"

echo ""
echo "✅ Keystore generated successfully!"
echo ""
echo "📄 Keystore Details:"
echo "   File: $KEYSTORE_NAME"
echo "   Alias: $ALIAS"
echo "   Algorithm: RSA 2048-bit"
echo "   Validity: $VALIDITY_DAYS days (~70 years)"
echo "   Format: PKCS12"
echo ""

# Verify keystore
echo "🔍 Verifying keystore..."
keytool -list -v -keystore "$KEYSTORE_NAME" -storepass "ios18photos2025"

echo ""
echo "📋 GitHub Actions Secrets Setup:"
echo "================================="
echo ""
echo "Add these secrets to your GitHub repository:"
echo ""

# Generate base64 for GitHub Actions
echo "SIGNING_KEY (base64 encoded keystore):"
echo "```"
base64 -i "$KEYSTORE_NAME"
echo "```"
echo ""

echo "ALIAS:"
echo "$ALIAS"
echo ""

echo "KEY_STORE_PASSWORD:"
echo "ios18photos2025"
echo ""

echo "KEY_PASSWORD:"
echo "ios18photos2025"
echo ""

echo "⚠️  Security Notes:"
echo "- Never commit this keystore to version control"
echo "- Store passwords securely"
echo "- Use different passwords for production"
echo "- Keep backup copies in secure location"
echo ""

echo "🚀 Setup Complete!"
echo "Your release keystore is ready for GitHub Actions CI/CD"