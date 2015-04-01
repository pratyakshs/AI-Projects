#!/bin/bash

# run this in the directory containing the file 'check'
# produces test0,...,test4 - validation data
# produces data0,...,data4 - training data

len=`wc -l check | awk '{print $1}'`
len_5=`echo $len / 5 | bc`
for i in {0..4}
do
 	s=`echo $len_5 \* $i + 1 | bc`
	s_1=`echo $s - 1 | bc`
	e=`echo $s + $len_5 - 1 | bc`
	e_1=`echo $e + 1 | bc`
 	sed -n $s,${e}p check > test${i}
	sed -n 1,${s_1}p check >> data${i}
	sed -n $e_1,${len}p check >> data${i}
done

