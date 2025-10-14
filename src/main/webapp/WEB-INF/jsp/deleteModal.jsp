<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0,0,0,0.5); /* 半透明の背景 */
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
}

.hidden {
  display: none;
}
</style>

</head>
<body>
<!-- jsp/modal/confirmDeleteModal.jsp -->
<div id="confirmModal" class="modal hidden">
  <div class="modal-content">
    <p>本当に削除しますか？</p>
    <button id="confirmDelete">はい</button>
    <button id="cancelDelete">キャンセル</button>
  </div>
</div>
</body>
</html>