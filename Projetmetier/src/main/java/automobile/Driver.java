package automobile;

public class Driver {
    private String name;

    public Driver(String name) {
        this.name = name;
    }

    public void drive(Car car) {
        System.out.println(name + " is driving " + car.getModel());
        car.accelerate(10);
    }

    public void testTruck(Truck truck) {
        System.out.println(name + " is testing the truck...");
        truck.load(500);  // appel vers Truck.load
        truck.brake();    // appel hérité de Car
    }
}
