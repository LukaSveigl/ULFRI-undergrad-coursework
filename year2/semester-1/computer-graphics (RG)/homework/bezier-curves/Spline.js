import { Vector } from "./Vector.js";
import { Bernstein } from "./Bernstein.js";
import { Bezier } from './Bezier.js';

export class Spline {
    constructor(curves) {
        this.curves = new Array(curves.length);
        this.curves.fill(0).splice(0, curves.length, ...curves);
    }

    value(t) {
        // Method that returns value of spline at time t 

        // Domain of t is [a, b] with a being floor(t) and b being a + 1 
        // 1. Get the appropriate curve in spline (curve with a index)
        // 2. Fix domain of t to [0, 1]
        // 3. Get value of that bezier curve at t

        if (t < this.curves.length) {
            // Get lower and upper bound of domain
            let a = Math.floor(t);
            let b = a + 1;

            // Get appropriate curve
            let curve = this.curves[a];

            let fixed_t = t;

            // Fix domain of t to [0, 1], if t is full number it represents lower bound
            if (a != b) {
                fixed_t = (t - a) / (b - a);
            } else {
                fixed_t = 0;
            }

            return curve.value(fixed_t);

        } else {
            console.log("Value of t is out of bounds!");
            return null;
        }

    }

    derivative(t) {
        // Method that returns derivative value of spline at time t 

        // Domain of t is [a, b] with a being floor(t) and b being a + 1
        // 1. Get the appropriate curve in spline (curve with a index)
        // 2. Fix domain of t to [0, 1]
        // 3. Get derivative value of that bezier curve at t

        if (t < this.curves.length) {
            // Get lower and upper bound of domain
            let a = Math.floor(t);
            let b = a + 1;

            // Get appropriate curve
            let curve = this.curves[a];

            let fixed_t = t;

            // Fix domain of t to [0, 1], if t is full number it represents lower bound
            if (a != b) {
                fixed_t = (t - a) / (b - a);
            } else {
                fixed_t = 0;
            }

            return curve.derivative(fixed_t);

        } else {
            console.log("Value of t is out of bounds!");
            return null;
        }
    }

    makeContinuous() {
        // Method that makes spline continuous by moving neighbour points to their average

        // Array of new coords
        let new_coords = [];

        // Loop through all curves
        for (let i = 0; i < this.curves.length - 1; i++) {

            // Reset new coords
            new_coords = new Array(this.curves[0].points[0].vec.length);

            // Get length number of points
            let p_length = this.curves[i].points.length;

            // Loop through all coords in certain point
            for (let j = 0; j < this.curves[i].points[0].vec.length; j++) {

                // New coords are average of last point of current curve and first point of next curve
                new_coords[j] = (this.curves[i].points[p_length - 1].vec[j] + this.curves[i + 1].points[0].vec[j]) / 2;

            }

            // Set coords
            this.curves[i].points[p_length - 1].vec = new_coords;
            this.curves[i + 1].points[0].vec = new_coords;

        }
    }

    moveToDerivative(index, avg_derivative) {
        // Method to move points to their average derivative by using 
        // rules C'(1) = n(Pn-1 - Pn) and C'(0) = n(P1 - P0)

        // Get curves
        let curr_curve = this.curves[index];
        let next_curve = this.curves[index + 1];

        // Get curve lengths
        let cn = curr_curve.points.length;
        let nn = next_curve.points.length;

        // Get last point in current curve
        let p1_last = curr_curve.points[cn - 1];

        // Get first point in next curve
        let p2_first = next_curve.points[0];

        // Calculate Pn-1 = Pn - C'(1)/n, where C'(1) is average derivative
        let new_p1 = p1_last.sub(avg_derivative.divScalar(cn - 1));

        // Calculate P1 = C'(0)/n + P0, where C'(0) is average derivative
        let new_p2 = avg_derivative.divScalar(nn - 1).add(p2_first);

        // Set points
        this.curves[index].points[cn - 2] = new_p1;
        this.curves[index + 1].points[1] = new_p2;
    }

    makeSmooth() {
        // Method that makes spline smooth by adjusting endpoint derivatives to their average

        // Make this spline continuous
        this.makeContinuous();

        // Loop through all curves
        for (let i = 0; i < this.curves.length - 1; i++) {

            // Get derivative at end of first curve
            let d1 = this.curves[i].derivative(1);

            // Get derivative at start of next curve
            let d2 = this.curves[i + 1].derivative(0);

            // Create new vector to hold average
            let avg = new Vector(new Array(d1.length()));

            // Calculate average for each coordinate
            for (let j = 0; j < d1.length(); j++) {
                avg.vec[j] = (d1.vec[j] + d2.vec[j]) / 2;
            }

            // If derivatives aren't at average already
            if (!d1.equal(d2)) {
                // Move curves to correct point
                this.moveToDerivative(i, avg);
            }
        }
    }
}