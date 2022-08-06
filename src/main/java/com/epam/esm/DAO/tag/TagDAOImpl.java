package com.epam.esm.DAO.tag;


import com.epam.esm.entity.tag.Tag;
import com.epam.esm.exception.NoDataFoundException;
import com.epam.esm.exception.tag.TagAlreadyExistException;
import com.epam.esm.mapper.TagMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class TagDAOImpl implements TagDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Tag create(Tag tag) {
        String QUERY_CREATE_TAG = "insert into tag (id, name) values(?, ?);";
        try{
            jdbcTemplate.update(QUERY_CREATE_TAG, tag.getId(), tag.getName());
            return tag;
        }catch (DataIntegrityViolationException e){
            throw new TagAlreadyExistException("tag with name \"" + tag.getName() + "\" already exists");
        }
    }

    @Override
    public Tag get(UUID tagId) {
        String QUERY_GET_TAG = "select * from tag where id = ?;";
        try{
            return jdbcTemplate.queryForObject(QUERY_GET_TAG, new TagMapper(), tagId);
        }catch (EmptyResultDataAccessException e){
            throw new NoDataFoundException("no tag found with id: " + tagId);
        }
    }

    @Override
    public Tag getByName(String name) {
        String QUERY_GET_BY_NAME = "select * from tag where name = ?";
        try {
            return jdbcTemplate.queryForObject(QUERY_GET_BY_NAME, new TagMapper(), name);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<Tag> getAll() {
        String QUERY_GET_ALL = "select * from tag;";
        return jdbcTemplate.query(QUERY_GET_ALL, new TagMapper());
    }

    @Override
    public int delete(UUID tagId) {
        String QUERY_DELETE_TAG = "delete from tag where id = ?;";
        return jdbcTemplate.update(QUERY_DELETE_TAG, tagId);
    }

    @Override
    public void createWithGiftCertificate(UUID certificateId, List<Tag> tags) {
        String QUERY_CREATE_TAG = "insert into tag (id, name) values(?, ?);";
        String QUERY_CREATE_CONNECTION
                = "insert into gift_certificate_tag (tag_id, gift_certificate_id) values (?, ?);";

        tags.forEach(tag -> {
            Tag byName = getByName(tag.getName());
            if(byName == null){
                tag.setId(UUID.randomUUID());
                jdbcTemplate.update(QUERY_CREATE_TAG, tag.getId(), tag.getName());
            } else
                tag.setId(byName.getId());
            jdbcTemplate.update(QUERY_CREATE_CONNECTION, tag.getId(), certificateId);
        });
    }

    @Override
    public List<Tag> getTags(UUID certificateId) {
        String QUERY_GET_TAGS = "select *from get_certificate_tags(?);";

        return jdbcTemplate.query(QUERY_GET_TAGS, new TagMapper(), certificateId);
    }


}
