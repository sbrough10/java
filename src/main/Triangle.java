package main;

/**
 * Created by stephen.broughton on 5/28/15.
 */
public class Triangle extends Polygon {

    public Triangle(Vector2D nodeA, Vector2D nodeB, Vector2D nodeC) {
        super(nodeA);
        addLine(nodeB);
        addLine(nodeC);
        addLine(nodeA);
    }

    @Override
    public float getArea() {
        Vector2D vecA = edges.anteNode.value.getDirection(0);
        Vector2D vecB = edges.postNode.value.getDirection(0);
        return Math.abs( vecA.getX() * vecB.getY() - vecA.getY() * vecB.getX() ) / 2;
    }
}
