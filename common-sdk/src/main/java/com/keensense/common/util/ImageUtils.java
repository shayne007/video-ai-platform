package com.keensense.common.util;

import com.keensense.common.exception.VideoException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * 图片缩放工具类.<br>
 * 
 * @see <a href="http://luoyahu.iteye.com/blog/1312043">http://luoyahu.iteye.com
 *      /blog/1312043</a>
 */
public class ImageUtils {

	public static void resize(File originalFile, File resizedFile, int newWidth, float quality) throws IOException {

		/*if (quality > 1) {
			throw new IllegalArgumentException("Quality has to be between 0 and 1");
		}

		ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
		Image i = ii.getImage();
		Image resizedImage = null;

		int iWidth = i.getWidth(null);
		int iHeight = i.getHeight(null);

		if (iWidth > iHeight) {
			resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight) / iWidth, Image.SCALE_SMOOTH);
		} else {
			resizedImage = i.getScaledInstance((newWidth * iWidth) / iHeight, newWidth, Image.SCALE_SMOOTH);
		}
		Image temp = new ImageIcon(resizedImage).getImage();
		BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedImage.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
		g.drawImage(temp, 0, 0, null);
		g.dispose();
		// Soften.
		float softenFactor = 0.05f;
		float[] softenArray = { 0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4), softenFactor, 0, softenFactor,
				0 };
		Kernel kernel = new Kernel(3, 3, softenArray);
		ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		bufferedImage = cOp.filter(bufferedImage, null);
		// Write the jpeg to a file.
		FileOutputStream out = new FileOutputStream(resizedFile);
		// Encodes image as a JPEG data stream
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
		param.setQuality(quality, true);
		encoder.setJPEGEncodeParam(param);
		encoder.encode(bufferedImage);
		out.flush();
		out.close();*/
	} // Example usage

	/**
	 * 图片转化成base64字符串
	 * 
	 * @param picPath
	 *            待处理的图片绝对路径
	 * @return 返回Base64编码过的字节数组字符串
	 */
	public static String image2Str(String picPath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		/*
		String imgFile = picPath;// 待处理的图片
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
*/		
		return null;
	}

	/**
	 * base64字符串转化成图片
	 * 
	 * @param imgStr
	 *            Base64编码过的字节数组字符串
	 * @param imgFilePath
	 *            新生成的图片路径
	 * @return true 成功，false失败
	 */
	public static boolean str2Image(String imgStr, String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) {
			// 图像数据为空
			return false;
		}
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
	 * base64字符串转化成图片
	 * 
	 * @param imgStr
	 *            Base64编码过的字节数组字符串
	 * @param imgFilePath
	 *            新生成的图片路径
	 * @return true 成功，false失败
	 */
	public static boolean str2ImageSec(String imgStr, String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片
		/*if (imgStr == null) // 图像数据为空
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] bytes1 = decoder.decodeBuffer(imgStr.replace("data:image/jpeg;base64,", ""));
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            RenderedImage bi1 = ImageIO.read(bais);
            File w2 = new File(imgFilePath);// 可以是jpg,png,gif格式<br>
            if (!w2.exists()) {
                w2.createNewFile();
            }
            ImageIO.write(bi1, "jpg", w2);
            
			return true;
		} catch (Exception e) {
			return false;
		}*/
		return true;
	}

