def osebe(obiski):
    return set(os for os, _ in obiski)


def aktivnosti(obiski):
    return set(ak for _, ak in obiski)


def udelezenci(aktivnost, obiski):
    return set(os for os, ak in obiski if ak == aktivnost)


def po_aktivnostih(obiski):
    return {ak: set(o for o, a in obiski if a == ak) for os, ak in obiski}


def skupine(obiski):
    return list(po_aktivnostih(obiski).values())


def okuzeni(skupine, nosilci):
    sets = [[s for s in skp if s not in nosilci] for skp in skupine for ns in nosilci if ns in skp]
    return set([inner for outer in sets for inner in outer])


def zlati_prinasalec(skupine):
    people = set([inner for outer in skupine for inner in outer])
    max_ime = ""
    max_okuzb = 0
    for p in people:
        okuzenih = 0
        for s in skupine:
            if p in s:
                okuzenih += len(s) - 1
        if max_okuzb == okuzenih:
            max_okuzb = okuzenih
            max_ime = min(max_ime, p)
        elif max_okuzb < okuzenih:
            max_okuzb = okuzenih
            max_ime = p
    return max_ime


def korakov_do_vseh(skupine, prvi):
    skp = skupine[:]
    okuzbe = set([prvi])
    steps = 0
    encountered = set([prvi])
    while skp:
        new = []
        for s in skp[:]:
            for o in okuzbe:
                if o in s:
                    new += s
                    if s in skp:
                        skp.remove(s)
                new = list(set(new))
        okuzbe = set([n for n in new if n not in encountered])
        encountered = encountered.union(set(okuzbe))
        if len(new) == 0:
            break
        steps += 1
    else:
        return steps
    return None

import unittest
import random
import ast


