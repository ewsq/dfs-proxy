## 204:
命令操作
```cmd
cd /weed/
./restart.sh 192.168.1.204 dc1 dc2
```
restart.sh内容
```shell
#!/bin/bash
LOCAL_IP=$1
DC1=$2
DC2=$3
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
if [ ! -d "./volumes/v4" ];then
mkdir -p ./volumes/v4
fi
if [ ! -d "./volumes/v5" ];then
mkdir -p ./volumes/v5
fi
if [ ! -d "./volumes/v6" ];then
mkdir -p ./volumes/v6
fi
pkill -f weed
# rm -rf {./masters/m1,./masters/m2,./masters/m3,./volumes/v1,./volumes/v2,./volumes/v3,./volumes/v4,./volumes/v5,./volumes/v6,./filers/f1}
# mkdir {./masters/m1,./masters/m2,./masters/m3,./volumes/v1,./volumes/v2,./volumes/v3,./volumes/v4,./volumes/v5,./volumes/v6,./filers/f1}

nohup weed -v=3 master -ip=$LOCAL_IP -port=9333 -mdir=./masters/m1 -peers=$LOCAL_IP:9333 -defaultReplication=100 > m1.log 2>&1 &

nohup weed -v=3 volume -ip=$LOCAL_IP -port=7081 -dir=./volumes/v1 -mserver=$LOCAL_IP:9333 -dataCenter=$DC1 -max=100 > v1.log 2>&1 &

nohup weed -v=3 volume -ip=$LOCAL_IP -port=7082 -dir=./volumes/v2 -mserver=$LOCAL_IP:9333 -dataCenter=$DC1 -max=100 > v2.log 2>&1 &

nohup weed -v=3 volume -ip=$LOCAL_IP -port=7083 -dir=./volumes/v3 -mserver=$LOCAL_IP:9333 -dataCenter=$DC1 -max=100 > v3.log 2>&1 &

nohup weed -v=3 volume -ip=$LOCAL_IP -port=7084 -dir=./volumes/v4 -mserver=$LOCAL_IP:9333 -dataCenter=$DC2 -max=100 > v4.log 2>&1 &

nohup weed -v=3 volume -ip=$LOCAL_IP -port=7085 -dir=./volumes/v5 -mserver=$LOCAL_IP:9333 -dataCenter=$DC2 -max=100 > v5.log 2>&1 &

nohup weed -v=3 volume -ip=$LOCAL_IP -port=7086 -dir=./volumes/v6 -mserver=$LOCAL_IP:9333 -dataCenter=$DC2 -max=100 > v6.log 2>&1 &
```



