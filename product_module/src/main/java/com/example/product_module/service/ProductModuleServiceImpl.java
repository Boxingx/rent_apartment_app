package com.example.product_module.service;


import com.example.product_module.application_exceptions.BookingHistoryException;
import com.example.product_module.email_sender.MailSender;
import com.example.product_module.entity.ApartmentEntity;
import com.example.product_module.entity.BookingHistoryEntity;
import com.example.product_module.entity.ClientApplicationEntity;
import com.example.product_module.entity.ProductEntity;
import com.example.product_module.repository.BookingHistoryRepository;
import com.example.product_module.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.product_module.constant_project.ConstantProject.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class ProductModuleServiceImpl implements ProductModuleService {

    private final BookingHistoryRepository bookingHistoryRepository;

    private final ProductRepository productRepository;

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
                    //TODO не рабочий
                    /**Скидка 10% если пользователь зарегистрировался по реферальной ссылке,хранится 3 месяца*/
                    if (p.getId() == 9l) {
                        if (clientApplicationEntity.getParentCity().equals(apartmentEntity.getAddressEntity().getCity())) {
                            saveProductToBookingHistory(p, history);
                            break;
                        }
                    }
                    //TODO не рабочий
                    /**Скидка 7% если пользователь арендует квартиру в зимнее время года*/
                    if (p.getId() == 8l && p.getStatus().equals("true")) {
                        if (clientApplicationEntity.getParentCity().equals(apartmentEntity.getAddressEntity().getCity())) {
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
                    /**Скидка 5% если пользователь ввел промокод*/
                    if (p.getId() == 7l) {
                        if (nonNull(history.getPromoCode()) && history.getPromoCode().equals(PROMO_CODE)) {
                                saveProductToBookingHistory(p, history);
                                break;
                        }
                    }
                }
            }
        }

        long paymentWithoutDiscount = history.getDaysCount() * Long.parseLong(apartmentEntity.getPrice());

        if (nonNull(history.getProductEntity().getDiscount())) {
            Long discountNumber = history.getProductEntity().getDiscount();
            Double discountPercent = (double) discountNumber / 100.0;
            Double finalPayment = (double) paymentWithoutDiscount - (paymentWithoutDiscount * discountPercent);
            history.setFinalPayment(finalPayment);
            bookingHistoryRepository.save(history);
            mailSender.sendEmail(SUBJECT , "Здравствуйте " + history.getClientApplicationEntity().getNickName() +
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
                    history.getClientApplicationEntity().getLoginMail());

            return finalPayment;
        }
        Double finalPayment = (double) paymentWithoutDiscount;
        history.setFinalPayment(finalPayment);
        return finalPayment;
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
