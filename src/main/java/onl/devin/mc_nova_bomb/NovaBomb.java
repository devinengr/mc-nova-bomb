package onl.devin.mc_nova_bomb;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Main plugin class. Implements an event that listens for
 * a left-click, then turns the clicked blocks into a
 * nova bomb.
 * @author devinengr
 */
public class NovaBomb extends JavaPlugin implements Listener {

    private long novaBombCount;

    /**
     * Perform setup for the plugin.
     */
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();
        novaBombCount = 0;
    }

    /**
     * Listen for left-clicks performed by the player.
     * @param e the interaction event
     */
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        FileConfiguration c = this.getConfig();
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block block = e.getPlayer().getTargetBlock(null, c.getInt("player-reach"));
            List<Block> blocks = blocksInRadius(block, c.getInt("block-radius"));
            Vector center = BetterVec.add(block.getLocation().toVector(),
                    new Vector(0, c.getInt("launch-height"), 0));
            List<NovaBlock> novas = convertToNovaBlocks(center, blocks);
            for (NovaBlock nb : novas) {
                nb.control(this);
            }
        }
    }

    /**
     * Convert the given list of blocks into FallingBlock entities, so
     * they can be put into orbit around the given center.
     * @param center the center of orbit (what the blocks orbit around)
     * @param blocks the blocks to convert
     * @return a list of NovaBlock objects created from the given set of blocks
     */
    private List<NovaBlock> convertToNovaBlocks(Vector center, List<Block> blocks) {
        List<NovaBlock> falling = new ArrayList<>();
        for (Block block : blocks) {
            BlockData blockData = block.getBlockData().clone();
            FallingBlock fb = block.getWorld().spawnFallingBlock(block.getLocation(), blockData);
            falling.add(new NovaBlock(novaBombCount, center.clone(), fb));
            block.setType(Material.AIR);
        }
        novaBombCount++;
        return falling;
    }

    /**
     * Get a list of all the blocks that are within the specified
     * radius of the specified center.
     * @param center the center of the sphere of blocks to get
     * @param radius the distance from the center to the edge of the sphere
     * @return list of block references
     */
    private List<Block> blocksInRadius(Block center, int radius) {
        List<Block> blocks = new ArrayList<>();
        int x = center.getX();
        int y = center.getY();
        int z = center.getZ();
        for (int xi = x - radius; xi <= x + radius; xi++) {
            for (int yi = y - radius; yi <= y + radius; yi++) {
                for (int zi = z - radius; zi <= z + radius; zi++) {
                    Vector vec = new Vector(xi, yi, zi);
                    if (vec.isInSphere(center.getLocation().toVector(), radius)) {
                        blocks.add(vec.toLocation(center.getWorld()).getBlock());
                    }
                }
            }
        }
        return blocks;
    }

}
