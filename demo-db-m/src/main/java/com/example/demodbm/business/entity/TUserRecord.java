package com.example.demodbm.business.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author kenzhao
 * @since 2018-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TUserRecord extends Model<TUserRecord> {

    private static final long serialVersionUID = 1L;

    private Long hsid;

    private LocalDateTime cTime;

    private LocalDateTime createTime;

    private String uCardid;

    private String uName;

    private String uPhone;

    private BigDecimal amount;

    private String bankNo;

    private Long cTimeLong;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
