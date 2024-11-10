import java.util.ArrayList;

public class ParkingSystem {

    private AVLTree parkingLots;
    private AVLTree waitingLots;
    private AVLTree readyLots;

    // Constructor
    public ParkingSystem() {
        this.parkingLots = new AVLTree();
        this.waitingLots = new AVLTree();
        this.readyLots = new AVLTree();
    }

    public AVLTree getParkingLots() {
        return parkingLots;
    }

    public AVLTree getWaitingLots() {
        return waitingLots;
    }

    public AVLTree getReadyLots() {
        return readyLots;
    }

    public void addParkingLot(int capacity, int truckLimit) {
        ParkingLot newLot = new ParkingLot(capacity, truckLimit);
        parkingLots.insert(newLot);
    }

    public void addWaitingLot(ParkingLot waitingLot) {
        waitingLots.insert(waitingLot);
    }

    public void addReadyLot(ParkingLot waitingLot) {
        readyLots.insert(waitingLot);
    }

    public ParkingLot getParkingLot(int capacity) {
        return parkingLots.search(capacity);
    }

    public ParkingLot findSmallerLot(int capacity) {
        ParkingLot smallerLot = parkingLots.findInOrderPredecessor(capacity);
        if (smallerLot == null) {
            return null;
        }
        while (smallerLot.isFull()) {
            smallerLot = parkingLots.findInOrderPredecessor(smallerLot);
            if (smallerLot == null) {
                return null;
            }
        }
        return smallerLot;
    }

    public int addingTruck(Truck truck, int capacity) {
        ParkingLot parkingLot = getParkingLot(capacity);
        if (parkingLot != null && !parkingLot.isFull()) {
            if (parkingLot.getTruck_list_waiting().isEmpty()) {
                waitingLots.insert(parkingLot);
            }
            parkingLot.addTruck(truck);
            return parkingLot.getCapacity();
        }
        else {
            parkingLot = this.findSmallerLot(capacity);
            if (parkingLot != null && !parkingLot.isFull()) {
                if (parkingLot.getTruck_list_waiting().isEmpty()) {
                    waitingLots.insert(parkingLot);
                }
                parkingLot.addTruck(truck);
                return parkingLot.getCapacity();
            } else {
                return -1;
            }
        }
    }

    public ParkingLot findWaitingLot(int capacity) {
        ParkingLot readyLot = waitingLots.search(capacity);
        if (readyLot == null) {
            readyLot = waitingLots.findInOrderSuccessor(capacity);
        }
        if (readyLot == null) {
            return null;
        }
        while (readyLot != null && readyLot.getTruck_list_waiting().isEmpty()) {
            readyLot = waitingLots.findInOrderSuccessor(readyLot);
        }
        if (readyLot != null && readyLot.getTruck_list_waiting().size() == 1) {
            waitingLots.delete(readyLot.getCapacity());
        }
        return readyLot;
    }

    public int[] loadLots(int capacity, int load_amount) {
        ArrayList<Integer> outputs = new ArrayList<>();
        ParkingLot loadLot = readyLots.search(capacity);
        ArrayList<Truck> trucks = new ArrayList<>();
        if (loadLot == null) {
            loadLot = readyLots.findInOrderSuccessor(capacity);
        }

        if (loadLot == null) {
            outputs.add(-1);
        } else {
            while (load_amount != 0 && loadLot != null) {
                while (load_amount != 0 && !loadLot.getTruck_list_ready().isEmpty()) {
                    Truck loadedTruck = loadLot.getTruck_list_ready().deQueue();
                    if (loadLot.getTruck_list_ready().isEmpty()) {
                        readyLots.delete(loadLot.getCapacity());
                    }
                    if (loadLot.getCapacity() <= load_amount) {
                        loadedTruck.setCapacityConstraint(loadedTruck.getCapacityConstraint() - loadLot.getCapacity());
                        if (loadedTruck.getCapacityConstraint() == 0) {
                            loadedTruck.setCapacityConstraint(loadedTruck.getCapacity());
                        }
                        trucks.add(loadedTruck);
                        load_amount -= loadLot.getCapacity();
                    } else {
                        loadedTruck.setCapacityConstraint(loadedTruck.getCapacityConstraint() - load_amount);
                        trucks.add(loadedTruck);
                        load_amount = 0;
                    }
                }
                loadLot = readyLots.findInOrderSuccessor(loadLot);
            }
        }
        for(Truck truck:trucks){
            int placeCapacity = addingTruck(truck, truck.getCapacityConstraint());
            outputs.add(truck.getId());
            outputs.add(placeCapacity);
        }
        // Eğer outputs boşsa -1 dönen bir dizi oluştur
        if (outputs.isEmpty()) {
            return new int[]{-1};
        }

        // outputs ArrayList'ini int[] formatına çevir
        int[] resultArray = new int[outputs.size()];
        for (int i = 0; i < outputs.size(); i++) {
            resultArray[i] = outputs.get(i);
        }

        return resultArray;
    }
}


