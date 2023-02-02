Program najprej uporabnika povpraša po imenu, ki mora biti enako imenu certifikata.
Ko strežnik sprejme povezavo, iz polja commonName prebere ime in z njim poveže socket uporabnika.
V nadaljevanju se uporablja commonName za identifikacijo.

Uporabniška imena prijavljenih uporabnikov se vsa shranjujejo v VELIKIH ČRKAH,
ampak jih v privatnih sporočilih lahko naslavljamo tudi z malimi, saj se na strežniku
zgodi pretvorba v velike.

Privatno sporočilo pošljemo z sintakso 
	#PRIVATEMSG uporabnik sporočilo
kjer uporabnik zamenjamo z željenim uporabiškim imenom, v polje sporočilo pa napišemo naše sporočilo.
