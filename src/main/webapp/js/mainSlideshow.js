let slideIndex = 0; // 現在表示中のスライド番号
let slides; // 全ての画像要素を格納する変数
let dots;   // 全てのドットインジケーター要素を格納する変数
let autoPlayInterval; // 自動再生のインターバルIDを保持する変数

document.addEventListener('DOMContentLoaded', () => {
    // DOM読み込み完了後に実行
    // スライド画像要素とドット要素を取得
    slides = document.querySelectorAll('.hero-background-slideshow .slideshow-container .slides img');
    dots = document.querySelectorAll('.slideshow-container .dot'); 

    console.log('--- DOMContentLoaded START ---'); // 開発用ログ: 開始
    console.log('Selected slides:', slides); // 開発用ログ: 取得したスライド要素
    console.log('Number of slides found:', slides.length); // 開発用ログ: スライド枚数
    console.log('Selected dots:', dots); // 開発用ログ: 取得したドット要素
    console.log('Number of dots found:', dots.length); // 開発用ログ: ドット数

    if (slides.length > 0) {
        // スライドが存在すれば初期表示と自動再生を開始
        showSlides(slideIndex); // 最初のスライドを表示
        startAutoPlay(); // 自動再生を開始
        console.log('Slideshow initialized and auto-play started.'); // 開発用ログ: 初期化完了
    } else {
        console.log('No slides found or slides.length is 0. Slideshow not initialized.'); // 開発用ログ: スライドなし
    }
    console.log('--- DOMContentLoaded END ---'); // 開発用ログ: 終了
});

function showSlides(n) {
    // 指定された番号のスライドを表示する関数
    if (!slides || slides.length === 0) {
        console.log('showSlides: No slides to show or slides array is empty.'); // 開発用ログ: スライドなし
        return;
    }

    // スライドインデックスを調整（ループ処理）
    if (n >= slides.length) {
        slideIndex = 0; // 最後まで行ったら最初に戻る
    } else if (n < 0) {
        slideIndex = slides.length - 1; // 最初より前なら最後に行く
    } else {
        slideIndex = n; // 指定されたスライド番号に設定
    }

    const slidesContainer = document.querySelector('.hero-background-slideshow .slideshow-container .slides');
    if (slidesContainer) {
        // スライドコンテナを横に移動させて、該当スライドを表示
        const transformValue = -slideIndex * (100 / slides.length); // 移動距離を計算
        slidesContainer.style.transform = `translateX(${transformValue}%)`; // CSSでスライドを移動
        console.log(`showSlides: Setting transform to translateX(${transformValue}%) for slideIndex ${slideIndex}`); // 開発用ログ: スライド移動
        console.log('Current slidesContainer:', slidesContainer); // 開発用ログ: 現在のコンテナ
    } else {
        console.log('showSlides: slidesContainer not found.'); // 開発用ログ: コンテナなし
    }

    // ドットインジケーターの表示を更新
    if (dots && dots.length > 0) {
        // 全てのドットから 'active' クラスを削除
        for (let i = 0; i < dots.length; i++) {
            dots[i].className = dots[i].className.replace(" active", "");
        }
        // 現在のスライドに対応するドットに 'active' クラスを追加
        if (dots[slideIndex]) { 
            dots[slideIndex].className += " active"; // activeクラスを追加
            console.log(`Dot ${slideIndex} set to active.`); // 開発用ログ: ドットのアクティブ化
        }
    } else {
        console.log('showSlides: Dots elements not found or array is empty.'); // 開発用ログ: ドットなし
    }
}

function plusSlides(n) {
    // 次へ/前へボタンがクリックされた時の処理
    stopAutoPlay(); // 自動再生を一時停止
    showSlides(slideIndex + n); // 次または前のスライドへ移動
    startAutoPlay(); // 自動再生を再開
}

function currentSlide(n) {
    // ドットがクリックされた時の処理
    stopAutoPlay(); // 自動再生を一時停止
    showSlides(n); // クリックされたドットに対応するスライドへ移動
    startAutoPlay(); // 自動再生を再開
}

function startAutoPlay() {
    // 自動再生を開始する関数
    stopAutoPlay(); // 既存のインターバルをクリア（重複防止）
    autoPlayInterval = setInterval(() => {
        showSlides(slideIndex + 1); // 3秒ごとに次のスライドへ移動
    }, 3000); 
    console.log('AutoPlay started. Interval ID:', autoPlayInterval); // 開発用ログ: 自動再生開始
}

function stopAutoPlay() {
    // 自動再生を停止する関数
    clearInterval(autoPlayInterval); // インターバルをクリア
    console.log('AutoPlay stopped. Interval ID:', autoPlayInterval); // 開発用ログ: 自動再生停止
}

const heroSection = document.querySelector('.hero-section');
if (heroSection) {
    // ヒーローセクションへのマウスイベントを監視（自動再生の一時停止/再開）
    heroSection.addEventListener('mouseenter', () => {
        stopAutoPlay(); // マウスが乗ったら自動再生停止
        console.log('Mouse entered heroSection. AutoPlay paused.'); // 開発用ログ: 自動再生一時停止
    });
    heroSection.addEventListener('mouseleave', () => {
        startAutoPlay(); // マウスが離れたら自動再生再開
        console.log('Mouse left heroSection. AutoPlay resumed.'); // 開発用ログ: 自動再生再開
    });
} else {
    console.log('heroSection not found. Mouse events not attached.'); // 開発用ログ: ヒーローセクションなし
}