package com.miaoshaproject.socket;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;


public class HttpApi {
    private static final Logger LOG = Logger.getLogger(HttpApi.class);
    private static HttpApi mInstance;
    private static String URL = "http://192.168.1.123:88/";

    public static HttpApi getInstance() {
        if (mInstance == null) {
            mInstance = new HttpApi();
            HttpUtil.init();
        }
        return mInstance;
    }

    // 参数组合并请求
    //GET方法
    public JSONObject getResult(String params) {
        String reqString = URL + params;
        LOG.info("衡阳市中医医院入参:" + reqString);
        String resString = HttpUtil.getUrlContent(reqString);
        LOG.info("衡阳市中医医院出参:" + resString);
        return JSONObject.fromObject(resString);
    }
    //POST方法
    public JSONObject postResult(String params, String body) {
        String reqString = URL + params;
        LOG.info("衡阳市中医医院入参:" + reqString);
        String resString = HttpUtil.postForJson(reqString,body);
        LOG.info("衡阳市中医医院出参:" + resString);
        return JSONObject.fromObject(resString);
    }
    //PUT方法
    public JSONObject putResult(String params, String body) {
        String reqString = URL + params;
        LOG.info("衡阳市中医医院入参:" + reqString);
        String resString = HttpUtil.putForJson(reqString,body);
        LOG.info("衡阳市中医医院出参:" + resString);
        return JSONObject.fromObject(resString);
    }
    //DELETE方法
    public JSONObject deleteResult(String params, String body) {
        String reqString = URL + params;
        LOG.info("衡阳市中医医院入参:" + reqString);
        String resString = HttpUtil.getUrlContent(reqString);
        LOG.info("衡阳市中医医院出参:" + resString);
        return JSONObject.fromObject(resString);
    }

    // 2.1.1.获取所有用户信息
    public JSONObject GETALLUSER() {
        return getResult("user");
    }

    // 2.1.2.修改用户信息
    public JSONObject PUTUSER(String uid, String passwd, String description) {
        if(passwd==null&&description==null) {
            return putResult("user/?uid="+uid,null);
        }else if (passwd==null&&description!=null){
            return putResult("user/?uid="+uid + "&description=" + description,null);
        }else if(passwd!=null&&description==null){
            return putResult("user/?uid="+uid + "&passwd=" + passwd,null);
        }else {
            return putResult("user/?uid="+uid + "&passwd=" + passwd + "&description"+ description,null);
        }

    }

    // 2.1.3.创建新用户
    public JSONObject POSTNEWUSER(String username, String passwd, String description, String groupid) {
        return postResult("user/?username="+username+"&passwd="+passwd+"&description="+description+"&groupid="+groupid,null);
    }

    // 2.1.4.删除用户
    public JSONObject DELETEUSER(String uid) {
        return deleteResult("user/?uid="+uid,null);
    }

    // 2.1.5.获取在线用户列表
    public JSONObject GETONLINE() {
        return getResult("user/activeUsers");
    }

    // 2.1.6. 获取用户属组信息
    public JSONObject GETMESSAGE(String username) {
        return getResult("user/attribute?username="+username);
    }

    // 2.1.7. 获取指定ID用户信息
    public JSONObject GETUSERBYID(String uid) {
        return getResult("user/byid?uid="+uid);
    }

    // 2.1.8. 登录保活   定期更新token，防止token过期，需要客户端每45秒发送一次
    public JSONObject LOGINALINE() {
        return getResult("user/keepAlive");
    }

    // 2.1.9. 用户登录
    public JSONObject LOGIN(String username, String password) {
        return getResult("/user/login?username="+username+"&password="+password);
    }

    // 2.1.10. 退出登录
    public JSONObject OUTLINE() {
        return getResult("user/logout");
    }

    // 2.1.11. 获取无操作超时时间
    public JSONObject MOOPSTime() {
        return getResult("user/noOpsTime");
    }

    // 2.1.12. 设置无操作超时时间
    public JSONObject PUTMZYS(String timeout) {
        return putResult("user/noOpsTime?timeout="+timeout,null);
    }

    // 2.2.1. 获取用户组信息
    public JSONObject GETGROUP() {
        return getResult("group/");
    }
    // 2.2.2. 修改用户组信息
    public JSONObject PUTGROUP(String gid, String body) {
        return putResult("group/?gid="+gid,body);
    }

    // 2.2.3. 创建用户组
    public JSONObject CREATEGROUP(String groupname, String description, String body) {
        return postResult("group/?groupname="+groupname + "&description=" + description,body);
    }

    // 2.2.4. 删除用户组
    public JSONObject DELETEGROUP(String gid) {
        return deleteResult("group/?gid="+gid,null);
    }

