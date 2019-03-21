package com.tsoyolv.transfermoney.logic.dao;

import com.tsoyolv.transfermoney.logic.entity.DbEntity;

import java.util.Collection;

public interface Dao<T extends DbEntity> {
    Collection<T> get();

    T get(Long id);

    T save(T forSave);

    T delete(T forDelete);
}
