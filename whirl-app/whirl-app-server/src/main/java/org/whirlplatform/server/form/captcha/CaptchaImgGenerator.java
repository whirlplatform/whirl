package org.whirlplatform.server.form.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.UUID;
import javax.imageio.ImageIO;

/**
 * генерирует изображение, направляет его на указанный при инициализации поток вывода
 */
public class CaptchaImgGenerator {

    public static final double PI = 3.1415926535897932384626433832795;
    private static final int DEFAULT_LETTERS_COUNT = 6;
    private String captchaText;
    private Integer bufferLength = null;

    public CaptchaImgGenerator(Integer lettersCount, OutputStream out)
            throws IOException {
        this(50, 120, lettersCount, out);
    }

    public CaptchaImgGenerator(int height, int width, Integer lc,
                               OutputStream out) throws IOException {
        Integer lettersCount = (lc != null) ? lc : DEFAULT_LETTERS_COUNT;
        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Random rand = new Random(System.currentTimeMillis());
        Graphics2D graphics = bufferedImage.createGraphics();

        // set the font
        Integer fontSize = calcFontSize(width, height, lettersCount);
        graphics.setFont(new Font("arial", Font.BOLD, fontSize));

        // generate a random value
        this.captchaText = UUID.randomUUID().toString().replace("-", "")
                .substring(0, lettersCount);

        Color startingColor = new Color(rand.nextInt(255), rand.nextInt(255),
                rand.nextInt(255));
        Color endingColor = new Color(rand.nextInt(255), rand.nextInt(255),
                rand.nextInt(255));
        applyCurrentGradientPaint(graphics, bufferedImage.getWidth(),
                bufferedImage.getHeight(), startingColor, endingColor);

        graphics.fillRect(0, 0, bufferedImage.getWidth(),
                bufferedImage.getHeight());

        // Draw the image border.

        graphics.setColor(Color.black);

        // randomly set the color and make it really bright for more readability
        Color textColor = null; // new Color(rand.nextInt(235)+20,
        // rand.nextInt(225)+20,
        // rand.nextInt(235)+20).brighter().brighter();

        // Draw text on the CAPTCHA image.
        drawTextOnImage(graphics, captchaText, bufferedImage.getWidth(),
                bufferedImage.getHeight(), fontSize, textColor);

        // Apply noise on the CAPTCHA image.
        applyNoiseOnImage(graphics, bufferedImage.getWidth(),
                bufferedImage.getHeight(), startingColor, endingColor);

        // applyNoise(graphics, bufferedImage.getWidth(),
        // bufferedImage.getHeight());

        drawBorders(graphics, bufferedImage.getWidth(),
                bufferedImage.getHeight());

        this.bufferLength = bufferedImage.getRaster().getDataBuffer().getSize();
        // write out the PNG file
        ImageIO.write(bufferedImage, "png", out);
        // System.out.println("DONE WRITYING NEW IMAGHE");

        // make sure your clean up the graphics object
        graphics.dispose();
    }

    /*
     * This helper method is used for applying current gradient paint to the
     * Graphics2D object.
     */
    private static void applyCurrentGradientPaint(Graphics2D graphics,
                                                  int width, int height, Color startingColor,
                                                  Color endingColor) {

        GradientPaint gradientPaint = new GradientPaint(0, 0, startingColor,
                width, height, endingColor);

        graphics.setPaint(gradientPaint);
    }

    public Integer getBufferLength() {
        return this.bufferLength;
    }

    protected int calcFontSize(int width, int height, int lettersCount) {
        int sizeW = 0;
        int sizeH = 0;
        sizeW = ((width) / (lettersCount)) * 8 / 5;
        sizeH = height * 7 / 8;
        int size = Math.min(sizeW, sizeH);
        return size;
    }

    /*
     * A helper method to draw the captcha text on the generated image.
     */
    private void drawTextOnImage(Graphics2D graphics, String captchaText,
                                 Integer width, Integer height, Integer fontSize, Color color) {
        Font font;
        TextLayout textLayout;
        double currentFontStatus = Math.random();

        // Generate random font status.
        if (currentFontStatus >= 0.5) {
            font = new Font("Arial", Font.PLAIN, fontSize);
        } else {
            font = new Font("Arial", Font.BOLD, fontSize);
        }

        if (color != null) {
            graphics.setColor(color);
        }

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        textLayout = new TextLayout(captchaText, font,
                graphics.getFontRenderContext());

        int w = (graphics.getFontMetrics()).stringWidth(captchaText);
        int d = (graphics.getFontMetrics()).getDescent();
        int a = (graphics.getFontMetrics()).getMaxAscent();

        int x = width / 2 - w / 2;
        int y = height / 2 + a / 2 - 2;

        textLayout.draw(graphics, x, y);
    }

    /*
     * A helper method to apply noise on the generated image.
     */
    private void applyNoiseOnImage(Graphics2D graphics, int bufferedImageWidth,
                                   int bufferedImageHeight, Color startingColor,
                                   Color endingColor) {

        // Applying shear.
        // applyShear(graphics, bufferedImageWidth, bufferedImageHeight,
        // startingColor, endingColor);

        // Drawing a broken line on the image.
        drawBrokenLineOnImage(graphics, bufferedImageWidth, bufferedImageHeight);
    }

