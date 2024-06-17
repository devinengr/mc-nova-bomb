package onl.devin.mc_nova_bomb;

import org.bukkit.Bukkit;
import org.bukkit.entity.FallingBlock;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 * A falling block entity with information to
 * calculate its velocity and direction.
 * @author devinengr
 */
public class NovaBlock {

    private long sphereID;
    private Vector center;
    private FallingBlock block;
    private Gravity gravity;

    /**
     * Create a block. The block will orbit around
     * the given center and will appear as the given
     * falling block entity.
     * @param sphereID identifier for the whole sphere to pair the block with
     * @param center center of orbit
     * @param block falling block entity to orbit the center
     */
    public NovaBlock(long sphereID, Vector center, FallingBlock block) {
        this.sphereID = sphereID;
        this.center = center.clone();
        this.block = block;
        this.gravity = new Gravity(0.1f);
    }

    /**
     * Run a scheduled task that updates the velocity and
     * direction of the block per tick.
     * @param plugin main plugin class
     */
    public void control(JavaPlugin plugin) {
        NovaRunnable runnable = new NovaRunnable(plugin, this);
        int taskID = Bukkit.getScheduler()
                .scheduleSyncRepeatingTask(
                        plugin,
                        runnable,
                        1, 1);
        // given the runnable the ID of the task, so that
        // it can be canceled later
        runnable.setTaskID(taskID);
    }

    /**
     * Returns the center of orbit.
     * @return center of orbit
     */
    public Vector getCenter() {
        return center;
    }

    /**
     * Returns the falling block entity associated
     * with this block.
     * @return falling block entity
     */
    public FallingBlock getBlock() {
        return block;
    }

    /**
     * Returns the Gravity instance associated
     * with this block.
     * @return gravity instance
     */
    public Gravity getGravity() {
        return gravity;
    }

    /**
     * Get the unique sphere ID associated with this
     * block. Multiple blocks containing the same
     * sphere ID means they are part of the same
     * nova bomb.
     * @return the sphere ID of this block
     */
    public long getSphereID() {
        return sphereID;
    }

}
