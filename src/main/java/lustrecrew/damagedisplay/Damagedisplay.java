package lustrecrew.damagedisplay;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class Damagedisplay extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, (Plugin)this);
        for (World worlds : getServer().getWorlds()) {
            for (LivingEntity ent : worlds.getLivingEntities()) {
                if (ent instanceof ArmorStand &&
                        ent.hasMetadata("dmgstand"))
                    ent.remove();
            }
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
    public void CreateDamageStand(double i, Entity entity) {
        Location location = entity.getLocation();
        location.add(new Vector(0.0D, 0.0D, 0.0D));
        final ArmorStand armorStand = (ArmorStand)entity.getWorld().spawnEntity(new Location(entity.getWorld(), location.getX() + Math.random(), location.getY() - Math.random(), location.getZ() + Math.random()), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setMetadata("dmgstand", (MetadataValue)new FixedMetadataValue((Plugin)this, "dmgstand"));
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setCustomName(ChatColor.GOLD + "✧" + ChatColor.RED + ChatColor.BOLD + Math.floor(i) + ChatColor.RESET + ChatColor.GOLD + "✧");
        armorStand.setCustomNameVisible(true);
        (new BukkitRunnable() {
            public void run() {
                armorStand.remove();
            }
        }).runTaskLater((Plugin)this, (20L));
    }
    @EventHandler
    public void ondmgindicator(EntityDamageEvent e) {
        if (e.getEntity() instanceof ArmorStand) {
            if (e.getEntity().hasMetadata("dmgstand"))
                e.setCancelled(true);
        } else {
            CreateDamageStand(e.getDamage(), e.getEntity());
        }
    }
}
