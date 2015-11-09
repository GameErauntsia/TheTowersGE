package io.github.galaipa;


import java.util.HashMap;
import org.bukkit.ChatColor;
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
        static String arena2;
        HashMap<String, Team> map = new HashMap<>();
        public TheTowersGE plugin;
        public GameListener(TheTowersGE instance) {
            plugin = instance;
        }
      @EventHandler
      public void onDeath(PlayerDeathEvent e){
        if(plugin.inGame){
            if(plugin.isInGame(e.getEntity())){
                Player killed = e.getEntity();
                e.setDeathMessage("");
                if(killed.getKiller() != null && killed.getKiller() instanceof Player){
                    plugin.Broadcast(ChatColor.YELLOW + killed.getKiller().getName() + "-(e)k " + killed.getName() + " hil du");
                }
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
                e.getPlayer().setScoreboard(plugin.board);
            }
        }
      }
      @EventHandler
      public void onLeave(PlayerQuitEvent e){
        if(plugin.inGame){
            if(plugin.isInGame(e.getPlayer())){
                map.put(e.getPlayer().getName(), plugin.getJokalaria(e.getPlayer()).getTeam());
                Jokalaria j = plugin.getJokalaria(e.getPlayer());
                plugin.jokalariak.remove(j);
                j.getTeam().removePlayer(j);
            }
        }
      }
      @EventHandler
      public void onJoin(PlayerJoinEvent e){
        if(plugin.inGame){
            if(map.get(e.getPlayer().getName()) != null){       
                Jokalaria j = new Jokalaria(e.getPlayer());
                plugin.jokalariak.add(j);
                j.setTeam(map.get(e.getPlayer().getName()));
                Player p = j.getPlayer();
                p.teleport(j.getTeam().getSpawn());
                p.setHealth(p.getMaxHealth());
                p.setScoreboard(plugin.board);
                plugin.setArmour(j);
                plugin.giveItems(j);
            }else{
                e.getPlayer().getInventory().clear();
                e.getPlayer().getInventory().setArmorContents(null);
                e.getPlayer().teleport(plugin.mainLobby);
            }
        }
      }
      
      @EventHandler
      public void onPickUp(PlayerPickupItemEvent e){
        if(plugin.inGame){
            if(plugin.isInGame(e.getPlayer())){
                ItemStack i = e.getItem().getItemStack();
                if(i.hasItemMeta() && i.getItemMeta().hasDisplayName() && i.getItemMeta().getDisplayName().equalsIgnoreCase("Arropa")){
                    e.getItem().remove();
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
                           plugin.Broadcast(ChatColor.BLUE + "Talde urdinak tantoa egin du (" + plugin.scoreUrdina.getScore() + ")");
                           if(plugin.scoreUrdina.getScore() == 10){
                               plugin.amaiera(plugin.urdina,plugin.gorria);
                           }
                        }
                        else{
                            plugin.scoreGorria.setScore((plugin.scoreGorria.getScore()+1));
                            plugin.Broadcast(ChatColor.RED + "Talde gorriak tantoa egin du (" + plugin.scoreGorria.getScore() + ")");
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
                    p.sendMessage("Point A (Gorria)");
                    e.setCancelled(true);
                }
                else if(izena.equalsIgnoreCase(ChatColor.GREEN +"Point B (Gorria)")){
                    l2_red = e.getBlock().getLocation();
                    p.sendMessage("Point B (Gorria)");
                    e.setCancelled(true);
                }
                else if(izena.equalsIgnoreCase(ChatColor.GREEN +"Point A (Urdina)")){
                    l1_blue = e.getBlock().getLocation();
                    p.sendMessage("Point A (Urdina)");
                    e.setCancelled(true);
                }
                else if(izena.equalsIgnoreCase(ChatColor.GREEN +"Point B (Urdina)")){
                    l2_blue = e.getBlock().getLocation();
                    p.sendMessage("Point B (Urdina)");
                    e.setCancelled(true);
                }else if(izena.equalsIgnoreCase(ChatColor.GREEN +"Burdina")){
                    plugin.iron = e.getBlock().getLocation();
                    p.sendMessage("Burdina");
                    e.setCancelled(true);
                }else if(izena.equalsIgnoreCase(ChatColor.GREEN +"Exp")){
                    plugin.exp = e.getBlock().getLocation();
                    p.sendMessage("Exp");
                    e.setCancelled(true);
                }else if(izena.equalsIgnoreCase(ChatColor.GREEN +"Lobby")){
                    plugin.lobby = e.getBlock().getLocation();
                    p.sendMessage("Lobby-a");
                    e.setCancelled(true);
                }
                
                else if(izena.equalsIgnoreCase(ChatColor.GREEN +"Gorde")){
                    plugin.saveSelection(arena2,"gorria", l1_red, l2_red);
                    plugin.saveSelection(arena2,"urdina", l1_blue, l2_blue);
                    plugin.SaveSpawn(arena2,plugin.iron, "iron");
                    plugin.SaveSpawn(arena2,plugin.exp, "exp");
                    plugin.SaveSpawn(arena2,plugin.lobby, "lobby");
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
                      else if(p.hasPermission("tt.admin")){
                          
                      }
                      else{
                       event.setCancelled(true);
                       p.sendMessage(ChatColor.GREEN +"[TheTowers]" + ChatColor.RED + "You can't use command during the game");
                      }

              }
          }
      }
        public static void adminGui(Player p, String arena){
           arena2 = arena;
           p.getInventory().clear();
           Inventory inv = p.getInventory();
           inv.addItem(item(Material.STAINED_CLAY,14,1,ChatColor.GREEN + "Point A (Gorria)"));
           inv.addItem(item(Material.STAINED_CLAY,14,1,ChatColor.GREEN + "Point B (Gorria)"));
           inv.addItem(item(Material.STAINED_CLAY,11,1,ChatColor.GREEN + "Point A (Urdina)"));
           inv.addItem(item(Material.STAINED_CLAY,11,1,ChatColor.GREEN + "Point B (Urdina)"));
           inv.addItem(item(Material.STAINED_CLAY,7,1,ChatColor.GREEN + "Lobby"));
           inv.addItem(item(Material.STAINED_CLAY,9,1,ChatColor.GREEN + "Burdina"));
           inv.addItem(item(Material.STAINED_CLAY,1,1,ChatColor.GREEN + "Exp"));
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
