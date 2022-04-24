let url = "www.change.me";

const spiceNames = ["salt", "chilli flakes"]

const ID_ILLUMINATE = "spice_rack_illuminate"

chrome.runtime.onInstalled.addListener(() => {
    chrome.storage.sync.set( { url } );
    chrome.contextMenus.create({
        id: ID_ILLUMINATE,
        title: 'Illuminate',
        contexts: ["selection"]
    });
});

chrome.contextMenus.onClicked.addListener( info => {
    if (ID_ILLUMINATE === info.menuItemId) {
        const selectionText = info.selectionText.toUpperCase();

        const identified = spiceNames.filter(spiceName =>
            selectionText.includes(spiceName.toUpperCase())
        );

        identified.forEach(spiceName => console.log(spiceName));

        illuminate(identified);
    }
});

function getUrl() {
    let ret;
    chrome.storage.sync.get("url", ({ url }) => {
        ret = url;
    });
    return ret;
}

function illuminate(names) {
    fetch(
        getUrl() + "/api/illuminate",
        {
            method: "POST",
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(names)
        }
    ).then(result => {
        console.log(result);
    });
}