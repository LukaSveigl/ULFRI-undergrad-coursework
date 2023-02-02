import { BoundingBox } from "./BoundingBox.js";
import { Vector } from "./Vector.js";
import { Point } from "./Point.js";

export class QuadTree {
    constructor(bound) {
        if (bound instanceof BoundingBox) {
            this.bound = bound;
        }
        else {
            throw "Parameter bound is not a bounding box!";
        }

        this.CAPACITY = 3;

        this.points = new Array();

        this.topLeft = null;
        this.topRight = null;
        this.botLeft = null;
        this.botRight = null;
    }

    update() {
        if (this.points.length < this.CAPACITY) {
            this.topLeft = null;
            this.topRight = null;
            this.botLeft = null;
            this.botRight = null;
        }
        else {
            if (this.topLeft != null) {
                this.topLeft.update();
            }
            if (this.topRight != null) {
                this.topRight.update();
            }
            if (this.botLeft != null) {
                this.botLeft.update();
            }
            if (this.botRight != null) {
                this.botRight.update();
            }
        }
    }

    insert(point) {
        if (point instanceof Point) {
            // Check if point belongs in this quad tree
            if (this.bound.contains(point)) {
                // If this quad tree has space and has no sub divisions
                if (this.points.length < this.CAPACITY && this.topLeft == null) {
                    this.points.push(point);
                    return true;
                }
                else {
                    if (this.topLeft == null) {
                        this.subdivide();
                    }

                    if (this.topLeft.insert(point)) {
                        return true;
                    }
                    if (this.topRight.insert(point)) {
                        return true;
                    }
                    if (this.botLeft.insert(point)) {
                        return true;
                    }
                    if (this.botRight.insert(point)) {
                        return true;
                    }
                }
            }
            else {
                return false;
            }
        }
        else {
            throw "Parameter point is not a vector!";
        }
    }

    subdivide() {
        let newHW = this.bound.width / 2;
        let newHH = this.bound.height / 2;

        const currentX = this.bound.center.vec[0];
        const currentY = this.bound.center.vec[1];

        this.topLeft = new QuadTree(new BoundingBox(new Vector([currentX, currentY]), newHW, newHH));
        this.topRight = new QuadTree(new BoundingBox(new Vector([currentX + newHW, currentY]), newHW, newHH));
        this.botLeft = new QuadTree(new BoundingBox(new Vector([currentX, currentY + newHH]), newHW, newHH));
        this.botRight = new QuadTree(new BoundingBox(new Vector([currentX + newHW, currentY + newHH]), newHW, newHH));

        this.topLeft.setCapacity(this.CAPACITY);
        this.topRight.setCapacity(this.CAPACITY);
        this.botLeft.setCapacity(this.CAPACITY);
        this.botRight.setCapacity(this.CAPACITY);
    }

    findInRange(range) {
        if (range instanceof BoundingBox) {
            let points = new Array();

            if (this.bound.intersects(range)) {
                for (let i = 0; i < this.points.length; i++) {
                    if (range.contains(this.points[i])) {
                        points.push(points[i]);
                    }
                }

                if (this.topLeft == null) {
                    return points;
                }

                points = points.concat(this.topLeft.findInRange(range));
                points = points.concat(this.topRight.findInRange(range));
                points = points.concat(this.botLeft.findInRange(range));
                points = points.concat(this.botRight.findInRange(range));
            }

            return points;
        }
        else {
            throw "Parameter range is not a bounding box!";
        }
    }

    draw(ctx) {
        if (ctx instanceof CanvasRenderingContext2D) {
            ctx.lineWidth = 1;

            if (this.topLeft != null) {
                this.topLeft.draw(ctx);
            }
            if (this.topRight != null) {
                this.topRight.draw(ctx);
            }
            if (this.botLeft != null) {
                this.botLeft.draw(ctx);
            }
            if (this.botRight != null) {
                this.botRight.draw(ctx);
            }

            ctx.strokeStyle = "black";
            ctx.width = 10;
            ctx.beginPath();
            ctx.rect(this.bound.center.vec[0], this.bound.center.vec[1], this.bound.width, this.bound.height);
            ctx.stroke();
        }
        else {
            throw "Parameter ctx is not a canvas 2d rendering context!";
        }
    }

    setBoundDimensions(width, height) {
        this.bound.width = width;
        this.bound.height = height;

        if (this.topLeft != null) {
            this.topLeft.setBoundDimensions(width / 2, height / 2);
        }
        if (this.topRight != null) {
            this.topRight.setBoundDimensions(width / 2, height / 2);
        }
        if (this.botLeft != null) {
            this.botLeft.setBoundDimensions(width / 2, height / 2);
        }
        if (this.botRight != null) {
            this.botRight.setBoundDimensions(width / 2, height / 2);
        }
    }

    setCapacity(capacity) {
        this.CAPACITY = capacity;

        if (this.topLeft != null) {
            this.topLeft.setCapacity(capacity);
        }
        if (this.topRight != null) {
            this.topRight.setCapacity(capacity);
        }
        if (this.botLeft != null) {
            this.botLeft.setCapacity(capacity);
        }
        if (this.botRight != null) {
            this.botRight.setCapacity(capacity);
        }
    }
}