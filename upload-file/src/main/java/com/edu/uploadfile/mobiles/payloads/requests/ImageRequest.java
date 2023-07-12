package com.edu.uploadfile.mobiles.payloads.requests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ImageRequest {
    private Long id;
    private String name;
    private String type;
    private String uriLocal;
    private String url;


}
