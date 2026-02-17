import sys
import mysql.connector
import os
import pandas as pd
import csv
import json
import utils
import Steam_Scraper.scrapper2 as scrapper

directory_path = 'Steam_Scraper/user_data'

list_directory = "Steam_Games_Scraper_main/list.json"


db = mysql.connector.connect(
    host = "localhost",
    user = "root",
    passwd = "root",
    database = "game_reccomandation"
)

cursor = db.cursor()


game_list = []

def dump_game_list():
    with open(list_directory, "w") as list:
        json_list = json.dumps(game_list)
        json.dump(game_list, list)
        
cursor.execute("SELECT ID FROM Game")
games = cursor.fetchall()

for game in games:
    id = "".join(str(game))
    id = id.replace("(", "")
    id = id.replace(")", "")
    id = id.replace(",", "")
    print(id)
    game_list.append(id)
dump_game_list()

user_count = 0
for name in os.listdir(directory_path):
    if (name == "processed_users.txt"):
        continue
    with open(os.path.join(directory_path, name), encoding="utf8") as f:
        id = utils.get_user_id(name)
        
        cursor.execute("SELECT ID FROM User WHERE (ID = %s)", (str(id),))
        if (cursor.fetchone() is not None): 
            print("skip")
            continue
        
        friend_list_json = scrapper.fetch_friends(scrapper.api_key, id)
        friend_count = 0
        
        if (friend_list_json is not None and 'friendslist' in friend_list_json):
            friend_count = len((friend_list_json['friendslist'])['friends'])
        cursor.execute("INSERT INTO User (ID, friends) VALUES(%s, %s)", (str(id), str(friend_count)))
        db.commit()
            
        csv_file = csv.reader(f)
        for line in csv_file:
            if (line[0] == "appid"):
                continue
            gameID = utils.get_game_id(line)
            #Check if game is in database
            cursor.execute("SELECT ID FROM Game WHERE (ID = %s)", (str(gameID),))
            if (cursor.fetchone() is None):
                #Add game to database if it isn't in already
                cursor.execute("INSERT INTO Game (ID) VALUES(%s)", (str(gameID),))
                db.commit()
                game_list.append(gameID)
            #Create association between user and game  
            cursor.execute("INSERT INTO UserGame VALUES(%s, %s, %s)", (str(id), str(gameID), str(utils.get_playtime_forever(line))))  
        user_count = user_count + 1
        print(user_count)


db.commit()
dump_game_list() 
           

                
                
            
            
            
