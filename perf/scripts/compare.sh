#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 1 ]]; then
  echo "Usage: $0 <run_id>"
  exit 1
fi

RUN_ID="$1"
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
BASE_DIR="${ROOT_DIR}/perf/results/${RUN_ID}"
BEFORE_JTL="${BASE_DIR}/before/results.jtl"
AFTER_JTL="${BASE_DIR}/after/results.jtl"
SUMMARY_MD="${BASE_DIR}/comparison-summary.md"

if [[ ! -f "${BEFORE_JTL}" || ! -f "${AFTER_JTL}" ]]; then
  echo "before/after JTL not found under ${BASE_DIR}"
  exit 1
fi

summarize_jtl() {
  local jtl_file="$1"
  awk -F',' '
    $1 == "timeStamp" { next }
    {
      label = $3
      elapsed = $2 + 0
      success = ($8 == "true")

      count[label]++
      total[label] += elapsed
      if (!success) {
        error[label]++
      }

      values[label] = values[label] " " elapsed

      if (!(label in minTs) || $1 < minTs[label]) {
        minTs[label] = $1
      }
      if (!(label in maxTs) || $1 > maxTs[label]) {
        maxTs[label] = $1
      }
    }
    END {
      for (label in count) {
        split(values[label], raw, " ")
        n = 0
        delete sorted
        for (i in raw) {
          if (raw[i] != "") {
            n++
            sorted[n] = raw[i] + 0
          }
        }
        asort(sorted)
        p95Index = int((n * 95 + 99) / 100)
        if (p95Index < 1) {
          p95Index = 1
        }

        durationSec = (maxTs[label] - minTs[label]) / 1000.0
        if (durationSec <= 0) {
          durationSec = 1
        }

        avg = total[label] / count[label]
        p95 = sorted[p95Index]
        errRate = (error[label] + 0) * 100.0 / count[label]
        tps = count[label] / durationSec

        printf "%s\t%d\t%.2f\t%.2f\t%.2f\t%.2f\n", label, count[label], errRate, avg, p95, tps
      }
    }
  ' "${jtl_file}" | sort -k1,1
}

BEFORE_SUMMARY="$(summarize_jtl "${BEFORE_JTL}")"
AFTER_SUMMARY="$(summarize_jtl "${AFTER_JTL}")"

{
  echo "# Phase1 Virtual Thread Benchmark Summary"
  echo
  echo "- run_id: ${RUN_ID}"
  echo "- generated_at: $(date '+%Y-%m-%d %H:%M:%S')"
  echo
  echo "## Before"
  echo
  echo "| label | samples | error% | avg(ms) | p95(ms) | tps |"
  echo "|---|---:|---:|---:|---:|---:|"
  while IFS=$'\t' read -r label samples error avg p95 tps; do
    [[ -z "${label:-}" ]] && continue
    printf "| %s | %s | %s | %s | %s | %s |\n" "$label" "$samples" "$error" "$avg" "$p95" "$tps"
  done <<< "${BEFORE_SUMMARY}"
  echo
  echo "## After"
  echo
  echo "| label | samples | error% | avg(ms) | p95(ms) | tps |"
  echo "|---|---:|---:|---:|---:|---:|"
  while IFS=$'\t' read -r label samples error avg p95 tps; do
    [[ -z "${label:-}" ]] && continue
    printf "| %s | %s | %s | %s | %s | %s |\n" "$label" "$samples" "$error" "$avg" "$p95" "$tps"
  done <<< "${AFTER_SUMMARY}"
} > "${SUMMARY_MD}"

echo "comparison summary generated: ${SUMMARY_MD}"
