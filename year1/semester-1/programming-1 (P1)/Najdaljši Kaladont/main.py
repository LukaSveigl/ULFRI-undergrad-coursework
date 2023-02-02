words = ["DROG", "SPORED", "LES", "DVIG", "LED", "KOL", "DREVORED", "DOL", "LOK"]

words.sort()


def generate_connections(wds):
    in_c = [0] * len(wds)
    out_c = [0] * len(wds)

    count = 0
    for wd in wds:
        for w in wds:
            if wd != w:
                if wd[0] == w[-1]:
                    in_c[count] += 1
                if wd[-1] == w[0]:
                    out_c[count] += 1
        count += 1
    return in_c, out_c


def generate_min(in_c, out_c, is_last):
    min_in = max(in_c)
    min_out = max(out_c)

    for i in in_c:
        if i != 0:
            if i < min_in:
                min_in = i

    for i, o in zip(in_c, out_c):
        if i == min_in:
            if not is_last:
                if 0 < o < min_out:
                    min_out = o
            else:
                if o < min_out:
                    min_out = o
    return min_in, min_out


def generate_starters(min_in, min_out, in_conn, out_conn, wds):
    strs = []

    for ic, oc, w in zip(in_conn, out_conn, wds):
        if ic == min_in and oc == min_out:
            strs.append(w)

    return strs


def find_allowed(wd, wds):
    allowed = []
    for w in wds:
        if wd != w:
            if wd[-1] == wd[0]:
                allowed.append(w)
    return allowed


in_connections, out_connections = generate_connections(words)
smallest_in, smallest_out = generate_min(in_connections, out_connections, False)
starters = generate_starters(smallest_in, smallest_out, in_connections, out_connections, words)
print(starters)

longest = 0

def generate_chain(w, wds):
    chains = [w]
    word = w
    current_words = wds
    while True:
        allowed = find_allowed(word, current_words)
        if len(allowed) == 0:
            return chains
        else:
            for a in allowed:



for s in starters:

    while True:
        allowed = find_allowed(s, words)
        if len(allowed) == 0:
            break
        for a in allowed:



