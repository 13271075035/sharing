

package com.sharing.common.constant;

/**
 * 常量
 *
 * @author tecsmile
 */
public interface Constant {
    /**
     * 成功
     */
    int SUCCESS = 1;
    /**
     * 失败
     */
    int FAIL = 0;
    /**
     * 删除标志
     */
    int DELETE = 1;
    int FDELETE = 0;
    //查询审核数据类型（1为管理员查询，2为个人申请记录查询）
    int QUERYADMINTYPE=1;
    int QUERYPERSONALTYPE=1;
    /**
     * OK
     */
    String OK = "OK";
    /**
     * 用户标识
     */
    String USER_KEY = "userId";
    /**
     * 菜单根节点标识
     */
    Long MENU_ROOT = 0L;
    /**
     * 部门根节点标识
     */
    Long DEPT_ROOT = 0L;
    /**
     * 数据字典根节点标识
     */
    Long DICT_ROOT = 0L;
    /**
     * 升序
     */
    String ASC = "asc";
    /**
     * 降序
     */
    String DESC = "desc";
    /**
     * 支行名
     */
    String  branchName="branchName";
    /**
     * 创建时间字段名
     */
    String CREATE_DATE = "create_date";
    String CREATE_TIME = "create_time";

    /**
     * 创建时间字段名
     */
    String ID = "id";

    /**
     * 数据权限过滤
     */
    String SQL_FILTER = "sqlFilter";

    /**
     * 当前页码
     */
    String PAGE = "page";
    /**
     * 每页显示记录数
     */
    String LIMIT = "limit";
    /**
     * 排序字段
     */
    String ORDER_FIELD = "orderField";
    /**
     * 排序方式
     */
    String ORDER = "order";
    /**
     * token header
     */
    String TOKEN_HEADER = "token";

    /**
     * 云存储配置KEY
     */
    String CLOUD_STORAGE_CONFIG_KEY = "CLOUD_STORAGE_CONFIG_KEY";
    /**
     * 短信配置KEY
     */
    String SMS_CONFIG_KEY = "SMS_CONFIG_KEY";
    /**
     * 邮件配置KEY
     */
    String MAIL_CONFIG_KEY = "MAIL_CONFIG_KEY";

    /**
     * TOKEN key
     */
    public static final String REDIS_TOKEN_KEY = "token_";

    /**
     * redis 中保存token 的过期时间 60*60*24=86400 秒
     */
    public static final int REDIS_TOKEN_TIMEOUT = 86400;

    /**
     * 用户 redis key
     */
    public static final String REDIS_USER_KEY = "user_";
    /**
     * 用户
     */
    public static final String REDIS_PERMISSION_KEY = "permission_";

    /**
     * 操作类型 (1修改2删除3新增)
     */
    enum operationType {

        update(1),
        delete(2),
        add(3);


        private int value;

        operationType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

     /**
     * 处理状态(1待审批2驳回3通过)
     */
    enum ProcessingStatus {
         wait(1),
         reject(2),
         pass(3);
        private int value;
        ProcessingStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }


    }

    /**
     * 定时任务状态
     */
    enum ScheduleStatus {
        /**
         * 暂停
         */
        PAUSE(0),
        /**
         * 正常
         */
        NORMAL(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3),
        /**
         * FASTDFS
         */
        FASTDFS(4),
        /**
         * 本地
         */
        LOCAL(5);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 短信服务商
     */
    enum SmsService {
        /**
         * 阿里云
         */
        ALIYUN(1),
        /**
         * 腾讯云
         */
        QCLOUD(2);

        private int value;

        SmsService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    String TRUE = "true";

    String FALSE = "false";
}

