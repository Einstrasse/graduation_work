#!/bin/bash

cd ~pi/Projects/weekly_alarm
nohup ./alarmserver &>/var/log/alarmserver.log &
exit 1