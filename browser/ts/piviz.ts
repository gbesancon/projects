"use strict";

import pi = require("./data/pi");

class PiViz {
    private pi: pi.Pi;

    constructor() {
        this.pi = new pi.Pi();
    }

    public run() {
        this.pi.load();
    }
}

const piviz = new PiViz();
piviz.run();
