# This file contains the class which represents the
# implementation of A* (an informed search) algorithm.
# It finds the path from a given start node, picks up all
# treasures and finds the exit using a list of open tiles and
# a dict of closed tiles. The heuristic function it uses is
# based on manhattan distance between a start node and chosen
# end node.
# It is also capable of writing the results to a file
# and displaying them on screen

from copy import deepcopy
from itertools import groupby


class AStar:
    def __init__(self) -> None:
        self._labyrinth = []
        self._drawer = None

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

    def _write_results(self, cost, checked, route, labyrinth_number) -> None:
        # If results file has too many lines already, clear it
        if sum(1 for _ in open("results.txt", "r")) > 50:
            file = open("results.txt", "w")
            file.close()

        # Open file with results
        file = open("results.txt", "a")

        # Write run statistics to results file
        file.write("\n\n")
        file.write("Algorithm: A*\n")
        file.write("Labyrinth: labyrinth_" + labyrinth_number + ".txt\n")
        file.write("\n")
        file.write("Cost:            " + str(cost) + "\n")
        file.write("Length of route: " + str(len(route)))
        file.write("Checked tiles:   " + str(checked) + "\n")
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

    def _search(self, start_node, end_node, treasures) -> tuple:
        # Copy start node, initialize list of open tiles and list of closed tiles
        start = start_node
        open_list = [start]
        closed_list = dict()

        # Get closest treasure based on manhattan distance - used to estimate heuristics
        manhattans = []

        for t in treasures:
            manhattans.append(self._manhattan_distance(start, t))

        heuristics = self._generate_heuristics(
            start_node, treasures[manhattans.index(min(manhattans))])

        # Set dict of predecessors and create empty list that
        # holds already found treasures
        found_treasures = []
        previous = dict()

        x, y = start

        # Initialize counters and route
        cost = 0
        checked = 0
        route = []

        # Set g_score and f_score matrix to infinity and set starting value
        # for start node
        g_score = self._generate_infinity()
        g_score[y][x] = 0

        f_score = self._generate_infinity()
        f_score[y][x] = heuristics[y][x]

        while open_list:
            # Select node with lowest f_score as current node
            lowest = self._find_lowest_score(open_list, f_score)
            curr_node = [t for t in open_list if t == lowest][0]
            checked += 1

            # Remove node from open list and add it to closed list
            open_list.remove(curr_node)
            closed_list[curr_node] = True

            # If current node is end node and all treasures found
            if curr_node == end_node:
                if len(found_treasures) == len(treasures):
                    # Assemble route from last treasure to end node
                    curr_cost, curr_route = self._assemble_route(
                        curr_node, start, previous)

                    route.extend(curr_route)
                    cost += curr_cost

                    # Remove duplicate entries - needed because treasure nodes
                    # would be input twice
                    route = self._remove_consecutive_duplicates(route)

                    return cost, checked, route

            # If current node is treasure and not already found
            if curr_node in treasures:
                if curr_node not in found_treasures:
                    # Assemble route from last treasure or start point to
                    # newly found treasure
                    curr_cost, curr_route = self._assemble_route(
                        curr_node, start, previous)

                    route.extend(curr_route)
                    cost += curr_cost

                    # Reset start node
                    start = curr_node

                    # Add treasure to found
                    found_treasures.append(curr_node)

                    # Again calculate heuristics based on treasures not found yet
                    tmp_treasures = [
                        x for x in treasures if x not in found_treasures]

                    manhattans = []

                    for t in tmp_treasures:
                        manhattans.append(self._manhattan_distance(start, t))

                    # Check if no distances were calculated - needed in case all treasures
                    # are found
                    if not manhattans:
                        heuristics = self._generate_heuristics(
                            start, end_node)
                    else:
                        heuristics = self._generate_heuristics(
                            start, tmp_treasures[manhattans.index(min(manhattans))])

                    # Reset predecessors and g_score, f_score matrices
                    previous = dict()

                    x, y = start
                    g_score = self._generate_infinity()
                    g_score[y][x] = 0

                    f_score = self._generate_infinity()
                    f_score[y][x] = heuristics[y][x]

                    open_list = [start]
                    closed_list = dict()

            # Get possible next moves
            next_nodes = self._find_next_move(curr_node)

            # For each possible move, check if it isn't a wall and if
            # it isn't marked as closed, if not, calculate distance and
            # update g_score and f_score matrices
            for next_node in next_nodes:
                x, y = next_node
                if self._labyrinth[y][x] != -1 and next_node not in closed_list:
                    open_list.append(next_node)
                    x1, y1 = curr_node
                    x2, y2 = next_node
                    dist = g_score[y1][x1] + \
                        self._labyrinth[y2][x2]

                    if dist < g_score[y2][x2]:
                        previous[next_node] = curr_node
                        g_score[y2][x2] = dist
                        f_score[y2][x2] = g_score[y2][x2] + heuristics[y2][x2]

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
