package com.example.demo;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.example.demo.templateCreate.SummaryDto;

public class GitlabSendRequest {

	private String HOST = GrobalConfig.HOST;
	private int PORT = GrobalConfig.PORT;
	private String USER = GrobalConfig.USER;
	private String PASSWORD = GrobalConfig.PASSWORD;
	private Charset charset = GrobalConfig.charset;
	private String GIT_URL = GrobalConfig.GIT_URL;
	private String TOKEN = GrobalConfig.TOKEN;
	private String GROUP_ID = GrobalConfig.GROUP_ID;
	private boolean PROXY_FLG = GrobalConfig.PROXY_FLG;

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
	 * ISSUEの統計情報を取得
	 * @return
	 * @throws Exception
	 */
	public String getAuthToken(String userName, String password) throws Exception {

		// ISSUE作成のPOSTリクエスト
		String strPostIssueUrl = "https://192.168.33.39/oauth/token";

		if(userName.length() == 0 || password.length() == 0) {
			System.out.println("リクエストパラメータエラー");
			return "400";
		}

		// リクエストを作成
		ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("grant_type", "password"));
		nvps.add(new BasicNameValuePair("username", userName));
		nvps.add(new BasicNameValuePair("password", password));

		System.out.println("リクエストURL:" + strPostIssueUrl);

		// ISSUE作成のリクエストを送信
		String ret = sendPostRequest(strPostIssueUrl, nvps);

		// returnCodeがエラー値違いであれば、tokenを抽出
		if(ret.length() != 3) {
			ret = new Util().returnTokenFromStr(ret);
		}

		return ret;
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
		RequestConfig config = null;

		if(PROXY_FLG) {
			//プロキシ使う場合
			config = RequestConfig.custom().setProxy(proxy).build();
		} else {
			config = RequestConfig.custom().build();
		}
		HttpClient httpclient = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider)
				.setDefaultRequestConfig(config)
				.build();

		return httpclient;

	}


	/**
	 * POSTリクエストを送り、レスポンスの文字列を返す
	 * @param url
	 * @param nvps
	 * @return
	 * @throws Exception
	 */
	private String sendPostRequest(String url, ArrayList<NameValuePair> nvps) throws Exception {

		String ret = "";
		HttpClient httpclient = getHttpClient();
		HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(nvps, charset));

		HttpResponse res = httpclient.execute(post);
		int status = res.getStatusLine().getStatusCode();
		System.out.println(res.getStatusLine());

		if (status == HttpStatus.SC_CREATED || status == HttpStatus.SC_OK ){
			ret =EntityUtils.toString(res.getEntity(),charset);
		} else {
			ret = Integer.toString(status);
		}
		return ret;
	}

}
