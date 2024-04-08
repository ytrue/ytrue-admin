package com.ytrue.bean.resp.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CaptchaResp implements Serializable {
    @Serial
    private static final long serialVersionUID = -2368863016297932647L;

    @Schema(description = "uuid")
    private String uuid;

    @Schema(description = "base64图片")
    private String img;
}
