package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;
import java.util.List;

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
        canvas.drawCircle(0,0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        int current_color = paint.getColor();
        paint.setColor(c.getColor());
        c.getShape().accept(this);
        paint.setColor(current_color);
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        Style current_style = paint.getStyle();
        paint.setStyle(Style.FILL_AND_STROKE);
        f.getShape().accept(this);
        paint.setStyle(current_style);
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
        canvas.translate(-l.getX(), -l.getY());
//        canvas.restore();
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        Style current_style = paint.getStyle();
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(current_style);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        final List<? extends Point> points = s.getPoints();
        final int n = points.size();

        if(n < 2) {
            return null;
        }

        final float[] pts = new float[n * 4];

        for (int i = 0; i < n - 1; i++) {
          Point currentPt = points.get(i);
          Point nextPt = points.get(i+1);

          pts[i * 4] = currentPt.getX();
          pts[i * 4 + 1] = currentPt.getY();
          pts[i * 4 + 2] = nextPt.getX();
          pts[i * 4 + 3] = nextPt.getY();
        }

        Point lastPt = points.get(n - 1);
        Point firstPt = points.get(0);

        pts[(n - 1) * 4] = lastPt.getX();
        pts[(n - 1) * 4 + 1] = lastPt.getY();
        pts[(n - 1) * 4 + 2] = firstPt.getX();
        pts[(n - 1) * 4 + 3] = firstPt.getY();

        // Draw the polygon by connecting the points
        canvas.drawLines(pts, paint);
        return null;
    }
}
