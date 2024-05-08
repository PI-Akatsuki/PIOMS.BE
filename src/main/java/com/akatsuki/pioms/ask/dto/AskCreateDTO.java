package com.akatsuki.pioms.ask.dto;

import com.akatsuki.pioms.ask.entity.AdminEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AskCreateDTO {
    private String title;
    private String content;
    private int franchiseOwnerCode;
}
