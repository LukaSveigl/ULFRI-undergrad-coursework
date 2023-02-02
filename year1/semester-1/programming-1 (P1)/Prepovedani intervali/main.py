# Naloga 1:
'''
prepovedani = [
    (12, 18),
    (2, 5),
    (3, 8),
    (0, 4),
    (15, 19),
    (6, 9),
    (13, 17),
    (4, 8)
]

highest = 0

for intvl in prepovedani:
    if intvl[-1] > highest:
        highest = intvl[-1]

print("Najvišja zgornja meja:", highest)
'''

# Naloga 2:
'''
prepovedani = [
    (12, 18),
    (2, 5),
    (3, 8),
    (0, 4),
    (15, 19),
    (6, 9),
    (13, 17),
    (4, 8)
]

highest = 0

for intvl in prepovedani:
    if intvl[-1] > highest:
        highest = intvl[-1]

count = 0

while count <= highest:
    found = False

    for intvl2 in prepovedani:
        if intvl2[0] <= count <= intvl2[1]:
            found = True
            temp_interval = tuple(intvl2)
            break

    if found == True:
        print(count, "je vsebovan v", temp_interval)
    else:
        print(count, "je dovoljeno")
    count += 1
'''

# Dodatna naloga 1:
'''
prepovedani = [
    (12, 18),
    (2, 5),
    (3, 8),
    (0, 4),
    (15, 19),
    (6, 9),
    (13, 17),
    (4, 8)
]

count = 0

while count <= 30:
    found = False

    for intvl2 in prepovedani:
        if intvl2[0] <= count <= intvl2[1]:
            found = True
            break
    if not found:
        break
    count += 1

print("Prvo dovoljeno število:", count)
'''

# Dodatna naloga 2:
'''
prepovedani = [
    (12, 18),
    (2, 5),
    (3, 8),
    (0, 4),
    (15, 19),
    (6, 9),
    (13, 17),
    (4, 8),
    (9, 12)
]

count = 0

while count <= 30:
    found = False

    for intvl2 in prepovedani:
        if intvl2[0] <= count <= intvl2[1]:
            found = True
            break
    if not found:
        break
    count += 1

print("Prvo dovoljeno število:", count)
'''
