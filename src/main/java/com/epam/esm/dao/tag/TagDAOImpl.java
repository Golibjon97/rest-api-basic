package com.epam.esm.dao.tag;

import com.epam.esm.dto.mapper.TagMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NoDataFoundException;
import com.epam.esm.exception.TagAlreadyExistException;
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
        try{
            jdbcTemplate.update(QUERY_INSERT_TAG, tag.getId(), tag.getName());
            return tag;
        }catch (DataIntegrityViolationException e){
            throw new TagAlreadyExistException("tag with name \"" + tag.getName() + "\" already exists");
        }
    }

    @Override
    public Tag get(UUID tagId) {
        try{
            return jdbcTemplate.queryForObject(QUERY_GET_TAG, new TagMapper(), tagId);
        }catch (EmptyResultDataAccessException e){
            throw new NoDataFoundException("no tag found with id: " + tagId);
        }
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(QUERY_GET_ALL, new TagMapper());
    }

    @Override
    public int delete(UUID tagId) {
        return jdbcTemplate.update(QUERY_DELETE_TAG, tagId);
    }

    @Override
    public void createWithGiftCertificate(UUID certificateId, List<Tag> tags) { // Used for creating connection with one certificate ID and multiple tags

        tags.forEach(tag -> {       // comes from certificateDTO with list of tags. Then creates the tag into tag table if it doesn't exist
            Tag byName = getByName(tag.getName());
            if(byName == null){
                tag.setId(UUID.randomUUID());
                jdbcTemplate.update(QUERY_INSERT_TAG, tag.getId(), tag.getName());
            } else
                tag.setId(byName.getId());
            jdbcTemplate.update(QUERY_CREATE_CONNECTION, tag.getId(), certificateId);
        });
    }

    @Override
    public List<Tag> getTags(UUID certificateId) {

        return jdbcTemplate.query(QUERY_GET_TAGS, new TagMapper(), certificateId);
    }

    @Override
    public Tag getByName(String name) {
        try {
            return jdbcTemplate.queryForObject(QUERY_GET_BY_NAME, new TagMapper(), name);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

}