	/**
	 * 图片截取缩略图
	 * 
	 * @param approot
	 *            根路径
	 * @param filePath
	 *            文件路径
	 * @param picType
	 *            新文件后缀
	 * @return
	 * @throws IOException
	 */
	public static String snapshotPic(String approot, String filePath, String picType) throws IOException {
		String smallFilePath;
		String ffmpegPath = approot + "3nd\\ffmpeg.exe";
		String fileName = RandomUtils.get18TimeRandom() + "-" + picType + ".jpg";
		File file = new File(approot + "ftp\\");
		if(!file.exists()){
			try {
				file.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		smallFilePath = approot + "ftp\\" + fileName;
		List<String> commend = new java.util.ArrayList<String>();
		commend.add(ffmpegPath);
		commend.add("-i");
		commend.add(filePath);
		commend.add("-y");
		commend.add("-f");
		commend.add("image2");
		commend.add("-ss");
		commend.add("1");
		if ("small".equals(picType)) {
			commend.add("-s");
			commend.add("120x120");
		}
		commend.add(smallFilePath);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream in = process.getInputStream();
			in.close();
			Thread.sleep(1000);
			process.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smallFilePath;
	}


	/**
	 * 将图片的白色区域设置成透明
	 * 
	 * @param srcImagePath
	 *            原图片路径
	 * @param destImagePath
	 *            目标图片路径
	 */
	public static void convert(String srcImagePath, String destImagePath) {
		try {
			BufferedImage image = ImageIO.read(new File(srcImagePath));
			ImageIcon imageIcon = new ImageIcon(image);
			BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
					BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
			g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
			int alpha = 0;
			for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
				for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
					int rgb = bufferedImage.getRGB(j2, j1);
					if (colorInRange(rgb)) {
						alpha = 0;
					} else {
						alpha = 255;
					}
					rgb = (alpha << 24) | (rgb & 0x00ffffff);
					bufferedImage.setRGB(j2, j1, rgb);
				}
			}
			g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());

			// 生成图片为PNG
			String outFile = destImagePath.substring(0, destImagePath.lastIndexOf("."));

			ImageIO.write(bufferedImage, "png", new File(outFile + ".png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean colorInRange(int color) {
		int red = (color & 0xff0000) >> 16;
		int green = (color & 0x00ff00) >> 8;
		int blue = (color & 0x0000ff);
		if (red >= color_range && green >= color_range && blue >= color_range) {
			return true;
		}
		return false;
	}

	public static int color_range = 210;

	/**
	 * 截取图片: 背景设置成白色
	 * 
	 * @param srcImageFile
	 *            原图片地址
	 * @param x
	 *            截取时的x坐标
	 * @param y
	 *            截取时的y坐标
	 * @param desWidth
	 *            截取的宽度
	 * @param desHeight
	 *            截取的高度
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
			e.printStackTrace();
		}
		return resultFileName;
	}
	
	
	/**
     * url转码
     *
     * @param str
     * @return
     */
    public static String getEncodeUrl(String str) throws UnsupportedEncodingException {
        String head = str.substring(0, str.lastIndexOf("/") + 1);
        String param = str.substring(str.lastIndexOf("/") + 1, str.length());
        String url = head + URLEncoder.encode(param, "UTF-8");
        // 空格会转换成+号，替换空格
        url = url.replace("+", "%20");
        return url;
    }
	
	
	/**
     * 图片获取
     *
     * @param imgUrl
     * @return
     */
    public static String editImage(String imgUrl, int x, int y, int width, int height) {
        try {
            // 必须要转码才能获取
//			getEncodeUrl(imgUrl)
            URL url = new URL(imgUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 得到输入流
            InputStream inputStream = conn.getInputStream();
            BufferedImage bi = ImageIO.read(inputStream);
            Image image = bi.getScaledInstance(bi.getWidth(), bi.getHeight(), Image.SCALE_DEFAULT);
            ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
            //原始到目标图片进行过滤
            Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));

            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(img, 0, 0, Color.WHITE, null);
            g.dispose();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(tag, "jpg", outputStream);
            return new String(Base64.encodeBase64(outputStream.toByteArray()));
        } catch (Exception e) {
     
        }
        return null;
    }

	/**
     * 图片获取
     *
     * @return
     */
    public static String editBase64(String base64, int x, int y, int width, int height) {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(base64);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(b);
            BufferedImage bi = ImageIO.read(inputStream);
            Image image = bi.getScaledInstance(bi.getWidth(), bi.getHeight(), Image.SCALE_DEFAULT);
            ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
            //原始到目标图片进行过滤
            Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));

            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(img, 0, 0, Color.WHITE, null);
            g.dispose();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(tag, "jpg", outputStream);
            return new String(Base64.encodeBase64(outputStream.toByteArray()));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
	/**
	 * 将图片不相干区域填成白色
	 *
	 *            画图模式
	 * @param width
	 *            感兴趣区/非感兴趣区宽度
	 * @param height
	 *            感兴趣区/非感兴趣区高度
	 * @param polygons
	 *            感兴趣区/非感兴趣区坐标
	 * @param destImagePath
	 *            目标图标路径
	 * @param file
	 *            源文件
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
			e.printStackTrace();
		}
	}

	/**
	 * 给图片添加水印文字、可设置水印文字的旋转角度
	 * 
	 * @param logoText
	 *            水印内容
	 * @param srcImgPath
	 *            原图片位置
	 * @param targerPath
	 *            生成图片保存位置
	 * @param degree
	 *            旋转角度
	 *            水印距图片下边距的距离
	 */
	public static void markImageByText(String logoText, String logoText1, String logoText2, String srcImgPath,
			String targerPath, Integer degree) {

		InputStream is = null;
		OutputStream os = null;
		try {
			// 1、源图片
			Image srcImg = ImageIO.read(new File(srcImgPath));
			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
					BufferedImage.TYPE_INT_RGB);
			int srcImgWidth = srcImg.getWidth(null);// 获取图片的宽
			int srcImgHeight = srcImg.getHeight(null);// 获取图片的高

			// 2、得到画笔对象
			Graphics2D g = buffImg.createGraphics();
			// 3、设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0,
					0, null);
			// 4、设置水印旋转
			if (null != degree) {
				g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
			}
			// 5、设置水印文字颜色
			Color color = Color.red;
			g.setColor(color);
			// 6、设置水印文字Font
			Font font = new Font("宋体", Font.BOLD, 20);
			g.setFont(font);
			// 7、设置水印文字透明度
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
			// 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
			int x = srcImgWidth - getWatermarkLength(logoText, g) - 20;
			int x1 = srcImgWidth - getWatermarkLength(logoText1, g) - 20;
			int x2 = srcImgWidth - getWatermarkLength(logoText2, g) - 20;
			g.drawString(logoText, x, srcImgHeight - 80);
			g.drawString(logoText1, x1, srcImgHeight - 50);
			g.drawString(logoText2, x2, srcImgHeight - 20);
			// 9、释放资源
			g.dispose();
			// 10、生成图片
			os = new FileOutputStream(targerPath);
			ImageIO.write(buffImg, "JPG", os);

