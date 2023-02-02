import { Transformation } from './Transformation.js'
import { PointManager } from './PointManager.js'
import { Vector } from './Vector.js'

export class TransformPoints {

    constructor() {
        this.point_manager = new PointManager();
        this.transformation = new Transformation();
    }

    transformPoints(input) {
        let points = this.point_manager.parsePoints(input); // Get points into array of vectors

        this.transformation.translate(new Vector(2.8, 0, 0)); // Apply needed transformations
        this.transformation.rotateY(Math.PI / 4);             // to transformation matrix
        this.transformation.translate(new Vector(0, 0, 7.15));
        this.transformation.translate(new Vector(0, 2.45, 0));
        this.transformation.scale(new Vector(1.8, 1.8, 1));
        this.transformation.rotateY(5 * (Math.PI / 10));

        for (let i = 0; i < points.length; i++) {
            points[i] = this.transformation.transform(points[i]); // Apply transformations to all vectors of points
        }

        return this.point_manager.formatPoints(points);
    }
}