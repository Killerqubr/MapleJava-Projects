import java.awt.*;
import javax.swing.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

// 使用JFrame构建窗体
public class Maple extends JFrame{
    public static void main(String[] args) {
        Maple maple = new Maple();
    }

    // 类Maple实例方法
    // * this关键词指向当前对象,即类Maple(等效于Python中的self)
    public Maple() {
        this.setTitle("MapleGame!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置默认退出方式(Close方法退出)
        this.setSize(826, 547);
        this.setVisible(true);

        // 样式
        this.setResizable(false);

        // 组件 I | MaplePanel布局
        MaplePanel panel = new MaplePanel(); 
        this.add(panel);
        
    }
    
    // 类内定义MaplePanel类
    class MaplePanel extends JPanel{
        
        // 重写父类方法paint(图像绘制)
        @Override
        public void paint(Graphics graphics){
            // 调用父类paint方法(整个Override实际上就是在原有paint上加新东西)
            super.paint(graphics);

            // 组件 I | 导入并绘制图像[脑残.jpg]
            Image image = new ImageIcon("MapleBox/images/脑残.jpg").getImage();
            graphics.drawImage(image, 0, 0, 826, 547, null);

        }
    }
}
