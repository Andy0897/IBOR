package com.example.IBOR.CarPart;

import com.example.IBOR.Car.Car;

import java.util.List;

public class CarPartWithBase64Images {
    private CarPart carPart;
    private List<String> base64Images;

    public CarPartWithBase64Images(CarPart carPart, List<String> base64Images) {
        this.carPart = carPart;
        this.base64Images = base64Images;
    }

    public CarPart getCarPart() {
        return carPart;
    }

    public List<String> getBase64Images() {
        return base64Images;
    }
}
