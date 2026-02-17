
def get_user_id(filename):
    x = filename.split("_")
    return x[len(x) - 2]

def get_game_id(line):
    return line[0] 

def get_playtime_forever(line):
    playtime = line[1]
    playtime_float = 0
    try:
        playtime_float = float(playtime)
    except:
        playtime_float = 0
    
    return playtime_float