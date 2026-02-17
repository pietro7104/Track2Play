import sys
import mysql.connector
import os
import pandas as pd
import csv
import json
import utils
import Steam_Games_Scraper_main.SteamGamesScraper as scraper

games_file = "Steam_Games_Scraper_main/games.json"

db = mysql.connector.connect(
    host = "localhost",
    user = "root",
    passwd = "root",
    database = "game_reccomandation"
)

cursor = db.cursor()



dataset = {}
i = 0

with open(games_file, 'r', encoding='utf-8') as fin:
    text = fin.read()
    if len(text) > 0:
      dataset = json.loads(text)
      
for app in dataset:
    appID = app
    game = dataset[app]
    cursor.execute("SELECT ID FROM Game WHERE (ID = %s)", (str(appID),))
    if (cursor.fetchone() is not None):
        i += 1
        print(i)
        
        total_tag_votes = 0
        tags = game['tags']
        for tag in tags:
            total_tag_votes += tags[tag]
            cursor.execute("SELECT * FROM Tag WHERE tag_name=%s",(str(tag),))
            if (cursor.fetchone() is None):
                cursor.execute("INSERT INTO Tag VALUES(%s)",(str(tag),))
                db.commit()
                
            cursor.execute("SELECT * FROM GameTag WHERE (GameID=%s AND tag_name=%s)", (str(appID), str(tag)))
            if (cursor.fetchone() is None):
                cursor.execute("INSERT INTO GameTag VALUES(%s, %s, %s)",(str(appID), str(tag), str(tags[tag])))
                db.commit()
                total_tag_votes += tags[tag]
        
        cursor.execute("UPDATE Game SET price=%s, dlc_count=%s, achievement_count=%s, metacritic_score=%s, median_playtime_forever=%s, required_age=%s, positive_votes=%s, negative_votes=%s, total_tag_votes=%s WHERE (ID=%s)",
                       (str(game['price']), str(game['dlc_count']), str(game['achievements']), str(game['metacritic_score']), str(game['median_playtime_forever']), str(game['required_age']), str(game['positive']), str(game['negative']), str(total_tag_votes), str(appID)))
        db.commit()
            
        
        languages = game['supported_languages']
        for language in languages:
            cursor.execute("SELECT * FROM Language WHERE language_name=%s",(str(language),))
            if (cursor.fetchone() is None):
                cursor.execute("INSERT INTO Language VALUES(%s)",(str(language),))
                db.commit()
            cursor.execute("SELECT * FROM GameLanguage WHERE (GameID=%s AND language_name=%s)", (str(appID), str(language)))
            if (cursor.fetchone() is None):
                cursor.execute("INSERT INTO GameLanguage VALUES(%s, %s)",(str(appID), str(language)))
                db.commit()
        
        genres = game['genres']
        for genre in genres:
            cursor.execute("SELECT * FROM Genre WHERE genre_name=%s",(str(genre),))
            if (cursor.fetchone() is None):
                cursor.execute("INSERT INTO Genre VALUES(%s)",(str(genre),))
                db.commit()
            
            cursor.execute("SELECT * FROM GameGenre WHERE (GameID=%s AND genre_name=%s)", (str(appID), str(genre)))
            if (cursor.fetchone() is None):
                cursor.execute("INSERT INTO GameGenre VALUES(%s, %s)",(str(appID), str(genre)))
                db.commit()
        
        categories = game['categories']
        for category in categories:
            cursor.execute("SELECT * FROM Category WHERE category_name=%s",(str(category),))
            if (cursor.fetchone() is None):
                cursor.execute("INSERT INTO Category VALUES(%s)",(str(category),))
                db.commit()
    
            cursor.execute("SELECT * FROM GameCategory WHERE (GameID=%s AND category_name=%s)", (str(appID), str(category)))
            if (cursor.fetchone() is None):
                cursor.execute("INSERT INTO GameCategory VALUES(%s, %s)",(str(appID), str(category)))
                db.commit()
        
        