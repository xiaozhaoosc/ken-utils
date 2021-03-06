package com.example.demodbm.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demodbm.business.entity.TUserNew;
import com.example.demodbm.business.entity.TUserRecord;
import com.example.demodbm.business.entity.User;
import com.example.demodbm.business.entity.UserWei;
import com.example.demodbm.business.mapper.UserDao;
import com.example.demodbm.business.service.IUserService;
import com.example.demodbm.business.service.MPTUserNewService;
import com.example.demodbm.business.service.MPTUserRecordService;
import com.example.demodbm.business.service.MPTUserWeiService;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author kenzhao
 * @date 2018/10/31 18:02
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao,User> implements IUserService {

  private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private Gson gson = new Gson();
  @Autowired
  private IUserService userService;

  @Autowired
  private MPTUserRecordService tUserRecordService;
  @Autowired
  private MPTUserNewService mptUserNewService;
  @Autowired
  private MPTUserWeiService mptUserWeiService;

  /**
   * 添加用户信息
   */
  @Override
  public void addUserInfo(String fileName) {
//    String[] fileNames = {"1101","1031","102731","102527","102225","101922"};
    String[] fileNames0 = {"12","13","14","15","16","17","18","19","1101","1031","102731","102527","102225","101922",
        "110205","110507","110709","1107011","1107013","1107015","1107017","1107019","1107021","1107023","1107025","1107027","1107029"};
    String[] fileNames = {"0214","0216","0218","0220","0222","0224","0226","0228"};
    if ("all".equals(fileName)) {
      for (int i = 0; i < fileNames.length; i++) {
        log.info("---------" + i + "---------" + fileNames[i]);
        addUser("e:\\csv\\csv\\" + fileNames[i] + ".csv");
      }
    }else{
      addUser(fileName);
    }

  }

  /**
   * 添加用户信息
   */
  @Override
  public void addUserInfo2(String fileName) {
    addUser2(fileName);
  }

  /**
   * 用户信息
   * 线程安全
   */
  @Override
  @Cacheable(value = "my-redis-user", sync=true)
  public User getUserInfo(String phone) {
    log.info("begin in getUserInfo---------" + phone + "---------");
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(User::getUPhone,phone);
    User user = (User)userService.getObj(queryWrapper);
    log.info("end in getUserInfo---------" + phone + "---------");
    return user;
  }

  /**
   * 用户信息
   */
  @Override
  @Cacheable(value = "my-redis-user",key = "#phone")
  public User getUserInfo2(String phone) {
    log.info("begin in getUserInfo2---------" + phone + "---------");
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(User::getUPhone,phone);
    User user = (User)userService.getObj(queryWrapper);
    log.info("end in getUserInfo2---------" + phone + "---------");
    return user;
  }


  private void addUser2(String fileName){
    int count = 0;
    int sumCount = 0;
    try {
      InputStream inputStream = new FileInputStream(new File(fileName));
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      String line;
      int i = 0;
      List<TUserNew> users = new ArrayList<>();
      List<TUserRecord> tUserRecords = new ArrayList<>();

      Map<String, String> isExits = new HashMap<>();
      while ((line = reader.readLine()) != null) {
        i++;
        sumCount = i;
//        if (i > 20) {
//          break;
//        }
        if (line == null || line.length() < 500) {
          continue;
        }
        if (users.size() >= 200) {
          mptUserNewService.saveBatch(users);
          count = count + users.size();
          users.clear();
          isExits.clear();
        }

        if (tUserRecords.size() >= 500) {
          tUserRecordService.saveBatch(tUserRecords);
          tUserRecords.clear();
        }
//                System.out.println(i + ":" + line);
        int index = line.indexOf("\"\"phone\"\":\"\"");
//        int createTimeIndex = line.indexOf("\"\"create_time\"\":\"\"");
        int indexName = line.indexOf("\"\"name\"\":\"\"");
        int indexAmount = line.indexOf("\"\"lamt\"\":");
//        int indexAccNo = line.indexOf("\"\"card\"\":\"\"");

        String linePhone = line.substring(index + 12, index + 23);
        String lineName = line.substring(indexName + 11, indexName + 14);
//        String createTime = line.substring(createTimeIndex + 18, createTimeIndex + 37);
        String amount = line.substring(indexAmount + 11, indexAmount + 18);
//        String accNo = line.substring(indexAccNo + 13, indexAccNo + 32);
//        accNo = accNo.replaceAll("\"", "");
//        accNo = accNo.replaceAll(",", "");

        amount = amount.replaceAll("\"", "");
        amount = amount.replaceAll(",", "");
//          lineName =db.NtNotifyAppMsgs.find({modleType:'XPM001',lastTime:{$gt:1536988172000}});
        lineName = lineName.replaceAll("\"", "");
        System.out.println(i + ":" + linePhone + lineName + amount );

        QueryWrapper<TUserNew> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TUserNew::getUPhone, linePhone);

        TUserNew user = new TUserNew();
        user.setUPhone(linePhone);
        user.setUName(lineName);
        LocalDateTime ldt = LocalDateTime.now();
        user.setCreateTime(ldt);
        user.setCTime(LocalDateTime.now());
        user.setCTimeLong(System.currentTimeMillis());
        user.setAmount(new BigDecimal(amount));
//        user.setBankNo(accNo);
        String userStr = gson.toJson(user);
        tUserRecords.add(gson.fromJson(userStr, TUserRecord.class));
        TUserNew userSel = mptUserNewService.getOne(queryWrapper);
        if (userSel != null && userSel.getHsid() != null) {
          continue;
        }
        if (isExits.containsKey(linePhone)) {
          continue;
        }
        users.add(user);
        isExits.put(linePhone, "1");
      }
      if (users.size() > 0) {
        count = count + users.size();
        mptUserNewService.saveBatch(users);
        users.clear();
      }
      if (tUserRecords.size() > 0) {
        tUserRecordService.saveBatch(tUserRecords);
        users.clear();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    log.info("---------count:" + count + "---------sumCount:" + sumCount);
  }
  private void addUser(String fileName){
    int count = 0;
    int sumCount = 0;
    try {
      InputStream inputStream = new FileInputStream(new File(fileName));
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      String line;
      int i = 0;
      List<TUserNew> users = new ArrayList<>();
      List<TUserRecord> tUserRecords = new ArrayList<>();

      Map<String, String> isExits = new HashMap<>();
      while ((line = reader.readLine()) != null) {
        i++;
        sumCount = i;
//        if (i > 20) {
//          break;
//        }
        if (line == null || line.length() < 500) {
          continue;
        }
        if (users.size() >= 200) {
          mptUserNewService.saveBatch(users);
          count = count + users.size();
          users.clear();
          isExits.clear();
        }

        if (tUserRecords.size() >= 500) {
          tUserRecordService.saveBatch(tUserRecords);
          tUserRecords.clear();
        }
//                System.out.println(i + ":" + line);
        int index = line.indexOf("\"\"phone\"\":\"\"");
        int createTimeIndex = line.indexOf("\"\"create_time\"\":\"\"");
        int indexName = line.indexOf("\"\"acc_name\"\":\"\"");
        int indexAmount = line.indexOf("\"\"amount\"\":");
        int indexAccNo = line.indexOf("\"\"acc_no\"\":\"\"");

        String linePhone = line.substring(index + 12, index + 23);
        String lineName = line.substring(indexName + 15, indexName + 18);
        String createTime = line.substring(createTimeIndex + 18, createTimeIndex + 37);
        String amount = line.substring(indexAmount + 11, indexAmount + 18);
        String accNo = line.substring(indexAccNo + 13, indexAccNo + 32);
        accNo = accNo.replaceAll("\"", "");
        accNo = accNo.replaceAll(",", "");

        amount = amount.replaceAll("\"", "");
        amount = amount.replaceAll(",", "");
//          lineName =db.NtNotifyAppMsgs.find({modleType:'XPM001',lastTime:{$gt:1536988172000}});
        lineName = lineName.replaceAll("\"", "");
        System.out.println(i + ":" + linePhone + lineName + createTime + amount + "：" + accNo);

        QueryWrapper<TUserNew> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TUserNew::getUPhone, linePhone);

        TUserNew user = new TUserNew();
        user.setUPhone(linePhone);
        user.setUName(lineName);
        LocalDateTime ldt = LocalDateTime.parse(createTime,df);
        user.setCreateTime(ldt);
        user.setCTime(LocalDateTime.now());
        user.setCTimeLong(System.currentTimeMillis());
        user.setAmount(new BigDecimal(amount));
        user.setBankNo(accNo);
        String userStr = gson.toJson(user);
        tUserRecords.add(gson.fromJson(userStr, TUserRecord.class));
        TUserNew userSel = mptUserNewService.getOne(queryWrapper);
        if (userSel != null && userSel.getHsid() != null) {
          continue;
        }
        if (isExits.containsKey(linePhone)) {
          continue;
        }
        users.add(user);
        isExits.put(linePhone, "1");
      }
      if (users.size() > 0) {
        count = count + users.size();
        mptUserNewService.saveBatch(users);
        users.clear();
      }
      if (tUserRecords.size() > 0) {
        tUserRecordService.saveBatch(tUserRecords);
        users.clear();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    log.info("---------count:" + count + "---------sumCount:" + sumCount);
  }

  /**
   * 同步信息
   */
  @Override
  public Long syncUser(String date) {
    Long resultNum = 0L;
    int day = LocalDateTime.now().getDayOfYear();
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime ldt = LocalDateTime.parse(date + " 00:00:00",df);
    LocalDateTime ldg = ldt.plusDays(1L);

    while (day > ldg.getDayOfYear()) {
      QueryWrapper<TUserRecord> queryWrapper = new QueryWrapper<>();
      queryWrapper.lambda().between(TUserRecord::getCreateTime, ldt, ldg);
      List<TUserRecord> tUserRecordList = tUserRecordService.list(queryWrapper);
      resultNum = resultNum + tUserRecordList.size();
      log.info(ldg.toString() + "个数：" + tUserRecordList.size());
      ldt = ldg;
      ldg = ldt.plusDays(1L);

      List<UserWei> userWeis = new ArrayList<>();
      Map<String, String> isExits = new HashMap<>();
      int i = 0;
      for (TUserRecord td:tUserRecordList
      ) {
        i++;
        QueryWrapper<UserWei> userWeiQueryWrapper = new QueryWrapper<>();
        userWeiQueryWrapper.lambda().eq(UserWei::getUPhone, td.getUPhone());
        UserWei userWei = mptUserWeiService.getOne(userWeiQueryWrapper);
        if (userWei == null && !isExits.containsKey(td.getUPhone())) {
          String tdStr = gson.toJson(td);
          UserWei newUserWei = gson.fromJson(tdStr, UserWei.class);
          newUserWei.setHsid(null);
          userWeis.add(newUserWei);
        }else{
//          log.info(td.getUPhone() + "-------" + i);
        }
        isExits.put(td.getUPhone(), "1");
      }
      if (userWeis.size() > 0) {
        log.info(ldg.toString() + "insert个数：" + userWeis.size() + "isExits个数：" + isExits.size());
        mptUserWeiService.saveBatch(userWeis);
        try {
          Thread.sleep(5000L);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

    }
    return resultNum;
  }
}