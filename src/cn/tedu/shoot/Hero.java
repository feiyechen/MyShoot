package cn.tedu.shoot;
import java.awt.image.BufferedImage;

public class Hero extends FlyingObject{

    /** 静态块，只加载一次，所有对象共享一份 */
    private static BufferedImage[] images;
    static {
        images = new BufferedImage[2];
        images[0] = loadImage("hero0.png");
        images[1] = loadImage("hero1.png");
    }

    private int life;
    private int doubleFire;
    /** 构造方法 */
    public Hero(){
        super(97,124,140,400);
        life = 3;
        doubleFire = 0;
    }

    public void moveTo(int x, int y){
        this.x = x-this.width/2;
        this.y = y-this.height/2;
    }

    /** 移动方法 */
    public void step(){

    }

    int index = 0; //下标
    /** 获取图片 */
    public BufferedImage getImage(){ //每10毫秒走一次
        if(isLife()){ //活着的
            return images[index++% images.length]; //n张图片轮换的写法
        }
        return null; //死了、删除了
    }

    /** 发射（生成子弹对象） */
    public Bullet[] shoot(){
        int xStep = this.width/4;
        int yStep = 20;
        if(doubleFire>0){
            Bullet[] bs = new Bullet[2];
            bs[0] = new Bullet(this.x+xStep,this.y-yStep); //第一发子弹的位置
            bs[1] = new Bullet(this.x+3*xStep,this.y-yStep); //第二发子弹的位置
            doubleFire-=2; //发射一次双倍，火力值-2
            return bs;
        }else {
            Bullet[] bs = new Bullet[1];
            bs[0] = new Bullet(this.x+2*xStep,this.y-yStep); //单倍子弹的位置
            return bs;
        }
    }

    /** 增命 */
    public void addLife(){
        life++;
    }

    /** 获取命 */
    public int getLife(){
        return life;
    }

    /** 减命 */
    public void subtractLife(){
        life--;
    }

    /** 增火力 */
    public void addDoubleFire(){
        doubleFire+=40;
    }

    /** 火力清零 */
    public void clearDoubleFire(){
        doubleFire=0;
    }
}
