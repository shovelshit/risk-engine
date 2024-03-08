package com.ljf.risk.engine.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 名单分类
 * @author lijinfeng
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("t_engine_roaster")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Roaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String code;
    private String name;
    /**
     * 创建时间
     */
    private Date createTime;
    private String remark;
    private Date updateTime;
    private String updateUser;
}
