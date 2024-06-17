package onl.devin.mc_nova_bomb;

import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

/**
 * Calculates velocity and direction for falling block
 * entities.
 * @author devinengr
 */
public class Gravity {

    private double accel;

    /**
     * Create a new Gravity instance.
     * @param accel the rate to speed up the falling block
     */
    public Gravity(double accel) {
        this.accel = accel;
    }

    /**
     * Helper function for calculateVelocity.
     * @param dir direction to apply acceleration speed to
     * @return normalized vector, magnified based on accel's value
     */
    private Vector getAccelVector(Vector dir) {
        if (dir.length() == 0) {
            return new Vector(0, 1, 0);
        }
        Vector norm = BetterVec.normalize(dir);
        Vector mul = BetterVec.multiply(norm, new Vector(accel, accel, accel));
        return mul;
    }

    /**
     * Get a velocity and direction to apply to the falling block. This
     * vector will cause the falling block to orbit around the specified
     * center block throughout a period of ticks.
     * @param center the center that the falling block should orbit around
     * @param affected the falling block to orbit the center
     * @return a vector to apply to the falling block's velocity
     */
    public Vector calculateVelocity(Vector center, FallingBlock affected) {
        Vector vecAffected = affected.getLocation().toVector();
        Vector dir = BetterVec.subtract(center, vecAffected);
        Vector accelVec = getAccelVector(dir);
        Vector res = BetterVec.add(affected.getVelocity(), accelVec);
        return res;
    }

}
