package main;

import com.sun.istack.internal.NotNull;

/**
 * Created by stephen.broughton on 5/21/15.
 */
public class Curve extends Path {

    final BoundingBox outerBounds = new BoundingBox();
    final LinkListNode.Chain<BoundingTree> innerBounds = new LinkListNode.Chain<>();
    protected Vector2D ctrlA;
    protected Vector2D ctrlB;

    public Curve(Vector2D vec0, Vector2D vec1, Vector2D vec2, Vector2D vec3) {
        super(vec0, vec3);
        ctrlA = vec1;
        ctrlB = vec2;
    }
    public Curve(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        this(new BasicVector(x0, y0), new BasicVector(x1, y1), new BasicVector(x2, y2), new BasicVector(x3, y3));
    }

    private void insert(final float value, final float[] array, final int length){
        int i = 0;
        while(i < length){
            if(value < array[i]){
                for(int j = length + 1; i < j; j--){
                    array[j] = array[j-1];
                }
                break;
            }
            i++;
        }
        array[i] = value;
    }

    private int appendVertices(final float p, final float q, final float r, final float s, final float[] statPoints, int length) {
        /**
         * p = (1 - t)^3 * p + 3 * (1 - t)^2 * t * q + 3 * (1 - t) * t^2 * r + t^3 * s
         * d(p)/dt = 3 * (q - p) * (1-t)^2 + 6 * (r - q) * (1-t) * t + 3 * (s - r) * t^2
         *         = 3 * (q - p) + 6 * (p - 2 * q + r) * t + 3 * (-p + 3 * q - 3 * r + s) * t^2
         *
         * If d(p)/dt = 0 ->
         *      t = [ -b ± √(b * b - 4 * a * c) ] / [ 2 * a ]
         *
         */

        final float a = 3 * (-p + 3 * q - 3 * r + s);
        final float b = 6 * (p - 2 * q + r);
        final float c = 3 * (q - p);

        if(a != 0) {
            final double discrim = Math.sqrt(b * b - 4 * a * c);
            final float rootA = (float) (-b + discrim) / (2 * a);
            final float rootB = (float) (-b - discrim) / (2 * a);

            if (0 < rootA && rootA < 1){
                insert(rootA, statPoints, length);
                length++;
            }
            if (0 < rootB && rootB < 1){
                insert(rootB, statPoints, length);
                length++;
            }
        } else {
            final float root = -c / b;

            if (0 < root && root < 1){
                insert(root, statPoints, length);
                length++;
            }
        }
        return length;
    }

    void generateBounds() {
        // Set stationary points list with both path nodes and the curve vertices
        int length = 0;
        float[] statPoints = new float[5];
        length = appendVertices(nodeA.getX(), ctrlA.getX(), ctrlB.getX(), nodeB.getX(), statPoints, length);
        length = appendVertices(nodeA.getY(), ctrlA.getY(), ctrlB.getY(), nodeB.getY(), statPoints, length);
        statPoints[length] = 1;
        length++;

        float minX = nodeA.getX();
        float minY = nodeA.getY();
        float maxX = nodeB.getX();
        float maxY = nodeB.getY();
        float t0 = 0;

        for(int i = 0; i < length; i++){
            float t1 = statPoints[i];
            Vector2D vec = getPointOnCurve(t1);
            if (vec.getX() < minX) minX = vec.getX();
            else if (maxX < vec.getX()) maxX = vec.getX();

            if (vec.getY() < minY) minY = vec.getY();
            else if (maxY < vec.getY()) maxY = vec.getY();

            if (vec != nodeA) innerBounds.link( new BoundingTree(t0, t1) );
            t0 = t1;
        }

        outerBounds.setUnsafe(minX, minY, maxX, maxY);

    }

