@REM Build rust and run uniffi-bindgen to generate Kotlin bindings
cd /d "%~dp0\.."

echo === Building Rust FFI Library ===
cargo build --release

echo === Generating Python Bindings ===
cargo run --bin uniffi-bindgen -- generate --language python --out-dir python/src/external --library target/release/poc_ffi.dll
copy target\release\poc_ffi.dll python\src\external\poc_ffi.dll

echo === Python Bindings Generated Successfully ===
