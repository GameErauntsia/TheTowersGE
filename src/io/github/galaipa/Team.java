
package io.github.galaipa;

import java.util.ArrayList;
import org.bukkit.Color;
import org.bukkit.Location;

public class Team {
    private String id;
    private Location spawnpoint;
    private Color color;
    private Cuboid win;
    ArrayList<Jokalaria> players = new ArrayList<>();;
    public Team(String n) {
        players = new ArrayList();
	id = n;
        if(id.equalsIgnoreCase("urdina")){
            color = Color.BLUE;
        }else{
            color = Color.RED;
        }
	}

    public String getID(){
        return id;
    }
    public void setWin(Location l1, Location l2){
        win = new Cuboid(l1,l2);
    }
    public Cuboid getWin(){
        return win;
    }
    public Location getSpawn(){
        return spawnpoint;
    }
    public void setSpawn(Location l){
        spawnpoint = l;
    }
    public void addPlayer(Jokalaria p){
        players.add(p);
    }
    public void removePlayer(Jokalaria p){
        players.remove(p);
    }
    public ArrayList<Jokalaria> getPlayers(){
        return players;
    }
    public Color getColor(){
        return color;
    }

    }
