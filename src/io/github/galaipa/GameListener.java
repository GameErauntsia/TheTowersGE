package io.github.galaipa;


import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GameListener implements Listener{
        private Location l1_blue;
        private Location l2_blue;
        private Location l1_red;
        private Location l2_red;
        HashMap<String, Team> map = new HashMap<>();
        public TheTowersGE plugin;
        public GameListener(TheTowersGE instance) {
            plugin = instance;
        }
      @EventHandler
      public void onDeath(PlayerDeathEvent e){
        if(plugin.inGame){
            if(plugin.isInGame(e.getEntity())){
                e.setDeathMessage("");
            }
        } 
          } 
    @EventHandler
        public void noTeamKill(EntityDamageByEntityEvent event){
        if((event.getEntity() instanceof Player) && (event.getDamager() instanceof Player)){
        if(plugin.inGame){
            if(plugin.isInGame((Player) event.getEntity())){
                Jokalaria j1 = plugin.getJokalaria((Player) event.getEntity());
                Jokalaria j2 = plugin.getJokalaria((Player) event.getDamager());
                if(j1.getTeam().equals(j2.getTeam())){
                    event.setCancelled(true);
                }
            }
        } }else if (event.getDamager() instanceof Arrow) {
        if(plugin.inGame){
            if(plugin.isInGame((Player) event.getEntity())){
                Jokalaria j1 = plugin.getJokalaria((Player) event.getEntity());
                Jokalaria j2 = plugin.getJokalaria((Player)(((Arrow)event.getDamager()).getShooter()));
                if(j1.getTeam().equals(j2.getTeam())){
                    event.setCancelled(true);
                }
            }
            }
        }
          }
      @EventHandler(priority = EventPriority.HIGHEST) 
      public void onRespawn(PlayerRespawnEvent e){
        if(plugin.inGame){
            if(plugin.isInGame(e.getPlayer())){
                Jokalaria j = plugin.getJokalaria(e.getPlayer());
                e.setRespawnLocation(j.getTeam().getSpawn());
                plugin.setArmour(j);
                plugin.giveItems(j);
            }
        }
      }
      @EventHandler
      public void onLeave(PlayerQuitEvent e){
        if(plugin.inGame){
            if(plugin.isInGame(e.getPlayer())){
                map.put(e.getPlayer().getName(), plugin.getJokalaria(e.getPlayer()).getTeam());
                plugin.jokalariak.remove(plugin.getJokalaria(e.getPlayer()));
            }
        }
      }
      @EventHandler
      public void onJoin(PlayerJoinEvent e){
        if(plugin.inGame){
            if(map.get(e.getPlayer().getName()) != null){
                Team t = map.get(e.getPlayer().getName());
                Jokalaria j = new Jokalaria(e.getPlayer());
                plugin.jokalariak.add(j);
                j.setTeam(t);
                j.getPlayer().teleport(j.getTeam().getSpawn());
            }
        }
      }
      
      @EventHandler
      public void onPickUp(PlayerPickupItemEvent e){
        if(plugin.inGame){
            if(plugin.isInGame(e.getPlayer())){
                ItemStack i = e.getItem().getItemStack();
                if(i.equals(plugin.getColorArmor(Material.LEATHER_HELMET,Color.BLUE)) ||
                    i.equals(plugin.getColorArmor(Material.LEATHER_CHESTPLATE,Color.BLUE))||
                    i.equals(plugin.getColorArmor(Material.LEATHER_LEGGINGS,Color.BLUE))||
                    i.equals(plugin.getColorArmor(Material.LEATHER_BOOTS,Color.BLUE))||
                    i.equals(plugin.getColorArmor(Material.LEATHER_HELMET,Color.RED))||
                    i.equals(plugin.getColorArmor(Material.LEATHER_BOOTS,Color.RED))||
                    i.equals(plugin.getColorArmor(Material.LEATHER_BOOTS,Color.RED))||
                    i.equals(plugin.getColorArmor(Material.LEATHER_BOOTS,Color.RED))){
                    e.setCancelled(true);
                }
            }
        }
      }
      @EventHandler
     public void onMove(PlayerMoveEvent e) {
        if(plugin.inGame){
            if(plugin.isInGame(e.getPlayer())){
                    if(plugin.getJokalaria(e.getPlayer()).getTeam().getWin().contains(e.getPlayer().getLocation())){
                        e.getPlayer().teleport(plugin.getJokalaria(e.getPlayer()).getTeam().getSpawn());
                        if(plugin.getJokalaria(e.getPlayer()).getTeam().getID().equalsIgnoreCase("urdina")){
                           plugin.scoreUrdina.setScore((plugin.scoreUrdina.getScore()+1)); 
                           if(plugin.scoreUrdina.getScore() == 10){
                               plugin.amaiera(plugin.urdina,plugin.gorria);
                           }
                        }
                        else{
                            plugin.scoreGorria.setScore((plugin.scoreGorria.getScore()+1));
                             if(plugin.scoreGorria.getScore() == 10){
                                       plugin.amaiera(plugin.gorria,plugin.urdina);
                                   }
                        }
                        for(Jokalaria j : plugin.jokalariak){
                            j.getPlayer().getWorld().playSound(j.getPlayer().getLocation(),Sound.NOTE_PLING, 10, 1);
                        }
                    }
            }
        }
      }
     @EventHandler
     public void onAdminMenu(BlockPlaceEvent e){
         if(plugin.admin){
             Player p = e.getPlayer();
             if(p.getItemInHand().getType() == Material.STAINED_CLAY){
                String izena = p.getItemInHand().getItemMeta().getDisplayName();
                if(izena.equalsIgnoreCase(ChatColor.GREEN +"Point A (Gorria)")){
                    l1_red = e.getBlock().getLocation();
                    e.setCancelled(true);
                }
                else if(izena.equalsIgnoreCase(ChatColor.GREEN +"Point B (Gorria)")){
                    l2_red = e.getBlock().getLocation();
                    e.setCancelled(true);
                }
                else if(izena.equalsIgnoreCase(ChatColor.GREEN +"Point A (Urdina)")){
                    l1_blue = e.getBlock().getLocation();
                    e.setCancelled(true);
                }
                else if(izena.equalsIgnoreCase(ChatColor.GREEN +"Point B (Urdina)")){
                    l2_blue = e.getBlock().getLocation();
                    e.setCancelled(true);
                }
                else if(izena.equalsIgnoreCase(ChatColor.GREEN +"Gorde")){
                    plugin.saveSelection("gorria", l1_red, l2_red);
                    plugin.saveSelection("urdina", l1_blue, l2_blue);
                    plugin.admin = false;
                    p.getInventory().clear();
                    e.setCancelled(true);
                }

             }
         }
     }
     
      @EventHandler
      public void PlayerCommand(PlayerCommandPreprocessEvent event) {
          if(plugin.inGame == true){
              Player p = event.getPlayer();
              if(plugin.isInGame(event.getPlayer())){
                      if(event.getMessage().toLowerCase().startsWith("/thetowers")){
                        }
                      else if(event.getMessage().toLowerCase().startsWith("/t")){
                        }
                      else{
                       event.setCancelled(true);
                       p.sendMessage(ChatColor.GREEN +"[TheTowers]" + ChatColor.RED + "You can't use command during the game");
                      }

              }
          }
      }
        public static void adminGui(Player p){
           p.getInventory().clear();
           Inventory inv = p.getInventory();
           inv.addItem(item(Material.STAINED_CLAY,14,1,ChatColor.GREEN + "Point A (Gorria)"));
           inv.addItem(item(Material.STAINED_CLAY,14,1,ChatColor.GREEN + "Point B (Gorria)"));
           inv.addItem(item(Material.STAINED_CLAY,11,1,ChatColor.GREEN + "Point A (Urdina)"));
           inv.addItem(item(Material.STAINED_CLAY,11,1,ChatColor.GREEN + "Point B (Urdina)"));
           inv.addItem(item(Material.STAINED_CLAY,13,1,ChatColor.GREEN + "Gorde"));
           p.updateInventory();
        }
    public static ItemStack item(Material material, int id, int amount,String name){
            ItemStack b = new ItemStack(material, amount, (short) id);
            ItemMeta metaB = b.getItemMeta();                          
            metaB.setDisplayName(name);
            b.setItemMeta(metaB);
            return b;
    }  
}
