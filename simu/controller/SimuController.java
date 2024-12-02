package simu.controller;

import simu.model.*;
import java.util.ArrayList;

public class SimuController {
    // Instance variables
    private MyEngine myEngine;
    private Car car;
    private ServicePoint servicePoint;
    private CarDealerShop carDealerShop;
    private Customer customer;

    // Means and variances
    private int arrivalMean;
    private int arrivalVariance;
    private int financeMean;
    private int financeVariance;
    private int testdriveMean;
    private int testdriveVariance;
    private int closureMean;
    private int closureVariance;
    private int simulationSpeed;
    private int arrivalServicePoints;
    private int financeServicePoints;
    private int testdriveServicePoints;
    private int closureServicePoints;

    private int vanMean;
    private int vanVariance;
    private int suvMean;
    private int suvVariance;
    private int sedanMean;
    private int sedanVariance;
    private int sportMean;
    private int sportVariance;
    private int compactMean;
    private int compactVariance;

    // Cars to be created
    private ArrayList<String[]> carsToBeCreated;

    // Parameterless constructor (does not initialize fields)
    public SimuController() {
        carsToBeCreated = new ArrayList<>();
    }

    // Getters and Setters for all instance variables

    public MyEngine getMyEngine() {
        return myEngine;
    }

    public void setMyEngine(MyEngine myEngine) {
        this.myEngine = myEngine;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public ServicePoint getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(ServicePoint servicePoint) {
        this.servicePoint = servicePoint;
    }

    public CarDealerShop getCarDealerShop() {
        return carDealerShop;
    }

    public void setCarDealerShop(CarDealerShop carDealerShop) {
        this.carDealerShop = carDealerShop;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getArrivalMean() {
        return arrivalMean;
    }

    public void setArrivalMean(int arrivalMean) {
        this.arrivalMean = arrivalMean;
    }

    public int getArrivalVariance() {
        return arrivalVariance;
    }

    public void setArrivalVariance(int arrivalVariance) {
        this.arrivalVariance = arrivalVariance;
    }

    public int getFinanceMean() {
        return financeMean;
    }

    public void setFinanceMean(int financeMean) {
        this.financeMean = financeMean;
    }

    public int getFinanceVariance() {
        return financeVariance;
    }

    public void setFinanceVariance(int financeVariance) {
        this.financeVariance = financeVariance;
    }

    public int getTestdriveMean() {
        return testdriveMean;
    }

    public void setTestdriveMean(int testdriveMean) {
        this.testdriveMean = testdriveMean;
    }

    public int getTestdriveVariance() {
        return testdriveVariance;
    }

    public void setTestdriveVariance(int testdriveVariance) {
        this.testdriveVariance = testdriveVariance;
    }

    public int getClosureMean() {
        return closureMean;
    }

    public void setClosureMean(int closureMean) {
        this.closureMean = closureMean;
    }

    public int getClosureVariance() {
        return closureVariance;
    }

    public void setClosureVariance(int closureVariance) {
        this.closureVariance = closureVariance;
    }

    public int getSimulationSpeed() {
        return simulationSpeed;
    }

    public void setSimulationSpeed(int simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
    }

    public int getArrivalServicePoints() {
        return arrivalServicePoints;
    }

    public void setArrivalServicePoints(int arrivalServicePoints) {
        this.arrivalServicePoints = arrivalServicePoints;
    }

    public int getFinanceServicePoints() {
        return financeServicePoints;
    }

    public void setFinanceServicePoints(int financeServicePoints) {
        this.financeServicePoints = financeServicePoints;
    }

    public int getTestdriveServicePoints() {
        return testdriveServicePoints;
    }

    public void setTestdriveServicePoints(int testdriveServicePoints) {
        this.testdriveServicePoints = testdriveServicePoints;
    }

    public int getClosureServicePoints() {
        return closureServicePoints;
    }

    public void setClosureServicePoints(int closureServicePoints) {
        this.closureServicePoints = closureServicePoints;
    }

    public int getVanMean() {
        return vanMean;
    }

    public void setVanMean(int vanMean) {
        this.vanMean = vanMean;
    }

    public int getVanVariance() {
        return vanVariance;
    }

    public void setVanVariance(int vanVariance) {
        this.vanVariance = vanVariance;
    }

    public int getSuvMean() {
        return suvMean;
    }

    public void setSuvMean(int suvMean) {
        this.suvMean = suvMean;
    }

    public int getSuvVariance() {
        return suvVariance;
    }

    public void setSuvVariance(int suvVariance) {
        this.suvVariance = suvVariance;
    }

    public int getSedanMean() {
        return sedanMean;
    }

    public void setSedanMean(int sedanMean) {
        this.sedanMean = sedanMean;
    }

    public int getSedanVariance() {
        return sedanVariance;
    }

    public void setSedanVariance(int sedanVariance) {
        this.sedanVariance = sedanVariance;
    }

    public int getSportMean() {
        return sportMean;
    }

    public void setSportMean(int sportMean) {
        this.sportMean = sportMean;
    }

    public int getSportVariance() {
        return sportVariance;
    }

    public void setSportVariance(int sportVariance) {
        this.sportVariance = sportVariance;
    }

    public int getCompactMean() {
        return compactMean;
    }

    public void setCompactMean(int compactMean) {
        this.compactMean = compactMean;
    }

    public int getCompactVariance() {
        return compactVariance;
    }

    public void setCompactVariance(int compactVariance) {
        this.compactVariance = compactVariance;
    }

    public ArrayList<String[]> getCarsToBeCreated() {
        return carsToBeCreated;
    }

    public void setCarsToBeCreated(ArrayList<String[]> carsToBeCreated) {
        this.carsToBeCreated = carsToBeCreated;
    }
}
