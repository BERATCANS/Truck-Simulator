public class Truck {
    private int id;
    private int capacity;
    private int capacityConstraint;

    public Truck(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.capacityConstraint = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacityConstraint() {
        return capacityConstraint;
    }

    public void setCapacityConstraint(int capacityConstraint) {
        this.capacityConstraint = capacityConstraint;
    }

    @Override
    public String toString() {
        return "Truck{" +
                "id='" + id + '\'' +
                ", capacity=" + capacity +
                ", capacityConstraint=" + capacityConstraint +
                '}';
    }
}

