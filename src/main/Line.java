package main;

/**
 * Created by stephen.broughton on 5/22/15.
 */
public class Line extends Path {

    public Line(Vector2D nodeA, Vector2D nodeB){
        super(nodeA, nodeB);
    }

    public Line(float x0, float y0, float x1, float y1){
        this(new BasicVector(x0, y0), new BasicVector(x1, y1));
    }

    @Override
    public Vector2D getDirection(float t){
        return new BasicVector(nodeB.getX() - nodeA.getX(), nodeB.getY() - nodeA.getY());
    }


}
