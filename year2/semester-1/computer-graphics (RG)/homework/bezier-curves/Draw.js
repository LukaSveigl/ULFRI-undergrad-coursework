import { Vector } from "./Vector.js";
import { Bernstein } from "./Bernstein.js";
import { Bezier } from './Bezier.js';
import { Spline } from './Spline.js';

export class Draw {
    constructor() {
        // Get canvas from document
        this.canvas = document.getElementById("canvas");

        // Get drawing context from canvas
        this.ctx = this.canvas.getContext("2d");

        // Get dimensions of canvas
        this.width = canvas.width;
        this.height = canvas.height;

        // Internal array of splines
        this.splines = new Array();

        // Internal array of colors
        this.colors = new Array();

        // Index of selected spline
        this.selected_spline = 0;
    }


    // Methods that modify internal arrays/values

    addSpline() {
        // Method that adds new empty spline, adds new color of spline
        // and adds option to select element

        // Add new empty spline
        this.splines.push(new Spline([]));

        // Add color of spline to array
        this.colors.push(document.getElementById("splineColor").value);

        // Newly created spline becomes selected spline
        this.selected_spline = this.splines.length - 1;

        // Create new option for select element
        let option = document.createElement("option");

        // Set text and value of option
        option.text = "Spline " + (this.selected_spline + 1);
        option.value = this.selected_spline;

        // Add option of newly created spline to select element
        document.getElementById("selectSpline").options.add(option);

        // Set value of select to newly created spline
        document.getElementById("selectSpline").value = this.selected_spline;
    }

    addCurve() {
        // Method that adds new Bezier curve to selected spline

        // Add new empty curve to selected spline
        this.splines[this.selected_spline].curves.push(new Bezier([]));
    }

    addPoint(p) {
        // Method that adds single point to last curve of selected spline

        // If no splines in canvas
        if (this.splines.length == 0) {
            // Add empty spline
            this.addSpline();
        }

        // If no curves in spline
        if (this.splines[this.selected_spline].curves.length == 0) {
            // Add new curve
            this.addCurve();
        }

        let spline_length = this.splines[this.selected_spline].curves.length;

        // If last curve in spline is full
        if (this.splines[this.selected_spline].curves[spline_length - 1].points.length >= 4) {
            // Make spline smooth
            this.splines[this.selected_spline].makeSmooth();

            // Add new curve
            this.addCurve();
        }

        // If not adding to first curve
        if (this.splines[this.selected_spline].curves.length > 1) {
            // Get number of curves in spline
            let num_of_curves = this.splines[this.selected_spline].curves.length;

            // Get number of points in previous curve
            let num_of_points = this.splines[this.selected_spline].curves[num_of_curves - 2].points.length;

            // If no points in last curve
            if (this.splines[this.selected_spline].curves[num_of_curves - 1].points.length == 0) {

                // First point is last point of previous curve (C0 continuity)
                let first_point = this.splines[this.selected_spline].curves[num_of_curves - 2].points[num_of_points - 1];

                // Get point before last in previous curve to calculate 
                let prev_second_point = this.splines[this.selected_spline].curves[num_of_curves - 2].points[num_of_points - 2];

                // Calculate differences between first point and point before last
                let diff_x = first_point.vec[0] - prev_second_point.vec[0];
                let diff_y = first_point.vec[1] - prev_second_point.vec[1];

                // Calculate new point: 
                // New x = first_point x + difference between x coordinates
                // New y = first_point y + difference between y coordinates 
                let second_point = new Vector([first_point.vec[0] + diff_x, first_point.vec[1] + diff_y]);

                // Add first and second point to curve
                this.splines[this.selected_spline].curves[this.splines[this.selected_spline].curves.length - 1].points.push(first_point);
                this.splines[this.selected_spline].curves[this.splines[this.selected_spline].curves.length - 1].points.push(second_point);
            }

        }

        // Add point to last curve in spline
        this.splines[this.selected_spline].curves[this.splines[this.selected_spline].curves.length - 1].points.push(p);
    }

