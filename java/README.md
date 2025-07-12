# Java Options Calculator

This Java project demonstrates how to use Rust-generated UniFFI bindings from Java to perform Black-Scholes options calculations.

## Project Structure

```
java/
├── build.gradle                    # Gradle build configuration
├── src/
│   ├── main/java/com/example/
│   │   └── OptionsCalculator.java  # Main application
│   └── test/java/com/example/
│       └── OptionsCalculatorTest.java # Unit tests
├── run.bat                         # Windows build/run script
└── run.sh                          # Unix build/run script
```

## Prerequisites

- Java 11 or higher
- Gradle 7.0+ (or use the wrapper)
- Rust toolchain (for building the native library)

## Building and Running

### Quick Start (Windows)
```bash
run.bat
```

### Quick Start (Unix/Linux/Mac)
```bash
chmod +x run.sh
./run.sh
```

### Manual Steps

1. **Build the Rust library** (from project root):
   ```bash
   cargo build
   ```

2. **Build the Java project**:
   ```bash
   cd java
   ./gradlew build
   ```

3. **Run the application**:
   ```bash
   ./gradlew run
   ```

4. **Run tests**:
   ```bash
   ./gradlew test
   ```

## How It Works

The Java application uses the Kotlin UniFFI bindings to call Rust functions:

1. **BsModel**: A data class representing the Black-Scholes model parameters
2. **OptionType**: An enum for Call/Put options
3. **bsCall()**: Function to calculate Black-Scholes call option price
4. **bsPrem()**: Function to calculate option premium using a model

### Example Usage

```java
// Create a Black-Scholes model
BsModel model = new BsModel(100.0, 0.25); // spot=100, volatility=25%

// Calculate call option price
double callPrice = Poc_ffiKt.bsCall(100.0, 105.0, 0.25, 0.05, 0.25);

// Calculate option premiums
double callPremium = Poc_ffiKt.bsPrem(model, 105.0, 0.05, 0.25, OptionType.CALL);
double putPremium = Poc_ffiKt.bsPrem(model, 105.0, 0.05, 0.25, OptionType.PUT);
```

## Dependencies

- **Kotlin stdlib**: Required for Kotlin-Java interoperability
- **JNA**: For native library loading and FFI
- **JUnit**: For unit testing

## Native Library

The application automatically loads the native `poc_ffi.dll` (Windows) library from the `../target/debug/` directory. The Gradle build script handles copying the library to the appropriate location.

## Troubleshooting

1. **Library not found**: Ensure the Rust library is built (`cargo build`)
2. **JNA errors**: Check that the native library is in the correct path
3. **Kotlin compatibility**: Ensure Kotlin stdlib version matches the generated bindings
