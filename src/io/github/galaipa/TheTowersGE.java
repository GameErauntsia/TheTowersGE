
package io.github.galaipa;

import static io.github.galaipa.GameListener.item;
import java.util.ArrayList;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.cyberiantiger.minecraft.instantreset.InstantReset;
import org.cyberiantiger.minecraft.instantreset.InstantResetWorld;


public class TheTowersGE extends JavaPlugin{
    ArrayList<Jokalaria> jokalariak = new ArrayList<>();
    Team urdina;
    Team gorria;
    Boolean admin;
    Boolean inGame;
    Score scoreUrdina;
    Score scoreGorria;
    String taldea = "urdina";
    Location lobby;
    @Override
    public void onEnable(){
        defaultValues();
        getConfig().options().copyDefaults(true);
        saveConfig();
        getServer().getPluginManager().registerEvents(new GameListener(this),this);
        defaultValues();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("thetowers")){
            if(args.length < 1){
                
            }else if (args[0].equalsIgnoreCase("join")){
                join(p);
                return true;
            }else if (args[0].equalsIgnoreCase("leave")){
                leave(p);
                return true;
            }
        }else if(cmd.getName().equalsIgnoreCase("thetowersadmin")){
            if(args.length < 1){
                
            }else if(args[0].equalsIgnoreCase("start")){
                start();
                return true;
            }else if (args[0].equalsIgnoreCase("spawn")){
                SaveSpawn(p.getLocation(),args[1]);
                p.sendMessage("Spawn set");
                return true;
            }else if (args[0].equalsIgnoreCase("win")){
                admin = true;
                p.sendMessage("Admin menu");
                GameListener.adminGui(p);
            }
        }
        return false;
    }
    public void defaultValues(){
        urdina = new Team("urdina");
        gorria = new Team("gorria");
        inGame = false;
        admin = false;
        jokalariak.clear();    
}
    public void join(Player p){
        loadLobby();
        if(getJokalaria(p) != null){
            p.sendMessage(ChatColor.GREEN +"[TheTowers] " +ChatColor.RED + "Dagoeneko bazaude zerrendan");
        }else{
        Jokalaria j = new Jokalaria(p);
        jokalariak.add(j);
        if(taldea.equalsIgnoreCase("urdina")){
            j.setTeam(urdina);
            p.sendMessage(ChatColor.GREEN +"[TheTowers] " +ChatColor.BLUE + "Talde urdinean sartu zara");
            taldea = "gorria";
        }
        else{
            j.setTeam(gorria);
            p.sendMessage(ChatColor.GREEN +"[TheTowers] " +ChatColor.RED + "Talde gorrian sartu zara");
            taldea = "urdina";
        }
        j.getPlayer().teleport(lobby);
        }
    }
    public void leave(Player p){
       Jokalaria j = getJokalaria(p);
       jokalariak.remove(j);
       j.getTeam().removePlayer(j);
       p.teleport(lobby);
       p.sendMessage(ChatColor.GREEN +"[TheTowers] " +ChatColor.RED + "Jokotik irten zara");
    }
    public void teleportSpawn(){
        for(Jokalaria j : jokalariak){
            Player p = j.getPlayer();
            p.teleport(j.getTeam().getSpawn());
            p.setHealth(p.getMaxHealth());
            setArmour(j); 
            giveItems(j);
        }
    }
             public void start(){
                    loadSelection(urdina);
                    loadSelection(gorria);
                    inGame = true;
                    Broadcast(ChatColor.GREEN +"[TheTowers]" + ChatColor.GREEN + "Jokoa orain hasiko da");
                    BukkitRunnable task = new BukkitRunnable() {
                    int countdown = 10;
                    public void run(){
                    for(Jokalaria j : jokalariak){
                        Player p = j.getPlayer();
                        p.setLevel(countdown);
                        p.sendMessage(ChatColor.GREEN + " " + countdown);
                        p.getWorld().playSound(p.getLocation(),Sound.NOTE_STICKS, 10, 1);
                        sendTitle(p,20,40,20,Integer.toString(countdown),"");
                    }
                    countdown--;
                    if (countdown < 0) {
                    this.cancel();
                    Broadcast(ChatColor.GREEN + "-----------------------------------------------");
                    Broadcast(ChatColor.BOLD.toString());
                    Broadcast(ChatColor.WHITE + "                         §lThe Towers");
                    Broadcast(ChatColor.GREEN + "                   " + "Zorte on denei!") ;
                    Broadcast(ChatColor.GREEN + "                " + "Partida 10 puntutara da");
                    Broadcast(ChatColor.BOLD.toString());
                    Broadcast(ChatColor.GREEN + "-----------------------------------------------");
                    teleportSpawn();
                    setScoreBoard();
                    }
                    }
                    };task.runTaskTimer(this, 0L, 20L);

            }
    public void setScoreBoard(){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("The Towers", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD +"The Towers");
        scoreGorria = objective.getScore(ChatColor.RED + "Gorriak:"); 
        scoreUrdina = objective.getScore(ChatColor.BLUE + "Urdinak:");
        scoreGorria.setScore(0);
        scoreUrdina.setScore(0);
        for(Jokalaria j : jokalariak){
                j.getPlayer().setScoreboard(board);
        }
        }
    public void amaiera(Team irabazlea, Team galtzailea){
        Broadcast(ChatColor.YELLOW + "------------------------------------------------");
        Broadcast(ChatColor.GREEN + "            TheTowers partida amaitu da         ");
        Broadcast(ChatColor.GREEN + "            Irabazlea: talde" + irabazlea.getID());
        Broadcast(ChatColor.YELLOW + "------------------------------------------------");
        for(Jokalaria j : jokalariak){
            j.getPlayer().teleport(lobby);
        }
        defaultValues();
        InstantReset irPlugin = (InstantReset) getServer().getPluginManager().getPlugin("InstantReset");
        if (irPlugin!= null && irPlugin.isEnabled()) {
            InstantResetWorld world = irPlugin.getInstantResetWorld(getConfig().getString("Win.urdina.World"));
            if (world != null) {
                irPlugin.resetWorld(world);
            }
        }
    }
        
    
    public void setArmour(Jokalaria j){
            Player p = j.getPlayer();
            p.getInventory().setArmorContents(null); //Inbentarioa garbitu
            p.getInventory().clear();
            p.getInventory().setHelmet(getColorArmor(Material.LEATHER_HELMET,j.getTeam().getColor())); // Koloretako armadura jarri
            p.getInventory().setChestplate(getColorArmor(Material.LEATHER_CHESTPLATE,j.getTeam().getColor()));
            p.getInventory().setLeggings(getColorArmor(Material.LEATHER_LEGGINGS,j.getTeam().getColor()));
            p.getInventory().setBoots(getColorArmor(Material.LEATHER_BOOTS,j.getTeam().getColor()));
    }
    public void giveItems(Jokalaria j){
        Player p = j.getPlayer();
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
            i.setItemMeta(meta);
            return i;
        }
    public void Broadcast(String s){
              for(Jokalaria j : jokalariak){
                  Player p = j.getPlayer();
                  p.sendMessage(s);
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
   public void saveSelection(String id, Location l1, Location l2){
                getConfig().set("Win." + id + ".World", l1.getWorld().getName());
                getConfig().set("Win." + id + ".Min.x", l1.getX());
                getConfig().set("Win." + id + ".Min.y", l1.getY());
                getConfig().set("Win." + id + ".Min.z", l1.getZ());
                
                getConfig().set("Win." + id + ".Max.x", l2.getX());
                getConfig().set("Win." + id + ".Max.y", l2.getY());
                getConfig().set("Win." + id + ".Max.z", l2.getZ());
                saveConfig();
    }
    public void loadSelection(Team s){
                String w22 = getConfig().getString("Spawn." + s.getID() +".World");
                Double x22 = getConfig().getDouble("Spawn." + s.getID() +".X");
                Double y22 = getConfig().getDouble("Spawn." + s.getID() +".Y");
                Double z22 = getConfig().getDouble("Spawn." + s.getID() +".Z");
                s.setSpawn(new Location(Bukkit.getServer().getWorld(w22), x22, y22, z22));
                
                String w = getConfig().getString("Win." + s.getID() + ".World");
                Double x = getConfig().getDouble("Win." + s.getID() + ".Min.x");
                Double y= getConfig().getDouble("Win." + s.getID() + ".Min.y");
                Double z = getConfig().getDouble("Win." + s.getID() + ".Min.z");
                Double x2 = getConfig().getDouble("Win." + s.getID() + ".Max.x");
                Double y2= getConfig().getDouble("Win." + s.getID() + ".Max.y");
                Double z2 = getConfig().getDouble("Win." + s.getID() + ".Max.z");
                Location l1 = new Location(Bukkit.getServer().getWorld(w), x, y, z);
                Location l2 = new Location(Bukkit.getServer().getWorld(w), x2, y2, z2);
                s.setWin(l1, l2);
    }
   public void SaveSpawn(Location l,String t){;
                getConfig().set("Spawn."+ t +".World", l.getWorld().getName());
                getConfig().set("Spawn."+ t +".X", l.getX());
                getConfig().set("Spawn."+ t +".Y", l.getY());
                getConfig().set("Spawn."+ t +".Z", l.getZ());
                saveConfig();
   }
   public void loadLobby(){
                String w22 = getConfig().getString("Spawn.lobby.World");
                Double x22 = getConfig().getDouble("Spawn.lobby.X");
                Double y22 = getConfig().getDouble("Spawn.lobby.Y");
                Double z22 = getConfig().getDouble("Spawn.lobby.Z");
                lobby =new Location(Bukkit.getServer().getWorld(w22), x22, y22, z22);
   }
}
