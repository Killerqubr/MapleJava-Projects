package game;

import javax.swing.JFrame;

public class JavaGame {
    
    public static void main(String[] args) {
        
        //set window
        JFrame window = new JFrame();
        //set default close-button
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //make sure the window can't be reset size
        window.setResizable(false);
        //set title
        window.setTitle("Title");

        /*引用 */
        GamePanel gamePanel = new GamePanel();
        /*添加 */
        window.add(gamePanel);
        /*打包 */
        window.pack();
        //window will appear at the ceter of the screen
        window.setLocationRelativeTo(null);
        //you can see the window
        window.setVisible(true);

        /*启动游戏线程 */
        gamePanel.startGameThread();

    }

}
