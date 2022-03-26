package cn.tedu.shoot;
import java.awt.*;
import java.awt.image.BufferedImage; //专门装图片的类

public class Sky extends FlyingObject{

    /** 静态块，只加载一次，所有对象共享一份 */
    private static BufferedImage image;
    static {
        image = loadImage("background.png");
    }

    private int speed; //移动速度
    private int y1; //第2张图片的y坐标
    /** 构造方法 */
    public Sky(){
        super(World.WIDTH,World.HEIGHT,0,0);
        speed = 1;
        y1 = -World.HEIGHT;
    }

    /** 移动方法 */
    public void step(){
        y+=speed;
        y1+=speed;
        if(y>=World.HEIGHT){
            y=-World.HEIGHT;
        }
        if(y1>=World.HEIGHT){
            y1=-World.HEIGHT;
        }
    }

    /** 获取图片 */
    public BufferedImage getImage(){
        return image; //直接返回image即可
    }

    /** 重写画对象 */
    public void paintObject(Graphics g){
        g.drawImage(getImage(),x,y,null); //第一张图
        g.drawImage(getImage(),x,y1,null); //第二张图
    }
}
