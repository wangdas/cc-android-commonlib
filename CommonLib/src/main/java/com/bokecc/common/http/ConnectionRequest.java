package com.bokecc.common.http;

import com.bokecc.common.application.ApplicationData;
import com.bokecc.common.exception.NetworkException;
import com.bokecc.common.exception.SdcardException;
import com.bokecc.common.http.bean.ResponseInfo;
import com.bokecc.common.http.listener.DownloadListener;
import com.bokecc.common.http.bean.NetworkInfo;
import com.bokecc.common.utils.Tools;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 联网底层类
 * （1）负责网络数据组装/发送/接收
 * 
 * @author wangyue
 * 
 */
public class ConnectionRequest {

	private final String TAG = "Common_ConnectionRequest";

	/**
	 * 联网类型枚举值
	 */
	protected enum RequestStatus {
		GET,//get请求
		POST,//post请求
		HEAD,//head请求
		PUT,//put请求
		DELETE,//delete请求
		POSTFILE,//上传文件
		POSTFILEBYTE,//上传文件通过byte
		ASYNCDOWNLOADFILE//下载文件
	}

	/** 连接超时时间 */
	private int TIMEOUT = 10000;

	/**是否取消联网获取数据 */
	private boolean isCancelled = false;

	/** 取消联网 */
	public void cancel(){
		isCancelled = true;
	}

	public void setTimeout(int timeout){
		TIMEOUT = timeout;
	}

	/**
	 * get请求
	 * @param httpUrl
	 * @param params
	 * @return
	 * @throws NetworkException
	 */
	public ResponseInfo doGet(String httpUrl,Map<String,Object> params,Map<String,Object> bodyParams,Map<String, Object> headers) throws NetworkException{
		return doRequest("GET",httpUrl,params,bodyParams,headers);
	}

	/**
	 * post请求
	 * @param httpUrl
	 * @param params
	 * @return
	 * @throws NetworkException
	 */
	public ResponseInfo doPost(String httpUrl,Map<String,Object> params,Map<String,Object> bodyParams,Map<String, Object> headers) throws NetworkException {
		return doRequest("POST",httpUrl,params,bodyParams,headers);
	}

	/**
	 * post请求
	 * @param httpUrl
	 * @param params
	 * @return
	 * @throws NetworkException
	 */
	public ResponseInfo doPut(String httpUrl,Map<String,Object> params,Map<String,Object> bodyParams,Map<String, Object> headers) throws NetworkException {
		return doRequest("PUT",httpUrl,params,bodyParams,headers);
	}

	/**
	 * DELETE请求
	 * @param httpUrl
	 * @param params
	 * @return
	 * @throws NetworkException
	 */
	public ResponseInfo doDelete(String httpUrl,Map<String,Object> params,Map<String,Object> bodyParams,Map<String, Object> headers) throws NetworkException {
		return doRequest("DELETE",httpUrl,params,bodyParams,headers);
	}

	/**
	 * HEAD请求
	 * @param httpUrl
	 * @param params
	 * @return
	 * @throws NetworkException
	 */
	public ResponseInfo doHead(String httpUrl,Map<String,Object> params,Map<String,Object> bodyParams,Map<String, Object> headers) throws NetworkException {
		return doRequest("HEAD",httpUrl,params,bodyParams,headers);
	}

