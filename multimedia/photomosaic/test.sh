#!/bin/bash
set -e
set -x

IMAGE_FILEPATH="/mnt/c/Users/GBesancon/Desktop/2019-08-16b - Gallery/EleftheriaGilles-105.jpg"
TILES_FOLDERPATH="/mnt/c/Users/GBesancon/Desktop/2019-08-16b - Gallery"
TILES_EXTENSION=".jpg"
MOSAIC_FILEPATH="${PWD}/EleftheriaGilles.jpg"

for n in 10 20 30 40 50 60 70 80 90 100
do
    for depth in 0 1 2 3 4 5
    do
        time python3 photomosaic-cli.py \
            --image-file "${IMAGE_FILEPATH}" \
            --width $n --length $n --depth $depth \
            --mosaic-file "${MOSAIC_FILEPATH}-${n}x${n}x${depth}-colors.png" \
            --pool-cache-folder "cache/"
        time python3 photomosaic-cli.py \
            --image-file "${IMAGE_FILEPATH}" \
            --width $n --length $n --depth $depth \
            --use-image-tile --image-tile-folder "${TILES_FOLDERPATH}" --image-tile-extension "${TILES_EXTENSION}" \
            --mosaic-file "${MOSAIC_FILEPATH}-${n}x${n}x${depth}-images.png" \
            --pool-cache-folder "cache/"
    done
done