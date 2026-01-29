# CREATE per le tabelle principali del database

CREATE TABLE Utente (
	IdUtente int auto_increment primary key,
    Username varchar(30) unique not null,
    Password varchar(128) not null,
    Codice_ISO char(2) not null
);

CREATE TABLE Gioco (
	IdGioco varchar(36) primary key,        # varchar(36) dato che l id usato da IsThereAnyDeal è di 36 caratteri, ma dopo il testing potremmo cambiare in char(36)
    Titolo varchar(100),
    Copertina varchar(150)         # "Copertina" indica il link da dove prendere la copertina
);

CREATE TABLE Acquisto (
	Data_Acquisto timestamp,
    IdUtente int references Utente(IdUtente),
    IdGioco varchar(36) references Gioco(IdGioco),
    Piattaforma varchar(50) not null,
    Prezzo double not null,
    Regalo boolean not null,
    Moneta_ISO char(3) not null,
    primary key(Data_Acquisto, IdUtente, IdGioco)
);

CREATE TABLE Wishlist(
	IdUtente int references Utente(IdUtente),
    IdGioco varchar(36) references Gioco(IdGioco),
    Data_Aggiunta DATE not null,
    primary key(IdUtente, IdGioco)
);

CREATE TABLE Collezione(
	IdUtente int references Utente(IdUtente),
    Num_Tot int default 0 not null,
    Num_Completati int default 0 not null,
    Num_NonCompletati int default 0 not null,
    primary key(IdUtente)
);

CREATE TABLE Aggiunto(
	IdUtente int references Utente(IdUtente),
    IdGioco varchar(36) references Gioco(IdGioco),
    Data_Aggiunta DATE not null,
    primary key(IdUtente, IdGioco)
);

CREATE TABLE Completamento (
	IdUtente int references Utente(IdUtente),
    IdGioco varchar(36) references Gioco(IdGioco),
    Data_Completamento DATE not null,
    Stato boolean not null,
    primary key(IdUtente, IdGioco)
);

# --------------------- #
# Creazione dei trigger #

# TRIGGER 1 #
# Trigger per la creazione automatica della collezione di un nuovo utente
# Quando un nuovo utente viene inserito nel database, questo trigger viene attivato per la creazione nella relazione "Collezione" di un record per il nuovo utente
Delimiter //

create trigger insert_collezione
after insert on utente
for each row
BEGIN
	INSERT INTO Collezione VALUES (NEW.IdUtente ,DEFAULT, DEFAULT, DEFAULT);
END;
//

# TRIGGER 2 #
# Trigger per l aggiornamento del numero di giochi totali e il numero di giochi non completati quando inserisce un gioco nella sua collezione
Delimiter //

create trigger incrementa_giochi
after insert on aggiunto
for each row
BEGIN
	UPDATE collezione set Num_tot = Num_tot + 1 WHERE IdUtente = NEW.IdUtente;

	UPDATE collezione set Num_NonCompletati = Num_NonCompletati + 1 WHERE IdUtente = NEW.IdUtente;
END;
//

# TRIGGER 3 #
# Trigger per l inserimento nella tabella "completamento" di un nuovo gioco acquistato da un utente, per indicare che quell utente non ha completato il nuovo gioco acquistato
# Il trigger inserisce anche il gioco acquistato nella collezione dell utente, inserendo il record in "aggiunto" che è la relazione che contiene i collegamenti tra i giochi e le collezioni degli utenti
# L inserimento non viene eseguito se l utente ha acquistato il gioco come regalo, oppure se sta acquistando un gioco che ha già precedentemente acquistato
Delimiter //

create trigger insert_completamento
after insert on acquisto
for each row
BEGIN
	IF NEW.Regalo = 0 THEN
		IF NOT EXISTS (SELECT * FROM completamento WHERE IdUtente = NEW.IdUtente AND IdGioco = NEW.IdGioco) THEN
			INSERT completamento VALUES (NEW.IdUtente, NEW.IdGioco, CAST(NOW() AS DATE), false);
		END IF;

        IF NOT EXISTS (SELECT * FROM aggiunto WHERE IdUtente = NEW.IdUtente AND IdGioco = NEW.IdGioco) THEN
        	INSERT aggiunto VALUES (NEW.IdUtente, NEW.IdGioco, CAST(NOW() AS DATE));
        END IF;
    END IF;
END;
//


# TRIGGER 4 #
# Trigger per la modifica del numero di giochi completati e numero di giochi non completati in seguito ad un update su "completamento"
# Quindi se l update è per porre a true l attributo "Stato", e quindi per indicare che quell utente ha completato quel gioco, allora aggiorniamo il Num_Completati + 1 e il Num_NonCompletati - 1
DELIMITER //

create trigger update_collezione
after update on completamento
for each row
BEGIN
	IF OLD.Stato != NEW.Stato THEN
		IF NEW.Stato = true THEN
			UPDATE collezione SET Num_Completati = Num_Completati + 1  WHERE IdUtente = NEW.IdUtente;
            UPDATE collezione SET Num_NonCompletati = Num_NonCompletati - 1  WHERE IdUtente = NEW.IdUtente;
        END IF;

        IF NEW.Stato = false THEN
        	UPDATE collezione SET Num_Completati = Num_Completati - 1 WHERE IdUtente = NEW.IdUtente;
            UPDATE collezione SET Num_NonCompletati = Num_NonCompletati + 1  WHERE IdUtente = NEW.IdUtente;
        END IF;
    END IF;
END;
//

# TRIGGER 5 #
# Trigger per l aggiornamento del numero di giochi in una collezione in seguito alla rimozione di un gioco dalla collezione (delete su aggiunto)
DELIMITER //

create trigger delete_aggiunto
after delete on aggiunto
for each row
BEGIN
    UPDATE Collezione SET Num_Tot = Num_Tot - 1 WHERE IdUtente = OLD.IdUtente;
    IF (SELECT Stato From Completamento C WHERE C.IdUtente = OLD.IdUtente AND C.IdGioco = OLD.IdGioco) = false THEN
        UPDATE Collezione SET Num_NonCompletati = Num_NonCompletati - 1 WHERE IdUtente = OLD.IdUtente;
    END IF;

    IF (SELECT Stato From Completamento C WHERE C.IdUtente = OLD.IdUtente AND C.IdGioco = OLD.IdGioco) = true THEN
        UPDATE Collezione SET Num_Completati = Num_Completati - 1 WHERE IdUtente = OLD.IdUtente;
    END IF;

    DELETE FROM Completamento WHERE IdUtente = OLD.IdUtente AND IdGioco = OLD.IdGioco;
END;
//

# Trigger 6 #
# Trigger per l eliminazione della collezione, wishlist e acquisti di un utente dopo la cancellazione del suo account
DELIMITER //

create trigger delete_utente
    after delete on utente
    for each row
BEGIN
    DELETE FROM Aggiunto WHERE IdUtente = OLD.IdUtente;

    DELETE FROM Collezione WHERE IdUtente = OLD.IdUtente;

    DELETE FROM Acquisto WHERE IdUtente = OLD.IdUtente;

    DELETE FROM Completamento WHERE IdUtente = OLD.IdUtente;

    DELETE FROM Wishlist WHERE IdUtente = OLD.IdUtente;
END;
//