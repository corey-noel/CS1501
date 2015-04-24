/**
 * Created by Corey on 2/1/2015.
 */
public class Point {
    public final int x;
    public final int y;

    public Point(int xVal, int yVal) {
        x = xVal;
        y = yVal;
    }


    /**
     *
     * @param direction the direction of the neighboring point to be returned.
     * 0 as north, going around clockwise
     *
     * @return the neighboring point in that direction
     */
    public Point getNeighbor(int direction) {
        if (direction >= 8 || direction < 0)
            return null;

        int yOffset = 0;
        int xOffset = 0;

        if (direction == 7 || direction < 2)
            yOffset = -1;
        else if (direction > 2 && direction < 6)
            yOffset = 1;

        if (direction > 0 && direction < 4)
            xOffset = 1;
        else if (direction > 4)
            xOffset = -1;

        return new Point(x + xOffset, y + yOffset);
    }
}
