# from besede import nouns
# n_sorted = sorted(nouns)

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


def generate_minimals(in_c, out_c, wds):
    mi = min(in_connections)
    mo = max(out_connections)

    for _, ic, oc in zip(wds, in_c, out_c):
        if ic == mi:
            if 0 < oc < mo:
                mo = oc

    return mi, mo


def generate_words(mi, mo, wds, in_c, out_c):
    strs = []
    for wd, ic, oc in zip(wds, in_c, out_c):
        if ic == mi and oc == mo:
            strs.append(wd)
    return strs


def find_allowed(wd, wds):
    allowed = []
    for w in wds:
        if wd != w:
            if wd[-1] == wd[0]:
                allowed.append(w)
    return allowed


in_connections, out_connections = generate_connections(words)
min_in, min_out = generate_minimals(in_connections, out_connections, words)
starters = generate_words(min_in, min_out, words, in_connections, out_connections)

print(starters)


def generate_chain(start, wds):
    chains = []
    tmp_chain = [start]
    allowed = ["tmp"]
    words_to_check = [start]
    new_words = []
    while len(allowed) > 0:
        for w in words_to_check:
            allowed = find_allowed(start, new_words)
            in_conn, out_conn = generate_connections(allowed)
            mi, mo = generate_minimals(in_conn, out_conn, allowed)
            new_words = generate_words(mi, mo, allowed, in_conn, out_conn)










