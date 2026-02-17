import sys
import mysql.connector
import os
import pandas as pd
import csv
import json
import utils
from multiprocessing import Pool, Lock
import Steam_Games_Scraper_main.SteamGamesScraper as scraper

games_file = "Steam_Games_Scraper_main/games.json"

db = mysql.connector.connect(
    host = "localhost",
    user = "root",
    passwd = "root",
    database = "game_rec"
)

cursor = db.cursor()

cursor.execute("SELECT ID FROM User")
users = cursor.fetchall()

lock = Lock()

    
def CompileUser(user):
        cursor.execute("SELECT G.*, UG.playtime_forever FROM UserGame UG JOIN Game G ON (UG.GameID = G.ID) WHERE (UserID=%s)",(str(user[0]),))
        games = cursor.fetchall()
        print("user: " + str(user[0]))
        #print(games)
        game_count = 0
        average_price = 0
        average_dlc = 0
        average_achievement = 0
        average_metacritic = 0
        average_median_playtime = 0
        average_game_median_playtime = 0
        average_required_age = 0
    
        average_positive_vote_percentage = 0
        no_votes_games = 0
        no_score_games = 0
    
        for game in games:
            game_count += 1
            average_price += game[1]
            average_dlc += game[2]
            average_achievement += game[3]
            if (game[4] > 0): average_metacritic += game[4]
            else: no_score_games += 1
            average_game_median_playtime += game[5]
            average_median_playtime += game[10]
            average_required_age += game[6]
        
            if (game[7] + game[8] > 0): average_positive_vote_percentage += (game[7]/(game[7] + game[8]))
            else: no_votes_games += 1
        
            
        
        
            cursor.execute("SELECT tag_name, votes FROM GameTag WHERE (GameID=%s) ORDER BY votes DESC LIMIT 5",(str(game[0]),))
            tags = cursor.fetchall()
            i = 0
            for tag in tags:
                weight = 1 - (0.2*i)
                i += 1
                cursor.execute("SELECT * FROM UserTag WHERE (UserID=%s AND tag_name=%s)",(str(user[0]), str(tag[0])))
                user_tag = cursor.fetchone()
                if (user_tag is None):
                    cursor.execute("INSERT INTO UserTag VALUES (%s, %s, %s, %s)",(str(user[0]), str(tag[0]), str(1), str(weight)))
                    db.commit()
                else:
                    appearences = int(user_tag[2]) + 1
                    weighted_appearences = int(user_tag[3]) + weight
                    
                    cursor.execute("UPDATE UserTag SET appearences=%s, weight=%s WHERE (UserID=%s AND tag_name=%s)",(str(appearences), str(weighted_appearences), str(user[0]), str(tag[0])))
                    db.commit()
        
            
            
        if (game_count > 0):
            average_price /= game_count
            average_dlc /= game_count
            average_achievement /= game_count
            if (game_count - no_score_games > 0): average_metacritic /= (game_count - no_score_games)
            else: average_metacritic = -1
            average_median_playtime /= game_count
            average_game_median_playtime /= game_count
            average_required_age /= game_count
            if (no_votes_games > 0): average_positive_vote_percentage /= (game_count - no_votes_games)
            else: average_positive_vote_percentage = -1
            
            cursor.execute("UPDATE User SET average_price=%s, average_dlc_count=%s, average_achievement_count=%s, average_required_age=%s, average_metacritic_score=%s, average_positive_vote_percentage=%s, average_playtime=%s, average_game_playtime=%s WHERE (ID=%s)"
                               ,(str(average_price), str(average_dlc), str(average_achievement), str(average_required_age), str(average_metacritic), str(average_positive_vote_percentage), str(average_median_playtime), str(average_game_median_playtime), str(user[0])))
            db.commit()
    


if __name__ == '__main__':  
    with Pool(12) as p:
        p.map(CompileUser, users)

