# This file contains the class that contains the main
# "driver" implementation of this project. It gets user
# input for labyrinths and algorithms, then instantiates
# and runs the correct algorithm.


from algorithms.BFS import BFS
from algorithms.DFS import DFS
from algorithms.IDAStar import IDAstar
from algorithms.IDDFS import IDDFS
from algorithms.AStar import AStar
from algorithms.IDAStar import IDAstar
from Drawer import Drawer


class Driver:
    def __init__(self) -> None:
        # Prepare flags and their value domains
        self._labyrinth = ""
        self._algorithm = ""
        self._repeat = ""

        self._labyrinth_domain = ("1", "2", "3", "4", "5", "6", "7", "8", "9")
        self._algorithm_domain = ("BFS", "DFS", "IDDFS", "AStar", "IDAStar")
        self._repeat_domain = ("Y", "N")

        self._bfs = None
        self._dfs = None
        self._iddfs = None
        self._astar = None
        self._idastar = None

    def _reset(self) -> None:
        self._labyrinth = ""
        self._algorithm = ""
        self._repeat = ""

        self._bfs = None
        self._dfs = None
        self._iddfs = None
        self._astar = None
        self._idastar = None

    def _get_labyrinth(self) -> None:
        # Get labyrinth on which to run algorithm
        while self._labyrinth not in self._labyrinth_domain:
            self._labyrinth = input(
                "Enter labyrinth number [" + ", ".join(self._labyrinth_domain) + "]: ")

    def _get_algorithm(self) -> None:
        # Get algorithm
        while self._algorithm.upper() not in [x.upper() for x in self._algorithm_domain]:
            self._algorithm = input(
                "Enter algorithm [" + ", ".join(self._algorithm_domain) + "]: ")

    def _get_repeat(self) -> None:
        # Check if end program
        while self._repeat.upper() not in self._repeat_domain:
            self._repeat = input(
                "Repeat [" + ", ".join(self._repeat_domain) + "]: ")

    def _run_algorithm(self) -> None:
        if not self._algorithm:
            print("Please enter an algorithm!")
        else:
            # Match algorithm, initialize it and run
            match self._algorithm.upper():
                case "BFS":
                    self._bfs = BFS()
                    self._bfs.set_drawer(Drawer())
                    self._bfs.run(self._labyrinth)
                case "DFS":
                    self._dfs = DFS()
                    self._dfs.set_drawer(Drawer())
                    self._dfs.run(self._labyrinth)
                case "IDDFS":
                    self._iddfs = IDDFS()
                    self._iddfs.set_drawer(Drawer())
                    self._iddfs.run(self._labyrinth)
                case "ASTAR":
                    self._astar = AStar()
                    self._astar.set_drawer(Drawer())
                    self._astar.run(self._labyrinth)
                case "IDASTAR":
                    self._idastar = IDAstar()
                    self._idastar.set_drawer(Drawer())
                    self._idastar.run(self._labyrinth)
                case _:
                    print("No valid algorithm!")

    def run(self) -> None:
        # Main loop of this project - it is used to run code until user decides
        # to end the program
        while True:
            # Prepare classes, get labyrinth and algorithm from user
            self._reset()
            self._get_labyrinth()
            self._get_algorithm()

            # Execute algorithms
            self._run_algorithm()

            # Ask user if code should be repeated
            self._get_repeat()

            if self._repeat.upper() == "N":
                break
