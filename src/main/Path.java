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
        public static class Point extends Intersection {
            public final float tA;
            public final float tB;

            public Point(float tA, float tB){
                this.tA = tA;
                this.tB = tB;
            }
        }

        public static class Segment extends Intersection {
            public final float t0A;
            public final float t1A;
            public final float t0B;
            public final float t1B;

            public Segment(float t0A, float t1A, float t0B, float t1B){
                this.t0A = t0A;
                this.t1A = t1A;
                this.t0B = t0B;
                this.t1B = t1B;

            }
        }
    }

    public static class Contact {

        private LinkListNode.Chain<Curve.BoxComp> tests = new LinkListNode.Chain<>();
        public final LinkListNode.Chain<Intersection> points = new LinkListNode.Chain<>();

        public Contact(Line pA, Line pB) {

        }

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
                while(tests.postNode != null) {
                    tests.iterate((bcomp, node) -> {
                        bcomp.divideAndConquer(node, points);
                        return true;
                    });
                }
            }
        }

    }
}
