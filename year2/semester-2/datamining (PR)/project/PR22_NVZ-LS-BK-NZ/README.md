# Analiza energetike v Sloveniji
## Projektna naloga pri predmetu Podatkovno rudarjenje

Pri projektu za predmet Podatkovno rudarjenje smo analizirali področje energetike v Sloveniji. Za to temo smo se odločili, ker je energetika vse bolj pomembna v sodobni družbi, osredotočili pa smo se na analizo cen energentov ter odvisnosti med njimi.
<br><br>
Pridobljene zbirke cen energentov skozi čas smo prilagodili za opazovanje cen skozi četrtletja, kar nam je omogočalo v primerjavi uporabiti tudi druge dejavnike kot so letni časi.
<br><br>
V času pisanja vmesnega poročila smo odgovorili na 2 od 4ih zastavljenih vprašanj, bolj natančno, primerjali smo cene el. energije ter poiskali morebitne korelacije cen zemeljskega plina, el. energije in pogonskih goriv.
<br><br>
Koda, uporabljena za odgovarjanje na vprašanja je dostopna v Jupyter notebook formatu v mapi [src](src), za branje smo uporabili naš pomožni modul [data_extraction.py](src/reading/data_extraction.py), prav tako pa smo uporabljali tudi modul z pomožnimi funkcijami [misc_utils.py](src/misc_utils.py).


## 1. Primerjava cen električne energije

Pri tem vprašanju smo primerjali cene el. energije za različne standardne porabniške skupine pri gospodinjskih in negospodinjskih odjemalcih. Vsa koda, uporabljena za odgovarjanje na to vprašanje je dostopna na [price_comparsion.ipynb](src/price_comparison.ipynb).


### 1.1 Pregled podatkov

Vprašanja smo se lotili tako, da smo najprej pregledali podatke iz podatkovnih množic [Cene el. energije za gospodinjske odjemalce](data/Cene%20el.%20energije%20za%20gospodinjske%20odjemalce.csv) in [Cene el. energije za negospodinjske odjemalce](data/Cene%20el.%20energije%20za%20negospodinjske%20odjemalce.csv), kar smo storili z izpisom opisa podatkovnega okvirja ter nekaj osnovnimi grafi. 
<br><br>
Ugotovili smo, da podatkov ni treba čistiti, saj so podani v razumljivem in izpopolnjenem formatu, z izrisom grafov pa smo ugotovili, da imajo odjemalci z manjšimi močmi priklopov višje cene el. energije kot odjemalci z močnejšimi priklopi. Prav tako smo opazili, da imajo negospodinjski odjemalci več standardnih porabniških skupin ter kot pričakovano, večje priklopne moči v primerjavi z gospodinjski odjemalci, in da je razpon med cenami električne energije negospodinjskih odjemalcev veliko manjši.
<br>
![Primerjava cen gospodinjskih in negospodinjskih odjemalcev](resources/Primerjava%20cen%20priklopov%20-%20gospodinjstva%20negospodinjstva.png)


### 1.2 Analiza gospodinjskih odjemalcev

