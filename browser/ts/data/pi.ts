"use strict";

export class Pi {
    private files: string[] = [];

    constructor() {
        for (let i = 1; i <= 1000; i++) {
            this.files.push("pi-billion.txt.sf-part" + this.padd(i, 4));
        }
    }

    public load() {
        for (let i = 1; i <= 1000; i++) {
            console.log(this.files[i]);
        }
    }

    public padd(num: number, length: number) {
        let r = "" + num;
        while (r.length < length) {
            r = "0" + r;
        }
        return r;
    }
}
