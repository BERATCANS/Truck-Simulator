
public class ParkingLot {
    private final int capacity_constraint;
    private final int truck_limit;
    private MyQueue truck_list_waiting;
    private MyQueue truck_list_ready;


    // Constructor
    public ParkingLot(int capacity_constraint, int truck_limit) {
        this.capacity_constraint = capacity_constraint;
        this.truck_limit= truck_limit;
        this.truck_list_waiting = new MyQueue(truck_limit);
        this.truck_list_ready = new MyQueue(truck_limit);
    }

    public boolean addTruck(Truck truck) {
        if (truck_list_waiting.size() + truck_list_ready.size() <= truck_limit) {
            truck_list_waiting.enQueue(truck);
            return true;
        } else {
            return false;
        }
    }
    public void clearTrucks() {
        truck_list_waiting.clear(); // This will remove all trucks from the list
    }
    public Truck removeTruck() {
        return truck_list_waiting.deQueue();
    }

    public int getCapacity() {
        return capacity_constraint;
    }

    public int getTruck_limit() {
        return truck_limit;
    }

    public MyQueue getTruck_list_waiting() {
        return truck_list_waiting;
    }

    public MyQueue getTruck_list_ready() {
        return truck_list_ready;
    }

    public boolean isFull(){
        return truck_list_waiting.size() + truck_list_ready.size() == truck_limit;
    }
    public boolean isFullReady(){
        return truck_list_ready.size() == truck_limit;
    }

    @Override
    public String toString() {
        return "ParkingLot{" +
                "capacity_constraint=" + capacity_constraint +
                ", truck_limit=" + truck_limit +
                ", truck_list_waiting=" + truck_list_waiting+
                ", truck_list_ready=" + truck_list_ready +
                '}';
    }
}

