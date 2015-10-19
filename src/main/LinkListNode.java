package main;

import com.sun.istack.internal.NotNull;

/**
 * Created by stephen.broughton on 5/22/15.
 */
public class LinkListNode<T> {

    public LinkListNode<T> anteNode = null;
    public LinkListNode<T> postNode = null;
    public final T value;

    public LinkListNode(T value){
        this.value = value;
    }

    public interface RoutineTypeI<U> {
        boolean run(U value);
    }

    public interface RoutineTypeII<U> {
        boolean run(U value, LinkListNode<U> node);
    }

    public interface RoutineTypeIII<U> {
        void run(U value);
    }

    public interface RoutineTypeIV<U> {
        void run(U value, LinkListNode<U> node);
    }

    public void iterate(@NotNull final RoutineTypeI<T> routine){
        LinkListNode<T> current = this.postNode;
        while( routine.run( current.value ) ){
            current = current.postNode;
        }
    }

    public void iterate(@NotNull final RoutineTypeII<T> routine){
        LinkListNode<T> current = this.postNode;
        while( routine.run( current.value, current ) ){
            current = current.postNode;
        }
    }

    public @NotNull Chain<T> link(@NotNull final LinkListNode<T> node){
        return new Chain(this, node);
    }

    public @NotNull Chain<T> link(@NotNull final Chain<T> chain){
        this.link(chain.anteNode);
        chain.anteNode = this;
        return chain;
    }

    public @NotNull Chain<T> link(final T value){
        return this.link(new LinkListNode<>(value));
    }

    public @NotNull LinkListNode<T> unlink(){
        this.anteNode.link(this.postNode);
        return this;
    }

    public static class Chain<T> {
        protected LinkListNode<T> anteNode;
        protected LinkListNode<T> postNode;

        public Chain(){
        }

        public Chain(@NotNull LinkListNode<T> anteNode, @NotNull LinkListNode<T> postNode){
            this.anteNode = anteNode;
            this.postNode = postNode;
            anteNode.postNode = postNode;
            postNode.anteNode = anteNode;
        }

        public Chain(T anteNodeVal, T postNodeVal){
            this( new LinkListNode<>(anteNodeVal), new LinkListNode<>(postNodeVal) );
        }

        public @NotNull Chain<T> link(@NotNull LinkListNode<T> node){
            if(this.anteNode == null){
                this.postNode = this.anteNode = node;
            } else {
                this.postNode.link(node);
                this.postNode = node;
            }
            return this;
        }

        public @NotNull Chain<T> link(T value){
            return link( new LinkListNode<T>(value) );
        }

        public @NotNull Chain<T> link(@NotNull Chain<T> chain){
            if(chain.anteNode != null) {
                if (this.anteNode == null) {
                    this.anteNode = chain.anteNode;
                    this.postNode = chain.postNode;

                } else {
                    this.postNode.link(chain.anteNode);
                    this.postNode = chain.postNode;
                }
            }
            return this;
        }

        public void iterate(@NotNull RoutineTypeI<T> routine){
            if(this.anteNode != null){
                LinkListNode<T> current = this.anteNode;
                while( routine.run(current.value) && current != this.postNode ){
                    current = current.postNode;
                }
            }
        }

        public void iterate(@NotNull RoutineTypeII<T> routine){
            if(this.anteNode != null){
                LinkListNode<T> current = this.anteNode;
                while( routine.run(current.value, current) && current != this.postNode ){
                    current = current.postNode;
                }
            }
        }

        public @NotNull LinkListNode.Chain<T> unlink(){
            this.anteNode.anteNode.link(this.postNode.postNode);
            return this;
        }

        private transient String asString;

        @Override
        public String toString(){
            asString = "[ ";
            iterate(value -> {
                asString += value.toString() + " ";
                return true;
            });
            return asString + "]";
        }
    }
}
