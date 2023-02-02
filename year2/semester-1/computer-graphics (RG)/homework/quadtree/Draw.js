import { QuadTree } from "./QuadTree.js";
import { BoundingBox } from "./BoundingBox.js";
import { Point } from "./Point.js";
import { Vector } from "./Vector.js";

export class Draw {
    constructor(ctx) {
        if (ctx instanceof CanvasRenderingContext2D) {
            this.ctx = ctx;
        }
        else {
            throw "Parameter ctx is not a canvas 2d rendering context!";
        }

        this.quadTree = new QuadTree(new BoundingBox(new Vector([0, 0]), this.ctx.canvas.width, this.ctx.canvas.height));

        this.points = new Array();

        this.limit = 50;

        this.drawGrid = true;

        this.currCapacity = this.quadTree.CAPACITY;
        this.currPointSpeed = 1.5;
    }


    draw() {
        this.draw = this.draw.bind(this);

        this.ctx.clearRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);

        this.spawnPoints();

        this.update();

        if (this.drawGrid) {
            this.drawQuadTree();
        }

        this.drawPoints();

        requestAnimationFrame(this.draw);
    }

    drawPoints() {
        for (let i = 0; i < this.points.length; i++) {
            this.points[i].draw(this.ctx);
        }
    }

    drawQuadTree() {
        this.quadTree.draw(this.ctx);
    }

    update() {
        this.quadTree = new QuadTree(new BoundingBox(new Vector([0, 0]), this.ctx.canvas.width, this.ctx.canvas.height));

        this.quadTree.setCapacity(this.currCapacity);

        for (const p of this.points) {
            this.quadTree.insert(p);
            p.update(this.ctx.canvas.width, this.ctx.canvas.height);
        }

        for (let i = 0; i < this.points.length; i++) {
            for (let j = 0; j < this.points.length; j++) {
                if (i != j && this.points[i].intersects(this.points[j])) {
                    this.points[i].setIntersect(true);
                    this.points[j].setIntersect(true);
                }
            }
        }
    }

    setDrawGrid(drawGrid) {
        if (typeof drawGrid == "boolean") {
            this.drawGrid = drawGrid;
        }
        else {
            throw "Parameter drawGrid is not a boolean!";
        }
    }

    setLimit(limit) {
        this.limit = Number.parseInt(limit);
    }

    spawnPoints() {
        if (this.points.length < this.limit) {
            for (let i = 0; i < this.limit - this.points.length; i++) {
                this.points.push(new Point(Math.random() * this.ctx.canvas.width, Math.random() * this.ctx.canvas.height));
            }
        }
        else if (this.points.length > this.limit) {
            this.points.splice(this.limit - 1, this.points.length - this.limit);
            this.quadTree.points.splice(this.limit - 1, this.quadTree.points.length - this.limit);
        }
    }

    setQuadDimensions(width, height) {
        this.quadTree.setBoundDimensions(width, height);
    }

    setQuadCapacity(capacity) {
        this.currCapacity = capacity;
    }

    setPointSpeed(speed) {
        this.currPointSpeed = speed;

        for (let i = 0; i < this.points.length; i++) {
            this.points[i].setSpeed(this.currPointSpeed);
        }
    }
}