    addPoints(p1, p2) {
        // Method that adds 2 points in correct order

        // If no spline exists or if spline is empty
        if (this.splines.length == 0 || this.splines[this.selected_spline].curves.length == 0) {
            this.addPoint(p1);
            this.addPoint(p2);
        }
        else {
            this.addPoint(p2);
            this.addPoint(p1);
        }
    }

    delSpline() {
        // Method that deletes selected spline


        // If selected_spline is equal to length of select options (so one index too much)
        if (this.selected_spline >= document.getElementById("selectSpline").length) {
            // Decrease selected spline
            this.selected_spline--;
        }

        // Delete selected spline
        this.splines.splice(this.selected_spline, 1);

        // Remove color that pertains to that spline
        this.colors.splice(this.selected_spline, 1);

        // Remove option of spline from select element
        document.getElementById("selectSpline").remove(this.selected_spline);

        // Redraw everything
        this.draw();

        // If index higher than 0, decrement
        if (this.selected_spline > 0) {
            this.selected_spline--;
        }

        // Set previous spline as selected
        document.getElementById("selectSpline").value = this.selected_spline;
    }

    changeSpline(spline) {
        // Set selected spline
        this.selected_spline = spline;
    }


    // Methods used for drawing

    clearScreen() {
        this.ctx.clearRect(0, 0, this.width, this.height);
    }

    drawPoints() {
        // Method that draws all points in all splines

        // Loop through all splines
        for (let i = 0; i < this.splines.length; i++) {
            // Loop throug all curves in spline
            for (let j = 0; j < this.splines[i].curves.length; j++) {
                // Loop through all points in curve
                for (let k = 0; k < this.splines[i].curves[j].points.length; k++) {
                    // Get current point
                    let curr_point = this.splines[i].curves[j].points[k];

                    // If first or last point (interpolation points)
                    if (k == 0 || k == this.splines[i].curves[j].points.length - 1) {
                        // Start drawing path
                        this.ctx.beginPath();

                        // Set dot color to black
                        this.ctx.fillStyle = "black";

                        // Draw circle with radius 3
                        this.ctx.arc(curr_point.vec[0], curr_point.vec[1], 3, 0, Math.PI * 2);

                        // Set fill color and fill dot
                        this.ctx.fillStyle = "black";
                        this.ctx.fill();

                        // Close drawing path
                        this.ctx.closePath();
                    }
                    // If approximation points
                    else {
                        this.ctx.fillStyle = "black";
                        this.ctx.fillRect(curr_point.vec[0] - 3, curr_point.vec[1] - 3, 6, 6);

                        // If first approximation point
                        if (k == 1) {
                            let end_point = this.splines[i].curves[j].points[k - 1];

                            // Start drawing path
                            this.ctx.beginPath();

                            // Set color of line to grey
                            this.ctx.strokeStyle = "grey";

                            // Set starting coordinates of line
                            this.ctx.moveTo(curr_point.vec[0], curr_point.vec[1]);

                            // Create line to end coordinates
                            this.ctx.lineTo(end_point.vec[0], end_point.vec[1]);

                            // Draw
                            this.ctx.stroke();

                            // Close drawing path
                            this.ctx.closePath();
                        }
                        // If second approximation point
                        else {
                            let end_point = this.splines[i].curves[j].points[k + 1];

                            // Start drawing path
                            this.ctx.beginPath();

                            // Set color of line to grey
                            this.ctx.strokeStyle = "grey";

                            // Set starting coordinates of line
                            this.ctx.moveTo(curr_point.vec[0], curr_point.vec[1]);

                            // Create line to end coordinates
                            this.ctx.lineTo(end_point.vec[0], end_point.vec[1]);

                            // Draw
                            this.ctx.stroke();

                            // Close drawing path
                            this.ctx.closePath();
                        }
                    }
                }
            }
        }
    }

