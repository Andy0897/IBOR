package com.example.IBOR.Car;

import com.example.IBOR.CarBrand.BrandRepository;
import com.example.IBOR.CarModel.ModelDTO;
import com.example.IBOR.CarModel.ModelRepository;
import com.example.IBOR.ImageEncoder;
import jakarta.validation.Valid;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cars")
public class CarController {
    CarRepository carRepository;
    CarService carService;
    BrandRepository brandRepository;
    ModelRepository modelRepository;

    public CarController(CarRepository carRepository, CarService carService, BrandRepository brandRepository, ModelRepository modelRepository) {
        this.carRepository = carRepository;
        this.carService = carService;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
    }

    @GetMapping("/")
    public String getShowCars(Model model) {
        List<Car> cars = (List<Car>) carRepository.findAll();
        model.addAttribute("cars", cars);
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("encoder", new ImageEncoder());
        return "car/show";
    }

    @GetMapping("/filter")
    public String filterCars(
            @RequestParam(value = "brand", required = false) Long brandId,
            @RequestParam(value = "model", required = false) Long modelId,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(value = "mileage", required = false) Integer maxMileage,
            @RequestParam(value = "isOffer", required = false) Boolean isOffer,
            Model model) {

        List<Car> filteredCars = ((List<Car>) carRepository.findAll()).stream()
                .filter(car -> (brandId == null || car.getBrand().getId().equals(brandId)))
                .filter(car -> (modelId == null || car.getModel().getId().equals(modelId)))
                .filter(car -> (minPrice == null || car.getPrice() >= minPrice))
                .filter(car -> (maxPrice == null || car.getPrice() <= maxPrice))
                .filter(car -> (maxMileage == null || car.getMileage() <= maxMileage))
                .filter(car -> (isOffer == null || car.isOffer() == isOffer))
                .collect(Collectors.toList());

        model.addAttribute("cars", filteredCars);
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("models", modelRepository.findAll());
        model.addAttribute("encoder", new ImageEncoder());
        return "car/show";
    }


    @GetMapping("/show/{Id}")
    public String getShowCar(@PathVariable("Id") Long id, Model model) {
        Car car = carRepository.findById(id).get();
        model.addAttribute("car", car);
        model.addAttribute("encoder", new ImageEncoder());
        return "car/showSingle";
    }


    @GetMapping("/add")
    public String getAddCar(Model model) {
        Car car = new Car();
        model.addAttribute("car", car);
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("isBrandSelected", true);
        model.addAttribute("isModelSelected", true);
        model.addAttribute("areImagesSelected", true);
        model.addAttribute("isEngineSelected", true);
        model.addAttribute("isGearboxSelected", true);
        model.addAttribute("isEurostandardSelected", true);
        model.addAttribute("isCategorySelected", true);
        model.addAttribute("hasUploadError", false);
        return "car/add";
    }

    @PostMapping("/submit")
    public String submitCar(@Valid Car car, BindingResult bindingResult, @RequestParam("images") MultipartFile[] images, @RequestParam(value = "mainImageIndex", required = false) Integer mainImageIndex, Model model) {
        if (mainImageIndex == null || mainImageIndex < 0 || mainImageIndex >= car.getImages().size()) {
            mainImageIndex = 0;
        }
        return carService.submitCar(car, bindingResult, images, mainImageIndex, model);
    }

    @PostMapping("/delete/{id}")
    public String getSubmitDeleteCar(@PathVariable("id") Long id) {
        return carService.submitDeleteCar(id);
    }

    @GetMapping("/offers/add/{id}")
    public String getAddOffer(@PathVariable("id") Long id, Model model) {
        int offerPrice = 1000;
        model.addAttribute("carId", id);
        model.addAttribute("offerPrice", offerPrice);
        model.addAttribute("invalidPrice", false);
        return "car/addOffer";
    }

    @PostMapping("/offers/submit")
    public String getSubmitOffer(@RequestParam("carId") Long id, @RequestParam("offerPrice") int offerPrice, Model model) {
        return carService.submitOffer(id, offerPrice, model);
    }

    @PostMapping("/offers/delete/{id}")
    public String getSubmitDeleteOffer(@PathVariable("id") Long id) {
        return carService.submitDeleteOffer(id);
    }
}
