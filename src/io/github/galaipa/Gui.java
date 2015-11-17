package io.github.galaipa;

import static io.github.galaipa.GameListener.item;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
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
    public static Inventory joinGUI2 = Bukkit.createInventory(null, 9, ChatColor.RED +"TheTowers: Aukeratu taldea");
    public static Inventory joinGUI = Bukkit.createInventory(null, 9, ChatColor.RED +"TheTowers");
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
        joinGUI.setItem(0,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "TheTowers"));
        joinGUI.setItem(1,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "TheTowers"));
        joinGUI.setItem(2,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "TheTowers"));
        joinGUI.setItem(3,item(Material.BOW,0,1,ChatColor.BLUE + "Jolastu"));
        joinGUI.setItem(4,item(Material.BOOK_AND_QUILL,0,1,ChatColor.BLUE + "Estatistikak"));
       // joinGUI.setItem(4,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "TheTowers"));
        joinGUI.setItem(5,item(Material.PUMPKIN,1,1,ChatColor.BLUE + "Ikuslea"));
        joinGUI.setItem(6,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "TheTowers"));
        joinGUI.setItem(7,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "TheTowers"));
        joinGUI.setItem(8,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "TheTowers"));
        setGuiVip();
        setGuiEguraldia();
        setGuiOrdua();
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
                    plugin.join(player,"ausaz");
                }
                else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Talde Gorria")){
                    if(!player.hasPermission("tt.vip")){
                        player.closeInventory();
                        player.sendMessage(ChatColor.GREEN +"[TheTowers] " +ChatColor.RED + "Vip-ek bakarrik aukera dezakete zein taldetan sartu");
                    }else{
                        plugin.join(player,"gorria");
                    }
                }
                else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Talde Urdina")){
                    if(!player.hasPermission("tt.vip")){
                        player.closeInventory();
                        player.sendMessage(ChatColor.GREEN +"[TheTowers] " +ChatColor.RED + "Vip-ek bakarrik aukera dezakete zein taldetan sartu");
                    }else{
                        plugin.join(player,"urdina");
                    }
                }
                 
        }}else if (event.getInventory().getName().equals(joinGUI.getName())) {
            if(event.getCurrentItem().getItemMeta() != null){
                ItemStack clicked = event.getCurrentItem(); 
                event.setCancelled(true);
                if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Ikuslea")){
                        if(plugin.inGame){
                            player.closeInventory();
                            plugin.joinSpectator(player);
                        }else{
                            player.sendMessage(ChatColor.GREEN +"[TheTowers] " +ChatColor.RED + "Ez da inor jolasten ari");
                        }
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Jolastu")){
                    player.openInventory(joinGUI2);
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Estatistikak")){
                    player.sendMessage(ChatColor.GREEN +"==================================================");
                    player.sendMessage(ChatColor.BLUE +"                    THE TOWERS ESTATISTIKAK");
                    player.sendMessage(ChatColor.GREEN +"   ");
                    player.sendMessage(ChatColor.BLUE +"Irabazitako partidak: "+ ChatColor.YELLOW + GEAPI.infoStat("ttirabazi", player));
                    player.sendMessage(ChatColor.BLUE +"Jokatutako partidak: "+ ChatColor.YELLOW + GEAPI.infoStat("ttjokatu", player));
                    player.sendMessage(ChatColor.GREEN +"==================================================");    
                    player.closeInventory();
                }}
            
        }else if (event.getInventory().getName().equals(vipGUI.getName())) {
                ItemStack clicked = event.getCurrentItem(); 
                event.setCancelled(true);
                if(!player.hasPermission("tt.vip")){
                    player.sendMessage(ChatColor.GREEN +"[TheTowers] " +ChatColor.RED + "Vip-ek bakarrik erabil dezakete hori");
                    player.closeInventory();
                    return;
                }
                if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Eguraldia")){
                    player.openInventory(eguraldiaGUI);
                } else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Ordua")){
                    player.openInventory(orduaGUI);
                } else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Mapa")){
                    player.openInventory(mapaGUI);
                }
        }else if (event.getInventory().getName().equals(eguraldiaGUI.getName())) {
                ItemStack clicked = event.getCurrentItem(); 
                event.setCancelled(true);
                World world = Bukkit.getServer().getWorld(plugin.getConfig().getString("Win.urdina.World"));
                if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Eguzkia")){
                    world.setStorm(false);
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Euria")){
                    world.setStorm(true);
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Euria")){
                    world.setStorm(true);
                    world.setThundering(true);
                    world.setThunderDuration(5);
                }
                player.closeInventory();
        }else if (event.getInventory().getName().equals(orduaGUI.getName())) {
                ItemStack clicked = event.getCurrentItem(); 
                event.setCancelled(true);
                World world = Bukkit.getServer().getWorld(plugin.getConfig().getString("Win.urdina.World"));
                if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Goiza")){
                    world.setTime(8000);
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Eguerdia")){
                    world.setTime(12000);
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Gaua")){
                    world.setTime(20000);
                }
                player.closeInventory();
        }else if (event.getInventory().getName().equals(eguraldiaGUI.getName())) {
                ItemStack clicked = event.getCurrentItem(); 
                event.setCancelled(true);
                if(botoa.contains(player)){
                    player.closeInventory();
                    player.sendMessage("Botoa behin bakarrik eman dezakezu");
                    return;
                }
                if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Klasikoa")){
                    a++;
                    botoa.add(player);
                    player.closeInventory();
                    player.sendMessage(ChatColor.YELLOW +"Mapa klasikoa aukeratu duzu");
                } else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Remake")){
                    b++;
                    botoa.add(player);
                    player.closeInventory();
                    player.sendMessage(ChatColor.YELLOW +"Remake mapa aukeratu duzu");
                }
        }
    }
    public static Inventory vipGUI = Bukkit.createInventory(null, 9, ChatColor.RED +"TheTowers: Vip aukerak");
    public  static void setGuiVip() {
        vipGUI.setItem(0,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        vipGUI.setItem(1,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        vipGUI.setItem(2,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        vipGUI.setItem(3,item(Material.DAYLIGHT_DETECTOR,1,1,ChatColor.YELLOW + "Eguraldia"));
        vipGUI.setItem(4,item(Material.WATCH,0,1,ChatColor.YELLOW + "Ordua"));
        vipGUI.setItem(5,item(Material.MAP,1,1,ChatColor.YELLOW + "Mapa"));
        vipGUI.setItem(6,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        vipGUI.setItem(7,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        vipGUI.setItem(8,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
    }
    
    public static void openGuiVip(Player p){
        p.openInventory(vipGUI);
    }
    public static Inventory eguraldiaGUI = Bukkit.createInventory(null, 9, ChatColor.RED +"TT Vip aukerak : Eguraldia");
    public  static void setGuiEguraldia() {
        eguraldiaGUI.setItem(0,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        eguraldiaGUI.setItem(1,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        eguraldiaGUI.setItem(2,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        eguraldiaGUI.setItem(3,item(Material.DOUBLE_PLANT,0,1,ChatColor.YELLOW + "Eguzkia"));
        eguraldiaGUI.setItem(4,item(Material.WATER_BUCKET,0,1,ChatColor.YELLOW + "Euria"));
        eguraldiaGUI.setItem(5,item(Material.LAVA_BUCKET,0,1,ChatColor.YELLOW + "Ekaitza"));
        eguraldiaGUI.setItem(6,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        eguraldiaGUI.setItem(7,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
       eguraldiaGUI.setItem(8,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
    }
    public static Inventory orduaGUI = Bukkit.createInventory(null, 9, ChatColor.RED +"TT Vip aukerak : Ordua");
    public  static void setGuiOrdua() {
        orduaGUI.setItem(0,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        orduaGUI.setItem(1,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        orduaGUI.setItem(2,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        orduaGUI.setItem(3,item(Material.WOOL,4,1,ChatColor.YELLOW + "Goiza"));
        orduaGUI.setItem(4,item(Material.WOOL,14,1,ChatColor.YELLOW + "Eguerdia"));
        orduaGUI.setItem(5,item(Material.WOOL,15,1,ChatColor.YELLOW + "Gaua"));
        orduaGUI.setItem(6,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        orduaGUI.setItem(7,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        orduaGUI.setItem(8,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
    }   
    public static Inventory mapaGUI = Bukkit.createInventory(null, 9, ChatColor.RED +"TT Vip aukerak : Mapa");
    public  static void setGuiMapa() {
        mapaGUI.setItem(0,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        mapaGUI.setItem(1,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        mapaGUI.setItem(2,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        mapaGUI.setItem(3,item(Material.NETHER_BRICK,1,1,ChatColor.YELLOW + "Klasikoa"));
        mapaGUI.setItem(4,item(Material.GRASS,1,1,ChatColor.YELLOW + "Remake"));
       // mapaGUI.setItem(5,item(Material.WOOL,15,1,ChatColor.YELLOW + "Gaua"));
        mapaGUI.setItem(6,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        mapaGUI.setItem(7,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
        mapaGUI.setItem(8,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Vip ezarpenak"));
    }
    public static void maingui(Jokalaria j){
        int i;
        if(j.getTeam().getID().equalsIgnoreCase("urdina")){
            i = 11;
        }else{
            i = 14;
        }
        Inventory inv = j.getPlayer().getInventory();
        inv.setItem(0,item(Material.STAINED_GLASS_PANE,i,1,ChatColor.GREEN + "Game Erauntsia TheTowers"));
        inv.setItem(1,item(Material.STAINED_GLASS_PANE,i,1,ChatColor.GREEN + "Game Erauntsia TheTowers"));
        inv.setItem(2,item(Material.STAINED_GLASS_PANE,i,1,ChatColor.GREEN + "Game Erauntsia TheTowers"));
        inv.setItem(3,item(Material.STAINED_CLAY,14,1,ChatColor.YELLOW + "Jokoa hasteko bozkatu"));
        inv.setItem(4,item(Material.STAINED_GLASS_PANE,i,1,ChatColor.GREEN + "Game Erauntsia TheTowers"));
        inv.setItem(5,item(Material.BARRIER,0,1,ChatColor.RED  + "Jokotik irten"));
        inv.setItem(6,item(Material.STAINED_GLASS_PANE,i,1,ChatColor.GREEN + "Game Erauntsia TheTowers"));
        inv.setItem(7,item(Material.STAINED_GLASS_PANE,i,1,ChatColor.GREEN + "Game Erauntsia TheTowers"));
        inv.setItem(8,item(Material.STAINED_GLASS_PANE,i,1,ChatColor.GREEN + "Game Erauntsia TheTowers"));
    }
    
      @EventHandler
      public void onInventoryClick2(PlayerInteractEvent event){
          if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK ){
              Player p = event.getPlayer();
              if(plugin.isInGame(p)){
              if(p.getItemInHand() != null && p.getItemInHand().hasItemMeta() && p.getItemInHand().getItemMeta().hasDisplayName()){
                   if(p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW  +"Jokoa hasteko bozkatu")){
                      event.setCancelled(true);
                      p.getInventory().setItem(3,item(Material.STAINED_CLAY,5,1,ChatColor.GREEN + "Jokoa hasteko bozkatu duzu"));
                      bozkatu(p);
                  }else if(p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED  +"Jokotik irten")){
                      event.setCancelled(true);
                      plugin.leave(p);
                  }else if(p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN  +"Game Erauntsia TheTowers")){
                      event.setCancelled(true);
                  }else if(p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Jokoa hasteko bozkatu duzu")){
                      event.setCancelled(true);
                  }
          }
              }
          }
      }
    public void bozkatu(Player p){
        
        plugin.bozkak++;
        p.sendMessage(ChatColor.GREEN +"[TheTowers] " + ChatColor.YELLOW + "Jokoa hasteko bozkatu duzu. Jokalarien %60ak bozkatzean hasiko da");
        
        if(plugin.jokalariak.size() == 1){
            
        }else if(plugin.bozketa){
            
        }
        else if(plugin.bozkak *100 / plugin.jokalariak.size() > 60){
            plugin.bozketa = true;
            plugin.start();
        }
    }
}
