package main;

/**
 * Created by stephen.broughton on 5/28/15.
 */
public class Polygon extends Region {

    public Polygon(Vector2D nodeA) {
        super(nodeA);
    }

    @Override
    public Region addLine(Vector2D nodeB) {
        edges.link(new Line(lastNode, nodeB));
        lastNode = nodeB;
        return this;
    }

    @Override
    public Region addCurve(Vector2D ctrlA, Vector2D ctrlB, Vector2D nodeB) {
        return this;
    }

    @Override
    public float getArea() {
        return super.getArea();
    }
}
