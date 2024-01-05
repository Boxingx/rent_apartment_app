package com.example.rent_module.service;


import com.example.rent_module.application_exceptions.ApartmentException;
import com.example.rent_module.application_exceptions.BookApartmentException;
import com.example.rent_module.application_exceptions.PhotoException;
import com.example.rent_module.integration.GeoCoderRestTemplateManager;
import com.example.rent_module.integration.ProductRestTemplateManager;
import com.example.rent_module.integration.YandexWeatherRestTemplateManager;
import com.example.rent_module.kafka.KafkaProducer;
import com.example.rent_module.mapper.ApplicationMapper;
import com.example.rent_module.model.dto.*;
import com.example.rent_module.model.dto.geocoder_city_to_location.Geometry;
import com.example.rent_module.model.dto.yandex_weather_integration.YandexWeatherResponse;
import com.example.rent_module.model.entity.*;
import com.example.rent_module.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.rent_module.config.CityTranslationStatic.*;
import static com.example.rent_module.constant_project.ConstantProject.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.FALSE;


@Service
@RequiredArgsConstructor
public class RentApartmentServiceImpl implements RentApartmentService {

    private final AddressRepository addressRepository;

    private final ApartmentRepository apartmentRepository;

    private final EntityManager entityManager;

    private final ApplicationMapper applicationMapper;

    private final GeoCoderRestTemplateManager restTemplateManager;

    private final ClientRepository clientRepository;

    private final BookingHistoryRepository bookingHistoryRepository;

    private final ProductRestTemplateManager productRestTemplateManager;

    private final YandexWeatherRestTemplateManager yandexWeatherRestTemplateManager;

    private final PromoCodeRepository promoCodeRepository;

    private final PhotoRepository photoRepository;

    private final RatingRepository ratingRepository;

    private final JdbcTemplate jdbcTemplate;

    private final KafkaProducer kafkaProducer;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    //QUERY WITH CRITERIA API
    private List<AddressEntity> getAddressInformationByCriteria(String cityName) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AddressEntity> query = criteriaBuilder.createQuery(AddressEntity.class);
        Root<AddressEntity> root = query.from(AddressEntity.class);

        query.select(root).where(criteriaBuilder.equal(root.get("city"), cityName));
        List<AddressEntity> resultList = entityManager.createQuery(query).getResultList();

