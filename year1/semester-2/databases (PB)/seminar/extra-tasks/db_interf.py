import pyodbc


class DataBaseInterface:
    # Razred, ki vsebuje metode, potrebne za izvedbo naloge 4
    # Za pravilno izvedbo kličemo metode v sledečem vrstnem redu:
    # 1. prepare_tabele() -> pripravimo potrebne tabele
    # 2. calculate_gostote(pleme) -> izračunamo gostote in napolnimo tabele
    # 3. display_gostote() -> prikažemo tabele uporabniku

    def __init__(self, db):
        # Inicializiramo spremenljivko conn (tip pyodbc.connection), za uporabo v drugi metodah razreda

        self.connection_string = "DRIVER={MySQL ODBC 8.0 ANSI Driver};SERVER=localhost;DATABASE=" + \
            db + ";UID=pb;PWD=pbvaje"
        self.conn = pyodbc.connect(self.connection_string)

        self.pleme = ""

    def _create_gostota_populacije(self):
        # Ta metoda naj se ne bi klicala ročno zunaj razreda, da jo pokličemo uporabimo metodo prepare_tabele

        # Ustvarimo kurzor, uporabljen za kreiranje tabele gostotaPopulacije
        create_cursor = self.conn.cursor()

        try:
            # Če tabela že obstaja, jo izbiršemo
            create_cursor.execute("DROP TABLE IF EXISTS gostotaPopulacije")
        except pyodbc.DataError:
            pass

        # Pripravimo niz za kreiranje tabele
        create_string = "CREATE TABLE gostotaPopulacije (" \
                        "   id INTEGER AUTO_INCREMENT PRIMARY KEY," \
                        "   gostota DOUBLE NOT NULL" \
                        ")"

        # Ukaz izvedemo, ustvarimo tabelo
        create_cursor.execute(create_string)
        create_cursor.commit()  # Shranimo spremembe

        print("Table gostotaPopulacije created!")

    def _create_gostota_plemena(self):
        # Ta metoda naj se ne bi klicala ročno zunaj razreda, da jo pokličemo uporabimo metodo prepare_tabele

        # Ustvarimo kurzor, uporabljen za kreiranje tabele gostotaPopulacije
        create_cursor = self.conn.cursor()
        try:
            # Če tabela že obstaja, jo izbiršemo
            create_cursor.execute("DROP TABLE IF EXISTS gostotaPlemena")
        except pyodbc.DataError:
            pass

        # Pripravimo niz za kreiranje tabele
        create_string = "CREATE TABLE gostotaPlemena (" \
                        "   id INTEGER AUTO_INCREMENT PRIMARY KEY," \
                        "   gostota DOUBLE NOT NULL" \
                        ")"

        # Ukaz izvedemo, ustvarimo tabelo
        create_cursor.execute(create_string)
        create_cursor.commit()  # Shranimo spremembe

        print("Table gostotaPlemena created!")

    def prepare_tabele(self):
        # Ta metoda se uporabi, da pripravimo tabele

        self._create_gostota_populacije()
        self._create_gostota_plemena()

    def _calculate_gostota_populacije(self):
        # Ta metoda naj se ne bi klicala ročno zunaj razreda, da jo pokličemo uporabimo metodo calculate_gostota

        # Ustvarimo kurzorja za branje in pisanje v tabele
        select_cursor = self.conn.cursor()
        insert_cursor = self.conn.cursor()

        # Pripravimo niz za branje vsote populacije na določenem območju
        select_string = "SELECT SUM(n.population) " \
                        "FROM naselje n " \
                        "WHERE n.x BETWEEN ? AND ? " \
                        "  AND n.y BETWEEN ? AND ? "

        # Pripravimo niz za pisanje prebrane gostote v tabelo gostotaPopulacije
        insert_string = "INSERT INTO gostotaPopulacije (gostota) VALUES(?)"

        # V zanki skačemo iz kvadrata v kvadrat (premik za 10, saj so kvadrati velikosti 10x10)
        # Končamo z 240 zato, da ne prekoračimo meje (v tabelo ne zapišemo prazne vrstice)
        for x in range(-250, 250, 10):
            for y in range(-250, 250, 10):
                # Preberemo vsoto populacije za dani kvadrat (1 vrednost)
                select_cursor.execute(select_string, x, x + 10, y, y + 10)

                # Ker poizvedba vrača 1 vrednost, jo shranimo v val
                val = select_cursor.fetchval()

                # Če na območju ni naselji, se val nastavi na 0 (SUM vrne Null)
                if val is None:
                    val = 0

                # V tabelo gostot zapišemo gostoto (populacija / površina)
                insert_cursor.execute(insert_string, int(val) / 100)
                insert_cursor.commit()  # Shranimo spremembe

        print("Table gostotaPopulacije filled!")

    def _calculate_gostota_plemena(self, pleme):
        # Ta metoda naj se ne bi klicala ročno zunaj razreda, da jo pokličemo uporabimo metodo calculate_gostota

        # Ustvarimo kurzorja za branje in pisanje v tabele
        select_cursor = self.conn.cursor()
        insert_cursor = self.conn.cursor()

        # Pripravimo niz za branje vsote populacije na določenem območju, za določeno pleme
        select_string = "SELECT SUM(n.population)" \
                        "FROM naselje n INNER JOIN igralec i USING(pid)" \
                        "INNER JOIN pleme p USING(tid)" \
                        "WHERE n.x BETWEEN ? AND ?" \
                        "  AND n.y BETWEEN ? AND ?" \
                        "  AND p.tribe = ?"

        # Pripravimo niz za pisanje prebrane gostote v tabelo gostotaPlemena
        insert_string = "INSERT INTO gostotaPlemena (gostota) VALUES(?)"

        # V zanki skačemo iz kvadrata v kvadrat (premik za 10, saj so kvadrati velikosti 10x10)
        # Končamo z 250 zato, da ne prekoračimo meje (v tabelo ne zapišemo prazne vrstice)
        for x in range(-250, 250, 10):
            for y in range(-250, 250, 10):
                # Preberemo vsoto populacije za dani kvadrat (1 vrednost)
                select_cursor.execute(
                    select_string, x, x + 10, y, y + 10, pleme)

                # Ker poizvedba vrača 1 vrednost, jo shranimo v val
                val = select_cursor.fetchval()

                # Če na območju ni naselji, se val nastavi na 0 (SUM vrne Null)
                if val is None:
                    val = 0

                # V tabelo gostot zapišemo gostoto (populacija / površina)
                insert_cursor.execute(insert_string, val / 100)
                insert_cursor.commit()  # Shranimo spremembe

        print("Table gostotaPlemena filled!")

    def calculate_gostote(self, pleme):
        # Ta metoda se uporabi, da populiramo tabele

        self.pleme = pleme  # Shranimo podatek, za katero pleme smo izračunali konfiguracijo

        self._calculate_gostota_populacije()
        self._calculate_gostota_plemena(pleme)

    def _display_gostota_populacije(self):
        # Ta metoda naj se ne bi klicala ročno zunaj razreda, da jo pokličemo uporabimo metodo display_gostote

        # Ustvarimo kurzor za branje podatkov
        select_cursor = self.conn.cursor()

        # Pripravimo niz za branje podatkov iz tabele gostotaPopulacije
        select_string = "SELECT * FROM gostotaPopulacije"

        # Izvedemo poizvedbo
        select_cursor.execute(select_string)

        print("\nGostota populacije: \nid | gostota")
        for r in select_cursor:
            print(' '.join([str(col) for col in r]).replace(
                " ", "  | "))  # Izpišemo podatke

    def _display_gostota_plemena(self):
        # Ta metoda naj se ne bi klicala ročno zunaj razreda, da jo pokličemo uporabimo metodo display_gostote

        # Ustvarimo kurzor za branje podatkov
        select_cursor = self.conn.cursor()

        # Pripravimo niz za branje podatkov iz tabele gostotaPopulacije
        select_string = "SELECT * FROM gostotaPlemena"

        # Izvedemo poizvedbo
        select_cursor.execute(select_string)

        print("\nGostota plemena " + self.pleme + ": \nid | gostota")
        for r in select_cursor:
            print(' '.join([str(col) for col in r]).replace(
                " ", "  | "))  # Izpišemo podatke

    def display_gostote(self):
        # Ta metoda se uporabi, da prikažemo tabele

        self._display_gostota_populacije()
        self._display_gostota_plemena()
