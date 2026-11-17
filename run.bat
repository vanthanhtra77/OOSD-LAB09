@echo off
chcp 65001 > nul
echo Dang bien dich ma nguon...
javac -d bin -encoding UTF-8 src/*.java
if %errorlevel% neq 0 (
    echo Bien dich that bai!
    pause
    exit /b %errorlevel%
)
echo Bien dich thanh cong! Dang chay kiem thu...
echo.
java -cp bin Main
echo.
pause
