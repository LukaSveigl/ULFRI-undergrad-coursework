import { Vector } from './Vector.js';
import { Point } from './Point.js';

export class BoundingBox {
    constructor(center, width, height) {
        if (center instanceof Vector) {
            if (center.length() == 2) {
                this.center = center;
            }
            else {
                throw "Center length incorrect!";
            }
        }
        else {
            throw "Parameter center is not a vector!";
        }

        if (typeof width == "number") {
            this.width = width;
        }
        else {
            throw "Parameter width is not a number!";
        }

        if (typeof height == "number") {
            this.height = height;
        }
        else {
            throw "Parameter height is not a number!";
        }
    }

    contains(point) {
        if (point instanceof Point) {
            if ((point.x + (point.radius / 2) >= this.center.vec[0] - this.width) &&
                (point.y + (point.radius / 2) >= this.center.vec[1] - this.height) &&
                (point.x + (point.radius / 2) <= this.center.vec[0] + this.width) &&
                (point.y + (point.radius / 2) <= this.center.vec[1] + this.height)) {
                return true;
            }
            return false;
        }
        else {
            throw "Parameter point is not a vector!";
        }
    }

    intersects(box) {
        if (box instanceof BoundingBox) {
            if ((this.center.vec[0] < box.center.vec[0] + box.width) &&
                (this.center.vec[0] + this.width > box.center.vec[0]) &&
                (this.center.vec[1] < box.center.vec[1] + box.height) &&
                (this.center.vec[1] + this.height > box.center.vec[1])) {
                return true;
            }
            return false;
        }
        else {
            throw "Parameter box is not a bounding box!";
        }
    }
}