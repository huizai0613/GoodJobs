package cn.goodjobs.common.util.sharedpreferences;

public class DesEncrypt {
	private final static String KEY = "03285445" ;
	/**
	 *   加密String明文输入,String密文输出
	 *   @param   strMing
	 *   @return
	 */
	public static String getEncString(String strMing) {
		try {
			return DES.encryptDES(KEY, strMing) ;
		} catch (Exception e) {
			e.printStackTrace() ;
		}
		return "" ;
	}

	/**
	 *   解密   以String密文输入,String明文输出
	 *   @param   strMi
	 *   @return
	 */
	public static String getDesString(String strMi) {
		try {
			return DES.decryptDES(KEY, strMi) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "" ;
	}
}
