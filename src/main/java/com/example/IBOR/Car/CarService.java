package com.example.IBOR.Car;

import com.example.IBOR.CarBrand.BrandRepository;
import com.example.IBOR.CarModel.ModelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {
    private CarRepository carRepository;
    private BrandRepository brandRepository;
    private ModelRepository modelRepository;

    public CarService(CarRepository carRepository, BrandRepository brandRepository, ModelRepository modelRepository) {
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
    }

    @Transactional
    public String submitCar(Car car, BindingResult bindingResult, MultipartFile[] images, Integer mainImageIndex, Model model) {
        List<byte[]> imageList = new ArrayList<>();
        boolean nullImages = true;

        try {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    imageList.add(file.getBytes());
                    nullImages = false;
                }
            }
        } catch (IOException e) {
            model.addAttribute("car", car);
            model.addAttribute("hasUploadError", true);
            return "car/add";
        }

        if (car.getBrand() == null || car.getModel() == null || nullImages ||
                bindingResult.hasFieldErrors("power") || bindingResult.hasFieldErrors("mileage") ||
                bindingResult.hasFieldErrors("price") || car.getCategory() == null ||
                car.getEngine() == null || car.getGearbox() == null || car.getEurostandard() == null) {
            System.out.println("brand: " + car.getBrand());
            System.out.println("model: " + car.getModel());
            System.out.println("engine: " + car.getEngine());
            System.out.println("gearbox: " + car.getGearbox());
            model.addAttribute("car", car);
            model.addAttribute("brands", brandRepository.findAll());
            model.addAttribute("models", modelRepository.findAll());
            model.addAttribute("areImagesSelected", !nullImages);
            model.addAttribute("isBrandSelected", car.getBrand() != null);
            model.addAttribute("isModelSelected", car.getModel() != null);
            model.addAttribute("isEngineSelected", !car.getEngine().isEmpty());
            model.addAttribute("isGearboxSelected", !car.getGearbox().isEmpty());
            model.addAttribute("isEurostandardSelected", !car.getEurostandard().isEmpty());
            model.addAttribute("isCategorySelected", !car.getCategory().isEmpty());
            return "car/add";
        }

        car.setImages(imageList);

        // Save the selected main image index
        car.setMainImageIndex(mainImageIndex);

        carRepository.save(car);
        return "redirect:/home";
    }

    public String submitDeleteCar(Long id) {
        carRepository.deleteById(id);
        return "redirect:/cars/";
    }

    public String submitOffer(Long id, int offerPrice, Model model) {
        Car car = carRepository.findById(id).get();
        car.setOfferPrice(offerPrice);
        if (car.getOfferPrice() >= car.getPrice() || car.getOfferPrice() < 1000) {
            model.addAttribute("carId", id);
            model.addAttribute("offerPrice", offerPrice);
            model.addAttribute("invalidPrice", true);
            return "car/addOffer";
        }
        car.setOffer(true);
        carRepository.save(car);
        return "redirect:/cars/show/" + id;
    }

    public String submitDeleteOffer(Long id) {
        Car car = carRepository.findById(id).get();
        car.setOffer(false);
        car.setOfferPrice(0);
        carRepository.save(car);
        return "redirect:/cars/show/" + id;
    }


}
