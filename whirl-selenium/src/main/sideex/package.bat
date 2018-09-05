@echo off

setlocal

pushd %~dp0

set APP_NAME=sideex
set CHROME_PROVIDERS=content
set ROOT_DIRS=defaults
set ROOT_DIR=%CD%
set TMP_DIR=build

del /Q %APP_NAME%.xpi >NUL
rd /S /Q %TMP_DIR% >NUL

mkdir %TMP_DIR%\chrome 2>NUL

robocopy.exe chrome\content %TMP_DIR%\chrome\content /E
robocopy.exe chrome\locale %TMP_DIR%\chrome\locale /E
robocopy.exe chrome\skin %TMP_DIR%\chrome\skin /E
robocopy.exe components %TMP_DIR%\components /E
robocopy.exe %ROOT_DIRS% %TMP_DIR%\%ROOT_DIRS% /E
copy install.rdf %TMP_DIR%
copy chrome\chrome.manifest %TMP_DIR%\chrome.manifest

cd %TMP_DIR%
echo Package the %APP_NAME%.xpi

set PATH=%PATH%;%ProgramFiles%\7-Zip;%ProgramFiles(x86)%\7-Zip

7z.exe a -r -y -tzip ../%APP_NAME%.zip *

cd %ROOT_DIR%
rename %APP_NAME%.zip %APP_NAME%.xpi

endlocal
