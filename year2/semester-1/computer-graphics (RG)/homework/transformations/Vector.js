export class Vector {

    constructor(x = 0, y = 0, z = 0) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    toString() {
        return `(${this.x}, ${this.y}, ${this.z})`;
    }

    toArray() {
        return [this.x, this.y, this.z];
    }

    negate() {
        return new Vector(this.x * -1, this.y * -1, this.z * -1);
    }

    add(v) {
        if (v instanceof Vector) { // Check if passed argument is an object of class Vector
            return new Vector(this.x + v.x, this.y + v.y, this.z + v.z);
        } else {
            console.log("Parameter of method add is not a vector!");
            return Vector(0, 0, 0);
        }
    }

    subtract(v) {
        if (v instanceof Vector) { // Check if passed argument is an object of class Vector
            return new Vector(this.x - v.x, this.y - v.y, this.z - v.z);
        } else {
            console.log("Parameter of method subtract is not a vector!");
            return Vector(0, 0, 0);
        }
    }

    multiply(v) {
        if (v instanceof Vector) { // Check if passed argument is an object of class Vector
            return new Vector(this.x * v.x, this.y * v.y, this.z * v.z);
        } else {
            console.log("Parameter of method multiply is not a vector!");
            return Vector(0, 0, 0);
        }
    }

    divide(v) {
        if (v instanceof Vector) { // Check if passed argument is an object of class Vector
            return new Vector(this.x / v.x, this.y / v.y, this.z / v.z);
        } else {
            console.log("Parameter of method divide is not a vector!");
            return Vector(0, 0, 0);
        }
    }

    dot(v) {
        if (v instanceof Vector) { // Check if passed argument is an object of class Vector
            return this.x * v.x + this.y * v.y + this.z * v.z;
        } else {
            console.log("Parameter of method dot is not a vector!");
            return 0.0;
        }
    }

    cross(v) {
        if (v instanceof Vector) { // Check if passed argument is an object of class Vector
            return new Vector(
                this.y * v.z - this.z * v.y,
                this.z * v.x - this.x * v.z,
                this.x * v.y - this.y * v.x
            );
        } else {
            console.log("Parameter of method cross is not a vector!");
            return Vector(0, 0, 0);
        }
    }

    length() {
        return Math.sqrt(this.x ** 2 + this.y ** 2 + this.z ** 2);
    }

    normalize() {
        let len = this.length();
        return new Vector(this.x / len, this.y / len, this.z / len)
    }

    project(v) {
        // Used equation: p = ((a dot b) / length(b)^2 ) * b 
        // a = this
        // b = v

        if (v instanceof Vector) { // Check if passed argument is an object of class Vector

            let dot_prod = this.dot(v);
            let divided = dot_prod / (v.length() ** 2);

            return new Vector(v.x * divided, v.y * divided, v.z * divided);
        } else {
            console.log("Parameter of method project is not a vector!");
            return Vector(0, 0, 0);
        }
    }

    reflect(v) {
        // Used equation: r = a - (2 * (a dot normalized b) * normalized b)

        if (v instanceof Vector) { // Check if passed argument is an object of class Vector
            let v_normalized = v.normalize();
            let dot_prod = this.dot(v_normalized);
            let v_modified = new Vector(2 * dot_prod * v_normalized.x, 2 * dot_prod * v_normalized.y, 2 * dot_prod * v_normalized.z);

            return this.subtract(v_modified);
        } else {
            console.log("Parameter of method reflect is not a vector!");
            return Vector(0, 0, 0);
        }
    }

    angle(v) {
        if (v instanceof Vector) { // Check if passed argument is an object of class Vector

            return Math.acos(this.dot(v) / (this.length() * v.length()));
        } else {
            console.log("Parameter of method angle is not a vector!");
            return Vector(0, 0, 0);
        }
    }

}
