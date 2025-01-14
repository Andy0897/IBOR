package com.example.IBOR.CarModel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/models")
public class ModelController {
    ModelRepository modelRepository;

    public ModelController(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    @ResponseBody
    @GetMapping(value = "/models-by-brand/{brandId}")
    public List<ModelDTO> getModelsByBrand(@PathVariable("brandId") Long brandId) {
        List<ModelDTO> models = modelRepository.findAllByBrandId(brandId).stream().map(model -> new ModelDTO(model.getId(), model.getName()))
                .collect(Collectors.toList());
        return models;
    }
}
