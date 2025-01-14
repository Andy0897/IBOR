package com.example.IBOR.Car;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends CrudRepository<Car, Long> {
    @Query("SELECT c FROM Car c WHERE c.isOffer = true")
    public Iterable<Car> findOffers();

    @Modifying
    @Query(value = "DELETE FROM car_images WHERE car_car_id = :carId; DELETE FROM cars WHERE car_id = :carId;", nativeQuery = true)
    public void deleteByCarId(@Param("carId") Long carId);
}