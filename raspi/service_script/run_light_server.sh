#!/bin/bash

cd ~pi/Projects
nohup ./lightserver &>/var/log/lightserver.log &
exit 1