    drawCurves() {
        // Method that draws all curves in all splines

        // Set stroke width
        this.ctx.lineWidth = 1;

        // Set accuracy - how percise the values of curve will be
        let accuracy = 0.01;

        // Loop through all splines
        for (let i = 0; i < this.splines.length; i++) {
            // Get color of that spline
            this.ctx.strokeStyle = this.colors[i];

            // Begin drawing path
            this.ctx.beginPath();

            // Loop through all curves in spline
            for (let j = 0; j < this.splines[i].curves.length; j++) {

                // If atleast 2 points in spline
                if (this.splines[i].curves[j].points.length > 1) {
                    // Get starting point - first point in spline
                    let p1 = this.splines[i].value(0);

                    // Set starting coordinates of spline
                    this.ctx.moveTo(p1.vec[0], p1.vec[1]);

                    // Loop through combined length of all curves in spline
                    for (let k = 0.01; k <= this.splines[i].curves.length; k += accuracy) {
                        // Get value of spline at time k
                        let p2 = this.splines[i].value(k);

                        // Draw line to new point
                        this.ctx.lineTo(p2.vec[0], p2.vec[1]);
                    }

                    // Draw
                    this.ctx.stroke();
                }
            }
        }
    }

    draw() {
        // Driver method for drawing methods

        // Call draw methods
        this.clearScreen();
        this.drawCurves();
        this.drawPoints();
    }

    drawDashed(p1, p2) {
        // Method that draws dotted line while dragging

        // Redraw everything
        this.draw();

        // Draw points
        this.drawSomePoint(p1.vec[0], p1.vec[1], true);
        this.drawSomePoint(p2.vec[0], p2.vec[1]);

        // Begin line path
        this.ctx.beginPath();

        // Set dashes
        this.ctx.setLineDash([5, 15]);

        // Set line color
        this.ctx.strokeStyle = "grey";

        // Move to starting coordinates
        this.ctx.moveTo(p1.vec[0], p1.vec[1]);

        // Create line to end coordinates
        this.ctx.lineTo(p2.vec[0], p2.vec[1]);

        // Draw
        this.ctx.stroke();

        // Remove dottednes
        this.ctx.setLineDash([]);
    }

    drawSomePoint(x, y, circle = false) {
        // Method that draws specific point

        // If drawing circle
        if (circle == true) {
            // Begin drawing path
            this.ctx.beginPath();

            // Set dot color to black
            this.ctx.fillStyle = "black";

            // Draw circle with radius 3
            this.ctx.arc(x, y, 3, 0, Math.PI * 2);

            // Set fill color and fill dot
            this.ctx.fillStyle = "black";
            this.ctx.fill();

            // Close drawing path
            this.ctx.closePath();
        }
        else {
            // Draw square
            this.ctx.fillStyle = "black";
            this.ctx.fillRect(x - 3, y - 3, 6, 6);
        }
    }


    // Utility methods

    changeColor(color) {
        if (this.splines.length != 0) {
            // Change color of selected spline
            this.colors[this.selected_spline] = color;
        }
    }

    findPoint(x, y) {
        // Method that finds point within given coordinates

        // Loop through all splines
        for (let i = 0; i < this.splines.length; i++) {
            // Loop through all curves in spline
            for (let j = 0; j < this.splines[i].curves.length; j++) {
                // Loop through all points in curve
                for (let k = 0; k < this.splines[i].curves[j].points.length; k++) {
                    // If point in bounds
                    if (this.splines[i].curves[j].points[k].vec[0] < x + 6 && this.splines[i].curves[j].points[k].vec[0] + 6 > x) {
                        if (this.splines[i].curves[j].points[k].vec[1] < y + 6 && this.splines[i].curves[j].points[k].vec[1] + 6 > y) {
                            // Return indices of spline, curve and point
                            return [i, j, k];
                        }
                    }
                }
            }
        }
        // If point not found, return invalid indices
        return [-1, -1, -1];
    }

    getNumOfPoints() {
        let sum = 0;

        for (let i = 0; i < this.splines.length; i++) {
            for (let j = 0; j < this.splines[i].curves.length; j++) {
                sum += this.splines[i].curves[j].points.length;
            }
        }

        return sum;
    }
}



