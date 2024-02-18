package com.victor.kochnev.plugin.stackoverflow.entity.value.object;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class StackOverflowComment {
    private String idComment;
}
