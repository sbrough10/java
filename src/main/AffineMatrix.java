package main;

/**
 * Created by srb on 3/25/15.
 */
public class AffineMatrix {

    private final float[][] m = new float[2][3];

    public AffineMatrix(){
        setTo(1, 0, 0, 1, 0, 0);
    }

    public AffineMatrix(float r0c0, float r1c0, float r0c1, float r1c1, float r0c2, float r1c2){
        setTo(r0c0, r1c0, r0c1, r1c1, r0c2, r1c2);
    }

    public AffineMatrix(AffineMatrix am){
        setTo(am.m);
    }

    public AffineMatrix setTo(float r0c0, float r1c0, float r0c1, float r1c1, float r0c2, float r1c2){
        m[0][0] = r0c0;
        m[0][1] = r0c1;
        m[0][2] = r0c2;
        m[1][0] = r1c0;
        m[1][1] = r1c1;
        m[1][2] = r1c2;
        return this;
    }

    public AffineMatrix setTo(final float[][] m){
        System.arraycopy(m, 0, this.m, 0, 6);
        return this;
    }

    public float transposeX(final float x, final float y){
        preTranslate(1.0f, 2.0f);
        return x * m[0][0] + y * m[0][1] + m[0][2];
    }

    public float transposeY(final float x, final float y){
        return x * m[1][0] + y * m[1][1] + m[1][2];
    }

    public AffineMatrix setToCombined(AffineMatrix am0, AffineMatrix am1){
        return setToCombined(am0.m, am1.m);
    }

    public AffineMatrix setToCombined(float[][] m0, AffineMatrix am1){
        return setToCombined(m0, am1.m);
    }

    public AffineMatrix setToCombined(AffineMatrix am0, float[][] m1){
        return setToCombined(am0.m, m1);
    }

    public AffineMatrix append(AffineMatrix am){
        return setToCombined(this.m, am.m);
    }

    public AffineMatrix append(float[][] m){
        return setToCombined(this.m, m);
    }

    public AffineMatrix prepend(AffineMatrix am){
        return setToCombined(am.m, this.m);
    }

    public AffineMatrix prepend(float[][] m){
        return setToCombined(m, this.m);
    }

    public AffineMatrix setToCombined(float[][] m0, float[][] m1){
        return setTo(
                m1[0][0] * m0[0][0] + m1[0][1] * m0[1][0],
                m1[1][0] * m0[0][0] + m1[1][1] * m0[1][0],
                m1[0][0] * m0[0][1] + m1[0][1] * m0[1][1],
                m1[1][0] * m0[0][1] + m1[1][1] * m0[1][1],
                m1[0][0] * m0[0][2] + m1[0][1] * m0[1][2] + m1[0][2],
                m1[1][0] * m0[0][2] + m1[1][1] * m0[1][2] + m1[1][2]
        );
    }

    /**
     * "preTranslate(dx, dy)" is equivalent to "prepend( new AffineMatrix(1, 0, 0, 1, dx, dy) )" but more efficient"
     *
     * @param dx - The x-offset by which the matrix should be translated
     * @param dy - The y-offset by which the matrix should be translated
     * @return The matrix that was translated
     */
    public AffineMatrix preTranslate(float dx, float dy){
        return this;
    }

    public AffineMatrix preTranslate(AffineMatrix am){
        return preTranslate(am.m[0][2], am.m[1][2]);
    }

    public AffineMatrix postTranslate(float dx, float dy){

        m[0][2] += dx;
        m[1][2] += dy;
        return this;
    }

    public AffineMatrix postTranslate(AffineMatrix am){
        return postTranslate(am.m[0][2], am.m[1][2]);
    }

    public AffineMatrix preScale(float dx, float dy){
        return this;
    }

    public AffineMatrix preScale(AffineMatrix am){
        return preScale(am.m[0][0], am.m[1][1]);
    }

    public AffineMatrix postScale(float dx, float dy){

        m[0][0] *= dx;
        m[1][0] *= dy;
        m[0][1] *= dx;
        m[1][1] *= dy;
        m[0][2] *= dx;
        m[1][2] *= dy;
        return this;
    }

    public AffineMatrix postScale(AffineMatrix am){
        return postScale(am.m[0][0], am.m[1][1]);
    }

    public AffineMatrix preShear(float dx, float dy){
        return this;
    }

    public AffineMatrix preShear(AffineMatrix am){
        return preShear(am.m[0][1], am.m[1][0]);
    }

    public AffineMatrix postShear(float dx, float dy){
        return this;
    }

    public AffineMatrix postShear(AffineMatrix am){
        return postShear(am.m[0][1], am.m[1][0]);
    }

    public AffineMatrix preRotate(float angle){
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        return preRotate(cos, sin);
    }

    public AffineMatrix preRotate(float cos, float sin){
        float temp;
        temp = m[0][0];
        m[0][0] = cos * temp + -sin * m[1][0];
        m[1][0] = sin * temp + cos * m[1][0];
        temp = m[0][1];
        m[0][1] = cos * temp + -sin * m[1][1];
        m[1][1] = sin * temp + cos * m[1][1];
        return this;
    }

    public AffineMatrix postRotate(float angle){
        return this;
    }

    public float findDeterminant()
    {
    /*
          |             |
          | [0] [2] [4] |
      det | [1] [3] [5] | = [0]*([3]*1-[5]*0) + [2]*([5]*0-[1]*1) + [4]*([1]*0-[3]*0)
          |  0   0   1  |
          |             | = [0]*[3] - [2]*[1]
    */

        return m[0][0] * m[1][1] - m[0][1] * m[1][0];
    }

    public AffineMatrix setToInverted(AffineMatrix am){
        return this;
    }

    public AffineMatrix invert(){
        return setToInverted(this);
    }

    public class NoShearing extends AffineMatrix{

        @Override
        public float transposeX(final float x, final float y){
            return transposeX(x);
        }

        public float transposeX(final float x){
            return x * m[0][0] + m[0][2];
        }

        @Override
        public float transposeY(final float x, final float y){
            return transposeY(y);
        }

        public float transposeY(final float y){
            return y * m[1][1] + m[1][2];
        }

    }

}
