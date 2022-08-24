package com.epam.esm.service.tag;


import com.epam.esm.dao.tag.TagDAO;
import com.epam.esm.entity.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Tag API to manipulate with tags (create, get, delete)
 */
@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService{

    private final TagDAO tagDAO;

    /**
     * Creates a new tag
     * @param tag tag object to create a new tag
     * @return the created tag
     */
    @Override
    public Tag create(Tag tag) {

        tag.setId(UUID.randomUUID());
        tag = tagDAO.create(tag);

        return tag;
    }

    /**
     * gets the tag as per the given UUID
     * @param tagId UUID to find the existing tag
     * @return the tag object
     */
    @Override
    public Tag get(UUID tagId) {
        Tag tag = tagDAO.get(tagId);
        return tag;
    }

    /**
     * gets all the existing tags
     * @return all the existing tags
     */
    @Override
    public List<Tag> getAll() {
        return tagDAO.getAll();
    }

    /**
     * deletes the tag as per the given UUID
     * @param tagId to find the tag in order to delete
     * @return the HTTP status code
     */
    @Override
    public int delete(UUID tagId) {
        return tagDAO.delete(tagId);
    }

}
