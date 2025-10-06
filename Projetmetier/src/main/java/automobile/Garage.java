package automobile;

import java.util.ArrayList;
import java.util.List;

public class Garage {
    private List<Car> cars = new ArrayList<>();

    public void addCar(Car c) {
        cars.add(c);
    }

    public void showCars() {
        for (Car c : cars) {
            System.out.println(c.getModel());
        }
    }
}
