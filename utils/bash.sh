#!/bin/sh
#set -e
set -x

DEBUG=0

execute_command()
{
  if [ $DEBUG -eq 0 ]
  then
    echo Execute $@
    $@ > /dev/null
  else
    echo [DEBUG] Execute $@
    $@
  fi
  return $?
}

exit_on_error()
{
  if [ $1 -ne 0 ]; then exit $1; fi
}

check_command()
{
  COMMAND=$1
  result=0
  echo Checking $COMMAND is available
  execute_command command -v $COMMAND
  command_result=$?
  if [ $command_result -eq 0 ]
  then
    echo $COMMAND available
  else
    echo $COMMAND not available
    result=$command_result
  fi
  return $result
}

delete_folder()
{
  FOLDER_PATH=$1
  result=0
  if [ -d $FOLDER_PATH ]
  then
    echo Deleting folder $FOLDER_PATH
    execute_command rm -rf $FOLDER_PATH
    rm_result=$?
    if [ $rm_result -eq 0 ]
    then
      echo Folder $FOLDER_PATH deleted
    else
      echo Failed to delete folder $FOLDER_PATH 
      result=$rm_result
    fi
  else
    echo "Folder $FOLDER_PATH doesn't exist."
  fi
  return $result
}
