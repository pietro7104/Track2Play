CREATE TABLE User(
	ID int PRIMARY KEY,
    username varchar(100) UNIQUE,
    password varchar(300)
)