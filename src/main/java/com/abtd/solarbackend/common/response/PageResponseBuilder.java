package com.abtd.solarbackend.common.response;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.function.Function;

public class PageResponseBuilder {

    private PageResponseBuilder() {
    }

    public static Pageable buildPageable(PageRequestDto request) {

        Sort sort = Sort.by(
                request.getDirection().equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                request.getSortBy());

        return PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort);
    }

    public static <T> PageResponse<T> build(Page<T> page) {

        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    public static <T, R> PageResponse<R> build(
            Page<T> page,
            Function<T, R> mapper) {

        return PageResponse.<R>builder()
                .content(page.getContent()
                        .stream()
                        .map(mapper)
                        .toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}