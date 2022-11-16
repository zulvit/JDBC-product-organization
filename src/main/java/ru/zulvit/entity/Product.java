package ru.zulvit.entity;

import org.jetbrains.annotations.NotNull;

public record Product(int id, @NotNull String title) {
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
