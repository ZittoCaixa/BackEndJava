$ErrorActionPreference = 'Stop'

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectDir = Resolve-Path (Join-Path $scriptDir "..\..")
$wrapperProps = Join-Path $scriptDir "maven-wrapper.properties"

if (!(Test-Path $wrapperProps)) {
    Write-Error "Arquivo nao encontrado: $wrapperProps"
}

$distributionUrl = (Get-Content $wrapperProps | Where-Object { $_ -like 'distributionUrl=*' } | Select-Object -First 1).Split('=')[1]
if ([string]::IsNullOrWhiteSpace($distributionUrl)) {
    Write-Error "distributionUrl nao configurada em maven-wrapper.properties"
}

$downloadsDir = Join-Path $projectDir ".mvn\wrapper\downloads"
$installDir = Join-Path $projectDir ".mvn\wrapper\apache-maven"
New-Item -ItemType Directory -Force -Path $downloadsDir | Out-Null
New-Item -ItemType Directory -Force -Path $installDir | Out-Null

$zipName = Split-Path $distributionUrl -Leaf
$zipPath = Join-Path $downloadsDir $zipName
$mavenHome = Join-Path $installDir ($zipName -replace '-bin.zip', '')
$mvnCmd = Join-Path $mavenHome "bin\mvn.cmd"

if (!(Test-Path $mvnCmd)) {
    Write-Host "Baixando Maven: $distributionUrl"
    Invoke-WebRequest -Uri $distributionUrl -OutFile $zipPath

    Write-Host "Extraindo Maven em $installDir"
    Expand-Archive -Path $zipPath -DestinationPath $installDir -Force
}

if (!(Test-Path $mvnCmd)) {
    Write-Error "Nao foi possivel localizar mvn.cmd em $mvnCmd"
}

Push-Location $projectDir
try {
    & $mvnCmd @args
    exit $LASTEXITCODE
}
finally {
    Pop-Location
}

