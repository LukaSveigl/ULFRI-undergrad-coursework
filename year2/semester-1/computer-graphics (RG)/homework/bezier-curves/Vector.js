export class Vector {

    constructor(coordinates) {
        this.vec = new Array(coordinates.length);
        this.vec.fill(0).splice(0, coordinates.length, ...coordinates);
    }

    toString() {
        let str = '(';
        for (let i = 0; i < this.vec.length; i++) {
            str += this.vec[i] + ", ";
        }
        str = str.substring(0, str.length - 2);
        str += ')';

        return str;
    }

    toArray() {
        return this.vec;
    }

    length() {
        return this.vec.length;
    }

    add(v) {
        if (v instanceof Vector) { // Check if passed argument is an object of class Vector
            if (v.length() == this.vec.length) { // Check if vector is correct length
                let v_arr = new Array(v.length());

                for (let i = 0; i < v.length(); i++) {
                    v_arr[i] = this.vec[i] + v.vec[i];
                }

                return new Vector(v_arr);
            } else {
                console.log("Vectors not same length!");
                return null;
            }
        } else {
            console.log("Parameter of method add is not a vector!");
            return null;
        }
    }

    sub(v) {
        if (v instanceof Vector) { // Check if passed argument is an object of class Vector
            if (v.length() == this.vec.length) { // Check if vector is correct length
                let v_arr = new Array(v.length());

                for (let i = 0; i < v.length(); i++) {
                    v_arr[i] = this.vec[i] - v.vec[i];
                }

                return new Vector(v_arr);
            } else {
                console.log("Vectors not same length!");
                return null;
            }
        } else {
            console.log("Parameter of method add is not a vector!");
            return null;
        }
    }

    mul(v) {
        if (v instanceof Vector) { // Check if passed argument is an object of class Vector
            if (v.length() == this.vec.length) { // Check if vector is correct length
                let v_arr = new Array(v.length());

                for (let i = 0; i < v.length(); i++) {
                    v_arr[i] = this.vec[i] * v.vec[i];
                }

                return new Vector(v_arr);
            } else {
                console.log("Vectors not same length!");
                return null;
            }
        } else {
            console.log("Parameter of method add is not a vector!");
            return null;
        }
    }

    div(v) {
        if (v instanceof Vector) { // Check if passed argument is an object of class Vector
            if (v.length() == this.vec.length) { // Check if vector is correct length
                let v_arr = new Array(v.length());

                for (let i = 0; i < v.length(); i++) {
                    v_arr[i] = this.vec[i] / v.vec[i];
                }

                return new Vector(v_arr);
            } else {
                console.log("Vectors not same length!");
                return null;
            }
        } else {
            console.log("Parameter of method add is not a vector!");
            return null;
        }
    }

    mulScalar(s) {
        let v_arr = new Array(this.vec.length);

        for (let i = 0; i < v_arr.length; i++) {
            v_arr[i] = this.vec[i] * s;
        }

        return new Vector(v_arr);
    }

    divScalar(s) {
        let v_arr = new Array(this.vec.length);

        for (let i = 0; i < v_arr.length; i++) {
            v_arr[i] = this.vec[i] / s;
        }

        return new Vector(v_arr);
    }

    equal(v) {
        if (v instanceof Vector) { // Check if passed argument is an object of class Vector
            if (v.length() == this.vec.length) { // Check if vector is correct length

                for (let i = 0; i < v.length(); i++) {
                    if (this.vec[i] != v.vec[i]) {
                        return false;
                    }
                }

                return true;
            } else {
                console.log("Vectors not same length!");
                return false;
            }
        } else {
            console.log("Parameter of method equal is not a vector!");
            return false;
        }
    }
}
