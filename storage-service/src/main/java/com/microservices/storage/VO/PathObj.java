package com.microservices.storage.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.nio.file.Path;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PathObj {
    private Long itemId;
    private Path path;
}
