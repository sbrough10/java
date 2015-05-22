package main;

/**
 * Created by stephen.broughton on 5/21/15.
 */
public class Curve extends Path {

    protected Vector ctrlA;
    protected Vector ctrlB;

    public Curve(Vector vec0, Vector vec1, Vector vec2, Vector vec3){
        super(vec0, vec3);
        ctrlA = vec1;
        ctrlB = vec2;
    }

    public Curve(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3){
        this(new BasicVector(x0, y0), new BasicVector(x1, y1), new BasicVector(x2, y2), new BasicVector(x3, y3));
    }

    private LoopNode statPoints;

}
