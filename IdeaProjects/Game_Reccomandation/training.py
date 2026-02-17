import sys
import mysql.connector
import os
import pandas as pd
import csv
import json
import utils


import numpy as np
import sklearn.decomposition as decomp
from sklearn.decomposition import TruncatedSVD
import skfuzzy as fuzz
import matplotlib.pyplot as plt

from sklearn.preprocessing import StandardScaler
from sklearn.preprocessing import MinMaxScaler

db = mysql.connector.connect(
    host = "localhost",
    user = "root",
    passwd = "root",
    database = "game_rec"
)

cursor = db.cursor()

df = pd.read_csv("dataframe2.csv")

data = df

test_data = data.sample(500)

data = data.drop(test_data.index)
    
data.to_csv("user_data3.csv", index=False)
test_data.to_csv("test_data3.csv", index=False)



dataframe = pd.read_csv("user_data2.csv")

data = dataframe.copy()


data.drop("friends", axis=1, inplace=True)
data.drop("average_achievement_count", axis=1, inplace=True)
data.drop("average_required_age", axis=1, inplace=True)

cursor.execute("SELECT * FROM Genre")
genres = cursor.fetchall()
for genre in genres:
  data.drop(genre[0], axis=1, inplace=True)
  
cursor.execute("SELECT * FROM Category")
categories = cursor.fetchall()
for category in categories:
  data.drop(category[0], axis=1, inplace=True)




test_data = pd.read_csv("test_data2.csv")

test_data.drop("friends", axis=1, inplace=True)
test_data.drop("average_achievement_count", axis=1, inplace=True)
test_data.drop("average_required_age", axis=1, inplace=True)


for genre in genres:
  test_data.drop(genre[0], axis=1, inplace=True)
  

for category in categories:
  test_data.drop(category[0], axis=1, inplace=True)


ids = data.copy()
data.drop("ID", axis=1, inplace=True)
test_ids = test_data.copy()
test_data.drop("ID", axis=1, inplace=True)

scaler = MinMaxScaler()
data_scaled = scaler.fit_transform(data.to_numpy())
test_data_scaled = scaler.fit_transform(test_data.to_numpy())

#tsvd = TruncatedSVD(n_components=100)
#data_scaled = tsvd.fit_transform(data_scaled)
#test_data_scaled = tsvd.fit_transform(test_data_scaled)

data = pd.DataFrame(data_scaled)
test_data = pd.DataFrame(test_data_scaled)




cursor.execute('set global max_allowed_packet=67108864')


m = 1.7
error = 1e-7
maxiter = 2000



fpc_array = []
for i in range (1, 11):
    cntr, u, u0, d, jm, p, fpc = fuzz.cluster.cmeans(data.T, c=i, m=m, error=error, maxiter=maxiter)
    fpc_array.append(fpc)
    print("c="+str(i)+":\n  iter: "+str(p)+"\n  fpc: "+str(fpc))
    print("\nFuzzy Membership Matrix (first 5 data points):")
    print(u[:, :5])
    
xpoints = np.array([1, 2, 3, 4, 5, 6, 7, 8, 9, 10])
ypoints = np.array(fpc_array)

plt.plot(xpoints, ypoints)
plt.show()
quit()
c = 5

cntr, u, u0, d, jm, p, fpc = fuzz.cluster.cmeans(data.T, c=c, m=m, error=error, maxiter=500)



print("c="+str(c)+":\n  iter: "+str(p)+"\n  fpc: "+str(fpc) + " cntr: " + str(cntr))
print("\nFuzzy Membership Matrix (first 5 data points):")
print(u[:, :5])


cdf = pd.DataFrame(cntr)
cdf.to_csv("centroids.csv")


createtable = "CREATE TABLE UserClusters(UserID bigint primary key, FOREIGN KEY(UserID) references User(ID)"
for i in range (1, c+1):
  createtable += ", cluster" + str(i) + " double"
createtable += ")"

cursor.execute("DROP TABLE IF EXISTS UserClusters")
cursor.execute(createtable)


