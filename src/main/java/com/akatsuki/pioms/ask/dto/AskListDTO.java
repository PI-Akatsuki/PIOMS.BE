package com.akatsuki.pioms.ask.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class AskListDTO {
    private List<AskDTO> asks;
}