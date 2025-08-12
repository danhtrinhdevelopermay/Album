#!/bin/bash

# Validate GitHub Actions Workflow Syntax
# Checks for common syntax errors in the workflow file

echo "🔍 Validating GitHub Actions Workflow Syntax..."
echo "=============================================="

WORKFLOW_FILE=".github/workflows/build-apk.yml"
ERRORS=0

# Check if workflow file exists
if [ ! -f "$WORKFLOW_FILE" ]; then
    echo "❌ Workflow file not found: $WORKFLOW_FILE"
    exit 1
fi

echo "✅ Workflow file found: $WORKFLOW_FILE"

# Check for basic YAML structure
echo ""
echo "📋 Checking YAML structure..."

# Check for required top-level keys
if grep -q "^name:" "$WORKFLOW_FILE"; then
    echo "✅ 'name' key found"
else
    echo "❌ Missing 'name' key"
    ((ERRORS++))
fi

if grep -q "^on:" "$WORKFLOW_FILE"; then
    echo "✅ 'on' key found"
else
    echo "❌ Missing 'on' key"
    ((ERRORS++))
fi

if grep -q "^jobs:" "$WORKFLOW_FILE"; then
    echo "✅ 'jobs' key found"
else
    echo "❌ Missing 'jobs' key"
    ((ERRORS++))
fi

# Check for proper indentation
echo ""
echo "📏 Checking indentation..."

if grep -q "^  [^ ]" "$WORKFLOW_FILE"; then
    echo "✅ Proper indentation detected"
else
    echo "⚠️  No 2-space indentation found (may be using tabs)"
fi

# Check for secrets syntax issues
echo ""
echo "🔐 Checking secrets syntax..."

# Check for invalid secrets comparisons
if grep -q "secrets\.[A-Z_]* != ''" "$WORKFLOW_FILE"; then
    echo "❌ Found invalid secrets comparison with != ''"
    echo "   Use: secrets.SECRET_NAME (for truthy check)"
    echo "   Or:  !secrets.SECRET_NAME (for falsy check)"
    ((ERRORS++))
elif grep -q "secrets\.[A-Z_]* == ''" "$WORKFLOW_FILE"; then
    echo "❌ Found invalid secrets comparison with == ''"
    echo "   Use: !secrets.SECRET_NAME (for falsy check)"
    echo "   Or:  secrets.SECRET_NAME (for truthy check)"
    ((ERRORS++))
else
    echo "✅ Secrets syntax appears correct"
fi

# Check for proper secret references
SECRET_REFS=$(grep -o '\${{ secrets\.[A-Z_]* }}' "$WORKFLOW_FILE" | sort -u)
if [ -n "$SECRET_REFS" ]; then
    echo "✅ Found secret references:"
    echo "$SECRET_REFS" | sed 's/^/   /'
else
    echo "⚠️  No secret references found"
fi

# Check for conditional syntax
echo ""
echo "🔀 Checking conditional syntax..."

if grep -q "if: github\.ref == 'refs/heads/main'" "$WORKFLOW_FILE"; then
    echo "✅ Main branch conditions found"
else
    echo "⚠️  No main branch conditions found"
fi

# Check for actions versions
echo ""
echo "📦 Checking action versions..."

ACTION_VERSIONS=(
    "actions/checkout@v4"
    "actions/setup-java@v4"
    "actions/cache@v4"
    "actions/upload-artifact@v4"
    "android-actions/setup-android@v3"
    "softprops/action-gh-release@v2"
)

for action in "${ACTION_VERSIONS[@]}"; do
    if grep -q "$action" "$WORKFLOW_FILE"; then
        echo "✅ Using recommended version: $action"
    else
        ACTION_NAME=$(echo "$action" | cut -d'@' -f1)
        if grep -q "$ACTION_NAME" "$WORKFLOW_FILE"; then
            FOUND_VERSION=$(grep "$ACTION_NAME" "$WORKFLOW_FILE" | head -1 | grep -o '@v[0-9]*')
            echo "⚠️  Found $ACTION_NAME$FOUND_VERSION (recommended: $action)"
        fi
    fi
done

# Check for proper job dependencies
echo ""
echo "🔗 Checking job dependencies..."

if grep -q "needs: \[lint, test\]" "$WORKFLOW_FILE"; then
    echo "✅ Build job properly depends on lint and test"
else
    echo "⚠️  Build job dependencies may be incorrect"
fi

# Summary
echo ""
echo "📊 Validation Summary"
echo "===================="

if [ $ERRORS -eq 0 ]; then
    echo "✅ No syntax errors found in GitHub Actions workflow"
    echo ""
    echo "Next steps to verify workflow:"
    echo "1. Commit and push the workflow file"
    echo "2. Check GitHub Actions tab for any syntax errors"
    echo "3. Create test push to trigger the workflow"
else
    echo "❌ Found $ERRORS syntax errors"
    echo ""
    echo "Please fix the errors above before deploying"
    exit 1
fi