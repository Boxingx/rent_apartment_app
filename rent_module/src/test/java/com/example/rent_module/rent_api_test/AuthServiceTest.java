package com.example.rent_module.rent_api_test;

import com.example.rent_module.application_exceptions.RentAuthException;
import com.example.rent_module.application_exceptions.RentRegistrationException;
import com.example.rent_module.config.AuthToken;
import com.example.rent_module.model.dto.RentApartmentException;
import com.example.rent_module.model.entity.ClientApplicationEntity;
import com.example.rent_module.service.AuthService;
import jakarta.persistence.Column;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.example.rent_module.constant_project.ConstantProject.REGISTRATION_SUCCESSFUL;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class AuthServiceTest {

    @Autowired
    private AuthService authService;



    @Test
    public void registrationTest() {
        ClientApplicationEntity clientApplicationEntity = new ClientApplicationEntity();
        clientApplicationEntity.setNickName("floyd");
        clientApplicationEntity.setLoginMail("floyd@mail.ru");
        clientApplicationEntity.setPassword("12345");
        clientApplicationEntity.setCommercialStatus(false);
        clientApplicationEntity.setParentCity("Москва");

        AuthToken registration = authService.registration(clientApplicationEntity);

        Assertions.assertNotNull(registration.getToken());
        Assertions.assertEquals(REGISTRATION_SUCCESSFUL, registration.getMessage());
    }



    //todo не работает не пойму как сделать так что я жду наш Exception , в итоге вроде сделал через аннотацию @Test(expected = RentAuthException.class)
    @Test(expected = RentAuthException.class)
    public void registrationNegativeTest() {
        ClientApplicationEntity clientApplicationEntity = null;

        try {
            authService.registration(clientApplicationEntity);
        } catch (RentRegistrationException ex) {
            Assertions.assertEquals("Введите данные", ex.getMessage());
        }

//        Assertions.assertNotNull(registration);
//        Assertions.assertEquals("Введите данные", registration.getMessage());



//         Используйте Assertions.assertThrows для проверки исключения
//        RentAuthException exception = Assertions.assertThrows(RentAuthException.class, () -> {
//            authService.registration(clientApplicationEntity);
//        });

//         Проверьте сообщение об ошибке
//        Assertions.assertEquals("Введите данные", exception.getMessage());
    }
}
