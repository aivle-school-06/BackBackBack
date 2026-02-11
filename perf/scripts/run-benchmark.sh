#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 1 ]]; then
  echo "Usage: $0 <before|after> [run_id]"
  exit 1
fi

MODE="$1"
RUN_ID="${2:-$(date +%Y%m%d-%H%M%S)}"

if [[ "$MODE" != "before" && "$MODE" != "after" ]]; then
  echo "MODE must be before or after"
  exit 1
fi

if ! command -v jmeter >/dev/null 2>&1; then
  echo "jmeter command not found"
  exit 1
fi

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
RESULT_DIR="${ROOT_DIR}/perf/results/${RUN_ID}/${MODE}"
mkdir -p "${RESULT_DIR}"

if [[ "${MODE}" == "before" ]]; then
  VT_ENABLED=false
else
  VT_ENABLED=true
fi

pushd "${ROOT_DIR}" >/dev/null

echo "[${MODE}] starting app with perf profile..."
SPRING_PROFILES_ACTIVE=perf \
SPRING_THREADS_VIRTUAL_ENABLED="${VT_ENABLED}" \
APP_VIRTUAL_THREAD_ENABLED="${VT_ENABLED}" \
APP_VIRTUAL_THREAD_INSIGHT_ENABLED="${VT_ENABLED}" \
APP_VIRTUAL_THREAD_EMAIL_ENABLED="${VT_ENABLED}" \
./gradlew bootRun --no-daemon >"${RESULT_DIR}/server.log" 2>&1 &
SERVER_PID=$!

cleanup() {
  if ps -p "${SERVER_PID}" >/dev/null 2>&1; then
    kill "${SERVER_PID}" >/dev/null 2>&1 || true
    wait "${SERVER_PID}" >/dev/null 2>&1 || true
  fi
}
trap cleanup EXIT

echo "[${MODE}] waiting for server..."
READY=false
for _ in {1..120}; do
  if curl -sf "http://localhost:8080/api/perf/benchmark/fixture" >/dev/null; then
    READY=true
    break
  fi
  sleep 1
done

if [[ "${READY}" != "true" ]]; then
  echo "server not ready in time"
  exit 1
fi

echo "[${MODE}] running jmeter..."
jmeter -n \
  -t "${ROOT_DIR}/perf/jmeter/phase1-virtual-thread-benchmark.jmx" \
  -q "${ROOT_DIR}/perf/jmeter/props/common.properties" \
  -Jcompany.id=1 \
  -Jcompany.stock.code=900001 \
  -l "${RESULT_DIR}/results.jtl" \
  -e -o "${RESULT_DIR}/html-report" \
  >"${RESULT_DIR}/jmeter.log" 2>&1

cat > "${RESULT_DIR}/run-meta.env" <<EOF
RUN_ID=${RUN_ID}
MODE=${MODE}
SPRING_PROFILES_ACTIVE=perf
SPRING_THREADS_VIRTUAL_ENABLED=${VT_ENABLED}
APP_VIRTUAL_THREAD_ENABLED=${VT_ENABLED}
APP_VIRTUAL_THREAD_INSIGHT_ENABLED=${VT_ENABLED}
APP_VIRTUAL_THREAD_EMAIL_ENABLED=${VT_ENABLED}
EOF

echo "[${MODE}] completed: ${RESULT_DIR}"
popd >/dev/null
