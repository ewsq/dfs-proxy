#!/bin/bash
LOCAL_IP=$1
PEER_IP=$2
DC=$3
if [ ! -d "./masters/m1" ];then
mkdir -p ./masters/m1
fi
if [ ! -d "./volumes/v1" ];then
mkdir -p ./volumes/v1
fi
if [ ! -d "./volumes/v2" ];then
mkdir -p ./volumes/v2
fi
if [ ! -d "./volumes/v3" ];then
mkdir -p ./volumes/v3
fi
pkill -f weed
# rm -rf {./masters/m1,./masters/m2,./masters/m3,./volumes/v1,./volumes/v2,./volumes/v3,./volumes/v4,./volumes/v5,./volumes/v6,./filers/f1}
# mkdir {./masters/m1,./masters/m2,./masters/m3,./volumes/v1,./volumes/v2,./volumes/v3,./volumes/v4,./volumes/v5,./volumes/v6,./filers/f1}

nohup weed -v=3 master -ip=$LOCAL_IP -port=9333 -mdir=./masters/m1 -peers=$LOCAL_IP:9333,$PEER_IP:9333 -defaultReplication=100 > m1.log 2>&1 &

nohup weed -v=3 volume -ip=$LOCAL_IP -port=7081 -dir=./volumes/v1 -mserver=$LOCAL_IP:9333 -dataCenter=$DC -max=100 > v1.log 2>&1 &

nohup weed -v=3 volume -ip=$LOCAL_IP -port=7082 -dir=./volumes/v2 -mserver=$LOCAL_IP:9333 -dataCenter=$DC -max=100 > v2.log 2>&1 &

nohup weed -v=3 volume -ip=$LOCAL_IP -port=7083 -dir=./volumes/v3 -mserver=$LOCAL_IP:9333 -dataCenter=$DC -max=100 > v3.log 2>&1 &