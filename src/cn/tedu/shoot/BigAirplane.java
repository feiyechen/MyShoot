package cn.tedu.shoot;
import java.awt.image.BufferedImage;
import java.util.Random;

public class BigAirplane extends FlyingObject implements Enemy{

    /** 静态块，只加载一次，所有对象共享一份 */
    private static BufferedImage[] images;
    static {
        images = new BufferedImage[5];
        for(int i=0;i<images.length;i++){
            images[i] = loadImage("bigplane"+i+".png");
        }
    }

    private int speed; //移动速度
    /** 构造方法 */
    public BigAirplane(){
        super(69,99);
        speed = 2;
    }

    /** 移动方法 */
    public void step(){
        y+=speed;
    }

    int index = 1; //下标
    /** 获取图片 */
    public BufferedImage getImage(){
        if(isLife()){
            return images[0];
        }else if(isDead()){
            BufferedImage img = images[index++];
            if(index== images.length){
                state = REMOVE;
            }
            return img;
        }return null;
    }

    public int getScore(){
        return 3;
    }
}
