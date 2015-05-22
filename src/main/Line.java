package main;

/**
 * Created by stephen.broughton on 5/22/15.
 */
public class Line extends Path {

    public Line(Vector vec0, Vector vec1){
        super(vec0, vec1);
    }

    public Line(float x0, float y0, float x1, float y1){
        this(new BasicVector(x0, y0), new BasicVector(x1, y1));
    }
}