    // 2.2.5. 获取指定ID用户组信息
    public JSONObject GETGROUPBYID(String gid) {
        return getResult("group/byid?gid="+gid);
    }

    // 2.3.1. 获取权限列表
    public JSONObject GETPOWER() {
        return getResult("authority/");
    }

    // 2.4.1. 校验密码找回问题
    public JSONObject GETPASQUE(String username, String newpass, String body) {
        return putResult("security/check?username="+username+"&newpass="+newpass,body);
    }

    // 2.4.2. 获取密码找回问题
    public JSONObject GETPASQUE1(String username) {
        return getResult("security/question?username="+username);
    }

    // 2.4.3. 设置密码找回问题
    public JSONObject SETPAS(String username, String body) {
        return putResult("security/question?username="+username,body);
    }

    // 2.4.4. 删除密码找回问题
    public JSONObject DELETEQUE(String username) {
        return deleteResult("security/question?username="+username,null);
    }

    // 2.5.1. 订阅结构化数据推送服务
    public JSONObject SERVER(String chns) {
        return getResult("subscription/analyseResults?chns="+chns);
    }

    // 2.5.2. 订阅预警数据推送服务
    public JSONObject SERVER1(String chns) {
        return getResult("subscription/warningResults?chns=0");
    }

    // 2.7.1. 车辆属性-品牌车系
    public JSONObject CAR1(String family) {
        return getResult("carStyle/brand?family="+family);
    }

    // 2.7.2. 车辆属性-类别
    public JSONObject CAR2() {
        return getResult("carStyle/classify");
    }

    // 2.7.3. 车辆属性-颜色
    public JSONObject CAR3() {
        return getResult("carStyle/color");
    }

    // 2.7.4. 车辆属性-品牌
    public JSONObject CAR4() {
        return getResult("carStyle/family");
    }

    // 2.7.5. 车辆属性-车辆方位
    public JSONObject CAR5() {
        return getResult("carStyle/orientation");
    }

    // 2.7.6. 车辆属性-车牌颜色
    public JSONObject CAR6() {
        return getResult("carStyle/plateColor");
    }
    // 2.7.7. 车辆属性-车牌标识
    public JSONObject CAR7() {
        return getResult("carStyle/plateFlag");
    }
    // 2.7.8. 车辆属性-车牌类型
    public JSONObject CAR8() {
        return getResult("carStyle/plateType");
    }
    // 2.7.9. 车辆属性-子类别
    public JSONObject CAR9() {
        return getResult("carStyle/subclass");
    }

    // 2.7.10. 车辆属性-年款型号
    public JSONObject CAR10(String family, String brand) {
        return getResult("carStyle/type?family="+family+"&brand="+brand);
    }
    // 2.8.1. 三轮车属性-顶棚颜色
    public JSONObject THREECAR1() {
        return getResult("tricycleStyle/roofcolor");
    }
    // 2.9.1. 人体属性-年龄
    public JSONObject AGE() {
        return getResult("personStyle/age");
    }
    // 2.9.2. 人体属性-抱小孩
    public JSONObject KID() {
        return getResult("personStyle/baby");
    }
    // 2.9.3. 人体属性-拎东西
    public JSONObject THINGS() {
        return getResult("personStyle/bag");
    }
    // 2.9.4. 人体属性-下装款式
    public JSONObject KUZI() {
        return getResult("personStyle/bottom");
    }
    // 2.9.5. 人体属性-下装颜色
    public JSONObject KUZICOLOR() {
        return getResult("personStyle/bottomColor");
    }
    // 2.9.6. 人体属性-眼镜
    public JSONObject YANJING() {
        return getResult("personStyle/glasses");
    }
    // 2.9.7. 人体属性-发型
    public JSONObject HAIR() {
        return getResult("personStyle/hair");
    }
    //2.9.8. 人体属性-帽子
    public JSONObject HAT() {
        return getResult("personStyle/hat");
    }
    // 2.9.9. 人体属性-双肩包
    public JSONObject BAG2() {
        return getResult("personStyle/knapsack");
    }
    // 2.9.10. 人体属性-口罩
    public JSONObject MASK() {
        return getResult("personStyle/mask");
    }
    // 2.9.11. 人体属性-斜挎包
    public JSONObject BAG1() {
        return getResult("personStyle/messengerBag");
    }

