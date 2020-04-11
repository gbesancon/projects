import { expect } from "chai";
import "mocha";
import { Pi } from "./pi";

describe("", () => {

    it("Integer part digit 0 value is 3", () => {
        const pi = new Pi();
        expect(pi.getIntegerPartDigit(0)).to.equal("3");
    });

    it("Fractional part digit 0 value is 1", () => {
        const pi = new Pi();
        expect(pi.getFractionalPartDigit(0)).to.equal("1");
    });

    it("Fractional part digit 1 value is 4", () => {
        const pi = new Pi();
        expect(pi.getFractionalPartDigit(1)).to.equal("4");
    });

});
