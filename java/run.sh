#!/usr/bin/env bash

# Build and Run Java Options Calculator
# This script builds the Rust library, generates Kotlin bindings, and runs the Java application

set -e

echo "=== Building Rust FFI Library ==="
cd "$(dirname "$0")/.."

# Build the Rust library
cargo build

echo "=== Building and Running Java Application ==="
cd java

# Make sure we have gradle wrapper
if [ ! -f gradlew ]; then
    gradle wrapper
fi

# Build and run the application
./gradlew build
./gradlew run

echo "=== Running Tests ==="
./gradlew test

echo "=== Build Complete ==="
