package com.obrasmart.identity.repository;

import com.obrasmart.identity.entity.User;
import com.obrasmart.identity.entity.UserDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserDocumentRepository extends JpaRepository<UserDocument, UUID> {
    List<UserDocument> findByUser(User user);
}
