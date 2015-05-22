package main;

/**
 * Created by stephen.broughton on 5/21/15.
 */
public class Vector {

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    private float x;

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    private float y;

    public Vector(float x, float y){
        setX(x);
        setY(y);
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Vector){
            return equals( (Vector) obj );
        }
        return false;
    }

    public boolean equals(Vector vec){
        if(vec.getX() == getX() && vec.getY() == getY()){
            return true;
        }
        return false;
    }

    public Vector add(Vector vec){
        setX( getX() + vec.getX() );
        setY( getY() + vec.getY() );
        return this;
    }

    public Vector subtract(Vector vec){
        setX( getX() - vec.getX() );
        setY( getY() - vec.getY() );
        return this;
    }

    public Vector multiplyBy(float scalar){
        setX( getX() * scalar );
        setY( getY() * scalar );
        return this;
    }

    public Vector divideBy(float scalar){
        setX( getX() / scalar );
        setY( getY() / scalar );
        return this;
    }


}
