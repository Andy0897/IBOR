package com.example.IBOR.Car;

import com.example.IBOR.CarPart.CarPart;

import java.util.List;

public class CarWithBase64Images {
    private Car car;
    private List<String> base64Images;

    public CarWithBase64Images(Car car, List<String> base64Images) {
        this.car = car;
        this.base64Images = base64Images;
    }

    public Car getCar() {
        return car;
    }

    public List<String> getBase64Images() {
        return base64Images;
    }
}
