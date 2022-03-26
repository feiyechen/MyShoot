package cn.tedu.shoot;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.Arrays;
import java.awt.image.BufferedImage;

public class World extends JPanel{
    public static final int WIDTH = 400;
    public static final int HEIGHT = 700;

    public static final int START = 0;
    public static final int RUNNING = 1;
    public static final int PAUSE = 2;
    public static final int GAME_OVER = 3;
    private int state = START; //当前状态（默认START）

    private static BufferedImage start;
    private static BufferedImage pause;
    private static BufferedImage gameover;
    static {
        start = FlyingObject.loadImage("start.png");
        pause = FlyingObject.loadImage("pause.png");
        gameover = FlyingObject.loadImage("gameover.png");
    }

    private Sky sky = new Sky(); //天空
    private Hero hero = new Hero(); //英雄机
    private FlyingObject[] enemies = {}; //敌人数组
    private Bullet[] bullets = {};//子弹数组

    /** 随机生成敌人对象 */
    public FlyingObject nextOne(){
        Random rand = new Random();
        int type = rand.nextInt(20);
        if(type<5){
            return new Bee();
        }else if(type<12){
            return new Airplane();
        }else{
            return new BigAirplane();
        }
    }

    int enterIndex = 0;
    /** 敌人入场方法 */
    public void enterAction(){ //每10毫秒走一次
        enterIndex++;
        if(enterIndex%40==0){
            FlyingObject obj = nextOne(); //随机生成了敌人对象
            enemies = Arrays.copyOf(enemies,enemies.length+1); //敌人数组扩容
            enemies[enemies.length-1] = obj; //将敌人装到数组最后一个
        }
    }

    int shootIndex = 0;
    /** 子弹入场方法 */
    public void shootAction(){ //每10毫秒走一次
        shootIndex++;
        if(shootIndex%30==0){
            Bullet[] bs = hero.shoot(); //生成了子弹对象
            bullets = Arrays.copyOf(bullets,bullets.length+bs.length); //子弹数组扩容
            System.arraycopy(bs,0,bullets,bullets.length-bs.length,bs.length); //数组的追加
        }
    }

    /** 飞行物移动 */
    public void stepAction(){ //每10毫秒走一次
        sky.step();
        for(int i=0;i< enemies.length;i++){
            enemies[i].step();
        }
        for(int i=0;i< bullets.length;i++){
            bullets[i].step();
        }
    }

    /** 删除越界的敌人和子弹 */
    public void outOfBoundsAction(){ //每10毫秒走一次
        int index = 0;
        FlyingObject[] enemyLives = new FlyingObject[enemies.length];
        for(int i=0;i<enemies.length;i++){
            FlyingObject f = enemies[i];
            if(!f.outOfBounds() && !f.isRemove()){ //不越界并且非删除
                enemyLives[index] = f;
                index++;
            }
        }
        enemies = Arrays.copyOf(enemyLives,index);

        index = 0;
        Bullet[] bulletLives = new Bullet[bullets.length];
        for(int i=0;i< bullets.length;i++){
            Bullet b = bullets[i];
            if(!b.outOfBounds() && !b.isRemove()){
                bulletLives[index] = b;
                index++;
            }
        }
        bullets = Arrays.copyOf(bulletLives,index);
    }

    int score = 0;
    /** 子弹与敌人碰撞 */
    public void bulletBangAction(){ //每10毫秒走一次
        for(int i=0;i<bullets.length;i++){
            Bullet b = bullets[i];
            for(int j=0;j<enemies.length;j++){
                FlyingObject f = enemies[j];
                if(b.isLife() && f.isLife() && f.hit(b)){
                    b.goDead();
                    f.goDead();
                    if(f instanceof Enemy){
                        Enemy e = (Enemy) f;
                        score += e.getScore();
                    }
                    if(f instanceof Award){
                        Award a = (Award) f;
                        int type = a.getAwardType();
                        switch (type){
                            case Award.DOUBLE_FIRE:
                                hero.addDoubleFire();
                                break;
                            case Award.LIFE:
                                hero.addLife();
                                break;
                        }
                    }
                }
            }
        }
    }

    /** 英雄机与敌人碰撞 */
    public void heroBangAction(){ //每10毫秒走一次
        for(int i=0;i< enemies.length;i++){
            FlyingObject f = enemies[i];
            if(hero.isLife() && f.isLife() && f.hit(hero)){
                f.goDead();
                hero.subtractLife();
                hero.clearDoubleFire();
            }
        }
    }

    /** 检测游戏结束 */
    public void checkGameOverAction(){
        if(hero.getLife()<=0){ //游戏结束条件
            state=GAME_OVER; //当前状态修改为游戏结束
        }
    }

    /** 启动程序的执行 */
    public void action(){
        MouseAdapter l = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(state==RUNNING){
                    int x = e.getX(); //获取鼠标x坐标
                    int y = e.getY(); //获取鼠标y坐标
                    hero.moveTo(x,y); //英雄机随鼠标移动
                }
            }
            public void mouseClicked(MouseEvent e) {
                switch (state){ //鼠标点击时，根据不同状态，改变不同状态
                    case START:
                        state=RUNNING;
                        break;
                    case GAME_OVER:
                        score = 0; //清理现场
                        sky = new Sky();
                        hero = new Hero();
                        enemies = new FlyingObject[0];
                        bullets = new Bullet[0];
                        state=START;
                        break;
                }
            }
            public void mouseExited(MouseEvent e) {
                if(state==RUNNING){
                    state=PAUSE;
                }
            }
            public void mouseEntered(MouseEvent e) {
                if(state==PAUSE){
                    state=RUNNING;
                }
            }
        };
        this.addMouseListener(l); //处理鼠标操作
        this.addMouseMotionListener(l); //处理鼠标移动

        Timer timer = new Timer();
        int intervel = 10; //以毫秒为单位
        timer.schedule(new TimerTask()
        {
            public void run() { //定时干的事
                if(state==RUNNING){
                    enterAction(); //敌人入场
                    shootAction(); //子弹入场
                    stepAction(); //飞行物移动
                    outOfBoundsAction(); //删除越界敌人和子弹
                    bulletBangAction(); //子弹与敌人的碰撞
                    heroBangAction(); //英雄机与敌人的碰撞
                    checkGameOverAction(); //检测游戏结束
                }
                repaint(); //重画（调用paint）
            }
        },intervel,intervel); //定时计划
    }

    /** 重写 JPanel 里的 paint() */
    public void paint(Graphics g){
       sky.paintObject(g); //画天空
       hero.paintObject(g); //画英雄机
       for(int i=0;i<enemies.length;i++){
           enemies[i].paintObject(g); //画敌人
       }
       for(int i=0;i< bullets.length;i++){
           bullets[i].paintObject(g); //画子弹
       }

       g.drawString("SCORE: "+score,10,25);
       g.drawString("LIFE: "+hero.getLife(),10,45);

       switch (state){
           case START:
               g.drawImage(start,0,0,null);
               break;
           case PAUSE:
               g.drawImage(pause,0,0,null);
               break;
           case GAME_OVER:
               g.drawImage(gameover,0,0,null);
               break;
       }
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        World world = new World();
        frame.add(world);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        world.action();
    }
}
