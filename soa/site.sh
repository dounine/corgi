#!/bin/bash

function copyFile() {
    module_a_path=$(echo $1 | awk -F "soa" '{printf "\t" $2}')
    module_path=$(dirname $(dirname $module_a_path))
	module=$(dirname $(dirname $1))
	crdir=$soa$module_path
	if [ ! -d $crdir ];then
        	mkdir -p $crdir
    	fi
	#rm -rf $crdir"/*"
	cp -rf $1/* $crdir"/"
}
function siteInit() {
    files=$(find $dir -name site | grep -v "soa/target/site")
    soa=$(find $dir -name site | grep "soa/target/site")
    for item in $files
    do
    	copyFile $item
    done
}
dir=$(cd `dirname $0` ; pwd)
cd $dir
if [ $# -eq 0 ];then
mvn clean
mvn package
mvn site
fi
siteInit
#say structure finish
jettypid=$(ps -ef | grep jetty:run | sed -n '1p' | awk '{printf $2}')
kill $jettypid
mvn jetty:run
