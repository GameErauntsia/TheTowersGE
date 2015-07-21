package io.github.galaipa;

import org.bukkit.entity.Player;


public class Jokalaria {
    public Player player;
    public Team team;
    
        public Jokalaria(Player p) {
            player = p;
        }
        public void setTeam(Team t){
            t.addPlayer(this);
            team = t;
        }
        public Team getTeam(){
            return team;
        }
        public Player getPlayer(){
            return player;
        }
}
