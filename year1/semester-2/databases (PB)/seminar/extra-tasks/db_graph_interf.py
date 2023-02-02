import pyodbc
import pyqtgraph as pg


class DataBaseGraphingInterface:
    # Razred, ki vsebuje metode, potrebne za izvedbo 5. naloge
    # Za pravilno izvedbo kličemo metode v sledečem vrstnem redu:
    # 1. prepare_data() -> pripravimo potrebne podatke za grafe
    # 2. draw() -> izrišemo grafe

    def __init__(self, db):
        # Pripravimo okno za grafe ter povezavo na bazo
        self.window_pop = pg.plot()
        self.window_ple = pg.plot()
        self.window_pop.setWindowTitle("Prikaz grafa Gostota Populacije")
        self.window_ple.setWindowTitle("Prikaz grafa Gostota Plemena")

        self.window_pop.setLabel(
            "bottom", "<p style='font-size:15px;color:white'>Obmocje</p>")
        self.window_pop.setLabel(
            "left", "<p style='font-size:15px;color:white'>Gostota</p>")

        self.window_ple.setLabel(
            "bottom", "<p style='font-size:15px;color:white'>Obmocje</p>")
        self.window_ple.setLabel(
            "left", "<p style='font-size:15px;color:white'>Gostota</p>")

        self.xy_pop = dict()
        self.xy_ple = dict()

        self.connection_string = "DRIVER={MySQL ODBC 8.0 ANSI Driver};SERVER=localhost;DATABASE=" + \
            db + ";UID=pb;PWD=pbvaje"
        self.conn = pyodbc.connect(self.connection_string)

    def _prepare_data(self):
        # Ta metoda naj se ne bi klicala ročno zunaj razreda, da jo pokličemo uporabimo metodo draw

        # Ustvarimo kurzorja za branje podatkov iz baze
        populacija_cursor = self.conn.cursor()
        pleme_cursor = self.conn.cursor()

        # Pripravimo niza za branje podatkov
        pop_string = "SELECT * FROM gostotaPopulacije"
        ple_string = "SELECT * FROM gostotaPlemena"

        # Preberemo podatke o gostoti populacije
        populacija_cursor.execute(pop_string)

        for r in populacija_cursor:
            # Podatke shranimo v slovar
            self.xy_pop[float(r[0])] = float(r[1])

        # Preberemo podatke o gostoti plemena
        pleme_cursor.execute(ple_string)

        for r in pleme_cursor:
            # Podatke shranimo v slovar
            self.xy_ple[float(r[0])] = float(r[1])

    def _plot_populacija(self):
        # Ta metoda naj se ne bi klicala ročno zunaj razreda, da jo pokličemo uporabimo metodo draw

        # Ustvarimo graf z ustreznimi podatki
        bargraph_pop = pg.BarGraphItem(x=list(self.xy_pop.keys()), height=list(
            self.xy_pop.values()), width=1, brush="b")

        # Dodamo graf ustreznemu oknu
        self.window_pop.addItem(bargraph_pop)

    def _plot_pleme(self):
        # Ta metoda naj se ne bi klicala ročno zunaj razreda, da jo pokličemo uporabimo metodo draw

        # Ustvarimo graf z ustreznimi podatki
        bargraph_ple = pg.BarGraphItem(x=list(self.xy_ple.keys()), height=list(
            self.xy_ple.values()), width=0.2, brush="b")

        # Dodamo graf ustreznemu oknu
        self.window_ple.addItem(bargraph_ple)

    def draw(self):
        # Metoda za pripravo podatkov in izris grafov

        self._prepare_data()
        self._plot_populacija()
        self._plot_pleme()