    // 2.9.12. 人体属性-行走方向
    public JSONObject SEX() {
        return getResult("personStyle/sex");
    }
    // 2.9.14. 人体属性-单肩包
    public JSONObject shoulderBag() {
        return getResult("personStyle/shoulderBag");
    }
    // 2.9.15. 人体属性-是否撑伞
    public JSONObject umbrella() {
        return getResult("personStyle/umbrella");
    }
    // 2.9.16. 人体属性-上装款式
    public JSONObject upper() {
        return getResult("personStyle/upper");
    }
    // 2.9.17. 人体属性-上衣颜色
    public JSONObject upperColor() {
        return getResult("personStyle/upperColor");
    }
    // 2.9.18. 人体属性-上装纹理
    public JSONObject upperTexture() {
        return getResult("personStyle/upperTexture");
    }
    // 2.11.1. 获取设备授权状态信息
    public JSONObject status() {
        return getResult("licence/status");
    }
    // 2.11.2. 导出设备唯一识别码
    public JSONObject uid1() {
        return getResult("licence/uid");
    }
    // 2.11.3. 导出设备唯一识别码到U盘
    public JSONObject uidto(String filepath) {
        return getResult("licence/uid/to?filepath="+filepath);
    }

    // 2.12.1. 结构化检索-两轮车
    public JSONObject bikes(String offset,String count,String starttime,String endtime) {
        return getResult("search/bikes?offset="+offset+"&count="+count+"&starttime="+starttime+"&endtime="+endtime);
    }

    // 2.12.2. 查询指定ID目标信息-两轮车
    public JSONObject bikesbyid(String ids) {
        return getResult("search/byid/bikes?ids="+ids);
    }
    // 2.12.3. 查询指定ID目标信息-人脸
    public JSONObject faces(String ids) {
        return getResult("search/byid/faces?ids="+ids);
    }
    // 2.12.4. 查询指定ID目标信息-行人
    public JSONObject pedestrians(String ids) {
        return getResult("search/byid/pedestrians?ids="+ids);
    }
    // 2.12.5. 查询指定ID目标信息-三轮车
    public JSONObject tricycles(String ids) {
        return getResult("search/byid/tricycles?ids="+ids);
    }
    // 2.12.6. 查询指定ID目标信息-车辆
    public JSONObject vehicles(String ids) {
        return getResult("search/byid/vehicles?ids="+ids);
    }
    // 2.12.7. 结构化检索-人脸
    public JSONObject facesbyid(String offset,String count, String starttime,String endtime) {
        return getResult("search/faces?offset="+offset+"&count="+count+"&starttime="+starttime+"&endtime="+endtime);
    }
    // 2.12.8. 结构化检索-行人
    public JSONObject pedestrians1(String offset,String count, String starttime,String endtime) {
        return getResult("search/pedestrians?offset="+offset+"&count="+count+"&starttime="+starttime+"&endtime="+endtime);
    }
    // 2.12.9. 结构化检索-三轮车
    public JSONObject tricycles1(String offset,String count, String starttime,String endtime) {
        return getResult("search/tricycles?offset="+offset+"&count="+count+"&starttime="+starttime+"&endtime="+endtime);
    }
    // 2.12.10. 结构化检索-车辆
    public JSONObject vehicles1(String offset,String count, String starttime,String endtime) {
        return getResult("search/vehicles?offset="+offset+"&count="+count+"&starttime="+starttime+"&endtime="+endtime);
    }
    // 2.13.1. 目标流量统计-目标分类统计
    public JSONObject flow(String starttime,String endtime,String chns) {
        return getResult("flow/?starttime="+starttime+"&endtime="+endtime+"&chns="+chns);
    }
    // 2.13.2. 目标流量统计-行人属性统计
    public JSONObject pedestrian(String starttime,String endtime,String chns) {
        return getResult("pedestrian/?starttime="+starttime+"&endtime="+endtime+"&chns="+chns);
    }

    // 2.13.3. 目标流量统计-属性属性统计
    public JSONObject vehicle(String starttime,String endtime,String chns) {
        return getResult("vehicle/?starttime="+starttime+"&endtime="+endtime+"&chns="+chns);
    }
    // 2.14.1. 下载目标图片
    public JSONObject picture(String path) {
        return getResult("picture/?path="+path);
    }
    // 2.14.2. 下载目标图片到U盘
    public JSONObject pictureto(String picpath,String dstpath) {
        return getResult("picture/to?picpath="+picpath+"&dstpath="+dstpath);
    }
    // 2.15.1. 查询预警布控-车辆属性
    public JSONObject vehicleAttr(String offset,String count,String starttime,String endtime) {
        return getResult("warning/vehicleAttr/?offset="+offset+"&count="+count+"&starttime="+starttime+"&endtime="+endtime);
    }
    // 2.15.2. 修改预警布控-车辆属性
    public JSONObject vehicleAttr(String id,String name,String reason) {
        return putResult("warning/vehicleAttr/?id="+id+"&name="+name+"&reason="+reason,null);
    }
    // 2.15.3. 新增预警布控-车辆属性
    public JSONObject vehicleAttr1(String name,String reason) {
        return postResult("warning/vehicleAttr/?name="+name+"&reason="+reason,null);
    }
    // 2.15.4. 删除预警布控-车辆属性
    public JSONObject vehicleAttr2(String id) {
        return deleteResult("vehicle/?id="+id,null);
    }

