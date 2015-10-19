package main;

/**
 * Created by stephen.broughton on 5/22/15.
 */
public abstract class Path {

    protected Vector2D nodeA;
    protected Vector2D nodeB;

    protected Path(Vector2D vecA, Vector2D vecB){
        nodeA = vecA;
        nodeB = vecB;
    }

    public abstract Vector2D getDirection(float t);

    public void transform(AffineMatrix am){
        am.transpose(nodeA);
        am.transpose(nodeB);
    }

    public static class Intersection {
        public final float t0A;
        public final float t1A;
        public final float t0B;
        public final float t1B;

        public Intersection(float t0A, float t1A, float t0B, float t1B){
            this.t0A = t0A;
            this.t1A = t1A;
            this.t0B = t0B;
            this.t1B = t1B;

        }

        @Override
        public String toString() {
            return "{ t_A: (" + t0A + "," + t1A + "), t_B: (" + t0B + "," + t1B + ") }";
        }
    }

    public static class Contact {

        public final LinkListNode.Chain<Intersection> intersections = new LinkListNode.Chain<>();

        public Contact(Line pA, Line pB) {
            Vector2D vecA = pA.getDirection(0);
            Vector2D vecB = pB.getDirection(0);
            float denom = vecA.getX() * vecB.getY() - vecA.getY() * vecB.getX();
            if(denom != 0){
                float diffX = pA.nodeA.getX() - pB.nodeA.getX();
                float diffY = pA.nodeA.getY() - pB.nodeA.getY();
                float tA = (vecB.getX() * diffY - vecB.getY() * diffX) / +denom;
                float tB = (vecA.getY() * diffX - vecA.getX() * diffY) / -denom;
                if(0 <= tA && tA <= 1 && 0 <= tB && tB <= 1){
                    intersections.link(new Intersection(tA, tA, tB, tB));
                }
            }
        }

        private transient int count;
        public Contact(Curve pA, Curve pB, float precision){
            if(BoundingBox.Contact.existsBetween(pA.outerBounds, pB.outerBounds)){
                LinkListNode.Chain<Curve.BoxComp> tests = new LinkListNode.Chain<>();
                final LinkListNode.Chain<Curve.BoxComp> finalTests = tests;
                pA.innerBounds.iterate( (boxA, nodeA) -> {
                    pB.innerBounds.iterate( (boxB, nodeB) -> {
                        if(BoundingBox.Contact.existsBetween(boxA, boxB)) {
                            finalTests.link(new Curve.BoxComp(boxA, boxB));
                        }
                        return true;
                    });
                    return true;
                });
                count = 0;
                while(tests.postNode != null) {
                    LinkListNode.Chain<Curve.BoxComp> nextTests = new LinkListNode.Chain<>();
                    tests.iterate(bcomp -> {
                        count += 4;
                        bcomp.divideAndConquer(nextTests, intersections, precision);
                        return true;
                    });
                    tests = nextTests;
                }
                System.out.println("Intersect Test Count: " + count);
            }
        }

    }
}
