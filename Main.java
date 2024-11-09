import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) {
        ParkingSystem parkingSystem = new ParkingSystem();

        String fileName = "type1-small.txt";
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

                }

                else if (command.equals("add_truck")) {
                    int truckId = Integer.parseInt(st.nextToken()); // Kamyonun ID'si
                    int truckCapacity = Integer.parseInt(st.nextToken()); // Kamyonun kapasitesi

                    Truck truck = new Truck(truckId, truckCapacity);
                    ParkingLot parkingLot = parkingSystem.getParkingLot(truckCapacity);
                    if (parkingLot != null && !parkingLot.isFull()) {
                        parkingLot.addTruck(truck);
                        writer.println(parkingLot.getCapacity());
                    }
                    else{
                        parkingLot = parkingSystem.findParkingLot(truckCapacity);
                        if (parkingLot != null && !parkingLot.isFull()) {
                            parkingLot.addTruck(truck);
                            writer.println(parkingLot.getCapacity());
                        }
                        else{
                            writer.println(-1);
                        }
                    }
                }

                else if (command.equals("ready")) {
                    int capacity_constraint = Integer.parseInt(st.nextToken());

                    // Önce kapasiteye göre uygun ParkingLot'u al
                    ParkingLot readyLot = parkingSystem.findReadyLot(capacity_constraint);

                    if (readyLot == null) {
                        writer.println(-1);
                    }
                    else {
                        // ParkingSystem'in ready fonksiyonundan alınan sonucu yazdır
                        String id = parkingSystem.ready(readyLot);
                        if (id.equals("-1")) {
                            writer.println(-1);
                        } else {
                            writer.println(id);
                        }
                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println("Dosya okunurken bir hata oluştu: " + e.getMessage());
        }
    }
}

