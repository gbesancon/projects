;;;; Image Process Gimp script
;;;;
;;;; To run this from the command prompt:
;;;; gimp -c -d -i -b '(script-fu-image-process "file.jpg" tw th "directory")' \
;;;; '(gimp-quit 0)'
;;
(define (script-fu-image-process filename t-width t-height directory)
(let* ((img 0) (drw 0) (height 0) (width 0) (heightImg 0) (widthImg 0) (fileparts (strbreakup filename ".")))
;; car needed here because gimp functions return values as lists
(set! img (car (file-jpeg-load 1 filename filename)))
;; set image resolution to 72dpi
(gimp-image-set-resolution img 72 72)
;; Récupere les cote de l'image
(set! heightImg (car (gimp-image-height img)))
(set! widthImg (car (gimp-image-width img)))
;; 
(if (< heightImg widthImg)
(begin
(set! height t-height)
(set! width t-width)
)
) 
(if (> heightImg widthImg)
(begin
(set! height t-width)
(set! width t-height)
)
)
;; create thumbnail image
(gimp-image-scale img width height)
;; also flatten image to reduce byte storage even further
(set! drw (car (gimp-image-flatten img)))
;; save the image
(file-jpeg-save 1 img drw
(string-append directory "/" (car fileparts) ".jpg")
(string-append directory "/" (car fileparts) ".jpg")
0.92 0 0 0 "Copyright Gilles Besançon, 2014" 0 1 0 1)))

(script-fu-register "script-fu-image-process"
"/Xtns/Script-Fu/Utils/Image Process..."
"Process Image"
"Mark Willson, Todd Wallentine"
"Mark Willson, Todd Wallentine"
"Dec 2003, Jun 2007"
""
SF-VALUE "Image Name" " "
SF-VALUE "Thumbnail Width" "800"
SF-VALUE "Thumbnail Height" "600")
