package main;

public interface Vector{

    public float getX();

    public void setX(float x);

    public float getY();

    public void setY(float y);

    public boolean equals(Vector vec);

    public Vector add(Vector vec);

    public Vector subtract(Vector vec);

    public Vector multiplyBy(float scalar);

    public Vector divideBy(float scalar);
}