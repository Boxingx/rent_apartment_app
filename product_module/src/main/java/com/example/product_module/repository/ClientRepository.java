package com.example.product_module.repository;



import com.example.product_module.entity.ClientApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientApplicationEntity, Long> {

    List<ClientApplicationEntity> getClientApplicationEntitiesByNickName (String nickName);

    List<ClientApplicationEntity> getClientApplicationEntitiesByLoginMail (String loginMail);

}
