# This file contains the class that is used for drawing
# the labyrinths and found paths. The tile size of labyrinth
# tile changes dynamically based on size of current labyrinth
# but the screen stays the same, due to undefined behaviour
# when dynamically resizing screen

from pydraw import *


class Drawer:
    def __init__(self) -> None:
        self._labyrinth = []
        self._path = []
        self.screen = None

        self._tile_width = 40
        self._tile_height = 40

    def set_labyrinth(self, labyrinth) -> None:
        self._labyrinth = labyrinth

        # Reset tile size
        self._tile_width = 40
        self._tile_height = 40

        # Set tile size based on labyrinth size
        if len(labyrinth) > 40:
            self._tile_width -= 25
            self._tile_height -= 25
        elif len(labyrinth) > 30:
            self._tile_width -= 17
            self._tile_height -= 17
        elif len(labyrinth) > 20:
            self._tile_width -= 10
            self._tile_height -= 10

    def set_path(self, path) -> None:
        self._path = path

    def reset_screen(self) -> None:
        # If screen exists, clear all parameters of it
        # and this class
        if self.screen:
            self.screen.clear()
            self.screen.reset()
            self.screen.update()

            objects = self.screen.objects()

            for obj in objects:
                self.screen.remove(obj)

            self.screen = None
            self._labyrinth = []
            self._path = []

    def draw(self) -> None:
        # Create screen to draw to - it must be static size to avoid
        # undefined behaviour when changing sizes between labyrinths
        self.screen = Screen(
            1000, 1000, "Labyrinth")

        self.screen.update()

        # Loop through labyrinth and display correct color based on tile value
        for y, sublist in enumerate(self._labyrinth):
            for x, value in enumerate(sublist):
                if value == -1:
                    _ = Rectangle(self.screen, x * self._tile_width, y * self._tile_height,
                                  self._tile_width, self._tile_height, color=Color(0, 0, 0))
                elif value == -2:
                    _ = Rectangle(self.screen, x * self._tile_width, y * self._tile_height,
                                  self._tile_width, self._tile_height, color=Color(255, 0, 0))
                elif value == -3:
                    _ = Rectangle(self.screen, x * self._tile_width, y * self._tile_height,
                                  self._tile_width, self._tile_height, color=Color(255, 255, 0))
                elif value == -4:
                    _ = Rectangle(self.screen, x * self._tile_width, y * self._tile_height,
                                  self._tile_width, self._tile_height, color=Color(0, 255, 0))
                else:
                    _ = Rectangle(self.screen, x * self._tile_width, y * self._tile_height,
                                  self._tile_width, self._tile_height, color=Color(255, 255, 255))

                # Display value of tile
                _ = Text(self.screen, str(value), x * self._tile_width + int(self._tile_width / 2.5),
                         y * self._tile_height + int(self._tile_height / 2.5), Color(0, 0, 0), size=int(self._tile_height * 0.5))

        # Go through path and draw line on labyrinth which shows traversal
        for i in range(len(self._path) - 1):
            _ = Line(self.screen, self._path[i][0] * self._tile_width + self._tile_width / 2.5, self._path[i][1] * self._tile_height + self._tile_height / 2.5,
                     self._path[i + 1][0] * self._tile_width + self._tile_width /
                     2.5, self._path[i + 1][1] *
                     self._tile_height + self._tile_height / 2.5,
                     color=Color(0, 0, 255), thickness=3)

        self.screen.update()
