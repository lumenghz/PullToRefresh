#!/usr/bin/env bash
echo "Enter your msg below if you have:"
read commitMsg

git add .
time=`date "+%F %T"`

if [ "$commitMsg" ]
then
	git commit -m "$commitMsg"
else
	git commit -m "jakeruman commit at $time"
fi

git push origin master
