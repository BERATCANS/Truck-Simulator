
public class MyQueue {

    private int queueLength ;
    private Truck items[];
    int front = -1;
    int back = -1;
    public MyQueue(int queueLength) {
        this.queueLength = queueLength;
        this.items = new Truck[queueLength];
    }
    int size() {
        if (isEmpty()) {
            return 0;
        } else {
            return back - front + 1;
        }
    }
    boolean isFull(){
        if(back == queueLength - 1){
            return true;
        } else {
            return false;
        }
    }
    boolean isEmpty(){
        if(front == -1 && back == -1){
            return true;
        } else {
            return false;
        }
    }



    void enQueue(Truck itemValue) {
        if(!isFull()) {
            if (front == -1 && back == -1) {
                front = back = 0;
                items[back] = itemValue;
            } else {
                back++;
                items[back] = itemValue;
            }
        }
    }

    Truck deQueue(){
        if(isEmpty()){
            return null;
        } else {
            Truck dequeuedItem = items[front];
            items[front] = null;
            if (front == back) {
                front = back = -1;
            } else {
                front++;
            }
            return dequeuedItem;
        }
    }

    void display(){
        int i;

        if(!isEmpty()){
            for(i = front; i <= back; i++){
                System.out.println(items[i]);
            }
        }
    }

    void clear() {
        // Set all elements in the array to null
        for (int i = 0; i < queueLength; i++) {
            items[i] = null;
        }
        // Reset front and back indices
        front = -1;
        back = -1;
    }

}
