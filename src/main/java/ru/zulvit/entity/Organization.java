package ru.zulvit.entity;

import org.jetbrains.annotations.NotNull;

public record Organization(int inn, @NotNull String name, int checking_account) {
    @Override
    public String toString() {
        return "Organization{" +
                "inn=" + inn +
                ", name='" + name + '\'' +
                ", checking_account=" + checking_account +
                '}';
    }
}