Nato smo se osredotočili na gospodinjske odjemalce. Ob podrobnejšem pregledu grafa cen različnih standardnih porabniških skupin gospodinjskih odjemalcev skozi četrtletja, smo v letih 2017 in 2018 opazili zelo velik porast cen pri standardni porabniški skupini DA, zaradi česar nas je zanimal bolj natančen pregled vseh faktorjev, ki so vplivali na končno ceno. Prav tako smo opazili velik padec v cenah vseh porabniških skupin v drugem četrtletju leta 2020.
<br><br>
Med iskanjem vzrokov za padec cen v drugem četrtletju leta 2020, smo ugotovili, da se je [aprila tistega leta poraba el. energije zmanjšala za 19% v primerjavi z marcem](https://www.stat.si/statweb/News/Index/8852) in 15% v primerjavi z aprilom 2019. Manjši proizvodnji električne energije je sledila tudi manjša proraba goriv. Skupna poraba goriv aprila 2020 je bila za 19% manjša kot aprila 2019. Prav tako se je zmanjšala tudi oskrba z motornim (za 43%) in dizelskim gorivom (za 65%), kar je bilo večinoma posledica ukrepov proti COVID-19.


#### 1.2.1 Analiza porasta cen standardne porabniške skupine DA leta 2017/2018

Odločili smo se podrobneje analizirati porast cen standarnde porabniške skupine DA zadnjega četrletja 2016 ter med leti 2017 in 2018, saj so te, kot je razvidno iz grafa, v tem času močno narasle. Izpisali smo razlike cen ter procentualne razlike cen zadnjega četrletja 2016 ter četrtletnimi cenami med letoma 2017 in 2018 v primerjavi z istočasnimi cenami četrtletji standarnde porabniške skupine DE, ki je imela najnižje in relativno stabilne cene.
<br><br>
Iz izpisa smo opazili  da je cena električne energije standardne porabniške skupine DA v primerjavi z standardno porabniško skupino DE iz leta 2016 na leti 2017/2018 narasla za približno 87%. Že v zadnjem četrtletju 2016 sta se ceni razlikovali za 46% (0.0575€), nato pa je njuna razlika narasla na povprečno 133%.
<br><br>
Ob preverjanju možnih dejavnikov, ki bi lahko vplivali na višje cene, smo ugotovili, da je se je v letu 2018 zgodil porast cen energetskih surovin ter [da je upadla domača proizvodnja, kar je dvignilo ceno zemeljskega plina](https://www.gzs.si/Portals/SN-informacije-Pomoc/Vsebine/GG/december-2018/65.pdf). Prav tako se je v Angliji zaprlo največje skladišče zemeljskega plina.
<br>
![Razčlenitev cene električne energije skupine DA(<1000KWh)](resources/Razclenitev%20-%20DA.png)


### 1.3 Analiza negospodinjskih odjemalcev

Podobno smo se osredotočili še na negospodinjske odjemalce. Ob pregledu grafa sprememb cen negospodinjskih odjmealcev smo opazili tri zanimivosti, to so porast cen električne energije v prvem četrtletju 2013 za odjemalsko skupino IB (20 do <500 MWh), padec cen v drugem četrtletju 2020 in naraščanje cen med drugim in četrtim četrtletjem 2021 pri vseh standardnih porabniških skupinah, razen pri skupini IA (<20 MWh). Opazili smo tudi, da so cene standardne porabniške skupine IA (<20 MWh) veliko višje kot cene ostalih skupin.
<br><br>
Padec cen v letu 2020 lahko kot pri cenah gospodinjskih odjemalcev pojasnimo z prvim valom COVID-19, naraščanje cen v prvem četrtletju 2013 pa smo poskusili obrazložiti v nadaljevanju [(poglavje 1.3.2)](#132-analiza-porasta-cen-električne-energije-skupine-ib-leta-2013).

#### 1.3.1 Analiza razlik v ceni med različnimi standardnimi porabniškimi skupinami

Ob izračunu razlik cen različnih standardnih porabniških skupin negospodinjskih odjemalcev v primerjavi z skupino IF ter prikazu teh vrednosti s procenti, smo opazili, da se cene porabniške skupine IA močno razlikujejo od ostalih skupin (predpostavili smo 2 oziroma 3 možne gruče). Zato smo se odločili, da bomo na odstopanje vrednosti cen skupine IA odgovorili z gručenjem. Z hierarhičnim gručenjem smo pokazali, da se standardne porabniške skupine delijo v 2 gruči, in sicer skupina IA, ter vse ostale. Rezultat je potrdil tudi koeficient silhuete, ki je znašal okoli 0.55, kar nakazuje na dokaj dobro gručenost. To pomeni, da je skupina IA drugačna od ostalih, zaradi česar morda ni tako povezana z spremembami cen ostalih skupin.
<br>
![Dendrogram gručenja standardnih porabniških skupin](resources/Negospodinjsko%20grucenje.png)

#### 1.3.2 Analiza porasta cen električne energije skupine IB leta 2013

Nato smo analizirali porast cen električne energije skupine IB leta 2013. Ponovno smo razčlenili cene električne energije omenjene skupine na posamezne faktorje cene in opazili da je leta 2013 močno narasla cena omrežnine, rahlo pa so se dvignili tudi dodatki in prispevki. Omenjena faktorja sta vplivala na višino cene brez davkov in z davki, kar potrjujejo tudi na [Ministrstvu za infrastukturo - Portal energetika](https://www.energetika-portal.si/nc/novica/n/v-zadnjem-cetrtletju-2013-znizanje-cen-elektricne-energije-za-gospodinjstva-in-industrijo-2620/). Razlog za višanje cen te skupine so bile torej povišane cene omrežnine in prispevkov za zagotavljanje podpor proizvodnji električne energije.


### 1.4 Analiza razčlenitve cen električne energije gospodinjskih in negospodinjskih odjemalcev

Prvo vprašanje smo zaključili z analizo razčlenitve cen el. energije pri gospodinjskih in negospodinjskih odjemalcih ter primerjali, koliko plača povprečno slovensko gospodinjstvo v primerjavi z negospodinjskimi odjemalci.
<br><br>
Izračunali smo povprečno razčlenitev cen gospodinjskih standardnih porabniških skupin DA in DB, ter povprečno razčlenitev cen el. energije vseh negospodinjskih odjemalcev. Pri gospodinjskih odjemalcih smo vzeli skupini DA in DB zato, ker le ti predstavljata standardno slovensko gospodinjstvo. Nato smo izpisali najmanjše, največje in povprečne oblike cen električne energije, ločeno za gospodinjske in negospodinjske odjemalce ter jih prav tako za lažje primerjanje med skupinami odjemalcev prikazali v stolpičnih diagramih.
<br><br>
Ugotovili smo, da povprečen gospodinjski odjemalec v vseh primerih plača za električno energijo več, kot negospodinjski odjemalec. Predvidevamo, da je razlog za to višja moč priklopa pri negospodinjskih odjemalcih, zaradi česar porabijo dosti več elektrike.
<br>
![Primerjava cen el. energije gospodinjskih in negospodinjskih odjemalcev](resources/Gospodinjstva%20-%20negospodinjstva%20-%20primerjava.png)

## 2. Iskanje morebitnih korelacij v cenah energentov

Pri tem vprašanju snas je zanimalo, ali obstaja odvisnost med cenami zemeljskega plina, električne enrgije in pogonskih goriv. Vsa koda, uporabljena za odgovarjanje na to vprašanje je dostopna na [fuel_correlation.ipynb](src/fuel_correlation.ipynb).


### 2.1 Pregled podatkov

Tudi tega vprašanja smo se lotili tako, da smo najprej pregledali podatke iz podatkovne množice [Cene energentov](data/Cene%20energentov.csv), kar smo storili z izpisom opisa podatkovnega okvirja ter grafom, ki prikazuje cene energentov v četrtletnih intervalih.
<br><br>
Iz opisa podatkovnega okvirja smo opazili, da povprečje močno kvarijo visoke številke. Po podrobnejšem pregledu podatkovne množice smo ugotovili, da mere vrednosti energentov niso standardizirane, bolj natančno, vrednosti energenta Kurilno olje so zapisane kot EUR/1000l, medtem ko so pri ostalih pogonskih gorivih zapisane kot EUR/l. Zaradi tega smo morali podatke ob branju popraviti, tako da smo cene kurilnega olja delili s 1000, s čimer smo jih pretvorili v mero EUR/l.
<br>
![Primerjava energentov](resources/Primerjava%20energentov.png)


### 2.2 Iskanje korelacije v podatkih

Nato smo želeli najti potencialne odvisnosti v ceni različnih energentov. To smo storili tako da smo najprej zračunali pearsonove koeficiente korelacije ter p-vrednosti za kombinacije atributov podatkovne množice, ki so se nam zdeli smiselni oziroma zanimivi.
<br><br>
Pri tem smo ugotovili, da cene pogonskih goriv, kurilnega olja in zemeljskega plina korelirajo, kar je smiselno, saj gre za energente, ki spadajo v skupino foslinih goriv. Bencin, dizel in pa kurilno olje se pridobivajo iz nafte zato njihova cena seveda močno korelira (bencin in dizel imata pearsonov koeficient korelacije 0.90, dizel in kurilno olje pa 0.93) in je verjetno tudi močno odvisna od cene nafte.
<br><br> 
Prav tako smo ugotovili, da cene pogonskih goriv in električne energije ne korelirajo v primeru gospodinjskih odjemalcev, pač pa korelirajo v primeru negospodinjskih odjemalcev (pearsonov koeficietn korelacije je namreč 0.61). Sklepamo da, zato ker se pogonska goriva ne uporabljajo pri proizvodnji električne energije ali ogrevanju (z izjemo kurilnega olja, kar pa ne spremeni rezultatov, saj je pearsonov koeficient korelacije cene olja in električne energije gospodinjskih odjemalcev 0.064).
<br><br>
Ugotovili smo tudi, da cene zemeljskega plina in električne energije pri negospodinjskih odjemalcih korelirajo (pearsonov koeficient korelacije ima vrednost 0.72), pri negospodinjskih pa ne (pearsonov koeficient korelacije ima vrednost -0.14). Sklepamo da je razlog zato, uporaba zemeljskega plina pri proizvodnji električne energije.
<br><br>
Zanimivo se nam je zdelo da cene električne energije pri gospodinjskih in negospodinjskih odjemalcih ne korelirajo (pearsonov koeficient korelacije ima vrednost -0.14), cene zemeljskega plina pri gospodinjskih in negospodinjskih odjemalcih pa vendar korelirajo (pearsonov koeficient korelacije je 0.77).
<br>

#### 2.2.1 Grafičen prikaz nekaterih ugotovitev

Z uporabo razsevnih diagramov, kjer vsaka točka predstavlja četrtletje smo primerjali odvisnosti cen po dveh energentov. Zelena diagonala na diagramih predstavlja pozitivno korelacijo cen energentov, rdeča pa negativno. Pri diagramih v katerih sta narisani obe diagonali, sta te predstavljeni z razlogom prikaza, da se cene ne korelirajo po nobeni od njiju. Na tak način smo dodatno potrdili zgornje ugotovitve ter jih vizualno predstavili [(vsi grafi so na voljo v zvezku fuel_correlation.ipynb)](src/fuel_correlation.ipynb).
<br>
![Prva točka](resources/Korelacija%201.png) ![Druga točka](resources/Korelacija%202.png)