    // 2.15.5. 查询指定ID预警布控-车辆属性
    public JSONObject vehicleAttrbyid(String ids) {
        return getResult("warning/vehicleAttr/byid?ids="+ids);
    }
    // 2.16.1. 查询预警布控-车牌
    public JSONObject vehicleLic(String offset,String count,String starttime,String endtime) {
        return getResult("warning/vehicleLic/?offset="+offset+"&count="+count+"&starttime=&"+starttime+"endtime="+endtime);
    }
    // 2.16.2. 修改预警布控-车牌
    public JSONObject vehicleLic1(String id,String name,String resson) {
        return putResult("warning/vehicleLic/?id="+id+"&name="+name+"&reason="+resson,null);
    }
    // 2.16.3. 新增预警布控-车牌
    public JSONObject vehicleLic2(String name,String reason) {
        return postResult("warning/vehicleLic/?name="+name+"&reason="+reason,null);
    }
    // 2.16.4. 删除预警布控-车牌
    public JSONObject vehicleLic3(String ids) {
        return deleteResult("warning/vehicleLic/?id="+ids,null);
    }
    //2.16.5. 查询指定ID预警布控-车牌
    public JSONObject vehicleLic4(String ids) {
        return getResult("warning/vehicleLic/byid?ids="+ids);
    }
    // 2.17.1. 查询预警布控-车辆图片
    public JSONObject vehiclePic1(String offset,String count,String starttime,String endtime) {
        return getResult("warning/vehiclePic/?offset="+offset+"&count="+count+"&starttime="+starttime+"&endtime="+endtime);
    }
    // 2.17.2. 修改预警布控-车辆图片
    public JSONObject vehiclePic2(String id,String name,String reason,String desc,String threshold) {
        return putResult("warning/vehiclePic/?id="+id+"&name="+name+"&reason="+reason+"&desc="+desc+"&threshold="+threshold,null);
    }
    // 2.17.3. 新增预警布控-车辆图片
    public JSONObject vehiclePic3(String name,String reason,String desc,String threshold,String file) {
        return postResult("warning/vehiclePic/?name="+name+"&reason="+reason+"&desc="+desc+"&threshold="+threshold,file);
    }
    // 2.17.4. 删除预警布控-车辆图片
    public JSONObject vehiclePic4(String ids) {
        return deleteResult("warning/vehiclePic/?id="+ids,null);
    }
    // 2.17.5. 查询指定ID预警布控-车辆图片
    public JSONObject vehiclePic5(String ids) {
        return getResult("warning/vehiclePic/byid?ids="+ids);
    }
    // 2.17.6. 获取指定ID预警布控图片-车辆图片
    public JSONObject vehiclePic6(String ids) {
        return getResult("warning/vehiclePic/picture?id="+ids);
    }
    // 2.18.1. 查询预警布控-三轮车图片
    public JSONObject tricyclePic1(String offset,String count,String starttime,String endtime) {
        return getResult("warning/tricyclePic/?offset="+offset+"&count="+count+"&starttime="+starttime+"&endtime="+endtime);
    }

