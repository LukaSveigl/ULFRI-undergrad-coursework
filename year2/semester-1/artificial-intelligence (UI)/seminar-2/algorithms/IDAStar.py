# This file contains the class which represents the
# implementation of IDA* (Iterative A*) algorithm.
# It finds the path from a given start node, picks up all
# treasures and finds the exit using a list of open tiles and
# a dict of closed tiles. The heuristic function it uses is
# based on manhattan distance between a start node and chosen
# end node. Each iteration, it only checks nodes whose cost
# is under a certain boundary, calculated based on heuristics.
# It is also capable of writing the results to a file
# and displaying them on screen

from copy import deepcopy
from itertools import groupby


class IDAstar:
    def __init__(self) -> None:
        self._labyrinth = []
        self._drawer = None
        self._found = False
        self._start = (0, 0)
        self._checked = 0
        self._level = 0

    def set_drawer(self, drawer) -> None:
        self._drawer = drawer

    def _reset(self) -> None:
        self._labyrinth = []

    def _set_labyrinth(self, labyrinth) -> None:
        # Open correct file
        with open("./labyrinths/labyrinth_" + labyrinth + ".txt", "r") as file:
            # Read lines until EOF is reached
            line = file.readline()
            while line != "":
                # Split each line by ",", cast each element of list to integer
                self._labyrinth.append([int(node)
                                        for node in line.split(",")])
                line = file.readline()

    def _display_results(self, cost, checked, route) -> None:
        print("Route:   ", route)
        print("Cost:    ", cost)
        print("Length of route:" + str(len(route)))
        print("Checked: ", checked)
        print("Recursion depth: ", self._level)

    def _write_results(self, cost, checked, route, labyrinth_number) -> None:
        # If results file has too many lines already, clear it
        if sum(1 for _ in open("results.txt", "r")) > 50:
            file = open("results.txt", "w")
            file.close()

        # Open file with results
        file = open("results.txt", "a")

        # Write run statistics to results file
        file.write("\n\n")
        file.write("Algorithm: IDA*\n")
        file.write("Labyrinth: labyrinth_" + labyrinth_number + ".txt\n")
        file.write("\n")
        file.write("Cost:            " + str(cost) + "\n")
        file.write("Length of route: " + str(len(route)))
        file.write("Checked tiles:   " + str(checked) + "\n")
        file.write("Recursion depth: " + str(self._level))
        file.write("Route taken:   \n")
        counter = 0
        for x, y in route:
            counter += 1
            file.write("(" + str(x) + ", " + str(y) + "), ")
            # If over 9 nodes in line, add new line
            if counter > 9:
                counter = 0
                file.write("\n")
        file.write("\n----------------------------")
        file.close()

    def _find_start_node(self) -> tuple:
        # Loop through all lists in labyrinth list and
        # look for start node, when start node is found
        # return tuple of it's coordinates
        for y, sublist in enumerate(self._labyrinth):
            for x, value in enumerate(sublist):
                if value == -2:
                    return (x, y)

    def _find_end_nodes(self) -> list:
        # End nodes are all treasures in labyrinth + end node
        # because we search paths from start -> treasure -> treasure -> end
        end_nodes = []

        # End stores the labyrinth's end node, which must be added last to list
        end = (0, 0)

        for y, sublist in enumerate(self._labyrinth):
            for x, value in enumerate(sublist):
                if value == -3:
                    end_nodes.append((x, y))
                elif value == -4:
                    end = (x, y)
        # Add end node to list of end nodes
        end_nodes.append(end)

        return end_nodes

    def _remove_consecutive_duplicates(self, list) -> list:
        return [x[0] for x in groupby(list)]

    def _manhattan_distance(self, node1, node2) -> int:
        return abs(node1[0] - node2[0]) + abs(node1[1] - node2[1])

    def _generate_visited(self) -> list:
        # Copy labyrinth to visited
        visited = deepcopy(self._labyrinth)

        # Mark walls of labyrinth as visited
        for y, sublist in enumerate(visited):
            for x, value in enumerate(sublist):
                if value == -1:
                    visited[y][x] = True
                else:
                    visited[y][x] = False

        return visited

    def _generate_heuristics(self, start_node, end_node) -> list:
        heuristics = deepcopy(self._labyrinth)

        for y, sublist in enumerate(heuristics):
            for x, value in enumerate(sublist):
                if value != -1:
                    heuristics[y][x] = self._manhattan_distance(
                        (x, y), end_node)
                else:
                    heuristics[y][x] = float("inf")

        return heuristics

    def _generate_infinity(self):
        infinity = deepcopy(self._labyrinth)

        for y, sublist in enumerate(infinity):
            for x, _ in enumerate(sublist):
                infinity[y][x] = float("inf")

        return infinity

    def _find_lowest_score(self, nodes, f_score):
        lowest = float("inf")
        lowest_node = (0, 0)
        for x, y in nodes:
            if f_score[y][x] < lowest:
                lowest = f_score[y][x]
                lowest_node = (x, y)

        return lowest_node

    def _find_next_move(self, curr_node) -> list:
        x, y = curr_node
        # Return coordinates of moves that exist from current node,
        # which are left, top, right, bottom
        return [(x - 1, y), (x, y + 1), (x + 1, y), (x, y - 1)]

    def _assemble_route(self, end, start, previous) -> list:
        # This method assembles the route from end node to start node
        # using a dict

        # Initialize path
        path = []

        cost = 0

        curr_node = end
        # prev_node = previous[curr_node]

        while curr_node:
            # If current node is start, return reversed path
            if curr_node == start:
                path.append(curr_node)
                return cost, path[::-1]

            # Add current node to path, current node is previous
            x, y = curr_node
            if self._labyrinth[y][x] > 0:
                cost += self._labyrinth[y][x]
            path.append(curr_node)
            curr_node = previous[curr_node]

    def _find(self, path: list, g_score, bound, heuristics, end_node, treasures: list, level):
        self._level += 1
        # Get top node in path - path acts as a stack
        node = path[-1]
        x, y = node

        f_score = g_score + heuristics[y][x]

        # If f_score out of bounds, stop check
        if f_score > bound:
            return f_score

        self._checked += 1

        # If end node reached and all treasures found,
        # set found as true and return
        if node == end_node:
            if not treasures:
                self._found = True
                return f_score

        # If treasure found, set found as true, remove treasure,
        # recalculate heuristics and return
        if node in treasures:
            self._found = True
            treasures.remove(node)

            manhattans = []

            for tr in treasures:
                manhattans.append(self._manhattan_distance(node, tr))

            # Check if no distances were calculated - needed in case all treasures
            # are found
            if not manhattans:
                heuristics = self._generate_heuristics(
                    node, end_node)
            else:
                heuristics = self._generate_heuristics(
                    node, treasures[manhattans.index(min(manhattans))])

            return f_score

        minimum = float("inf")

        # Find next possible nodes
        next_nodes = self._find_next_move(node)

        # For each possible node, check if it is valid and not yet in path
        for next_node in next_nodes:
            x, y = next_node
            if self._labyrinth[y][x] != -1 and next_node not in path:
                # Add node on top of stack
                # path.append(next_node)
                path.append(next_node)

                x1, y1 = node
                x2, y2 = next_node
                # Perform check for newly added node
                # t = self._find(
                #    path, g_score + (self._labyrinth[y1][x1] + self._labyrinth[y2][x2]), bound, heuristics, end_node, treasures, level + 1)

                t = self._find(
                    path, g_score + self._labyrinth[y2][x2], bound, heuristics, end_node, treasures, level + 1)

                # If found, return
                if self._found:
                    return t

                if t < minimum:
                    minimum = t

                # Remove node from top of stack
                path.pop()

        # Return minimum as f_score
        return minimum

    def _search(self, start_node, end_node, treasures) -> tuple:
        start = start_node
        # Get closest treasure based on manhattan distance - used to estimate heuristics
        manhattans = []

        for tr in treasures:
            manhattans.append(self._manhattan_distance(start, tr))

        heuristics = self._generate_heuristics(
            start, treasures[manhattans.index(min(manhattans))])

        x, y = start
        # Set bound and add start node to path
        bound = heuristics[y][x]
        path = [start]

        route = []
        cost = 0

        while True:
            # print(bound)

            # Perform search
            t = self._find(path, 0, bound, heuristics,
                           end_node, treasures, self._level)

            # If one of end nodes was found
            if self._found:
                # If end node and treasures found
                if not treasures and end_node in path:
                    # Extend route
                    route += path

                    route = self._remove_consecutive_duplicates(route)

                    # Calculate cost and return
                    for node in route:
                        x, y = node
                        if self._labyrinth[y][x] > 0:
                            cost += self._labyrinth[y][x]
                    return cost, self._checked, route
                # If treasure found
                else:
                    # Reset found, add path and reset path
                    self._found = False
                    route += path
                    path = [route[-1]]
            if t == float("inf"):
                break

            bound = t

    def run(self, labyrinth) -> None:
        # Set labyrinth, find start node and all end nodes
        self._set_labyrinth(labyrinth)
        start_node = self._find_start_node()
        end_nodes = self._find_end_nodes()

        cost, checked, route = self._search(
            start_node, end_nodes[-1], end_nodes[0:-1])

        # Display results
        self._display_results(cost, checked, route)
        self._write_results(cost, checked, route, labyrinth)

        # Set drawer parameters and draw labyrinth
        self._drawer.reset_screen()
        self._drawer.set_labyrinth(self._labyrinth)
        self._drawer.set_path(route)
        self._drawer.draw()

        # Reset internal parameters
        self._reset()
