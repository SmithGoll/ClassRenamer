#!/usr/bin/bash

SCRIPT_DIR="$(realpath ""$0"")"
SCRIPT_DIR="${SCRIPT_DIR%/*}"

exec java -cp "${SCRIPT_DIR}/build":"${SCRIPT_DIR}/lib/asm-9.6.jar" CMain "$@"
