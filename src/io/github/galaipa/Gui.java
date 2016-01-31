package io.github.galaipa;

import static io.github.galaipa.GameListener.item;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class Gui implements Listener {
    static int a = 0;
    static int b = 0;
    ArrayList<Player> botoa = new ArrayList<>();
     public TheTowersGE plugin;
    public Gui(TheTowersGE instance) {
            plugin = instance;
        }
    public static Inventory joinGUI2 = Bukkit.createInventory(null, 9, ChatColor.RED +"TaldeJokoa: Aukeratu taldea");
    public static Inventory joinGUI = Bukkit.createInventory(null, 9, ChatColor.RED +"TaldeJokoa");
    public  static void setGui2() {
        joinGUI2.setItem(0,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu taldea"));
        joinGUI2.setItem(1,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu taldea"));
        joinGUI2.setItem(2,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu taldea"));
        joinGUI2.setItem(3,item(Material.WOOL,1,1,ChatColor.WHITE + "Ausaz aukeratu"));
        joinGUI2.setItem(4,item(Material.WOOL,14,1,ChatColor.RED + "Talde Gorria"));
        joinGUI2.setItem(5,item(Material.WOOL,11,1,ChatColor.BLUE + "Talde Urdina"));
        joinGUI2.setItem(6,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu taldea"));
        joinGUI2.setItem(7,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu taldea"));
        joinGUI2.setItem(8,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu taldea"));
    }
    public  static void setGui() {
        joinGUI.setItem(0,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Game Erauntsia"));
        joinGUI.setItem(1,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Game Erauntsia"));
        joinGUI.setItem(2,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Game Erauntsia"));
        joinGUI.setItem(3,item(Material.BOW,0,1,ChatColor.BLUE + "Jolastu"));
        joinGUI.setItem(4,item(Material.BOOK_AND_QUILL,0,1,ChatColor.BLUE + "Estatistikak"));
       // joinGUI.setItem(4,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Game Erauntsia"));
        joinGUI.setItem(5,item(Material.PUMPKIN,1,1,ChatColor.BLUE + "Ikuslea"));
        joinGUI.setItem(6,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Game Erauntsia"));
        joinGUI.setItem(7,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Game Erauntsia"));
        joinGUI.setItem(8,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Game Erauntsia"));
        setGuiMapa();
        setGui2();
    }
    public static void openGui(Player p){
        p.openInventory(joinGUI);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked(); 
        if (event.getInventory().getName().equals(joinGUI2.getName())) {
            if(event.getCurrentItem().getItemMeta() != null ){
                ItemStack clicked = event.getCurrentItem(); 
                event.setCancelled(true);
               /* if(plugin.inGame){
                    player.sendMessage(ChatColor.GREEN +"[TheTowers] " +ChatColor.RED + "Partida hasita dago");
                    return;
                }*/
                if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "Ausaz aukeratu")){
                    plugin.joinRandom(player);
                }
                else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Talde Gorria")){
                    if(!player.hasPermission("tt.vip")){
                        player.closeInventory();
                        player.sendMessage(ChatColor.GREEN +"[TaldeJokoak] " +ChatColor.RED + "Vip-ek bakarrik aukera dezakete zein taldetan sartu");
                    }else{
                        plugin.join(player,"gorria");
                    }
                }
                else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Talde Urdina")){
                    if(!player.hasPermission("tt.vip")){
                        player.closeInventory();
                        player.sendMessage(ChatColor.GREEN +"[TaldeJokoak] " +ChatColor.RED + "Vip-ek bakarrik aukera dezakete zein taldetan sartu");
                    }else{
                        plugin.join(player,"urdina");
                    }
                }
            
            }
        }else if (event.getInventory().getName().equals(joinGUI.getName())) {
            if(event.getCurrentItem().getItemMeta() != null){
                ItemStack clicked = event.getCurrentItem(); 
                event.setCancelled(true);
                if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Ikuslea")){
                        if(plugin.inGame){
                            player.closeInventory();
                            plugin.joinSpectator(player);
                        }else{
                            player.sendMessage(ChatColor.GREEN +"[TaldeJokoak] " +ChatColor.RED + "Ez da inor jolasten ari");
                        }
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Jolastu")){
                    player.openInventory(joinGUI2);
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Estatistikak")){
                    player.sendMessage(ChatColor.GREEN +"==================================================");
                    player.sendMessage(ChatColor.BLUE +"                    TALDE JOKOAK ESTATISTIKAK");
                    player.sendMessage(ChatColor.GREEN +"   ");
                    player.sendMessage(ChatColor.BLUE +"Irabazitako partidak: "+ ChatColor.YELLOW + GEAPI.infoStat("ttirabazi", player));
                    player.sendMessage(ChatColor.BLUE +"Jokatutako partidak: "+ ChatColor.YELLOW + GEAPI.infoStat("ttjokatu", player));
                    player.sendMessage(ChatColor.GREEN +"==================================================");    
                    player.closeInventory();
                }}
    }else if (event.getInventory().getName().equals(mapaGUI.getName())) {
        Player p = (Player) event.getWhoClicked();
            if(event.getCurrentItem().getItemMeta() != null ){
                ItemStack clicked = event.getCurrentItem(); 
                event.setCancelled(true);
                if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "The Towers Klasikoa")){
                    bozkatu(p,"a");
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Destroy The Nexus 1")){
                    bozkatu(p,"b");
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "The Towers 2")){
                    bozkatu(p,"c");
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "The Towers 3")){
                    bozkatu(p,"d");
                }
                  
            }
    }
    }
   
    public static Inventory mapaGUI = Bukkit.createInventory(null, 27, ChatColor.RED +"Talde jokoak : Mapa");
    public  static void setGuiMapa() {
        mapaGUI.setItem(0,item(Material.STAINED_GLASS_PANE,1,1,ChatColor.RED + "The Towers"));
        mapaGUI.setItem(1,item(Material.STAINED_GLASS_PANE,1,1,ChatColor.RED + "The Towers"));
        mapaGUI.setItem(2,item(Material.STAINED_GLASS_PANE,1,1,ChatColor.RED + "The Towers"));
        mapaGUI.setItem(3,item(Material.STAINED_GLASS_PANE,1,1,ChatColor.RED+ "The Towers"));
        mapaGUI.setItem(4,item(Material.STAINED_GLASS_PANE,0,1,ChatColor.WHITE + "Talde Jokoak"));
        mapaGUI.setItem(5,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.RED + "Destroy The Nexus"));
        mapaGUI.setItem(6,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.RED + "Destroy The Nexus"));
        mapaGUI.setItem(7,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.RED + "Destroy The Nexus"));
        mapaGUI.setItem(8,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.RED + "Destroy The Nexus"));
        
        mapaGUI.setItem(9,item(Material.STAINED_GLASS_PANE,1,1,ChatColor.RED + "The Towers"));
        mapaGUI.setItem(10,item(Material.NETHER_BRICK,1,1,ChatColor.GREEN + "The Towers Klasikoa"));
        mapaGUI.setItem(11,item(Material.WOOL,14,1,ChatColor.GREEN + "The Towers 2"));
        mapaGUI.setItem(12,item(Material.STAINED_GLASS_PANE,1,1,ChatColor.RED + "The Towers"));
        mapaGUI.setItem(13,item(Material.STAINED_GLASS_PANE,0,1,ChatColor.WHITE + "Talde Jokoak"));
        mapaGUI.setItem(14,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.RED + "Destroy The Nexus"));
      //  mapaGUI.setItem(15,item(Material.GRASS,1,1,ChatColor.GREEN + "Destroy The Nexus 1"));
        mapaGUI.setItem(17,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.RED + "Destroy The Nexus"));
        
        mapaGUI.setItem(18,item(Material.STAINED_GLASS_PANE,1,1,ChatColor.RED + "The Towers"));
        mapaGUI.setItem(19,item(Material.GRASS,1,1,ChatColor.GREEN + "The Towers 3"));
        mapaGUI.setItem(20,item(Material.STAINED_GLASS_PANE,1,1,ChatColor.RED + "The Towers"));
        mapaGUI.setItem(21,item(Material.STAINED_GLASS_PANE,1,1,ChatColor.RED + "The Towers"));
        mapaGUI.setItem(22,item(Material.STAINED_GLASS_PANE,0,1,ChatColor.WHITE + "Talde Jokoak"));
        mapaGUI.setItem(23,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.RED + "Destroy The Nexus"));
        mapaGUI.setItem(24,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.RED + "Destroy The Nexus"));
        mapaGUI.setItem(25,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.RED + "Destroy The Nexus"));
        mapaGUI.setItem(26,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.RED + "Destroy The Nexus"));

    }
    public static void maingui(Jokalaria j){
        int i;
        if(j.getTeam().getID().equalsIgnoreCase("urdina")){
            i = 11;
        }else{
            i = 14;
        }
        Inventory inv = j.getPlayer().getInventory();
        inv.setItem(0,item(Material.STAINED_GLASS_PANE,i,1,ChatColor.GREEN + "Game Erauntsia"));
        inv.setItem(1,item(Material.STAINED_GLASS_PANE,i,1,ChatColor.GREEN + "Game Erauntsia"));
        inv.setItem(2,item(Material.STAINED_GLASS_PANE,i,1,ChatColor.GREEN + "Game Erauntsia"));
        inv.setItem(3,item(Material.STAINED_CLAY,14,1,ChatColor.YELLOW + "Mapa bozkatu"));
        inv.setItem(4,item(Material.STAINED_GLASS_PANE,i,1,ChatColor.GREEN + "Game Erauntsia"));
        inv.setItem(5,item(Material.BARRIER,0,1,ChatColor.RED  + "Jokotik irten"));
        inv.setItem(6,item(Material.STAINED_GLASS_PANE,i,1,ChatColor.GREEN + "Game Erauntsia"));
        inv.setItem(7,item(Material.STAINED_GLASS_PANE,i,1,ChatColor.GREEN + "Game Erauntsia"));
        inv.setItem(8,item(Material.STAINED_GLASS_PANE,i,1,ChatColor.GREEN + "Game Erauntsia"));
    }
    
      @EventHandler
      public void onInventoryClick2(PlayerInteractEvent event){
          if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK ){
              Player p = event.getPlayer();
              if(plugin.isInGame(p)){
              if(p.getItemInHand() != null && p.getItemInHand().hasItemMeta() && p.getItemInHand().getItemMeta().hasDisplayName()){
                  String izena = p.getItemInHand().getItemMeta().getDisplayName();
                   if(izena.equalsIgnoreCase(ChatColor.YELLOW  +"Mapa bozkatu")){
                      event.setCancelled(true);
                      p.openInventory(mapaGUI);
                  }else if(izena.equalsIgnoreCase(ChatColor.RED  +"Jokotik irten")){
                      event.setCancelled(true);
                      plugin.leave(p);
                  }else if(izena.equalsIgnoreCase(ChatColor.GREEN  +"Game Erauntsia")){
                      event.setCancelled(true);
                  }else if(izena.equalsIgnoreCase(ChatColor.GREEN + "Jokoa hasteko bozkatu duzu")){
                      event.setCancelled(true);
                  }
          }
              }
          }
      }
    public HashMap<String, Integer> bozketa = new HashMap<>();
    public void bozkatu(Player p,String mapa){
        plugin.bozkak++;
        p.closeInventory();
        p.sendMessage(ChatColor.GREEN +"[TaldeJokoak] " + ChatColor.YELLOW + "Jokoa hasteko bozkatu duzu. Jokalarien %60ak bozkatzean hasiko da");
        p.getInventory().setItem(3,item(Material.STAINED_CLAY,5,1,ChatColor.GREEN + "Jokoa hasteko bozkatu duzu"));
        if(bozketa.get(mapa) == null){
            bozketa.put(mapa, 0);
        }
        bozketa.put(mapa, bozketa.get(mapa)+ 1);
        if(plugin.jokalariak.size() == 1){
            
        }else if(plugin.bozketa){
            
        }
        else if(plugin.bozkak *100 / plugin.jokalariak.size() > 60){
            plugin.bozketa = true;
            Integer arena = 0;
            String arena2 = "";
            for(String map : bozketa.keySet()){
                if(bozketa.get(map) > arena ){
                    arena2 = map;
                    arena = bozketa.get(map);
                }
            }
            if(arena2.equalsIgnoreCase("b")){
                plugin.jokoa = "Destroy The Nexus";
            }
            plugin.start(arena2);
            bozketa.clear();
        }
    }
}
