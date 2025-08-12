#!/bin/bash

# Validate CI/CD Setup for iOS18Photos Android Project
# This script checks that all build requirements are met

set -e

echo "🔍 Validating CI/CD Setup for iOS18Photos Android Project"
echo "=========================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

SUCCESS=0
WARNINGS=0
ERRORS=0

check_success() {
    echo -e "${GREEN}✅ $1${NC}"
    ((SUCCESS++))
}

check_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
    ((WARNINGS++))
}

check_error() {
    echo -e "${RED}❌ $1${NC}"
    ((ERRORS++))
}

# Check Java environment
echo ""
echo "📋 Checking Build Environment..."
echo "--------------------------------"

if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
    echo "Java Version: $JAVA_VERSION"
    if [[ "$JAVA_VERSION" =~ ^17\. ]] || [[ "$JAVA_VERSION" =~ ^1\.8\. ]]; then
        check_success "Java version is compatible"
    else
        check_warning "Java version may not be optimal (recommended: 17 or 8)"
    fi
else
    check_error "Java not found - required for Android builds"
fi

# Check Android SDK
if [ -n "$ANDROID_HOME" ]; then
    check_success "ANDROID_HOME is set: $ANDROID_HOME"
    
    if [ -f "$ANDROID_HOME/build-tools/34.0.0/aapt" ]; then
        check_success "Android Build Tools 34.0.0 found"
    else
        check_error "Android Build Tools 34.0.0 not found"
    fi
else
    check_warning "ANDROID_HOME not set (may be set in CI environment)"
fi

# Check Gradle wrapper
echo ""
echo "🔧 Checking Gradle Setup..."
echo "---------------------------"

if [ -f "./gradlew" ]; then
    check_success "Gradle wrapper found"
    
    if [ -x "./gradlew" ]; then
        check_success "Gradle wrapper is executable"
    else
        check_warning "Gradle wrapper is not executable (will be fixed in CI)"
    fi
else
    check_error "Gradle wrapper not found"
fi

# Check project structure
echo ""
echo "📁 Checking Project Structure..."
echo "--------------------------------"

required_files=(
    "app/build.gradle"
    "app/src/main/AndroidManifest.xml"
    "settings.gradle"
    "build.gradle"
)

for file in "${required_files[@]}"; do
    if [ -f "$file" ]; then
        check_success "Required file found: $file"
    else
        check_error "Required file missing: $file"
    fi
done

# Check GitHub Actions workflow
echo ""
echo "🚀 Checking GitHub Actions Workflow..."
echo "-------------------------------------"

if [ -f ".github/workflows/build-apk.yml" ]; then
    check_success "GitHub Actions workflow found"
    
    # Check for updated signing approach
    if grep -q "jarsigner" ".github/workflows/build-apk.yml"; then
        check_success "Updated APK signing method detected"
    else
        check_warning "May be using outdated signing method"
    fi
    
    # Check for secrets handling
    if grep -q "secrets.SIGNING_KEY" ".github/workflows/build-apk.yml"; then
        check_success "Keystore secrets configuration found"
    else
        check_error "Keystore secrets not configured"
    fi
else
    check_error "GitHub Actions workflow not found"
fi

# Check keystore setup
echo ""
echo "🔐 Checking Keystore Configuration..."
echo "------------------------------------"

if [ -f "keystore/debug.keystore" ]; then
    check_success "Debug keystore found"
else
    check_error "Debug keystore missing"
fi

if [ -f "keystore/generate_release_keystore.sh" ]; then
    check_success "Release keystore generation script found"
    
    if [ -x "keystore/generate_release_keystore.sh" ]; then
        check_success "Generation script is executable"
    else
        check_warning "Generation script is not executable"
    fi
else
    check_error "Release keystore generation script missing"
fi

if [ -f "keystore/release.keystore" ]; then
    check_warning "Release keystore found in repository (should not be committed)"
else
    check_success "Release keystore not in repository (correct for security)"
fi

# Check lint configuration
echo ""
echo "🔍 Checking Code Quality Setup..."
echo "--------------------------------"

if [ -f "app/lint-baseline.xml" ]; then
    check_success "Lint baseline configuration found"
else
    check_warning "Lint baseline not found (may cause CI failures)"
fi

# Check for build outputs
echo ""
echo "📦 Checking Build Outputs..."
echo "----------------------------"

if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    APK_SIZE=$(du -h "app/build/outputs/apk/debug/app-debug.apk" | cut -f1)
    check_success "Debug APK found (size: $APK_SIZE)"
else
    check_warning "Debug APK not found (run ./gradlew assembleDebug to build)"
fi

# Test local build
echo ""
echo "🔨 Testing Local Build..."
echo "-------------------------"

if [ -x "./gradlew" ]; then
    echo "Running lint check..."
    if ./gradlew lintDebug > /dev/null 2>&1; then
        check_success "Lint check passes"
    else
        check_warning "Lint check has issues (check app/build/reports/lint-results-debug.html)"
    fi
    
    echo "Testing debug build..."
    if ./gradlew assembleDebug > /dev/null 2>&1; then
        check_success "Debug build succeeds"
    else
        check_error "Debug build fails"
    fi
else
    check_warning "Cannot test local build (gradlew not executable)"
fi

# Summary
echo ""
echo "📊 Validation Summary"
echo "===================="
echo -e "${GREEN}✅ Successes: $SUCCESS${NC}"
echo -e "${YELLOW}⚠️  Warnings:  $WARNINGS${NC}"
echo -e "${RED}❌ Errors:    $ERRORS${NC}"

echo ""
if [ $ERRORS -eq 0 ]; then
    if [ $WARNINGS -eq 0 ]; then
        echo -e "${GREEN}🎉 Perfect! Your CI/CD setup is ready for GitHub Actions.${NC}"
    else
        echo -e "${YELLOW}✅ Good! CI/CD should work with minor warnings noted above.${NC}"
    fi
    echo ""
    echo "Next steps:"
    echo "1. Commit and push your changes"
    echo "2. Generate release keystore: cd keystore && ./generate_release_keystore.sh"
    echo "3. Add GitHub secrets: SIGNING_KEY, ALIAS, KEY_STORE_PASSWORD, KEY_PASSWORD"
    echo "4. Push to trigger GitHub Actions build"
else
    echo -e "${RED}🔧 Issues found! Please fix the errors above before proceeding.${NC}"
    exit 1
fi