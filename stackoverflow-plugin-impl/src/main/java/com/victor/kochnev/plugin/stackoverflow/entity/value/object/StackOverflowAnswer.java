package com.victor.kochnev.plugin.stackoverflow.entity.value.object;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StackOverflowAnswer {
    private Long answerId;
    private Long userId;
    private String userName;
    private ZonedDateTime createDate;
}
