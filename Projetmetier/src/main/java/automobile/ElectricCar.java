package automobile;

public class ElectricCar extends Car {
    private int batteryLevel;

    public ElectricCar(String model, int speed, int batteryLevel) {
        super(model, speed);
        this.batteryLevel = batteryLevel;
    }

    public void chargeBattery(int amount) {
        batteryLevel += amount;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }
}
