def v_pot(s):
    return [tuple([st[0], int(st[1:])]) for st in s.split()]


def odsek(x, y, smer, razdalja):
    odseki = []
    if smer == ">":
        for r in range(razdalja + 1):
            odseki.append(tuple([x + r, y]))
    elif smer == "^":
        for r in range(razdalja + 1):
            odseki.append(tuple([x, y + r]))
    elif smer == "<":
        for r in range(razdalja + 1):
            odseki.append(tuple([x - r, y]))
    else:
        for r in range(razdalja + 1):
            odseki.append(tuple([x, y - r]))
    return odseki


def tocke(st):
    points = [(0, 0)]
    for s in st:
        x, y = points[-1]
        smer, razdalja = s
        points += odsek(x, y, smer, razdalja)[1:]
    return points


def presecisca(s, t):
    tocke_s = set(tocke(v_pot(s)))
    tocke_t = set(tocke(v_pot(t)))
    return sorted(list(tocke_s & tocke_t))


def tocke_razdalja(st):
    points = [(0, (0, 0))]
    for s in st:
        x, y = points[-1][-1]
        smer, razdalja = s
        ods = odsek(x, y, smer, razdalja)[1:]
        for o in ods:
            points.append((points[-1][0] + 1, o))
    return points


def unique(lst):
    new_list = []
    for l in lst:
        if l[-1] not in set([nl[-1] for nl in new_list]):
            new_list.append(l)
        else:
            i = [nl[-1] for nl in new_list].index(l[-1])
            if new_list[i][0] < l[0]:
                new_list[i] = l
    return new_list


def prvo_srecanje(s, t):
    tocke_s = tocke_razdalja(v_pot(s))[1:]
    tocke_t = tocke_razdalja(v_pot(t))[1:]

    crd_s = set([ts[-1] for ts in set(tocke_s)])
    crd_t = set([tt[-1] for tt in set(tocke_t)])

    interceptions = crd_s & crd_t
    intercepts = []

    for ts in set(tocke_s + tocke_t):
        if ts[-1] in interceptions:
            intercepts.append(ts)

    ints = unique(intercepts)
    return min(ints)

import unittest


