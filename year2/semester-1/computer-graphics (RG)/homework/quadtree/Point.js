import { Vector } from "./Vector.js";

export class Point {
    constructor(x, y) {
        if (typeof x == "number") {
            this.x = x;
        }
        else {
            throw "Parameter x is not a number!";
        }

        if (typeof y == "number") {
            this.y = y;
        }
        else {
            throw "Parameter y is not a number!";
        }

        this.radius = 10;
        this.velocity = 1.5;
        this.strokeWidth = 2;

        this.xDirection = Math.random() < 0.5 ? -1 : 1;
        this.yDirection = Math.random() < 0.5 ? -1 : 1;

        this.intersectFound = false;
    }

    intersects(point) {
        if (point instanceof Point) {
            const xDiff = this.x - point.x;
            const yDiff = this.y - point.y;

            const radSquare = Math.pow(this.radius, 2);

            return (Math.pow(xDiff, 2) + Math.pow(yDiff, 2) <= 4 * radSquare);
        }
        else {
            throw "Parameter point is not a point!";
        }
    }

    update(canvasWidth, canvasHeight) {
        //if (typeof canvasWidth == "number" && typeof canvasHeight == "number") {
        if (this.x >= canvasWidth - this.radius) {
            this.xDirection = -Math.abs(this.xDirection);
        }
        if (this.x <= this.radius) {
            this.xDirection = Math.abs(this.xDirection);
        }
        if (this.y >= canvasHeight - this.radius) {
            this.yDirection = -Math.abs(this.yDirection);
        }
        if (this.y <= this.radius) {
            this.yDirection = Math.abs(this.yDirection);
        }

        this.x += this.velocity * this.xDirection;
        this.y += this.velocity * this.yDirection;

        this.intersectFound = false;
        //}
        //else {
        //throw "One of the parameters (canvasWidth, canvasHeight) is not an number";
        //}
    }

    draw(ctx) {
        ctx.lineWidth = this.strokeWidth;

        ctx.beginPath();

        if (this.intersectFound) {
            ctx.strokeStyle = "blue";
        }
        else {
            ctx.strokeStyle = "black";
        }

        ctx.arc(this.x, this.y, this.radius, 0, 2 * Math.PI);
        ctx.stroke();
        ctx.closePath();
    }

    setIntersect(flag) {
        this.intersectFound = flag;
    }

    setSpeed(speed) {
        this.velocity = Number.parseFloat(speed);
    }
}