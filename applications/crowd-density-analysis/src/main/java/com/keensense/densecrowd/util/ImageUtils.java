package com.keensense.densecrowd.util;

import com.keensense.densecrowd.feature.PolygonPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.*;
import javax.imageio.stream.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.List;
import java.util.*;
import java.util.regex.*;

/**
 * 图片缩放工具类.<br>
 *
 * @see <a href=
 * "http://luoyahu.iteye.com/blog/1312043">http://luoyahu.iteye.com/blog/1312043</a>
 */
@Slf4j
public class ImageUtils {

    /**
     * 将图片不相干区域填成白色
     *
     * @param areaType      画图模式
     * @param width         感兴趣区/非感兴趣区宽度
     * @param height        感兴趣区/非感兴趣区高度
     * @param polygons      感兴趣区/非感兴趣区坐标
     * @param destImagePath 目标图标路径
     * @param file          源文件
     * @throws IOException
     */
    public static void reDrawPNG(int width, int height, PolygonPoint[][] polygons, String destImagePath, File file)
            throws IOException {

        // 创建BufferedImage对象
        // BufferedImage image = new BufferedImage(width, height,
        // BufferedImage.TYPE_BYTE_GRAY);
        FileInputStream fileIput = new FileInputStream(file);
        // BufferedImage image = new BufferedImage(fileIput);
        BufferedImage image = ImageIO.read(fileIput);

        // 获取Graphics2D
        Graphics2D g2d = image.createGraphics();

        Color color = new Color(255, 255, 255);
        g2d.setPaint(color);

        Rectangle r = new Rectangle(0, 0, width, height);
        Area a = new Area(r);

        Area roi = new Area();
        for (int i = 0; i < polygons.length; i++) {
            Polygon p = new Polygon();
            PolygonPoint[] polygon = polygons[i];

            for (int j = 0; j < polygon.length; j++) {
                PolygonPoint pp = polygon[j];
                if (null != pp) {
                    p.addPoint(pp.getX(), pp.getY());
                }
            }
            roi.add(new Area(p));
        }

        Area fillArea = a;
        fillArea.subtract(roi);

        g2d.setClip(fillArea);
        g2d.fillRect(0, 0, width, height);

        // 释放对象
        g2d.dispose();

        // 保存文件
        try {
            ImageIO.write(image, "png", new File(destImagePath));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 将图片的白色区域设置成透明
     *
     * @param srcImagePath  原图片路径
     * @param destImagePath 目标图片路径
     */
    public static void convert(String srcImagePath, String destImagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(srcImagePath));
            ImageIcon imageIcon = new ImageIcon(image);
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            /*int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
				for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
					int rgb = bufferedImage.getRGB(j2, j1);
					if (colorInRange(rgb))
						alpha = 0;
					else
						alpha = 255;
					rgb = (alpha << 24) | (rgb & 0x00ffffff);
					bufferedImage.setRGB(j2, j1, rgb);
				}
			}*/
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());

            // 生成图片为PNG
            String outFile = destImagePath.substring(0, destImagePath.lastIndexOf("."));

            ImageIO.write(bufferedImage, "png", new File(outFile + ".png"));

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static boolean colorInRange(int color) {
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        if (red >= color_range && green >= color_range && blue >= color_range) {
            return true;
        } else {
            return false;
        }
    }

    private static final int color_range = 210;
    private static Pattern pattern = Pattern.compile("[0-9]*");

    public static boolean isNo(String str) {
        return pattern.matcher(str).matches();
    }

//	/*    *//**
//			 * 缩放png图片
//			 *
//			 * @param originalFile
//			 *            原始图片
//			 * @param resizedFile
//			 *            新图片
//			 * @param newWidth
//			 *            新宽度
//			 * @param newHeight
//			 *            新高度
//			 * @param quality
//			 *            缩放质量
//			 * @throws IOException
//			 *//*
//				 * public static void resizePngPicture(File originalFile, File
//				 * resizedFile,int newWidth, int newHeight,float quality) throws
//				 * IOException {
//				 *
//				 * if (quality > 1) { throw new IllegalArgumentException(
//				 * "Quality has to be between 0 and 1"); }
//				 *
//				 * ImageIcon ii = new
//				 * ImageIcon(originalFile.getCanonicalPath()); Image i =
//				 * ii.getImage(); Image resizedImage = null;
//				 *
//				 * resizedImage = i.getScaledInstance(newWidth, newHeight,
//				 * Image.SCALE_SMOOTH);
//				 *
//				 *
//				 * // This code ensures that all the pixels in the image are
//				 * loaded. Image temp = new ImageIcon(resizedImage).getImage();
//				 *
//				 * // Create the buffered image. BufferedImage bufferedImage =
//				 * new BufferedImage(temp.getWidth(null), temp.getHeight(null),
//				 * BufferedImage.TYPE_BYTE_GRAY);
//				 *
//				 * // Copy image to buffered image. Graphics g =
//				 * bufferedImage.createGraphics();
//				 *
//				 * // Clear background and paint the image.
//				 * g.setColor(Color.white); g.fillRect(0, 0,
//				 * temp.getWidth(null), temp.getHeight(null)); g.drawImage(temp,
//				 * 0, 0, null); g.dispose();
//				 *
//				 * // Soften. float softenFactor = 0.05f; float[] softenArray =
//				 * {0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4),
//				 * softenFactor, 0, softenFactor, 0}; Kernel kernel = new
//				 * Kernel(3, 3, softenArray); ConvolveOp cOp = new
//				 * ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
//				 * bufferedImage = cOp.filter(bufferedImage, null);
//				 *
//				 * // Write the jpeg to a file. FileOutputStream out = new
//				 * FileOutputStream(resizedFile);
//				 *
//				 * // Encodes image as a JPEG data stream JPEGImageEncoder
//				 * encoder = JPEGCodec.createJPEGEncoder(out);
//				 *
//				 * JPEGEncodeParam param = encoder
//				 * .getDefaultJPEGEncodeParam(bufferedImage);
//				 *
//				 * param.setQuality(quality, true);
//				 *
//				 * encoder.setJPEGEncodeParam(param);
//				 * encoder.encode(bufferedImage); out.flush(); out.close(); }
//				 */
//
//	public static void resize(File originalFile, File resizedFile, int newWidth, float quality) throws IOException {
//
//		if (quality > 1) {
//			throw new IllegalArgumentException("Quality has to be between 0 and 1");
//		}
//
//		ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
//		Image i = ii.getImage();
//		Image resizedImage = null;
//
//		int iWidth = i.getWidth(null);
//		int iHeight = i.getHeight(null);
//
//		if (iWidth > iHeight) {
//			resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight) / iWidth, Image.SCALE_SMOOTH);
//		} else {
//			resizedImage = i.getScaledInstance((newWidth * iWidth) / iHeight, newWidth, Image.SCALE_SMOOTH);
//		}
//
//		// This code ensures that all the pixels in the image are loaded.
//		Image temp = new ImageIcon(resizedImage).getImage();
//
//		// Create the buffered image.
//		BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null),
//				BufferedImage.TYPE_INT_RGB);
//
//		// Copy image to buffered image.
//		Graphics g = bufferedImage.createGraphics();
//
//		// Clear background and paint the image.
//		g.setColor(Color.white);
//		g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
//		g.drawImage(temp, 0, 0, null);
//		g.dispose();
//
//		// Soften.
//		float softenFactor = 0.05f;
//		float[] softenArray = { 0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4), softenFactor, 0, softenFactor,
//				0 };
//		Kernel kernel = new Kernel(3, 3, softenArray);
//		ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
//		bufferedImage = cOp.filter(bufferedImage, null);
//
//		// Write the jpeg to a file.
//		FileOutputStream out = new FileOutputStream(resizedFile);
//
//		// Encodes image as a JPEG data stream
//		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//
//		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
//
//		param.setQuality(quality, true);
//
//		encoder.setJPEGEncodeParam(param);
//		encoder.encode(bufferedImage);
//		out.flush();
//		out.close();
//	} // Example usage

    /***
     * 按指定的比例缩放图片
     * @param sourceImagePath
     * 源地址
     * @param destinationPath
     * 改变大小后图片的地址
     * @param scale
     * 缩放比例，如1.2 */
    public static void scaleImage(String sourceImagePath, String destinationPath, double scale, String format) {
        File file = new File(sourceImagePath);
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(file);
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            width = parseDoubleToInt(width * scale);
            height = parseDoubleToInt(height * scale);
            Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = outputImage.getGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            ImageIO.write(outputImage, format, new File(destinationPath));
        } catch (IOException e) {
            log.error("scaleImage方法压缩图片时出错了");
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 将double类型的数据转换为int，四舍五入原则
     *
     * @param sourceDouble
     * @retur
     */
    private static int parseDoubleToInt(double sourceDouble) {
        int result = 0;
        result = (int) sourceDouble;
        return result;
    }

    /**
     * base64字符串转化成图片
     *
     * @param imgStr      Base64编码过的字节数组字符串
     * @param imgFilePath 新生成的图片路径
     * @return true 成功，false失败
     */
    public static boolean str2Image(String imgStr, String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 图片截取缩略图
     *
     * @param root
     * @param resPath
     * @param filePath
     * @param picType
     * @return
     * @throws IOException
     */
    public static String snapshotPic(String approot, String resPath, String filePath, String picType)
            throws IOException {
        String smallFilePath;
        String ffmpegPath = approot + "3nd" + File.separator + "ffmpeg.exe";

        smallFilePath = filePath.substring(0, filePath.lastIndexOf('.')) + "-" + picType + ".jpg";

        List<String> commend = new java.util.ArrayList<>();
        commend.add(ffmpegPath);
        commend.add("-i");
        commend.add(filePath);
        commend.add("-y");
        commend.add("-f");
        commend.add("image2");
        commend.add("-ss");
        commend.add("1");
        if (picType.equals("small")) {
            commend.add("-s");
            commend.add("120x120");
        }
        commend.add(smallFilePath);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commend);
        builder.redirectErrorStream(true);
        log.info("视频截图开始" + commend.toString());
        Process process = builder.start();
        InputStream in = process.getInputStream();
        String info = StreamUtils.copyToString(in, Charset.defaultCharset());
        log.info("result:" + info);
        in.close();
        process.destroy();
        log.info("视频截图完成...");
        return smallFilePath;
    }

    /**
     * 图片截取缩略图
     *
     * @param approot  根路径
     * @param filePath 文件路径
     * @param picType  新文件后缀
     * @return
     * @throws IOException
     */
    public static String snapshotPic(String approot, String filePath, String picType) throws IOException {
        String smallFilePath;
        String ffmpegPath = approot + "3nd\\ffmpeg.exe";
        //String resPath = PropertiesUtil.getFtpPackey("file.service.path").substring(1,PropertiesUtil.getFtpPackey("file.service.path").length());
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf('.')) + "-" + picType + ".jpg";
        smallFilePath = approot + "\\" + fileName;
        List<String> commend = new java.util.ArrayList<String>();
        commend.add(ffmpegPath);
        commend.add("-i");
        commend.add(filePath);
        commend.add("-y");
        commend.add("-f");
        commend.add("image2");
        commend.add("-ss");
        commend.add("1");
        if (picType.equals("small")) {
            commend.add("-s");
            commend.add("120x120");
        }
        commend.add(smallFilePath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            builder.redirectErrorStream(true);
            System.out.println("视频截图开始" + commend.toString());
            Process process = builder.start();
            InputStream in = process.getInputStream();
            String info = StreamUtils.copyToString(in, Charset.defaultCharset());
            //System.out.println("result:" + info);
            in.close();
            process.destroy();
            System.out.println("视频截图完成...");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("视频截图失败！");
        }
        return smallFilePath;
    }

    /**
     * 截取图片: 背景设置成白色
     *
     * @param srcImageFile 原图片地址
     * @param x            截取时的x坐标
     * @param y            截取时的y坐标
     * @param desWidth     截取的宽度
     * @param desHeight    截取的高度
     */
    public static String imgCut(String srcImageFile, int x, int y, int desWidth, int desHeight) {
        String resultFileName = "";
        try {
            Image img;
            ImageFilter cropFilter;
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getWidth();
            int srcHeight = bi.getHeight();
            if (srcWidth >= desWidth && srcHeight >= desHeight) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                cropFilter = new CropImageFilter(x, y, desWidth, desHeight);
                img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));

                if (desWidth == 0) {
                    desWidth = 1;
                }
                if (desHeight == 0) {
                    desHeight = 1;
                }

                BufferedImage tag = new BufferedImage(desWidth, desHeight, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, Color.WHITE, null);
                g.dispose();

                resultFileName = srcImageFile.substring(0, srcImageFile.lastIndexOf(".")) + "_"
                        + System.currentTimeMillis() + "_cut.png";// 输出文件

                ImageIO.write(tag, "png", new File(resultFileName));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return resultFileName;
    }

