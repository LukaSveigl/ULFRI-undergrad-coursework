#Ogrevalna naloga 1:
"""
bobri = "54321"
globina = 3
bobri = bobri[-globina:][::-1] + bobri[:-globina]
print(bobri)
"""
#Ogrevalna naloga 2:
"""
bobri = "ABCDEFGHI"
globina = 3
bobri = bobri[-globina:][::-1] + bobri[:-globina]
print(bobri)
"""
#Obvezna naloga:
"""
num_of_beavers = int(input("Enter number of beavers: "))
num_of_holes = int(input("Enter number of holes: "))

holes = []
beavers = "".join(str(i) for i in list(range(1, num_of_beavers + 1)))[::-1]

for hole_count in range(num_of_holes):
    hole = num_of_beavers + 1
    while hole > num_of_beavers:
        hole = int(input("Enter hole depth: "))
    holes.append(hole)

for depth in holes:
    beavers = beavers[-depth:][::-1] + beavers[:-depth]

print(beavers)
"""
#Dodatna naloga:
num_of_beavers = int(input("Enter number of beavers: "))
num_of_holes = int(input("Enter number of holes: "))

holes = []
return_holes = []
original = beavers = "".join(str(i) for i in list(range(1, num_of_beavers + 1)))[::-1]

print("Original beavers:", beavers)

for hole_count in range(num_of_holes):
    hole = num_of_beavers + 1
    while hole > num_of_beavers:
        hole = int(input("Enter hole depth: "))
    holes.append(hole)

for depth in holes:
    beavers = beavers[-depth:][::-1] + beavers[:-depth]

print("Mixed beavers:", beavers)


def check_consecutive(lst):
    count = 0
    for v1 in lst[::-1]:
        count += 1
        if abs(int(lst[0]) - int(v1)) == 1:
            return count
    return None


while beavers != original:
    depth_of = check_consecutive(beavers)
    beavers = beavers[-depth_of:][::-1] + beavers[:-depth_of]
    return_holes.append(depth_of)

print("Unmixed beavers:", beavers)
print("List of holes to return to original:", return_holes)
