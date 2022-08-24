package com.epam.esm.dao.tag;

import com.epam.esm.dao.BaseDAO;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TagDAO extends BaseDAO<Tag> {

    String QUERY_DELETE_TAG = "delete from tag where id = ?";
    String QUERY_INSERT_TAG = "insert into tag (id, name) values(?, ?)";
    String QUERY_GET_TAG = "select * from tag where id = ?";
    String QUERY_GET_BY_NAME = "select * from tag where name = ?";
    String QUERY_GET_ALL = "select * from tag";
    String QUERY_CREATE_CONNECTION = "insert into gift_certificate_tag (tag_id, gift_certificate_id) values (?, ?);";
    String QUERY_GET_TAGS = "select * from tag t where t.id in (select tag_id from gift_certificate_tag where gift_certificate_id = ?)";


    void createWithGiftCertificate(UUID certificateId, List<Tag> tags);

    List<Tag> getTags(UUID certificateId);

    Tag getByName(String name);
}
