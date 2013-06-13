package utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.BufferedImageLuminanceSource;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.swetake.util.Qrcode;

public class PBUtils {

	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	public static byte[] getBytes(InputStream is) throws IOException {
		byte[] data = null;

		Collection<byte[]> chunks = new ArrayList<byte[]>();
		byte[] buffer = new byte[1024 * 1000];
		int read = -1;
		int size = 0;

		while ((read = is.read(buffer)) != -1) {
			if (read > 0) {
				byte[] chunk = new byte[read];
				System.arraycopy(buffer, 0, chunk, 0, read);
				chunks.add(chunk);
				size += chunk.length;
			}
		}

		if (size > 0) {
			ByteArrayOutputStream bos = null;
			try {
				bos = new ByteArrayOutputStream(size);
				for (Iterator<byte[]> itr = chunks.iterator(); itr.hasNext();) {
					byte[] chunk = (byte[]) itr.next();
					bos.write(chunk);
				}
				data = bos.toByteArray();
			} finally {
				if (bos != null) {
					bos.close();
				}
			}
		}
		return data;
	}

	
	public static String zxingQRDecode(BufferedImage image) throws NotFoundException
	{
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Map map = new HashMap<DecodeHintType, Object>();
		map.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		Result res = new MultiFormatReader().decode(bitmap,map);
		return res.getText();
	}
	
	private static final int BLACK = 0xff000000;
	private static final int WHITE = 0xFFFFFFFF;

	public static BufferedImage zxingQRencode(String text, int width, int height)
			throws WriterException {
		Map map = new HashMap<EncodeHintType, Object>();
		map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		map.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//		map.put(EncodeHintType.MIN_VERFSION, "5");
		BitMatrix byteMatrix = new MultiFormatWriter().encode(text,
				BarcodeFormat.QR_CODE, width, height,map);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, byteMatrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public static BufferedImage addLogo(BufferedImage img2, String logostr) {
		
        double width = img2.getWidth();
        double height = img2.getHeight();
        
        Graphics2D gmimi2 = img2.createGraphics();

        // add logo by string
        BufferedImage logoImg = new java.awt.image.BufferedImage(54, 24,BufferedImage.TYPE_USHORT_565_RGB);
        double logoWidth = logoImg.getWidth();
        double logoHeight = logoImg.getHeight();
        Graphics2D logo2d = logoImg.createGraphics();
        logo2d.fillRoundRect(0, 0, 54,24, 0, 0);
        logo2d.setColor(Color.DARK_GRAY);
        logo2d.fillRoundRect(2, 2, 50,20, 0, 0);

//        logo2d.c
//        logo2d.clearRect(0, 0, 55, 25);
        // draw text
        logo2d.setPaint(Color.BLUE);
        logo2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm1 = logo2d.getFontMetrics();
        int x1 = logoImg.getWidth() - fm1.stringWidth(logostr) - 10;
        int y1 = fm1.getHeight();
        logo2d.drawString(logostr, x1, y1);
        logo2d.dispose();
        
        // add logo
        gmimi2.drawImage(logoImg, null, (int)(width - logoWidth)/2, (int)(height - logoHeight)/2);
        
        BufferedImage imgmini = new java.awt.image.BufferedImage(200, 200,BufferedImage.TYPE_USHORT_565_RGB);

        int miniwidth = 200;//缩略图宽度
        int miniheight = 200;//缩略图高度
        
        Graphics2D g2d = imgmini.createGraphics();
        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, miniwidth, miniheight);
        g2d.drawImage(img2, null,  (int) (miniwidth - (width)) / 2, (int) (miniheight - (height)) / 2);
        // draw text
        g2d.setPaint(Color.DARK_GRAY);
        g2d.setFont(new Font("Serif", Font.BOLD, 20));
        String s = "Hello QRCode !";
        FontMetrics fm = g2d.getFontMetrics();
        int x = imgmini.getWidth() - fm.stringWidth(s) - 25;
        int y = fm.getHeight();
        g2d.drawString(s, x, y);
        g2d.dispose();
        return imgmini;
	}

