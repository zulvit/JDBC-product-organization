package ru.zulvit.entity;

import org.jetbrains.annotations.NotNull;

public record Overhead(int id, @NotNull String date, int organizationId) {
    @Override
    public String toString() {
        return "Overhead{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", organizationId=" + organizationId +
                '}';
    }
}
