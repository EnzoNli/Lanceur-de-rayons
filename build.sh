#!/bin/bash

# Pour connaître l'emplacement du script compare.sh
MYPATH=$(dirname "$0")

# crée un répertoire bin si il n'existe pas

if [ ! -d "$MYPATH/bin" ]; then
   mkdir $MYPATH/bin
fi

if [ ! -d "$MYPATH/veclib" ]; then
   mkdir $MYPATH/veclib
fi

# récupère les fichiers à compiler pour Java

find $MYPATH/src -name *.java -print >$MYPATH/tocompile

javac -d $MYPATH/bin @$MYPATH/tocompile
javac -d $MYPATH/veclib @$MYPATH/tocompile

jar cmf manifest.mf raytracer.jar -C $MYPATH/bin -C $MYPATH/veclib .
