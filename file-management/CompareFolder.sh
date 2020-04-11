#!/bin/sh
cd $1
for i in `ls -1 *`; do
firstFile=$i
secondFile=../$2/$i
echo “Comparing $i”
diff ${firstFile} ${secondFile};
done
cd ..
