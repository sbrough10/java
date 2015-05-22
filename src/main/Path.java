package main;

/**
 * Created by stephen.broughton on 5/22/15.
 */
public abstract class Path {

    protected Vector nodeA;
    protected Vector nodeB;

    protected Path(Vector vec0, Vector vec1){
        nodeA = vec0;
        nodeB = vec1;
    }

    public static class Contact {
        public Contact(Path p0, Path p1) {

        }

        private LoopNode intersections;

    }
}
