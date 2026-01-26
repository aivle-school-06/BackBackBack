#!/usr/bin/env bash
set -euo pipefail

APP_DIR="${APP_DIR:-/home/ec2-user/app/BackBackBack}"
LOG_DIR="${LOG_DIR:-$APP_DIR/logs}"
PID_FILE="${PID_FILE:-$APP_DIR/app.pid}"

cd "$APP_DIR"
mkdir -p "$LOG_DIR"

if [ -f "$APP_DIR/.env" ]; then
  set -a
  # shellcheck disable=SC1091
  . "$APP_DIR/.env"
  set +a
fi

SPRING_PROFILES_ACTIVE="${SPRING_PROFILES_ACTIVE:-dev}"

if ! command -v docker >/dev/null 2>&1; then
  echo "[ERROR] docker 명령을 찾을 수 없습니다." >&2
  exit 1
fi

if command -v systemctl >/dev/null 2>&1; then
  if ! systemctl is-active --quiet docker; then
    sudo systemctl start docker
  fi
fi

if docker compose version >/dev/null 2>&1; then
  DOCKER_COMPOSE="docker compose"
elif command -v docker-compose >/dev/null 2>&1; then
  DOCKER_COMPOSE="docker-compose"
elif sudo -n docker compose version >/dev/null 2>&1; then
  DOCKER_COMPOSE="sudo docker compose"
elif command -v docker-compose >/dev/null 2>&1 && sudo -n docker-compose version >/dev/null 2>&1; then
  DOCKER_COMPOSE="sudo docker-compose"
else
  echo "[ERROR] docker compose 명령을 찾을 수 없습니다." >&2
  exit 1
fi

$DOCKER_COMPOSE up -d

JAR_PATH="${JAR_PATH:-}"
if [ -z "$JAR_PATH" ]; then
  JAR_PATH="$(ls -1 "$APP_DIR"/build/libs/*.jar 2>/dev/null | head -n 1 || true)"
fi

if [ -z "$JAR_PATH" ]; then
  echo "[ERROR] JAR 파일을 찾지 못했습니다. JAR_PATH를 지정하거나 build/libs/*.jar를 포함하세요." >&2
  exit 1
fi

if [ -f "$PID_FILE" ]; then
  if kill -0 "$(cat "$PID_FILE")" 2>/dev/null; then
    echo "[INFO] 이미 실행 중입니다. (pid: $(cat "$PID_FILE"))"
    exit 0
  fi
  rm -f "$PID_FILE"
fi

nohup java ${JAVA_OPTS:-} -jar "$JAR_PATH" \
  --spring.profiles.active="$SPRING_PROFILES_ACTIVE" \
  > "$LOG_DIR/app.log" 2>&1 &

echo $! > "$PID_FILE"
echo "[INFO] 서버 시작 완료 (pid: $(cat "$PID_FILE"))"
