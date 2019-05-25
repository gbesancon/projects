REM http://sourceforge.net/projects/backuppc/files/cygwin-rsyncd/3.0.9.0/
REM http://www.commentcamarche.net/faq/7040-rsync-sous-windows

SET SOURCE_DRIVE=%1
SET BACKUP_DRIVE=%2

ECHO BACKUP %SOURCE_DRIVE% TO %BACKUP_DRIVE%

rsync.exe -auvzr --delete --progress --dry-run "/cygdrive/%SOURCE_DRIVE%/01-Documents" "/cygdrive/%BACKUP_DRIVE%/Backup"
rsync.exe -auvzr --delete --progress --dry-run "/cygdrive/%SOURCE_DRIVE%/02-Multimedia" "/cygdrive/%BACKUP_DRIVE%/Backup"

pause
