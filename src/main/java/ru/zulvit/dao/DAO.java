package ru.zulvit.dao;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {

    @NotNull Optional<T> findById(int id);

    @NotNull List<@NotNull T> getAll();

    void save(@NotNull T entity);

    void update(@NotNull T entity);

    void delete(@NotNull T entity);
}