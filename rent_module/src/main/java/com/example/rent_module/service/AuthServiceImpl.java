package com.example.rent_module.service;


import com.example.rent_module.application_exceptions.RentAuthException;
import com.example.rent_module.application_exceptions.RentRegistrationException;
import com.example.rent_module.config.AuthToken;
import com.example.rent_module.model.dto.ApartmentWithMessageDto;
import com.example.rent_module.model.dto.AuthDto;
import com.example.rent_module.model.entity.ClientApplicationEntity;
import com.example.rent_module.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.rent_module.base64.ApplicationEncoderDecoder.decode;
import static com.example.rent_module.base64.ApplicationEncoderDecoder.encode;
import static com.example.rent_module.constant_project.ConstantProject.*;
import static com.example.rent_module.service.GenerateTokenImpl.generateToken;
import static java.util.Objects.isNull;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ClientRepository clientRepository;

//    private final UserSession userSession;

    /**
     * Метод регистрации, проверки занят ли никнейм или почтовый адрес, если нет то кодируется пароль и сохраняется в БД вся сущность
     */
    @Override
    public AuthToken registration(ClientApplicationEntity clientApplicationEntity) {
        if(isNull(clientApplicationEntity)) {
            throw new RentAuthException("Введите данные");
        }
        ClientApplicationEntity resultNickName = clientRepository.getClientApplicationEntityByNickName(clientApplicationEntity.getNickName());
        if (!isNull(resultNickName)) {
            throw new RentRegistrationException(NICKNAME_IS_TAKEN);
        }
        ClientApplicationEntity resultMail = clientRepository.getClientApplicationEntitiesByLoginMail(clientApplicationEntity.getLoginMail());
        if (!isNull(resultMail)) {
            throw new RentRegistrationException(LOGIN_IS_TAKEN);
        }
        String encode = encode(clientApplicationEntity.getPassword());
        clientApplicationEntity.setPassword(encode);
        clientApplicationEntity.setUserToken(generateToken());
        clientRepository.save(clientApplicationEntity);
        return new AuthToken(REGISTRATION_SUCCESSFUL, clientApplicationEntity.getUserToken());
    }

    /**
     * Метод авторизации, принимает DTO с логином и паролем,идет проверка на null и если всё хорошо пароль декодируется и сверяется с тем что в БД,
     * в случае если пароли совпали пользователь будет авторизован
     */
    @Override
    public AuthToken auth(AuthDto authDto) {
        ClientApplicationEntity result = clientRepository.getClientApplicationEntitiesByLoginMail(authDto.getLogin());
        if (isNull(result)) {
            throw new RentAuthException(USER_NOT_EXIST);
        }
        if (!decode(result.getPassword()).equals(authDto.getPassword())) {
            throw new RentAuthException(WRONG_PASSWORD);
        }
        if (isNull(result.getUserToken())) {
            result.setUserToken(generateToken());
            clientRepository.save(result);
            return new AuthToken(WELCOME, result.getUserToken());
        } else return new AuthToken(WELCOME, result.getUserToken());
    }

    @Override
    public boolean checkValidToken(String token) {
        ClientApplicationEntity client = clientRepository.findClientApplicationEntitiesByUserToken(token);
        if (isNull(client)) {
            return false;
        }
        return true;
    }
}
