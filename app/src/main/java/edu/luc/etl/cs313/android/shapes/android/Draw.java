package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO entirely your job (except onCircle)

    private final Canvas canvas;
    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas; // FIXME
        this.paint = paint; // FIXME
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        paint.setColor(c.getColor());
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        paint.setStyle(Style.FILL);
        f.getShape().accept(this);
        paint.setStyle(Style.STROKE);
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape shape : g.getShapes()) {
            shape.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.save();
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.restore();
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {

        float[] polygonPoints = new float[s.getPoints().size() * 2];

        for (int i = 0; i < s.getPoints().size(); i++) {
            polygonPoints[2 * i] = s.getPoints().get(i).getX();
            polygonPoints[2 * i + 1] = s.getPoints().get(i).getY();
        }

        // Draw the polygon by connecting the points
        canvas.drawLines(polygonPoints, paint);
        return null;
    }
}
