package cn.tedu.shoot;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Bullet extends FlyingObject{

    /** 静态块，只加载一次，所有对象共享一份 */
    private static BufferedImage image;
    static {
        image = loadImage("bullet.png");
    }

    private int speed; //移动速度
    /** 构造方法 */
    public Bullet(int x,int y){
        super(8,14,x,y);
        speed = 3;
    }

    /** 移动方法 */
    public void step(){
        y-=speed;
    }

    /** 获取图片 */
    public BufferedImage getImage(){
        if(isLife()){ //活着
            return image;
        }else if(isDead()){ //死了
            state = REMOVE;
        }
        return null; //删除
    }

    /** 重写检测越界 */
    public boolean outOfBounds(){
        return this.y <= -this.height;
    }
}
