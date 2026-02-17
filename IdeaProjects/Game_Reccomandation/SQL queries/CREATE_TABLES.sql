DROP TABLE IF exists userlanguage;
DROP TABLE IF exists gamelanguage;
DROP TABLE IF exists language;
DROP TABLE IF exists usercategory;
DROP TABLE IF exists gamecategory;
DROP TABLE IF exists Category;
DROP TABLE IF exists usergenre;
DROP TABLE IF exists gamegenre;
DROP TABLE IF exists Genre;
DROP TABLE IF exists usertag;
DROP TABLE IF exists gametag;
DROP TABLE IF exists Tag;
DROP TABLE IF exists UserGame;
DROP TABLE IF exists Game;
DROP TABLE User;

CREATE TABLE User(
	ID bigint PRIMARY KEY,
    friends int,
    average_playtime float,
    average_price float,
    average_dlc_count float,
    average_achievement_count float,
    average_required_age float,
    average_metacritic_score float,
    average_positive_vote_percentage float,
    average_game_playtime float
);

CREATE TABLE Game(
	ID bigint PRIMARY KEY,
    price float,
    dlc_count int,
    achievement_count int,
    metacritic_score float,
    median_playtime_forever float,
    required_age int,
    positive_votes int,
    negative_votes int,
    total_tag_votes int
);

CREATE TABLE UserGame(
	UserID bigint,
    GameID bigint,
    playtime_forever float,
    
    foreign key(UserID) references User(ID),
    foreign key(GameID) references Game(ID),
    PRIMARY KEY(UserID, GameID)
);

CREATE TABLE Tag(
	tag_name varchar(200) PRIMARY KEY
);

CREATE TABLE UserTag(
	UserID bigint,
    tag_name varchar(200),
    appearences int,
    weight float,
    
    foreign key(UserID) references User(ID),
    foreign key(tag_name) references Tag(tag_name),
    PRIMARY KEY(UserID, tag_name)
);

CREATE TABLE GameTag(
	GameID bigint,
    tag_name varchar(200),
    votes int,
    
    foreign key(GameID) references Game(ID),
    foreign key(tag_name) references Tag(tag_name),
    PRIMARY KEY(GameID, tag_name)
);

CREATE TABLE Genre(
	genre_name varchar(200) PRIMARY KEY
);

CREATE TABLE UserGenre(
	UserID bigint,
    genre_name varchar(200),
    appearences int,
    
    foreign key(UserID) references User(ID),
    foreign key(genre_name) references Genre(genre_name),
    PRIMARY KEY(UserID, genre_name)
);

CREATE TABLE GameGenre(
	GameID bigint,
    genre_name varchar(200),
    
    foreign key(GameID) references Game(ID),
    foreign key(genre_name) references Genre(genre_name),
    PRIMARY KEY(GameID, genre_name)
);

CREATE TABLE Category(
	category_name varchar(200) PRIMARY KEY
);

CREATE TABLE UserCategory(
	UserID bigint,
    category_name varchar(200),
    appearences int,
    
    foreign key(UserID) references User(ID),
    foreign key(category_name) references Category(category_name),
    PRIMARY KEY(UserID, category_name)
);

CREATE TABLE GameCategory(
	GameID bigint,
    category_name varchar(200),
    
    foreign key(GameID) references Game(ID),
    foreign key(category_name) references Category(category_name),
    PRIMARY KEY(GameID, category_name)
);

CREATE TABLE Language(
	language_name varchar(200) PRIMARY KEY
);

CREATE TABLE UserLanguage(
	UserID bigint,
    language_name varchar(200),
    appearences int,
    
    foreign key(UserID) references User(ID),
    foreign key(language_name) references Language(language_name),
    PRIMARY KEY(UserID, language_name)
);

CREATE TABLE GameLanguage(
	GameID bigint,
    language_name varchar(200),
    
    foreign key(GameID) references Game(ID),
    foreign key(language_name) references Language(language_name),
    PRIMARY KEY(GameID, language_name)
);

