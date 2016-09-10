#!/bin/bash

for ANT_FILE in ./*/scripts/*.ant
do
  ../eclipse-tips/eclipse-ant/run_eclipse_ant_runner.sh ~/02-Tools/eclipse-modeling-neon-R-linux-gtk-x86_64 $ANT_FILE 
done

