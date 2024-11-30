package simu.model;

public class Employee {
    private int id;
    private static int i = 1;
    private static long sum = 0;
    private double workingTime;
    private double idleTime;
    private double totalTime;
    private boolean available;

    public Employee() {
        id = ++i;
        workingTime = 0.0;
        totalTime = 0.0;
        idleTime = totalTime - workingTime;
        available = true;
    }

    public int getId() {
        return id;
    }

    public void setWorkingTime(double workingTime) {
        this.workingTime = workingTime;
    }

    public void addWorkingTime(double workingTime) {
        this.workingTime += workingTime;
    }

    public double getWorkingTime() {
        return workingTime;
    }

    public void setIdleTime(double idleTime) {
        this.idleTime = idleTime;
    }

    public double getIdleTime() {
        return idleTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean getAvailable() {
        return available;
    }
}
