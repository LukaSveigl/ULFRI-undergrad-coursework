import { Vector } from "./Vector.js";
import { Bernstein } from "./Bernstein.js";
import { Bezier } from './Bezier.js';
import { Spline } from './Spline.js';
import { Draw } from './Draw.js';

// Create instance of Draw class
let dr = new Draw();

// Create flags for dragged and clicked
let dragged = false;
let clicked = false;

// Create empty temporary points for adding
let pt1 = new Vector([]);
let pt2 = new Vector([]);

// Create empty temporary points for moving
let selected_point = new Vector([]);
let approximation_point = new Vector([]);

// Create empty array of indices
let indices = [];


function mouseDown(e) {
    // Function that handles the mousedown event of canvas

    // Set flags clicked and dragged
    clicked = true;
    dragged = false;

    // Get coordinates of click
    pt1.vec[0] = e.pageX - dr.canvas.offsetLeft;
    pt1.vec[1] = e.pageY - dr.canvas.offsetTop;

    // If moving points
    if (document.getElementById("movePoints").checked == true) {
        // Check if no points in canvas
        if (dr.getNumOfPoints() == 0) {
            alert("No points in canvas - nothing to move - please change action to add!");
        }
        else {
            // Get indices of point clicked (if result [-1, -1, -1], no point selected)
            indices = dr.findPoint(pt1.vec[0], pt1.vec[1]);

            // If indices aren't [-1, -1, -1]
            if (indices[0] != -1) {
                // Get selected point
                selected_point = dr.splines[indices[0]].curves[indices[1]].points[indices[2]];
            }
        }
    }

    // Add event listeners for mousemove and mouseup - simulating drag
    dr.canvas.addEventListener('mousemove', mouseMove);
    dr.canvas.addEventListener('mouseup', mouseUp);
}

function mouseMove(e) {
    // Function that handles the mousemove event of canvas

    // If click occured
    if (clicked == true) {
        // Set dragged flag
        dragged = true;

        // If adding points
        if (document.getElementById("addPoints").checked == true) {
            // Set temporary coordinates for drawing dotted line
            let p_tmpX = e.pageX - dr.canvas.offsetLeft;
            let p_tmpY = e.pageY - dr.canvas.offsetTop;

            let p_tmp = new Vector([p_tmpX, p_tmpY]);

            // Draw dotted line
            dr.drawDashed(pt1, p_tmp);
        }
        // If moving points
        else {
            // Set temporary coordinates for moving selected point
            let p_tmpX = e.pageX - dr.canvas.offsetLeft;
            let p_tmpY = e.pageY - dr.canvas.offsetTop;

            // If point was found (indices aren't [-1, -1, -1])
            if (indices[0] != -1) {
                // If first point in first curve
                if (indices[1] == 0 && indices[2] == 0) {

                    // Get approximation point - second point in curve
                    approximation_point = dr.splines[indices[0]].curves[indices[1]].points[indices[2] + 1];

                    // Calculate differences between selected_point and current coordinates
                    let diff_x = selected_point.vec[0] - p_tmpX;
                    let diff_y = selected_point.vec[1] - p_tmpY;

                    // Adjust approximation point
                    approximation_point.vec[0] -= diff_x;
                    approximation_point.vec[1] -= diff_y;

                    // Set approximation point to spline
                    dr.splines[indices[0]].curves[indices[1]].points[indices[2]] = approximation_point;
                }
                // If last point in last curve
                else if (indices[1] == dr.splines[indices[0]].curves.length - 1 && indices[2] == dr.splines[indices[0]].curves[indices[1]].points.length - 1) {

                    // Get approximation point - point before last in curve
                    approximation_point = dr.splines[indices[0]].curves[indices[1]].points[indices[2] - 1];

                    // Calculate differences between selected_point and current coordinates
                    let diff_x = selected_point.vec[0] - p_tmpX;
                    let diff_y = selected_point.vec[1] - p_tmpY;

                    // Adjust approximation point
                    approximation_point.vec[0] -= diff_x;
                    approximation_point.vec[1] -= diff_y;

                    // Set approximation point to spline
                    dr.splines[indices[0]].curves[indices[1]].points[indices[2]] = approximation_point;
                }

                // Set current mouse coordinates to selected point
                selected_point.vec[0] = p_tmpX;
                selected_point.vec[1] = p_tmpY;

                // Smoothen spline
                dr.splines[indices[0]].makeSmooth();

                // Set selected point to spline
                dr.splines[indices[0]].curves[indices[1]].points[indices[2]] = selected_point;
            }
            // Redraw everything
            dr.draw();
        }
    }
}

function mouseUp(e) {
    // Function that handles the mouseup event of canvas

    // If drag occured
    if (dragged == true) {
        // If adding points
        if (document.getElementById("addPoints").checked == true) {
            // Get coordinates of second point
            pt2.vec[0] = e.pageX - dr.canvas.offsetLeft;
            pt2.vec[1] = e.pageY - dr.canvas.offsetTop;

            // Add points to spline
            dr.addPoints(new Vector([pt1.vec[0], pt1.vec[1]]), new Vector([pt2.vec[0], pt2.vec[1]]));
        }
        // If moving points
        else {
            // Reset indices and selected point
            selected_point = new Vector([]);
            indices = [];
        }
    }

    // Redraw everything
    dr.draw();

    // Reset flags to false
    clicked = false;
    dragged = false;

    // Remove event listeners
    dr.canvas.removeEventListener("mousemove", mouseMove);
    dr.canvas.removeEventListener("mouseup", mouseUp);
}

function addSplineHandler() {
    // Funtion that handles addSpline button click
    dr.addSpline();
}

function delSplineHandler() {
    // Funtion that handles deleteSpline button click
    dr.delSpline();
}

function changeColorHandler() {
    // Function that handles changes in color picker

    // Change color of selected spline
    dr.changeColor(spl_color.value);

    // Redraw everything
    dr.draw();
}

function changeSplineHandler() {
    // Function that handles spline selector
    dr.changeSpline(spl_select.value);
}

// Get button elements
const add_btn = document.getElementById("addSpline");
const del_btn = document.getElementById("deleteSpline");

// Get color picker
const spl_color = document.getElementById("splineColor");

// Get select element
const spl_select = document.getElementById("selectSpline");

// Add event listener to add & delete spline
add_btn.addEventListener('click', addSplineHandler);
del_btn.addEventListener('click', delSplineHandler);

// Add event listener to color picker
spl_color.addEventListener('input', changeColorHandler);

// Add event listener to spline select
spl_select.addEventListener('change', changeSplineHandler);


dr.canvas.addEventListener('mousedown', mouseDown);
