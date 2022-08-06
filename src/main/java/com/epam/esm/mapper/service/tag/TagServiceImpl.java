package com.epam.esm.mapper.service.tag;


import com.epam.esm.DTO.response.BaseResponseDto;
import com.epam.esm.exception.NoDataFoundException;
import com.epam.esm.exception.UnknownDataBaseException;
import com.epam.esm.DAO.tag.TagDAO;
import com.epam.esm.entity.tag.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService{

    private final TagDAO tagDAO;



    @Override
    public BaseResponseDto<Tag> create(Tag tag) {

        tag.setId(UUID.randomUUID());
        tag = tagDAO.create(tag);

        if(tag != null )
            return new BaseResponseDto<>(200, "tag created", tag);
        throw new UnknownDataBaseException("Unknown problem occurred in the database");
    }

    @Override
    public BaseResponseDto<Tag> get(UUID tagId) {
        Tag tag = tagDAO.get(tagId);
        return new BaseResponseDto<>(200, "success", tag);
    }

    @Override
    public BaseResponseDto<List<Tag>> getAll() {
        return new BaseResponseDto<>(200, "list of tags", tagDAO.getAll());
    }

    @Override
    public BaseResponseDto delete(UUID tagId) {
        int delete = tagDAO.delete(tagId);
        if(delete == 1)
            return new BaseResponseDto(204, "certificate deleted");
        throw new NoDataFoundException("there is no tag with id: " + tagId + " to delete");
    }
}
