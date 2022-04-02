package com.yang.serviceedu.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class MemberVo {

        @ApiModelProperty(value = "会员id")
        @TableId(value = "id", type = IdType.ID_WORKER_STR)
        private String id;

        @ApiModelProperty(value = "手机号")
        private String mobile;

        @ApiModelProperty(value = "昵称")
        private String nickname;

        @ApiModelProperty(value = "用户头像")
        private String avatar;
}
