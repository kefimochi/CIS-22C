import java.util.Iterator;
import java.util.NoSuchElementException;

import StackAndQueuePackage.*;

public class BinaryTree<T> implements BinaryTreeInterface<T> {
    private BinaryNode<T> root;

    public BinaryTree() {
        root = null;
    }

    public BinaryTree(T rootData) {
        root = new BinaryNode<>(rootData);
    }

    public BinaryTree(T rootData, BinaryTree<T> leftTree, 
    BinaryTree<T> rightTree) {
        privateSetTree(rootData, leftTree, rightTree);
    }

    public void setTree(T rootData) {
        root = new BinaryNode<>(rootData);
    } // end setTree

    public void setTree(T rootData, BinaryTreeInterface<T> leftTree,
    BinaryTreeInterface<T> rightTree) {
        privateSetTree(rootData, (BinaryTree<T>)leftTree, 
        (BinaryTree<T>)rightTree);
    } // end setTree

    private void privateSetTree(T rootData, BinaryTree<T> leftTree, 
    BinaryTree<T> rightTree) {
        root = new BinaryNode<>(rootData);

        if ((leftTree != null) && !leftTree.isEmpty()) {
            root.setLeftChild(leftTree.root);
        }

        if ((rightTree != null) && !rightTree.isEmpty()) {
            if (rightTree != leftTree) {
                root.setRightChild(rightTree.root);
            } else {
                root.setRightChild(rightTree.root.copy());
            }
        } // end if

        if ((leftTree != null) && (leftTree != this)) {
            leftTree.clear(); 
        }

        if ((rightTree != null) && (rightTree != this)){
            rightTree.clear();
        }
    } // end privateSetTree    

    private class PreorderIterator implements Iterator<T> {
        private StackInterface<BinaryNode<T>> nodeStack;

        public PreorderIterator() {
            nodeStack = new LinkedStack<>();
            if (root != null)
            nodeStack.push(root);
        }

        public boolean hasNext() {
            return !nodeStack.isEmpty();
        }

        public T next() {
            BinaryNode<T> nextNode;

            if (hasNext()) {
                nextNode = nodeStack.pop();
                BinaryNode<T> leftChild = nextNode.getLeftChild();
                BinaryNode<T> rightChild = nextNode.getRightChild();

                // Push into stack in reverse order of recursive calls
                if (rightChild != null) {
                    nodeStack.push(rightChild);
                }
                if (leftChild != null) {
                    nodeStack.push(leftChild);
                }
            } else {
                throw new NoSuchElementException();
            }
            return nextNode.getData();
        } // end next

        public void remove() {
            throw new UnsupportedOperationException();
        }
    } // end PreorderIterator

    public void iterativePreorderTraverse() {
        StackInterface<BinaryNode<T>> nodeStack = new LinkedStack<>();
        if (root != null) {
            nodeStack.push(root);
        }
        BinaryNode<T> nextNode;
        while (!nodeStack.isEmpty()) {
            nextNode = nodeStack.pop();
            BinaryNode<T> leftChild = nextNode.getLeftChild();
            BinaryNode<T> rightChild = nextNode.getRightChild();

            // Push into stack in reverse order of recursive calls
            if (rightChild != null) {
                nodeStack.push(rightChild);
            }

            if (leftChild != null) {
                nodeStack.push(leftChild);
            }
            System.out.print(nextNode.getData() + " ");
        } 
    } // end iterativePreorderTraverse

    private class InorderIterator implements Iterator<T> {
        private StackInterface<BinaryNode<T>> nodeStack;
        private BinaryNode<T> currentNode;

        public InorderIterator() {
            nodeStack = new LinkedStack<>();
            currentNode = root;
        }

        public boolean hasNext() {
            return !nodeStack.isEmpty() || (currentNode != null);
        }

        public T next() {
            BinaryNode<T> nextNode = null;

            // Find leftmost node with no left child
            while (currentNode != null) {
                nodeStack.push(currentNode);
                currentNode = currentNode.getLeftChild();
            }

            // Get leftmost node, then move to its right subtree
            if (!nodeStack.isEmpty()) {
                nextNode = nodeStack.pop();
                assert nextNode != null; // Since nodeStack was not empty
                // before the pop
                currentNode = nextNode.getRightChild();
            } else {
                throw new NoSuchElementException();
            }
            return nextNode.getData(); 
        } // end next

