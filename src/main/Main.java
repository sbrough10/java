package main;
/**
 * Created by stephen.broughton on 5/21/15.
 */
public class Main {

    public static void main(String args[]){

        Curve curveA, curveB;
        curveA = new Curve(209, 21, 621, 35, 144, 376, 556, 373);
        curveB = new Curve(193, 526, 408, 516, 68, 281, 505, 141);

        curveA.generateBounds();
        curveB.generateBounds();

        long start, end;
        Path.Contact contact;

        start = System.currentTimeMillis();
        contact = new Path.Contact(curveA, curveB, 128);
        end = System.currentTimeMillis() - start;
        System.out.println("End: " + end);

        contact.intersections.iterate(intersect -> {
            drawPoint(curveA.getPointOnCurve(intersect.t0A));
            drawPoint(curveA.getPointOnCurve(intersect.t1A));
            drawPoint(curveB.getPointOnCurve(intersect.t0B));
            drawPoint(curveB.getPointOnCurve(intersect.t1B));
            return true;
        });

        Line lineA = new Line(0, 0, 100, 100);
        Line lineB = new Line(10, 0, 110, 110);

        start = System.nanoTime();
        contact = new Path.Contact(lineA, lineB);
        end = System.nanoTime() - start;
        System.out.println("End: " + end);

    }

    public static void drawPoint(Vector2D vec){
        System.out.println("drawPoint({x: " + vec.getX() + ", y: " + vec.getY() + "}, 3);");
    }

}
