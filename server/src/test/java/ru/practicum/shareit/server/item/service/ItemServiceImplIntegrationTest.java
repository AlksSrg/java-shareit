package ru.practicum.shareit.server.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.item.CommentNotAllowedException;
import ru.practicum.shareit.exception.item.ItemNotFoundException;
import ru.practicum.shareit.exception.item.ItemNotOwnedByUserException;
import ru.practicum.shareit.exception.user.UserNotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Sql(scripts = {"/test-schema.sql", "/test-data.sql"})
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Test
    void findByOwnerId_shouldReturnUserItemsWithPagination() {
        Long ownerId = 1L;
        Integer from = 0;
        Integer size = 10;

        List<ItemResponseDto> items = itemService.findByOwnerId(ownerId, from, size);

        assertThat(items).isNotEmpty();
        assertThat(items).allMatch(item -> item.getOwner().id().equals(ownerId));
    }

    @Test
    void findByOwnerId_shouldReturnPaginatedResults() {
        Long ownerId = 1L;
        Integer from = 0;
        Integer size = 2;

        List<ItemResponseDto> items = itemService.findByOwnerId(ownerId, from, size);

        assertThat(items).hasSize(2);
    }

    @Test
    void findByOwnerId_shouldThrowExceptionForNonExistentUser() {
        Long nonExistentOwnerId = 999L;
        Integer from = 0;
        Integer size = 10;

        assertThrows(UserNotFoundException.class, () -> {
            itemService.findByOwnerId(nonExistentOwnerId, from, size);
        });
    }

    @Test
    void create_shouldCreateNewItem() {
        Long ownerId = 1L;
        ItemCreateRequestDto createDto = new ItemCreateRequestDto(
                "New Item", "New Description", true, null);

        ItemResponseDto createdItem = itemService.create(ownerId, createDto);

        assertThat(createdItem).isNotNull();
        assertThat(createdItem.getId()).isNotNull();
        assertThat(createdItem.getName()).isEqualTo("New Item");
        assertThat(createdItem.getDescription()).isEqualTo("New Description");
        assertThat(createdItem.getAvailable()).isTrue();
        assertThat(createdItem.getOwner().id()).isEqualTo(ownerId);
    }

    @Test
    void create_shouldCreateItemWithRequestId() {
        Long ownerId = 1L;
        Long requestId = 1L; // Изменено с 100L на 1L
        ItemCreateRequestDto createDto = new ItemCreateRequestDto(
                "Requested Item", "Item from request", true, requestId);

        ItemResponseDto createdItem = itemService.create(ownerId, createDto);

        assertThat(createdItem).isNotNull();
        assertThat(createdItem.getRequestId()).isEqualTo(requestId);
    }

    @Test
    void update_shouldUpdateExistingItem() {
        Long itemId = 1L;
        Long ownerId = 1L;
        ItemUpdateRequestDto updateDto = new ItemUpdateRequestDto(
                "Updated Name", "Updated Description", false);

        ItemResponseDto updatedItem = itemService.update(itemId, ownerId, updateDto);

        assertThat(updatedItem).isNotNull();
        assertThat(updatedItem.getName()).isEqualTo("Updated Name");
        assertThat(updatedItem.getDescription()).isEqualTo("Updated Description");
        assertThat(updatedItem.getAvailable()).isFalse();
    }

    @Test
    void update_shouldThrowExceptionWhenUserNotOwner() {
        Long itemId = 1L;
        Long notOwnerId = 2L;
        ItemUpdateRequestDto updateDto = new ItemUpdateRequestDto("New Name", null, null);

        assertThrows(ItemNotOwnedByUserException.class, () -> {
            itemService.update(itemId, notOwnerId, updateDto);
        });
    }

    @Test
    void findById_shouldReturnItemWithComments() {
        Long itemId = 1L;

        ItemResponseDto item = itemService.findById(itemId);

        assertThat(item).isNotNull();
        assertThat(item.getId()).isEqualTo(itemId);
        assertThat(item.getComments()).isNotEmpty();
    }

    @Test
    void findById_shouldThrowExceptionForNonExistentItem() {
        Long nonExistentItemId = 999L;

        assertThrows(ItemNotFoundException.class, () ->
                itemService.findById(nonExistentItemId));
    }

    @Test
    void searchAvailableItems_shouldReturnMatchingItems() {
        String searchText = "laptop";
        Integer from = 0;
        Integer size = 10;

        List<ItemResponseDto> items = itemService.searchAvailableItems(searchText, from, size);

        assertThat(items).isNotEmpty();
        assertThat(items).allMatch(ItemResponseDto::getAvailable);
        assertThat(items).allMatch(item ->
                item.getName().toLowerCase().contains("laptop") ||
                        item.getDescription().toLowerCase().contains("laptop"));
    }

    @Test
    void searchAvailableItems_shouldReturnEmptyListForEmptyText() {
        String emptyText = "";
        Integer from = 0;
        Integer size = 10;

        List<ItemResponseDto> items = itemService.searchAvailableItems(emptyText, from, size);

        assertThat(items).isEmpty();
    }

    @Test
    void searchAvailableItems_shouldReturnEmptyListForNullText() {
        String nullText = null;
        Integer from = 0;
        Integer size = 10;

        List<ItemResponseDto> items = itemService.searchAvailableItems(nullText, from, size);

        assertThat(items).isEmpty();
    }

    @Test
    void delete_shouldDeleteItem() {
        Long itemId = 5L;
        Long ownerId = 2L;

        itemService.delete(itemId, ownerId);

        assertThrows(ItemNotFoundException.class, () -> itemService.findById(itemId));
    }

    @Test
    void delete_shouldThrowExceptionWhenUserNotOwner() {
        Long itemId = 1L;
        Long notOwnerId = 2L;

        assertThrows(ItemNotOwnedByUserException.class, () ->
                itemService.delete(itemId, notOwnerId));
    }

    @Test
    void addComment_shouldAddCommentWhenUserHasCompletedBooking() {
        Long itemId = 1L;
        Long userId = 2L;
        CommentCreateRequestDto commentDto = new CommentCreateRequestDto("Great item!");

        CommentResponseDto result = itemService.addComment(itemId, userId, commentDto);

        assertThat(result).isNotNull();
        assertThat(result.text()).isEqualTo("Great item!");
        assertThat(result.authorName()).isNotNull();
        assertThat(result.created()).isNotNull();
    }

    @Test
    void addComment_shouldThrowExceptionWhenUserHasNoCompletedBooking() {
        Long itemId = 1L;
        Long userId = 3L;
        CommentCreateRequestDto commentDto = new CommentCreateRequestDto("Test comment");

        assertThrows(CommentNotAllowedException.class, () ->
                itemService.addComment(itemId, userId, commentDto));
    }

    @Test
    void getItemOrThrow_shouldReturnItemWhenExists() {
        Long itemId = 1L;

        var item = itemService.getItemOrThrow(itemId);

        assertThat(item).isNotNull();
        assertThat(item.getId()).isEqualTo(itemId);
    }

    @Test
    void getItemOrThrow_shouldThrowExceptionWhenItemNotFound() {
        Long nonExistentItemId = 999L;

        assertThrows(ItemNotFoundException.class, () ->
                itemService.getItemOrThrow(nonExistentItemId));
    }

    @Test
    void findByRequestId_shouldReturnItemsForRequest() {
        Long requestId = 1L;

        List<ItemResponseDto> items = itemService.findByRequestId(requestId);

        assertThat(items).isNotEmpty();
        assertThat(items).allMatch(item -> requestId.equals(item.getRequestId()));
    }

    @Test
    void findByRequestId_shouldReturnEmptyListForNonExistentRequest() {
        Long nonExistentRequestId = 999L;

        List<ItemResponseDto> items = itemService.findByRequestId(nonExistentRequestId);

        assertThat(items).isEmpty();
    }
}