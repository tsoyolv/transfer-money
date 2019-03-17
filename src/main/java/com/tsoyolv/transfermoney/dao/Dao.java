package com.tsoyolv.transfermoney.dao;

import com.tsoyolv.transfermoney.model.DbEntity;

import java.util.Collection;

public interface Dao<T extends DbEntity> {
    Collection<T> get();

    T get(Long id);

    T save(T forSave);

    T delete(T forDelete);
}
