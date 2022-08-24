package com.epam.esm.dao.gift_certificate;

import com.epam.esm.dto.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.enums.SortEnum;
import com.epam.esm.exception.NoDataFoundException;
import com.epam.esm.exception.UnknownDataBaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class GiftCertificateDAOImpl implements GiftCertificateDAO {
    private final JdbcTemplate jdbcTemplate;
    private static final String selectStatement = "select *from get_certificate(?, ?) ";
    @Override
    public GiftCertificate create(GiftCertificate certificate) {
        jdbcTemplate.update(QUERY_INSERT_CERTIFICATE,
                certificate.getId(), certificate.getName(), certificate.getDescription(), certificate.getPrice(),
                certificate.getDuration(), certificate.getCreateDate(), certificate.getLastUpdateDate());
        return certificate;
    }

    @Override
    public GiftCertificate get(UUID id) {
        try {
            return jdbcTemplate.queryForObject(
                    QUERY_GET_CERTIFICATE,
                    new GiftCertificateMapper(),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            throw new NoDataFoundException("no certificate found with id: " + id);
        }
    }

    @Override
    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(QUERY_GET_ALL, new GiftCertificateMapper());
    }

    @Override
    @Transactional
    public int delete(UUID id) {
        jdbcTemplate.update(QUERY_DELETE_CONNECTIONS, id);
        return jdbcTemplate.update(QUERY_DELETE_CERTIFICATE, id);
    }

    @Override
    public GiftCertificate update(GiftCertificate update, UUID certificateId) {
        int updateResult = jdbcTemplate.update(
                QUERY_UPDATE_CERTIFICATE,
                update.getName(),
                update.getDescription(),
                update.getPrice(),
                update.getDuration(),
                update.getLastUpdateDate(),
                certificateId);
        if(updateResult == 1)
            return update;
        throw new UnknownDataBaseException("cannot update certificate");
    }

    @Override
    public List<GiftCertificate> searchAndGetByNameOrTagName(
            String searchWord, String tagName, SortEnum sort
    ) {
        String QUERY_SEARCH_AND_GET_BY_TAG_NAME = getAllQueries(sort);
        try {
            return jdbcTemplate.query(
                    QUERY_SEARCH_AND_GET_BY_TAG_NAME,
                    new GiftCertificateMapper(),
                    searchWord,
                    tagName
            );
        } catch (EmptyResultDataAccessException e){
            throw new NoDataFoundException("no matching gift certificate found");
        }
    }

    public String getAllQueries(SortEnum sort) {

        switch (sort){
            case DATE_ASC:
                return selectStatement + "order by create_date;";

            case DATE_DESC:
                return selectStatement + "order by create_date desc;";

            case NAME_ASC:
                return selectStatement + "order by name;";

            case NAME_DESC:
                return selectStatement + "order by name desc;";

            case NAME_DATE_ASC:
                return selectStatement + "order by name, create_date;";

            case NAME_DATE_DESC:
                return selectStatement + "order by name, create_date desc;";

            default:
                return selectStatement + ";";
        }
    }

}