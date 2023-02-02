import { Vector } from './Vector.js';

export class Matrix {

    constructor(...m) {
        this.m = new Array(16);
        this.m.fill(0).splice(0, m.length, ...m);
    }

    toArray() {
        return [...this.m];
    }

    toString() {
        return `(${this.m.join(',')})`;
    }

    negate() {
        return new Matrix(...this.m.map(function (x) { return x * -1; }));
    }

    add(m) {
        if (m instanceof Matrix) { // Check if passed argument is an object of class Matrix
            let m_tmp = [...m.m];

            for (let i = 0; i < this.m.length; i++) {
                m_tmp[i] += this.m[i];
            }

            return new Matrix(...m_tmp);
        } else {
            console.log("Parameter of method add is not a matrix!")
            return new Matrix(...new Array(16).fill(0));
        }
    }

    subtract(m) {
        if (m instanceof Matrix) { // Check if passed argument is an object of class Matrix
            let m_tmp = [...m.m];

            for (let i = 0; i < this.m.length; i++) {
                m_tmp[i] = this.m[i] - m_tmp[i];
            }

            return new Matrix(...m_tmp);
        } else {
            console.log("Parameter of method subtract is not a matrix!")
            return new Matrix(...new Array(16).fill(0));
        }
    }

    transpose() {
        let new_m = Array(this.m.length);

        for (let i = 0; i < 4; i++) {
            for (let j = 0; j < 4; j++) {
                let org_index = j + (i * 4);
                let new_index = i + (j * 4);

                new_m[new_index] = this.m[org_index];
            }
        }
        return new Matrix(...new_m);
    }

    multiply(m) {
        if (m instanceof Matrix) { // Check if passed argument is an object of class Matrix
            let new_m = Array(this.m.length);
            let element = 0;
            let offset_columns = 0;
            let offset_rows = 0;

            for (let i = 0; i < this.m.length; i++) { // Go through every space in new matrix

                if (i % 4 == 0 && i != 0) {
                    offset_columns += 4;
                    offset_rows = 0;
                }

                for (let j = 0, k = 0; j < 4 && k < 16; j++, k += 4) {
                    element += this.m[j + offset_columns] * m.m[k + offset_rows];
                }
                offset_rows += 1;

                if (offset_rows % 4 == 0) {
                    offset_rows = 0;
                }

                new_m[i] = element;
                element = 0;
            }

            return new Matrix(...new_m);

        } else {
            console.log("Parameter of method multiply is not a matrix!")
            return new Matrix(...new Array(16).fill(0));
        }
    }

    multiplyVector(v) {
        if (v instanceof Vector) {
            let vector_arr = v.toArray();
            vector_arr.push(1); // Create array with 4 homogenous coordinates
            let new_vector = new Array(4);

            let element = 0;

            let offset = 0;

            for (let i = 0; i < 4; i++) {
                for (let j = 0; j < 4; j++) {
                    element += this.m[j + offset] * vector_arr[j];
                }
                new_vector[i] = element;
                element = 0;
                offset += 4;
            }

            for (let i = 0; i < new_vector.length; i++) { // Homogenization
                new_vector[i] /= new_vector[new_vector.length - 1];
            }

            return new Vector(new_vector[0], new_vector[1], new_vector[2]);

        } else {
            console.log("Parameter of method multiplyVector is not a vector!");
            return Vector(0, 0, 0);
        }
    }
}
