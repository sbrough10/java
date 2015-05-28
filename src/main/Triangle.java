package main;

/**
 * Created by stephen.broughton on 5/28/15.
 */
public class Triangle extends Polygon {

    public Triangle(Vector nodeA, Vector nodeB, Vector nodeC) {
        super(nodeA);
        addLine(nodeB);
        addLine(nodeC);
        addLine(nodeA);
    }

    @Override
    public float getArea() {
        Vector vecA = edges.anteNode.value.getDirection();
        Vector vecB = edges.postNode.value.getDirection();
        return ( vecA.getX() * vecB.getY() - vecA.getY() * vecB.getX() ) / 2;
    }
}
