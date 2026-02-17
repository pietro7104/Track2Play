import sys
import mysql.connector
import os
import pandas as pd
import csv
import json
import utils

db = mysql.connector.connect(
    host = "localhost",
    user = "root",
    passwd = "root",
    database = "game_rec"
)

cursor = db.cursor()

cursor.execute('set session group_concat_max_len = 999999999999')

df = pd.read_sql("SELECT User.ID, User.friends, User.average_price, User.average_achievement_count, User.average_required_age, User.average_positive_vote_percentage" +
  ", GROUP_CONCAT(DISTINCT Concat(UserCategory.category_name, ':',UserCategory.appearences) SEPARATOR ',') AS category_appearences" +
  ", GROUP_CONCAT(DISTINCT Concat(UserGenre.genre_name, ':',UserGenre.appearences) SEPARATOR ',') AS genre_appearences" + 
  ", GROUP_CONCAT(DISTINCT Concat(UserTag.tag_name, ':',UserTag.appearences) SEPARATOR ',') AS tag_appearences " +
  #", GROUP_CONCAT(DISTINCT Concat(UserTag.tag_name, ':',UserTag.weight) SEPARATOR ',') AS tag_weight " +
"FROM User " +
  "LEFT JOIN UserCategory ON User.ID = UserCategory.UserID " +
  "LEFT JOIN UserGenre ON User.ID = UserGenre.UserID " +
  "LEFT JOIN UserTag ON User.ID = UserTag.UserID " +
"GROUP BY User.ID, User.friends", db)


cursor.execute("SELECT category_name FROM Category")
categories = cursor.fetchall()

cursor.execute("SELECT genre_name FROM Genre")
genres = cursor.fetchall()

cursor.execute("SELECT tag_name FROM Tag")
tags = cursor.fetchall()

category_df = pd.DataFrame()
for category in categories:
  category_df[category[0]] = 0

genre_df = pd.DataFrame()
for genre in genres:
  genre_df[genre[0]] = 0
  
tag_df = pd.DataFrame()
for tag in tags:
  tag_df[tag[0] +'_tag_appearences'] = 0
  #tag_df[tag[0] +'_tag_weight'] = pd.Series(dtype=float)

for index, row in df.iterrows():
  category_appearences = row['category_appearences'].split(',')
  for appearence in category_appearences:
    x = appearence.split(':')
    category_df.at[index, x[0]] = int(x[1])
  
  genre_appearences = row['genre_appearences'].split(',')
  for appearence in genre_appearences:
    x = appearence.split(':')
    genre_df.at[index, x[0]] = int(x[1])
    
  tag_appearences = row['tag_appearences'].split(',')
  for appearence in tag_appearences:
    x = appearence.split(':')
    tag_df.at[index, x[0]+'_tag_appearences'] = int(x[1])
  #tag_weights = row['tag_weight']
  #for weight in tag_weights:
   #x = weight.split(':')
   #print(x[0])
   #tag_df.at[index, x[0]+'_weight'] = float(x[1])
   #print("index: " + str(index) + " " + x[0] + " " + str(tag_df.at[index, x[0]+'_weight']))
  
  
    

  
df = pd.concat([df.drop('category_appearences', axis=1), category_df], axis=1)
df = pd.concat([df.drop('genre_appearences', axis=1), genre_df], axis=1)
df = pd.concat([df.drop('tag_appearences', axis=1), tag_df], axis=1)

#category_columns = df['category_appearences'].str.split(',', expand=True)
#genre_columns = df['genre_appearences'].str.split(',', expand=True)

#df = pd.concat([df.drop('category_appearences', axis=1), category_columns], axis=1)
#df = pd.concat([df.drop('genre_appearences', axis=1), genre_columns], axis=1)

df = df.fillna(0)
print(df)

data = df

test_data = data.sample(500)

data = data.drop(test_data.index)
    
data.to_csv("user_data2.csv", index=False)
test_data.to_csv("test_data2.csv", index=False)
#data = data.drop('ID', axis=1)


df.to_csv("dataframe2.csv", index=False)

