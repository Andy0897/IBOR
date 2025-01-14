package com.example.IBOR.CarPart;

import com.example.IBOR.Car.Car;
import com.example.IBOR.Car.CarWithBase64Images;
import com.example.IBOR.CarBrand.BrandRepository;
import jakarta.validation.Valid;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/car-parts")
public class CarPartController {
    private CarPartRepository carPartRepository;
    private CarPartService carPartService;
    private BrandRepository brandRepository;

    public CarPartController(CarPartRepository carPartRepository, CarPartService carPartService, BrandRepository brandRepository) {
        this.carPartRepository = carPartRepository;
        this.carPartService = carPartService;
        this.brandRepository = brandRepository;
    }

    @GetMapping("/add")
    public String getAddCarPart(Model model) {
        model.addAttribute("carPart", new CarPart());
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("categories", List.of(
                "Трансмисия",
                "Спирачна система",
                "Окачване и кормилно управление",
                "Електрическа система",
                "Двигател",
                "Филтри",
                "Аксесоари",
                "Интериор",
                "Екстериор",
                "Системи за охлаждане",
                "Масла и течности"
        ));
        model.addAttribute("isBrandSelected", true);
        model.addAttribute("isModelSelected", true);
        model.addAttribute("isCategorySelected", true);
        model.addAttribute("areImagesSelected", true);

        return "car-part/add";
    }

    @PostMapping("/submit")
    public String submitCarPart(
            @ModelAttribute @Valid CarPart carPart,
            BindingResult bindingResult,
            @RequestParam("images") MultipartFile[] images,
            @RequestParam(value = "mainImageIndex", required = false) Integer mainImageIndex,
            Model model) {

        if (mainImageIndex == null || mainImageIndex < 0 || mainImageIndex >= carPart.getImages().size()) {
            mainImageIndex = 0;
        }
        return carPartService.submitCarPart(carPart, bindingResult, images, mainImageIndex, model);
    }

    @GetMapping("/")
    public String getShowCars(Model model) {
        List<CarPart> carParts = (List<CarPart>) carPartRepository.findAll();

        List<CarPartWithBase64Images> carPartsWithImages = carParts.stream().map(carPart -> {
            List<String> base64Images = carPart.getImages().stream()
                    .map(Base64::encodeBase64String)
                    .collect(Collectors.toList());
            return new CarPartWithBase64Images(carPart, base64Images);
        }).collect(Collectors.toList());

        model.addAttribute("carPartsWithImages", carPartsWithImages);
        return "car-part/show";
    }

    @GetMapping("/show/{Id}")
    public String getShowCarPart(@PathVariable("Id") Long id, Model model) {
        CarPart carPart = carPartRepository.findById(id).get();
        List<String> base64Images = carPart.getImages().stream()
                .map(Base64::encodeBase64String)
                .collect(Collectors.toList());
        CarPartWithBase64Images carPartWithImages = new CarPartWithBase64Images(carPart, base64Images);
        model.addAttribute("carPart", carPartWithImages);
        return "car-part/showSingle";
    }

    @PostMapping("/delete/{id}")
    public String getSubmitDeleteCarPart(@PathVariable("id") Long id) {
        return carPartService.submitDeleteCarPart(id);
    }
}