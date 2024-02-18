package com.victor.kochnev.plugin.stackoverflow.entity.value.object;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StackOverflowComment {
    private String idComment;
}
