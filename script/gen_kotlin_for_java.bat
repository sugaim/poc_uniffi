@REM Build rust and run uniffi-bindgen to generate Kotlin bindings
cd /d "%~dp0\.."

echo === Building Rust FFI Library ===
cargo build --release

echo === Generating Kotlin Bindings ===
cargo run --bin uniffi-bindgen -- generate --language kotlin --out-dir java/src/main/kotlin --library target/release/poc_ffi.dll

echo === Kotlin Bindings Generated Successfully ===