	/**
	 * 联网请求
	 * @param method 请求方式
	 * @param httpUrl
	 * @param params
	 * @return
	 * @throws NetworkException
	 */
	private ResponseInfo doRequest(String method,String httpUrl,Map<String,Object> params,Map<String,Object> bodyParams,Map<String, Object> headers) throws NetworkException {
		Tools.log(TAG,"request:"+httpUrl+",request="+(params != null ? params.toString() : "")+",bodyParams="+(bodyParams != null ? bodyParams.toString() : "")+",headers="+headers.toString());
		HttpURLConnection urlConnection = null;
		OutputStream os = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			if(isCancelled)
				throw new NetworkException(NetworkException.TYPE_CLOSE);

			//判断是否有网络
			NetworkInfo networkInfo = Tools.getNetworkInfo();
			if(!networkInfo.isConnectToNetwork()){
				throw new NetworkException(NetworkException.TYPE_NOTNETWORK);
			}

			//拼接参数
			if(params!=null && params.size() > 0){
				StringBuilder sb = new StringBuilder();
				for (Map.Entry<String,Object> entry : params.entrySet()) {
					sb.append(entry.getKey()+"=");
					String value = String.valueOf(entry.getValue());
					if(value != null){
						sb.append(URLEncoder.encode(value,"UTF-8")+"&");
					}else{
						sb.append("&");
					}
				}
				httpUrl = httpUrl+"?"+sb.toString();
			}

			String paramsStr = "";
			if(bodyParams!=null && bodyParams.size() > 0){
				JSONObject jsonObject = new JSONObject(bodyParams);
				paramsStr = jsonObject.toString();
			}

//			if(!isPost){
//				if(params!=null && params.size() > 0){
//					StringBuilder sb = new StringBuilder();
//					for (Map.Entry<String,Object> entry : params.entrySet()) {
//						sb.append(entry.getKey()+"=");
//						String value = String.valueOf(entry.getValue());
//						if(value != null){
//							sb.append(URLEncoder.encode(value,"UTF-8")+"&");
//						}else{
//							sb.append("&");
//						}
//					}
//					paramsStr = sb.toString();
//				}
//				//get请求拼接请求地址
//				if(paramsStr != null && paramsStr.length() > 0){
//					httpUrl = httpUrl+"?"+paramsStr;
//				}
//			}else{
//				JSONObject jsonObject = new JSONObject(params);
//				paramsStr = jsonObject.toString();
//			}

			// 根据地址创建URL对象(网络访问的url)
			URL url = new URL(httpUrl);
			// url.openConnection()打开网络链接
			urlConnection = (HttpURLConnection) url.openConnection();
			//针对https增加认证
//            urlConnection.setSSLSocketFactory(getDefaultSSLSocketFactory());

			// 设置请求的方式
//			if(isPost){
//				urlConnection.setRequestMethod("POST");
//			}else{
//				urlConnection.setRequestMethod("GET");
//			}

			urlConnection.setRequestMethod(method);

			// 设置从主机读取数据超时时间
			urlConnection.setReadTimeout(TIMEOUT);
			// 设置连接主机超时时间
			urlConnection.setConnectTimeout(TIMEOUT);
			// 设置请求的头
			urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
			// 设置请求的头
			urlConnection.setRequestProperty("Connection","keep-alive");
			// 设置请求的头
			urlConnection.setRequestProperty("Content-Type","application/json");
			// 设置请求的头
			urlConnection.setRequestProperty("Content-Length",(String.valueOf(paramsStr.getBytes().length)));//只有post才有长度

			//设置自定义header
			for (Map.Entry<String,Object> entry : headers.entrySet()) {
				urlConnection.setRequestProperty(entry.getKey(),String.valueOf(entry.getValue()));
			}

			if(bodyParams!=null && bodyParams.size() > 0){
				// 发送POST请求必须设置允许输出
				urlConnection.setDoOutput(true);
				// 发送POST请求必须设置允许输入，setDoInput的默认值就是true
				urlConnection.setDoInput(true);

				//获取输出流
				os = urlConnection.getOutputStream();
				os.write(paramsStr.getBytes());
				os.flush();
			}

			if(isCancelled)
				throw new NetworkException(NetworkException.TYPE_CLOSE);

			// 获取响应的状态码
			int statusCode = urlConnection.getResponseCode();
			// 获取响应的输入流对象
			if (statusCode == 200) {
				is = urlConnection.getInputStream();
			}else {
				is = urlConnection.getErrorStream();
			}

				//判断网关是否加入了压缩
				String contentEncoding = urlConnection.getContentEncoding();
				if (contentEncoding != null){
					String value = contentEncoding.toLowerCase(Locale.ENGLISH);
					if(value.indexOf("gzip")!=-1){
						is = new GZIPInputStream(is);
					}
				}

				if(isCancelled)
					throw new NetworkException(NetworkException.TYPE_CLOSE);

				// 创建字节输出流对象
				baos = new ByteArrayOutputStream();
				// 定义读取的长度
				int len = 0;
				// 定义缓冲区
				byte buffer[] = new byte[1024];
				// 按照缓冲区的大小，循环读取
				while ((len = is.read(buffer)) != -1) {
					// 根据读取的长度写入到os对象中
					baos.write(buffer, 0, len);
				}

				// 返回字符串
				byte[] byteArray = baos.toByteArray();
				String responseStr = new String(byteArray,"utf-8");
				Tools.log(TAG,"response:"+url+",response="+responseStr);

				//获得Cookie的值
				Map<String, List<String>> responseHeaders = urlConnection.getHeaderFields();
				return new ResponseInfo(responseStr, responseHeaders);
//			}else{
//				throw new NetworkException(""+statusCode,NetworkException.TYPE_HTTPCODEERROR);
//			}
		}catch(NetworkException e){
			throw e;
		}catch(SocketTimeoutException e){
			throw new NetworkException(NetworkException.TYPE_TIMEOUT);
		}catch (Exception e) {
			throw new NetworkException(e.getMessage(),NetworkException.TYPE_OTHER);
		}finally{
			try {
				if(baos!=null){
					baos.close();
					baos = null;
				}
				if(is!=null){
					is.close();
					is = null;
				}
				if(os!=null){
					os.close();
					os = null;
				}
				if(urlConnection!=null){
					urlConnection.disconnect();
					urlConnection = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 方式一
	 * @return
	 */
	private static synchronized SSLSocketFactory getDefaultSSLSocketFactory() {
		try {
			//服务端公钥证书
			final String certName = "uwca.crt";

			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[]{
					new X509TrustManager() {
						public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

						}

						public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
							try{
								InputStream certInput = new BufferedInputStream(ApplicationData.getInstance().globalContext.getAssets().open(certName));
								CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
								final X509Certificate serverCert = (X509Certificate)certificateFactory.generateCertificate(certInput);

								for(X509Certificate cert: x509Certificates){
									cert.checkValidity();
									try {
										cert.verify(serverCert.getPublicKey());
									} catch (InvalidKeyException e) {
										e.printStackTrace();
									} catch (NoSuchAlgorithmException e) {
										e.printStackTrace();
									} catch (NoSuchProviderException e) {
										e.printStackTrace();
									} catch (SignatureException e) {
										e.printStackTrace();
									}
								}
							}catch (Exception e){
								e.printStackTrace();
							}
						}

						public X509Certificate[] getAcceptedIssuers() {
							return new X509Certificate[0];
						}
					}
			}, null);
			return sslContext.getSocketFactory();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

    /**
     * 方式二
     * @return
     */
    private static SSLSocketFactory getSocketFactory() {
        try {
			String certName = "uwca.crt";

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            InputStream is = ApplicationData.getInstance().globalContext.getAssets().open(certName);
            keyStore.setCertificateEntry("0", certificateFactory.generateCertificate(is));
            if (is!=null){
                is.close();
            }

            // 创建一个 TrustManager 仅把 Keystore 中的证书 作为信任的锚点
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //初始化keystore
            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(ApplicationData.getInstance().globalContext.getAssets().open("client.bks"), "123456".toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, "123456".toCharArray());

			SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * 上传文件
	 * @param url
	 * @param params
	 * @param files
	 * @return
	 * @throws NetworkException
	 */
	public String doPostFile(String url,Map<String,Object> params,Map<String,File> files,Map<String, Object> headers) throws NetworkException,SdcardException {
		HttpURLConnection urlConnection = null;
		DataOutputStream os = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			if(isCancelled)
				throw new NetworkException(NetworkException.TYPE_CLOSE);
			
			//判断网络是否可用
		    NetworkInfo networkInfo = Tools.getNetworkInfo();
		    if(!networkInfo.isConnectToNetwork()){
				throw new NetworkException(NetworkException.TYPE_NOTNETWORK);
			}
		    
			String BOUNDARY = java.util.UUID.randomUUID().toString();
			String PREFIX = "--", LINEND = "\r\n";
			String CHARSET = "UTF-8";

			URL uri = new URL(url);
			urlConnection = (HttpURLConnection) uri.openConnection();
			urlConnection.setReadTimeout(TIMEOUT); // 缓存的最长时间
			urlConnection.setDoInput(true);// 允许输入
			urlConnection.setDoOutput(true);// 允许输出
			urlConnection.setUseCaches(false); // 不允许使用缓存
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("connection", "keep-alive");
			urlConnection.setRequestProperty("Charsert", "UTF-8");
			urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
			
			//设置自定义header
            for (Map.Entry<String,Object> entry : headers.entrySet()) {
            	urlConnection.setRequestProperty(entry.getKey(),String.valueOf(entry.getValue()));
    		}

			os = new DataOutputStream(urlConnection.getOutputStream());
			
			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			if(params!=null){
//	            if(params!=null && params.size() > 0){
//	        		for (Map.Entry<String, String> entry : params.entrySet()) {
//	        			sb.append(entry.getKey()+"=");
//	        			sb.append(URLEncoder.encode(entry.getValue(),"UTF-8")+"&");
//	        		}
//	            }
	            
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINEND);
					sb.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"" + LINEND);
					sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
					sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
					sb.append(LINEND);
					sb.append(entry.getValue());
					sb.append(LINEND);
				}
				
	            os.write(sb.toString().getBytes());
			}

			// 发送文件数据
			if (files != null) {
				for (Map.Entry<String, File> file : files.entrySet()) {
					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
							+ file.getKey() + "\"" + LINEND);
					sb1.append("Content-Type: application/octet-stream; charset="
							+ CHARSET + LINEND);
					sb1.append(LINEND);
					os.write(sb1.toString().getBytes());

					FileInputStream fis = new FileInputStream(file.getValue());
					byte[] buffer = new byte[1024 * 1024 * 2];
					int len = 0;
					while ((len = fis.read(buffer)) != -1) {
						os.write(buffer, 0, len);
					}

					fis.close();
					os.write(LINEND.getBytes());
				}
			}

			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			os.write(end_data);
			os.flush();
			
			// 得到响应码
			int statusCode = urlConnection.getResponseCode();
			if (statusCode == 200) {
                is = urlConnection.getInputStream();

                //判断网关是否加入了压缩
				String contentEncoding = urlConnection.getContentEncoding();
 				if (contentEncoding != null){
					String value = contentEncoding.toLowerCase(Locale.ENGLISH);
					if(value.indexOf("gzip")!=-1){
						is = new GZIPInputStream(is);
					}
				}
 				
 			    // 创建字节输出流对象  
                baos = new ByteArrayOutputStream();
                // 定义读取的长度  
                int len = 0;  
                // 定义缓冲区  
                byte buffer[] = new byte[1024];  
                // 按照缓冲区的大小，循环读取  
                while ((len = is.read(buffer)) != -1) {  
                    // 根据读取的长度写入到os对象中  
                	baos.write(buffer, 0, len);  
                }  

                // 返回字符串  
                byte[] byteArray = baos.toByteArray();
                String response = new String(byteArray,"utf-8");
                
                if(isCancelled)
					throw new NetworkException(NetworkException.TYPE_CLOSE);

				return response;
			}else{
				throw new NetworkException(String.valueOf(statusCode),NetworkException.TYPE_HTTPCODEERROR);
			}
		}catch(SocketTimeoutException e){
			throw new NetworkException(NetworkException.TYPE_TIMEOUT);
		}catch (Exception e) {
			throw new NetworkException(e.getMessage(),NetworkException.TYPE_OTHER);
		}finally{
			try {
				if(baos!=null){
					baos.close();
				    baos = null;
			    }
			    if(is!=null){
				    is.close();
				    is = null;
			    }
			    if(os!=null){
			    	os.close();
			    	os = null;
			    }
			    if(urlConnection!=null){
			    	urlConnection.disconnect();
			    	urlConnection = null;
			    }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 上传文件
	 * @param url
	 * @param params
	 * @param files
	 * @return
	 * @throws NetworkException
	 */
	public String doPostFileByte(String url,Map<String,Object> params,Map<String,byte[]> files,Map<String, Object> headers) throws NetworkException,SdcardException {
		HttpURLConnection urlConnection = null;
		DataOutputStream os = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			if(isCancelled)
				throw new NetworkException(NetworkException.TYPE_CLOSE);

			//判断网络是否可用
			NetworkInfo networkInfo = Tools.getNetworkInfo();
			if(!networkInfo.isConnectToNetwork()){
				throw new NetworkException(NetworkException.TYPE_NOTNETWORK);
			}

			String BOUNDARY = java.util.UUID.randomUUID().toString();
			String PREFIX = "--", LINEND = "\r\n";
			String CHARSET = "UTF-8";

			URL uri = new URL(url);
			urlConnection = (HttpURLConnection) uri.openConnection();
			urlConnection.setReadTimeout(TIMEOUT); // 缓存的最长时间
			urlConnection.setDoInput(true);// 允许输入
			urlConnection.setDoOutput(true);// 允许输出
			urlConnection.setUseCaches(false); // 不允许使用缓存
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("connection", "keep-alive");
			urlConnection.setRequestProperty("Charsert", "UTF-8");
			urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

			//设置自定义header
			for (Map.Entry<String,Object> entry : headers.entrySet()) {
				urlConnection.setRequestProperty(entry.getKey(),String.valueOf(entry.getValue()));
			}

			os = new DataOutputStream(urlConnection.getOutputStream());

			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			if(params!=null){
//	            if(params!=null && params.size() > 0){
//	        		for (Map.Entry<String, String> entry : params.entrySet()) {
//	        			sb.append(entry.getKey()+"=");
//	        			sb.append(URLEncoder.encode(entry.getValue(),"UTF-8")+"&");
//	        		}
//	            }

				for (Map.Entry<String, Object> entry : params.entrySet()) {
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINEND);
					sb.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"" + LINEND);
					sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
					sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
					sb.append(LINEND);
					sb.append(entry.getValue());
					sb.append(LINEND);
				}

				os.write(sb.toString().getBytes());
			}

			// 发送文件数据
			if (files != null)
				for (Map.Entry<String, byte[]> file : files.entrySet()) {
					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
							+ file.getKey() + "\"" + LINEND);
					sb1.append("Content-Type: application/octet-stream; charset="
							+ CHARSET + LINEND);
					sb1.append(LINEND);
					os.write(sb1.toString().getBytes());


					byte[] data = file.getValue();
					os.write(data, 0, data.length);
					os.write(LINEND.getBytes());
				}

			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			os.write(end_data);
			os.flush();

