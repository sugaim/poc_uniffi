@echo off
REM Build and Run Java Options Calculator
REM This script builds the Rust library, generates Kotlin bindings, and runs the Java application

echo === Building Rust FFI Library ===
cd /d "%~dp0\.."

REM Build the Rust library
cargo build

echo === Building and Running Java Application ===
cd java

REM Make sure we have gradle wrapper
if not exist gradlew.bat (
    gradle wrapper
)

REM Build and run the application
call gradlew.bat build
call gradlew.bat run

echo === Running Tests ===
call gradlew.bat test

echo === Build Complete ===
pause
