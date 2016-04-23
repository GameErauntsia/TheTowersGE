
package io.github.galaipa;

import de.goldengamerzone.worldreset.WorldReset;
import static io.github.galaipa.GameListener.item;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;


public class TheTowersGE extends JavaPlugin{
    ArrayList<Jokalaria> jokalariak = new ArrayList<>();
    ArrayList<Player> ikusleak = new ArrayList<>();
    public static HashMap<Block, Material> blokeak = new HashMap<>();
    Team urdina,gorria;
    Boolean admin,bozketa,itxi,inGame = false;
    Score scoreUrdina,scoreGorria;
    Location exp,iron,mainLobby,LobbyUr,LobbyGor;
    int scheduler,bozkak;
    Scoreboard board;
    String jokoa = "The Towers";
    @Override
    public void onEnable(){
        defaultValues();
        getConfig().options().copyDefaults(true);
        saveConfig();
        getServer().getPluginManager().registerEvents(new GameListener(this),this);
        getServer().getPluginManager().registerEvents(new Gui(this),this);
        getServer().getPluginManager().registerEvents(new SignListener(this),this);
        defaultValues();
        hookPlayerPoints();
        setupGEAPI();
    }
    @Override
    public void onDisable(){
        if(inGame){
            System.out.print(jokalariak.toString());
            for(Jokalaria j : jokalariak){
                leave(j.getPlayer());
            }
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("thetowers")){
            if(args.length < 1){
                GEAPI.gehituStat("ttirabazi", 1, p);
            }else if (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("sartu")){
                if(getJokalaria(p) != null){
                    p.sendMessage(ChatColor.GREEN + jokoa +ChatColor.RED + "Dagoeneko bazaude sartuta");
                    return true;
                }else{
                    Gui.openGui(p);
                    return true;
                }
            }else if (args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("atera")){
                if(ikusleak.contains(p)){
                    leaveSpectator(p);
                }else if(jokalariak.contains(getJokalaria(p))){
                    leave(p);
                }else{
                     p.sendMessage(ChatColor.GREEN + jokoa +ChatColor.RED + "Ez zaude partidan");
                }
                return true;
            }
        }else if(cmd.getName().equalsIgnoreCase("thetowersadmin")){
            if(args.length < 1){
                
            }else if(args[0].equalsIgnoreCase("start")){
                start(args[1]);
                return true;
            }else if (args[0].equalsIgnoreCase("spawn")){
                SaveSpawn(args[1],p.getLocation(),args[2]);
                p.sendMessage("Spawn set");
                return true;
            }else if (args[0].equalsIgnoreCase("setup")){
                admin = true;
                p.sendMessage("Admin menu");
                GameListener.adminGui(p,args[1]);
            }else if(args[0].equalsIgnoreCase("join")){
                Player p2 = Bukkit.getServer().getPlayer(args[1]);
                join(p2,args[2]);
                p.sendMessage(args[1] + "jokalaria talde" + args[2]+ "-an sartu duzu");
                return true;
            }else if(args[0].equalsIgnoreCase("puntuazioa")){
                if(args.length < 3){
                     p.sendMessage(ChatColor.RED + "ttadmin puntuazioa gorria urdina");
                    return true;
                }
                scoreUrdina.setScore(Integer.parseInt(args[2])); 
                scoreGorria.setScore(Integer.parseInt(args[1]));
                Broadcast(ChatColor.BLUE + "Administratzaile batek puntuazio aldaketa egin du");
                return true;
            }else if(args[0].equalsIgnoreCase("itxi")){
                if(itxi){
                    itxi = false;
                    p.sendMessage(ChatColor.GREEN + "Irekita");
                }else{
                    itxi = true;
                    p.sendMessage(ChatColor.RED + "Itxita");
                }
                 return true;
            }
        }
        return false;
    }
    public void defaultValues(){
        inGame = false;
        itxi = false;
        urdina = new Team("urdina");
        gorria = new Team("gorria");
        admin = false;
        jokalariak.clear();    
        ikusleak.clear();
        Gui.setGui();
        bozkak = 0;
        bozketa = false;
        GameListener.map.clear();
}
public void join(Player p, String s){
        if(jokalariak.isEmpty()){
            loadLobby();
        }
        Jokalaria j = new Jokalaria(p);
        jokalariak.add(j);
        if(s.equalsIgnoreCase("gorria")){
            j.setTeam(gorria);
            p.teleport(LobbyGor);
            p.sendMessage(ChatColor.GREEN +"["+ jokoa  + "]"+ChatColor.RED + " Talde gorrian sartu zara");
        }else{
            j.setTeam(urdina);
            p.teleport(LobbyUr);
            p.sendMessage(ChatColor.GREEN +"["+ jokoa  + "]"+ChatColor.BLUE + " Talde urdinean sartu zara");
        }
        if(!inGame){
            Gui.maingui(j);
            setArmour(j);
        }else{
            teleportSpawn(j);
            p.setScoreboard(board);
            Broadcast(ChatColor.GREEN + p.getName() +" jokoan sartu da ("+ ChatColor.RED + gorria.getPlayers().size() + ChatColor.GREEN + "/"+ChatColor.BLUE + urdina.getPlayers().size() + ChatColor.GREEN  +")" );
        }
        
    }public void joinRandom(Player p){
         if(urdina.getPlayers().size() <= gorria.getPlayers().size()){
             join(p,"urdina");
         }else{
             join(p,"gorria");
         }
    }
    public void joinSpectator(final Player p){
        ikusleak.add(p);
        p.teleport(exp);
        p.setScoreboard(board);
        p.sendMessage(ChatColor.GREEN +"["+ jokoa  + "]"+ChatColor.RED + " Ikuslegoan sartu zara");
        new BukkitRunnable(){
            @Override
            public void run(){
                p.setGameMode(GameMode.SPECTATOR);
                this.cancel();
             }
        }.runTaskLater(this, 40);
    }public void leaveSpectator(Player p){
        ikusleak.remove(p);
        if(p.isOnline()){
            p.teleport(mainLobby);
            p.sendMessage(ChatColor.GREEN +"["+ jokoa + "]"+ChatColor.RED + " Jokotik irten zara");
            p.setGameMode(GameMode.SURVIVAL);
        }
    }
    public void leave(Player p){
       Jokalaria j = getJokalaria(p);
       if(!inGame || p.isOp()){
           j.returnInv();
       }
       if(p.isOnline()){
           p.teleport(mainLobby);
           p.sendMessage(ChatColor.GREEN +"["+ jokoa + "]"+ChatColor.RED + " Jokotik irten zara");
           p.updateInventory();
       }
       jokalariak.remove(j);
       j.getTeam().removePlayer(j);
       Broadcast(ChatColor.RED + p.getName() +" jokotik irten da"+ChatColor.WHITE +"("+ ChatColor.RED + gorria.getPlayers().size() + ChatColor.WHITE + "/"+ChatColor.BLUE + urdina.getPlayers().size() + ChatColor.WHITE  +")");
       if(jokalariak.isEmpty()){
           reset();
       }
    }
    public void teleportSpawn(final Jokalaria j){
            final Player p = j.getPlayer();
            p.teleport(j.getTeam().getSpawn());
            new BukkitRunnable(){
                @Override
                public void run(){
                    p.setHealth(p.getMaxHealth());
                    setArmour(j); 
                    giveItems(j);
                    this.cancel();
                 }
            }.runTaskLater(this, 40);
    }
     public void start(final String arena){
            loadSelection(urdina,arena);
            loadSelection(gorria,arena);
            inGame = true;
            Broadcast(ChatColor.GREEN + jokoa + ChatColor.GREEN + " jokoa orain hasiko da");
            scheduler =  Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
                int countdown = 10;
                @Override
                public void run(){
                    for(Jokalaria j : jokalariak){
                        Player p = j.getPlayer();
                        p.setLevel(countdown);
                        p.getWorld().playSound(p.getLocation(),Sound.NOTE_STICKS, 10, 1);
                        sendTitle(p,20,40,20,ChatColor.YELLOW + Integer.toString(countdown),"");
                    }
                    countdown--;
                    if (countdown < 0) {
                        Broadcast(ChatColor.GREEN + "-----------------------------------------------");
                        Broadcast(ChatColor.BOLD.toString());
                        Broadcast(ChatColor.WHITE + "                         §l" + jokoa);
                        Broadcast(ChatColor.BOLD.toString());
                        Broadcast(ChatColor.GREEN + "                          " + "Zorte on guztiei!") ;
                        Broadcast(ChatColor.BOLD.toString());
                        Broadcast(ChatColor.GREEN + "-----------------------------------------------");
                        for(Jokalaria j : jokalariak){
                            j.returnInv();
                            teleportSpawn(j);
                        }
                        setScoreBoard();
                        if(jokoa.equalsIgnoreCase("The Towers")){
                            loadSpawners(arena);
                        }else{
                            Bukkit.getScheduler().cancelTask(scheduler);
                            Blokeak();
                        }
                    }
                }
            },0, 20);

    }
    public void Spawners(){
        Bukkit.getScheduler().cancelTask(scheduler);
        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
            World w = urdina.getSpawn().getWorld();
            @Override
            public void run(){  
                w.dropItemNaturally(exp, new ItemStack(Material.EXP_BOTTLE));
                w.dropItemNaturally(iron, new ItemStack(Material.IRON_INGOT));
            }
        }, 0, 60);
    }
    public void Blokeak(){
        Bukkit.getScheduler().cancelTask(scheduler);
        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
            World w = urdina.getSpawn().getWorld();
            @Override
            public void run(){  
                for(Block b : blokeak.keySet()){
                    b.setType(blokeak.get(b));
                }
                blokeak.clear();
            }
        }, 0, 400);
    }
    public void setScoreBoard(){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("TaldeJokoak", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + jokoa);
        scoreGorria = objective.getScore(ChatColor.RED + "Gorriak:"); 
        scoreUrdina = objective.getScore(ChatColor.BLUE + "Urdinak:");
        scoreGorria.setScore(0);
        scoreUrdina.setScore(0);
        for(Jokalaria j : jokalariak){
            j.getPlayer().setScoreboard(board);
        }
        }
    public void amaiera(Team irabazlea, Team galtzailea){
        inGame = false;
        Broadcast(ChatColor.YELLOW + "------------------------------------------------");
        Broadcast(ChatColor.GREEN + "            "+ jokoa + " partida amaitu da         ");
        Broadcast(ChatColor.GREEN + "            Irabazlea: talde " + irabazlea.getID());
        Broadcast(ChatColor.YELLOW + "------------------------------------------------");
        for(Jokalaria j : irabazlea.getPlayers()){
            Player p = j.getPlayer();
            getPlayerPoints().getAPI().give(p.getUniqueId(), 70);
            p.sendMessage(ChatColor.GREEN + "Zorionak! irabazteagatik 70 puntu lortu dituzu");
            p.teleport(mainLobby);
            GEAPI.gehituStat("ttirabazi",1,p);
            GEAPI.gehituStat("ttjokatu",1,p);
        }
        for(Jokalaria j : galtzailea.getPlayers()){
            Player p = j.getPlayer();
            getPlayerPoints().getAPI().give(p.getUniqueId(), 20);
            p.getPlayer().sendMessage(ChatColor.GREEN + "Zorionak! jolasteagatik 20 puntu lortu dituzu");
            p.getPlayer().teleport(mainLobby);
            GEAPI.gehituStat("ttjokatu",1,p);
        }
        reset();
    }
    public void reset(){
        for(Player p : ikusleak){
            if(p.isOnline()){
                p.teleport(mainLobby);
                p.setGameMode(GameMode.SURVIVAL);
            }
        }
        Bukkit.getScheduler().cancelTask(scheduler);
        defaultValues(); 
        WorldReset.resetWorld("TheTowersMapa");
    }
        
    
    public void setArmour(Jokalaria j){
        Player p = j.getPlayer();
        p.getInventory().setArmorContents(null); 
        p.getInventory().setHelmet(getColorArmor(Material.LEATHER_HELMET,j.getTeam().getColor())); // Koloretako armadura jarri
        p.getInventory().setChestplate(getColorArmor(Material.LEATHER_CHESTPLATE,j.getTeam().getColor()));
        p.getInventory().setLeggings(getColorArmor(Material.LEATHER_LEGGINGS,j.getTeam().getColor()));
        p.getInventory().setBoots(getColorArmor(Material.LEATHER_BOOTS,j.getTeam().getColor()));
    }
    public void giveItems(Jokalaria j){
        Player p = j.getPlayer();
        p.getInventory().clear();
        p.getInventory().addItem(new ItemStack(Material.BAKED_POTATO,10));
        if(j.getTeam()== urdina){
            p.getInventory().addItem(item(Material.STAINED_GLASS,11,32,ChatColor.BLUE + "Talde Urdina"));
        }else{
            p.getInventory().addItem(item(Material.STAINED_GLASS,14,32,ChatColor.RED + "Talde Gorria"));
        }
            
    }
    public ItemStack getColorArmor(Material m, Color c) {
        ItemStack i = new ItemStack(m, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
        meta.setColor(c);
        meta.setDisplayName("Arropa");
        i.setItemMeta(meta);
        return i;
        }
    public void Broadcast(String s){
      for(Jokalaria j : jokalariak){
          Player p = j.getPlayer();
          p.sendMessage(s);
      }
      for(Player p : ikusleak){
          if(p.isOnline()){
            p.sendMessage(s);
          }
      }
          }
    public  void sendTitleAll(Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle){
              for(Jokalaria j : jokalariak){
                  Player p = j.getPlayer();
                  sendTitle(p,fadeIn,stay,fadeOut,title,subtitle);
              }
          }
    public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
        connection.sendPacket(packetPlayOutTimes);
        if (subtitle != null) {
            subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
            connection.sendPacket(packetPlayOutSubTitle);
        }
        if (title != null) {
            title = title.replaceAll("%player%", player.getDisplayName());
            title = ChatColor.translateAlternateColorCodes('&', title);
            IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
            PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
            connection.sendPacket(packetPlayOutTitle);
        }
    }
    public boolean isInGame(Player p){
        for(Jokalaria j : jokalariak){
            if(j.getPlayer() == p){
                return true;
            }
        }
        return false;
    }
    public Jokalaria getJokalaria(Player p){
        for(Jokalaria j : jokalariak){
            if(j.getPlayer() == p){
                return j;
            }
        }
        return null;
    }
   public void saveSelection(String arena,String id, Location l1, Location l2){
        getConfig().set(arena + ".Win." + id + ".World", l1.getWorld().getName());
        getConfig().set(arena + ".Win." + id + ".Min.x", l1.getX());
        getConfig().set(arena + ".Win." + id + ".Min.y", l1.getY());
        getConfig().set(arena + ".Win." + id + ".Min.z", l1.getZ());

        getConfig().set(arena + ".Win." + id + ".Max.x", l2.getX());
        getConfig().set(arena + ".Win." + id + ".Max.y", l2.getY());
        getConfig().set(arena + ".Win." + id + ".Max.z", l2.getZ());
        saveConfig();
    }
    public void loadSelection(Team s, String arena){
        String w22 = getConfig().getString(arena + ".Spawn." + s.getID() +".World");
        Double x22 = getConfig().getDouble(arena + ".Spawn." + s.getID() +".X");
        Double y22 = getConfig().getDouble(arena + ".Spawn." + s.getID() +".Y");
        Double z22 = getConfig().getDouble(arena + ".Spawn." + s.getID() +".Z");
        s.setSpawn(new Location(Bukkit.getServer().getWorld(w22), x22, y22, z22));

        String w = getConfig().getString(arena + ".Win." + s.getID() + ".World");
        Double x = getConfig().getDouble(arena + ".Win." + s.getID() + ".Min.x");
        Double y= getConfig().getDouble(arena + ".Win." + s.getID() + ".Min.y");
        Double z = getConfig().getDouble(arena + ".Win." + s.getID() + ".Min.z");
        Double x2 = getConfig().getDouble(arena + ".Win." + s.getID() + ".Max.x");
        Double y2= getConfig().getDouble(arena + ".Win." + s.getID() + ".Max.y");
        Double z2 = getConfig().getDouble(arena + ".Win." + s.getID() + ".Max.z");
        Location l1 = new Location(Bukkit.getServer().getWorld(w), x, y, z);
        Location l2 = new Location(Bukkit.getServer().getWorld(w), x2, y2, z2);
        s.setWin(l1, l2);
    }
   public void SaveSpawn(String arena, Location l,String t){;
        getConfig().set(arena + ".Spawn."+ t +".World", l.getWorld().getName());
        getConfig().set(arena + ".Spawn."+ t +".X", l.getX());
        getConfig().set(arena + ".Spawn."+ t +".Y", l.getY());
        getConfig().set(arena + ".Spawn."+ t +".Z", l.getZ());
        saveConfig();
   }
   public void loadLobby(){
        /*String w22 = getConfig().getString("Lobby.World");
        Double x22 = getConfig().getDouble("Lobby.X");
        Double y22 = getConfig().getDouble("Lobby.Y");
        Double z22 = getConfig().getDouble("Lobby.Z");
        lobby =new Location(Bukkit.getServer().getWorld(w22), x22, y22, z22);*/
        mainLobby = new Location(getServer().getWorld("Jokoak"),1641,213,116);
        LobbyGor = new Location(getServer().getWorld("Jokoak"),1639,213,110);
        LobbyUr = new Location(getServer().getWorld("Jokoak"),1640,213,121);
   }
   public void loadSpawners(String arena){
        String w= getConfig().getString(arena + ".Spawn.exp.World");
        Double x = getConfig().getDouble(arena + ".Spawn.exp.X");
        Double y = getConfig().getDouble(arena + ".Spawn.exp.Y");
        Double z = getConfig().getDouble(arena + ".Spawn.exp.Z");
        exp =new Location(Bukkit.getServer().getWorld(w), x, y, z);
        Double x2 = getConfig().getDouble(arena + ".Spawn.iron.X");
        Double y2 = getConfig().getDouble(arena + ".Spawn.iron.Y");
        Double z2 = getConfig().getDouble(arena + ".Spawn.iron.Z");
        iron =new Location(Bukkit.getServer().getWorld(w), x2, y2, z2);
        Spawners();
   }
   public Team kontrakoTaldea(Player p){
       if(getJokalaria(p).getTeam().equals(urdina)){
           return gorria;
       }else{
           return urdina;
       }
   }
   
    private PlayerPoints playerPoints;
    private boolean hookPlayerPoints() {
        final Plugin plugin = getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = PlayerPoints.class.cast(plugin);
        return playerPoints != null; 
    }
    public PlayerPoints getPlayerPoints() {
        return playerPoints;
    }
    private boolean setupGEAPI(){
        GameErauntsiaMC api = (GameErauntsiaMC) getServer().getPluginManager().getPlugin("GameErauntsiaMC");
        GEAPI = api.getAPI();
        GEAPI.kargatuStat("ttirabazi");
        GEAPI.kargatuStat("ttjokatu");
        return GEAPI != null; 
    }
    private GEAPI GEAPI;
    
}
