package com.epam.esm.DAO.tag;

import com.epam.esm.entity.tag.Tag;
import com.epam.esm.DAO.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TagDAO extends BaseDAO<Tag> {

    void createWithGiftCertificate(UUID certificateId, List<Tag> tags);

    List<Tag> getTags(UUID certificateId);

    Tag getByName(String name);
}
