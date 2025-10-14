//パスワードを表示させる
document.addEventListener("DOMContentLoaded", function () {
    const toggle = document.getElementById("togglePassword");
    const pass = document.getElementById("pass");
    const confirm = document.getElementById("confirmPass");

    toggle.addEventListener("change", function () {
        const type = this.checked ? "text" : "password";
        pass.type = type;
        confirm.type = type;
    });
});
