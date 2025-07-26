package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//按键事件
public class KeyHandler implements KeyListener{

    //用以判断的中间值
    public int up, down, left, right;

    //useless
    @Override
    public void keyTyped(KeyEvent e) {

    }

    //输入按下
    @Override
    public void keyPressed(KeyEvent e) {

        //得到输入
        int code = e.getKeyCode();

        //if w, a, s, d
        if(code == KeyEvent.VK_UP){
            up = 1;
        }
        if(code == KeyEvent.VK_LEFT){
            left = 1;
        }
        if(code == KeyEvent.VK_DOWN){
            down = 1;
        }
        if(code == KeyEvent.VK_RIGHT){
            right = 1;
        }

    }

    //输入松开
    @Override
    public void keyReleased(KeyEvent e) {

        //得到输入
        int code = e.getKeyCode();

        //if w, a, s, d
        if(code == KeyEvent.VK_UP){
            up = 0;
        }
        if(code == KeyEvent.VK_LEFT){
            left = 0;
        }
        if(code == KeyEvent.VK_DOWN){
            down = 0;
        }
        if(code == KeyEvent.VK_RIGHT){
            right = 0;
        }

    }
    
}
