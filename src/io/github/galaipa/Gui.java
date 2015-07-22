package io.github.galaipa;

import static io.github.galaipa.GameListener.item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class Gui implements Listener {
     public TheTowersGE plugin;
    public Gui(TheTowersGE instance) {
            plugin = instance;
        }
    public static Inventory joinGUI = Bukkit.createInventory(null, 9, ChatColor.RED +"TheTowers: Aukeratu taldea");
    public  static void setGui() {
        joinGUI.setItem(0,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu taldea"));
        joinGUI.setItem(1,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu taldea"));
        joinGUI.setItem(2,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu taldea"));
        joinGUI.setItem(3,item(Material.WOOL,1,1,ChatColor.WHITE + "Ausaz aukeratu"));
        joinGUI.setItem(4,item(Material.WOOL,14,1,ChatColor.RED + "Talde Gorria"));
        joinGUI.setItem(5,item(Material.WOOL,11,1,ChatColor.BLUE + "Talde Urdina"));
        joinGUI.setItem(6,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu taldea"));
        joinGUI.setItem(7,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu taldea"));
        joinGUI.setItem(8,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu taldea"));
        setGuiVip();
        setGuiEguraldia();
        setGuiOrdua();
    }
    public static void openGui(Player p){
        p.openInventory(joinGUI);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked(); 
        if (event.getInventory().getName().equals(joinGUI.getName())) {
            if(event.getCurrentItem().getItemMeta() != null ){
                ItemStack clicked = event.getCurrentItem(); 
                event.setCancelled(true);
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
        }}else if (event.getInventory().getName().equals(vipGUI.getName())) {
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
                    player.closeInventory();
                    player.sendMessage(ChatColor.GREEN +"[TheTowers] " +ChatColor.RED + "Oraindik ez dago erabilgarri");
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

}
