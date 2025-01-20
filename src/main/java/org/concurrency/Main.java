package org.concurrency;

import java.util.Random;
import java.util.concurrent.TimeUnit;

interface Vehicle {
}

class TwoWheeler implements Vehicle {
}

class FourWheeler implements Vehicle {
}

class ParkingSystem {
    private final int TWO_WHEELER_PARKING = 10;
    private final int FOUR_WHEELER_PARKING = 10;
    private final Object lock = new Object();
    private int twoWheelerCount = 0;
    private int fourWheelerCount = 0;

    public void park(Vehicle vehicle) {
        synchronized (lock) {
            if (vehicle instanceof FourWheeler && fourWheelerCount < FOUR_WHEELER_PARKING) {
                fourWheelerCount++;
                System.out.println(vehicle.getClass().getSimpleName() + " parked. Four wheeler count: " + fourWheelerCount);
            } else if (vehicle instanceof TwoWheeler && twoWheelerCount < TWO_WHEELER_PARKING) {
                twoWheelerCount++;
                System.out.println(vehicle.getClass().getSimpleName() + " parked. Two wheeler count: " + twoWheelerCount);
            } else {
                System.out.println(vehicle.getClass().getSimpleName() + " parking not available.");
            }
        }
    }

    public void leave(Vehicle vehicle) {
        synchronized (lock) {
            if (vehicle instanceof FourWheeler && fourWheelerCount > 0) {
                fourWheelerCount--;
                System.out.println(vehicle.getClass().getSimpleName() + " left. Four wheeler Count: " + fourWheelerCount);
            } else if (vehicle instanceof TwoWheeler && twoWheelerCount > 0) {
                twoWheelerCount--;
                System.out.println(vehicle.getClass().getSimpleName() + " left. Two wheeler Count: " + twoWheelerCount);
            }
        }
    }
}

public class Main {

    public static void main(String[] args) {
        // Implementation of parking system using oops and multi threading
        ParkingSystem parkingSystem = new ParkingSystem();
        while (true) {
            Random random = new Random();
            int randomVehicleSelector = random.nextInt(0, 2);
            int randomActionSelector = random.nextInt(0, 2);
            int randomSleep = random.nextInt(0, 2);
            try {
                TimeUnit.SECONDS.sleep(randomSleep);
                switch (randomVehicleSelector) {
                    case 0 -> {
                        if (randomActionSelector == 0) {
                            Thread.ofVirtual().start(() -> parkingSystem.park(new FourWheeler()));
                        } else {
                            Thread.ofVirtual().start(() -> parkingSystem.park(new TwoWheeler()));
                        }
                    }
                    case 1 -> {
                        if (randomActionSelector == 0) {
                            Thread.ofVirtual().start(() -> parkingSystem.leave(new FourWheeler()));
                        } else {
                            Thread.ofVirtual().start(() -> parkingSystem.leave(new TwoWheeler()));
                        }
                    }
                    default -> {}

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}