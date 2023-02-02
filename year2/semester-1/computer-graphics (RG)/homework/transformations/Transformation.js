import { Matrix } from './Matrix.js';
import { Vector } from './Vector.js';

export class Transformation {

    constructor() {
        let elements = new Array(16); // Create an array of elements to pass to new matrix
        elements.fill(0);

        for (let i = 0; i < elements.length; i += 5) { // Transform elements into an identity (diag. elem. are 1, vector doesn't change)
            elements[i] = 1;
        }

        this.m = new Matrix(...elements);
    }

    toArray() {
        return this.m.toArray();
    }

    toString() {
        return this.m.toString();
    }

    translate(v) {
        if (v instanceof Vector) {
            let translation_matrix = new Matrix(
                1, 0, 0, v.x,
                0, 1, 0, v.y,
                0, 0, 1, v.z,
                0, 0, 0, 1
            );

            this.m = translation_matrix.multiply(this.m);
        } else {
            console.log("Parameter of method translate is not a vector!");
        }
    }

    scale(v) {
        if (v instanceof Vector) {
            let scaling_matrix = new Matrix(
                v.x, 0, 0, 0,
                0, v.y, 0, 0,
                0, 0, v.z, 0,
                0, 0, 0, 1
            );

            this.m = scaling_matrix.multiply(this.m);
        } else {
            console.log("Parameter of method scale is not a vector!");
        }
    }

    rotateX(angle) {
        let rotational_matrix = new Matrix(
            1, 0, 0, 0,
            0, Math.cos(angle), -Math.sin(angle), 0,
            0, Math.sin(angle), Math.cos(angle), 0,
            0, 0, 0, 1
        );

        this.m = rotational_matrix.multiply(this.m);
    }

    rotateY(angle) {
        let rotational_matrix = new Matrix(
            Math.cos(angle), 0, Math.sin(angle), 0,
            0, 1, 0, 0,
            -Math.sin(angle), 0, Math.cos(angle), 0,
            0, 0, 0, 1
        );

        this.m = rotational_matrix.multiply(this.m);
    }

    rotateZ(angle) {
        let rotational_matrix = new Matrix(
            Math.cos(angle), -Math.sin(angle), 0, 0,
            Math.sin(angle), Math.cos(angle), 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        );

        this.m = rotational_matrix.multiply(this.m);
    }

    transform(v) {
        if (v instanceof Vector) {
            return this.m.multiplyVector(v);
        } else {
            console.log("Parameter of method transform is not a vector!");
            return new Vector(0, 0, 0);
        }
    }
}