#!/usr/bin/env bash
set -euo pipefail

APP_DIR="${APP_DIR:-/home/ec2-user/app/BackBackBack}"
PID_FILE="${PID_FILE:-$APP_DIR/app.pid}"

if [ ! -f "$PID_FILE" ]; then
  echo "[INFO] PID 파일이 없어 종료를 건너뜁니다."
  exit 0
fi

PID="$(cat "$PID_FILE")"

if kill -0 "$PID" 2>/dev/null; then
  kill "$PID"
  for _ in {1..20}; do
    if kill -0 "$PID" 2>/dev/null; then
      sleep 1
    else
      break
    fi
  done
  if kill -0 "$PID" 2>/dev/null; then
    kill -9 "$PID"
  fi
fi

rm -f "$PID_FILE"
echo "[INFO] 서버 종료 완료"
