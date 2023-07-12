package com.edu.uploadfile.mobiles.payloads.response;

public class ImageResponse {
    private Long id;
    private String name;
    private String type;
    private String uriLocal;
    private String url;

    /**
     * load all photo1
     */
    public ImageResponse(Long id, String name, String type, String uriLocal, String url) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.uriLocal = uriLocal;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUriLocal() {
        return uriLocal;
    }

    public void setUriLocal(String uriLocal) {
        this.uriLocal = uriLocal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
