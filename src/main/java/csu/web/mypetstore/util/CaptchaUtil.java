package csu.web.mypetstore.util;

import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class CaptchaUtil {
    // 验证码字符集
    private static final String CODE_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    // 验证码长�?
    private static final int CODE_LENGTH = 4;
    // 图片宽度
    private static final int WIDTH = 100;
    // 图片高度
    private static final int HEIGHT = 40;
    // 干扰线数�?
    private static final int LINE_COUNT = 5;

    /**
     * 生成验证码图片并写入响应
     */
    public static void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 创建图片缓冲�?
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 2. 获取画笔
        Graphics g = image.getGraphics();
        // 3. 设置背景�?
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        // 4. 设置边框
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
        // 5. 生成随机字符
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CODE_CHAR.length());
            char c = CODE_CHAR.charAt(index);
            code.append(c);
            // 绘制字符（随机颜色和位置�?
            g.setColor(new Color(random.nextInt(150), random.nextInt(150), random.nextInt(150)));
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString(String.valueOf(c), 20 * i + 10, 30);
        }
        // 6. 添加干扰�?
        for (int i = 0; i < LINE_COUNT; i++) {
            g.setColor(new Color(random.nextInt(200), random.nextInt(200), random.nextInt(200)));
            g.drawLine(
                    random.nextInt(WIDTH), random.nextInt(HEIGHT),
                    random.nextInt(WIDTH), random.nextInt(HEIGHT)
            );
        }
        // 7. 将验证码存入Session
        HttpSession session = request.getSession();
        session.setAttribute("captchaCode", code.toString());
        // 8. 输出图片
        response.setContentType("image/jpeg");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "jpeg", os);
        os.close();
    }
}