    // 2.18.2. 修改预警布控-三轮车图片
    public JSONObject tricyclePic2(String id,String name,String reason,String desc,String threshold) {
        return putResult("warning/tricyclePic/?id="+id+"&name="+name+"&reason="+reason+"&desc="+desc+"&threshold="+threshold,null);
    }
    // 2.18.3. 新增预警布控-三轮车图片
    public JSONObject tricyclePic3(String name,String reason,String desc,String threshold,String file) {
        return postResult("warning/tricyclePic/?name="+name+"&reason="+reason+"&desc="+desc+"&threshold="+threshold,file);
    }
    // 2.18.4. 删除预警布控-三轮车图片
    public JSONObject tricyclePic4(String ids) {
        return deleteResult("warning/tricyclePic/?id="+ids,null);
    }
    // 2.18.5. 查询指定ID预警布控-三轮车图片
    public JSONObject tricyclePic5(String ids) {
        return getResult("warning/tricyclePic/byid?ids="+ids);
    }
    // 2.18.6. 获取指定ID预警布控图片-三轮车图片
    public JSONObject tricyclePic6(String ids) {
        return getResult("warning/tricyclePic/picture?id="+ids);
    }
    // 2.19.1. 查询预警布控-两轮车属性
    public JSONObject bikeAttr1(String offset,String count,String starttime,String endtime) {
        return getResult("warning/bikeAttr/?offset="+offset+"&count="+count+"&starttime="+starttime+"&endtime="+endtime);
    }
    // 2.19.2. 修改预警布控-两轮车属性
    public JSONObject bikeAttr2(String id,String name,String reason) {
        return putResult("warning/bikeAttr/?id="+id+"&name="+name+"&reason="+reason,null);
    }
    // 2.19.3. 新增预警布控-两轮车属性
    public JSONObject bikeAttr3(String name,String reason) {
        return postResult("warning/bikeAttr/?name="+name+"&reason="+reason,null);
    }
    // 2.19.4. 删除预警布控-两轮车属性
    public JSONObject bikeAttr4(String ids) {
        return deleteResult("warning/bikeAttr/?id="+ids,null);
    }
    // 2.19.5. 查询指定ID预警布控-两轮车属性
    public JSONObject bikeAttr5(String ids) {
        return getResult("warning/bikeAttr/byid?ids="+ids);
    }
    // 2.20.1. 查询预警布控-两轮车图片
    public JSONObject bikePic1(String offset,String count,String starttime,String endtime) {
        return getResult("warning/bikePic/?offset="+offset+"&count="+count+"&starttime="+starttime+"&endtime="+endtime);
    }
    // 2.20.2. 修改预警布控-两轮车图片
    public JSONObject bikePic12(String id,String name,String reason,String desc,String threshold) {
        return putResult("warning/bikePic/?id="+id+"&name="+name+"&reason="+reason+"&desc="+desc+"&threshold="+threshold,null);
    }
    // 2.20.3. 新增预警布控-两轮车图片
    public JSONObject bikePic13(String name,String reason,String desc,String threshold,String file) {
        return postResult("warning/bikePic/?name="+name+"&reason="+reason+"&desc="+desc+"&threshold="+threshold,file);
    }
    // 2.20.4. 删除预警布控-两轮车图片
    public JSONObject bikePic14(String ids) {
        return deleteResult("warning/bikePic/?id="+ids,null);
    }
    // 2.20.5. 查询指定ID预警布控-两轮车图片
    public JSONObject bikePic15(String ids) {
        return getResult("warning/bikePic/byid?ids="+ids);
    }
    // 2.20.6. 获取指定ID预警布控图片-两轮车图片
    public JSONObject bikePic16(String ids) {
        return getResult("warning/bikePic/picture?id="+ids);
    }
    // 2.21.1. 查询预警布控-行人属性
    public JSONObject pedestrainAttr1(String offset,String count,String starttime,String endtime) {
        return getResult("warning/pedestrainAttr/?offset="+offset+"&count="+count+"&starttime="+starttime+"&endtime="+endtime);
    }
    // 2.21.2. 修改预警布控-行人属性
    public JSONObject pedestrainAttr2(String id,String name,String reason) {
        return putResult("warning/pedestrainAttr/?id="+id+"&name="+name+"&reason="+reason,null);
    }
    // 2.21.3. 新增预警布控-行人属性
    public JSONObject pedestrainAttr3(String name,String reason) {
        return getResult("warning/pedestrainAttr/?name="+name+"&reason="+reason);
    }
    // 2.21.4. 删除预警布控-行人属性
    public JSONObject pedestrainAttr4(String ids) {
        return deleteResult("warning/pedestrainAttr/?id="+ids,null);
    }
    // 2.21.5. 查询指定ID预警布控-行人属性
    public JSONObject pedestrainAttr5(String ids) {
        return getResult("warning/pedestrainAttr/byid?ids="+ids);
    }
    // 2.22.1. 查询预警布控-行人图片
    public JSONObject pedestrainPic1(String offset,String count,String starttime,String endtime) {
        return getResult("warning/pedestrainPic/?offset="+offset+"&count="+count+"&starttime="+starttime+"&endtime="+endtime);
    }
    // 2.22.2. 修改预警布控-行人图片
    public JSONObject pedestrainPic2(String id,String name,String reason,String desc,String threshold) {
        return putResult("warning/pedestrainPic/?id="+id+"&name="+name+"&reason="+reason+"&desc="+desc+"&threshold="+threshold,null);
    }
    // 2.22.3. 新增预警布控-行人图片
    public JSONObject pedestrainPic3(String name,String reason,String desc,String threshold,String file) {
        return postResult("warning/pedestrainPic/?name="+name+"&reason="+reason+"&desc="+desc+"&threshold="+threshold,file);
    }

