package com.example.IBOR.CarPart;

import com.example.IBOR.CarBrand.BrandRepository;
import com.example.IBOR.ImageEncoder;
import com.example.IBOR.OrderItem.OrderItem;
import com.example.IBOR.OrderItem.OrderItemDTO;
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

        if (mainImageIndex == null || mainImageIndex < 0 || mainImageIndex >= images.length) {
            mainImageIndex = 0;
        }
        return carPartService.submitCarPart(carPart, bindingResult, images, mainImageIndex, model);
    }

    @GetMapping("/")
    public String getShowCarParts(Model model) {
        List<CarPart> carParts = (List<CarPart>) carPartRepository.findAll();
        model.addAttribute("carParts", carParts);
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
        model.addAttribute("encoder", new ImageEncoder());
        return "car-part/show";
    }

    @GetMapping("/filter")
    public String filterCarParts(
            @RequestParam(value = "brand", required = false) Long brandId,
            @RequestParam(value = "model", required = false) Long modelId,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            Model model) {
        List<CarPart> filteredCarParts = ((List<CarPart>) carPartRepository.findAll()).stream()
                .filter(carPart -> (brandId == null || carPart.getBrand().getId().equals(brandId)))
                .filter(carPart -> (modelId == null || carPart.getModel().getId().equals(modelId)))
                .filter(carPart -> (category.isEmpty() || carPart.getCategory().equalsIgnoreCase(category)))
                .filter(carPart -> (minPrice == null || carPart.getPrice() >= minPrice))
                .filter(carPart -> (maxPrice == null || carPart.getPrice() <= maxPrice))
                .collect(Collectors.toList());
        model.addAttribute("carParts", filteredCarParts);
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
        model.addAttribute("encoder", new ImageEncoder());
        return "car-part/show";
    }


    @GetMapping("/show/{Id}")
    public String getShowCarPart(@PathVariable("Id") Long id, Model model) {
        CarPart carPart = carPartRepository.findById(id).get();
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setQuantity(1);
        model.addAttribute("carPart", carPart);
        model.addAttribute("encoder", new ImageEncoder());
        model.addAttribute("orderItem", orderItemDTO);
        model.addAttribute("invalidQuantity", false);
        return "car-part/showSingle";
    }

    @GetMapping("/update-quantity/{id}")
    public String getUpdateQuantity(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("invalidQuantity", false);
        return "car-part/updateQuantity";
    }

    @PostMapping("/submit-update-quantity/{id}")
    public String getSubmitUpdateQuantity(@PathVariable("id") Long id, @RequestParam("quantity") int quantity, Model model) {
        return carPartService.submitUpdateQuantity(id, quantity, model);
    }
}