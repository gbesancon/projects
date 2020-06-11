import argparse
import matplotlib.pyplot
import numpy as np
import os
import photomosaic
import skimage.io
import sys
import tempfile
from tqdm import tqdm
import warnings

PNG_EXTENSION = '.png'

class PhotoMosaicCLI:
    """
    https://github.com/danielballan/photomosaic
    http://danielballan.github.io/photomosaic/docs/getting-tiles.html
    """

    def parse_args(self, args: any):
        parser = argparse.ArgumentParser(description="photomosaic")
        parser.add_argument('--image-file', required=True)
        parser.add_argument('--width', required=True, type=int)
        parser.add_argument('--length', required=True, type=int)
        parser.add_argument('--depth', required=False, type=int, default=0)
        parser.add_argument('--mosaic-file', required=False)
        parser.add_argument('--mosaic-scale', required=False, type=int, default=1)
        parser.add_argument('--display', required=False, default=False, action='store_true')
        parser.add_argument('--use-image-tile', required=False, default=False, action='store_true')
        parser.add_argument('--image-tile-folder', required=False)
        parser.add_argument('--image-tile-extension', required=False, default=PNG_EXTENSION)
        parser.add_argument('--image-tile-limit', required=False, type=int, default=1)
        parser.add_argument('--pool-cache-folder', required=False, default=None)
        parsed_args = parser.parse_args(args)
        return parsed_args

    def load_image(self, filepath: str) -> any:
        print("Loading image", filepath)
        image = skimage.io.imread(filepath)
        print("Image loaded.")
        return image

    def create_color_pool(self, folderpath: str):
        print("Creating color pool", folderpath)
        photomosaic.rainbow_of_squares(folderpath, (100,100))
        print("Color pool created")
        
    def create_pool(self, folderpath: str, file_extension: str) -> any:
        print("Preparing pool", folderpath)
        pool = photomosaic.make_pool(os.path.join(folderpath, "*" + file_extension))
        print("Pool prepared.")
        return pool

    def get_cached_pool_filepath(self, cache_folderpath: str, folderpath: str, file_extension: str) -> str:
        return os.path.join(
            cache_folderpath, 
            folderpath.replace(os.path.sep, "").replace(" ", "").replace(".", "-").replace("*", "") + "-" + 
            file_extension.replace(os.path.sep, "").replace(" ", "").replace(".", "").replace("*", "") + ".json"
            )

    def get_cached_pool(self, cache_folderpath: str, folderpath: str, file_extension: str) -> any:
        pool = None
        cached_pool_filepath = self.get_cached_pool_filepath(cache_folderpath, folderpath, file_extension)
        print("Retrieving cached pool", cached_pool_filepath)
        if os.path.exists(cached_pool_filepath):
            pool = photomosaic.import_pool(cached_pool_filepath)
        if pool:
            print("Pool retrieved.")
        else:
            print("Pool not found")
        return pool

    def cache_pool(self, cache_folderpath: str, folderpath: str, file_extension: str, pool: any):
        cached_pool_filepath = self.get_cached_pool_filepath(cache_folderpath, folderpath, file_extension)
        print("Caching the pool", cached_pool_filepath)
        if not os.path.exists(cache_folderpath):
            os.makedirs(cache_folderpath)
        photomosaic.export_pool(pool, cached_pool_filepath)
        print("Pool cached.")

    def make_mosaic(self, image, pool, grid_dims, *, mask=None, depth=0, scale=1, tile_limit=0):
        """
        Based on photomosaic.basic_mosaic
        """
        # Size the image to be evenly divisible by the tiles.
        image = skimage.img_as_float(image)
        image = photomosaic.rescale_commensurate(image, grid_dims, depth)
        if mask is not None:
            mask = photomosaic.rescale_commensurate(mask)

        # Use perceptually uniform colorspace for all analysis.
        converted_img = photomosaic.perceptual(image)

        # Adapt the color palette of the image to resemble the palette of the pool.
        adapted_img = photomosaic.adapt_to_pool(converted_img, pool)

        # Partition the image into tiles and characterize each one's color.
        tiles = photomosaic.partition(adapted_img, grid_dims=grid_dims, mask=mask, depth=depth)
        tile_colors = [np.mean(adapted_img[tile].reshape(-1, 3), 0)
                    for tile in tqdm(tiles, desc='analyzing tiles')]

        # Match a pool image to each tile.
        if tile_limit == 0:
            match = photomosaic.simple_matcher(pool)
        else:
            match = photomosaic.simple_matcher_unique(pool, limit=tile_limit)
        matches = [match(tc) for tc in tqdm(tile_colors, desc='matching')]

        # Draw the mosaic.
        canvas = np.ones_like(image)  # white canvas same shape as input image
        return photomosaic.draw_mosaic(canvas, tiles, matches, scale=scale)

    def create_mosaic(self, image, pool, n_width: int, n_length: int, depth: int, scale: int, tile_limit: int) -> any:
        print("Creating mosaic")
        mosaic = self.make_mosaic(image, pool, (n_length, n_width), depth=depth, scale=scale, tile_limit=tile_limit)
        print("Mosaic created.")
        return mosaic

    def save_mosaic(self, mosaic: any, filepath: str) -> any:
        print("Saving mosaic as", filepath)
        skimage.io.imsave(filepath, mosaic)
        print("Mosaic saved.")

    def display_mosaic(self, mosaic: any):
        print("Displaying mosaic")
        matplotlib.pyplot.imshow(mosaic)
        print("Mosaic displayed.")

    def main(self, args: dict):
        # Parse arguments
        parsed_args = self.parse_args(args)

        # Load image
        image = self.load_image(parsed_args.image_file)

        # Detect use of color tiles
        if not parsed_args.use_image_tile:
            # parsed_args.image_tile_folder = os.path.join(tempfile.gettempdir(), 'color_tiles')
            parsed_args.image_tile_folder = 'color_tiles'
            parsed_args.image_tile_extension = PNG_EXTENSION

        pool = None

        # Try to load pool from a cached object
        if parsed_args.pool_cache_folder:
            pool = self.get_cached_pool(parsed_args.pool_cache_folder, parsed_args.image_tile_folder, parsed_args.image_tile_extension)
        if pool is None:
            if not parsed_args.use_image_tile:
                self.create_color_pool(parsed_args.image_tile_folder)
            pool = self.create_pool(parsed_args.image_tile_folder, parsed_args.image_tile_extension)
            if parsed_args.pool_cache_folder:
                self.cache_pool(parsed_args.pool_cache_folder, parsed_args.image_tile_folder, parsed_args.image_tile_extension, pool)

        # Create the mosaic
        mosaic = self.create_mosaic(image, pool, parsed_args.width, parsed_args.length, parsed_args.depth, parsed_args.mosaic_scale, parsed_args.image_tile_limit)
        
        # Save mosaic in a file
        if parsed_args.mosaic_file:
            self.save_mosaic(mosaic, parsed_args.mosaic_file)

        # Displat mosaic on screen
        if parsed_args.display:
            self.display_mosaic(mosaic)

if __name__ == '__main__':
    args = sys.argv[1:]
    PhotoMosaicCLI().main(args)