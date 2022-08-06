package com.epam.esm.DAO.gift_certificate;

import com.epam.esm.DAO.BaseDAO;
import com.epam.esm.entity.gift_certificate.GiftCertificate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GiftCertificateDAO extends BaseDAO<GiftCertificate> {

    GiftCertificate update(GiftCertificate updateCertificate, UUID certificateId);

    List<GiftCertificate> searchAndGetByTagName(
            String searchWord, String tagName, boolean doNameSort, boolean doDateSort, boolean isDescending);

}
