package com.example.IBOR.Car;

import com.example.IBOR.CarBrand.BrandRepository;
import com.example.IBOR.CarModel.ModelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
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
                bindingResult.hasFieldErrors("price") || car.getCategory().equals("null") ||
                car.getEngine().equals("null") || car.getGearbox().equals("null") || car.getEurostandard().equals("null")) {
            model.addAttribute("car", car);
            model.addAttribute("brands", brandRepository.findAll());
            model.addAttribute("models", modelRepository.findAll());
            model.addAttribute("areImagesSelected", !nullImages);
            model.addAttribute("isBrandSelected", !(car.getBrand() == null));
            model.addAttribute("isModelSelected", !(car.getModel() == null));
            model.addAttribute("isEngineSelected", !car.getEngine().equals("null"));
            model.addAttribute("isGearboxSelected", !car.getGearbox().equals("null"));
            model.addAttribute("isEurostandardSelected", !car.getEurostandard().equals("null"));
            model.addAttribute("isCategorySelected", !car.getCategory().equals("null"));
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
