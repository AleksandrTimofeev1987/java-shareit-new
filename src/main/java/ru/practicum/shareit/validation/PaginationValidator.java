package ru.practicum.shareit.validation;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.model.BadRequestException;

@Slf4j
public class PaginationValidator {

    public static void validatePaginationParameters(Integer from,
                                                    Integer size) {
        if (from < 0 || size < 1) {
            final String message = "Pagination parameters are incorrect. From should be zero or bigger and Size should be positive.";
            log.warn(message);
            throw new BadRequestException(message);
        }
    }

}