class TestObvezna(unittest.TestCase):
    ime = "".join(random.choice("qwertyuiop") for _ in range(10))
    aktivnost = "".join(random.choice("asdfghjkl") for _ in range(10))
    rnd_obiski = [(ime, aktivnost)]

    obiski = [("Ana", "kava"), ("Berta", "kava"), ("Cilka", "telovadba"),
              ("Dani", "zdravnik"), ("Ana", "zdravnik"), ("Cilka", "kava"),
              ("Ema", "telovadba")]

    def test_osebe(self):
        self.assertEqual({"Ana", "Berta", "Cilka", "Dani", "Ema"}, osebe(self.obiski))
        self.assertEqual({self.ime}, osebe(self.rnd_obiski))
        self.assertEqual(set(), osebe([]))

    def test_aktivnosti(self):
        self.assertEqual({"zdravnik", "kava", "telovadba"}, aktivnosti(self.obiski))
        self.assertEqual({self.aktivnost}, aktivnosti(self.rnd_obiski))
        self.assertEqual(set(), aktivnosti([]))

    def test_udelezenci(self):
        self.assertEqual({"Ana", "Berta", "Cilka"}, udelezenci("kava", self.obiski))
        self.assertEqual(set(), udelezenci("sprehod", self.obiski))
        self.assertEqual({self.ime}, udelezenci(self.aktivnost, self.rnd_obiski))
        self.assertEqual(set(), udelezenci(self.aktivnost, []))

    def test_po_aktivnostih(self):
        self.assertEqual({
            "kava": {"Ana", "Berta", "Cilka"},
            "zdravnik": {"Ana", "Dani"},
            "telovadba": {"Cilka", "Ema"}},
            po_aktivnostih(self.obiski))
        self.assertEqual({self.aktivnost: {self.ime}}, po_aktivnostih(self.rnd_obiski))

    def test_skupine(self):
        def form(s):
            return sorted(s, key=lambda x: "-".join(sorted(x)))

        self.assertEqual(
            form([{"Ana", "Berta", "Cilka"}, {"Dani", "Ana"}, {"Cilka", "Ema"}]),
            form(skupine(self.obiski)))

        self.assertEqual(
            form([{self.ime}]),
            form(skupine([(self.ime, self.aktivnost)])))

    def test_okuzeni(self):
        skupine = [{"Ana", "Berta", "Cilka"}, {"Dani", "Ana"}, {"Cilka", "Ema"}, {"Fan??i"}]

        self.assertEqual({"Berta", "Cilka", "Dani"},
                         okuzeni(skupine, {"Ana"}))
        self.assertEqual({"Ana", "Berta", "Ema"},
                         okuzeni(skupine, {"Cilka"}))
        self.assertEqual({"Ana", "Cilka"},
                         okuzeni(skupine, {"Ema", "Dani"}))
        self.assertEqual({"Ana", "Ema"},
                         okuzeni(skupine, {"Cilka", "Berta"}))
        self.assertEqual({"Berta", "Cilka", "Dani"},
                         okuzeni(skupine, {"Ana", "Ema"}))
        self.assertEqual({"Cilka"},
                         okuzeni(skupine, {"Ema"}))
        self.assertEqual(set(),
                         okuzeni(skupine, {"Fan??i"}))
        self.assertEqual(set(),
                         okuzeni(skupine, set()))
        self.assertEqual({self.ime},
                         okuzeni([{self.ime, self.aktivnost}], {self.aktivnost}))

    def test_zlati_prinasalec(self):
        self.assertEqual(
            "Ana",
            zlati_prinasalec([{"Ana", "Berta", "Cilka"}, {"Dani", "Ana"}, {"Cilka", "Ema"}, {"Cilka"}]))
        self.assertEqual(
            "Cilka",
            zlati_prinasalec([{"Fan??i", "Berta", "Cilka"}, {"Dani", "Fan??i"}, {"Cilka", "Ema"}, {"Cilka"}]))
        self.assertEqual(
            "Cilka",
            zlati_prinasalec([{"Fan??i", "Berta", "Cilka"}, {"Dani", "Fan??i"}, {"Cilka", "Ema"}, {"Fan??i"}]))

    def test_korakov_do_vseh(self):
        skupine = [{"Cilka", "Ema", "Jana", "Sa??a"},
                   {"Ema"},
                   {"Fan??i", "Greta", "Sa??a"},
                   {"Greta", "Nina"},
                   {"Greta", "Olga", "Rebeka"},
                   {"Micka", "Ana", "Klara"},
                   {"Fan??i", "Iva", "Berta", "??pela"},
                   {"Klara", "Cilka", "Dani"},
                   {"Petra", "Dani", "Lara", "??pela"}]
        self.assertEqual(5, korakov_do_vseh(skupine, "Ana"))
        self.assertEqual(4, korakov_do_vseh(skupine, "Klara"))
        self.assertEqual(4, korakov_do_vseh(skupine, "Dani"))
        self.assertEqual(3, korakov_do_vseh(skupine, "Ema"))

        skupine.append({"Tina"})
        self.assertIsNone(korakov_do_vseh(skupine, "Ema"))
        self.assertIsNone(korakov_do_vseh(skupine, "Tina"))
        skupine[-1].add("Ur??a")
        skupine[-1].add("Vesna")
        skupine.append({"Zala", "??ana"})
        self.assertIsNone(korakov_do_vseh(skupine, "Ema"))
        self.assertIsNone(korakov_do_vseh(skupine, "Tina"))


class TestOneLineMixin:
    functions = {elm.name: elm
                 for elm in ast.parse(open(__file__, "r", encoding="utf-8").read()).body
                 if isinstance(elm, ast.FunctionDef)}

    def assert_is_one_line(self, func):
        func
        body = self.functions[func.__code__.co_name].body
        self.assertEqual(len(body), 1, "\nFunkcija ni dolga le eno vrstico")
        self.assertIsInstance(body[0], ast.Return, "\nFunkcija naj bi vsebovala le return")

    def test_nedovoljene_funkcije(self):
        dovoljene_funkcije = {
            "preberi_zapiske", "osebe", "aktivnosti", "udelezenci",
            "po_aktivnostih", "skupine", "okuzeni", "zlati_prinasalec",
            "korakov_do_vseh"}
        for func in self.functions:
            self.assertIn(func, dovoljene_funkcije, f"\nFunkcija {func} ni dovoljena.")


class TestDodatna(unittest.TestCase, TestOneLineMixin):
    def test_oneline(self):
        for func in (osebe, aktivnosti, udelezenci, po_aktivnostih, skupine):
            self.assert_is_one_line(func)


class TestIzziv(unittest.TestCase, TestOneLineMixin):
    def test_oneline(self):
        for func in (okuzeni, zlati_prinasalec):
            self.assert_is_one_line(func)


if __name__ == "__main__":
    unittest.main()