    // 2.22.4. 删除预警布控-行人图片
    public JSONObject pedestrainPic4(String ids) {
            return deleteResult("warning/pedestrainPic/?id="+ids,null);
    }
    // 2.22.5. 查询指定ID预警布控-行人图片
    public JSONObject pedestrainPic5(String ids) {
        return getResult("warning/pedestrainPic/byid?ids="+ids);
    }
    // 2.22.6. 获取指定ID预警布控图片-行人图片
    public JSONObject pedestrainPic6(String ids) {
        return getResult("warning/pedestrainPic/picture?id="+ids);
    }
    // 2.23.1. 查询预警布控-人脸库
    public JSONObject facelib1(String offset,String count,String starttime,String endtime) {
        return getResult("warning/facelib/?offset="+offset+"&count="+count+"&starttime="+starttime+"&endtime="+endtime);
    }
    // 2.23.2. 修改预警布控-人脸库
    public JSONObject facelib2(String id,String name,String reason,String total) {
            return putResult("warning/facelib/?id="+id+"&name="+name+"&reason="+reason+"&total="+total,null);
    }
    // 2.23.3. 新增预警布控-人脸库
    public JSONObject facelib3(String name,String reason) {
        return postResult("warning/facelib/?name="+name+"&reason="+reason,null);
    }
    // 2.23.4. 删除预警布控-人脸库
    public JSONObject facelib4(String ids) {
            return deleteResult("warning/facelib/?id="+ids,null);
    }
    // 2.24.1. 查询预警布控-人脸图片
    public JSONObject facePic1(String offset,String count,String starttime,String endtime,String facelibid) {
        return getResult("warning/facePic/?offset="+offset+"&count="+count+"&starttime="+starttime+"&endtime="+endtime+"&facelibid="+facelibid);
    }
    // 2.24.2. 修改预警布控-人脸图片
    public JSONObject facePic2(String id,String libid,String name) {
            return putResult("warning/facePic/?id="+id+"&libid="+libid+"&name="+name,null);
    }
    // 2.24.3. 新增预警布控-人脸图片
    public JSONObject facePic3(String libid,String name,String file) {
            return deleteResult("warning/facePic/?libid="+libid+"&name="+name,file);
    }
    // 2.24.4. 通过id删除预警布控-人脸图片
    public JSONObject facePic4(String ids,String libid) {
            return deleteResult("warning/facePic/?id="+ids+"&libid="+libid,null);
    }
    // 2.24.5. 查询指定ID预警布控-人脸图片
    public JSONObject facePic5(String ids) {
            return getResult("warning/facePic/byid/?id="+ids);
    }
    // 2.24.6. 获取指定ID预警布控图片-人脸图片
    public JSONObject facePic6(String ids) {
            return getResult("warning/facePic/picture/?id="+ids);
    }
    // 2.24.7. 通过证件号删除预警布控-人脸图片
    public JSONObject facePic7(String libid,String idNumber) {
            return deleteResult("warning/facePic/pid?idNumber="+idNumber+"&libid="+libid,null);
    }
    // 2.25.1. 查询预警布控计划
    public JSONObject schedule1(String warntype,String warnid) {
            return getResult("warning/schedule/?warntype="+warntype+"&warnid="+warnid);
    }
    // 2.25.2. 修改预警布控计划
    public JSONObject schedule2(String id,String schedule) {
            return putResult("warning/schedule/?id="+id,schedule);
    }
    // 2.25.3. 新增预警布控计划
    public JSONObject schedule3(String warntype,String warnid,String schedule) {
            return postResult("warning/schedule/?warntype="+warntype+"&warnid="+warnid,schedule);
    }
    // 2.25.4. 删除预警布控计划
    public JSONObject schedule4(String id) {
            return deleteResult("warning/schedule/?id="+id,null);
    }
    // 2.26.4. 获取预警结果分类信息
    public JSONObject history4() {
            return getResult("warning/history/warnType");
    }
    // 2.27.1. 上传本地目标图片-车辆
    public JSONObject localcar1(String path) {
            return postResult("similarVehicle/local?path="+path,null);
    }
    // 2.27.3. 上传远程目标图片-车辆
    public JSONObject localcar3(String file) {
            return postResult("similarVehicle/remote",file);
    }
    // 2.27.2. 以图搜图进度
    public JSONObject localcar2() {
            return getResult("similarVehicle/progress");
    }
    // 2.27.4. 以图搜图分析结果
    public JSONObject localcar4(String offset,String count) {
            return getResult("similarVehicle/result?offset="+offset+"&count="+count);
    }
    // 2.27.5. 开始以图搜图
    public JSONObject localcar5(String starttime,String endtime,String chns,String threshold) {
        return postResult("similarVehicle/start?starttime="+starttime+"&endtime="+endtime+"&chns="+chns+"&threshold="+threshold,null);
    }
    // 2.27.6. 停止以图搜图
    public JSONObject localcar6() {
            return postResult("similarVehicle/stop",null);
    }
    // 2.28.1. 上传本地目标图片-三轮车
    public JSONObject similarTricycle1(String path) {
        return postResult("similarTricycle/local?path="+path,null);
    }
    // 2.28.2. 以图搜图进度
    public JSONObject similarTricycle2() {
        return getResult("similarTricycle/progress");
    }
    // 2.28.3. 上传远程目标图片-三轮车
    public JSONObject similarTricycle3(String file) {
        return postResult("similarTricycle/remote",file);
    }
    // 2.28.4. 以图搜图分析结果
    public JSONObject similarTricycle4(String offset,String count) {
        return getResult("similarTricycle/result?offset="+offset+"&count="+count);
    }
    // 2.28.6. 停止以图搜图
    public JSONObject similarTricycle6() {
        return postResult("similarTricycle/stop",null);
    }
    // 2.28.5. 开始以图搜图
    public JSONObject similarTricycle5(String starttime,String endtime,String chns,String threshold) {
        return postResult("similarTricycle/start?starttime="+starttime+"&endtime="+endtime+"&chns="+chns+"&threshold="+threshold,null);
    }
    // 2.29.1. 上传本地目标图片-两轮车
    public JSONObject similarBike1(String path) {
        return postResult("similarBike/local?path="+path,null);
    }
    // 2.29.2. 以图搜图进度
    public JSONObject similarBike2() {
        return getResult("similarBike/progress");
    }
    // 2.29.3. 上传远程目标图片-两轮车
    public JSONObject similarBike3(String file) {
        return postResult("similarBike/remote",file);
    }
    // 2.29.4. 以图搜图分析结果
    public JSONObject similarBike4(String offset,String count) {
        return getResult("similarBike/result?offset="+offset+"&count="+count);
    }
    // 2.29.5. 开始以图搜图
    public JSONObject similarBike5(String starttime,String endtime,String chns,String threshold) {
        return postResult("similarBike/start?starttime="+starttime+"&endtime="+endtime+"&chns="+chns+"&threshold="+threshold,null);
    }
    // 2.29.6. 停止以图搜图
    public JSONObject similarBike6() {
        return postResult("similarBike/stop",null);
    }
    // 2.30.1. 上传本地目标图片-行人
    public JSONObject similarPedestrain1(String path) {
        return postResult("similarPedestrain/local?path="+path,null);
    }
    // 2.30.2. 以图搜图进度
    public JSONObject similarPedestrain2() {
        return getResult("similarPedestrain/progress");
    }
    // 2.30.3. 上传远程目标图片-行人
    public JSONObject similarPedestrai3(String file) {
        return postResult("similarPedestrain/remote",file);
    }
    // 2.30.4. 以图搜图分析结果
    public JSONObject similarPedestrain4(String offset,String count) {
        return getResult("similarPedestrain/result?offset="+offset+"&count="+count);
    }
    // 2.30.5. 开始以图搜图
    public JSONObject similarPedestrain5(String starttime,String endtime,String chns,String threshold) {
        return postResult("similarPedestrain/start?starttime="+starttime+"&endtime="+endtime+"&chns="+chns+"&threshold="+threshold,null);
    }
    // 2.30.6. 停止以图搜图
    public JSONObject similarPedestrain6() {
        return postResult("similarPedestrain/stop",null);
    }
    // 2.31.1. 上传本地目标图片-人脸
    public JSONObject similarFace1(String path) {
        return postResult("similarFace/local?path="+path,null);
    }
    // 2.31.2. 以图搜图进度
    public JSONObject similarFace2() {
        return getResult("similarFace/progress");
    }
    // 2.31.3. 上传远程目标图片-人脸
    public JSONObject similarFace3(String file) {
        return postResult("similarFace/remote",file);
    }
    // 2.31.4. 以图搜图分析结果
    public JSONObject similarFace4(String offset,String count) {
        return getResult("similarFace/result?offset="+offset+"&count="+count);
    }
    // 2.31.5. 开始以图搜图
    public JSONObject similarFace5(String starttime,String endtime,String chns,String threshold) {
        return postResult("similarFace/start?starttime="+starttime+"&endtime="+endtime+"&chns="+chns+"&threshold="+threshold,null);
    }
    // 2.31.6. 停止以图搜图
    public JSONObject similarFace6() {
        return postResult("similarFace/stop",null);
    }
    // 2.32.1. 获取已添加设备列表
    public JSONObject camera1() {
            return getResult("camera/");
    }
    // 2.32.2. 修改已添加设备参数
    public JSONObject camera2(String content) {
            return putResult("camera/",content);
    }
    // 2.32.3. 删除已添加设备
    public JSONObject camera3(String channel) {
            return deleteResult("camera/?channel="+channel,null);
    }
    // 2.32.4. 删除所有已添加设备
    public JSONObject camera4() {
            return deleteResult("camera/all",null);
    }
    // 2.32.5. 自定义方式添加设备
    public JSONObject camera5(String channel,String ip,String rtsp) {
            return postResult("camera/custom?channel="+channel+"&ip="+ip,rtsp);
    }
    // 2.32.6. 获取设备在线状态
    public JSONObject camera6( ) {
            return getResult("camera/status");
    }
    // 2.33.1. 添加设备
    public JSONObject onvif1(String channel,String remoteChannel,String ip) {
            return postResult("onvif/camera?channel="+channel+"&remoteChannel="+remoteChannel+"&ip="+ip,null);
    }
    // 2.33.2. 获取搜索设备列表
    public JSONObject onvif2() {
            return getResult("onvif/discovery");
    }
    // 2.33.3. 设备搜索开关
    public JSONObject onvif3(String enable) {
            return getResult("onvif/discovery?enable="+enable);
    }
    // 2.34.1. 修改已添加设备参数
    public JSONObject gb281811(String content) {
            return putResult("gb28181/camera",content);
    }
    // 2.34.2. 添加设备
    public JSONObject gb281812(String channel,String ip) {
            return postResult("gb28181/camera?channel="+channel+"&ip="+ip,null);
    }
    // 2.34.3. 手动添加设备
    public JSONObject gb281813(String channel,String ip) {
        return postResult("gb28181/camera/manual?channel="+channel+"&ip="+ip,null);
    }
    // 2.34.5. 获取搜索设备列表
    public JSONObject gb281815(String mark) {
            return getResult("gb28181/discovery?mark="+mark);
    }

