@echo off

:: Файл убивает процессы с портами 9876 и 9877
::  https://gitlab.com/whirlplatform/whirl/-/issues/49

setlocal enabledelayedexpansion

set "ports=9876 9877"

for %%i in (%ports%) do (
    set "port=%%i"
    for /f "tokens=5" %%a in ('netstat -aon ^| findstr !port!') do (
        taskkill /f /pid %%a
    )
)

pause
