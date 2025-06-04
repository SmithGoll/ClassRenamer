#!/usr/bin/bash

SCRIPT_DIR="$(realpath ""$0"")"
SCRIPT_DIR="${SCRIPT_DIR%/*}"

cd "${SCRIPT_DIR}"

exec javac -cp lib/asm-9.6.jar src/*.java -d build
