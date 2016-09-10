REM http://sourceforge.net/projects/backuppc/files/cygwin-rsyncd/3.0.9.0/
REM http://www.commentcamarche.net/faq/7040-rsync-sous-windows

rsync.exe -auvzr --delete --progress "/cygdrive/g/01-Documents" "/cygdrive/f/Backup"
rsync.exe -auvzr --delete --progress "/cygdrive/g/02-Images" "/cygdrive/f/Backup"
echo %DATE% - %TIME% > "F:\Backup\timestamp"

pause
