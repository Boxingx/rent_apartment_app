package com.example.rent_module.mapper;

import com.example.rent_module.model.dto.AddressDto;
import com.example.rent_module.model.dto.ApartmentDto;
import com.example.rent_module.model.entity.AddressEntity;
import com.example.rent_module.model.entity.ApartmentEntity;
import org.mapstruct.Mapper;


/**Интерфейс для маппинга, работает с плагином MapStruct*/
@Mapper(componentModel = "spring")
public interface ApplicationMapper {

//    @Mapping(target = "cityInfo", source = "city")
//    @Mapping(target = "cityInfo", ignore = true)
//    @Mapping(target = "city", ignore = true)
    AddressDto addressEntityToAddressDto(AddressEntity addressEntity);

//    @AfterMapping
//    default void prepareCityField(AddressEntity addressEntity, @MappingTarget AddressDto addressDto) {
//        addressDto.setCity("Значение города изменено после обработки");
//    }

    ApartmentDto apartmentEntityToApartmentDto(ApartmentEntity apartmentEntity);

    ApartmentEntity apartmentDtoToApartmentEntity(ApartmentDto apartmentDto);

    AddressEntity addressDtoToAddressEntity(AddressDto addressDto);

}
