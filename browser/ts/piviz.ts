'use strict';

import pi = require('./data/pi');

class PiViz {
    private pi: pi.Pi;

    constructor() {
        this.pi = new pi.Pi();
    }

    run() {
        this.pi.load();
    }
}

let piviz = new PiViz();
piviz.run();