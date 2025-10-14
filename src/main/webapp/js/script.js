//*************ハンバーガーメニュー********************/

// ハンバーガーアイコンとメニュー本体の要素を取得
const hamburgerIcon = document.querySelector('.hamburger-icon');
const navLinks = document.querySelector('.nav-links');
const closeIcon = document.querySelector('.close-icon');

// アイコンをクリックしたときの処理を設定
hamburgerIcon.addEventListener('click', () => {
    // ハンバーガーアイコンに"active"クラスを付けたり外したりする
    hamburgerIcon.classList.toggle('active');
    // メニュー本体にも"active"クラスを付けたり外したりする
    navLinks.classList.toggle('active');
});

// 閉じるアイコンをクリックしたときの処理を設定
closeIcon.addEventListener('click', (e) => {
    e.preventDefault(); 
    // ハンバーガーアイコンの"active"クラスを外す
    hamburgerIcon.classList.remove('active');
    // メニュー本体の"active"クラスを外す
    navLinks.classList.remove('active');
});