        return resultList;
    }


    /**
     * Метод выгружает квартиры по определенному городу
     */
    @Override
    public GetAddressInfoResponseDto getAddressByCity(String cityName) {
        List<ApartmentEntity> apartmentsList = apartmentRepository.findApartmentEntitiesByAddressEntity_City(cityName);

        if (apartmentsList.isEmpty()) {
            GetAddressInfoResponseDto getAddressInfoResponseDto = new GetAddressInfoResponseDto(null);
            getAddressInfoResponseDto.setExceptionCode(ERROR_CODE_250);
            getAddressInfoResponseDto.setExceptionMessage(NOT_HAVE_APARTMENT_IN_THIS_CITY);

            return getAddressInfoResponseDto;
        }
        return new GetAddressInfoResponseDto(apartmentEntityToDto(apartmentsList));
    }


    public GetAddressInfoResponseDto getApartmentByCityAndRoomsCount(String city, String roomsCount) {
        List<ApartmentEntity> apartmentEntityList = apartmentRepository.findApartmentEntitiesByRoomsCountAndAddressEntity_City(roomsCount, city);
        if (apartmentEntityList.isEmpty()) {
            GetAddressInfoResponseDto getAddressInfoResponseDto = new GetAddressInfoResponseDto(null);
            getAddressInfoResponseDto.setExceptionCode(ERROR_CODE_255);
            getAddressInfoResponseDto.setExceptionMessage(NO_APARTMENT_IN_CITY_AND + city + ROOMS_COUNT + roomsCount);

            return getAddressInfoResponseDto;
        }
        return new GetAddressInfoResponseDto(apartmentEntityToDto(apartmentEntityList));
    }


    @Override
    public GetAddressInfoResponseDto getApartmentByCityAndPriceAndRoomsCount(String city, String priceTo, String roomsCount) {
        List<ApartmentEntity> apartmentEntitiesList = apartmentRepository.findApartmentEntitiesByRoomsCountAndPriceToAndAddressEntity_City(roomsCount, priceTo, city);
        if (apartmentEntitiesList.isEmpty()) {
            GetAddressInfoResponseDto getAddressInfoResponseDto = new GetAddressInfoResponseDto(null);
            getAddressInfoResponseDto.setExceptionCode(ERROR_CODE_255);
            getAddressInfoResponseDto.setExceptionMessage(NO_APARTMENT_IN_CITY_AND + city + WITH_PRICE + priceTo + AND_ROOMS_COUNT + roomsCount);

            return getAddressInfoResponseDto;
        }
        return new GetAddressInfoResponseDto(apartmentEntityToDto(apartmentEntitiesList));
    }


    @Override
    public GetAddressInfoResponseDto findApartmentEntitiesByAverageRatingAndAddressEntity_City(String cityName, String averageRating) {
        GetAddressInfoResponseDto getAddressInfoResponseDto = new GetAddressInfoResponseDto(null);
        List<ApartmentEntity> apartmentList = apartmentRepository.findApartmentEntitiesByAddressEntity_City(cityName);

        List<ApartmentEntity> apartmentListAfterChange = new ArrayList<>();

        if (apartmentList.isEmpty()) {
            getAddressInfoResponseDto.setExceptionCode(ERROR_CODE_255);
            getAddressInfoResponseDto.setExceptionMessage(NO_APARTMENT_WITH_CITY_FILTER);

            return getAddressInfoResponseDto;
        }

        for (ApartmentEntity a : apartmentList) {
            Long rating = apartmentRepository.getAvgRatingByCityId(a.getId());
            if (rating != null) {
                a.setAverageRating(apartmentRepository.getAvgRatingByCityId(a.getId()).toString());
                apartmentRepository.save(a);
                apartmentListAfterChange.add(a);
            }
        }

        List<ApartmentEntity> result = apartmentListAfterChange.stream().filter(o -> Integer.parseInt(o.getAverageRating()) <= Integer.parseInt(averageRating)).collect(Collectors.toList());

        if (result.isEmpty()) {
            getAddressInfoResponseDto.setExceptionCode(ERROR_CODE_255);
            getAddressInfoResponseDto.setExceptionMessage(NO_APARTMENT_WITH_RATING_FILTER);

            return getAddressInfoResponseDto;
        }
        getAddressInfoResponseDto.setApartmentDtoList(apartmentEntityToDto(result));

        return getAddressInfoResponseDto;
    }


    /**
     * Метод принимает объект с координатами, в случае если объект пустой устанавливается код ошибки и сообщение об ошибке, а если
     * не пустой то получаем JSON из которого вытаскивается нужное поле city(на англ языке) парсится на русский язык с помощью метода getCityInRussianLanguage
     * класса CityTranslationStatic и происходит поиск апартаметов по городу который мы в итоге получили
     */
    @Override
    public GetAddressInfoResponseDto getApartmentsByLocation(PersonsLocation location) {
        GetAddressInfoResponseDto getAddressInfoResponseDto = new GetAddressInfoResponseDto(null);
        if (location == null) {
            getAddressInfoResponseDto.setExceptionCode(ERROR_CODE_255);
            getAddressInfoResponseDto.setExceptionMessage(LOCATION_UNKNOWN);

            return getAddressInfoResponseDto;
        }

        YandexWeatherResponse weather = yandexWeatherRestTemplateManager.getWeatherByLocation(location);
        String infoByLocation = restTemplateManager.getInfoByLocation(location); //ТУТ СОДЕРЖИТСЯ ГОРОД НА АНГЛИЙСКОМ

        GetAddressInfoResponseDto addressByCity = getAddressByCity(getCityInRussianLanguage(infoByLocation));

        addressByCity.setTemp(weather.getFactWeather().getTemp());
        addressByCity.setCondition(weather.getFactWeather().getCondition());

        return addressByCity;
    }


    @Override
    public ApartmentWithMessageDto getApartmentById(Long id) {
        ApartmentEntity apartmentEntity = apartmentRepository.findById(id).orElseThrow(() -> new ApartmentException(APARTMENT_ERROR));
        if (apartmentEntity.getStatus().equals("false")) {
            return new ApartmentWithMessageDto(APARTMENT_STATUS_FALSE, applicationMapper.apartmentEntityToApartmentDto(apartmentEntity));
        }
        return new ApartmentWithMessageDto(APARTMENT_STATUS_TRUE, applicationMapper.apartmentEntityToApartmentDto(apartmentEntity));
    }


    @Override
    public ApartmentWithMessageDto registrationNewApartment(ApartmentDto apartmentDto, String token) {
        ClientApplicationEntity client = clientRepository.findClientApplicationEntitiesByUserToken(token);
        if(!client.isCommercialStatus()) {
            throw new ApartmentException("Регистрация новых квартир доступна только аккаунтам с коммерческим статусом.");
        }
        ApartmentEntity apartmentEntity = applicationMapper.apartmentDtoToApartmentEntity(apartmentDto);
        apartmentEntity.setRegistrationDate(LocalDateTime.now().format(formatter));
        apartmentEntity.setClientApplicationEntity(client);
        apartmentRepository.save(apartmentEntity);
        AddressEntity addressEntity = applicationMapper.addressDtoToAddressEntity(apartmentDto.getAddressDto());
        addressEntity.setApartmentEntity(apartmentEntity);
        addressRepository.save(addressEntity);

        return new ApartmentWithMessageDto(APARTMENT_SAVED, apartmentDto);

    }


    @Override
    public ApartmentWithMessageDto bookApartment(Long id, LocalDate start, LocalDate end, String promoCode, String userToken) {
        ApartmentEntity apartmentEntity = apartmentRepository.getApartmentEntityById(id);

        if (apartmentEntity.getStatus().equals("false")) {
            return new ApartmentWithMessageDto("Квартира занята", null);
        }

        apartmentEntity.setStatus("false");
        apartmentRepository.save(apartmentEntity);

        ClientApplicationEntity client = clientRepository.findClientApplicationEntitiesByUserToken(userToken);

        clientRepository.save(incrementBookingCount(client));

        BookingHistoryEntity bookingHistoryEntity = prepareBookingHistory(apartmentEntity, client, start, end);

        Double finalPayment = 0.0;
        Long paymentWithoutDiscount = Long.parseLong(apartmentEntity.getPrice()) * ChronoUnit.DAYS.between(start, end);
        Double paymentWithoutDiscountDouble = (double) paymentWithoutDiscount;
        bookingHistoryEntity.setFinalPayment(paymentWithoutDiscountDouble);

        if (nonNull(promoCode)) {
            bookingHistoryEntity.setPromoCode(promoCode);
            PromoCodeEntity promoCodeEntityByPromoCode = promoCodeRepository.getPromoCodeEntityByPromoCode(promoCode);
            Long discountNumber = promoCodeEntityByPromoCode.getDiscount();
            Double discountPercent = (double) discountNumber / 100.0;
            finalPayment = paymentWithoutDiscountDouble - (paymentWithoutDiscountDouble * discountPercent);
            bookingHistoryEntity.setFinalPayment(finalPayment);
        }

        bookingHistoryRepository.save(bookingHistoryEntity);

        String cityRu = bookingHistoryEntity.getApartmentEntity().getAddressEntity().getCity();
        String englishCity = getEnglishCityByRuCity(cityRu);
        String englishCountry = getEnglishCountryByEnglishCity(englishCity);

        Geometry locationByCity = restTemplateManager.getLocationByCity(englishCity, englishCountry);

        String weatherByLocation = yandexWeatherRestTemplateManager.getWeatherByCoordinates(locationByCity.getLatitude(), locationByCity.getLongitude());

        try {
            finalPayment = productRestTemplateManager.prepareProduct(bookingHistoryEntity.getId(), weatherByLocation);
            return new ApartmentWithMessageDto(prepareBookingResponse(start, end, true, finalPayment), applicationMapper.apartmentEntityToApartmentDto(apartmentEntity));

        } catch (Exception e) {
            /**
             *  Тут пишем в топик, и сообщаем пользователю, что серсис расчета скидки в данный момент недоступен, но как только он
             *  возобновит свою работу пользователь получит сообщение на почту с уже расчитаной скидкой и ссылкой на оплату.
             *  */
            kafkaProducer.sendMessageToTopic(bookingHistoryEntity.getId().toString());
            throw new BookApartmentException(prepareBookingResponse(start, end, false, null), applicationMapper.apartmentEntityToApartmentDto(apartmentEntity));
        }
    }


    public ResponseEntity<byte[]> getImage(Long id) {
        PhotoEntity photoEntity = photoRepository.getPhotoEntityById(id);
        byte[] photo = photoEntity.getImageData();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(photo.length);

        ResponseEntity response = new ResponseEntity<>(photo, headers, HttpStatus.OK);

        return response;
    }


    public String addPhoto(Long id, MultipartFile multipartFile) {
        if (!checkPhotoSize(multipartFile)) {
            throw new PhotoException(PHOTO_SIZE_ERROR);
        }
        List<PhotoEntity> entityList = photoRepository.findPhotoEntitiesByApartmentEntity(id);
        int count = entityList.size();
        checkPhotoCount(count);
        ApartmentEntity apartmentEntityById = apartmentRepository.getApartmentEntityById(id);
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setApartmentEntity(apartmentEntityById);
        try {
            byte[] bytes = multipartFile.getBytes();
            photoEntity.setImageData(bytes);
            photoRepository.save(photoEntity);

            return PHOTO_ADDED;
        } catch (IOException e) {
            throw new PhotoException(SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public GetAddressInfoResponseDto getApartmentByCityAndPriceFromTo(String cityName, String priceFrom, String priceTo) {
        List<ApartmentEntity> apartmentEntities = apartmentRepository.getApartmentEntitiesByAddressEntity_CityAndPriceGreaterThanEqualAndPriceIsLessThanEqual(cityName, priceFrom, priceTo);
        if (apartmentEntities.isEmpty()) {
            throw new ApartmentException(APARTMENT_ERROR);
        }
        return new GetAddressInfoResponseDto(apartmentEntityToDto(apartmentEntities));
    }

    @Override
    public String addReviewForApartment(Integer rating, String review, Long apartmentId) {
        RatingEntity ratingEntity = new RatingEntity();
        ratingEntity.setRating(rating);
        ratingEntity.setReview(review);
        ratingEntity.setApartmentEntity(apartmentRepository.getApartmentEntityById(apartmentId));
        ratingRepository.save(ratingEntity);
        return "спасибо за отзыв";
    }


    /**
     * Метод для подготовки сообщения
     */
    private String prepareBookingResponse(LocalDate start, LocalDate end, boolean discountFlag, Double finalPayment) {
        String discountType;
        if (discountFlag) {
            discountType = YOUR_PAYMENT + finalPayment;
        } else {
            discountType = WITHOUT_DISCOUNT;
        }
        return APARTMENT_BOOKED + start + SPACE + LocalTime.of(12, 0) + TO + end + SPACE + LocalTime.of(12, 0) + discountType + INFO_SEND_TO_MAIL;
    }


    private BookingHistoryEntity prepareBookingHistory(ApartmentEntity apartmentEntity, ClientApplicationEntity client, LocalDate start, LocalDate end) {
        BookingHistoryEntity bookingHistoryEntity = new BookingHistoryEntity();
        bookingHistoryEntity.setApartmentEntity(apartmentEntity);
        bookingHistoryEntity.setClientApplicationEntity(client);
        bookingHistoryEntity.setStartDate(start);
        bookingHistoryEntity.setEndDate(end);
        bookingHistoryEntity.setDaysCount(ChronoUnit.DAYS.between(start, end));
        bookingHistoryEntity.setSchedulerProcessing(FALSE);
        bookingHistoryEntity.setSchedulerMailReview(FALSE);

        return bookingHistoryEntity;
    }


    private ClientApplicationEntity incrementBookingCount(ClientApplicationEntity client) {
        if (isNull(client.getBookingCount())) {
            client.setBookingCount(1);
        } else {
            client.setBookingCount(client.getBookingCount() + 1);
        }
        return client;
    }


    private List<ApartmentDto> apartmentEntityToDto(List<ApartmentEntity> apartmentEntityList) {
        List<ApartmentDto> resultList = new ArrayList<>();

        for (ApartmentEntity e : apartmentEntityList) {

            AddressDto addressDto = applicationMapper.addressEntityToAddressDto(e.getAddressEntity());
            ApartmentDto apartmentDto = applicationMapper.apartmentEntityToApartmentDto(e);
            apartmentDto.setAddressDto(addressDto);

            resultList.add(apartmentDto);
        }
        return resultList;
    }


    private boolean checkPhotoCount(int count) {
        if (count >= 5) {
            throw new PhotoException(PHOTO_COUNT_ERROR);
        }
        return true;
    }


    private boolean checkPhotoSize(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return false;
        }
        long maxSize = 20000000l;

        return multipartFile.getSize() < maxSize;
    }

}
