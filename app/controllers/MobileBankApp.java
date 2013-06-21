package controllers;

import models.bank.PBTransfer;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class MobileBankApp extends Controller {

	public static Form<PBTransfer> transForm = Form.form(PBTransfer.class);

	public static Result index() {
		String uuid = session("uuid");
		if(true)
			return ok(views.html.bank.transfer.transferout.render(transForm));
		else return ok("<message status=\"1\">please do login!</message>");
	}
	public static Result transferOutA() {
		String uuid = session("uuid");
		Form<PBTransfer> fill = transForm.bindFromRequest();
		return ok(views.html.bank.transfer.transferoutAmount.render(transForm));
	}
	
	public static Result transferOutB() {
		String uuid = session("uuid");
		Form<PBTransfer> fill = transForm.bindFromRequest();
		return ok();
	}
}
