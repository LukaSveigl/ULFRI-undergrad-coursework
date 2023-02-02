import { Vector } from './Vector.js';

export class PointManager {

    parsePoints(input) {
        let lines = input.replace(/  +/g, ' ').split(/\r?\n/); // Replace multiple spaces by one and split string by newlines

        let arr = Array();

        for (let i = 0; i < lines.length; i++) {
            let line = lines[i];
            if (line && line.trim()) { // Check if line has elements which aren't just whitespace
                // Remove whitespace from both ends of string, split into elements by space and convert all of them to float
                let elements = line.trim().split(" ").map(function (x) { x = parseFloat(x); return x; });
                arr.push(new Vector(elements[0], elements[1], elements[2]));
            }
        }
        return arr;
    }

    formatPoints(points) {
        let str = "";

        for (let i = 0; i < points.length; i++) {
            // Convert points to array and fix their decimal places to 2, then join by space
            let line = points[i].toArray();
            str += line + "\n";
        }
        str = str.trim().replace(/,/g, " "); // Remove unneeded whitespace and replace "," with space
        return str;
    }
}
