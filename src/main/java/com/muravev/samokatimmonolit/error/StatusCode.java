package com.muravev.samokatimmonolit.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StatusCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Не авторизован", "Неавторизованный доступ"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Нет доступа"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Не валидный запрос"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера"),

    INVENTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Инвентарь не найден"),
    INVENTORY_IS_USED(HttpStatus.BAD_REQUEST, "Инвенетарь в аренде"),

    INVENTORY_MODEL_NOT_FOUND(HttpStatus.NOT_FOUND, "Модель инвентаря не найдена"),
    INVENTORY_MODEL_CANNOT_REMOVED(HttpStatus.BAD_REQUEST, "Модель инвентаря не может быть удалена"),

    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "Пользователь уже существует"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Пользователь не найден"),
    USER_INVITE_IS_EXPIRED(HttpStatus.BAD_REQUEST, "Код устарел", "Запросите новый код"),
    USER_INVALID_INVITE_CODE(HttpStatus.BAD_REQUEST, "Неверный код", "Перепроверьте или запросите новый код"),

    ORGANIZATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Организация не найдена"),
    ORGANIZATION_INVALID_INN(HttpStatus.BAD_REQUEST, "Не валидный ИНН"),
    ORGANIZATION_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "Организация уже существует"),

    OFFICE_NOT_FOUND(HttpStatus.NOT_FOUND, "Офис проката не найден"),

    TARIFF_NOT_FOUND(HttpStatus.NOT_FOUND, "Тариф не найден"),
    TARIFF_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "Тариф данного типа уже существует", "Удалите существующий тариф аналогичного типа"),

    RENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Аренда не найдена"),
    ALREADY_UNPAID_RENT(HttpStatus.CONFLICT, "Имеется не оплаченная аренда", "Аренда будет доступна после погашения задолженности"),
    MAX_ACTIVE_RENTS(HttpStatus.BAD_REQUEST, "Исчерпан лимит активным аренд"),
    CONCURRENCY_STARTING_RENTS(HttpStatus.CONFLICT, "Одновременное начало нескольких аренд"),

    FILE_UPLOAD_WITH_ERROR(HttpStatus.BAD_REQUEST, "Ошибка загрузки файла"),
    FILE_DOWNLOAD_WITH_ERROR(HttpStatus.BAD_REQUEST, "Ошибка загрузки файла"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "Файл не найден"),

    DEPOSIT_CREATING_ERROR(HttpStatus.PROCESSING, "Ошибка резервации залога"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String description;

    StatusCode(HttpStatus httpStatus, String message) {
        this(httpStatus, message, null);
    }
}
