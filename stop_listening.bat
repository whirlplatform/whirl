@echo off
setlocal enabledelayedexpansion

set "ports=9876 9877"

for %%i in (%ports%) do (
    set "port=%%i"
    for /f "tokens=5" %%a in ('netstat -aon ^| findstr !port!') do (
        taskkill /f /pid %%a
    )
)

pause
