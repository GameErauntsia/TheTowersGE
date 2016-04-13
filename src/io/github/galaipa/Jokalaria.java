package io.github.galaipa;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Jokalaria {
    public Player player;
    public Team team;
    private int exp;
    ItemStack[] armor;
    ItemStack[] inv;
        public Jokalaria(Player p) {
            player = p;
            exp = p.getLevel();
            inv = p.getInventory().getContents();
            armor = p.getInventory().getArmorContents();
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
        public void returnInv() {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setLevel(exp);
            player.getInventory().setContents(inv);
            player.getInventory().setArmorContents(armor);
        }
}
