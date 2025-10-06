package automobile;

public class Truck extends Car {
    private int capacity;

    public Truck(String model, int capacity) {
        super(model, 0);
        this.capacity = capacity;
    }

    public void load(int weight) {
        if (weight <= capacity) {
            System.out.println("Loading " + weight + " kg");
        } else {
            System.out.println("Over capacity!");
        }
    }

    public int getCapacity() {
        return capacity;
    }
}
