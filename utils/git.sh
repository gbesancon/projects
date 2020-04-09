#!/bin/sh

source $(dirname $(realpath ${BASH_SOURCE[0]}))/bash.sh

git_clone_repository()
{
  URL=$1
  FOLDER_PATH=$2
  result=0
  delete_folder $FOLDER_PATH
  delete_folder_result=$?
  if [ $delete_folder_result -eq 0 ]
  then
    echo Cloning repository $URL
    execute_command git clone --recurse-submodules $URL $FOLDER_PATH
    git_clone_result=$?
    if [ $git_clone_result -eq 0 ]
    then
      echo Repository $URL cloned.
    else
      echo Failed cloning repository $URL.
      result=$git_clone_result
    fi
  fi
  return $result
}

git_checkout_branch()
{
  BRANCH_NAME=$1
  result=0
  echo Checking out branch $BRANCH_NAME
  execute_command git checkout $BRANCH_NAME
  git_checkout_result=$?
  if [ $git_checkout_result -eq 0 ]
  then
    echo Branch $BRANCH_NAME checked out 
  else
    echo Failed checking out branch $BRANCH_NAME
    result=$git_checkout_result
  fi
  return $result
}

git_pull()
{
  result=0
  echo Pulling git commits
  execute_command git pull
  git_pull_result=$?
  if [ $git_pull_result -eq 0 ]
  then
    echo Git commits pulled
  else
    echo Failed pulling git commits
    result=$git_pull_result
  fi
  return $result
}

git_status()
{
  result=0
  echo Show status
  execute_command git status
  git_status_result=$?
  if [ $git_status_result -eq 0 ]
  then
    echo Status showed
  else
    echo Failed to show status
    result=$git_status_result
  fi
  return $result
}

git_add_all()
{
  result=0
  echo Adding all files
  execute_command git add --all
  git_add_result=$?
  if [ $git_add_result -eq 0 ]
  then
    echo All files added
  else
    echo Failed adding all files
    result=$git_add_result
  fi
  return $result
}

git_commit()
{
  MESSAGE=$1
  result=0
  echo Comiting files
  git commit -m "$MESSAGE"
  git_commit_result=$?
  if [ $git_commit_result -eq 0 ]
  then
    echo Files committed
  else
    echo Failed committing files
    result=$git_commit_result
  fi
  return $result
}

git_push()
{
  result=0
  echo Pushing commits
  execute_command git push
  git_push_result=$?
  if [ $git_push_result -eq 0 ]
  then
    echo Commits pushed.
  else
    echo Failed pushing commits
    result=$git_push_result
  fi
  return $result
}