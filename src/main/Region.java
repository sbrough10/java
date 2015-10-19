package main;

/**
 * Created by srb on 5/23/15.
 */
public class Region {

    LinkListNode.Chain<Path> edges = new LinkListNode.Chain<>();
    Vector2D lastNode;

    public Region(Vector2D nodeA){
        lastNode = nodeA;
    }

    public Region addLine(Vector2D nodeB){
        edges.link(new Line(lastNode, nodeB));
        lastNode = nodeB;
        return this;
    }

    public Region addCurve(Vector2D ctrlA, Vector2D ctrlB, Vector2D nodeB){
        edges.link(new Curve(lastNode, ctrlA, ctrlB, nodeB));
        lastNode = nodeB;
        return this;
    }

    public Region close(){
        if(!isClosed()){
            addLine(edges.anteNode.value.nodeA);
        }
        return this;
    }

    public boolean isClosed(){
        return edges.anteNode.value.nodeA == lastNode;
    }

    public float getArea(){
        return 0;
    }

    public static class Contact {



    }
}
