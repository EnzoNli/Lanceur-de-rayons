#!/bin/bash
IMAGE1=$1
IMAGE2=$2

# Pour connaître l'emplacement du script compare.sh
MYPATH=$(dirname "$0")

java -cp $MYPATH/bin comparateur.TestCompareImage $IMAGE1 $IMAGE2
