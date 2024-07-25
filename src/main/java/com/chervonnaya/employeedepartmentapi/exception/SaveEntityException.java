package com.chervonnaya.employeedepartmentapi.exception;

import lombok.Getter;

@Getter
public class SaveEntityException extends RuntimeException {
    private final String entityClass;
    private Long id;

    public SaveEntityException(String simpleName, String message) {
        super(message);
        this.entityClass = simpleName;
    }

    public SaveEntityException(String entityClass, Long id, String message) {
        super(message);
        this.entityClass = entityClass;
        this.id = id;
    }
}