    public Vector2D assignPointOnCurve(float t, Vector2D vector) {
        final float s = 1 - t;
        final float t2 = t * t;
        final float s2 = s * s;
        final float t3 = t2 * t;
        final float s3 = s2 * s;
        vector.setX(nodeA.getX() * s3 + ctrlA.getX() * s2 * t + ctrlB.getX() * s * t2 + nodeB.getX() * t3);
        vector.setY(nodeA.getY() * s3 + ctrlA.getY() * s2 * t + ctrlB.getY() * s * t2 + nodeB.getY() * t3);
        return vector;
    }

    public Vector2D getPointOnCurve(float t){
        final float s = 1 - t;
        final float t2 = t * t;
        final float s2 = s * s;
        final float t3 = t2 * t;
        final float s3 = s2 * s;
        return new BasicVector(
                nodeA.getX() * s3 + 3 * ctrlA.getX() * s2 * t + 3 * ctrlB.getX() * s * t2 + nodeB.getX() * t3,
                nodeA.getY() * s3 + 3 * ctrlA.getY() * s2 * t + 3 * ctrlB.getY() * s * t2 + nodeB.getY() * t3
        );
    }

    @Override
    public Vector2D getDirection(float t) {
        return null;
    }

    @Override
    public void transform(AffineMatrix am) {
    }

    class BoundingTree extends BoundingBox {

        float t0;
        float t1;
        float tHalf;
        BoundingTree innerBoxA;
        BoundingTree innerBoxB;

        BoundingTree(float t0, float t1){
            this.t0 = t0;
            this.t1 = t1;
            Vector2D vec0 = getPointOnCurve(t0);
            Vector2D vec1 = getPointOnCurve(t1);
            super.set(vec0.getX(), vec0.getY(), vec1.getX(), vec1.getY());
        }

        void subDivide(){
            if(innerBoxA == null){
                tHalf = (t0 + t1) / 2;
                innerBoxA = new BoundingTree(t0, tHalf);
                innerBoxB = new BoundingTree(tHalf, t1);
            }
        }

    }

    static class BoxComp {

        final BoundingTree treeA;
        final BoundingTree treeB;

        BoxComp(BoundingTree treeA, BoundingTree treeB){
            this.treeA = treeA;
            this.treeB = treeB;
        }

        void divideAndConquer(
                @NotNull final LinkListNode.Chain<BoxComp> nextTests,
                @NotNull final LinkListNode.Chain<Path.Intersection> intersections,
                final float precision
        ) {
            treeA.subDivide();
            treeB.subDivide();
            final LinkListNode.Chain<BoxComp> subComps = new LinkListNode.Chain<>();
            final LinkListNode.Chain<Path.Intersection> segments = new LinkListNode.Chain<>();

            int count = 0;

            if(dacHelper(treeA.innerBoxA, treeB.innerBoxA, precision, segments, subComps)) count++;
            if(dacHelper(treeA.innerBoxA, treeB.innerBoxB, precision, segments, subComps)) count++;
            if(dacHelper(treeA.innerBoxB, treeB.innerBoxA, precision, segments, subComps)) count++;
            if(dacHelper(treeA.innerBoxB, treeB.innerBoxB, precision, segments, subComps)) count++;
            if(count == 4){
                intersections.link(new Path.Intersection(treeA.tHalf, treeA.tHalf, treeB.tHalf, treeB.tHalf));
            } else {
                intersections.link(segments);
                nextTests.link(subComps);

            }
        }

        boolean dacHelper(
                @NotNull final BoundingTree boxA, @NotNull final BoundingTree boxB,
                final float precision,
                @NotNull final LinkListNode.Chain<Path.Intersection> segments,
                @NotNull final LinkListNode.Chain<BoxComp> subComps
        ) {
            if(BoundingBox.Contact.existsBetween(boxA, boxB)){
                if(boxA.meetsPrecision(precision) && boxB.meetsPrecision(precision)){
                    segments.link(new Path.Intersection(boxA.t0, boxA.t1, boxB.t0, boxB.t1));
                } else {
                    subComps.link(new BoxComp(boxA, boxB));
                }
                return true;
            }
            return false;
        }

        @Override
        public String toString(){
            return "Compare( " + treeA + " & " + treeB + " )";
        }
    }

}
