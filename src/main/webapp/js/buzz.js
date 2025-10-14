/*
* fetch：サーバーにリクエストを送信し、応答を受け取るためのAPI
* then : 通信が成功した後の結果を処理するメソッド
* catch : サーバーとの通信などが失敗した場合のエラー処理を行う
* const : 一度値を代入したら、再代入できない変数を宣言するためのキーワード
* credentials : リクエストに関連するセッションを含めるようにブラウザに指示する
*/


document.addEventListener('DOMContentLoaded', function () {
   // CSSの『.buzz-form』の<form>要素を取得
	const buzzForms = document.querySelectorAll('.buzz-form');

	// 取得した<form>要素に対してループ処理を実施
  buzzForms.forEach(function (form) {
    
	 // <form>の「submit」イベントを監視する
	 form.addEventListener('submit', function (e) {
      e.preventDefault(); // HTTPリクエストとページ遷移を中断する

	  // フォーム内から「name="postId"」の値を取得する
      const postId = form.querySelector('input[name="postId"]').value;
	  // 「key=value&...」形式のURLを扱うためオブジェクトを生成
	  const params = new URLSearchParams();
	  // ↑で生成したオブジェクトに、「postId」で、取得した値をセット
	  params.append('postId', postId);

	  const button = form.querySelector('.buzz-button'); // button要素を取得
	  const countSpan = form.querySelector('.buzz-count'); // countSpan要素を取得
	  // 現在が「バズる🔥」状態（buzzedクラスがない）である場合のみ処理
	  if (!button.classList.contains('buzzed')) {
	  // アニメーションをリセットするため、一時的にbuzzedクラスを削除
	  // 強制的にリフロー（再描画）させ、再度buzzedクラスを付ける
	  button.classList.remove('buzzed'); 
	  void button.offsetWidth; // 強制リフロー (ブラウザに再描画を促す)
	  }
	  
	  // サーバーへのHTTPリクエストを開始
      fetch('BuzzServlet', {
		// HTTPメソッドとして「POST」を指定
        method: 'POST',
		// URLSearchParamsオブジェクトをセットする
        body: params,
		// リクエストに、関連するセッションを含めるようにブラウザに指示する
		credentials: 'include',
		// サーバーに、リクエストの形式が「application/x-www-form-urlencoded」であることを伝える
		headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
      })
	  // リクエストが成功し、サーバー応答が返って実行される
      .then(response => {
		// レスポンスのHTTPステータスコードが200-299の範囲外かを確認する
        if (!response.ok){
			// 範囲外なら、エラーを投げて次の.catch()ブロックに移動する
          throw new Error('通信失敗' + response.status);
        }
		// レスポンス本文をJSON形式として解析し、次のPromiseに渡す
		return response.json();
      })
	  // 前のPromiseでJSON解析に成功した、オブジェクト（data）を受け取って実行される
      .then(data => {
	  // オブジェクトのbuzzCountプロパティをHTML要素のテキストに変換
		countSpan.textContent = data.buzzCount;
		
		// オブジェクトのlikedプロパティがtrueか確認する
		if (data.liked) {
			button.classList.add('buzzed');
			button.textContent = 'バズ済み✔️';
		} else {
			button.classList.remove('buzzed');
			button.textContent = 'バズる🔥';
		}
	})
	
	// Promiseチェーンでエラーが投げられた場合に実行
      .catch(error => {
        console.error('バズエラー:', error);
        alert('バズに失敗しました');
      });
    });
  });
});