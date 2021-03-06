package com.overtech.lenovo.entity.tasklist.taskbean;

/**
 * 工单详情
 *
 * @author Overtech Will
 */
public class Task {
    public long appointment_home_mills;//工程师预约上门时间
    public String repair_person_contact_information;//门店报修人员电话
    public String isUrgent;//是否紧急
    public String issue_resume;//任务单描述
    public String issue_type;//任务单分类
    public double latitude;//任务单对应门店经度
    public double longitude;//任务单对应门店纬度
    public String remarks;//任务单备注
    public String taskLogo;//任务单所属项目图标
    public String taskType;//  工单类型
    public String workorder_code;//工单单号
    public String workorder_create_datetime;//任务单发布时间

    public String title;//工单通知消息标题
    public String content;//工单通知消息内容
    public String username;//工单通知消息发送人
    public String datetime;//工单通知消息时间

    public String notification_item_time;//消息通知时间
    public String notification_item_who;//消息通知发送者
    public String notification_item_content;//通知内容
}
