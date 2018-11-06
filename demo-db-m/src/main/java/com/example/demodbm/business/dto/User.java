package com.example.demodbm.business.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * Description:
 *
 * @author kenzhao
 * @date 2018/10/22 17:45
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user")
public class User extends Model<User> {

  private static final long serialVersionUID = 1L;
  private Long hsid;
  private String uPhone;
  private String uName;
  private String uCardid;
  private String bankNo;
  private String createTime;
  private String cTime;
  private Long cTimeLong;
  private BigDecimal amount;

  @Override
  protected Serializable pkVal() {
    return this.hsid;
  }
}