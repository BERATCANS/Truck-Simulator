public class ParkingSystem {

    private AVLTree parkingLots; // AVL ağacı ile park alanlarını saklar

    // Constructor
    public ParkingSystem() {
        this.parkingLots = new AVLTree();
    }

    public AVLTree getParkingLots() {
        return parkingLots;
    }

    public void addParkingLot(int capacity, int truckLimit) {
        ParkingLot newLot = new ParkingLot(capacity, truckLimit);
        parkingLots.insert(newLot);
    }

    public ParkingLot getParkingLot(int capacity) {
        return parkingLots.search(capacity);
    }

    public ParkingLot findReadyLot(int capacity){
        return parkingLots.findReadyParkingLot(capacity);
    }
    public ParkingLot findParkingLot(int capacity){
        return parkingLots.findSmallerParkingLot(capacity);
    }
    public String ready(ParkingLot readyLot) {
        MyQueue waitingQueue = readyLot.getTruck_list_waiting();
        ParkingLot tempLot = readyLot;
        if(waitingQueue.isEmpty() && readyLot.getTruck_limit()==readyLot.getTruck_list_ready().size()){
            return "-1";
        }
        while (waitingQueue.isEmpty()) {
            ParkingLot nextLot = parkingLots.findInOrderSuccessor(tempLot);
            if (nextLot == null) {
                return "-1";
            }
            waitingQueue = nextLot.getTruck_list_waiting();
            tempLot = nextLot;
        }

        Truck truck = waitingQueue.deQueue();
        if (truck != null) {
            readyLot.getTruck_list_ready().enQueue(truck);
            int truckId = truck.getId(); // Truck ID'sini al
            int capacity = tempLot.getCapacity(); // Bu Truck'ın alındığı ParkingLot'un kapasitesini al

            return truckId + " " + capacity;
        }

        return "-1";
    }

}

