package com.epam.esm.dao.gift_certificate;

import com.epam.esm.dao.BaseDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.enums.SortEnum;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GiftCertificateDAO extends BaseDAO<GiftCertificate> {

    String QUERY_INSERT_CERTIFICATE = "insert into gift_certificate(id, name, description, price, duration, create_date, last_update_date) values(?, ?, ?, ?, ?, ?, ?)";
    String QUERY_GET_CERTIFICATE = "select * from gift_certificate where id = ?";
    String QUERY_GET_ALL = "select * from gift_certificate";
    String QUERY_DELETE_CERTIFICATE = "delete from gift_certificate where id = ?";
    String QUERY_DELETE_CONNECTIONS = "delete from gift_certificate_tag where gift_certificate_id = ?";
    String QUERY_UPDATE_CERTIFICATE = "update gift_certificate set name = ?, description = ?, price = ?, duration = ?, last_update_date = ? where id = ?";


    GiftCertificate update(GiftCertificate updateCertificate, UUID certificateId);

    List<GiftCertificate> searchAndGetByNameOrTagName(
            String searchWord, String tagName, SortEnum sortEnum);

}
