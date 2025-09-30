package ru.practicum.shareit.server.item.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {

    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @Test
    void toEntity_shouldConvertCreateRequestDtoToEntity() {
        ItemCreateRequestDto dto = new ItemCreateRequestDto(
                "Test Item", "Test Description", true, 1L);

        Item item = itemMapper.toEntity(dto);

        assertThat(item).isNotNull();
        assertThat(item.getName()).isEqualTo("Test Item");
        assertThat(item.getDescription()).isEqualTo("Test Description");
        assertThat(item.getAvailable()).isTrue();
        assertThat(item.getRequestId()).isEqualTo(1L);
        assertThat(item.getId()).isNull();
        assertThat(item.getOwner()).isNull();
    }

    @Test
    void toResponseDto_shouldConvertEntityToResponseDto() {
        User owner = User.builder()
                .id(1L)
                .name("Owner")
                .email("owner@example.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .owner(owner)
                .requestId(1L)
                .build();

        ItemResponseDto dto = itemMapper.toResponseDto(item);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Test Item");
        assertThat(dto.description()).isEqualTo("Test Description");
        assertThat(dto.available()).isTrue();
        assertThat(dto.requestId()).isEqualTo(1L);
        assertThat(dto.owner()).isNotNull();
        assertThat(dto.comments()).isNull();
        assertThat(dto.lastBooking()).isNull();
        assertThat(dto.nextBooking()).isNull();
    }

    @Test
    void updateEntity_shouldUpdateEntityFromUpdateRequestDto() {
        Item item = Item.builder()
                .id(1L)
                .name("Old Name")
                .description("Old Description")
                .available(true)
                .build();

        ItemUpdateRequestDto updateDto = new ItemUpdateRequestDto(
                "New Name", "New Description", false);

        itemMapper.updateEntity(updateDto, item);

        assertThat(item.getName()).isEqualTo("New Name");
        assertThat(item.getDescription()).isEqualTo("New Description");
        assertThat(item.getAvailable()).isFalse();

        assertThat(item.getId()).isEqualTo(1L);
    }

    @Test
    void updateEntity_shouldIgnoreNullFields() {
        Item item = Item.builder()
                .id(1L)
                .name("Old Name")
                .description("Old Description")
                .available(true)
                .build();

        ItemUpdateRequestDto updateDto = new ItemUpdateRequestDto(null, null, null);

        itemMapper.updateEntity(updateDto, item);

        assertThat(item.getName()).isEqualTo("Old Name");
        assertThat(item.getDescription()).isEqualTo("Old Description");
        assertThat(item.getAvailable()).isTrue();
    }

    @Test
    void toCommentResponseDto_shouldConvertCommentEntityToResponseDto() {
        User author = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("Test Item")
                .build();

        LocalDateTime created = LocalDateTime.of(2024, 1, 15, 10, 0);
        Comment comment = Comment.builder()
                .id(1L)
                .text("Great item!")
                .item(item)
                .author(author)
                .created(created)
                .build();

        CommentResponseDto dto = itemMapper.toCommentResponseDto(comment);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.text()).isEqualTo("Great item!");
        assertThat(dto.authorName()).isEqualTo("Test User");
        assertThat(dto.created()).isEqualTo(created);
    }

    @Test
    void toCommentEntity_shouldConvertCreateRequestDtoToCommentEntity() {
        CommentCreateRequestDto dto = new CommentCreateRequestDto("Test comment");

        Comment comment = itemMapper.toCommentEntity(dto);

        assertThat(comment).isNotNull();
        assertThat(comment.getText()).isEqualTo("Test comment");
        assertThat(comment.getId()).isNull();
        assertThat(comment.getItem()).isNull();
        assertThat(comment.getAuthor()).isNull();
        assertThat(comment.getCreated()).isNull();
    }
}