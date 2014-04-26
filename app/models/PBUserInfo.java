package models;

public class PBUserInfo {

	public String familyName;
	
	public String firstName;
	
	public Integer sex;
	
	public String mobileNo;
	
	public String creditCardNo;
	
	public String passWord;

    public String passWordrepead;

    public String session;

    public static void create(){
		
	}
	
	public static PBUserInfo findByMobileNo(String mobile)
	{
		PBUserInfo tmp = new PBUserInfo();
		tmp.familyName = "Wu";
		tmp.firstName = "Aaron";
		tmp.sex = 2;
		tmp.mobileNo = "13625001207";
		tmp.creditCardNo = "6225885823016273";
		return tmp;
	}
	
	public static PBUserInfo userWithLoginInfo(String mobile,String passWord)
	{
		PBUserInfo tmp = new PBUserInfo();
		tmp.familyName = "Wu";
		tmp.firstName = "Aaron";
		tmp.sex = 2;
		tmp.mobileNo = "13625001207";
		tmp.creditCardNo = "6225885823016273";
		return tmp;
	}
}
