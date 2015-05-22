package main;

/**
 * Created by stephen.broughton on 5/21/15.
 */
public class BasicVector implements Vector {

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
    public boolean equals(Object obj){
        if(obj instanceof Vector){
            return equals( (Vector) obj );
        }
        return false;
    }

    @Override
    public boolean equals(Vector vec){
        if(vec.getX() == getX() && vec.getY() == getY()){
            return true;
        }
        return false;
    }

    @Override
    public Vector add(Vector vec){
        setX( getX() + vec.getX() );
        setY( getY() + vec.getY() );
        return this;
    }

    @Override
    public Vector subtract(Vector vec){
        setX( getX() - vec.getX() );
        setY( getY() - vec.getY() );
        return this;
    }

    @Override
    public Vector multiplyBy(float scalar){
        setX( getX() * scalar );
        setY( getY() * scalar );
        return this;
    }

    @Override
    public Vector divideBy(float scalar){
        setX( getX() / scalar );
        setY( getY() / scalar );
        return this;
    }


}

