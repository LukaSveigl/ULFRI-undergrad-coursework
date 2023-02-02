export class Bernstein {
    constructor(n, k) {
        this.n = n;
        this.k = k;
    }

    factorial(x) {
        // Method that calculates factorial of x

        let res = 1;

        for (let i = x; i > 0; i--) {
            res *= i;
        }

        return res;
    }

    value(x) {
        // Method that calculates value of Bernstein polynomial at value x
        // Used equation: b = bin(n k) * x^k * (1 - x)^(n - k)

        let binomial = this.factorial(this.n) / (this.factorial(this.k) * this.factorial(this.n - this.k));

        if (this.k > this.n || this.k < 0) {
            return 0;
        }

        return binomial * Math.pow(x, this.k) * Math.pow(1 - x, this.n - this.k);
    }

    derivative(x) {
        // Method that calculates derivative of Bernstein polynomial at value x
        // Used equation: b' = n * (b(k - 1, n - 1, x) - b(k, n - 1, x))

        let b1 = new Bernstein(this.n - 1, this.k - 1);
        let b2 = new Bernstein(this.n - 1, this.k);

        return this.n * (b1.value(x) - b2.value(x));
    }
}