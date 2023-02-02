# Analiza področja energetike v Sloveniji

V sklopu projekta smo si izbrali analizo področja energetike v Sloveniji.
Za to področje smo se odločili, ker se z njim v sodobni družbi soočamo na vsakem
koraku in je vedno bolj pomembno ter prisotno. Bolj natančno bomo analizirali
ceno energentov, ter razne odvisnosti med energenti.

# Glavni cilji/vprašanja projekta
- `Primerjava cen električne energije`: Zanima nas primerjava cen električne energije med gospodinjskimi in negospodinjskimi odjemalci ter močjo priklopa.
- `Morebitna korelacija cene zemeljskega plina, električne energije in pogonskih goriv`: Zanima nas, ali obstaja relacija oz. odvisnost med cenami
  zemeljskega plina, električne energije in pogonskih goriv glede na morebitne dobavne oz. geopolitične krize.
- `Analiza energetske bilance`: Zanima nas rast porabe energije glede na panogo.
- `Analiza proizvodnje električne energije`: Zanima nas rast dejanske moči in proizvodnje energije glede na vir, uvoz in izvoz, količina izgub, vsebnost ogljika 
  v oskrbi z energijo.

# Podatkovne zbirke

Nekatere zbirke bomo iz mesečnih združili v letne, ker nas zanima analiza čez daljše časovno obdobje.
Pri cenah energentov bomo uporabili četrtletne podatke, saj nam to omogoča da v pregled vključimo tudi
druge dejavnike, kot so letni čas.

## Viri podatkov

Vse podatkovne zbirke smo pridobili iz spletne strani [OPSI: Odprtokodni podatki Slovenije](https://podatki.gov.si)
oziroma [SiStat: Statistični urad Republike Slovenije](https://pxweb.stat.si/SiStat/sl).

- `Energetski kazalnik: letno`: zbirka dostopna na: [Energetski kazalnik](https://pxweb.stat.si/SiStatData/pxweb/sl/Data/-/1817902S.px)
- `Proizvodnja in dejanska moč v elektrarnah: letno`: zbirka dostopna na: [Proizvodnja in moč](https://pxweb.stat.si/SiStatData/pxweb/sl/Data/-/1817604S.px)
- `Energetska bilanca (TJ): letno`: zbirka dostopna na: [Energetska bilanca](https://pxweb.stat.si/SiStatData/pxweb/sl/Data/-/1817903S.px)
- `Električna energija (GWh): mesečno`: zbirka dostopna na: [Električna energija](https://pxweb.stat.si/SiStatData/pxweb/sl/Data/-/1817601S.px)
- `Cene energentov: četrtletno`: zbirka dostopna na: [Cene energentov](https://pxweb.stat.si/SiStatData/pxweb/sl/Data/-/H028S.px)  
- `Cene električne energije za negospodinjske odjemalce (EUR/kWh): četrtletno`: zbirka dostopna na: [Cene električne energije za negospodinjske odjemalce](https://pxweb.stat.si/SiStatData/pxweb/sl/Data/-/1817520S.px)  
- `Cene električne energije za gospodinjske odjemalce (EUR/kWh): četrtletno`: zbirka dostopna na: [Cene električne energije za gospodinjske odjemalce](https://pxweb.stat.si/SiStatData/pxweb/sl/Data/-/1817515S.px)  

## Oblika podatkov

Podatkovne zbirke smo pridobili v formatu `.csv`, posamezne zbirke pa spremlja tudi pojasnilo in metodološki pristopi.
