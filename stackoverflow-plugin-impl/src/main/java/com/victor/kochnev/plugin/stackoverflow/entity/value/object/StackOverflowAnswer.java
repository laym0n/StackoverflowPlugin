package com.victor.kochnev.plugin.stackoverflow.entity.value.object;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Builder
public class StackOverflowAnswer {
    private String userName;
    private ZonedDateTime lastEditDateTime;
}
