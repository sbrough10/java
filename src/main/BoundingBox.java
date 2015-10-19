package main;

/**
 * Created by srb on 5/23/15.
 */
public class BoundingBox {

    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    public BoundingBox(){

    }

    public BoundingBox(float x0, float y0, float x1, float y1){
        set(x0, y0, x1, y1);
    }

    public void set(float x0, float y0, float x1, float y1){
        if(x0 < x1) {
            minX = x0;
            maxX = x1;
        } else {
            minX = x1;
            maxX = x0;
        }
        if(y0 < y1) {
            minY = y0;
            maxY = y1;
        } else {
            minY = y1;
            maxY = y0;
        }
    }

    void setUnsafe(float minX, float minY, float maxX, float maxY){
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public boolean surrounds(BoundingBox bbox){
        return this.minX <= bbox.minX && bbox.maxX <= this.maxX && this.minY <= bbox.minY && bbox.maxY <= this.maxY;
    }

    public float getArea(){
        return (this.maxX - this.minX) * (this.maxY - this.minY);
    }

    public boolean meetsPrecision(final float precision){
        return this.maxX - this.minX <= precision && this.maxY - this.minY <= precision;
    }

    @Override
    public String toString(){
        return "{ x: [" + minX + "," + maxX + "], y: [" + minY + "," + maxY + "] }";
    }

    public static class Contact {

        public final BoundingBox result;

        public static boolean existsBetween(BoundingBox b0, BoundingBox b1){
            return b0.maxX >= b1.minX && b1.maxX >= b0.minX && b0.maxY >= b1.minY && b1.maxY >= b0.minY;
        }


        Contact(BoundingBox b0, BoundingBox b1) {
           if( !existsBetween(b0, b1) ){
               result = null;
           } else {
               float minX = b0.minX < b1.minX ? b1.minX : b0.minX;
               float minY = b0.minY < b1.minY ? b1.minY : b0.minY;
               float maxX = b0.maxX < b1.maxX ? b0.maxX : b1.maxX;
               float maxY = b0.maxY < b1.maxY ? b0.maxY : b1.maxY;

               result = new BoundingBox();
               result.setUnsafe(minX, minY, maxX, maxY);
           }
        }
    }


}