        public void remove() {
            throw new UnsupportedOperationException();
        }
    } // end InorderIterator

    public void iterativeInorderTraverse() {
        StackInterface<BinaryNode<T>> nodeStack = new LinkedStack<>();
        BinaryNode<T> currentNode = root;

        while (!nodeStack.isEmpty() || (currentNode != null)) {
            // Find leftmost node with no left child
            while (currentNode != null) {
                nodeStack.push(currentNode);
                currentNode = currentNode.getLeftChild();
            }

            // Visit leftmost node, then traverse its right subtree
            if (!nodeStack.isEmpty()) {
                BinaryNode<T> nextNode = nodeStack.pop();
                assert nextNode != null; // Since nodeStack was not empty
                // before the pop
                System.out.print(nextNode.getData() + " ");
                currentNode = nextNode.getRightChild();
            }
        }
    } // end iterativeInorderTraverse

    private class PostorderIterator implements Iterator<T> {
        private StackInterface<BinaryNode<T>> nodeStack;
        private BinaryNode<T> currentNode;

        public PostorderIterator() {
            nodeStack = new LinkedStack<>();
            currentNode = root;
        }

        public boolean hasNext() {
            return !nodeStack.isEmpty() || (currentNode != null);
        }

        public T next() {
            BinaryNode<T> leftChild, rightChild, nextNode = null;

            // Find leftmost leaf
            while (currentNode != null) {
                nodeStack.push(currentNode);
                leftChild = currentNode.getLeftChild();
                if (leftChild == null) {
                    currentNode = currentNode.getRightChild();
                } else {
                    currentNode = leftChild;
                }
            } // end while

            // Stack is not empty either because we just pushed a node, or
            // it wasn't empty to begin with since hasNext() is true.
            // But Iterator specifies an exception for next() in case
            // hasNext() is false.

            if (!nodeStack.isEmpty()) {
                nextNode = nodeStack.pop();
                // nextNode != null since stack was not empty before pop

                BinaryNode<T> parent = null;
                if (!nodeStack.isEmpty()) {
                    parent = nodeStack.peek();
                    if (nextNode == parent.getLeftChild()) {
                        currentNode = parent.getRightChild();
                    } else {
                        currentNode = null;
                    }
                } else {
                    currentNode = null;
                }
            } else {
                throw new NoSuchElementException();
            } // end if

            return nextNode.getData();
        } 

        public void remove() {
            throw new UnsupportedOperationException();
        } 
    } // end PostorderIterator

    private class LevelOrderIterator implements Iterator<T>
    {
        private QueueInterface<BinaryNode<T>> nodeQueue;

        public LevelOrderIterator() {
            nodeQueue = new LinkedQueue<>();
            if (root != null)
            nodeQueue.enqueue(root);
        }

        public boolean hasNext() {
            return !nodeQueue.isEmpty();
        } 

        public T next() {
            BinaryNode<T> nextNode;

            if (hasNext()) {
                nextNode = nodeQueue.dequeue();
                BinaryNode<T> leftChild = nextNode.getLeftChild();
                BinaryNode<T> rightChild = nextNode.getRightChild();

                // Add to queue in order of recursive calls
                if (leftChild != null) {
                    nodeQueue.enqueue(leftChild);
                }
                if (rightChild != null) {
                    nodeQueue.enqueue(rightChild);
                }
            } else {
                throw new NoSuchElementException();
            }
            return nextNode.getData();
        } // end next

        public void remove()
        {
        throw new UnsupportedOperationException();
        } // end remove
    } // end LevelOrderIterator

    public T getRootData() {
        return null;
    }

    public int getHeight() {
        return 0;
    }

    public int getNumberOfNodes() {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public void clear() {

    }

    public Iterator<T> getPreorderIterator() {
        return null;
    }

    public Iterator<T> getPostorderIterator() {
        return null;
    }

    public Iterator<T> getInorderIterator() {
        return null;
    }

    public Iterator<T> getLevelOrderIterator() {
        return null;
    }
} // end BinaryTree