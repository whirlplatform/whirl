package org.whirlplatform.server.utils;

public class XPoint {

    /**
     * The x coordinate of the point
     */
    public int x;

    /**
     * The y coordinate of the point
     */
    public int y;

    /**
     * Constructs a new point with the given x and y coordinates.
     *
     * @param x the x coordinate of the new point
     * @param y the y coordinate of the new point
     */
    public XPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return ("x: " + x + ", y: " + y);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        XPoint other = (XPoint) obj;
        if (x != other.x)
            return false;
        return y == other.y;
    }

}