			//System.out.println("图片完成添加水印文字");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != is) {
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (null != os) {
					os.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
		return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
	}

	/**
	* 将图片转换成Base64编码
	* @param imgFile 待处理图片
	* @return
	30      */
	public static String getImgStr(String imgFile){
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		//读取图片字节数组
		try{
			if(StringUtils.isNotEmpty(imgFile)){
		        // 创建URL
		        URL url = new URL(imgFile);
		        byte[] by = new byte[1024];
		        // 创建链接
		        URLConnection conn = url.openConnection();
		        conn.setConnectTimeout(5 * 1000);
		        InputStream is = conn.getInputStream();
		        // 将内容读取内存中
		        int len = -1;
		        while ((len = is.read(by)) != -1) {
		        	data.write(by, 0, len);
		        }
		        // 关闭流
		        is.close();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return new String(Base64.encodeBase64(data.toByteArray()));
	}
    /**
     * 将图片转换成Base64编码
     *
     * @param imgFile
     *            待处理图片
     * @return 30
     */
    public static String getImgStrWithPriffix(String imgFile) {
        String prefix = imgFile.substring(imgFile.lastIndexOf(".") + 1, imgFile.length());
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        // 读取图片字节数组
        try {
            if (StringUtils.isNotEmpty(imgFile)) {
                SSLContext sslcontext = null;
                // 创建URL
                URL url = null;
                if(imgFile.indexOf("https")!=-1){
                    try {
                        sslcontext = SSLContext.getInstance("SSL","SunJSSE");
                        try {
                            sslcontext.init(null, new TrustManager[]{new MyX509TrustManager()}, new java.security.SecureRandom());
                        } catch (KeyManagementException e) {
                            e.printStackTrace();
                        }
                        // 创建URL
                        url = new URL(imgFile);
                        HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
                            public boolean verify(String s, SSLSession sslsession) {
                                System.out.println("WARNING: Hostname is not matched for cert.");
                                return true;
                            }
                        };
                        HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
                        HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchProviderException e) {
                        e.printStackTrace();
                    }
                }else{
                    url = new URL(imgFile);
                }
                byte[] by = new byte[1024];
                // 创建链接
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(5 * 1000);
                InputStream is = conn.getInputStream();
                // 将内容读取内存中
                int len = -1;
                while ((len = is.read(by)) != -1) {
                    data.write(by, 0, len);
                }
                // 关闭流
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(imgFile.indexOf("https")!=-1){
            prefix = "jpg";
        }
        return "data:image/" + prefix + ";base64," + new String(Base64.encodeBase64(data.toByteArray()));
    }

	public InputStream downloadImgUrl(String imgUrl) throws IOException {
		URL url = new URL(imgUrl); // 必须要转码才能获取
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)"); // 防止屏蔽程序抓取而返回403错误
		InputStream inputStream = conn.getInputStream();
		return inputStream;
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
		return encode.encode(data).replaceAll("\\r|\\n","");
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


	public static void main(String[] args) {
		String ss = "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";
		String ss2 = "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";
		System.out.println("搜的出圖"+Base64.decodeBase64(ss.getBytes()).length);
		System.out.println("搜不出圖"+Base64.decodeBase64(ss2.getBytes()).length);
	}


	
}
