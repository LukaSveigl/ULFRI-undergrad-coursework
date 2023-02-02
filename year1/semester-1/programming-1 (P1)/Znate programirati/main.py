from math import *

goal = float(input("Enter distance to goal in meters: "))

while True:
    velocity = float(input("Enter projectile velocity: "))
    angle = float(input("Enter projectile angle: "))

    distance = ( ( velocity ** 2 ) * sin( 2 * radians(angle) ) ) / 9.81

    if goal - 5 < distance < goal + 5:
        print("Goal hit!")
        break
    else:
        print("Goal not hit, try again!")