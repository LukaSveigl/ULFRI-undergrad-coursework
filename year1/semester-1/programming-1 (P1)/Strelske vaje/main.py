from math import *

velocity = float( input("Enter projectile velocity: ") )
angle = float( input("Enter projectile angle: ") )
distance = ( ( velocity ** 2 ) * sin( 2 * radians(angle) ) ) / 9.81
print("The projectile flies ", distance, "meters" )

