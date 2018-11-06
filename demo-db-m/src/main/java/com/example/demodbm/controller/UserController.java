package com.example.demodbm.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demodbm.business.service.IUserService;
import com.example.demodbm.business.dto.User;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kenzhao
 * @since 2018-09-10
 */
@RestController
@Log
public class UserController {


  @Autowired
  private IUserService userService;

  private SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @RequestMapping("/addXnUser")
  public String addXnUser(String fileName) {

    try {
      InputStream inputStream = new FileInputStream(new File(fileName));
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      String line; int i = 0;
      List<User> users = new ArrayList<>();

      Map<String, String> isExits = new HashMap<>();
      while ((line = reader.readLine()) != null) {
        i++;
//        if (i > 20) {
//          break;
//        }
        if (line == null || line.length() < 500) {
          continue;
        }
        if (users.size() >= 200) {
          userService.saveBatch(users);
          users.clear();
          isExits.clear();
        }
//                System.out.println(i + ":" + line);
        int index = line.indexOf("\"\"phone\"\":\"\"");
        int createTimeIndex = line.indexOf("\"\"create_time\"\":\"\"");
        int indexName = line.indexOf("\"\"acc_name\"\":\"\"");
        int indexAmount = line.indexOf("\"\"amount\"\":");
        int indexAccNo = line.indexOf("\"\"acc_no\"\":\"\"");



        String linePhone = line.substring(index+12, index+23);
        String lineName = line.substring(indexName+15, indexName+18);
        String createTime = line.substring(createTimeIndex+18, createTimeIndex+37);
        String amount = line.substring(indexAmount+11, indexAmount+18);
        String accNo = line.substring(indexAccNo+13, indexAccNo+32);
        accNo = accNo.replaceAll("\"", "");
        accNo = accNo.replaceAll(",", "");


        amount = amount.replaceAll("\"", "");
        amount = amount.replaceAll(",", "");
//          lineName =db.NtNotifyAppMsgs.find({modleType:'XPM001',lastTime:{$gt:1536988172000}});
        lineName = lineName.replaceAll("\"", "");
        System.out.println(i + ":" + linePhone + lineName + createTime + amount + "：" + accNo);


        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUPhone,linePhone);

        User user = userService.getOne(queryWrapper);
        if (user != null && user.getHsid() != null && user.getHsid().intValue() > 0) {

          continue;
        }
        if (isExits.containsKey(linePhone)) {
          continue;
        }

        user = new User();
        user.setUPhone(linePhone);
        user.setUName(lineName);
        user.setCreateTime(createTime);
        Date date = new Date();
        user.setCTime(sdf.format(date));
        user.setCTimeLong(date.getTime());
        user.setAmount(new BigDecimal(amount));
        user.setBankNo(accNo);
        users.add(user);
        isExits.put(linePhone, "1");
      }
      if (users.size() > 0) {
        userService.saveBatch(users);
        users.clear();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "OK";
  }
}

