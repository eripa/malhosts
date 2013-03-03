#!/usr/bin/env bash

path=`dirname $0`

which java >& /dev/null 
if [ $? -ne 0 ] ; then
  echo "This tool requires java, please install java or make sure it is in your PATH before retrying. Exiting.."
  exit 1
fi

if [ `id -u` -ne 0 ] ; then
  echo "This script requires super user privileges. *Hint* sudo $0"
  exit 1
fi

args=''
for ((i=$#;i>0;i--)) ; do
 args="${args} ${1}"
 shift
done

java -jar $path/malhosts.jar $args

