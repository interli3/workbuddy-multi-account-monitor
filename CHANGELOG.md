# Changelog

## 3.6.1 - 2026-09-19

- Validate every cached login candidate against the official resource endpoint before replacing an account token.
- Keep multi-account scheduled check-in synchronized with current and historical WorkBuddy login snapshots.
- Treat HTTP 400 / business code 10001 as the official idempotent “already checked in” result.
- Add regression tests for revoked newer snapshots, invalid candidates and check-in business responses.

## 3.6.0 - 2026-09-16

- Dynamic health cards for all current and future accounts.
- Fresh light glass Android UI with overview, accounts and records sections.
- Recent deductions on the overview and complete history in records.
- Corrected cumulative-credit and daily-usage calculations.
- Idempotent multi-account daily check-in with Windows scheduling helpers.
- LAN HTTP and optional MQTT snapshot delivery.
- JWT expiry fallback when legacy credential entries lack `expiresAt`.
- Credential expiry affects health only after the authorization is actually invalid.
- Privacy-safe first-run configuration and local signing material generation.
