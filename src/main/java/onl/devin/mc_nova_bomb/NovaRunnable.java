package onl.devin.mc_nova_bomb;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This Runnable class adjusts the velocity and direction of
 * each NovaBlock every tick. Each NovaBlock gets its own
 * Runnable.
 * @author devinengr
 */
public class NovaRunnable implements Runnable {

    private static Map<Long, Vector> dirStarts = new HashMap<>();

    private JavaPlugin plugin;
    private NovaBlock novaBlock;
    private int taskID;
    private int i = 0;

    /**
     * Create a Runnable.
     * @param plugin the plugin class to get config data from
     * @param novaBlock the block to put into motion
     */
    public NovaRunnable(JavaPlugin plugin, NovaBlock novaBlock) {
        this.plugin = plugin;
        this.novaBlock = novaBlock;
        Random rng = new Random();
        FileConfiguration c = plugin.getConfig();
        // only put the direction in the map once for each unique sphere
        if (!dirStarts.containsKey(novaBlock.getSphereID())) {
            dirStarts.put(novaBlock.getSphereID(), new Vector(
                    (rng.nextFloat() - 0.5) * c.getInt("launch-to-side-multiplier"),
                    0.5,
                    (rng.nextFloat() - 0.5) * c.getInt("launch-to-side-multiplier")));
        }
    }

    /**
     * Assign a task ID to this Runnable. Must be the ID
     * returned by the scheduleSyncRepeatingTask() function.
     * It is used to cancel the Runnable when it is finished.
     * @param taskID the ID for the task
     */
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    /**
     * Cancel the task using the ID specified in
     * setTaskID().
     */
    private void cancel() {
        Bukkit.getScheduler().cancelTask(taskID);
        // since cancel() is called for every block in a nova bomb,
        // and dirStarts contains one direction for each nova bomb,
        // this ID needs to be removed from it only once.
        if (dirStarts.containsKey(novaBlock.getSphereID())) {
            dirStarts.remove(novaBlock.getSphereID());
        }
    }

    /**
     * Control the motion of the NovaBlock. Put it into
     * orbit, maintain orbit, and then launch after a period
     * of ticks defined by the user config.
     */
    @Override
    public void run() {
        FileConfiguration c = plugin.getConfig();
        if (i < c.getInt("ticks-to-launch")) {
            // set initial velocity when blocks launch upward
            novaBlock.getBlock().setVelocity(dirStarts.get(novaBlock.getSphereID()));
        } else if (i < c.getInt("ticks-to-orbit")) {
            // block is current orbiting the center, so calculate direction
            novaBlock.getBlock().setVelocity(
                    novaBlock.getGravity().calculateVelocity(
                            novaBlock.getCenter().clone(), novaBlock.getBlock()));
        } else {
            // calculate launch direction
            Vector launchDir = BetterVec.subtract(
                    novaBlock.getBlock().getLocation().toVector(),
                    novaBlock.getCenter());
            // launch block
            novaBlock.getBlock().setVelocity(launchDir);
            cancel();
        }
        i++;
    }

}