    // 2.35.1. 获取录像流RTSP Url地址
    public JSONObject playback1(String chns,String starttime,String endtime) {
            return getResult("stream/playback?chns="+chns+"&starttime="+starttime+"&endtime="+endtime);
    }
    // 2.35.2. 获取实时流RTSP Url地址
    public JSONObject playback2(String chns) {
            return getResult("stream/realtime?chns=0");
    }
    // 2.36.1. 重启系统
    public JSONObject reboot() {
            return putResult("system/reboot",null);
    }
    // 2.36.4. 设备关机
    public JSONObject shutdown() {
            return putResult("system/shutdown",null);
    }
    // 2.42.1. 系统日志-查询日志
    public JSONObject logType(String logType,String beginTime,String endTime,String count,String offset) {
            return getResult("log/?logType="+logType+"&beginTime="+beginTime+"&endTime="+endTime+"&count="+count+"&offset="+offset);
    }
    // 2.43.1. 获取所有硬盘信息
    public JSONObject infos() {
            return getResult("hd-disk/all/infos");
    }
    // 2.47.1. 查询录像文件
    public JSONObject videofiles(String chns,String streamtype,String lock,String starttime,String endtime) {
            return getResult("videofiles/?chns="+chns+"&streamtype="+streamtype+"&lock="+lock+"&starttime="+starttime+"&endtime="+endtime);
    }
    // 2.47.2. 查询录像文件-全部
    public JSONObject videofiles1(String offset) {
            return getResult("videofiles/all?offset="+offset);
    }
    // 2.53.1. 设置播放方向
    public JSONObject direct(String playBackChannel,String direct) {
            return putResult("playback/direct?playBackChannel="+playBackChannel+"&direct="+direct,null);
    }
    // 2.53.3. 开始回放
    public JSONObject play1(String playBackChannel,String channel,String subtype,String from,String to) {
            return putResult("playback/play?playBackChannel="+playBackChannel+"&channel="+channel+"&subtype="+subtype+"&from="+from+"&to="+to,null);
    }
    // 2.53.2. 暂停回放
    public JSONObject pause(String playBackChannel) {
            return putResult("playback/pause?playBackChannel="+playBackChannel,null);
    }
    /**
     * 工具类测试
     */
    public static void main(String[] args) {
        JSONObject a = HttpApi.getInstance().pause("11");
        System.out.println(a);
    }
}