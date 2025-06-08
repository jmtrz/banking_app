# PowerShell script to run the Java banking application
# Navigate to the project root directory
Set-Location -Path $PSScriptRoot

# Compile all Java files from the root directory
Write-Host "Compiling Banking Application..."
javac -d bin src\*.java src\ui\*.java src\events\*.java

# Run the application with the correct classpath
Write-Host "Running Banking Application..."
java -cp bin App

Write-Host "Application started!"