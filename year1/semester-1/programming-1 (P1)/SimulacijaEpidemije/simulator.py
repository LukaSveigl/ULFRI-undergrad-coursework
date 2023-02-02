# Tu dodajte svoje razrede

import risar
import random as rd
from math import sin, cos, radians, sqrt

class NIJZ:
    def __init__(self):
        self.okuzenih = 0
        self.ozdravljenih = 0

        self.prev = risar.maxY

        self.x_okuzbe = 0
        self.y_okuzbe = risar.maxY

        self.x_ozdravljeni = 0
        self.y_ozdravljeni = risar.maxY

    def sproci_okuzbo(self):
        self.okuzenih += 1

    def sporoci_ozdravitev(self):
        self.okuzenih -= 1
        self.ozdravljenih += 1

    def porocaj(self):
        risar.crta(self.x_okuzbe, self.y_okuzbe, self.x_okuzbe + 5, risar.maxY - self.okuzenih, barva=risar.rdeca)
        self.x_okuzbe += 5
        self.y_okuzbe = risar.maxY - self.okuzenih

        risar.crta(self.x_ozdravljeni, self.y_ozdravljeni, self.x_ozdravljeni + 5, risar.maxY - self.ozdravljenih, barva=risar.zelena)
        self.x_ozdravljeni += 5
        self.y_ozdravljeni = risar.maxY - self.ozdravljenih

nijz = NIJZ()

class Oseba:
    def __init__(self):
        x, y = risar.nakljucne_koordinate()
        self.hitrost = rd.uniform(0, 5)
        self.smer = rd.uniform(-180, 180)
        self.krog = risar.krog(x, y, 4, barva=risar.bela, sirina=1)
        self.okuzen = False
        self.ozdravljen = False
        self.zdravljenje = 0
        self.izolacija = False
        self.cas_v_izolaciji = 0

    def premik(self, osebe):
        found = False
        if not self.izolacija:
            kot = radians(90 - self.smer)
            nx, ny = self.krog.x() + self.hitrost * cos(kot), self.krog.y() - self.hitrost * sin(kot)

            for o in osebe:
                if o.je_izolirana():
                    diff = sqrt(((nx - o.krog.x()) ** 2) + ((ny - o.krog.y()) ** 2))
                    if diff < 20:
                        if self.smer > 0:
                            self.smer = self.smer - 180
                            found = True
                        elif self.smer < 0:
                            self.smer = self.smer + 180
                            found = True

            if nx <= 0:
                nx = 0
                if 0 < ny < risar.maxY:
                    if -90 <= self.smer < 0:
                        self.smer = -self.smer
                    else:
                        self.smer = self.smer + 90
            elif nx >= risar.maxX:
                nx = risar.maxX
                if 0 < ny < risar.maxY:
                    if 0 < self.smer < 90:
                        self.smer = -self.smer
                    else:
                        self.smer = -90 - (self.smer - 90)
            elif ny < 0:
                ny = 0
                if self.smer > 0:
                    self.smer = self.smer + 90
                else:
                    self.smer = self.smer - 90
            elif ny > risar.maxY:
                ny = risar.maxY
                if self.smer > 0:
                    self.smer = self.smer - 90
                else:
                    self.smer = 180 - self.smer
            else:
                self.smer += rd.uniform(-20, 20)

            if not found:
                self.krog.setX(nx)
                self.krog.setY(ny)
        else:
            self.cas_v_izolaciji += 1

        if self.cas_v_izolaciji == 100:
            self.izolacija = False
            self.cas_v_izolaciji = 0
            risar.zapolni(self.krog, risar.crna)

    def okuzi_se(self):
        if not self.ozdravljen and not self.izolacija and not self.okuzen:
            risar.spremeni_barvo(self.krog, risar.rdeca)
            self.okuzen = True
            nijz.sproci_okuzbo()

    def okuzi_bliznje(self, osebe):
        for o in osebe:
            diff = sqrt(((self.krog.x() - o.krog.x()) ** 2) + ((self.krog.y() - o.krog.y()) ** 2))
            if self.krog.x() != o.krog.x() and self.krog.y() != o.krog.y():
                if self.okuzen:
                    if 0 <= diff <= 8:
                        o.okuzi_se()

    def zdravi_se(self):
        if self.okuzen and not self.ozdravljen:
            self.zdravljenje += 1
            if self.zdravljenje >= 150:
                self.ozdravljen = True
                self.okuzen = False
                risar.spremeni_barvo(self.krog, risar.zelena)
                nijz.sporoci_ozdravitev()

    def vrni_krog(self):
        return self.krog

    def v_izolacijo(self):
        risar.zapolni(self.krog, risar.rumena)
        self.izolacija = True

    def je_izolirana(self):
        return self.izolacija


# Vse od tod naprej pustite pri miru

import risar


def main():
    # Tole poskrbi, da razred navidez dobi metode, ki jih še nisi sprogramiral(a)
    # Tega ni potrebno razumeti. Ignoriraj.

    from unittest.mock import Mock
    from itertools import count

    if hasattr(Oseba, "premik") and Oseba.premik.__code__.co_argcount == 1:
        Oseba.premik = lambda self, osebe, f=Oseba.premik: f(self)

    for method in ("premik", "okuzi_se", "okuzi_bliznje", "zdravi_se"):
        if not hasattr(Oseba, method):
            setattr(Oseba, method, Mock())

    globals().setdefault("nijz", None)

    osebe = [Oseba() for _ in range(100)]
    for oseba in osebe[:5]:
        oseba.okuzi_se()
    for oseba in osebe:
        if hasattr(Oseba, "vrni_krog") and hasattr(Oseba, "v_izolacijo"):
            oseba.vrni_krog().setOnClick(oseba.v_izolacijo)

    for cas in count():
        for oseba in osebe:
            oseba.zdravi_se()
            oseba.okuzi_bliznje(osebe)
            oseba.premik(osebe)
        if nijz and cas % 10 == 0:
            nijz.porocaj()
        risar.cakaj(0.02)


main()
