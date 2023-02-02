import db_interf
import db_graph_interf
import sys
from pyqtgraph.Qt import QtCore, QtGui

naloga = input("Zaženemo nalogo 4 ali pet - izberite med [4, 5]: ")

# Dodano zato, ker sem delal svojo seminarsko v shemi z imenom "seminarska", kar je lahko drugace
# kot ime vase baze
database = input("\nVnesite ime podatkovne baze(sheme): ")

if naloga == "4":

    print("\nSkripta za izračun gostot populacij v kvadratih velikosti 10x10 \n")

    # Pridobi ime plemena, sproti preverjaj če je ime pravilno
    while (True):
        pleme = input(
            "Vnesite želeno pleme - izberite med [Huni, Tevtoni, Rimljani, Galci, Narava, Natarji, Egipcani]: ")

        if pleme in {"Huni", "Tevtoni", "Rimljani", "Galci", "Narava", "Natarji", "Egipcani"}:
            break

    # Ustvari instanco razreda DataBaseInterface
    db_interface = db_interf.DataBaseInterface(database)

    # Pripravi tabele ter izračunaj gostote
    db_interface.prepare_tabele()
    db_interface.calculate_gostote(pleme)

    display = input("\nŽelite prikazati tabele - izberite med [y, n]: ")

    if display == "y":
        db_interface.display_gostote()

else:
    # Ustvari instanco razreda DataBaseGraphingInterface
    db_grapher = db_graph_interf.DataBaseGraphingInterface(database)

    # Nariši grafe
    db_grapher.draw()

    # Zaženi Qt event loop, razen če je način sistema interaktiven ali QtCore ne obstaja
    if(sys.flags.interactive != 1) or not hasattr(QtCore, 'PYQT_VERSION'):
        QtGui.QApplication.instance().exec_()