class Test(unittest.TestCase):
    def test_v_pot(self):
        self.assertEqual(
            [("^", 2), (">", 6), ("^", 6), ("<", 1), ("v", 3), ("<", 6), ("v", 4)],
            v_pot("^2 >6 ^6 <1 v3 <6 v4"))
        self.assertEqual(
            [(">", 4), ("^", 3), (">", 4), ("^", 3), ("<", 6), ("v", 8), (">", 1), ("^", 5)],
            v_pot(">4 ^3 >4 ^3 <6 v8 >1 ^5"))
        self.assertEqual([(">", 100), ("^", 42), ("<", 13)], v_pot(">100 ^42 <13"))

    def test_odseki(self):
        self.assertEqual([(2, 5), (2, 6), (2, 7), (2, 8)], odsek(2, 5, "^", 3))
        self.assertEqual([(5, 5), (5, 4), (5, 3)], odsek(5, 5, "v", 2))
        self.assertEqual([(2, 5), (1, 5), (0, 5), (-1, 5), (-2, 5)],
                         odsek(2, 5, "<", 4))
        self.assertEqual([(-1, 3), (0, 3)], odsek(-1, 3, ">", 1))

    def test_tocke(self):
        self.assertEqual(
            [(0, 0), (1, 0), (2, 0), (3, 0), (3, -1), (3, -2), (4, -2), (5, -2)],
            tocke([(">", 3), ("v", 2), (">", 2)]))
        self.assertEqual(
            [(0, 0), (0, 1), (0, 2), (1, 2), (2, 2), (3, 2), (4, 2), (5, 2),
             (6, 2), (6, 3), (6, 4), (6, 5), (6, 6), (6, 7), (6, 8), (5, 8),
             (5, 7), (5, 6), (5, 5), (4, 5), (3, 5), (2, 5), (1, 5), (0, 5),
             (-1, 5), (-1, 4), (-1, 3), (-1, 2), (-1, 1)],
            tocke([("^", 2), (">", 6), ("^", 6), ("<", 1), ("v", 3), ("<", 6), ("v", 4)]))
        self.assertEqual(
            [(0, 0), (1, 0), (2, 0), (3, 0), (4, 0), (4, 1), (4, 2), (4, 3),
             (5, 3), (6, 3), (7, 3), (8, 3), (8, 4), (8, 5), (8, 6), (7, 6),
             (6, 6), (5, 6), (4, 6), (3, 6), (2, 6), (2, 5), (2, 4), (2, 3),
             (2, 2), (2, 1), (2, 0), (2, -1), (2, -2), (3, -2), (3, -1), (3, 0),
             (3, 1), (3, 2), (3, 3)],
            tocke([(">", 4), ("^", 3), (">", 4), ("^", 3), ("<", 6), ("v", 8),
                   (">", 1), ("^", 5)]))

    def test_presecisca(self):
        self.assertEqual(
            [(0, 0), (2, 2), (2, 5), (3, 2), (4, 2), (5, 6), (6, 3), (6, 6)],
            sorted(presecisca("^2 >6 ^6 <1 v3 <6 v4", ">4 ^3 >4 ^3 <6 v8 >1 ^5"))
        )
        self.assertEqual(
            [(-4221, -3448), (-4221, -3429), (-4114, -3611), (-4099, -3660),
             (-4083, -3809), (-3946, -3448), (-3852, -2559), (-3749, -3611),
             (-3749, -3480), (-3749, -3403), (-3480, -2559), (-3365, -3448),
             (-3365, -3024), (-3326, -3480), (-3266, -3024), (-3127, -3024),
             (-2367, -4101), (-2326, -3210), (-2246, -3164), (-2183, -4106),
             (-1378, -6560), (-1378, -6402), (-1378, -6307), (-1366, -6590),
             (-821, -6254), (-524, -6012), (-494, -6012), (-455, -6012),
             (-377, -6012), (-46, -5668), (0, 0), (39, -5668), (499, -6895),
             (499, -6750), (562, -5065), (562, -4757), (973, -5348),
             (973, -5186), (974, -6355), (996, -7017), (1421, -6026),
             (1913, -6258)],
            sorted(presecisca(">995 ^671 >852 ^741 >347 ^539 >324 ^865 >839 ^885 >924 v983 >865 v823 >457 ^124 >807 ^941 >900 ^718 >896 v795 >714 v129 >465 ^470 <625 ^200 <707 ^552 <447 v305 <351 v571 <346 v38 <609 ^581 <98 v707 >535 v332 <23 v630 <66 ^833 <699 v445 <981 v81 <627 ^273 >226 v51 <177 v806 >459 v950 >627 ^462 <382 v847 >335 v573 <902 v581 <375 v288 >26 ^922 >710 v159 >481 ^907 <852 ^926 <905 v140 <581 ^908 >158 v955 >349 ^708 >196 v13 >628 v862 <899 ^50 <56 v89 <506 ^65 >664 v243 <701 v887 <552 ^665 <674 ^813 <433 ^87 >951 v970 >914 v705 >79 ^328 <107 v86 <307 ^550 <872 ^224 <595 v600 >442 v426 <139 ^528 >680 ^35 <951 v275 <78 ^113 <509 ^821 >150 ^668 <981 ^102 <632 v864 >636 v597 >385 ^322 >464 ^249 <286 v138 <993 ^329 >874 v849 >6 v632 <751 ^235 >817 v495 <152 v528 >872 v91 >973 v399 <14 v544 >20 ^54 <793 ^90 <756 v36 >668 v221 <286 v681 <901 ^312 >290 v874 <155 ^863 >35 v177 >900 v865 >250 v810 <448 v648 <358 ^308 >986 v562 <112 v858 >77 v880 <12 ^702 <987 v662 >771 ^6 >643 ^845 >54 ^987 <994 v878 <934 ^805 <85 v760 <775 v578 <557 ^544 <522 ^495 <678 v68 >615 ^700 <415 ^597 <964 v858 >504 ^805 <392 ^140 <721 v215 <842 ^929 <30 ^64 <748 v136 >274 v605 >863 ^460 <354 ^78 >705 v298 <456 ^117 >308 v186 <707 v367 >824 ^965 <162 v19 >950 v582 >911 v436 <165 ^506 <186 v906 <69 ^412 >810 ^13 <350 ^314 >192 ^963 <143 v937 <685 v574 >434 v937 <365 ^646 <741 ^703 <66 ^959 <103 ^799 <480 ^340 >981 ^96 <675 ^662 >536 ^15 >171 ^382 >396 v431 <922 v662 >365 v921 >915",
                              "<999 v290 <462 v773 <687 v706 <785 v219 >102 ^307 <466 v166 >11 v712 <675 v844 >834 ^665 >18 v91 >576 ^187 <832 v969 <856 ^389 >275 v587 <153 ^329 >833 ^762 >487 ^607 >232 v361 >301 v738 <121 v896 >729 v767 >596 ^996 >856 v849 >748 v506 <949 ^166 >194 v737 <946 v504 <908 v980 <249 ^885 >930 v910 >860 v647 <985 ^688 <695 ^207 <182 v444 >809 v394 >441 ^664 <721 ^31 >690 ^597 >694 ^942 >878 ^320 >874 ^162 <840 ^575 <602 ^649 <337 v775 <316 v588 >603 v175 <299 v538 >117 ^213 <542 v429 >969 v641 >946 v373 <406 v119 >58 v686 >460 ^906 <303 v13 <209 v546 >33 v545 >806 ^615 >416 v294 <932 v877 >270 ^350 >40 ^720 <248 v13 <120 v657 <787 ^313 >93 ^922 >330 v184 <595 v578 >144 v213 <827 ^787 >41 v142 >340 v733 <547 ^595 <49 ^652 <819 v691 >871 v628 >117 ^880 <140 ^736 <776 ^151 >781 ^582 >438 v382 >747 v390 >956 ^44 <205 ^680 >775 v152 <8 v80 >730 ^922 <348 ^363 <44 v355 >556 v880 >734 ^60 >102 ^776 <822 v732 <332 v769 <272 v784 >908 ^58 <252 ^290 >478 v192 >638 ^548 >169 v946 <749 v638 <962 ^844 >458 v283 >354 ^95 <271 ^738 >764 ^757 >862 ^176 <699 v810 <319 ^866 >585 ^743 <483 v502 >904 v248 <792 v37 >679 ^607 <439 ^326 <105 ^95 <486 v214 >981 ^260 >801 ^212 <718 ^302 <644 v987 <73 ^228 <576 ^507 <231 v63 >871 ^802 >282 v237 <277 ^418 >116 ^194 >829 ^786 <982 v131 >630 ^358 >939 v945 <958 v961 >889 ^949 <469 v980 >25 v523 <830 ^343 >780 ^581 >562 ^115 <569 v959 >738 ^299 <719 ^732 <444 v579 <13 ^242 <953 ^169 >812 v821 >961 v742 >814 v483 >479 v123 <745 v892 <534"))
        )


