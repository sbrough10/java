package main;

/**
 * Created by stephen.broughton on 5/21/15.
 */
public class Curve extends Path {

    final BoundingBox outerBounds = new BoundingBox();
    final LinkListNode.Chain<BoundingTree> innerBounds = new LinkListNode.Chain<>();
    protected Vector ctrlA;
    protected Vector ctrlB;
    // Transient variables for use in list node iterations
    private transient float minX = nodeA.getX();
    private transient float minY = nodeA.getY();
    private transient float maxX = nodeB.getX();
    private transient float maxY = nodeB.getY();
    private transient Float t0;

    public Curve(Vector vec0, Vector vec1, Vector vec2, Vector vec3) {
        super(vec0, vec3);
        ctrlA = vec1;
        ctrlB = vec2;
    }
    public Curve(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        this(new BasicVector(x0, y0), new BasicVector(x1, y1), new BasicVector(x2, y2), new BasicVector(x3, y3));
    }

    private void appendVertices(LinkListNode<Float> statPoints, float c0, float c1, float c2, float c3) {

        final float a = (-3 * c0 + 9 * c1 - 9 * c2 + 3 * c3);
        final float b = (6 * c0 - 12 * c1 + 6 * c2);
        final float c = (3 * c1 - 3 * c0);

        final double descrim = Math.sqrt(b * b - 4 * a * c);
        final float rootA = (float) (-b + descrim) / (2 * a);
        final float rootB = (float) (-b - descrim) / (2 * a);

        if (0 < rootA && rootA < 1) statPoints.link(rootA);
        if (0 < rootB && rootB < 1) statPoints.link(rootB);
    }

    private void generateBounds() {
        // Set stationary points list with both path nodes and the curve vertices
        LinkListNode<Float> statPoints = new LinkListNode<>(null);
        statPoints.link(0f).link(1f);
        appendVertices(statPoints, nodeA.getX(), ctrlA.getX(), ctrlB.getX(), nodeB.getX());
        appendVertices(statPoints, nodeA.getY(), ctrlA.getY(), ctrlB.getY(), nodeB.getY());

        statPoints.iterate(t1 -> {
            Vector vec = assignPointOnCurve(t1, new BasicVector(0,0));
            if (vec == null) return false;
            else {
                if (vec.getX() < minX) minX = vec.getX();
                else if (maxX < vec.getX()) maxX = vec.getX();

                if (vec.getY() < minY) minY = vec.getY();
                else if (maxY < vec.getY()) maxY = vec.getY();

                if (vec != nodeA) innerBounds.anteNode.link( new BoundingTree(t0, t1) ).link(innerBounds);
                t0 = t1;

                return true;
            }
        });

        outerBounds.setUnsafe(minX, minY, maxX, maxY);

    }

    public Vector assignPointOnCurve(float t, Vector vector) {
        final float s = 1 - t;
        final float t2 = t * t;
        final float s2 = s * s;
        final float t3 = t2 * t;
        final float s3 = s2 * s;
        vector.setX(nodeA.getX() * s3 + ctrlA.getX() * s2 * t + ctrlB.getX() * s * t2 + nodeB.getX() * t3);
        vector.setY(nodeA.getY() * s3 + ctrlA.getY() * s2 * t + ctrlB.getY() * s * t2 + nodeB.getY() * t3);
        return vector;
    }

    class BoundingTree extends BoundingBox {

        final float t0;
        final float t1;
        BoundingTree innerBoxA;
        BoundingTree innerBoxB;
        BoundingBox testAgainst;

        BoundingTree(float t0, float t1){
            this.t0 = t0;
            this.t1 = t1;
            Vector vec0 = assignPointOnCurve(t0, new BasicVector(0,0));
            Vector vec1 = assignPointOnCurve(t1, new BasicVector(0,0));
            super.set(vec0.getX(), vec0.getY(), vec1.getX(), vec1.getY());
        }

        void subDivide(){
            if(innerBoxA == null){
                float tHalf = (t0 + t1) / 2;
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

        public void divideAndConquer(LinkListNode<BoxComp> node, LinkListNode.Chain<Path.Intersection> points) {
            treeA.subDivide();
            treeB.subDivide();
            int count = 0;

            if( BoundingBox.Contact.existsBetween(treeA.innerBoxA, treeB.innerBoxA) ) {
                node.anteNode.link(new BoxComp(treeA.innerBoxA, treeB.innerBoxA)).link(node);
                count++;
            }
            if( BoundingBox.Contact.existsBetween(treeA.innerBoxA, treeB.innerBoxB) ) {
                node.anteNode.link(new BoxComp(treeA.innerBoxA, treeB.innerBoxB)).link(node);
                count++;
            }
            if( BoundingBox.Contact.existsBetween(treeA.innerBoxB, treeB.innerBoxA) ) {
                node.anteNode.link(new BoxComp(treeA.innerBoxB, treeB.innerBoxA));
                count++;
            }
            if( BoundingBox.Contact.existsBetween(treeA.innerBoxB, treeB.innerBoxB) ) {
                node.anteNode.link(new BoxComp(treeA.innerBoxB, treeB.innerBoxB));
                count++;
            }
            if(count == 4){
                points.link(new Path.Intersection( (treeA.t0 + treeA.t1) / 2, (treeB.t0 + treeB.t1) / 2 ));
                new LinkListNode.Chain<BoxComp>(node.anteNode.anteNode.anteNode.anteNode, node.anteNode).unlink();
            }

            node.unlink();
        }
    }

}
