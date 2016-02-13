package io.github.galaipa;


import java.util.HashMap;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
        public static HashMap<String, Team> map = new HashMap<>();
        public TheTowersGE plugin;
        public GameListener(TheTowersGE instance) {
            plugin = instance;
        }
      @EventHandler
      public void onDeathTT(PlayerDeathEvent e){
        if(plugin.inGame){
            if(plugin.isInGame(e.getEntity())){
                final Player killed = e.getEntity();
                e.setDeathMessage("");
                if(killed.getKiller() != null && killed.getKiller() instanceof Player){
                    plugin.Broadcast(ChatColor.YELLOW + killed.getKiller().getName() + "-(e)k " + killed.getName() + " hil du");
                }
                Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable(){
                    @Override
                     public void run()
                    {
                      PacketPlayInClientCommand packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
                      ((CraftPlayer)killed).getHandle().playerConnection.a(packet);
                    }
                  }, 1);
        } 
        }
          } 
    @EventHandler
        public void noTeamKillTT(EntityDamageByEntityEvent event){
            if(event.getEntity() instanceof Player){
                Player damaged = (Player) event.getEntity();
                if(plugin.inGame && plugin.isInGame(damaged)){
                    if (event.getDamager() instanceof Arrow){
                        Jokalaria j1 = plugin.getJokalaria(damaged);
                        Jokalaria j2 = plugin.getJokalaria((Player)(((Arrow)event.getDamager()).getShooter()));
                        if(j1.getTeam().equals(j2.getTeam())){
                            event.setCancelled(true);
                        }
                    }else if(event.getDamager() instanceof Player){
                        Jokalaria j1 = plugin.getJokalaria(damaged);
                        Jokalaria j2 = plugin.getJokalaria((Player) event.getDamager());
                        if(j1.getTeam().equals(j2.getTeam())){
                            event.setCancelled(true);
                        }
                    }
                }
            } 
        }
      @EventHandler(priority = EventPriority.HIGHEST) 
      public void onRespawnTT(PlayerRespawnEvent e){
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
      public void onLeaveTT(PlayerQuitEvent e){
        if(plugin.inGame){
            Player p = e.getPlayer();
            if(plugin.isInGame(p)){
                map.put(p.getName(), plugin.getJokalaria(p).getTeam());
                plugin.leave(p);
            }
        }
      }
      @EventHandler
      public void onJoinTT(PlayerJoinEvent e){
        Player p = e.getPlayer();
        String izena = p.getName();
        if(plugin.inGame){
            if(map.get(izena) != null){       
                Jokalaria j = new Jokalaria(p);
                plugin.jokalariak.add(j);
                j.setTeam(map.get(izena));
                p.teleport(j.getTeam().getSpawn());
                p.setHealth(p.getMaxHealth());
                p.setScoreboard(plugin.board);
                p.getInventory().clear();
                p.getInventory().setArmorContents(null);
                plugin.setArmour(j);
                plugin.giveItems(j);
            }else if (p.getLocation().getWorld().getName().equalsIgnoreCase("TheTowersMapa")){
                p.teleport(plugin.mainLobby);
                p.getInventory().clear();
                p.getInventory().setArmorContents(null);
            }
        }else if(p.getLocation().getWorld().getName().equalsIgnoreCase("TheTowersMapa")){
                p.teleport(plugin.mainLobby);
                p.getInventory().clear();
                p.getInventory().setArmorContents(null);
        }
      }
      
      @EventHandler
      public void onPickUpTT(PlayerPickupItemEvent e){
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
     public void tantoa(Player p,int Irabazi){
        p.teleport(plugin.getJokalaria(p).getTeam().getSpawn());
        if(plugin.getJokalaria(p).getTeam().getID().equalsIgnoreCase("urdina")){
           plugin.scoreUrdina.setScore((plugin.scoreUrdina.getScore()+1)); 
           plugin.Broadcast(ChatColor.BLUE +  p.getName() + "-(e)k tantoa egin du (" + plugin.scoreUrdina.getScore() + ")");
           if(plugin.scoreUrdina.getScore() == 10){
               plugin.amaiera(plugin.urdina,plugin.gorria);
           }
        }
        else{
            plugin.scoreGorria.setScore((plugin.scoreGorria.getScore()+1));
            plugin.Broadcast(ChatColor.RED + p.getName() + "-(e)k tantoa egin du (" + plugin.scoreGorria.getScore() + ")");
             if(plugin.scoreGorria.getScore() == Irabazi){
                       plugin.amaiera(plugin.gorria,plugin.urdina);
                   }
        }
        for(Jokalaria j : plugin.jokalariak){
            j.getPlayer().getWorld().playSound(j.getPlayer().getLocation(),Sound.NOTE_PLING, 10, 1);
        }
     }
      @EventHandler
     public void onMoveTT(PlayerMoveEvent e) {
        if(plugin.jokoa.equalsIgnoreCase("The Towers")){
            if(plugin.inGame){
                if(plugin.isInGame(e.getPlayer())){
                        if(plugin.getJokalaria(e.getPlayer()).getTeam().getWin().contains(e.getPlayer().getLocation())){
                            if(!e.getPlayer().isDead()){
                                tantoa(e.getPlayer(),10);
                        }
                        }
                }
            }
        }
      }
     @EventHandler
     public void DTNtantoa(BlockBreakEvent e){
        if(plugin.jokoa.equalsIgnoreCase("Destroy The Nexus")){
            if(plugin.inGame){
                if(plugin.isInGame(e.getPlayer())){
                    if(plugin.getJokalaria(e.getPlayer()).getTeam().getWin().contains(e.getBlock().getLocation())){
                        tantoa(e.getPlayer(),3);
                    }
                }
            }
        }
     }
     @EventHandler
     public void onAdminMenuTT(BlockPlaceEvent e){
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
                }/*else if(izena.equalsIgnoreCase(ChatColor.GREEN +"Lobby")){
                    plugin.lobby = e.getBlock().getLocation();
                    p.sendMessage("Lobby-a");
                    e.setCancelled(true);
                }*/
                
                else if(izena.equalsIgnoreCase(ChatColor.GREEN +"Gorde")){
                    plugin.saveSelection(arena2,"gorria", l1_blue, l2_blue);
                    plugin.saveSelection(arena2,"urdina", l1_red, l2_red);
                    plugin.SaveSpawn(arena2,plugin.iron, "iron");
                    plugin.SaveSpawn(arena2,plugin.exp, "exp");
                  //  plugin.SaveSpawn(arena2,plugin.lobby, "lobby");
                    plugin.admin = false;
                    p.getInventory().clear();
                    e.setCancelled(true);
                }

             }
         }
     }
     
      @EventHandler
      public void PlayerCommand(PlayerCommandPreprocessEvent event) {
              Player p = event.getPlayer();
              if(plugin.isInGame(event.getPlayer())){
                      if(event.getMessage().toLowerCase().startsWith("/thetowers")){
                        }
                      else if(event.getMessage().toLowerCase().startsWith("/t")){
                        }
                      else if(event.getMessage().toLowerCase().startsWith("/login")){
                        }
                      else if(event.getMessage().toLowerCase().startsWith("/laguntza")){
                        }
                      else if(p.hasPermission("tt.admin")){
                      }
                      else{
                       event.setCancelled(true);
                       p.sendMessage(ChatColor.GREEN + plugin.jokoa + ChatColor.RED + "Ezin duzu komandorik erabili jolasten zaudenean");
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
          // inv.addItem(item(Material.STAINED_CLAY,7,1,ChatColor.GREEN + "Lobby"));
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
