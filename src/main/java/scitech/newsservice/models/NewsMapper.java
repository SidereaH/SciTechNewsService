package scitech.newsservice.models;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import scitech.newsservice.models.dto.NewsDto;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    @Mapping(target = "tags", expression = "java(newsObject.getTagsList())")
    @Mapping(target = "status", source = "status.name")
    NewsDto toDto(NewsObject newsObject);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", expression = "java(String.join(\",\", newsDto.getTags()))")
    @Mapping(target = "status", ignore = true)
    NewsObject toEntity(NewsDto newsDto);

    @Mapping(target = "id", ignore = false)
    @Mapping(target = "tags", expression = "java(String.join(\",\", newsDto.getTags()))")
    @Mapping(target = "status", ignore = true)
    void updateFromDto(NewsDto newsDto, @MappingTarget NewsObject newsObject);
}