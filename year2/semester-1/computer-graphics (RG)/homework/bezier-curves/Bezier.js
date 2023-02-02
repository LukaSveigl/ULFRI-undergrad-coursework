import { Bernstein } from "./Bernstein.js";
import { Vector } from "./Vector.js";

export class Bezier {
    constructor(points) {
        this.points = new Array(points.length);
        this.points.fill(0).splice(0, points.length, ...points);
    }


    factorial(x) {
        // Method that calculates factorial of x

        let res = 1;

        for (let i = x; i > 0; i--) {
            res *= i;
        }

        return res;
    }

    value(t) {
        // Method that returns value of Bezier curve at time t
        // Used equation: C(t) = sum(B(k, n, t) * Pk)

        let arr = new Array(this.points[0].length());
        arr.fill(0);

        let C = new Vector(arr);

        let bernstein = new Bernstein(this.points.length, 0);

        for (let i = 0; i < this.points.length; i++) {
            // Create new bernstein polynomial
            bernstein = new Bernstein(this.points.length - 1, i);

            // Calculate new value for each vector coordinate
            for (let j = 0; j < C.vec.length; j++) {
                C.vec[j] += bernstein.value(t) * this.points[i].vec[j];
            }
        }

        return C;
    }

    derivative(t) {
        // Method that returns derivative of Bezier curve at time t
        // Used equation: C' = sum(b(n - 1, i, t) * n * (Pi+1 - Pi))

        let arr = new Array(this.points[0].length());
        arr.fill(0);

        let Cd = new Vector(arr);

        //let Q = new Vector(arr);

        let bernstein = new Bernstein(this.points.length, 0);

        let n = this.points.length - 1;

        for (let i = 0; i < this.points.length - 1; i++) {
            // Create new bernstein polynomial
            bernstein = new Bernstein(this.points.length - 2, i);

            // Q = n(Pi+1 - Pi)
            let Q = this.points[i + 1].sub(this.points[i]).mulScalar(n);

            // Calculate new value for each vector coordinate
            for (let j = 0; j < Cd.vec.length; j++) {
                Cd.vec[j] += bernstein.value(t) * Q.vec[j];
            }
        }

        return Cd;
    }
}