package simu.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    @Test
    void testGetCarType() {
        Car newCar = new Car("Sedan", "Electric", 50000, 2000, 52000);
        assertEquals("Sedan", newCar.getCarType());
    }

    @Test
    void testSetCarType() {
        Car newCar = new Car("Sedan", "Electric", 50000, 2000, 52000);
        assertEquals(0.15, newCar.setCarTypeProb("sport"));
    }

    @Test
    void testCalculateCarTypeProb() {
        Car newCar = new Car("Sedan", "Electric", 50000, 2000, 52000);
        assertEquals(0.0034999999999999996, newCar.calculateCarTypeProb());
    }

    @Test
    void testSetCarTypeProb() {
        Car newCar = new Car("Sedan", "Electric", 50000, 2000, 52000);
        double result = newCar.setCarTypeProb("sport");
        assertEquals(0.15, newCar.setCarTypeProb("sport"));

    }
}
