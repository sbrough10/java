package main;

/**
 * Created by stephen.broughton on 5/22/15.
 */
public abstract class Path {

    protected Vector nodeA;
    protected Vector nodeB;

    protected Path(Vector vecA, Vector vecB){
        nodeA = vecA;
        nodeB = vecB;
    }

    public static class Intersection {
        final float tA;
        final float tB;

        public Intersection(float tA, float tB){
            this.tA = tA;
            this.tB = tB;
        }
    }

    public static class Contact {
        public Contact(Line pA, Line pB) {
            /**
             *  x# = (Bx - Ax)t + Ax
             *  y# = (By - Ay)t + Ay
             *
             *  (B0 - A0)t + A0 = (B1 - A1)t + A1
             *  (B0 - A0 - B1 + A1)t = A1 - A0
             *  t = (A1 - A0) / (B0 - A0 - B1 + A1)
             */

            float tX = (pB.nodeA.getX() - pA.nodeA.getX()) / (pA.nodeB.getX() - pA.nodeA.getX() - pB.nodeB.getX() + pB.nodeA.getX());
            float tY = (pB.nodeA.getY() - pA.nodeA.getY()) / (pA.nodeB.getY() - pA.nodeA.getY() - pB.nodeB.getY() + pB.nodeA.getY());

        }

        private LinkListNode<Curve.BoxComp> tests = new LinkListNode<>(null);
        private int pCount = 0;
        public final LinkListNode.Chain<Intersection> points = new LinkListNode.Chain<>();

        public Contact(Curve pA, Curve pB, float precision){
            if(BoundingBox.Contact.existsBetween(pA.outerBounds, pB.outerBounds)){
                pA.innerBounds.iterate( (boxA, nodeA) -> {
                    pB.innerBounds.iterate( (boxB, nodeB) -> {
                        if(BoundingBox.Contact.existsBetween(boxA, boxB)) {
                            tests.anteNode.link(new Curve.BoxComp(boxA, boxB)).link(tests);
                        }
                        return true;
                    });
                    return true;
                });
                precision = precision * precision;
                while(tests.postNode != tests) {
                    tests.iterate((bcomp, node) -> {
                        if (node == tests) return false;
                        else {
                            bcomp.divideAndConquer(node, points);
                            return true;
                        }
                    });
                }
            }
        }

    }
}
