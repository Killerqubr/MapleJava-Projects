package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

//不许警告！！！
@SuppressWarnings("BusyWait")

//贯彻Runnable并覆写其中的run() 使线程Thread正常运行
public class GamePanel extends JPanel implements Runnable{

    /*screen settings*/

    //16*16 pixels 像素单元
    final int defaultTileSize = 16;
    //scale 缩放
    final int scale = 4;
    //定义单元
    final int tileSize = defaultTileSize*scale;
    //屏幕最大显示单元数 16列12行
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    //屏幕尺寸
    final int screenWidth = tileSize*maxScreenCol;
    final int screenHeight = tileSize*maxScreenRow;

    //定义时间常量防止魔法数字
    final double NANO_PER_SECOND = 1000000000.0;
    final double NANO_PER_MS = 1000000.0;

    //实例化按键事件
    KeyHandler keyHandler = new KeyHandler();

    //数据
    int playerHeight = 48;
    int playerWidth = 48;
    double playerX = 100.0;
    double playerY = 100.0;
    int fps = 12000;
    double playerspeed = 300;

    /*构造器 */

    public GamePanel(){

        //应用尺寸
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        //背景颜色
        this.setBackground(Color.black);
        //双缓冲 减少闪烁 但默认true
        this.setDoubleBuffered(true);
        //允许接收与响应按键
        this.setFocusable(true);
        //导入按键事件
        this.addKeyListener(keyHandler);

    }

    //创建线程写外面以供主循环调用
    Thread gameThread;
    //将GamePanel类给予线程以将之实例化并启动
    public void startGameThread(){

        gameThread = new Thread(this);
        gameThread.start();

    }

    //覆写run() 线程启动时自动调用run() 游戏主循环
    @Override
    public void run() {
        
        //第一次绘制时间与绘制间隔
        double drawInterval = NANO_PER_SECOND/fps;
        double nextDrawTime = System.nanoTime() + drawInterval;
        //保证线程运行 主循环
        while (gameThread != null){

            //调用更新与绘制方法
            //repaint()是调用paintComponent()(protected)的方法
            update();
            repaint();

            //休眠与处理负数报错
            try {
                double sleepTime = (nextDrawTime - System.nanoTime())/NANO_PER_MS;
                Thread.sleep((long)sleepTime);
            } catch (InterruptedException | IllegalArgumentException e) {}

            //下一次绘制时间
            nextDrawTime += drawInterval;

        }

    }

    //防止fps影响速度
    double PlayerSpeed = playerspeed/fps;
    //更新玩家位置并防止sqrt(2)神力
    double finalPlayerSpeed, changeSpeed = PlayerSpeed/(Math.sqrt(2));
    public void update(){
        if (keyHandler.up + keyHandler.down + keyHandler.left + keyHandler.right == 2){
            finalPlayerSpeed = changeSpeed;
        }else{
            finalPlayerSpeed = PlayerSpeed;
        }
        if(keyHandler.left == 1){
            playerX -= finalPlayerSpeed;
        }
        if(keyHandler.down == 1){
            playerY += finalPlayerSpeed;
        }
        if (keyHandler.up == 1){
            playerY -= finalPlayerSpeed;
        }
        if(keyHandler.right == 1){
            playerX += finalPlayerSpeed;
        }

        //防止玩家逃出屏幕
        playerX = Math.max(0.0,Math.min(playerX,screenWidth - playerWidth));
        playerY = Math.max(0.0,Math.min(playerY,screenHeight - playerHeight));
    }

    //绘制 java内置paintComponent()是使用JPanel的常用方法
    @Override
    public void paintComponent(Graphics graphics){

        //固定格式
        super.paintComponent(graphics);
        //转2D以使更多方法可用
        Graphics2D graphics2D = (Graphics2D)graphics;

        //画笔颜色
        graphics2D.setColor(Color.white);
        //绘制矩形
        graphics2D.fillRect((int)playerX, (int)playerY, playerWidth, playerHeight);
        //清理资源 无意义
        graphics2D.dispose();

    }

}