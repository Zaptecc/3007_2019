@echo off
set /p filename=Enter file location: 
git add -v %filename%
timeout /t 10