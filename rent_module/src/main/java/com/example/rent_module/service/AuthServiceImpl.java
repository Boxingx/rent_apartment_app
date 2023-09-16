package com.example.rent_module.service;


import com.example.rent_module.model.dto.AuthDto;
import com.example.rent_module.model.dto.UserSession;
import com.example.rent_module.model.entity.ClientApplicationEntity;
import com.example.rent_module.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.rent_module.constant_project.ConstantProject.*;
import static java.util.Objects.isNull;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ClientRepository clientRepository;

    private final UserSession userSession;

    @Override
    public String registration(ClientApplicationEntity clientApplicationEntity) {
        ClientApplicationEntity resultNickName = clientRepository.getClientApplicationEntitiesByNickName(clientApplicationEntity.getNickName());
        if (!isNull(resultNickName)) {
            return NICKNAME_IS_TAKEN;
        }
        List<ClientApplicationEntity> resultMail = clientRepository.getClientApplicationEntitiesByLoginMail(clientApplicationEntity.getLoginMail());
        if(!resultMail.isEmpty()) {
            return LOGIN_IS_TAKEN;
        }
        clientRepository.save(clientApplicationEntity);
        return REGISTRATION_SUCCESSFUL;
    }

    /**Метод авторизации*/
    @Override
    public String auth(AuthDto authDto) {
        List<ClientApplicationEntity> result = clientRepository.getClientApplicationEntitiesByLoginMail(authDto.getLogin());
        if(result.isEmpty()) {
            return USER_NOT_EXIST;
        }
        if(result.get(0).getPassword().equals(authDto.getPassword())) {
            userSession.setNickName(result.get(0).getNickName());
            userSession.setLogin(result.get(0).getLoginMail());
            return WELCOME;
        }
        return WRONG_PASSWORD;
    }
}
