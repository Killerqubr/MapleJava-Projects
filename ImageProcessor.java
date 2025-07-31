import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageProcessor {
    // 图片色块改写的Java实现！
    public static void main(String[] args) throws IOException {
        
        //读取图片 
        try {
            Image image = Toolkit.getDefaultToolkit().getImage("input.png");

            /* 实例一个MediaTracker是为了监听图片载入进程, 因为Image类导入图片是异步的,
            在没有导入完成前image的width和height信息默认为-1 
            这一步确保无论导入什么尺寸的图片都能显示正确信息 */
            // ? 为了显示个信息引入个类可能有点不太必要
            MediaTracker tracker = new MediaTracker(new Component() {});
            tracker.addImage(image, 0);
            tracker.waitForAll();

            int width = image.getWidth(null);
            int height = image.getHeight(null);
            System.out.println("接受图片信息 | 长度: "+width+"px; 宽度: "+height+"px");

            /* 转换为BufferedImage
            Image类无法直接对图片的色块进行读取和修改, 使用PixelGrabber读取效率太低 
            转化为bufferedImage方便读取Image的RGBA信息 */
            // ? 似乎可以直接用BufferedImage读取文件,但是又要新引入一个类ImageIO,听着就好麻烦啊喵!~
        BufferedImage bufferedImage = new BufferedImage(
            width, height, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.getGraphics().drawImage(image, 0, 0, null);

        // 执行方法
        for (String arg : args) {
            switch (arg) {
                case "-rP":
                    int matchCount = replacePixels(bufferedImage, 
                                                new int[]{254, 254, 254, 255}, 
                                                new int[]{222, 213, 208, 0}, 
                                                50,
                                                true
                    );
                        
                    // 输出处理后的图片
                    ImageIO.write(bufferedImage, "png", new File("output.png"));
                    System.out.println("图片处理完成 | [replacePixels] 匹配的像素数量: "+matchCount);

                case "-rS":

            }
        }

        } catch (InterruptedException e) {
            System.out.println("无法载入图片 | "+e);
        }

        System.exit(0);
    } 

        /* 模块 | 将匹配的色块颜色更改为指定颜色
        Ver.1.0 普通替换方法; 新增alpha通道替换
        */
    private static int replacePixels(BufferedImage image,
                                    int[] targetColor,
                                    int[] newColor,
                                    int tolerance,
                                    boolean processAlpha) {
        int width = image.getWidth();
        int height = image.getHeight();
        int matchCount = 0;
    
        // 检查颜色数组长度
        boolean targetHasAlpha = targetColor.length > 3;
        boolean newHasAlpha = newColor.length > 3;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                int red   = (pixel >> 16) & 0xff;
                int green = (pixel >> 8)  & 0xff;
                int blue  = pixel & 0xff;

                // 颜色匹配检查（包括alpha通道条件）
                boolean colorMatch = 
                    Math.abs(red - targetColor[0]) <= tolerance &&
                    Math.abs(green - targetColor[1]) <= tolerance &&
                    Math.abs(blue - targetColor[2]) <= tolerance &&
                    (!processAlpha || !targetHasAlpha || 
                    Math.abs(alpha - targetColor[3]) <= tolerance);

                if (colorMatch) {
                    // 构建新像素值
                    int newAlpha = processAlpha && newHasAlpha ? 
                                    newColor[3] : alpha;
                    int newPixel = (newAlpha << 24) | 
                                    (newColor[0] << 16) | 
                                    (newColor[1] << 8) | 
                                    newColor[2];
                    
                    image.setRGB(x, y, newPixel);
                    matchCount++;
                }
            }
        }
        return matchCount;
    }
}