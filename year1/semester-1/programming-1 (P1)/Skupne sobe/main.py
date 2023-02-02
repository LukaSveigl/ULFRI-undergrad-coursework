# Ogrevanje 1:

'''
curr_num = 0
prev_num = 0
count = 0

while count < 16:
    print(curr_num)

    curr_num = (13 * prev_num + 1 ) % 16
    prev_num = curr_num

    count += 1

'''

# Ogrevanje 2:

'''
curr_num = 0
prev_num = 0
count = 0

while count < 1000:
    print(curr_num)

    curr_num = (13 * prev_num + 1 ) % 16
    prev_num = curr_num

    count += 1

'''

# Ogrevanje 3:

'''
curr_num = 0
prev_num = 0
count = 0

while count < 1000:
    print(curr_num)

    curr_num = (1664525 * prev_num + 1013904223) % (2 ** 32)
    prev_num = curr_num

    count += 1

'''

# Glavni del - Zaporedje sob:

'''
curr_num = 0
prev_num = 0

count = 0
count_rooms = 0

while count < 1000:

    curr_num = (1664525 * prev_num + 1013904223) % (2 ** 32)
    prev_num = curr_num

    if curr_num % 10 == 6:
        count_rooms += 1

    count += 1

print("Ana je v sobo 6 vstopila ", count_rooms, "krat.")
'''

# Dodatna naloga - Srečanja:


curr_num_ana = 0
prev_num_ana = 0

curr_num_berta = 0
prev_num_berta = 0

count = 0
count_rooms = 1  # srečata se že v sobi 0 (prvi sobi)

while count < 1000:

    curr_num_ana = (1664525 * prev_num_ana + 1013904223) % (2 ** 32)
    prev_num_ana = curr_num_ana

    curr_num_berta = (22695477 * prev_num_berta + 1) % (2 ** 32)
    prev_num_berta = curr_num_berta

    if curr_num_ana % 10 == curr_num_berta % 10:
        count_rooms += 1

    count += 1

print("Ana in Berta sta se srečali ", count_rooms, "krat.")