			// 得到响应码
			int statusCode = urlConnection.getResponseCode();
			if (statusCode == 200) {
				is = urlConnection.getInputStream();

				//判断网关是否加入了压缩
				String contentEncoding = urlConnection.getContentEncoding();
				if (contentEncoding != null){
					String value = contentEncoding.toLowerCase(Locale.ENGLISH);
					if(value.indexOf("gzip")!=-1){
						is = new GZIPInputStream(is);
					}
				}

				// 创建字节输出流对象
				baos = new ByteArrayOutputStream();
				// 定义读取的长度
				int len = 0;
				// 定义缓冲区
				byte buffer[] = new byte[1024];
				// 按照缓冲区的大小，循环读取
				while ((len = is.read(buffer)) != -1) {
					// 根据读取的长度写入到os对象中
					baos.write(buffer, 0, len);
				}

				// 返回字符串
				byte[] byteArray = baos.toByteArray();
				String response = new String(byteArray,"utf-8");

				if(isCancelled)
					throw new NetworkException(NetworkException.TYPE_CLOSE);

				return response;
			}else{
				throw new NetworkException(String.valueOf(statusCode),NetworkException.TYPE_HTTPCODEERROR);
			}
		}catch(SocketTimeoutException e){
			throw new NetworkException(NetworkException.TYPE_TIMEOUT);
		}catch (Exception e) {
			throw new NetworkException(e.getMessage(),NetworkException.TYPE_OTHER);
		}finally{
			try {
				if(baos!=null){
					baos.close();
					baos = null;
				}
				if(is!=null){
					is.close();
					is = null;
				}
				if(os!=null){
					os.close();
					os = null;
				}
				if(urlConnection!=null){
					urlConnection.disconnect();
					urlConnection = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 分段下载并写文件
	 * @param url
	 * @param filePath
	 * @param currentLength 当前下载位置
	 * @param targetLength 目标下载位置
	 * @param downloadListener 下载进度监听
	 * @return
	 * @throws NetworkException
	 */
	public byte[] doDownloadFile(String url,String filePath,long currentLength,long targetLength,DownloadListener downloadListener,Map<String, Object> headers) throws NetworkException,SdcardException {
		HttpURLConnection urlConnection = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		RandomAccessFile raFile = null;
		try {
			URL u = new URL(url);
			urlConnection = (HttpURLConnection) u.openConnection();
			// 设置请求的方式
			urlConnection.setRequestMethod("GET");
			// 设置请求的超时时间
			urlConnection.setReadTimeout(TIMEOUT);
			urlConnection.setConnectTimeout(TIMEOUT);
			// 设置请求的头
			urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
			// 设置请求的头
			urlConnection.setRequestProperty("Connection", "keep-alive");
			// 设置请求的头
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//设置断点长度
			if (targetLength > 0) {
				urlConnection.setRequestProperty("RANGE", "bytes=" + currentLength + "-" + targetLength);
			} else {
				urlConnection.setRequestProperty("RANGE", "bytes=" + currentLength + "-");
			}

			//设置自定义header
			for (Map.Entry<String, Object> entry : headers.entrySet()) {
				urlConnection.setRequestProperty(entry.getKey(), String.valueOf(entry.getValue()));
			}

			//开始连接
			urlConnection.connect();

			if (isCancelled)
				throw new NetworkException(NetworkException.TYPE_CLOSE);

			int statusCode = urlConnection.getResponseCode();
			if (statusCode == 200 || statusCode == 206) {
				// 得到数据总长度
				String val = urlConnection.getHeaderField("Content-Range");
				long totalLength = 0;
				if (val != null && val.length() > 0) {
					totalLength = Long.parseLong(val.substring(val.indexOf("/") + 1));
				}

				// 得到数据长度
				long downloadLength = urlConnection.getContentLength();

				is = urlConnection.getInputStream();

				// 判断网关是否加入了压缩
				String contentEncoding = urlConnection.getContentEncoding();
				if (contentEncoding != null) {
					String value = contentEncoding.toLowerCase(Locale.ENGLISH);
					if (value.indexOf("gzip") != -1) {
						is = new GZIPInputStream(is);
					}
				}

				if (isCancelled)
					throw new NetworkException(NetworkException.TYPE_CLOSE);

				bis = new BufferedInputStream(is);

				File file = new File(filePath);

				try{
					//创建文件夹
					File fold = file.getParentFile();
					if (null == fold || !fold.isDirectory()) {
						fold.mkdirs();
					}

					//创建文件
					if (null == file || !file.exists()) {
						file.createNewFile();
					}
					//写文件
					raFile = new RandomAccessFile(file, "rw");
					raFile.seek(currentLength);
				}catch (IOException e){
					throw new SdcardException(SdcardException.SDCARD_ERROR);
				}

				long cLength = 0;
				byte[] data = new byte[1024 * 5];

				if (Tools.getSdcardFreeSize(Tools.getRootPath()) > data.length) {
					while (cLength < downloadLength) {
						int bytesRead = bis.read(data, 0, data.length);
						if (bytesRead == -1)
							break;
						raFile.write(data, 0, bytesRead);
						cLength += bytesRead;
						//回调
						if (downloadListener != null) {
							downloadListener.uploadProgress(totalLength, currentLength + cLength);
						}
						if (isCancelled)
							break;
					}
				}else{
					throw new SdcardException(SdcardException.SDCARD_FULL);
				}
			} else {
				throw new NetworkException(String.valueOf(statusCode),NetworkException.TYPE_HTTPCODEERROR);
			}
		} catch (SdcardException e) {//内存卡错误，读写失败
			throw e;
		} catch (NetworkException e){//网络错误，httpcode，cancel
			throw new NetworkException(e.getMessage(),e.getErrorCode());
		} catch (UnknownHostException e){//域名解析错误
			throw new NetworkException(NetworkException.TYPE_UNKNOWHOST);
		} catch (Exception e) {
			throw new NetworkException(e.getMessage(),NetworkException.TYPE_OTHER);
		} finally {
			try {
				if (raFile != null) {
					raFile.close();
					raFile = null;
				}
				if (bis != null) {
					bis.close();
					bis = null;
				}
				if (is != null) {
					is.close();
					is = null;
				}
				if(urlConnection!=null){
					urlConnection.disconnect();
					urlConnection = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	//	/**
//	 * 访问网络
//	 * @param requestType
//	 * @param httpUrl
//	 * @param params
//	 * @param headers
//	 * @return
//	 * @throws IOException
//	 * @throws NetworkException
//	 */
//	protected ResponseInfo doRequest(RequestStatus requestType, String httpUrl, Map<String,String> params, Map<String, String> headers) throws IOException,NetworkException{
//		Response response = null;
//		try {
//			if(isCancelled)
//				throw new NetworkException(NetworkException.TYPE_CLOSE);
//
//			//判断是否有网络
//			NetworkInfo networkInfo = Tools.getNetworkInfo();
//			if(!networkInfo.isConnectToNetwork()){
//				throw new NetworkException(NetworkException.TYPE_NOTNETWORK);
//			}
//
//			//拼接参数
//
//			//get/head参数
//			String paramsStr = "";
//			//post/put/delete
//			if(requestType == RequestStatus.GET || requestType == RequestStatus.PUT){
//				if(params!=null && params.size() > 0){
//					StringBuilder sb = new StringBuilder();
//					for (Map.Entry<String,String> entry : params.entrySet()) {
//						sb.append(entry.getKey()+"=");
//						String value = entry.getValue();
//						if(value != null){
//							sb.append(URLEncoder.encode(value,"UTF-8")+"&");
//						}else{
//							sb.append("&");
//						}
//					}
//					paramsStr = sb.toString();
//				}
//			}else{
//				if(params!=null && params.size() > 0){
//					JSONObject jsonObject = new JSONObject();
//					for (Map.Entry<String,String> entry : params.entrySet()) {
//						jsonObject.put(entry.getKey(),entry.getValue());
//					}
//					paramsStr = jsonObject.toString();
//				}
//			}
//
//			//开启访问
//			OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
//			Request.Builder builder = new Request.Builder();
//			//设置自定义header
//			for (Map.Entry<String,String> entry : headers.entrySet()) {
//				builder.addHeader(entry.getKey(),entry.getValue());
//			}
//			switch (requestType){
//				case GET:
//					builder.url(httpUrl+"?"+paramsStr);
//					builder.get();
//					break;
//				case HEAD:
//					builder.url(httpUrl+"?"+paramsStr);
//					builder.head();
//					break;
//				case POST:
//					builder.url(httpUrl);
//					builder.post(RequestBody.create(MediaType.parse("application/json"), paramsStr));
//					break;
//				case PUT:
//					builder.url(httpUrl);
//					builder.put(RequestBody.create(MediaType.parse("application/json"), paramsStr));
//				case DELETE:
//					builder.url(httpUrl);
//					builder.delete(RequestBody.create(MediaType.parse("application/json"), paramsStr));
//					break;
//			}
//
//			Request request = builder.build();//创建Request 对象
//			response = client.newCall(request).execute();//得到Response对象
//			int code = response.code();
//
//			if (response.isSuccessful()) {
//				if(isCancelled)
//					throw new NetworkException(NetworkException.TYPE_CLOSE);
//				Headers responseHeaders = response.headers();
//				String responseStr = response.body().string();
//				return new ResponseInfo(responseStr, responseHeaders);
//			}else{
//				throw new NetworkException(""+code,NetworkException.TYPE_HTTPCODEERROR);
//			}
//		}catch(NetworkException e){
//			throw e;
//		}catch(SocketTimeoutException e){
//			throw new NetworkException(NetworkException.TYPE_TIMEOUT);
//		}catch (Exception e) {
//			throw new NetworkException(e.getMessage(),NetworkException.TYPE_OTHER);
//		}finally{
//			if(response!=null){
//				response.close();
//			}
//		}
//	}
	
}
