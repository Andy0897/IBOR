package com.example.IBOR.CarModel;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ModelRepository extends CrudRepository<Model, Long> {
    public List<Model> findByBrandId(Long id);

    List<Model> findAllByBrandId(Long brandId);
}