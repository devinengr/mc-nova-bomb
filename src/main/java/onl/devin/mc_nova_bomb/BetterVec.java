package onl.devin.mc_nova_bomb;

import org.bukkit.util.Vector;

/**
 * The vectors in the Bukkit library produce side effects.
 * When you call vec1.add(vec2), vec1 is modified. To avoid
 * this, you can call vec1.clone(), but it can get messy.
 * @author devinengr
 */
public class BetterVec {

    /**
     * Add two vectors.
     * @param v1 vector
     * @param v2 vector
     * @return result of v1 + v2
     */
    public static Vector add(Vector v1, Vector v2) {
        return new Vector(v1.getX() + v2.getX(),
                          v1.getY() + v2.getY(),
                          v1.getZ() + v2.getZ());
    }

    /**
     * Multiply two vectors.
     * @param v1 vector
     * @param v2 vector
     * @return result of v1 * v2
     */
    public static Vector multiply(Vector v1, Vector v2) {
        return new Vector(v1.getX() * v2.getX(),
                          v1.getY() * v2.getY(),
                          v1.getZ() * v2.getZ());
    }

    /**
     * Subtract v2 from v1.
     * @param v1 vector
     * @param v2 vector
     * @return result of v1 - v2
     */
    public static Vector subtract(Vector v1, Vector v2) {
        return new Vector(v1.getX() - v2.getX(),
                          v1.getY() - v2.getY(),
                          v1.getZ() - v2.getZ());
    }

    /**
     * Normalize the length of the vector so each value
     * is not higher than 1 or lower than -1.
     * @param v vector
     * @return normalized vector
     */
    public static Vector normalize(Vector v) {
        double l = v.length();
        double normX = v.getX() * (1 / l);
        double normY = v.getY() * (1 / l);
        double normZ = v.getZ() * (1 / l);
        return new Vector(normX, normY, normZ);
    }

}
