package automobile;

public class Car {
    private String model;
    private int speed;

    public Car(String model, int speed) {
        this.model = model;
        this.speed = speed;
    }

    public void accelerate(int increment) {
        speed += increment;
    }

    public void brake() {
        speed -= 10;
    }

    public String getModel() {
        return model;
    }
}
