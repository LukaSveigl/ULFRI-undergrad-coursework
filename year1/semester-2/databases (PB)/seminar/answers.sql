USE seminarska; #Delal sem v shemi seminarska, da nisem rabil brisati tabel
				# iz sheme vaje, spremeni po potrebi

#1. NALOGA (DDL)

CREATE TABLE IF NOT EXISTS pleme (
	tid INT PRIMARY KEY,
    tribe VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS aliansa (
	aid INT PRIMARY KEY,
    alliance VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS igralec (
	pid INT PRIMARY KEY,
    player VARCHAR(120) NOT NULL UNIQUE,
    tid INT NOT NULL,
    aid INT,
    FOREIGN KEY (tid) REFERENCES pleme (tid) 
		ON UPDATE RESTRICT		#Dodano, da ponesreči ne brišemo zapisov iz drugih tabel
		ON DELETE RESTRICT,		#kot bi to storili z uporabo ON DELETE CASCADE
	FOREIGN KEY (aid) REFERENCES aliansa (aid)
		ON UPDATE RESTRICT 
		ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS naselje (
	vid INT PRIMARY KEY,
    village VARCHAR(120) NOT NULL,
    x INT NOT NULL,
    y INT NOT NULL,
    population INT NOT NULL,
    pid INT NOT NULL,
    FOREIGN KEY (pid) REFERENCES igralec (pid)
		ON UPDATE RESTRICT 
		ON DELETE RESTRICT,
	CHECK (x BETWEEN -250 AND 250),
    CHECK (y BETWEEN -250 AND 250),
    CHECK (population >= 0)
);

# Izpraznimo tabele, lahko ponovno zaženemo INSERT stavke
DELETE FROM seminarska.naselje;
DELETE FROM seminarska.igralec;
DELETE FROM seminarska.pleme;
DELETE FROM seminarska.aliansa;

INSERT INTO pleme VALUES (1, "Rimljani");
INSERT INTO pleme VALUES (2, "Tevtoni");
INSERT INTO pleme VALUES (3, "Galci");
INSERT INTO pleme VALUES (4, "Narava");
INSERT INTO pleme VALUES (5, "Natarji");
INSERT INTO pleme VALUES (6, "Huni");
INSERT INTO pleme VALUES (7, "Egipcani");

INSERT INTO aliansa (aid, alliance)
	SELECT DISTINCT xw.aid, xw.alliance 
	FROM x_world xw;
    
INSERT INTO igralec (pid, player, tid, aid)
	SELECT DISTINCT xw.pid, xw.player, xw.tid, xw.aid
    FROM x_world xw;
    
INSERT INTO naselje (vid, village, x, y, population, pid)
	SELECT DISTINCT xw.vid, xw.village, xw.x, xw.y, xw.population, xw.pid
    FROM x_world xw;

# Prenastavimo vrednost aid igralca
UPDATE igralec SET aid = NULL
	WHERE aid = 0;

# Pobrišemo prazno alianso
DELETE FROM aliansa WHERE aid = 0;



#2. NALOGA (DML)

#a
SELECT i.pid, i.player, n.vid, n.village, n.population
FROM igralec i INNER JOIN naselje n USING(pid)
ORDER BY n.population DESC
LIMIT 1;

#b
SELECT a.aid, a.alliance
FROM aliansa a
WHERE (SELECT COUNT(i.aid)
		FROM igralec i
        WHERE i.aid = a.aid) = 60; # 60 članov je max
  
#c
SELECT COUNT(DISTINCT i.pid) AS 'St. z nadpovprecjem'
FROM igralec i INNER JOIN naselje n USING(pid)
WHERE n.population > (SELECT AVG(n2.population)
						FROM naselje n2);

#d
SELECT n.*
FROM naselje n INNER JOIN igralec i USING(pid)
WHERE i.aid IS NULL
ORDER BY x DESC, y DESC; 

#e
SELECT p.tribe
FROM pleme p INNER JOIN igralec i USING(tid)
INNER JOIN naselje n USING(pid)
GROUP BY p.tribe
ORDER BY SUM(n.population) DESC
LIMIT 1;

#f
SELECT COUNT(DISTINCT a3.aid)  as Nadpovprecne
FROM aliansa a3
WHERE a3.aid IN
	(SELECT a2.aid # ID alians z nadpovprecnim stevilom igralcev
	FROM aliansa a2 INNER JOIN igralec i2 USING(aid)
	GROUP BY a2.aid
	HAVING COUNT(i2.pid) > (SELECT AVG(t1.IgralciAliansa) # Povprecno st igralcev na alianso
						   FROM ( 
								SELECT COUNT(i.pid) as IgralciAliansa # Igralci po aliansah
								FROM igralec i INNER JOIN aliansa a USING(aid)
								GROUP BY a.aid
								) as t1));
  
#g
DROP FUNCTION IF EXISTS popObmocja;
DELIMITER // 
CREATE FUNCTION  popObmocja(x INTEGER, 
						   y INTEGER, 
                           razdalja INTEGER) RETURNS INTEGER
BEGIN
	DECLARE pop INTEGER;
    
    SELECT SUM(n.population) INTO pop
    FROM naselje n
    WHERE n.x BETWEEN x AND x + razdalja
	  AND n.y BETWEEN y AND y + razdalja;
      
	RETURN pop;
END //
DELIMITER ;
SELECT popObmocja(40, 40, 10);
SELECT popObmocja(40, 40, 20);

#h
SELECT DISTINCT i.player
FROM igralec i INNER JOIN naselje n USING(pid)
WHERE (SELECT COUNT(n2.vid)  	#Pogledamo, ce je st. vseh igralcevih naselji
	   FROM naselje n2       	#enako st. igralcevih naselji na tem obmocju
       WHERE n2.pid = i.pid) = (SELECT COUNT(n3.vid) 
							    FROM naselje n3 
                                WHERE n3.pid = i.pid 
                                  AND n3.x BETWEEN 150 AND 200
                                  AND n3.y BETWEEN 0 AND 100);

#i
SELECT a.aid, 
	   a.alliance, 
       (SUM(n.population) * 100) / 
       (SELECT SUM(n2.population) FROM naselje n2) AS 'Delez'
FROM aliansa a INNER JOIN igralec i USING(aid)
INNER JOIN naselje n USING(pid)
GROUP BY a.aid, a.alliance
HAVING Delez > 3
ORDER BY Delez DESC;

#j
SET @counter = 0;

UPDATE naselje SET village = CONCAT("Grad ", LPAD(@counter := @counter + 1, 2, 0))
WHERE pid = (SELECT i.pid FROM igralec i WHERE i.player = "Sirena")
ORDER BY population DESC;

SELECT n.* 
FROM naselje n INNER JOIN igralec i USING(pid)
WHERE i.player = "Sirena"
ORDER BY n.population DESC;



#3. NALOGA

#a
#Začnemo transakcijo
START TRANSACTION;

#V tabelo aliansa vstavimo novo alianso "HORDA-CAR"
INSERT INTO aliansa VALUES((SELECT MAX(a.aid) FROM aliansa a) + 1, "HORDA-CAR"); #AID ena vecji kot najvecji

#"Prevežemo" igralce v aliansah "HORDA" in "CAR" v novoustvarjeno alianso
UPDATE igralec SET aid = (SELECT aid FROM aliansa WHERE alliance = "HORDA-CAR") 
WHERE aid = (SELECT aid
			 FROM aliansa
             WHERE alliance = "HORDA")
   OR aid = (SELECT aid
			 FROM aliansa
			 WHERE alliance = "CAR");

#Ko smo igralce prevezali, izbrišemo stari aliansi
DELETE FROM aliansa WHERE alliance = "HORDA" OR alliance = "CAR";

COMMIT;

SELECT * FROM aliansa WHERE alliance = "HORDA-CAR"; #Pogledamo, da aliansa obstaja

#b
DROP TRIGGER IF EXISTS CheckIfAliansaFull_update;

DELIMITER //
CREATE TRIGGER CheckIfAliansaFull_update
BEFORE UPDATE ON igralec
FOR EACH ROW 
BEGIN
	DECLARE allianceSize INTEGER;
	DECLARE errorMessage VARCHAR(255);
    SET errorMessage = "This alliance is full"; #Dolocimo sporocilo

	IF OLD.aid != NEW.aid THEN #Pogledamo, ce se aid spremeni
		SET allianceSize = (SELECT COUNT(i.pid) #Dobimo velikost alianse
							FROM igralec i
                            WHERE i.aid = NEW.aid);
                            
		IF allianceSize + 1 > 60 THEN #Pogledamo, ce je aliansa prevelika
			SIGNAL SQLSTATE '45000' #Ustavimo stavek
			SET MESSAGE_TEXT = errorMessage; #Posljemo sporocilo
        END IF;
    END IF;
END //
DELIMITER ;
