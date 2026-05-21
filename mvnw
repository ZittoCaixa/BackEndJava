#!/usr/bin/env sh
DIR="$(cd "$(dirname "$0")" && pwd)"
powershell -NoProfile -ExecutionPolicy Bypass -File "$DIR/.mvn/wrapper/mvnw.ps1" "$@"

