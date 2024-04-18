package com.jonastalk.common.component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

//import com.jonastalk.auth.v1.dto.EmailVerificationDto;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


/**
 * @name UtilComponent.java
 * @brief UtilComponent
 * @author Jonas Lim
 * @date 2023.10.31
 */
@Slf4j
@Getter
@Component
public class CommonUtilComponent {

	/**
	 * @brief Verification IPs
	 * @author Jonas Lim
	 * @date 2023.10.31
	 */
//    private Map<String, EmailVerificationDto> verificationIps = new HashMap<String, EmailVerificationDto>();
	
    /**
     * @brief Get Client IP Address
     * @author Jonas Lim
     * @date 2023.10.22
     * @param req
     * @return Client IP
     */
    public String getClientIp(HttpServletRequest request) {
    	if (request == null) return null;

	    String ip = request.getHeader("X-Forwarded-For");
	    // log.info("> X-FORWARDED-FOR : " + ip);

	    if (ip == null) {
	        ip = request.getHeader("Proxy-Client-IP");
	        // log.info("> Proxy-Client-IP : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	        // log.info(">  WL-Proxy-Client-IP : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getHeader("HTTP_CLIENT_IP");
	     // log.info("> HTTP_CLIENT_IP : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	     // log.info("> HTTP_X_FORWARDED_FOR : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getRemoteAddr();
	     // log.info("> getRemoteAddr : "+ip);
	    }
	    // log.info("> Result : IP Address : "+ip);

	    return ip;
	}
    
    
    /**
	 * @brief Bytes to Hex
	 * @author Jonas Lim
	 * @date 2023.10.31
     * @param hash
     * @return Hex Data
     */
	public String bytesToHex(byte[] hash) {
	    StringBuilder hexString = new StringBuilder(2 * hash.length);
	    for (int i = 0; i < hash.length; i++) {
	        String hex = Integer.toHexString(0xff & hash[i]);
	        if(hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
	
	/**
	 * @brief Make Alert And Redirect
	 * @author Jonas Lim
	 * @date 2023.10.31
	 * @param response
	 * @param messge
	 * @param href
	 * @throws IOException
	 */
	public void makeAlertAndRedirect(HttpServletResponse response, String messge, String href) throws IOException {
		
		if (href == null || href.isEmpty() || response == null) {
			href = "/home";
		}
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<script>alert('"+ messge +"'); location.href='"+ href + "';</script>");
		out.flush();
	}

	
	/**
	 * @brief Get IP Address
	 * @author Jonas Lim
	 * @date 2023.10.31
	 * @param request
	 * @return IP Address
	 */
	public String getIpAddress(HttpServletRequest request) {

        String ip = null;

        ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_CLIENT_IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-Real-IP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-RealIP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        }
		return ip;
	}
	
	/**
	 * @brief Get Random String
	 * @author Jonas Lim
	 * @date 2023.10.31
	 * @param length
	 * @return Random String
	 */
	public String getRandomString(int length) {
		return (new RandomString(length)).nextString();
	}
	
    
    /**
     * @name readFile(String filePath)
     * @brief Read File
     * @author Jonas Lim
     * @date 2023.11.28
     * @param filePath
     * @return
     */
    public byte[] readFile(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        byte[] content = fis.readAllBytes();
        fis.close();
        return content;
    }
	
	
	/**
	 * @name writeFile(String filePath, byte[] content)
	 * @brief Write File
	 * @author Jonas Lim
	 * @date 2023.11.28
	 * @param filePath
	 * @param content
	 * @return
	 */
    public void writeFile(String filePath, byte[] content) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(content);
        fos.close();
    }
    
    
    /**
	 * @name generateDate(int field, int amount)
	 * @brief Generate Date
	 * @author Jonas Lim
	 * @date 2023.11.29
	 * @param field
	 * @param amount
     * @return
     */
    public Date generateDate(int field, int amount) {
        Calendar c = Calendar.getInstance();
        c.add(field, amount);
        // c.add(Calendar.DATE, 1);         // 1일
        return c.getTime();
    }
    
    
    /**
	 * @name printStackTrace()
	 * @brief Print Stack Trace
	 * @author Jonas Lim
	 * @date 2024.02.01
     * @return
     */
    public void printStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        System.out.println("Stack Trace:");
        for (StackTraceElement element : stackTrace) {
            System.out.println(element);
        }
    }
    
    
    /**
	 * @name aggregateString(String delimiter, String... param)
	 * @brief aggregate String array
	 * @author Jonas Lim
	 * @date 2024.02.19
     * @return
     */
    public String aggregateString(String delimiter, String... param) {
    	if (!StringUtils.hasText(delimiter)) return null;
    	if (param == null || param.length == 0) return null;
    	StringBuilder stringBuilder = new StringBuilder();
    	boolean init = false;
    	for (String string : param) {
     		if (string == null) continue;
    		if (init) {
    			stringBuilder.append(delimiter);
    		} else {
        		init = true;
    		}
       		stringBuilder.append(string);
    	}
    	return stringBuilder.toString();
    }
}
