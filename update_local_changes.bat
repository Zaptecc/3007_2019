@echo off
git add -uv
set /p com_detail=Enter commit message: 
git commit -m "%com_detail%"
git push origin master
timeout /t 10