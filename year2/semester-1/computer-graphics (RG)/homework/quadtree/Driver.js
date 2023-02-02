import { Draw } from "./Draw.js";

const canvas = document.getElementById("canvas");
const ctx = canvas.getContext("2d");

const drawTree = document.getElementById("drawTree");
const pointCount = document.getElementById("pointCount");
const treeCapacity = document.getElementById("capacity");
const pointSpeed = document.getElementById("pointSpeed");

const draw = new Draw(ctx);

pointCount.value = draw.limit;
treeCapacity.value = draw.quadTree.CAPACITY;
pointSpeed.value = draw.currPointSpeed;

window.onload = () => {

    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    draw.draw = draw.draw.bind(draw);

    drawTree.onchange = function () {
        try {
            draw.setDrawGrid(drawTree.checked);
        }
        catch (err) {
            alert(err);
        }
    }

    pointCount.oninput = function () {
        draw.setLimit(pointCount.value);
    }

    treeCapacity.oninput = function () {
        if (Number.parseInt(treeCapacity.value) >= 1) {
            draw.setQuadCapacity(treeCapacity.value);
        }
    }

    pointSpeed.oninput = function () {
        if (Number.parseFloat(pointSpeed.value) >= 0 && Number.parseFloat(pointSpeed.value) <= 3) {
            draw.setPointSpeed(pointSpeed.value);
        }
    }

    window.onresize = () => resize();

    draw.draw();

    requestAnimationFrame(draw.draw);
}

function resize() {
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    draw.setQuadDimensions(canvas.width, canvas.height);
}