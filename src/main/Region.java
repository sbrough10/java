package main;

/**
 * Created by srb on 5/23/15.
 */
public class Region {

    LinkListNode.Chain<Path> edges = new LinkListNode.Chain<>();
    Vector lastNode;

    public Region(Vector nodeA){
        lastNode = nodeA;
    }

    public Region addLine(Vector nodeB){
        edges.link(new Line(lastNode, nodeB));
        lastNode = nodeB;
        return this;
    }

    public Region addCurve(Vector ctrlA, Vector ctrlB, Vector nodeB){
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
}
