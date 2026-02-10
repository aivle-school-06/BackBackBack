#!/usr/bin/env bash
set -euo pipefail

APP_NAME="${APP_NAME:-backbackback}"
ENV_FILE="${ENV_FILE:-/etc/${APP_NAME}/${APP_NAME}.env}"

if [ -f "$ENV_FILE" ]; then
  set -a
  # shellcheck disable=SC1090
  . "$ENV_FILE"
  set +a
fi

HEALTHCHECK_URL="${HEALTHCHECK_URL:-}"

if [ -n "$HEALTHCHECK_URL" ]; then
  if ! curl -fsS "$HEALTHCHECK_URL" >/dev/null; then
    echo "[ERROR] 헬스체크 실패: $HEALTHCHECK_URL" >&2
    exit 1
  fi
  echo "[INFO] 헬스체크 성공: $HEALTHCHECK_URL"
  exit 0
fi

HEALTHCHECK_URL="http://localhost:8080/actuator/health"
if ! curl -fsS "$HEALTHCHECK_URL" >/dev/null; then
  echo "[ERROR] 헬스체크 실패: $HEALTHCHECK_URL" >&2
  exit 1
fi
echo "[INFO] 헬스체크 성공: $HEALTHCHECK_URL"
exit 0
