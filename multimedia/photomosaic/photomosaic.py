import argparse
import matplotlib.pyplot
import os
import photomosaic
import skimage.io
import sys

class PhotoMosaic:
    def parse_args(self, args: any):
        parser = argparse.ArgumentParser(description="photomosaic")
        parser.add_argument('--image-source', required=True)
        parser.add_argument('--width', required=True, type=int)
        parser.add_argument('--length', required=True, type=int)
        parser.add_argument('--image_destination', required=False)
        parser.add_argument('--display', required=False, default=False, action='store_true')
        parser.add_argument('--use-color-tile', required=False, default=False, action='store_true')
        parser.add_argument('--color-tile-folderpath', required=False, default='color_tile/')
        parser.add_argument('--use-image-tile', required=False, default=False, action='store_true')
        parser.add_argument('--image-tile-folderpath', required=False)
        parsed_args = parser.parse_args(args)
        return parsed_args

    def load_image(self, filepath: str) -> any:
        print("Loading image", filepath)
        image = skimage.io.imread(filepath)
        print("Image loaded.")
        return image

    def create_color_pool(self, folderpath: str):
        print("Creating color pool")
        photomosaic.rainbow_of_squares(folderpath)        
        
    def create_pool(self, folderpath: str) -> any:
        pool = photomosaic.make_pool(folderpath + "*.png")
        return pool

    def create_mosaic(self, image, pool, n_width, n_length) -> any:
        mosaic = photomosaic.bassic_mosaic(image, pool, (n_width, n_length))
        return mosaic

    def save_mosaic(self, mosaic: any, filepath: str) -> any:
        skimage.io.imsave(filepath, mosaic)

    def display_mosaic(self, mosaic: any):
        matplotlib.pyplot.imshow(mosaic)

    def main(self, args: dict):
        parsed_args = self.parse_args(args)
        image = self.load_image(parsed_args.image_source)
        if parsed_args.use_color_tile:
            self.create_color_pool(parsed_args.color_tile_filepath)
            pool = self.create_pool()
        elif parsed_args.use_image_tile:
            pass
        else:
            print("unknown tile type")
            return
        mosaic = self.create_mosaic(image, pool, self.width, self.length)
        if parsed_args.image_destination:
            self.save_mosaic(mosaic, parsed_args.image_destination)
        if parsed_args.display:
            self.display_mosaic(mosaic)

if __name__ == '__main__':
    args = sys.argv[1:]
    args += ["--image-source", "/home/gbesancon/Downloads/2019-08-16b - Gallery/EleftheriaGilles-1.jpg", "--width", "30", "--length", "30", "--display", "--use-color-tile"]
    PhotoMosaic().main(args)