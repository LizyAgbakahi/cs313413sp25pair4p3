package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    // TODO entirely your job (except onCircle)

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (Shape s : g.getShapes()) {
            Location boundingBox = s.accept(this);
            int x = boundingBox.getX();
            int y = boundingBox.getY();
            int width = ((Rectangle) boundingBox.getShape()).getWidth();
            int height = ((Rectangle) boundingBox.getShape()).getHeight();

            if (x < minX) minX = x;
            if (y < minY) minY = y;
            if (x + width > maxX) maxX = x + width;
            if (y + height > maxY) maxY = y + height;
        }

        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));

    }

    @Override
    public Location onLocation(final Location l) {

        Location boundingBox = l.getShape().accept(this);
        return new Location(l.getX() + boundingBox.getX(), l.getY() + boundingBox.getY(), boundingBox.getShape());
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0, 0, r);
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (Point point : s.getPoints()) {
            int x = point.getX();
            int y = point.getY();
            if (x < minX) minX = x;
            if (y < minY) minY = y;
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
        }
        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
}
}
