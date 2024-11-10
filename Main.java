import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ParkingSystem parkingSystem = new ParkingSystem();
        int  l = 0;
        String fileName = "type3-small.txt";
        String outputFileName = "output.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);

                String command = st.nextToken();

                if (command.equals("create_parking_lot")) {
                    int capacity_constraint = Integer.parseInt(st.nextToken());
                    int truck_limit = Integer.parseInt(st.nextToken());
                    parkingSystem.addParkingLot(capacity_constraint, truck_limit);

                } else if (command.equals("add_truck")) {
                    int truckId = Integer.parseInt(st.nextToken()); // Kamyonun ID'si
                    int truckCapacity = Integer.parseInt(st.nextToken()); // Kamyonun kapasitesi
                    Truck truck = new Truck(truckId, truckCapacity);
                    ParkingLot parkingLot = parkingSystem.getParkingLot(truckCapacity);
                    if (parkingLot != null && !parkingLot.isFull()) {
                        parkingLot.addTruck(truck);
                        writer.println(parkingLot.getCapacity());
                        parkingSystem.addWaitingLot(parkingLot);
                    } else {
                        parkingLot = parkingSystem.findSmallerLot(truckCapacity);
                        if (parkingLot != null && !parkingLot.isFull()) {
                            parkingLot.addTruck(truck);
                            writer.println(parkingLot.getCapacity());
                            parkingSystem.addWaitingLot(parkingLot);
                        } else {
                            writer.println(-1);
                        }
                    }

                    l++;
                    System.out.println(l);
                    parkingSystem.getParkingLot(11).getTruck_list_waiting().display();

                } else if (command.equals("ready")) {
                    int capacity_constraint = Integer.parseInt(st.nextToken());

                    // Önce kapasiteye göre uygun ParkingLot'u al
                    ParkingLot readyLot = parkingSystem.findWaitingLot(capacity_constraint);
                    if (readyLot == null) {
                        writer.println(-1);
                    } else {
                        Truck truck = readyLot.getTruck_list_waiting().deQueue();
                        if (truck != null) {
                            readyLot.getTruck_list_ready().enQueue(truck);
                            int truckId = truck.getId(); // Truck ID'sini al
                            int capacity = readyLot.getCapacity();
                            writer.println(truckId + " " + capacity);
                            parkingSystem.addReadyLot(readyLot);
                        } else {
                            writer.println(-1);
                        }
                        if(l==598){
                            parkingSystem.getParkingLot(11).getTruck_list_waiting().display();
                        }
                    }
                    l++;
                    parkingSystem.getParkingLot(11).getTruck_list_waiting().display();
                    System.out.println(l);
                } else if (command.equals("load")) {
                    int capacity_constraint = Integer.parseInt(st.nextToken());
                    int load_amount = Integer.parseInt(st.nextToken());
                    int[] outputs = parkingSystem.loadLots(capacity_constraint, load_amount);
                    if (outputs.length == 0) {
                        writer.println(-1);
                    }
                    for (int i = 0; i < outputs.length; i++) {
                        writer.print(outputs[i]);
                        // Eğer bir sonraki eleman varsa ve index'in sonuna gelmediysek " - " ekle
                        if (i < outputs.length - 1 && i % 2 == 1) {
                            writer.print(" - ");
                        }
                        if (i < outputs.length - 1 && i % 2 == 0) {
                            writer.print(" ");
                        }

                    }
                    writer.println();

                    l++;
                    parkingSystem.getParkingLot(11).getTruck_list_waiting().display();
                    System.out.println(l);
                }
            }


            long endTime = System.currentTimeMillis();
            long time = (endTime - startTime)/1000;
            System.out.println(time);
        }
        catch (IOException e) {
            System.err.println("Dosya okunurken bir hata oluştu: " + e.getMessage());
        }
    }
}

