package com.example.rent_module.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.example.rent_module.constant_project.ConstantProject.NOT_HAVE_APARTMENT_IN_THIS_CITY;


/**Класс со статическими мапами, статическими блоками которые инициализируют эти мапы и со статическими методоми
 * для подбора города/страны на нужном языке(русский, английский)
 * */
public class CityTranslationStatic {

    private final static Logger logger = LoggerFactory.getLogger(CityTranslationStatic.class);
    private static final Map<String, String> cityTranslationMap = new HashMap<>();
    private static final Map<String, String> cityCountryMap = new HashMap<>();

    static {

        logger.info("start init map");
        cityTranslationMap.put("Moscow", "Москва");
        cityTranslationMap.put("Saint Petersburg", "Санкт-Петербург");
        cityTranslationMap.put("Omsk", "Омск");
        cityTranslationMap.put("Tyumen", "Тюмень");
        cityTranslationMap.put("Novosibirsk", "Новосибирск");
        cityTranslationMap.put("Krasnoyarsk", "Красноярск");
        cityTranslationMap.put("Krasnodar", "Краснодар");
        cityTranslationMap.put("Makhachkala", "Махачкала");
        cityTranslationMap.put("Penza", "Пенза");
        cityTranslationMap.put("Tver", "Тверь");
        logger.info("end init map");
    }

    static {
        cityCountryMap.put("Moscow", "Russia");
        cityCountryMap.put("Saint Petersburg", "Russia");
        cityCountryMap.put("Omsk" , "Russia");
        cityCountryMap.put("Tyumen", "Russia");
        cityCountryMap.put("Novosibirsk", "Russia");
        cityCountryMap.put("Krasnoyarsk", "Russia");
        cityCountryMap.put("Krasnodar", "Russia");
        cityCountryMap.put("Makhachkala", "Russia");
        cityCountryMap.put("Penza", "Russia");
        cityCountryMap.put("Tver", "Russia");

    }

    /**Приватный метод принимает мапу и значение и ищет ключ по переданному значению в переданной мапе */
    private static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;  // Если значение не найдено
    }

    /**Метод принимает город на русском языке и возвращает на английском, проходится по мапе cityTranslationMap */
    public static String getEnglishCityByRuCity(String cityRu) {
        return getKeyByValue(cityTranslationMap, cityRu);
    }

    /**Метод принимает город на английском языке и возвращает страну в которой находится этот город, проходится по мапе cityCountryMap */
    public static String getEnglishCountryByEnglishCity(String cityEng) {
        return cityCountryMap.get(cityEng);
    }

    /**Метод принимает город на английском языке и возвращает на русском, проходится по мапе cityTranslationMap */
    public static String getCityInRussianLanguage(String englishCity) {
        return cityTranslationMap.getOrDefault(englishCity,NOT_HAVE_APARTMENT_IN_THIS_CITY);
    }
}
