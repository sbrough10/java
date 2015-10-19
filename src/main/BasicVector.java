package main;

/**
 * Created by stephen.broughton on 5/21/15.
 */
public class BasicVector implements Vector2D {

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    private float x;

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    private float y;

    public BasicVector(float x, float y){
        setX(x);
        setY(y);
    }

    @Override
    public String toString(){
        return "{ x: " + getX() + ", y: " + getY() + " }";
    }
}

