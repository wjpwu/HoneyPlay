package controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import models.PBLogin;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.data.Form;
import play.libs.Images;
import play.mvc.Controller;
import play.mvc.Result;
import utils.PBUtils;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;

public class MobileApp extends Controller {

	public static Form<PBLogin> userForm = Form.form(PBLogin.class);
	
	
	public static Result getUUID()
	{
		String uuid = session("uuid");
		if (uuid == null) {
			uuid = java.util.UUID.randomUUID().toString();
			session("uuid", uuid);
		}
		Logger.info("call sessionUUID" + uuid);
		return ok(uuid);
	}

	public static Result postLogin() {
		Form<PBLogin> fill = userForm.bindFromRequest();
		String uuid = session("uuid");
		Logger.info("call postLogin:" + uuid + "/r" +fill.get().uuid +"/r" + fill.get().mobileNo + "/r"+ fill.get().passWord + "/r" + fill.get().captCode);
		String sessionPtCode = (String)Cache.get(uuid+"captCode");
		Map map = new HashMap();
		if(sessionPtCode != null && !sessionPtCode.equals(fill.get().captCode))
		{
			return ok("<message status=\"1\">captCode is wrong</message>");
		}
		else if(!"13625001207".equals(fill.get().mobileNo)){
			return ok("<message status=\"1\">Mobile No is not found</message>");
		}
		else if(!"password".equals(fill.get().passWord)){
			return ok("<message status=\"1\">Password is wrong</message>");
		}
		return ok("<message status=\"0\" token =\""+ new Long(System.currentTimeMillis()).toString() + "\">Welcome</message>");
//		if (fill.hasErrors()) {
//			return badRequest(views.html.login.render(fill,uuid));
//		}
//		// get from session
//		if (session(fill.get().mobileNo) != null) {
//
//		}
//		PBUserInfo user = PBUserInfo.findByMobileNo(fill.get().mobileNo);
//		if (user.creditCardNo != null) {
//			Logger.info("uuid from session:" + uuid);
//			if (uuid == null) {
//				uuid = java.util.UUID.randomUUID().toString();
//				session("uuid", uuid);
//				Logger.info("generate uuid :" + uuid);
//			}
//			// Access the cache
//			Cache.set(uuid + "item.key", user);
//			return ok(views.html.welcome.render(user));
//		} else if (user.passWord != null)
//			return unauthorized("Bad password");
//		return unauthorized();
	}

	// generate unique code
	public static Result getCaptcha(String uuid) throws IOException {
		Logger.info("call captcha" + new Long(System.currentTimeMillis()).toString());
		Images.Captcha captcha = Images.captcha();
		captcha.setBackground("#A8A8A8");
		String code = captcha.getText("#F4EAFD");
		// File f = Play.application().getFile("public/images/favicon.png");
		if(uuid != null)
		{
			Cache.set(uuid+"captCode", code.toLowerCase());
		}
//		File imageFile = File.createTempFile("pattern", ".png");
//		BufferedImage bi = ImageIO.read(captcha);
//		ImageIO.write(bi, "png", imageFile);
//		// return ok(Play.application().getFile("public/images/favicon.png"));
		Logger.info("get session uuid:"+uuid + " captcha code is:" + code);
		return ok(PBUtils.getBytes(captcha)).as("image/png");
	}
	
	public static Result getItemMD5(String client, String file) {
		Logger.info("call getItemMD5" + client +" "+ file);
//		Assets.at("/public", "/mobile/menu/"+file);
		//get .xml
		if(file != null && file.endsWith(".xml"))
		{
			File f = 
//					VirtualFile.getFile("/public/mobile/menu/" + file).file();
					Play.application().getFile("/public/mobile/menu/" + file);
			if(f.exists() && f.isFile())
				return ok(PBUtils.getFileMD5(f));
			else return ok("<message status=\"1\">find is not exist</message>"
					+ Play.application().path().getAbsolutePath() + " /n "
					+ Play.application().path().getPath());
		}
		// get icons
		else if(file != null && (file.endsWith(".png") || file.equals(".jpg")))
		{
			File f = Play.application().getFile("/public/mobile/icons/" + file);
			if(f.exists() && f.isFile())
				return ok(PBUtils.getFileMD5(f));
			else return ok("<message status=\"1\">file is not exist</message>");
		}
		return ok("<message status=\"1\">no support for this file type</message>");
	}
	
	public static Result getItem(String client, String file)
	{
		Logger.info("call getItem" + client +" "+ file);
		//get .xml
		if(file != null && file.endsWith(".xml"))
		{
			File f = Play.application().getFile("/public/mobile/menu/" + file);
			if(f.exists() && f.isFile())
				return ok(f).as("xml");
			else return ok("<message status=\"1\">file is not exist</message>");
		}
		// get icons
		else if(file != null && (file.endsWith(".png") || file.equals(".jpg")))
		{
			File f = Play.application().getFile("/public/mobile/icons/" + file);
			if(f.exists() && f.isFile())
				return ok(f).as("jepg/png");
			else return ok("<message status=\"1\">file is not exist</message>");
		}
		return ok("<message status=\"1\">no support for this file type</message>");
	}
	
	
	public static Result getQRcodePng(String text) throws WriterException, IOException, NotFoundException
	{
		Logger.info("call getQRcodePng encode " + text);
		BufferedImage qrCode = PBUtils.zxingQRencode(text, 198, 198);
//		File logo = Play.application().getFile("/public/mobile/icons/icon_myfavorite_pressed.png");
		BufferedImage qrCodeLogo = PBUtils.addLogo(qrCode, "Pact");
		boolean readAble = true;
		//check it's readable or not
		try{
			PBUtils.zxingQRDecode(qrCode);
		}
		catch (NotFoundException e)
		{
			readAble = false;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( readAble ? qrCodeLogo : qrCode, "PNG", baos );
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();	
        return ok(imageInByte).as("image/png");
	}
	
	public static Result newUser() {
		return TODO;
	}
}
