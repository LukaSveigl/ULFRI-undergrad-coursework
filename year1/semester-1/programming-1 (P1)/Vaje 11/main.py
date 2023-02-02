import risar
import random
import math


def random_circles(n):
    for i in range(n):
        x, y = risar.nakljucne_koordinate()
        color = risar.nakljucna_barva()
        radius = random.randint(10, 55)
        risar.krog(x, y, radius, barva=color)
    risar.stoj()

def draw_rcircle():
    x, y = risar.nakljucne_koordinate()
    color = risar.nakljucna_barva()
    radius = random.randint(10, 55)
    risar.krog(x, y, radius, barva=color)
    return x, y, color


def draw_rcircle_rad():
    x, y = risar.nakljucne_koordinate()
    color = risar.nakljucna_barva()
    radius = random.randint(10, 55)
    risar.krog(x, y, radius, barva=color)
    return x, y, radius


def connected_first_circle(n):
    x, y, _ = draw_rcircle()
    first = (x, y)
    for i in range(n - 1):
        x, y, color = draw_rcircle()
        risar.crta(first[0], first[1], x, y, barva=color)
    risar.stoj()


def connected_between(n):
    coords = []
    for i in range(n):
        x, y, color = draw_rcircle()
        for c in coords:
            risar.crta(x, y, c[0], c[1], barva=color)
        coords.append((x, y))
    risar.stoj()


def circle_on_elypsis(n, radius):
    main_coords = (risar.maxX / 2, risar.maxY / 2)
    circumference = 2 * math.pi * radius
    small_r = (circumference / n) / 2
    theta = (math.pi * 2)/n

    for i in range(n):
        x = radius * math.cos(theta * i) + main_coords[0]
        y = radius * math.sin(theta * i) + main_coords[1]
        risar.krog(x, y, small_r, barva=risar.nakljucna_barva())
    risar.stoj()


def connected_elypsis(n, radius):
    main_coords = (risar.maxX / 2, risar.maxY / 2)
    circumference = 2 * math.pi * radius
    small_r = (circumference / n) / 2
    theta = (math.pi * 2) / n

    coords = []

    for i in range(n):
        x = radius * math.cos(theta * i) + main_coords[0]
        y = radius * math.sin(theta * i) + main_coords[1]
        risar.krog(x, y, small_r, barva=risar.nakljucna_barva())
        for c in coords:
            risar.crta(x, y, c[0], c[1], barva=risar.nakljucna_barva())
        coords.append((x, y))
    risar.stoj()


def not_intersecting(n):
    encountered = []

    x, y = risar.nakljucne_koordinate()
    radius = random.randint(20, 60)
    risar.krog(x, y, radius, barva=risar.nakljucna_barva())

    encountered.append((x, y, radius))

    count = 0

    while count != n:
        x, y = risar.nakljucne_koordinate()
        radius = random.randint(20, 60)
        found = False
        while True:
            for e in encountered:
                dist_sq = (max(x, e[0]) - min(x, e[0])) ** 2 + (max(y, e[1]) - min(y, e[1]))
                radsum_sq = (radius + e[2]) ** 2
                if dist_sq <= radsum_sq:
                    found = True
                    break
                elif dist_sq + radius <= e[2]:
                    found = True
                    break
                elif dist_sq + e[2] <= radius:
                    found = True
                    break
            if not found:
                risar.krog(x, y, radius, barva=risar.nakljucna_barva())
                encountered.append((x, y, radius))
                break
    risar.stoj()


def gore(x1, y1, x2, y2, odmik):
    if odmik < 1:
        risar.crta(x1, y1, x2, y2)
    else:
        x, y = (x1+x2)/2, (y1+y2)/2
        y += random.randint(-odmik, odmik)
        gore(x1, y1, x, y, odmik//1.7)
        gore(x, y, x2, y2, odmik//1.7)



#random_circles(50)
#connected_first_circle(50)
#connected_between(50)
#circle_on_elypsis(50, 250)
#connected_elypsis(50, 250)
not_intersecting(1000)
#gore(0, risar.maxY/2, risar.maxX, risar.maxY/2, 200)
#risar.stoj()