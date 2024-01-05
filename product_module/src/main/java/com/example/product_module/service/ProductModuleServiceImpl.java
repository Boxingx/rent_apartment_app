package com.example.product_module.service;


import com.example.product_module.application_exceptions.BookingHistoryException;
import com.example.product_module.email_sender.MailSender;
import com.example.product_module.entity.*;
import com.example.product_module.repository.BookingHistoryRepository;
import com.example.product_module.repository.PhotoRepository;
import com.example.product_module.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.product_module.constant_project.ConstantProject.SUBJECT;
import static com.example.product_module.constant_project.ConstantProject.TRUE;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class ProductModuleServiceImpl implements ProductModuleService {

    private final BookingHistoryRepository bookingHistoryRepository;

    private final ProductRepository productRepository;

    private final PhotoRepository photoRepository;

    private final MailSender mailSender;

    /**
     * Метод выбора подходящей скидки
     * */
    public Double prepareProduct(Long id, String weather) {

        BookingHistoryEntity history = bookingHistoryRepository.findById(id).orElseThrow(() -> new BookingHistoryException());

        ApartmentEntity apartmentEntity = history.getApartmentEntity();

        ClientApplicationEntity clientApplicationEntity = history.getClientApplicationEntity();

        List<ProductEntity> products = prepareDiscountCollection();

        if (!products.isEmpty()) {
            for (ProductEntity p : products) {
                if (nonNull(p.getDiscount())) {
                    /**Скидка 17% если пользователь пользовался сервисом десять или больше раз*/
                    if (p.getId() == 3l) {
                        if (clientApplicationEntity.getBookingCount() >= 10) {
                            saveProductToBookingHistory(p, history);
                            break;
                        }
                    }
                    /**Скидка 15% если пользователь еще ниразу не арендовал квартиру*/
                    if (p.getId() == 1l) {
                        if (clientApplicationEntity.getBookingCount() == 1) {
                            saveProductToBookingHistory(p, history);
                            break;
                        }
                    }
                    /**Скидка 12% если пользователь пользовался сервисом пять или больше раз*/
                    if (p.getId() == 2l) {
                        if (clientApplicationEntity.getBookingCount() >= 5) {
                            saveProductToBookingHistory(p, history);
                            break;
                        }
                    }
                    /**Скидка 10% если город пользователя и город аренды совпадают*/
                    if (p.getId() == 4l) {
                        if (clientApplicationEntity.getParentCity().equals(apartmentEntity.getAddressEntity().getCity())) {
                            saveProductToBookingHistory(p, history);
                            break;
                        }
                    }
                    /**Скидка 10% если пользователь арендовал квартиру на 10 или больше дней*/
                    if (p.getId() == 6l) {
                        if (history.getDaysCount() >= 10) {
                            saveProductToBookingHistory(p, history);
                            break;
                        }
                    }
                    /**Скидка 5% если пользователь арендовал квартиру на 5 или больше дней*/
                    if (p.getId() == 5l) {
                        if (history.getDaysCount() >= 5) {
                            saveProductToBookingHistory(p, history);
                            break;
                        }
                    }
                }
            }
        }


        if (nonNull(history.getProductEntity().getDiscount())) {
            Long discountNumber = history.getProductEntity().getDiscount();
            Double discountPercent = (double) discountNumber / 100.0;
            Double finalPayment = history.getFinalPayment() - (history.getFinalPayment() * discountPercent);
            history.setFinalPayment(finalPayment);
            bookingHistoryRepository.save(history);

            List<PhotoEntity> photoEntities = photoRepository.findPhotoEntitiesByApartmentEntity(apartmentEntity.getId());// Загрузите изображение в виде массива байт
            List<byte[]> photos = new ArrayList<>();
            for (PhotoEntity p : photoEntities) {
                photos.add(p.getImageData());
            }

            mailSender.sendEmailWithImages(SUBJECT , "Здравствуйте " + history.getClientApplicationEntity().getNickName() +
                    ". Квартира в городе " + history.getApartmentEntity().getAddressEntity().getCity() +
                    " забронирована с " + history.getStartDate() +
                    " по " + history.getEndDate() +
                    ". Ждем вас "  + history.getStartDate() +
                    " в 12:00 по адресу город " + history.getApartmentEntity().getAddressEntity().getCity() +
                    " улица " + history.getApartmentEntity().getAddressEntity().getStreet() +
                    " дом " + history.getApartmentEntity().getAddressEntity().getBuildingNumber() +
                    " квартира " + history.getApartmentEntity().getAddressEntity().getApartmentsNumber() +
                    ". Ваш платеж за " + history.getDaysCount() +
                    " дней проживания, составил " + history.getFinalPayment() +
                    " вам была предоставлена скидка " + history.getProductEntity().getProductName() +
                    " которая составляет " + history.getProductEntity().getDiscount() +
                    " процентов. Погода на момент вашего заезда " + weather,
                    history.getClientApplicationEntity().getLoginMail(), photos);

            return finalPayment;
        }
        return history.getFinalPayment();
    }

    private void saveProductToBookingHistory(ProductEntity productEntity, BookingHistoryEntity history) {
        history.setProductEntity(productEntity);
        bookingHistoryRepository.save(history);
    }

    private List<ProductEntity> prepareDiscountCollection() {

        List<ProductEntity> collect = productRepository.findAll();
        Collections.sort(collect, Comparator.comparing((ProductEntity productEntity) -> productEntity.getDiscount()).reversed());

        return collect.stream().filter(p -> p.getStatus().equals(TRUE)).collect(Collectors.toList());
    }
}
