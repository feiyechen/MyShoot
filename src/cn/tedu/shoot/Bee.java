package cn.tedu.shoot;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Bee extends FlyingObject implements Award{

    /** 静态块，只加载一次，所有对象共享一份 */
    private static BufferedImage[] images;
    static {
        images = new BufferedImage[5];
        for(int i=0;i<images.length;i++){
            images[i] = loadImage("bee"+i+".png");
        }
    }

    private int xSpeed; //x坐标移动速度
    private int ySpeed; //y坐标移动速度
    private int awardType; //奖励类型（0或1）
    /** 构造方法 */
    public Bee(){
        super(60,50);
        xSpeed = 1;
        ySpeed = 2;
        Random rand = new Random();
        awardType = rand.nextInt(2);
    }

    /** 移动方法 */
    public void step(){
        y+=ySpeed;
        x+=xSpeed;
        if(x<=0 || x>=World.WIDTH-this.width){ //Bee超出边界
            xSpeed*=-1;
        }
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

    public int getAwardType(){
        return awardType;
    }
}
