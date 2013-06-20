package controllers;

import java.io.IOException;

import models.*;
import play.Logger;
import play.cache.Cache;
import play.data.Form;
import play.libs.Images;
import play.mvc.Controller;
import play.mvc.Result;
import utils.PBUtils;
import views.html.index;

public class Application extends Controller {

	public static Form<PBLogin> userForm = Form.form(PBLogin.class);

	public static Result index() {
		Logger.info("call index");

		String uuid = session("uuid");
		if (uuid == null) {
			uuid = java.util.UUID.randomUUID().toString();
			session("uuid", uuid);
		}
		Logger.info("generate uuid :" + uuid);
		return ok(views.html.mlogin.render(userForm,uuid));
	}

	public static Result login() {
		String uuid = session("uuid");
		Form<PBLogin> fill = userForm.bindFromRequest();
		Logger.info("call login:" + fill.get().mobileNo + " "+ fill.get().passWord + " "+ fill.get().captCode);
        return ok(index.render("hello world"));
//		return ok(views.html.demo_html5.render());
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
	public static Result captcha(String uuid,String radom) throws IOException {
		Logger.info("call captcha" +uuid +" "+ radom);
		Images.Captcha captcha = Images.captcha();
		captcha.setBackground("#FF0000");
		String code = captcha.getText("#F4EAFD");
		// File f = Play.application().getFile("public/images/favicon.png");
		if(uuid != null)
		{
			Cache.set(uuid, code);
		}
//		File imageFile = File.createTempFile("pattern", ".png");
//		BufferedImage bi = ImageIO.read(captcha);
//		ImageIO.write(bi, "png", imageFile);
		// return ok(Play.application().getFile("public/images/favicon.png"));
		Logger.info("get session uuid:"+uuid + " captcha code is:" + code);
		return ok(PBUtils.getBytes(captcha)).as("jepg/png");
	}


    public static Form<PBUserInfo> userInfoForm = Form.form(PBUserInfo.class);

    public static Result getReg()
    {
        return ok(views.html.mreg.render(userInfoForm));
    }

    public static Result postReg() {
        Form<PBUserInfo> fill = userInfoForm.bindFromRequest();
        Logger.info("call postReg:" + fill.get().mobileNo + " "+ fill.get().passWord);
        return ok();
    }

    public static Result lifeTools()
    {
        return ok(views.html.mlifetool.render());
    }

}
