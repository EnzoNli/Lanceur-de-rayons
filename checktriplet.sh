#!/bin/bash

# Pour connaître l'emplacement du script compare.sh
MYPATH=$(dirname "$0")

java -Dapple.awt.UIElement=true -cp $MYPATH/veclib Check "$1"

echo "Pas encore réalisé"

