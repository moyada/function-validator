package io.moyada.medivh.util;

/**
 * 构造元素
 * @author xueyikang
 * @since 1.0
 **/
public class Element {

    private Element() {
    }

    // 校验方法名配置
    private static final String METHOD_KEY = "medivh.method";
    private static final String DEFAULT_METHOD_NAME = "invalid0";
    public final static String METHOD_NAME = SystemUtil.getProperty(METHOD_KEY, DEFAULT_METHOD_NAME);


    // 临时变量名配置
    private static final String VARIABLE_KEY = "medivh.var";
    private static final String DEFAULT_VARIABLE_NAME = "mvar_0";
    public final static String LOCAL_VARIABLE = SystemUtil.getProperty(VARIABLE_KEY, DEFAULT_VARIABLE_NAME);


    // 异常信息头配置
    private static final String MESSAGE_KEY = "medivh.message";
    private static final String DEFAULT_MESSAGE = "Invalid input parameter";
    public final static String MESSAGE = SystemUtil.getProperty(MESSAGE_KEY, DEFAULT_MESSAGE);


    // 非空信息配置
    private static final String NULL_KEY = "medivh.info.null";
    private static final String DEFAULT_NULL_INFO = "is null";
    public final static String NULL_INFO = SystemUtil.getProperty(NULL_KEY, DEFAULT_NULL_INFO);


    // 相等信息配置
    private static final String EQUALS_KEY = "medivh.info.equals";
    private static final String DEFAULT_EQUALS_INFO = "cannot equals";
    public final static String EQUALS_INFO = SystemUtil.getProperty(EQUALS_KEY, DEFAULT_EQUALS_INFO);

    // 小于信息配置
    private static final String LESS_KEY = "medivh.info.less";
    private static final String DEFAULT_LESS_INFO = "less than";
    public final static String LESS_INFO = SystemUtil.getProperty(LESS_KEY, DEFAULT_LESS_INFO);


    // 大于信息配置
    private static final String GREAT_KEY = "medivh.info.great";
    private static final String DEFAULT_GREAT_INFO = "great than";
    public final static String GREAT_INFO = SystemUtil.getProperty(GREAT_KEY, DEFAULT_GREAT_INFO);

    public static final String ACTION_INFO = ", cause ";

    /**
     * 是否标记返回空值
     * @param values
     * @return
     */
    public final static boolean isReturnNull(String[] values) {
        if (null == values) {
            return true;
        }
        if (values.length == 0) {
            return true;
        }
        if (values.length != 1) {
            return false;
        }
        if (values[0].equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }
}
