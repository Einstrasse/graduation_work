#!/bin/bash

cd /home/pi/Desktop/mjpg-streamer/mjpg-streamer-experimental
export LD_LIBRARY_PATH=`pwd`
nohup ./mjpg_streamer -i "./input_uvc.so -n -f 15 -r 640x480 -l off" -o "./output_http.so -n -p 8083 -w ./www" &>/var/log/cctv.log &
exit 0