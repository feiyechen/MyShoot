package cn.tedu.shoot;
/** 奖励接口 */
public interface Award {
    public int DOUBLE_FIRE = 0; //类型一：火力值
    public int LIFE = 1; //类型二：命
    /** 获取奖励类型 */
    public int getAwardType();
}
