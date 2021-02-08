package com.example.demo;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.example.demo.templateCreate.SummaryDto;

public class GitlabSendRequest {

	public static String HOST = "proxy.hoge.jp";
	public static int PORT = 0000;
	public static String USER = "xxxxx";
	public static String PASSWORD = "xxxxx";
	private final Charset charset = StandardCharsets.UTF_8;
	private final String GIT_URL = "https://x.x.x.x/api/v4";
	private final String TOKEN = "xxxx";
	private final String GROUP_ID = "4";


	/**
	 * ISSUEの統計情報を取得
	 * @return
	 * @throws Exception
	 */
	public SummaryDto getIssueStatistics() throws Exception{

	    String strGetUrl = GIT_URL + "/groups/" + GROUP_ID + "/issues_statistics?private_token=" + TOKEN;
    	String todoStr  = sendGetRequest(strGetUrl + "&state=opened&labels=To%20Do");
    	String doingStr = sendGetRequest(strGetUrl + "&state=opened&labels=Doing");
    	String doneStr  = sendGetRequest(strGetUrl + "&state=opened&labels=Done");
    	String openAndCloseStr = sendGetRequest(strGetUrl);

		LocalDate today = LocalDate.now();
		Util util = new Util();

    	int todo = util.returnOpenedFromStr(todoStr);
    	int doing = util.returnOpenedFromStr(doingStr);
    	int done = util.returnOpenedFromStr(doneStr);

    	int openAndClosedArray[] = util.returnOpenedAndClosedFromStr(openAndCloseStr);
    	int opened = openAndClosedArray[0] - todo - doing - done;
    	int closed = openAndClosedArray[1];

		return new SummaryDto(today, opened, todo, doing, done, closed);
	}


	/**
	 * リクエストを送信する（ボディとヘッダーのうちの一部を返却）
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String[] sendHeadRequest(String url) throws Exception {
		String ret[] = new String[2];
		ret[0] = ""; //body
		ret[1] = ""; // X-Next-Page

		HttpClient httpclient = getHttpClient();

		HttpGet get = new HttpGet(url);
		HttpResponse res = httpclient.execute(get);
		int status = res.getStatusLine().getStatusCode();
		if (status == HttpStatus.SC_OK){
			ret[0] = EntityUtils.toString(res.getEntity(),charset);
			Header[] headers = res.getHeaders("X-Next-Page");
			for(Header head :headers) {
				ret[1] = head.getValue();
				break;
			}
		}
		return ret;
	}


	/**
	 * GETリクエストを送り、レスポンスの文字列を返す
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String sendGetRequest(String url) throws Exception {
		String ret = "";

		HttpClient httpclient = getHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse res = httpclient.execute(get);
		int status = res.getStatusLine().getStatusCode();
		if (status == HttpStatus.SC_OK){
			ret =EntityUtils.toString(res.getEntity(),charset);
		}
		return ret;
	}


	/**
	 * 通信用のHttpClientを作成し、返却する
	 * @return
	 * @throws Exception
	 */
	private HttpClient getHttpClient() throws Exception {

		HttpHost proxy = new HttpHost(HOST, PORT);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
				new AuthScope(proxy),
				new UsernamePasswordCredentials(USER, PASSWORD));
		RequestConfig config = RequestConfig.custom()
//				.setProxy(proxy)                   //プロキシ使う場合はコメント外す
				.build();
		HttpClient httpclient = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider)
				.setDefaultRequestConfig(config)
				.build();

		return httpclient;

	}
}
