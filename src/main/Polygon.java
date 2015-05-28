package main;

/**
 * Created by stephen.broughton on 5/28/15.
 */
public class Polygon extends Region {

    LinkListNode.Chain<Line> edges = new LinkListNode.Chain<>();

    public Polygon(Vector nodeA) {
        super(nodeA);
    }

    @Override
    public Region addLine(Vector nodeB) {
        edges.link(new Line(lastNode, nodeB));
        lastNode = nodeB;
        return this;
    }

    @Override
    public Region addCurve(Vector ctrlA, Vector ctrlB, Vector nodeB) {
        return this;
    }

    @Override
    public float getArea() {
        return super.getArea();
    }
}
