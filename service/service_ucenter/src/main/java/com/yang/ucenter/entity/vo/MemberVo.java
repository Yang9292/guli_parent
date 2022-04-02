package com.yang.ucenter.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UcenterMemberVo对象", description="会员表")
public class MemberVo {

        private static final long serialVersionUID = 1L;

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
