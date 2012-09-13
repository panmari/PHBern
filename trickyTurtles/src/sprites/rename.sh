#!/bin/sh
counter=0
root=crd
resolution=164x164
for i in `ls -1 ./crd*.gif`; do
	echo "Now working on $i"
	mv $i ${root}${counter}.gif
	counter=`expr $counter + 1`
done
