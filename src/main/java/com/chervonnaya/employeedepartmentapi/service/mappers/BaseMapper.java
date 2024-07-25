package com.chervonnaya.employeedepartmentapi.service.mappers;

public interface BaseMapper<E, D> {
    E map(D d);
}