	public static BufferedImage addLogo(BufferedImage img2, BufferedImage logo) {
		
        double width = img2.getWidth();
        double height = img2.getHeight();
        
        double logoWidth = logo.getWidth();
        double logoHeight = logo.getHeight();
        Graphics2D gmimi2 = img2.createGraphics();

        // add logo by string
        BufferedImage logoImg = new java.awt.image.BufferedImage(60, 25,BufferedImage.TYPE_USHORT_565_RGB);
        logoWidth = logoImg.getWidth();
        logoHeight = logoImg.getHeight();
        Graphics2D logo2d = logoImg.createGraphics();
        logo2d.setBackground(Color.gray);
        logo2d.clearRect(0, 0, 55, 25);
        // draw text
        logo2d.setPaint(Color.BLUE);
        logo2d.setFont(new Font("Serif", Font.BOLD, 20));
        String logostr = "Pact";
        FontMetrics fm1 = logo2d.getFontMetrics();
        int x1 = logoImg.getWidth() - fm1.stringWidth(logostr) - 10;
        int y1 = fm1.getHeight();
        logo2d.drawString(logostr, x1, y1);
        logo2d.dispose();
        
        // add logo
        gmimi2.drawImage(logoImg, null, (int)(width - logoWidth)/2, (int)(height - logoHeight)/2);
        
        BufferedImage imgmini = new java.awt.image.BufferedImage(300, 300,BufferedImage.TYPE_USHORT_565_RGB);

        int miniwidth = 300;//缩略图宽度
        int miniheight = 300;//缩略图高度
        
        Graphics2D g2d = imgmini.createGraphics();
        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, miniwidth, miniheight);
        g2d.drawImage(img2, null,  (int) (miniwidth - (width)) / 2, (int) (miniheight - (height)) / 2);
        // draw text
        g2d.setPaint(Color.DARK_GRAY);
        g2d.setFont(new Font("Serif", Font.BOLD, 20));
        String s = "QRCode Created from Aaron!";
        FontMetrics fm = g2d.getFontMetrics();
        int x = imgmini.getWidth() - fm.stringWidth(s) - 5;
        int y = fm.getHeight();
        g2d.drawString(s, x, y);
        g2d.dispose();
        return imgmini;
	}

	/**
	 * 生成二维码(QRCode)图片的公共方法
	 * 
	 * @param content
	 *            存储内容
	 * @param imgType
	 *            图片类型
	 * @param size
	 *            二维码尺寸
	 * @return
	 */
	private BufferedImage qRCodeCommon(String content, int size) {
		BufferedImage bufImg = null;
		try {
			Qrcode qrcodeHandler = new Qrcode();
			// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
			qrcodeHandler.setQrcodeErrorCorrect('M');
			qrcodeHandler.setQrcodeEncodeMode('B');
			// 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
			qrcodeHandler.setQrcodeVersion(size);
			// 获得内容的字节数组，设置编码格式
			byte[] contentBytes = content.getBytes("UTF-8");
			// 图片尺寸
			int imgSize = 67 + 12 * (size - 1);
			bufImg = new BufferedImage(imgSize, imgSize,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();
			// 设置背景颜色
			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, imgSize, imgSize);
			// 设定图像颜色> BLACK
			gs.setColor(Color.BLACK);
			// 设置偏移量，不设置可能导致解析出错
			int pixoff = 2;
			// 输出内容> 二维码
			if (contentBytes.length > 0 && contentBytes.length < 800) {
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			} else {
				throw new Exception("QRCode content bytes length = "
						+ contentBytes.length + " not in [0, 800].");
			}
			gs.dispose();
			bufImg.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufImg;
	}

	/***
	 * 
	 * @param code
	 * @param versionLevel
	 *            v5 <=84,84 v7 <=120,98
	 * 
	 * @return
	 */
	public static BufferedImage qrEncode(String content) {
		BufferedImage bufImg = null;
		int version = 5;
		int imgSize = 113;
		byte[] bs = content.getBytes();
		if (bs.length > 0 && bs.length <= 84)
			version = 5;
		else if (bs.length > 84 && bs.length <= 120)
			version = 7;
		Qrcode qrcodeHandler = new Qrcode();
		// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
		qrcodeHandler.setQrcodeErrorCorrect('M');
		qrcodeHandler.setQrcodeEncodeMode('B');
		qrcodeHandler.setQrcodeVersion(version);
		bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D gs = bufImg.createGraphics();
		// 设置背景颜色
		gs.setBackground(Color.WHITE);
		gs.clearRect(0, 0, imgSize, imgSize);
		// 设定图像颜色> BLACK
		gs.setColor(Color.BLACK);
		// 设置偏移量，不设置可能导致解析出错
		int pixoff = 2;
		// 输出内容> 二维码
		boolean[][] codeOut = qrcodeHandler.calQrcode(bs);
		for (int i = 0; i < codeOut.length; i++) {
			for (int j = 0; j < codeOut.length; j++) {
				if (codeOut[j][i]) {
					gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
				}
			}
		}
		gs.dispose();
		bufImg.flush();
		return bufImg;
	}

	/**
	 * Encoding the information to a QRCode, size of the information must be
	 * less than 84 byte.
	 * 
	 * @param srcValue
	 * @param qrcodePicfilePath
	 * @return
	 */
	public static boolean encode_84(String srcValue, String qrcodePicfilePath) {
		int MAX_DATA_LENGTH = 84; // 限制生成二维码的数据最大大小
		byte[] d = srcValue.getBytes();
		int dataLength = d.length;
		int imageWidth = 113; /*
							 * 113是预先计算出来的.
							 * 注意：图像宽度必须比生成的二维码图像宽度大,至少相等,否则,二维码识别不出来
							 */
		int imageHeight = imageWidth;
		BufferedImage bi = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, imageWidth, imageHeight);
		g.setColor(Color.BLACK);
		if (dataLength > 0 && dataLength <= MAX_DATA_LENGTH) {
			/* 生成二维码 */
			Qrcode qrcode = new Qrcode();
			qrcode.setQrcodeErrorCorrect('M'); // L, Q, H, 默认
			qrcode.setQrcodeEncodeMode('B'); // A, N, 默认
			qrcode.setQrcodeVersion(5); // 37字节, (37-1)*3+2+3-1+1 = 113
			boolean[][] b = qrcode.calQrcode(d);
			int qrcodeDataLen = b.length;
			for (int i = 0; i < qrcodeDataLen; i++) {
				for (int j = 0; j < qrcodeDataLen; j++) {
					if (b[j][i]) {
						g.fillRect(j * 3 + 2, i * 3 + 2, 3, 3); /*
																 * 画二维码图形,
																 * 画出的图形宽度是
																 * ((qrcodeDataLen
																 * -1)*3+2) + 3
																 * -1 ;
																 * 生成的image的宽度大小必须
																 * >=该值,
																 * 外围的1个像素用来标识此块区域为二维码
																 */
						/*
						 * fillRect(int x, int y, int width, int height) 函数作用：
						 * 填充指定的矩形。该矩形左边和右边位于 x 和 x + width - 1。顶边和底边位于 y 和 y +
						 * height - 1。 得到的矩形覆盖的区域宽度为 width 像素，高度为 height 像素。
						 * 使用图形上下文的当前颜色填充该矩形。 参数： x - 要填充矩形的 x 坐标。 y - 要填充矩形的 y
						 * 坐标。 width - 要填充矩形的宽度。 height - 要填充矩形的高度。
						 * 
						 * 参考：http://bk.chinaar.com/index.php?doc-view-2999
						 */
					}
				}
			}
			System.out.println("二维码数据长度(字节):" + qrcodeDataLen);
		} else {
			System.out.println("Generate QRCode image error! Data size is "
					+ dataLength + ", it is lager than 84 bytes.");
			return false;
		}
		g.dispose();
		bi.flush();
		/* generate image */
		File f = new File(qrcodePicfilePath);
		String suffix = f.getName().substring(f.getName().indexOf(".") + 1,
				f.getName().length());
		try {
			ImageIO.write(bi, suffix, f); // "png"
		} catch (IOException ioe) {
			System.out.println("Generate QRCode image error!"
					+ ioe.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * Encoding the information to a QRCode, size of the information must be
	 * less tah 500 byte.
	 * 
	 * @param srcValue
	 * @param qrcodePicfilePath
	 * @return
	 */
	public static boolean encode_500(String srcValue, String qrcodePicfilePath) {
		int MAX_DATA_LENGTH = 500; // 限制生成二维码的数据最大大小. 500字节的原始数据, 生成二维码时, 是89宽度
		byte[] d = srcValue.getBytes();
		int dataLength = d.length;
		int imageWidth = 269; /*
							 * 269是预先计算出来的.
							 * 注意：图像宽度必须比生成的二维码图像宽度大,至少相等,否则,二维码识别不出来
							 */
		int imageHeight = imageWidth;
		BufferedImage bi = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, imageWidth, imageHeight);
		g.setColor(Color.BLACK);
		if (dataLength > 0 && dataLength <= MAX_DATA_LENGTH) {
			/* 生成二维码 */
			Qrcode qrcode = new Qrcode();
			qrcode.setQrcodeErrorCorrect('M'); // L, Q, H, 默认
			qrcode.setQrcodeEncodeMode('B'); // A, N, 默认
			qrcode.setQrcodeVersion(18); // 0<= version <=40; 89字节,
											// (89-1)*3+2+3-1+1 = 269
			boolean[][] b = qrcode.calQrcode(d);
			int qrcodeDataLen = b.length;
			for (int i = 0; i < qrcodeDataLen; i++) {
				for (int j = 0; j < qrcodeDataLen; j++) {
					if (b[j][i]) {
						g.fillRect(j * 3 + 2, i * 3 + 2, 3, 3); /*
																 * 画二维码图形,
																 * 画出的图形宽度是
																 * ((qrcodeDataLen
																 * -1)*3+2) + 3
																 * -1 = 136;
																 * 生成的image的宽度大小必须
																 * >=(136+1),
																 * 外围的1个像素用来标识此块区域为二维码
																 */
						/*
						 * fillRect(int x, int y, int width, int height) 函数作用：
						 * 填充指定的矩形。该矩形左边和右边位于 x 和 x + width - 1。顶边和底边位于 y 和 y +
						 * height - 1。 得到的矩形覆盖的区域宽度为 width 像素，高度为 height 像素。
						 * 使用图形上下文的当前颜色填充该矩形。 参数： x - 要填充矩形的 x 坐标。 y - 要填充矩形的 y
						 * 坐标。 width - 要填充矩形的宽度。 height - 要填充矩形的高度。
						 * 
						 * 参考：http://bk.chinaar.com/index.php?doc-view-2999
						 */
					}
				}
			}
			System.out.println("二维码数据长度(字节):" + qrcodeDataLen);
		} else {
			return false;
		}
		g.dispose();
		bi.flush();
		/* generate image */
		File f = new File(qrcodePicfilePath);
		String suffix = f.getName().substring(f.getName().indexOf(".") + 1,
				f.getName().length());
		System.out.println(suffix);
		try {
			ImageIO.write(bi, suffix, f); // "png"
		} catch (IOException ioe) {
			System.out.println("Generate QRCode image error!"
					+ ioe.getMessage());
			return false;
		}

		return true;
	}

	public static void main(String[] args) throws Exception {

		String data = "家常菜郭林林家常菜郭林家常菜郭林郭林家常菜郭林家常菜郭林家常菜郭林郭林家常菜郭林郭林";
		System.out.println("字节数: " + data.getBytes().length);
		// encode84(data, "A.JPG");
		// System.out.println(decode("A.JPG"));
	}
}
