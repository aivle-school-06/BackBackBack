#!/usr/bin/env bash
set -euo pipefail

APP_NAME="${APP_NAME:-backbackback}"

if command -v systemctl >/dev/null 2>&1; then
  systemctl daemon-reload
  systemctl restart "$APP_NAME"
  echo "[INFO] 서버 시작 완료 (systemd: $APP_NAME)"
  exit 0
fi

echo "[ERROR] systemctl을 찾지 못했습니다." >&2
exit 1
