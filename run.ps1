# Banking Application Runner Script
# This script compiles and runs the consolidated Banking Application

Write-Host "Banking Application Runner" -ForegroundColor Green
Write-Host "=========================" -ForegroundColor Green
Write-Host ""

# Check if Java is installed
try {
    $javaVersion = java -version 2>&1
    Write-Host "Java version detected:" -ForegroundColor Yellow
    Write-Host $javaVersion[0] -ForegroundColor White
    Write-Host ""
} catch {
    Write-Host "ERROR: Java is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Java JDK 8 or higher and add it to your PATH" -ForegroundColor Red
    exit 1
}

# Check if javac is available
try {
    javac -version 2>&1 | Out-Null
    Write-Host "Java compiler (javac) is available" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Java compiler (javac) is not available" -ForegroundColor Red
    Write-Host "Please ensure JDK is installed (not just JRE)" -ForegroundColor Red
    exit 1
}

# Check if BankingApp.java exists
if (-not (Test-Path "BankingApp.java")) {
    Write-Host "ERROR: BankingApp.java not found in current directory" -ForegroundColor Red
    Write-Host "Please ensure you are in the correct directory" -ForegroundColor Red
    exit 1
}

Write-Host "Compiling BankingApp.java..." -ForegroundColor Yellow

# Compile the Java application
try {
    javac BankingApp.java
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Compilation successful!" -ForegroundColor Green
    } else {
        Write-Host "Compilation failed!" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "ERROR: Compilation failed" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Starting Banking Application..." -ForegroundColor Yellow
Write-Host ""
Write-Host "Login Credentials:" -ForegroundColor Cyan
Write-Host "Username: adminx" -ForegroundColor White
Write-Host "Password: 12345" -ForegroundColor White
Write-Host ""
Write-Host "Application Features:" -ForegroundColor Cyan
Write-Host "- Customer Management (Add, Edit, Delete, View)" -ForegroundColor White
Write-Host "- Transaction Management (Deposit, Withdraw, Transfer)" -ForegroundColor White
Write-Host "- Transaction Logs and Reports" -ForegroundColor White
Write-Host "- Real-time Dashboard with Statistics" -ForegroundColor White
Write-Host ""

# Run the Java application
try {
    java BankingApp
} catch {
    Write-Host "ERROR: Failed to run the application" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Application closed." -ForegroundColor Green