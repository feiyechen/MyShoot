package cn.tedu.shoot;
import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics;

/** 飞行物 */
public abstract class FlyingObject {
    public static final int LIFE = 0;
    public static final int DEAD = 1;
    public static final int REMOVE = 2;
    protected int state = LIFE; //当前状态（默认活着的）

    protected int width;
    protected int height;
    protected int x;
    protected int y;

    /** 专门给英雄机、天空、子弹提供的 */
    public FlyingObject(int width,int height,int x,int y){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    /** 专门给小敌机、大敌机、小蜜蜂提供的 */
    public FlyingObject(int width,int height){
        this.width = width;
        this.height = height;
        Random rand = new Random();
        x = rand.nextInt(World.WIDTH-this.width);
        y = -this.height;
    }

    /** 读取图片 */
    public static BufferedImage loadImage(String fileName){
        try {
            BufferedImage img = ImageIO.read(FlyingObject.class.getResource(fileName));//同包读文件
            return img;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /** 飞行物移动 */
    public abstract void step();

    /** 获取对象的图片 */
    public abstract BufferedImage getImage();

    /** 判断是否活着 */
    public boolean isLife(){
        return state==LIFE;
    }
    /** 判断是否死了 */
    public boolean isDead(){
        return state==DEAD;
    }
    /** 判断是否删除 */
    public boolean isRemove(){
        return state==REMOVE;
    }

    /** 画对象，g：画笔 */
    public void paintObject(Graphics g){
        g.drawImage(getImage(),x,y,null);
    }

    /** 检测是否越界 */
    public boolean outOfBounds(){
        return this.y >= World.HEIGHT;
    }

    /** 检测碰撞 this:敌人 other:子弹 or 英雄机*/
    public boolean hit(FlyingObject other){
        int x1 = this.x-other.width;
        int x2 = this.x+this.width;
        int y1 = this.y-other.height;
        int y2 = this.y+this.height;
        int x = other.x;
        int y = other.y;

        return x>=x1 && x<=x2 && y>=y1 && y<=y2;
    }

    public void goDead(){
        state = DEAD; //getImage已经设计好不同状态的图片读取
    }

}
