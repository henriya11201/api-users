package com.nisum.users.repository;

import com.nisum.users.domain.Phone;
import com.nisum.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, UUID> {

    void deleteAllByUser(User user);

}
