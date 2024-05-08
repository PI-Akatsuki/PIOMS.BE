package com.akatsuki.pioms.ask.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AskUpdateDTO {
    private String title;
    private String content;
}
