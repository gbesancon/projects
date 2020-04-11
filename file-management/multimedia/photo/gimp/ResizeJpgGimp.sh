#!/bin/bash
# SYNOPSIS
# thumbs.sh [prefix]
#
# DESCRIPTION
# Script to produce ‘thumbnails’ and images for use with expose
#
# thumbs/00000 .jpg = thumbnail (currently set to 800x600)
# 00000 .jpg = original sized image - just moved to the new name
#
#
# MODIFICATION HISTORY
# Mnemonic Rel Date Who
# PRJPG 1.0 01Feb04 mpw
# thumbs 1.1 18Jun07 tcw
# Written.
#

# Remove any existing processed jpgs
directory="thumbs"

rm -rf ${directory}
mkdir ${directory}

# Process files via the Gimp
for x in *.jpg
do
echo "..processing $x"
nice -n 19 gimp -c -d -i -b "(script-fu-image-process \"$x\" 800 600 \"${directory}\")" -b "(gimp-quit 0)"
done