    /*
     * This helper method is used for drawing a thick line on the image.
     */
    private void drawThickLineOnImage(Graphics graphics, int x1, int y1,
                                      int x2, int y2) {

        int dX = x2 - x1;
        int dY = y2 - y1;
        int xPoints[] = new int[4];
        int yPoints[] = new int[4];
        int thickness = 2;

        double lineLength = Math.sqrt(dX * dX + dY * dY);
        double scale = (double) (thickness) / (2 * lineLength);
        double ddx = -scale * (double) dY;
        double ddy = scale * (double) dX;

        graphics.setColor(Color.black);

        ddx += (ddx > 0) ? 0.5 : -0.5;
        ddy += (ddy > 0) ? 0.5 : -0.5;
        dX = (int) ddx;
        dY = (int) ddy;

        xPoints[0] = x1 + dX;
        yPoints[0] = y1 + dY;
        xPoints[1] = x1 - dX;
        yPoints[1] = y1 - dY;
        xPoints[2] = x2 - dX;
        yPoints[2] = y2 - dY;
        xPoints[3] = x2 + dX;
        yPoints[3] = y2 + dY;

        graphics.fillPolygon(xPoints, yPoints, 4);
    }

    /*
     * This helper method is used for drawing a broken line on the image.
     */
    private void drawBrokenLineOnImage(Graphics2D graphics, Integer width,
                                       Integer height) {
        int yPoint1;
        int yPoint2;
        int yPoint3;
        int yPoint4;
        int yPoint5;
        Random random = new Random();

        // Random Y Points.
        yPoint1 = random.nextInt(height);
        yPoint2 = random.nextInt(height);
        yPoint3 = height / 2;
        yPoint4 = random.nextInt(height);
        yPoint5 = random.nextInt(height);

        // Draw the random broken line.
        drawThickLineOnImage(graphics, 0, yPoint1, width / 4, yPoint2);
        drawThickLineOnImage(graphics, width / 4, yPoint2, width / 2, yPoint3);
        drawThickLineOnImage(graphics, width / 2, yPoint3, 3 * width / 4,
                yPoint4);
        drawThickLineOnImage(graphics, 3 * width / 4, yPoint4, width, yPoint5);
    }

    /*
     * This helper method is used for calculating the delta of the shearing
     * equation.
     */
    private double getDelta(int period, double i, double phase, double frames) {
        return (double) (period / 2)
                * Math.sin(i / (double) period + (2 * PI * phase) / frames);
    }

    /*
     * This helper method is used for applying shear on the image.
     */
    private void applyShear(Graphics2D graphics, int bufferedImageWidth,
                            int bufferedImageHeight, Color startingColor, Color endingColor) {

        int periodValue = 20;
        int numberOfFrames = 15;
        int phaseNumber = 7;
        double deltaX;
        double deltaY;

        applyCurrentGradientPaint(graphics, bufferedImageWidth,
                bufferedImageHeight, startingColor, endingColor);

        for (int i = 0; i < bufferedImageWidth; ++i) {
            deltaX = getDelta(periodValue, i, phaseNumber, numberOfFrames);
            graphics.copyArea(i, 0, 1, bufferedImageHeight, 0, (int) deltaX);
            graphics.drawLine(i, (int) deltaX, i, 0);
            graphics.drawLine(i, (int) deltaX + bufferedImageHeight, i,
                    bufferedImageHeight);
        }

        for (int i = 0; i < bufferedImageHeight; ++i) {
            deltaY = getDelta(periodValue, i, phaseNumber, numberOfFrames);
            graphics.copyArea(0, i, bufferedImageWidth, 1, (int) deltaY, 0);
            graphics.drawLine((int) deltaY, i, 0, i);
            graphics.drawLine((int) deltaY + bufferedImageWidth, i,
                    bufferedImageWidth, i);
        }
    }

    /*
     * This helper method is used for drawing the borders the image.
     */
    private void drawBorders(Graphics2D graphics, int width, int height) {
        graphics.setColor(Color.black);

        graphics.drawLine(0, 0, 0, width - 1);
        graphics.drawLine(0, 0, width - 1, 0);
        graphics.drawLine(0, height - 1, width, height - 1);
        graphics.drawLine(width - 1, height - 1, width - 1, 0);
    }

    /*
     * public void applyNoise( Graphics2D graphics, Integer width, Integer
     * height ){ Integer pixelsCount = 10; Random rand = new
     * Random(System.currentTimeMillis()); for( int t = 0; t < pixelsCount; t ++
     * ){ Integer x1 = rand.nextInt( width ); Integer y1 = rand.nextInt( height
     * ); Integer x2 = rand.nextInt( width ); Integer y2 = rand.nextInt( height
     * ); graphics.drawLine(x1, y1, x2, y2 ); } }
     */

    /**
     * return the value to check for when the user enters it in. Make sure you store this off in the
     * session or something like a database and NOT in the form of the webpage since the whole point
     * of this exercise is to ensure that only humans and not machines are entering the data.
     *
     * @return
     */
    public String getVerificationValue() {
        return this.captchaText;
    }
}
