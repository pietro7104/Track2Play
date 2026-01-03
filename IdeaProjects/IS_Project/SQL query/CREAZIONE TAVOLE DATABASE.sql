CREATE TABLE Utente (
	IdUtente int auto_increment primary key,
    Username varchar(30) unique not null,
    Password varchar(128) not null
);

CREATE TABLE Gioco (
	IdGioco varchar(36) primary key,
    Titolo varchar(100) not null,
    Copertina varchar(150) not null
);

CREATE TABLE Acquisto (
	Data_Acquisto timestamp,
    IdUtente int references Utente(IdUtente),
    IdGioco varchar(36) references Gioco(IdGioco),
    Piattaforma varchar(50) not null,
    Prezzo double not null,
    Regalo boolean not null,
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