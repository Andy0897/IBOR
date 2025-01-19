package com.example.IBOR.CarPart;

import com.example.IBOR.Car.Car;
import com.example.IBOR.Car.CarRepository;
import com.example.IBOR.CarBrand.BrandRepository;
import com.example.IBOR.CarModel.ModelRepository;
import com.example.IBOR.Cart.CartRepository;
import com.example.IBOR.Order.OrderRepository;
import com.example.IBOR.OrderItem.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarPartService {
    private CarPartRepository carPartRepository;
    private BrandRepository brandRepository;
    private ModelRepository modelRepository;

    public CarPartService(CarPartRepository carPartRepository, BrandRepository brandRepository, ModelRepository modelRepository) {
        this.carPartRepository = carPartRepository;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
    }

    @Transactional
    public String submitCarPart(
            CarPart carPart,
            BindingResult bindingResult,
            MultipartFile[] images,
            Integer mainImageIndex,
            Model model) {

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
            model.addAttribute("carPart", carPart);
            model.addAttribute("hasUploadError", true);
            return "car-part/add";
        }

        if (carPart.getBrand() == null || carPart.getModel() == null || nullImages ||
                bindingResult.hasFieldErrors("title") ||
                bindingResult.hasFieldErrors("description") ||
                bindingResult.hasFieldErrors("price") ||
                carPart.getQuantity() < 1 ||
                carPart.getCategory() == null || carPart.getCategory().equals("")) {

            model.addAttribute("carPart", carPart);
            model.addAttribute("brands", brandRepository.findAll());
            model.addAttribute("models", modelRepository.findAll());
            model.addAttribute("areImagesSelected", !nullImages);
            model.addAttribute("isBrandSelected", carPart.getBrand() != null);
            model.addAttribute("isModelSelected", carPart.getModel() != null);
            model.addAttribute("isCategorySelected", !carPart.getCategory().isEmpty());
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
            model.addAttribute("invalidQuantity", carPart.getQuantity() < 1);
            return "car-part/add";
        }

        carPart.setImages(imageList);
        carPart.setMainImageIndex(mainImageIndex);

        carPartRepository.save(carPart);

        return "redirect:/car-parts/";
    }

}
