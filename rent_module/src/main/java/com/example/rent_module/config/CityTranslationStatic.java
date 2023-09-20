package com.example.rent_module.config;

import java.util.HashMap;
import java.util.Map;

import static com.example.rent_module.constant_project.ConstantProject.NOT_HAVE_APARTMENT_IN_THIS_CITY;


/**Класс со статической мапой, статическим блоком который инициализирует мапу и со статическим методом который
 * принимает город на английском языке(как ключ) и отдает значение по заданному ключу в виде города, но уже на русском языке
 * */
public class CityTranslationStatic {

    private static final Map<String, String> cityTranslationMap = new HashMap<>();
    private static final Map<String, String> cityCountryMap = new HashMap<>();

    static {
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
        cityTranslationMap.put("Ashdod", "Ashdod");
    }

    static {
        cityCountryMap.put("Omsk" , "Russia");
        cityCountryMap.put("Moscow", "Russia");
    }

    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;  // Если значение не найдено
    }

    public static String getEnglishCityByRuCity(String cityRu) {
        return getKeyByValue(cityTranslationMap, cityRu);
    }

    public static String getEnglishCountryByEnglishCity(String cityEng) {
        return cityCountryMap.get(cityEng);
    }


    public static String getCityInRussianLanguage(String englishCity) {
        return cityTranslationMap.getOrDefault(englishCity,NOT_HAVE_APARTMENT_IN_THIS_CITY);
    }
}
