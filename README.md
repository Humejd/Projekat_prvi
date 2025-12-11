                                              Praćenje ličnih finansija

Ovaj projekat je desktop aplikacija napravljena u Javi za praćenje ličnih finansija. 
Korisniku omogućava unos, pregled, uređivanje i brisanje transakcija, kao i prikaz ukupnih prihoda, rashoda i trenutnog stanja.

Korišteni alati i tehnologije:

Java (Swing GUI)

IntelliJ IDEA

MongoDB (lokalna baza podataka)

MongoDB Java Driver

Glavne funkcionalnosti
1. Unos transakcija

Korisnik može unijeti:

vrstu transakcije (Prihod / Rashod),
iznos,
opis i
kategoriju (Hrana, Plata, Računi, Zabava, Prijevoz, Ostalo)

Transakcija se sprema u MongoDB.

2. Prikaz svih transakcija

Sve transakcije se prikazuju u tabeli.

ID svake transakcije je skriven jer se koristi samo interno.


3. Ažuriranje transakcija

Klikom na red u tabeli, podaci se učitaju u polja.

Nakon uređivanja korisnik klikne Ažuriraj.

Aplikacija mijenja zapis u bazi koristeći njegov ID.

4. Brisanje transakcija
   
Korisnik odabere transakciju.

Prije brisanja prikazuje se prozor za potvrdu.

Nakon potvrde zapis se briše iz baze.

5. Prikaz finansijskog pregleda
Aplikacija automatski izračunava:
ukupne prihode,
ukupne rashode,
trenutni saldo.

6. Export podataka
   
Postoji dugme za export u TXT fajl, koji sadrži:
listu svih transakcija,
sumu svih prihoda i rashoda,
stanje računa,
raspodjelu po kategorijama.

Fajl se snima u root projekta.

Struktura projekta:

Main.java – pokreće aplikaciju

FinanceTrackerForm.java – GUI logika

Transaction.java – model transakcije

TransactionManager.java – rad s MongoDB-om

MongoDBConnection.java – povezivanje sa bazom


Pokretanje
1.	Startati MongoDB server
2.	Otvoriti projekat u IntelliJ IDEA
3.	Pokrenuti Main.java