    public static String getURLImage(String imageUrl) throws Exception {
        //new一个URL对象
        URL url = new URL(imageUrl);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式为"GET" 
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = readInputStream(inStream);
        BASE64Encoder encode = new BASE64Encoder();
        return encode.encode(data);
    }

    private static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    // Returns the format name of the image in the object 'o'.
    // Returns null if the format is not known.
    public static String getFormatName(Object o) {
        try {
            // Create an image input stream on the image
            ImageInputStream iis = ImageIO.createImageInputStream(o);

            // Find all image readers that recognize the image format
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                // No readers found
                return null;
            }

            // Use the first reader
            ImageReader reader = iter.next();

            // Close stream
            iis.close();

            // Return the format name
            return reader.getFormatName();
        } catch (IOException e) {
            //
        }

        // The image could not be read
        return null;
    }


    public static void main(String[] args) throws IOException {
        /*
         * // File originalImage = new File("C:\\11.jpg"); //
		 * resize(originalImage, new File("c:\\11-0.jpg"),150, 0.7f); //
		 * resize(originalImage, new File("c:\\11-1.jpg"),150, 1f); String
		 * pathname = "c:\\1.png"; String small = "c:\\1-small.png"; File
		 * originalImage = new File(pathname); long start =
		 * System.currentTimeMillis(); for (int i = 0; i < 1; i++) {
		 * resize(originalImage, new File(small), 800, 1f); } long end =
		 * System.currentTimeMillis() - start; System.out.println(end);
		 */

        // String strImg =
//		String strImg = "";
//		// System.out.println(strImg);
//		strImg = imgCut("D:\\Test\\test11.png", 100, 100, 100, 100);
//		System.out.println(strImg);
    }
}
