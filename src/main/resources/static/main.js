const RELEASE_VERSION = "v1"

/**
* Ajax通信用のメソッド
* @param method : GET, POST
* @param url : リクエスト先のURL
* @param request : requestのjson
* @param successFunc : リクエスト成功時に起動するfunction
* @returns
*/
function sendAjaxRequest2(method, url, request, successFunc, failFunc){

    //ajaxでservletにリクエストを送信
    $.ajax({
       type    : method,   //GET / POST
       url     : url,      //送信先のServlet URL（適当に変えて下さい）
       data    : request,  //リクエストJSON
       async   : true      //true:非同期(デフォルト), false:同期
    })
    // 通信成功時
    .done( function(data) {
      console.log(url);
      successFunc(data)
    })
    // 通信失敗時
		 .fail( function(data) {
      if(failFunc != undefined){
        failFunc(data)
      } else {
        alert("リクエスト時になんらかのエラーが発生しました：");
      }
		 });
}


/***********************************************************
* 情報取得処理
*************************************************************/

/**
* ISSUEを取得する
*/
function getIssues(){

	var startDateStr = document.getElementById("start-date").value;
	var endDateStr   = document.getElementById("end-date").value;

	var today = getDateStr(0)

	//終了日は、今日以前の日付でない場合
	if(dateDiff(today, endDateStr) > 0){
		alert("終了日は今日以前の日付にしてください")
		return false;
	}

	//開始日が終了日よりも前ではない場合
	if(dateDiff(startDateStr, endDateStr) < 1){
		alert("開始日は終了日よりも前の日付にしてください")
		return false;
	}

    var method = "GET";
    var successFunc = dataSet;
    var url = "http://localhost:8080/api";
    var request = "startDateStr=" + startDateStr + "&endDateStr=" + endDateStr;
    sendAjaxRequest2(method, url, request, successFunc)
}


/*
* データを配列にセットし、グラフを表示する
*/
function dataSet(data){

	console.log(data)

	var dateList = [];
	var openedList = [];
	var todoList = [];
	var doingList = [];
	var doneList = [];
	var closedList = [];

	for(var i in data){
		var d = data[i];
		dateList.push(d.date)
		openedList.push(d.opened)
		todoList.push(d.todo)
		doingList.push(d.doing)
		doneList.push(d.done)
		closedList.push(d.closed)

	}


	//グラフ作成処理（バーンアップ）
	var ctx = document.getElementById("chart");
	var myLineChart = new Chart(ctx, {
	  type: 'line',
	  data: {
	    labels: dateList,
	    datasets: [
	      {
	        label: 'closed',
	        data: closedList,
	        borderColor: "rgba(254,55,148,1)",
	        backgroundColor: "rgba(254,55,148,0.5)",
	        lineTension:0, //ベジェ曲線の張り具合。 0（ゼロ）を指定すると直線になる
			//fill: '1'
	      },
	      {
	        label: 'done',
	        data: doneList,
	        borderColor: "rgba(133,73,186,1)",
	        backgroundColor: "rgba(133,73,186,0.5)",
	        lineTension:0, //ベジェ曲線の張り具合。 0（ゼロ）を指定すると直線になる
			fill: '-1'
	      },

	      {
	        label: 'doing',
	        data: doingList,
	        borderColor: "rgba(0,169,80,1)",
	        backgroundColor: "rgba(0,169,80,0.5)",
	        lineTension:0, //ベジェ曲線の張り具合。 0（ゼロ）を指定すると直線になる
			fill: '-1'
	      },

	      {
	        label: 'todo',
	        data: todoList,
	        borderColor: "rgba(246,112,25,1)",
	        backgroundColor: "rgba(246,112,25,0.5)",
	        lineTension:0, //ベジェ曲線の張り具合。 0（ゼロ）を指定すると直線になる
			fill: '-1'
	      },

	      {
	        label: 'opened',
	        data: openedList,
	        borderColor: "rgba(77,201,246,1)",
	        backgroundColor: "rgba(77,201,246,0.5)",
	        lineTension:0, //ベジェ曲線の張り具合。 0（ゼロ）を指定すると直線になる
			fill: '-1'
	      }
	    ],
	  },
	  options: {

	    title: {
	      display: true,
	      text: '累積フロー図',
	      fontSize: 28
	    },
	    scales: {
	      yAxes: [{
              stacked: true,
              scaleLabel: {                 // 軸ラベル
                  display: true,                // 表示設定
                  labelString: 'ISSUEの数',    // ラベル
                  fontSize: 16                   // フォントサイズ
              }
	      }],
	      xAxes: [{
              scaleLabel: {                 // 軸ラベル
                  display: true,                // 表示設定
                  labelString: '日付',    // ラベル
                  fontSize: 16                   // フォントサイズ
              }
	      }]
	    },

		plugins: {
			filler: {
				propagate: true //他のグラフに重ならないように、下を埋めないように設定
			},
			'samples-filler-analyser': {
				target: 'chart-analyser'
			}
		}
	  }
	});
}



/***********************************************************
* 共通処理
*************************************************************/

/*
* 日付の初期値を設定
*/
function initDate(){

	document.getElementById("start-date").value = getDateStr(30);
	document.getElementById("end-date").value   = getDateStr(0);
}


/**
* 現在日時から「minasDate」を引いた日時をyyyy-MM-ddで表示する
*/
function getDateStr(minasDate){

  var now = new Date();
  now.setDate(now.getDate()- minasDate)
  var year = now.getFullYear()
  var month = now.getMonth() + 1
  var date = now.getDate()
  return str = year + "-" + ("00" + month).slice(-2) + "-" + ("00" + date).slice(-2);

}

/*
* 2つの日付の差分日数を取得する
*/
function dateDiff(dateStr1, dateStr2){

	var dateStr1Arr = dateStr1.split("-")
	var dateStr2Arr = dateStr2.split("-")

	var d1 = new Date(dateStr1Arr[0], dateStr1Arr[1]-1,dateStr1Arr[2])
	var d2 = new Date(dateStr2Arr[0], dateStr2Arr[1]-1,dateStr2Arr[2])
	return ((d2 - d1) / 86400000);

}

console.log("read completed")
console.log("RELEASE_VERSION:" + RELEASE_VERSION)
