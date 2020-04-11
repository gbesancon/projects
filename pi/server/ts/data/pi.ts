"use strict";

export class Pi {
    private files: string[] = [];

    constructor() {
        for (let i = 1; i <= 1000; i++) {
            this.files.push(this.getFilename(i));
        }
    }

    public getDigit(index: number) {
        if (index > 0) {
            return this.getFractionalPartDigit(index);
        } else {
            if (index < 0) {
                return this.getIntegerPartDigit(-index);
            } else {
                throw new RangeError("No digit at position " + index + ".");
            }
        }
    }

    public getDigits(startIndex: number, endIndex: number) {
        if (startIndex > 0 && endIndex > 0) {
            if (startIndex < endIndex) {
                return this.getFractionalPartDigits(startIndex, endIndex);
            } else {
                throw new RangeError("Range [" + startIndex + "-" + endIndex + "] is inconsistent.");
            }
        } else {
            throw new RangeError("Start and end indices must be positive and not null.");
        }
    }

    public getIntegerPartDigit(index: number) {
        //
    }

    public getIntegerPartDigits(startIndex: number, endIndex: number) {
        //
    }

    public getFractionalPartDigit(index: number) {
        //
    }

    public getFractionalPartDigits(startIndex: number, endIndex: number) {
        //
    }

    private load() {
        for (let i = 1; i <= 1000; i++) {
            console.log(this.files[i]);
        }
    }

    private getFilename(index: number) {
        return "pi-billion.txt.sf-part" + this.padd(index, 4);
    }

    private padd(num: number, length: number) {
        let r = "" + num;
        while (r.length < length) {
            r = "0" + r;
        }
        return r;
    }
}
