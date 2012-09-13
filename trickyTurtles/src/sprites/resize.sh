#!/bin/sh
counter=1
root=karteklein
resolution=164x164
for i in `ls -1 ./*.gif`; do
	echo "Now working on $i"
	convert -resize $resolution $i ${root}_${counter}.gif
	counter=`expr $counter + 1`
done