class TestDodatna(unittest.TestCase):
    def test_prvo_srecanje(self):
        self.assertEqual(
            (6, (4, 2)),
            prvo_srecanje("^2 >6 ^6 <1 v3 <6 v4", ">4 ^3 >4 ^3 <6 v8 >1 ^5")
        )
        self.assertEqual(
            (96331, (562, -5065)),
            prvo_srecanje(">995 ^671 >852 ^741 >347 ^539 >324 ^865 >839 ^885 >924 v983 >865 v823 >457 ^124 >807 ^941 >900 ^718 >896 v795 >714 v129 >465 ^470 <625 ^200 <707 ^552 <447 v305 <351 v571 <346 v38 <609 ^581 <98 v707 >535 v332 <23 v630 <66 ^833 <699 v445 <981 v81 <627 ^273 >226 v51 <177 v806 >459 v950 >627 ^462 <382 v847 >335 v573 <902 v581 <375 v288 >26 ^922 >710 v159 >481 ^907 <852 ^926 <905 v140 <581 ^908 >158 v955 >349 ^708 >196 v13 >628 v862 <899 ^50 <56 v89 <506 ^65 >664 v243 <701 v887 <552 ^665 <674 ^813 <433 ^87 >951 v970 >914 v705 >79 ^328 <107 v86 <307 ^550 <872 ^224 <595 v600 >442 v426 <139 ^528 >680 ^35 <951 v275 <78 ^113 <509 ^821 >150 ^668 <981 ^102 <632 v864 >636 v597 >385 ^322 >464 ^249 <286 v138 <993 ^329 >874 v849 >6 v632 <751 ^235 >817 v495 <152 v528 >872 v91 >973 v399 <14 v544 >20 ^54 <793 ^90 <756 v36 >668 v221 <286 v681 <901 ^312 >290 v874 <155 ^863 >35 v177 >900 v865 >250 v810 <448 v648 <358 ^308 >986 v562 <112 v858 >77 v880 <12 ^702 <987 v662 >771 ^6 >643 ^845 >54 ^987 <994 v878 <934 ^805 <85 v760 <775 v578 <557 ^544 <522 ^495 <678 v68 >615 ^700 <415 ^597 <964 v858 >504 ^805 <392 ^140 <721 v215 <842 ^929 <30 ^64 <748 v136 >274 v605 >863 ^460 <354 ^78 >705 v298 <456 ^117 >308 v186 <707 v367 >824 ^965 <162 v19 >950 v582 >911 v436 <165 ^506 <186 v906 <69 ^412 >810 ^13 <350 ^314 >192 ^963 <143 v937 <685 v574 >434 v937 <365 ^646 <741 ^703 <66 ^959 <103 ^799 <480 ^340 >981 ^96 <675 ^662 >536 ^15 >171 ^382 >396 v431 <922 v662 >365 v921 >915",
                          "<999 v290 <462 v773 <687 v706 <785 v219 >102 ^307 <466 v166 >11 v712 <675 v844 >834 ^665 >18 v91 >576 ^187 <832 v969 <856 ^389 >275 v587 <153 ^329 >833 ^762 >487 ^607 >232 v361 >301 v738 <121 v896 >729 v767 >596 ^996 >856 v849 >748 v506 <949 ^166 >194 v737 <946 v504 <908 v980 <249 ^885 >930 v910 >860 v647 <985 ^688 <695 ^207 <182 v444 >809 v394 >441 ^664 <721 ^31 >690 ^597 >694 ^942 >878 ^320 >874 ^162 <840 ^575 <602 ^649 <337 v775 <316 v588 >603 v175 <299 v538 >117 ^213 <542 v429 >969 v641 >946 v373 <406 v119 >58 v686 >460 ^906 <303 v13 <209 v546 >33 v545 >806 ^615 >416 v294 <932 v877 >270 ^350 >40 ^720 <248 v13 <120 v657 <787 ^313 >93 ^922 >330 v184 <595 v578 >144 v213 <827 ^787 >41 v142 >340 v733 <547 ^595 <49 ^652 <819 v691 >871 v628 >117 ^880 <140 ^736 <776 ^151 >781 ^582 >438 v382 >747 v390 >956 ^44 <205 ^680 >775 v152 <8 v80 >730 ^922 <348 ^363 <44 v355 >556 v880 >734 ^60 >102 ^776 <822 v732 <332 v769 <272 v784 >908 ^58 <252 ^290 >478 v192 >638 ^548 >169 v946 <749 v638 <962 ^844 >458 v283 >354 ^95 <271 ^738 >764 ^757 >862 ^176 <699 v810 <319 ^866 >585 ^743 <483 v502 >904 v248 <792 v37 >679 ^607 <439 ^326 <105 ^95 <486 v214 >981 ^260 >801 ^212 <718 ^302 <644 v987 <73 ^228 <576 ^507 <231 v63 >871 ^802 >282 v237 <277 ^418 >116 ^194 >829 ^786 <982 v131 >630 ^358 >939 v945 <958 v961 >889 ^949 <469 v980 >25 v523 <830 ^343 >780 ^581 >562 ^115 <569 v959 >738 ^299 <719 ^732 <444 v579 <13 ^242 <953 ^169 >812 v821 >961 v742 >814 v483 >479 v123 <745 v892 <534")
        )


if __name__ == "__main__":
    unittest.main()