createtable = "CREATE TABLE GameClusters(GameID bigint primary key, FOREIGN KEY(GameID) references Game(ID)"
for i in range (1, c+1):
  createtable += ", cluster" + str(i) + " double"
createtable += ")"

cursor.execute("DROP TABLE IF EXISTS GameClusters")
cursor.execute(createtable)

for i in range(0, len(u[0])):
  id = ids.at[i, "ID"]
  scores = ""
  scoresNum = []
  for j in range(0, len(u)):
    score = u[j][i]
    scoresNum.append(score)
    scores += ", " + str(score)
  cursor.execute("INSERT INTO UserClusters VALUES(%s" + scores + ")", (str(id),))
  db.commit()  
  cursor.execute("SELECT GameID FROM UserGame WHERE (UserID=%s)", (str(id),))
  for game in cursor.fetchall():
    cursor.execute("SELECT * FROM GameClusters WHERE (GameID=%s)",(str(game[0]),))
    gameCluster = cursor.fetchone()
    if (gameCluster == None): 
      cursor.execute("INSERT INTO GameClusters VALUES(%s" + scores + ")",(str(game[0]),))
      db.commit() 
    else:
      gameScores = ""
      for k in range(1, len(gameCluster)):
        score = gameCluster[k] + scoresNum[k-1]
        if (k != 1): gameScores += ", " 
        gameScores += "cluster" + str(k) + "=" + str(score)
      cursor.execute("UPDATE GameClusters SET " + gameScores + " WHERE (GameID=%s)", (str(gameCluster[0]),))
      db.commit()     
  
cursor.execute("ALTER TABLE GameClusters ADD owners int")
db.commit()
cursor.execute("SELECT * FROM GameClusters")
for gameCluster in cursor.fetchall():
  cursor.execute("SELECT COUNT(GameID) FROM UserGame WHERE GameID=%s",(str(gameCluster[0]),))   
  owners = cursor.fetchone()[0]
  gameScores = ""
  for k in range(1, len(gameCluster)-1):
    score = gameCluster[k]
    if (k != 1): gameScores += ", " 
    gameScores += "cluster" + str(k) + "=" + str(score/owners)
  cursor.execute("UPDATE GameClusters SET " + gameScores + ", owners=%s WHERE (GameID=%s)", (str(owners), str(gameCluster[0]),))
  db.commit() 
    
  


#Test

u, u0, d, jm, p, fpc = fuzz.cmeans_predict(test_data.T, cntr, m, error, maxiter)

print("c="+str(c)+":\n  iter: "+str(p)+"\n  fpc: "+str(fpc))
print("\nFuzzy Membership Matrix (first 5 data points):")
print(u[:, :5])

hitPercent = 0
positionalScore = 0
for i in range(0, len(u[0])):
  id = test_ids.at[i, "ID"]
  scoreCalc = "(("+str(u[0][i])+"*GC.cluster1)"
  for j in range(1, len(u)):
    scoreCalc += " + ("+str(u[j][i])+"*GC.cluster"+str(j)+")"
  scoreCalc += ")"
  cursor.execute("SELECT GC.GameID, GC.owners, " + scoreCalc + " as Score " +
    "FROM " + 
    "gameclusters GC " +
    "WHERE (GC.owners > 100) " +
    "ORDER BY score DESC, GC.owners DESC " +
    "LIMIT 20")
 
  
  thisHitPercent = 0
  thisPositionalScore = 0
  k = 0
  for game in cursor.fetchall():
    k += 1
    gameId = game[0]
    cursor.execute("SELECT * FROM UserGame WHERE (UserID=%s AND GameID=%s)",(str(id), str(gameId)))
    if (cursor.fetchone() is not None): 
      thisHitPercent += 1
      thisPositionalScore += 1/k
  thisHitPercent /= 20
  hitPercent += thisHitPercent
  positionalScore += thisPositionalScore

hitPercent /= 500
averagePosScore = positionalScore/500
print("hit percent="+str(hitPercent*100)+" pos score="+str(positionalScore)+" avg pos score="+str(averagePosScore))
  
#END TEST
quit()


      
    
    