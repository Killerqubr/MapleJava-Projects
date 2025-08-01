import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
            // ! 没有办法显示.avif形式的图片信息,即使转成.png也不行 -> 推测为文件编码问题
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
                case "-rP" -> {
                    int matchCount = replacePixels(bufferedImage,
                            null,
                            new int[]{222, 213, 208, 255},
                            50,
                            true,
                            true
                    );
                        
                    // 输出处理后的图片
                    ImageIO.write(bufferedImage, "png", new File("output.png"));
                    System.out.println("ImageProcessor | [replacePixels] 匹配的像素数量: "+matchCount);
                    }

                case "-rS" -> {
                    bufferedImage = resizePic(bufferedImage,
                            800, 600,
                            new int[]{255, 255, 255, 255});

                    // 处理输出图片
                    ImageIO.write(bufferedImage, "png", new File("resized.png"));
                    System.out.println("ImageProcessor | [resizePic] 图片尺寸调整完成");
                    break;
                    }
            }
        }

        } catch (InterruptedException e) {
            System.out.println("无法载入图片 | "+e);
        }

        System.exit(0);
    } 

    /* ImageProcessor.replacePixels [-rP] | 将匹配的色块颜色更改为指定颜色
     * V1.3 | 可用: 识别并替换主体颜色(targetColor作参考色)
     * V1.1 | 可用: alpha通道替换
     */
private static int replacePixels(BufferedImage image,
                                int[] targetColor,
                                int[] newColor,
                                int tolerance,
                                boolean processAlpha,
                                boolean autoSelect) {
    // 参数校验
    if (newColor == null || newColor.length < 3) {
        throw new IllegalArgumentException("newColor不能为null且至少需要3个分量(RGB)");
    }

    int width = image.getWidth();
    int height = image.getHeight();
    int matchCount = 0;

    // 1. 自动颜色分析
    if (autoSelect) {
        Map<Integer, Integer> colorHistogram = new HashMap<>();
        
        // 第一次扫描：建立颜色直方图
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                colorHistogram.put(pixel, colorHistogram.getOrDefault(pixel, 0) + 1);
            }
        }

        // 2. 如果没有提供targetColor，使用出现频率最高的颜色
        if (targetColor == null && !colorHistogram.isEmpty()) {
            int mostFrequentColor = Collections.max(
                colorHistogram.entrySet(), 
                Map.Entry.comparingByValue()
            ).getKey();
            
            // 将颜色转换为数组格式
            targetColor = new int[]{
                (mostFrequentColor >> 16) & 0xFF,
                (mostFrequentColor >> 8) & 0xFF,
                mostFrequentColor & 0xFF
            };
            if (processAlpha) {
                targetColor = Arrays.copyOf(targetColor, 4);
                targetColor[3] = (mostFrequentColor >> 24) & 0xFF;
            }
        }

        // 3. 处理所有相近颜色
        if (targetColor != null) {
            
            for (Map.Entry<Integer, Integer> entry : colorHistogram.entrySet()) {
                int currentRGB = entry.getKey();
                int currentAlpha = (currentRGB >> 24) & 0xFF;
                int currentRed = (currentRGB >> 16) & 0xFF;
                int currentGreen = (currentRGB >> 8) & 0xFF;
                int currentBlue = currentRGB & 0xFF;

                boolean shouldReplace = 
                    Math.abs(currentRed - targetColor[0]) <= tolerance &&
                    Math.abs(currentGreen - targetColor[1]) <= tolerance &&
                    Math.abs(currentBlue - targetColor[2]) <= tolerance &&
                    (!processAlpha || targetColor.length <= 3 || 
                     Math.abs(currentAlpha - targetColor[3]) <= tolerance);

                if (shouldReplace) {
                    matchCount += replaceColorGroup(image, currentRGB, newColor, tolerance, processAlpha);
                }
            }
        }
    } 
    // 原始替换逻辑
    else if (targetColor != null) {  // 只有当targetColor不为null时才执行
        matchCount = replaceColorGroup(image, 
            (targetColor.length > 3 ? targetColor[3] : 255) << 24 |
            targetColor[0] << 16 | targetColor[1] << 8 | targetColor[2],
            newColor, tolerance, processAlpha);
    }

    return matchCount;
}

/**
 * 替换特定颜色组（辅助方法）
 */
private static int replaceColorGroup(BufferedImage image, 
                                   int targetRGB,
                                   int[] newColor,
                                   int tolerance,
                                   boolean processAlpha) {
    int matchCount = 0;
    int width = image.getWidth();
    int height = image.getHeight();
    
    int targetAlpha = (targetRGB >> 24) & 0xFF;
    int targetRed = (targetRGB >> 16) & 0xFF;
    int targetGreen = (targetRGB >> 8) & 0xFF;
    int targetBlue = targetRGB & 0xFF;
    
    boolean newHasAlpha = newColor.length > 3;

    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            int pixel = image.getRGB(x, y);
            int alpha = (pixel >> 24) & 0xFF;
            int red = (pixel >> 16) & 0xFF;
            int green = (pixel >> 8) & 0xFF;
            int blue = pixel & 0xFF;

            if (Math.abs(red - targetRed) <= tolerance &&
                Math.abs(green - targetGreen) <= tolerance &&
                Math.abs(blue - targetBlue) <= tolerance &&
                (!processAlpha || Math.abs(alpha - targetAlpha) <= tolerance)) {
                
                int newAlpha = processAlpha && newHasAlpha ? newColor[3] : alpha;
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
    
    /* ImageProcessor.resizePic [-rS] | 裁剪图片大小
     * V1 | 能将缺失的部分用指定色块补齐(通常为[255,255,255])
     */
    private static BufferedImage resizePic(BufferedImage original, 
                                        int newWidth, 
                                        int newHeight, 
                                        int[] fillColor) {
        // 创建新图像
        BufferedImage resized = new BufferedImage(
            newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g = resized.createGraphics();
        
        // 设置填充颜色（处理透明度）
        int alpha = fillColor.length > 3 ? fillColor[3] : 255;
        g.setColor(new Color(fillColor[0], fillColor[1], fillColor[2], alpha));
        g.fillRect(0, 0, newWidth, newHeight);
        
        // 计算缩放比例
        double scaleX = (double)newWidth / original.getWidth();
        double scaleY = (double)newHeight / original.getHeight();
        double scale = Math.min(scaleX, scaleY);
        
        // 计算居中位置
        int x = (newWidth - (int)(original.getWidth() * scale)) / 2;
        int y = (newHeight - (int)(original.getHeight() * scale)) / 2;
        
        // 绘制缩放后的图像
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        g.drawImage(original, scaleOp, x, y);
        
        g.dispose();
        return resized;
    }
}