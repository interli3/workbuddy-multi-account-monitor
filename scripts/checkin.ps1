$ErrorActionPreference = 'Stop'
$projectDir = Split-Path -Parent $PSScriptRoot
Set-Location -LiteralPath $projectDir

node src/cli.js import-local
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
node src/cli.js checkin-all
exit $LASTEXITCODE
