package com.epam.esm.mapper.service.tag;


import com.epam.esm.DTO.response.BaseResponseDto;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.mapper.service.BaseService;

import java.util.List;

public interface TagService extends BaseService<Tag> {
    BaseResponseDto<List<Tag>> getAll();

}
