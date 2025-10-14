console.log("delete.js is working");
document.addEventListener("DOMContentLoaded", function () {
  const deleteLinks = document.querySelectorAll(".delete-link");
  const modal = document.getElementById("confirmModal");
  const confirmBtn = document.getElementById("confirmDelete");
  const cancelBtn = document.getElementById("cancelDelete");

  let targetUrl = "";

  deleteLinks.forEach(link => {
    link.addEventListener("click", function (e) {
      e.preventDefault();
      targetUrl = link.getAttribute("data-url");
	  console.log("modal open triggered, target URL:", targetUrl);
      modal.classList.remove("hidden");
    });
  });

  confirmBtn.addEventListener("click", function () {
    window.location.href = targetUrl;
  });

  cancelBtn.addEventListener("click", function () {
    modal.classList.add("hidden");
  });
});
