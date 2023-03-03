#!/bin/bash
#*******************************************************************************
#
#  MODULE PROJET 2 - 2015, 2016 (c) Daniel Le Berre - Université d'Artois - tous droits réservés.
#   
#  Le code source de ce programme est distributé pour raison pédagogique
#  aux étudiants de troisième année de licence d'informatique de l'université d'Artois 2015.
#  Ces étudiants ont le droit de l'exécuter, l'étudier, le modifier pour leur usage personnel.
#  La redistribution de ce programme, sous quelle que forme que ce soit (source, binaire,
#  listing, etc) est strictement interdite. 
#  
#*******************************************************************************

set -e

. assert.sh

echo "Tests du comparateur d'images"

assert "./compare.sh TEST1/image1.png TEST1/image1.png" "OK\n0\n"
assert "./compare.sh TEST1/image2.png TEST1/image2.png" "OK\n0\n"
assert "./compare.sh TEST1/image1.png TEST1/image2.png" "OK\n879\n"
assert "cp diff.png localdiff.png"
assert "./compare.sh TEST1/diff.png localdiff.png" "OK\n0\n"

assert_end regression

echo "Tests de la bibliothèque mathématique"

assert "./checktriplet.sh \"P 1 1 1,add,P 2 2 2\"" "Interdit"
assert "./checktriplet.sh \"P 1 1 1,mul,P 2 2 2\"" "Interdit"
assert "./checktriplet.sh \"P 1 1 1,mul,2\"" "P 2.0 2.0 2.0"
assert "./checktriplet.sh \"P 1 1 1,sub,P 2 2 2\"" "V -1.0 -1.0 -1.0"
assert "./checktriplet.sh \"P 1 1 1,add,V 2 2 2\"" "P 3.0 3.0 3.0"
assert "./checktriplet.sh \"P 1 1 1,mul,V 2 2 2\"" "Interdit"
assert "./checktriplet.sh \"V 1 1 1,sub,V 2 2 2\"" "V -1.0 -1.0 -1.0"
assert "./checktriplet.sh \"V 1 1 1,dot,V 2 2 2\"" "6.0"
assert "./checktriplet.sh \"V 1 0 0,cross,V 0 1 0\"" "V 0.0 0.0 1.0"

assert_end regression

echo "Tests de la lecture des scènes"

assert "./checkscene.sh TEST3/test1.scene" "mascene.png\n307200\n1\n2\n"
assert "./checkscene.sh TEST3/test2.scene" "mascene.png\n307200\n1\n2\n"
assert "./checkscene.sh TEST3/test3.scene" "mascene.png\n307200\n3\n2\n"
assert "./checkscene.sh TEST3/test4.scene" "mascene.png\n307200\n6\n2\n"
assert "./checkscene.sh TEST3/test5.scene" "mascene.png\n307200\n1\n1\n"
assert "./checkscene.sh TEST3/test6.scene" "mascene.png\n786432\n4\n2\n"

assert_end regression

echo "Tests de la génération des images"

for testfile in `ls TEST4/*.test`
do
   imagefile=${testfile%.test}.png
   assert "./raytrace.sh $testfile" ""
   assert "./compare.sh $imagefile ${imagefile#TEST4/}" "OK\n0\n"
done

assert_end regression

# echo "Tests de la génération des images"

# for testfile in `ls TEST5/*.test`
# do
#    imagefile=${testfile%.test}.png
#    assert "./raytrace.sh $testfile" ""
#    assert "./compare.sh $imagefile ${imagefile#TEST5/}" "OK\n0\n"
# done

# for testfile in `ls TEST6/*.test`
# do
#    imagefile=${testfile%.test}.png
#    assert "./raytrace.sh $testfile" ""
#    assert "./compare.sh $imagefile ${imagefile#TEST6/}" "OK\n0\n"
# done

# for testfile in `ls TEST7/*.test`
# do
#    imagefile=${testfile%.test}.png
#    assert "./raytrace.sh $testfile" ""
#    assert "./compare.sh $imagefile ${imagefile#TEST6/}" "OK\n0\n"
# done

# assert_end regression

# echo "Tests des images mysteres (sans transformations)"

# for testfile in `ls MYSTERES/*.test`
# do
#    imagefile=${testfile%.test}.png
#    assert "./raytrace.sh $testfile" ""
#    assert "./compare.sh $imagefile ${imagefile#MYSTERES/}" "OK\n0\n"
# done

# assert_end regression
