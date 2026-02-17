import sys
import mysql.connector
import os
import pandas as pd
import csv
import json
import utils


import numpy as np
import sklearn.decomposition as decomp
import skfuzzy as fuzz
import matplotlib.pyplot as plt

from sklearn.preprocessing import StandardScaler
from sklearn.preprocessing import MinMaxScaler

db = mysql.connector.connect(
    host = "localhost",
    user = "root",
    passwd = "mYsql1212",
    database = "game_rec2"
)

cursor = db.cursor()



data = pd.read_csv("user_data2.csv")



data.drop("friends", axis=1, inplace=True)
data.drop("average_achievement_count", axis=1, inplace=True)
data.drop("average_required_age", axis=1, inplace=True)
data.drop('average_positive_vote_percentage', axis=1, inplace=True)

cursor.execute("SELECT * FROM Genre")
genres = cursor.fetchall()
for genre in genres:
  data.drop(genre[0], axis=1, inplace=True)
  
cursor.execute("SELECT * FROM Category")
categories = cursor.fetchall()
for category in categories:
  data.drop(category[0], axis=1, inplace=True)


averagePrice = sys.argv[1]
tagAppearences = sys.argv[2]



cursor.execute("SELECT tag_name FROM Tag")
tags = cursor.fetchall()

cursor.execute("SELECT tag_name FROM Tag")
tags = cursor.fetchall()

new_df = pd.DataFrame()
for tag in tags:
  new_df[tag[0] +'_tag_appearences'] = 0
  #new_df[tag[0] +'_tag_percentage'] = 0

totalTags = 0
tag_appearences = tagAppearences.split(',')
for appearence in tag_appearences:
  x = appearence.split(':')

  totalTags += int(x[1])
  if x[0]+'_tag_appearences' in new_df.columns:
    new_df.at[0, x[0]+'_tag_appearences'] = int(x[1])
#for appearences in tag_appearences:
 # x = appearence.split(':')
  #new_df.at[0, x[0]+'_tag_percentage'] = float(x[1])/float(totalTags)

#for tag in tags:
 # new_df.drop(tag[0]+'_tag_percentage', axis=1, inplace=True)


new_df.at[0, 'average_price'] = averagePrice
new_df.at[0, 'ID'] = -1
#new_df.at[0, 'average_positive_vote_percentage'] = 0.7





training_ids = data.copy()
new_ids = new_df.copy()
data.drop("ID", axis=1, inplace=True)


full_data = pd.concat([data, new_df], axis=0)

scaler = MinMaxScaler()

full_data_scaled = scaler.fit_transform(full_data.to_numpy())



full_data = pd.DataFrame(full_data_scaled)

test_data = full_data.iloc[training_ids.shape[0]:]




#tsvd = TruncatedSVD(n_components=100)
#data_scaled = tsvd.fit_transform(data_scaled)
#test_data_scaled = tsvd.fit_transform(test_data_scaled)

cursor.execute('set global max_allowed_packet=67108864')


m = 1.7
error = 1e-7
maxiter = 500


c = 5

#cntr, u, u0, d, jm, p, fpc = fuzz.cluster.cmeans(data.T, c=c, m=m, error=error, maxiter=maxiter)

#print("c="+str(c)+":\n  iter: "+str(p)+"\n  fpc: "+str(fpc) + " cntr: " + str(cntr))
#print("\nFuzzy Membership Matrix (first 5 data points):")
#print(u[:, :5])


cntr = pd.read_csv("centroids.csv")

u, u0, d, jm, p, fpc = fuzz.cmeans_predict(test_data.T, cntr, m, error, maxiter)



hitPercent = 0
positionalScore = 0
for i in range(0, len(u[0])):
  
  id = new_ids.at[i, "ID"]
  scoreCalc = "(("+str(u[0][i])+"*GC.cluster1)"
  for j in range(1, len(u)):
    scoreCalc += " + ("+str(u[j][i])+"*GC.cluster"+str(j)+")"
  scoreCalc += ")"
  cursor.execute("SELECT GC.GameID, GC.owners, " + scoreCalc + " as Score " +
    "FROM " + 
    "gameclusters GC " +
    "WHERE (GC.owners > 20) " +
    "ORDER BY score DESC, GC.owners DESC " +
    "LIMIT 20")
 
  
  thisHitPercent = 0
  thisPositionalScore = 0
  k = 0
  for game in cursor.fetchall():
    k += 1
    gameId = game[0]
    print(gameId)
    cursor.execute("SELECT * FROM UserGame WHERE (UserID=%s AND GameID=%s)",(str(id), str(gameId)))
    if (cursor.fetchone() is not None): 
      thisHitPercent += 1
      thisPositionalScore += 1/k
  thisHitPercent /= 20
  hitPercent += thisHitPercent
  positionalScore += thisPositionalScore

hitPercent /= len(u[0])
averagePosScore = positionalScore/len(u[0])

  
#END TEST
quit()