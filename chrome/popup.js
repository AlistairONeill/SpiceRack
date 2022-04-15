const server = document.getElementById("server");
const changeServer = document.getElementById("changeServer");

function init() {
    changeServer.addEventListener("click", async () => {
        openChangeServerDialog();
    });
}

function reload() {
    chrome.storage.sync.get("url", ({ url }) => {
        server.textContent = url;
    });
}

function openChangeServerDialog() {
    const url = prompt("Please enter the server");
    if (url != null && url !== "") {
        chrome.storage.sync.set({ url });
        reload();
    }
}

init();
reload();